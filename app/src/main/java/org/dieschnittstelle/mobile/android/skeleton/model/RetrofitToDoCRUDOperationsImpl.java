package org.dieschnittstelle.mobile.android.skeleton.model;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class RetrofitToDoCRUDOperationsImpl implements IToDoCRUDOperations {

    public static interface ToDoResource {

        @POST("/api/todos")
        public Call<ToDo> create(@Body ToDo item);

        @GET("/api/todos")
        public Call<List<ToDo>> readAll();

        @GET("/api/todos/{todoId}")
        public Call<ToDo> read(@Path("todoId") long id);

        @PUT("/api/todos/{todoId}")
        public Call<ToDo> update(@Path("todoId") long id, @Body ToDo item);

        @PUT("/api/users/auth")
        public Call<Boolean> authenticateUser(@Body User user);

        @DELETE("/api/todos")
        public Call<Boolean> deleteAll();

    }

    private ToDoResource toDoResource;

    public RetrofitToDoCRUDOperationsImpl() {
        Retrofit webapiBase = new Retrofit.Builder()
                .baseUrl("http://192.168.2.225:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.toDoResource = webapiBase.create(ToDoResource.class);
    }

    @Override
    public ToDo createToDo(ToDo item) {
        try {
            Log.i(RetrofitToDoCRUDOperationsImpl.class.getSimpleName(), "item created: " + item.toString());
            return this.toDoResource.create(item).execute().body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ToDo> readAllToDos() {
        try {
            return this.toDoResource.readAll().execute().body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ToDo readToDo(long id) {
        try {
            return this.toDoResource.read(id).execute().body();
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateToDo(ToDo item) {
        try {
            Log.i(RetrofitToDoCRUDOperationsImpl.class.getSimpleName(), "item updated: " + item.toString());
            this.toDoResource.update(item.getId(),item).execute().body();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteToDo(long id) {
        return false;
    }

    @Override
    public boolean authenticateUser(User user) {
        try {
            Log.i(RetrofitToDoCRUDOperationsImpl.class.getSimpleName(),"Authenticate user: " + user.getEmail() + ", password: " + user.getPwd());

            boolean authenticationResult = this.toDoResource.authenticateUser(user).execute().body();
            Log.i(RetrofitToDoCRUDOperationsImpl.class.getSimpleName(),"Authenticate result: " + authenticationResult);
            return authenticationResult;
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteAllTodos() {
        try {
            boolean deleteResult = Boolean.TRUE.equals(this.toDoResource.deleteAll().execute().body());
            Log.i(RetrofitToDoCRUDOperationsImpl.class.getSimpleName(),"Delete result: " + deleteResult);
            return deleteResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
