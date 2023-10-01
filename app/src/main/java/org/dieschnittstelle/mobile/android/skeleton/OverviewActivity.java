package org.dieschnittstelle.mobile.android.skeleton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.dieschnittstelle.mobile.android.skeleton.model.IToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.SimpleToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

import java.util.ArrayList;
import java.util.List;

public class OverviewActivity extends AppCompatActivity {

    private ListView listView;
    private List<ToDo> listData = new ArrayList<>();
    private ArrayAdapter<ToDo> listViewAdapter;

    private FloatingActionButton fab;

    private IToDoCRUDOperations crudOperations;

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

        // 1) Auswahl der darzustellenden Ansicht
        setContentView(R.layout.activity_overview);

        this.crudOperations = new SimpleToDoCRUDOperations();

        this.listView = findViewById(R.id.listView);
        this.fab = findViewById(R.id.fab);

        this.fab.setOnClickListener(view -> {
            onCreateNewItem();
        });

        // prepare the list view
        this.listViewAdapter = new ArrayAdapter<>(this, R.layout.activity_overview_listitem_simple, listData) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //    return super.getView(position, convertView, parent);
                ViewGroup itemView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_overview_listitem,null);
                TextView itemNameView = itemView.findViewById(R.id.itemName);
                ToDo item = this.getItem(position);
                itemNameView.setText(item.getName());
                return itemView;
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

        // initialize the list
        listViewAdapter.addAll(crudOperations.readAllToDos());
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
        // run the CRUD operation
        item = this.crudOperations.createToDo(item);
        // update the view
        this.listViewAdapter.add(item);
    }

    public void onEditedItemReceived(ToDo item) {
        Log.i(OverviewActivity.class.getSimpleName(),"item id: " + item.getId() + ", " + item.getName());
        int positionOfItemInList = listViewAdapter.getPosition(item);
        ToDo itemInList = listViewAdapter.getItem(positionOfItemInList);
        itemInList.setName(item.getName());
        itemInList.setDescription(item.getDescription());
        itemInList.setDone(item.isDone());
        //showMessage(("edited: " + item.getName()));
        listViewAdapter.notifyDataSetChanged();
    }

    public void showMessage(String msg) {
        Snackbar.make(findViewById(R.id.rootView),msg, Snackbar.LENGTH_SHORT).show();
    }
}
