package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import android.content.Intent;
import android.util.Log;
import android.view.inputmethod.EditorInfo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

public class DetailviewViewmodelImpl extends ViewModel implements IDetailviewViewmodel {

    private MutableLiveData<String> errorStatus = new MutableLiveData<>();

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

    @Override
    public boolean checkFieldInputCompleted(int actionId) {
        Log.i(DetailviewViewmodelImpl.class.getSimpleName(),"checkFieldInputCompleted(): " + actionId);
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
            if (item.getName() == null || item.getName().length() < 5) {
                Log.i(DetailviewViewmodelImpl.class.getSimpleName(),"Length of name: " + (item.getName() == null ? "<null>" : item.getName().length()));
                errorStatus.setValue("Name too short!");
            }
        }
        return false;
    }

    @Override
    public boolean onNameFieldInputChanged() {
        if (errorStatus.getValue() != null && errorStatus.getValue().length() > 0) {
            errorStatus.setValue(null);
        }
        return false;
    }

    @Override
    public MutableLiveData<String> getErrorStatus() {
        return this.errorStatus;
    }

    public void setItem(ToDo item) {
        this.item = item;
    }

}
