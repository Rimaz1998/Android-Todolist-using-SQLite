package com.rinzler.todolist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.rinzler.todolist.model.Todo;
import com.rinzler.todolist.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    //todo :always have a context
    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION    );
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_TODO_TITLE + " TEXT,"
                + Constants.KEY_TODO_DESCRIPTION + " TEXT,"
                + Constants.KEY_TODO_DATE_ADDED + " LONG);";

        sqLiteDatabase.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }

    //TODO : CRUD Operations
    public void addTodo(Todo todo){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.KEY_TODO_TITLE, todo.getTitle());
        values.put(Constants.KEY_TODO_DESCRIPTION, todo.getDescription());

        values.put(Constants.KEY_TODO_DATE_ADDED, java.lang.System.currentTimeMillis());

        Log.d("DATE", "Date " + values.get(Constants.KEY_TODO_DATE_ADDED));

        db.insert(Constants.TABLE_NAME,null,values);

        Log.d("DBHandler", "addedItem");

    }

    //TODO GET A TODO
    public Todo getTodo(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String []{
                            Constants.KEY_ID,
                            Constants.KEY_TODO_TITLE,
                            Constants.KEY_TODO_DESCRIPTION,
                            Constants.KEY_TODO_DATE_ADDED},
                    Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null);

        if (cursor !=null){
            cursor.moveToFirst();
        }

        Todo todo = new Todo();
        todo.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        todo.setTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_TODO_TITLE)));
        todo.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_TODO_DESCRIPTION)));

        //convert timestamp to something readable
        DateFormat dateFormat = DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_TODO_DATE_ADDED)))
                .getTime());//will be formated ex : FEB 23, 2020;

        todo.setDateTodoAdded(formattedDate);


        return todo;

    }
    //TODO GET ALL TODOS

    public List<Todo> getAllTodos(){

        SQLiteDatabase db = this.getReadableDatabase();
        List<Todo> todoList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
                        Constants.KEY_ID,
                        Constants.KEY_TODO_TITLE,
                        Constants.KEY_TODO_DESCRIPTION,
                        Constants.KEY_TODO_DATE_ADDED},
                        null,null,null,null, Constants.KEY_TODO_DATE_ADDED + " DESC");

        if (cursor.moveToFirst()){

            do {
                Todo todo = new Todo();

                todo.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                todo.setTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_TODO_TITLE)));
                todo.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_TODO_DESCRIPTION)));

                //convert timestamp to something readable
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_TODO_DATE_ADDED)))
                        .getTime());//will be formated ex : FEB 23, 2020;

                todo.setDateTodoAdded(formattedDate);

                todoList.add(todo);
            }while (cursor.moveToNext());
        }
        return  todoList;
    }

    //TODO UPDATE A TODO
    public int updateTodo(Todo todo){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_TODO_TITLE, todo.getTitle());
        values.put(Constants.KEY_TODO_DESCRIPTION, todo.getDescription());
        values.put(Constants.KEY_TODO_DATE_ADDED,todo.getDateTodoAdded());

       return  db.update(Constants.TABLE_NAME,values,Constants.KEY_ID + "=?",new String[]{String.valueOf(todo.getId())});
    }

    //TODO DELETE A TODO
    public void deleteTodo(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    //TODO GET TODO ROW COUNT
    public int getCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }

}
