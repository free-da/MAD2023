package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

public class DetailviewViewmodelImpl extends ViewModel implements IDetailviewViewmodel {

    private ToDo item;
    private MutableLiveData<Boolean> savedOccurred = new MutableLiveData<>();

    @Override
    public ToDo getItem() {
        return item;
    }

    public MutableLiveData<Boolean> getSavedOccurred() {
        return savedOccurred;
    }
    @Override
    public void onItemSaved() {
        savedOccurred.setValue(true);
    }

    public void setItem(ToDo item) {
        this.item = item;
    }

}
