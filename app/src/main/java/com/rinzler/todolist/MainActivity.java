package com.rinzler.todolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rinzler.todolist.data.DatabaseHandler;
import com.rinzler.todolist.model.Todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private EditText title;
    private EditText description;
    private Button saveButton;
    private static DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopUpDialog();
            }
        });

        byPassActivity();
    }

    private void byPassActivity(){
        if (db.getCount() > 0){
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
    }

    private void createPopUpDialog() {
        alertDialogBuilder = new AlertDialog.Builder(this);
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
        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();//creating out dialog object
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

        db.addTodo(todo);


        //todo or

        Snackbar.make(view, "Todo added to database",Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                //todo : then to move to next screen - details screen
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}