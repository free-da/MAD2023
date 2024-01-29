package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import org.dieschnittstelle.mobile.android.skeleton.model.IToDoCRUDOperations;
import org.dieschnittstelle.mobile.android.skeleton.model.RoomToDoCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.SyncedToDoCRUDOperationsImpl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class ToDoApplication extends Application {
    private IToDoCRUDOperations crudOperations;
    private boolean offlineMode;


    public void onCreate() {
        super.onCreate();
        try {
            if (checkConnectivity().get()) {
                this.crudOperations = new SyncedToDoCRUDOperationsImpl(this);
            } else {
                this.crudOperations = new RoomToDoCRUDOperationsImpl(this);
                this.offlineMode = true;
            }
            Toast.makeText(this, "Using CRUD Impl: " + crudOperations.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            this.crudOperations = new RoomToDoCRUDOperationsImpl(this);
            this.offlineMode = true;
            Toast.makeText(this,"Some unexpected error occurred. Using CRUD Impl: " + crudOperations.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        }
    }
    public IToDoCRUDOperations getCRUDOperations() {
            return crudOperations;
        }

    public CompletableFuture<Boolean> checkConnectivity() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        new Thread(() -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL("http://192.168.2.225:8080/api/todos").openConnection();
                conn.setConnectTimeout(500);
                conn.setReadTimeout(500);
                conn.setDoInput(true);
                conn.connect();
                conn.getInputStream();
                future.complete(true);
            } catch (Exception e) {
                future.complete(false);
            }
        }).start();
        return future;

    }

    public boolean isOfflineMode() {
        return offlineMode;
    }
}



