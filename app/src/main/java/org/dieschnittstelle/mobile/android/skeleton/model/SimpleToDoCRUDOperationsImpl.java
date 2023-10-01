package org.dieschnittstelle.mobile.android.skeleton.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleToDoCRUDOperationsImpl implements IToDoCRUDOperations{

    private static long idcounter = 0;

    private List<ToDo> items = new ArrayList<>();

    public SimpleToDoCRUDOperationsImpl() {
        Arrays.asList("lirem","dopsum","olor","sed","adipiscing")
                .forEach(name -> createToDo(new ToDo(name)));
    }

    @Override
    public ToDo createToDo(ToDo item) {
        item.setId(++idcounter);
        items.add(item);
        return item;
    }

    @Override
    public List<ToDo> readAllToDos() {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        return items;
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
