package org.dieschnittstelle.mobile.android.skeleton.model;

import java.util.List;

public interface IToDoCRUDOperations {

    public ToDo createToDo(ToDo item);

    public List<ToDo> readAllToDos();

    public ToDo readToDo(long id);

    public boolean updateToDo(ToDo item);

    public boolean deleteToDo(long id);

    public boolean authenticateUser(User user);

    public boolean deleteAllTodos();

}
