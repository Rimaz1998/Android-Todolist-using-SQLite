package com.rinzler.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rinzler.todolist.data.DatabaseHandler;
import com.rinzler.todolist.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private final List<Todo> todoList1 = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseHandler db;

    private FloatingActionButton fab;

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    private EditText title;
    private EditText description;
    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);



         db = new DatabaseHandler(this);
         fab = findViewById(R.id.list_fab);

         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 //Todo show the same pop up menu and add the item
                 createPopUpDialog();
             }
         });


         List<Todo> todoList = db.getAllTodos();

         for (Todo todo : todoList){
             todoList1.add(todo);
             Log.d("FORLOOP","the timeadded " + todo.getDateTodoAdded());
         }


         recyclerView = findViewById(R.id.recyclerView);
         adapter = new RecyclerViewAdapter(this,todoList);
         recyclerView.setAdapter(adapter);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
         recyclerView.setHasFixedSize(true);
         adapter.notifyDataSetChanged();



    }

    private void createPopUpDialog() {
        builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.pop_up,null);

        //set the views on the pop.xml
        title = view.findViewById(R.id.popup_title);
        description = view.findViewById(R.id.popup_description);
        saveButton = view.findViewById(R.id.popup_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!title.getText().toString().isEmpty() && !description.getText().toString().isEmpty()){
                    saveTodo(view);
                }else {
                    Snackbar.make(view,"Fields cannot be Empty ", Snackbar.LENGTH_SHORT);
                }

            }
        });

        builder.setView(view);
        alertDialog = builder.create();//creating out dialog object
        alertDialog.show();

    }

    private void saveTodo(View view) {
        Todo todo = new Todo();

        //get the text
        String getTitle = title.getText().toString().trim();
        String getDesc = description.getText().toString().trim();

        //set the text to the instance variable
        todo.setTitle(getTitle);
        todo.setDescription(getDesc);
       // db.addTodo(new Todo());
        db.addTodo(todo);



        Snackbar.make(view, "Todo added to database",Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                //todo : then to move to next screen - details screen
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                //kill the current recycler view and restart it
                finish();
            }
        }, 1000);

    }
}