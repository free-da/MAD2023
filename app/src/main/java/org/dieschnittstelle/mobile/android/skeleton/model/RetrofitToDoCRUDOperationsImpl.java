package org.dieschnittstelle.mobile.android.skeleton.model;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class RetrofitDataItemCRUDOperationsImpl implements IToDoCRUDOperations {

    public static interface ToDoResource {

        @POST("/api/todos")
        public ToDo create(@Body ToDo item);

        @GET("/api/todos")
        public List<ToDo> readAll();

        @GET("/api/todos/{todoId}")
        public ToDo read(@Path("todoId") long id);

        @PUT("/api/todos/{todoId}")
        public ToDo update(@Path("todoId") long id, @Body ToDo item);

    }

    private ToDoResource toDoResource;

    public RetrofitDataItemCRUDOperationsImpl() {
        Retrofit webapiBase = new Retrofit.Builder()
        //        .baseUrl("http://10.0.2.2:8080")
                .baseUrl("http://192l168.2.116:38605")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public ToDo createToDo(ToDo item) {
        return null;
    }

    @Override
    public List<ToDo> readAllToDos() {
        return null;
    }

    @Override
    public ToDo readToDo(long id) {
        return null;
    }

    @Override
    public boolean updateToDo(ToDo item) {
        return false;
    }

    @Override
    public boolean deleteToDo(long id) {
        return false;
    }
}
