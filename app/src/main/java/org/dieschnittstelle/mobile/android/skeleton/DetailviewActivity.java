package org.dieschnittstelle.mobile.android.skeleton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.DetailviewViewmodelImpl;

public class DetailviewActivity extends AppCompatActivity {

    public static final String ARG_ITEM = "item";
    public static final int ITEM_CREATED = 1;
    public static final int ITEM_EDITED = 2;
    private ActivityDetailviewBinding binding;
    private DetailviewViewmodelImpl viewmodel;
    public DetailviewActivity() {
        Log.i(DetailviewActivity.class.getSimpleName(),"constructor invoked");
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(DetailviewActivity.class.getSimpleName(),"oncreate invoked");

        // A) determine the view
        this.binding = DataBindingUtil.setContentView(this,R.layout.activity_detailview);
        this.binding.setLifecycleOwner(this);

        // B) instantiate or reuse the viewmodel
        this.viewmodel = new ViewModelProvider(this).get(DetailviewViewmodelImpl.class);

        // Has viewmodel been created the first time? then data needs to be set
        if (this.viewmodel.getItem() == null) {
            Log.i(DetailviewActivity.class.getSimpleName(),"viemodel has been created. No item has been set so far.");
            // B) determine the data
            ToDo item = (ToDo)getIntent().getSerializableExtra(ARG_ITEM);
            if (item == null) {
                this.viewmodel.setItem(new ToDo());
            } else {
                this.viewmodel.setItem(item);
            }
        } else {
            Log.i(DetailviewActivity.class.getSimpleName(),"use item from viewmodel: " + this.viewmodel.getItem());
        }
        this.viewmodel.getSavedOccurred().observe(this, occurred -> {
            onItemSaved();
        });

        // C) pass the data to the view (it will care itself how to deal with them)
        this.binding.setViewmodel(this.viewmodel);
        Log.i(DetailviewActivity.class.getSimpleName(),"errorStatus ( setting viewmodel): " + viewmodel.getErrorStatus().getValue());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(DetailviewActivity.class.getSimpleName(),"onPause invoked");
    }

    public void onItemSaved() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ARG_ITEM,this.viewmodel.getItem());
        setResult(this.viewmodel.getItem().getId() == 0L ? ITEM_CREATED : ITEM_EDITED,returnIntent);
        finish();
    }

}
