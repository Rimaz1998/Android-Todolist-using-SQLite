package com.rinzler.todolist.model;

public class Todo {
    private int id;
    private String title;
    private String description;
    private String dateTodoAdded;




    public Todo() {
    }

    //this constructor allows us to pass value without the id
    public Todo(String title, String description, String dateTodoAdded) {
        this.title = title;
        this.description = description;
        this.dateTodoAdded = dateTodoAdded;
    }

    public Todo(int id, String title, String description, String dateTodoAdded) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateTodoAdded = dateTodoAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTodoAdded() {
        return dateTodoAdded;
    }

    public void setDateTodoAdded(String dateTodoAdded) {
        this.dateTodoAdded = dateTodoAdded;
    }
}
