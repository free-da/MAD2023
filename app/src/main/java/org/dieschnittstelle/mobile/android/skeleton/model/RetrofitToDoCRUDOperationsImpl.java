package org.dieschnittstelle.mobile.android.skeleton.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
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
            return this.toDoResource.create(item).execute().body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ToDo> readAllToDos() {
        try {
            Thread.sleep(2500);
        } catch (Exception e) {

        }
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
            this.toDoResource.update(item.getId(),item);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteToDo(long id) {
        return false;
    }
}
