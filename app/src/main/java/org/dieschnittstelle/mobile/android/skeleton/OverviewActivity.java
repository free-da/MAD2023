package org.dieschnittstelle.mobile.android.skeleton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityOverviewListitemBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.IToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.RetrofitToDoCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.RoomToDoCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.SimpleToDoCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.SyncedToDoCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;
import org.dieschnittstelle.mobile.android.skeleton.model.User;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.OverviewViewmodelImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OverviewActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<ToDo> listViewAdapter;

    private OverviewViewmodelImpl overviewViewmodel;

    private FloatingActionButton fab;

    private ProgressBar progressBar;
    private IToDoCRUDOperations crudOperations;

    private MADAsyncOperationRunner operationRunner;

    private ActivityResultLauncher<Intent> callDetailViewLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == DetailviewActivity.ITEM_CREATED) {
                        ToDo item = (ToDo)result.getData().getSerializableExtra(DetailviewActivity.ARG_ITEM);
                        onNewItemReceived(item);
                    } else if (result.getResultCode() == DetailviewActivity.ITEM_EDITED){
                        ToDo item = (ToDo)result.getData().getSerializableExtra(DetailviewActivity.ARG_ITEM);
                        onEditedItemReceived(item);
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        showMessage("action cancelled!");
                    }

                    else {
                        showMessage("got result code for action: " + result.getResultCode());
                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_overview);

        this.crudOperations = ((ToDoApplication)getApplication()).getCRUDOperations();

        // obtain a view model, which may either be empty or contain items we have loaded before
        overviewViewmodel = new ViewModelProvider(this).get(OverviewViewmodelImpl.class);
        boolean initialViewmodel = false;
        if (overviewViewmodel.getItems() == null) {
            overviewViewmodel.setItems(new ArrayList<>());
            initialViewmodel = true;
        }

        this.listView = findViewById(R.id.listView);
        this.fab = findViewById(R.id.fab);
        this.progressBar = findViewById(R.id.progressBar);

        this.operationRunner = new MADAsyncOperationRunner(this,this.progressBar);

        this.fab.setOnClickListener(view -> {
            onCreateNewItem();
        });

        // prepare the list view
        this.listViewAdapter = new ArrayAdapter<>(this,R.layout.activity_overview_listitem,overviewViewmodel.getItems()) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View recyclableListItemView, @NonNull ViewGroup parent) {
                ActivityOverviewListitemBinding itemBinding = null;
                if (recyclableListItemView != null) {
                    itemBinding = (ActivityOverviewListitemBinding) recyclableListItemView.getTag();
                } else {
                    itemBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.activity_overview_listitem,null,false);
                    itemBinding.getRoot().setTag(itemBinding);
                }
                ToDo item = this.getItem(position);
                itemBinding.setItem(item);
                itemBinding.setController(OverviewActivity.this);
                return itemBinding.getRoot();
            }
        };
        this.listView.setAdapter(this.listViewAdapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positionOfSelectedItem, long l) {
                ToDo selectedItem = listViewAdapter.getItem(positionOfSelectedItem);
                onListitemSelected(selectedItem);
            }
        });

        if (initialViewmodel) {
            operationRunner.run(
                    // supplier (= the operation)
                    () -> crudOperations.readAllToDos(),
                    // consumer (= the reaction to the operation result)
                    items -> {
                        overviewViewmodel.getItems().addAll(items);
                        this.sortItems();
                    }
            );
        }
    }

    public void checkedChangedForListitem(ToDo item) {
        this.operationRunner.run(() -> crudOperations.updateToDo(item),
                changed -> {
                    showMessage("Checked changed for: " + item.getName());
                    this.sortItems();
                }
        );
    }

    public void onListitemSelected(ToDo listitem) {
        Intent callDetailViewIntent = new Intent(this, DetailviewActivity.class);
        callDetailViewIntent.putExtra(DetailviewActivity.ARG_ITEM,listitem);
        callDetailViewLauncher.launch(callDetailViewIntent);
    }

    public void onCreateNewItem() {
        callDetailViewLauncher.launch(new Intent(this,DetailviewActivity.class));
    }

    public void onNewItemReceived(ToDo item) {
        // 1. run the CRUD operation on a separate thread
        new Thread(() -> {
            ToDo createdItem = this.crudOperations.createToDo(item);
        // 2. update the view
            this.runOnUiThread(() -> {
                this.listViewAdapter.add(createdItem);
                this.sortItems();
            });
        }).start();

    }

    public void onEditedItemReceived(ToDo item) {
        this.operationRunner.run(
                () -> this.crudOperations.updateToDo(item),
                updated -> {
                    int positionOfItemInList = listViewAdapter.getPosition(item);
                    this.overviewViewmodel.getItems().remove(positionOfItemInList);
                    this.overviewViewmodel.getItems().add(item);
                    this.sortItems();
                });
    }

    public void showMessage(String msg) {
        Snackbar.make(findViewById(R.id.rootView),msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_overview_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sortItems) {
            this.sortItems();
            return true;
        } else if (item.getItemId() == R.id.deleteAll) {
            if (this.crudOperations instanceof SyncedToDoCRUDOperationsImpl) {
                this.operationRunner.run(() -> crudOperations.deleteAllTodos(),
                        deleted -> {
                            int numberOfDeletedItems = overviewViewmodel.getItems().size();
                            listViewAdapter.clear();
                            listViewAdapter.notifyDataSetChanged();
                            showMessage("Deleted " + numberOfDeletedItems + " Todos.");
                        }
                );
            } else {
                Toast.makeText(this,"deleteAllRemote is not available", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (item.getItemId() == R.id.runSync) {
            if (this.crudOperations instanceof SyncedToDoCRUDOperationsImpl) {
                    operationRunner.run(
                            // supplier (= the operation)
                            () -> crudOperations.readAllToDos(),
                            // consumer (= the reaction to the operation result)
                            items -> {
                                listViewAdapter.clear();
                                overviewViewmodel.getItems().addAll(items);
                                this.sortItems();
                                Toast.makeText(this,"Synced " + overviewViewmodel.getItems().size() + " items.", Toast.LENGTH_SHORT).show();
                            }
                    );

            } else {
                Toast.makeText(this,"RunSync is not available", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void sortItems() {
        this.overviewViewmodel.getItems().sort(overviewViewmodel.getCurrentSortMode());
        this.listViewAdapter.notifyDataSetChanged();

    }
}
