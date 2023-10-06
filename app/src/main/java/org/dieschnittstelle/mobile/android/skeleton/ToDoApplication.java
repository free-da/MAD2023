package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Application;
import android.widget.Toast;

import org.dieschnittstelle.mobile.android.skeleton.model.IToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.RetrofitToDoCRUDOperationsImpl;

public class ToDoApplication extends Application {

    public IToDoCRUDOperations getCRUDOperations() {
        IToDoCRUDOperations crudOperations = new RetrofitToDoCRUDOperationsImpl();
        Toast.makeText(this, "Using CRUD Impl: " + crudOperations.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();
        return crudOperations;
    }
}
