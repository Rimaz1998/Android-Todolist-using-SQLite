package com.rinzler.todolist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.rinzler.todolist.data.DatabaseHandler;
import com.rinzler.todolist.model.Todo;
import com.rinzler.todolist.util.Constants;

import org.w3c.dom.Text;
import org.w3c.dom.ls.LSException;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Todo> todoList;
    private DatabaseHandler db;

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    public RecyclerViewAdapter(Context context, List<Todo> todoList) {
        this.context = context;
        this.todoList = todoList;
        this.db = new DatabaseHandler(this.context);
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,null,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.title.setText(todo.getTitle());
        holder.description.setText("Description : " + todo.getDescription());
        holder.dateAdded.setText(todo.getDateTodoAdded());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context context;

        public TextView title;
        public TextView description;
        public TextView dateAdded;

        public Button rowDeleteButton;
        public Button rowEditButton;



        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            this.context = ctx;

            title = itemView.findViewById(R.id.row_title);
            description = itemView.findViewById(R.id.row_description);
            dateAdded = itemView.findViewById(R.id.row_dateadded);

            rowEditButton = itemView.findViewById(R.id.row_edit_button);
            rowDeleteButton = itemView.findViewById(R.id.row_delete_button);

            rowDeleteButton.setOnClickListener(this);
            rowEditButton.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {
            //TODO we get the id of the button clicked and remove / edit the todo
            int position = getAdapterPosition();
            Todo todo = todoList.get(position);
            switch (view.getId()){
                case R.id.row_delete_button:
                    //todo show a pop up for confirmation and delete the item
                    //get the todos position we going to remove from the todolist
                    //then pass the id
                    deleteTodo(todo.getId());
                    break;
                case R.id.row_edit_button:
                    //todo edit the todo / update
                    updateTodo(todo);
                    break;
            }
        }



        private void deleteTodo(final int id){

            builder= new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.delete_popup,null);

            Button yesButton = view.findViewById(R.id.yes_button);
            Button  noButton = view.findViewById(R.id.no_button);

            builder.setView(view);
            alertDialog = builder.create();
            alertDialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //calls the database and deletes the todo
                    db.deleteTodo(id);
                    //remove from the recycler view too
                    todoList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    alertDialog.dismiss();
                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });


        }
        private void updateTodo(final Todo newtodo ) {

            builder = new AlertDialog.Builder(context);

            View view = LayoutInflater.from(context).inflate(R.layout.pop_up,null);

            TextView titleTextView = view.findViewById(R.id.title);
            final EditText titleeditText = view.findViewById(R.id.popup_title);
            final EditText titleDescription = view.findViewById(R.id.popup_description);
            Button saveButton = view.findViewById(R.id.popup_button);


            //the position of the current item we selected is taken from the adapter and passed into the action item
            final Todo todo = todoList.get(getAdapterPosition());

            //getting the save items to the edittexts
            titleTextView.setText(R.string.title_text);
            saveButton.setText(R.string.update_text);

            titleeditText.setText(todo.getTitle());
            titleDescription.setText(todo.getDescription());

            builder.setView(view);
            alertDialog = builder.create();
            alertDialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo update the items entered by the user
                    db = new DatabaseHandler(context);




                    newtodo.setTitle(titleeditText.getText().toString().trim());
                    newtodo.setDescription(titleDescription.getText().toString().trim());

                    if (!titleeditText.getText().toString().isEmpty() && !titleDescription.getText().toString().isEmpty()){
                        db.updateTodo(newtodo);
                        notifyItemChanged(getAdapterPosition(),newtodo);//todo : Important to update the recycler view
                        Snackbar.make(view,"Todo Updated",Snackbar.LENGTH_SHORT);
                    }else {
                        Snackbar.make(view,"Fields cannot be Empty",Snackbar.LENGTH_SHORT);
                    }
                    alertDialog.dismiss();


                    db.close();
                }
            });

        }

    }




}
