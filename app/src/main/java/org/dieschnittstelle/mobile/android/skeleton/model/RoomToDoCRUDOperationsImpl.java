package org.dieschnittstelle.mobile.android.skeleton.model;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import java.util.List;

public class RoomToDoCRUDOperationsImpl implements IToDoCRUDOperations{

    @Dao
    public static interface ToDoDao {

        @Query("select * from todo")
        public List<ToDo> readAll();

        @Query("select * from todo where id == (:id)")
        public ToDo readById(long id);

        @Insert
        public long create(ToDo item);

        @Update
        public void update(ToDo item);

        @Delete
        public void delete(ToDo item);

        @Query("delete from todo")
        public void deleteAllTodos();
    }

    @Database(entities = {ToDo.class}, version = 1)
    public static abstract class ToDoDatabase extends RoomDatabase {
        public abstract ToDoDao getDao();
    }

    private ToDoDatabase db;
    public RoomToDoCRUDOperationsImpl(Context context) {
        db = Room.databaseBuilder(context,ToDoDatabase.class,"todo.db").build();
    }
    @Override
    public ToDo createToDo(ToDo item) {
        long id = db.getDao().create(item);
        item.setId(id);
        return item;
    }

    @Override
    public List<ToDo> readAllToDos() {
        return db.getDao().readAll();
    }

    @Override
    public ToDo readToDo(long id) {
        return db.getDao().readById(id);
    }

    @Override
    public boolean updateToDo(ToDo item) {
        db.getDao().update(item);
        return true;
    }

    @Override
    public boolean deleteToDo(long id) {
        db.getDao().delete(readToDo(id));
        return true;
    }

    @Override
    public boolean authenticateUser(User user) {
        return false;
    }

    @Override
    public boolean deleteAllTodos() {
        db.getDao().deleteAllTodos();
        return true;
    }
}
