package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

import java.util.List;

public class OverviewViewmodelImpl extends ViewModel implements IOverviewViewmodel {

    private List<ToDo> items;

    public void setItems(List<ToDo> items) {
        this.items = items;
    }
    @Override
    public List<ToDo> getItems() {
        return null;
    }
}
