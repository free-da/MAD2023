package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import androidx.lifecycle.MutableLiveData;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

public interface IDetailviewViewmodel {

    public ToDo getItem();
    public void onItemSaved();

    public boolean checkFieldInputCompleted(int actionId);
    public boolean onNameFieldInputChanged();
    public MutableLiveData<String> getErrorStatus();

}
