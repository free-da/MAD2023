package org.dieschnittstelle.mobile.android.skeleton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OverviewActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> listData = new ArrayList<>(Arrays.asList("Test", "Item", "noch ein", "Paar"));
    private ArrayAdapter<String> listViewAdapter;

    private FloatingActionButton fab;

    private ActivityResultLauncher<Intent> callDetailViewLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == DetailviewActivity.ITEM_CREATED) {
                        String item = result.getData().getStringExtra("item");
                        onNewItemCreated(item);
                    } else if (result.getResultCode() == DetailviewActivity.ITEM_EDITED){
                        String item = result.getData().getStringExtra("item");
                        onItemEdited(item);
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        showMessage("edit/create cancelled!");
                    }

                    else {
                        showMessage("got result code for created/edited: " + result.getResultCode());
                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1) Auswahl der darzustellenden Ansicht
        setContentView(R.layout.activity_overview);
        this.listView = findViewById(R.id.listView);
        this.fab = findViewById(R.id.fab);

        this.fab.setOnClickListener(view -> {
            onCreateNewItem();
        });
//                listData.forEach(listitem -> {
//                    TextView itemView = (TextView) getLayoutInflater().inflate(R.layout.activity_overview_listitem, null);
//                    itemView.setText(listitem);
//                    itemView.setOnClickListener(view -> {
//                        onListitemSelected(String.valueOf(((TextView)view).getText()));
//                    });
//                    this.listView.addView(itemView);
//                });
        this.listViewAdapter = new ArrayAdapter<>(this, R.layout.activity_overview_listitem, listData);
        this.listView.setAdapter(this.listViewAdapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    public void onListitemSelected(String listitem) {
        Intent callDetailViewIntent = new Intent(this, DetailviewActivity.class);
        callDetailViewIntent.putExtra("item",listitem);
        callDetailViewLauncher.launch(callDetailViewIntent);
    }

    public void onCreateNewItem() {
        callDetailViewLauncher.launch(new Intent(this,DetailviewActivity.class));
    }

    public void onNewItemCreated(String item) {
        showMessage(("created: " + item));
    }

    public void onItemEdited(String item) {
        showMessage(("edited: " + item));
    }

    public void showMessage(String msg) {
        Snackbar.make(findViewById(R.id.rootView),msg, Snackbar.LENGTH_SHORT).show();
    }
}
