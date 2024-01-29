package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import androidx.lifecycle.ViewModel;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

import java.util.Comparator;
import java.util.List;

public class OverviewViewmodelImpl extends ViewModel implements IOverviewViewmodel {

    private static final Comparator<ToDo> SORT_BY_CHECKED_AND_NAME = Comparator.comparing(ToDo::isDone).thenComparing(ToDo::getName);

    private Comparator<ToDo> currentSortMode = SORT_BY_CHECKED_AND_NAME;

    private List<ToDo> items;

    public void setItems(List<ToDo> items) {
        this.items = items;
    }
    @Override
    public List<ToDo> getItems() {
        return this.items;
    }

    @Override
    public Comparator<ToDo> getCurrentSortMode() {
        return this.currentSortMode;
    }

    @Override
    public void switchSortMode() {
        //TODO: implement switching
    }

}
