package org.dieschnittstelle.mobile.android.skeleton.viewmodel;

import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

import java.util.Comparator;
import java.util.List;

public interface IOverviewViewmodel {

    public List<ToDo> getItems();

    public Comparator<ToDo> getCurrentSortMode();

    public void switchSortMode();

}
