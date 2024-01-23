package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

public class DetailviewViewmodelImpl extends ViewModel implements IDetailviewViewmodel {

    private ToDo item;

    @Override
    public ToDo getItem() {
        return item;
    }

    @Override
    public void onItemSaved() {

    }

    public void setItem(ToDo item) {
        this.item = item;
    }

}
