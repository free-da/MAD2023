package org.dieschnittstelle.mobile.android.skeleton.model;

import android.content.Context;

import java.util.List;

public class SyncedToDoCRUDOperationsImpl implements IToDoCRUDOperations {

    private RetrofitToDoCRUDOperationsImpl remoteCRUD;
    private RoomToDoCRUDOperationsImpl localCRUD;

    public SyncedToDoCRUDOperationsImpl(Context context) {
        this.localCRUD = new RoomToDoCRUDOperationsImpl(context);
        this.remoteCRUD = new RetrofitToDoCRUDOperationsImpl();
    }
    @Override
    public ToDo createToDo(ToDo item) {
        item = localCRUD.createToDo(item);
        remoteCRUD.createToDo(item);
        return item;
    }

    @Override
    public List<ToDo> readAllToDos() {
        return localCRUD.readAllToDos();
    }

    @Override
    public ToDo readToDo(long id) {
        return localCRUD.readToDo(id);
    }

    @Override
    public boolean updateToDo(ToDo item) {
        if (localCRUD.updateToDo(item)){
            if(remoteCRUD.updateToDo(item)){
                return true;
            } else {
                throw new RuntimeException("remote update failed!");
            }
        } else {
            throw new RuntimeException("local update failed!");
        }
    }

    @Override
    public boolean deleteToDo(long id) {
        return false;
    }
}
