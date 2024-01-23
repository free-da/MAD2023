package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

public class DetailviewActivity extends AppCompatActivity {

    public static final String ARG_ITEM = "item";
    public static final int ITEM_CREATED = 1;
    public static final int ITEM_EDITED = 2;

    private TextView itemNameView;
    private TextView itemDescriptionView;

    private FloatingActionButton fab;

    private ToDo item;

    private ActivityDetailviewBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // A) determine the view
        this.binding = DataBindingUtil.setContentView(this,R.layout.activity_detailview);

        // B) prepare initialising the view by reading out its elements
        itemNameView = findViewById(R.id.itemName);
        itemDescriptionView = findViewById(R.id.itemDescription);
        fab = findViewById(R.id.fab);

        // C) bind data to the view element
        this.item = (ToDo)getIntent().getSerializableExtra(ARG_ITEM);
        // C.1) distinguish between the two cases for using this activity: create and edit
        if (item != null) {
//            itemNameView.setText(item.getName());
//            itemDescriptionView.setText(item.getDescription());
        } else {
            this.item = new ToDo();
        }

        this.binding.setController(this);
        // D) prepare the view for user interaction
        fab.setOnClickListener(view -> {
            onItemSaved();
        });
    }

    protected void onItemSaved() {
        String itemName = itemNameView.getText().toString();
        String itemDescription = itemDescriptionView.getText().toString();
        this.item.setName(itemName);
        this.item.setDescription(itemDescription);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ARG_ITEM,this.item);
        setResult(this.item.getId() == 0L ? ITEM_CREATED : ITEM_EDITED,returnIntent);
        finish();
    }

    public ToDo getItem() {
        return item;
    }
}
