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

    private ToDo item;

    private ActivityDetailviewBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // A) determine the view
        this.binding = DataBindingUtil.setContentView(this,R.layout.activity_detailview);

        // B) determine the data
        this.item = (ToDo)getIntent().getSerializableExtra(ARG_ITEM);
        if (item == null) {
            this.item = new ToDo();
        }

        // C) pass the data to the view (it will care itself how to deal with them)
        this.binding.setController(this);
    }

    public void onItemSaved() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ARG_ITEM,this.item);
        setResult(this.item.getId() == 0L ? ITEM_CREATED : ITEM_EDITED,returnIntent);
        finish();
    }

    public ToDo getItem() {
        return item;
    }
}
