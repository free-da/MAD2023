package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

public interface IDetailviewViewmodel {

    public ToDo getItem();
    public void onItemSaved();

}
