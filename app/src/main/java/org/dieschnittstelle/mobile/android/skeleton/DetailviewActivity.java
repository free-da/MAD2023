package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailviewActivity extends AppCompatActivity {

    public static final int ITEM_CREATED = 1;
    public static final int ITEM_EDITED = 2;

    private TextView itemNameView;
    private TextView itemDescriptionView;

    private FloatingActionButton fab;

    private boolean createMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // A) determine the view
        setContentView(R.layout.activity_detailview);

        // B) prepare initialising the view by reading out its elements
        itemNameView = findViewById(R.id.itemName);
        itemDescriptionView = findViewById(R.id.itemDescription);
        fab = findViewById(R.id.fab);

        // C) bind data to the view elementd
        String item = getIntent().getStringExtra("item");
        // C.1) distinguish between the two cases for using this activity: create and edit
        if (item != null) {
            itemNameView.setText(item);
        } else {
            this.createMode = true;
        }


        // D) prepare the view for user interaction
        fab.setOnClickListener(view -> {
            onItemSaved();
        });
    }

    protected void onItemSaved() {
        String item = itemNameView.getText().toString();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("item",item);
        setResult(createMode ? ITEM_CREATED : ITEM_EDITED,returnIntent);
        finish();
    }
}
