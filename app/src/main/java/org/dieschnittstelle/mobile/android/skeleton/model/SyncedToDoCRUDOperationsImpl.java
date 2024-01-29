package org.dieschnittstelle.mobile.android.skeleton.model;

import android.content.Context;
import android.util.Log;

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
        Log.i(SyncedToDoCRUDOperationsImpl.class.getSimpleName(),"created new item: " + item.getName());
        return item;
    }

    @Override
    public List<ToDo> readAllToDos() {
        List<ToDo> localTodos = localCRUD.readAllToDos();
        if (!localTodos.isEmpty()) {
            syncAllLocalToRemote();
        } else {
            syncAllRemoteToLocal();
        }
        return remoteCRUD.readAllToDos();
    }

    private void syncAllLocalToRemote() {
        remoteCRUD.deleteAllTodos();
        List<ToDo> localTodos = localCRUD.readAllToDos();
        for (ToDo todo : localTodos) {
            remoteCRUD.createToDo(todo);
        }
    }

    private void syncAllRemoteToLocal() {
        List<ToDo> remoteTodos = remoteCRUD.readAllToDos();
        for (ToDo todo : remoteTodos) {
            localCRUD.createToDo(todo);
        }
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

    @Override
    public boolean authenticateUser(User user) {
        return remoteCRUD.authenticateUser(user);
    }

    @Override
    public boolean deleteAllTodos() {
        deleteAllLocalTodos();
        deleteAllRemoteTodos();
        return true;
    }
    public boolean deleteAllLocalTodos() {
        return localCRUD.deleteAllTodos();
    }

    public boolean deleteAllRemoteTodos() {
        return remoteCRUD.deleteAllTodos();
    }
}
