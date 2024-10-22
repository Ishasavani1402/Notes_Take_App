package com.example.notestakeproject;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class MainActivity extends AppCompatActivity {

    AppCompatButton add_notes;
    RecyclerView rcycle_show_notes;
    List<Users> usersList;
    show_notes_on_rcycle show_notes_on_rcycle;
    FloatingActionButton btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        btn_add = findViewById(R.id.btn_add);
        rcycle_show_notes = findViewById(R.id.rcycle_show_notes);
        rcycle_show_notes.setLayoutManager(new LinearLayoutManager(this));// set layout manager
        Database_helper db_helper =Database_helper.getDB(this);

        // Load existing notes when the activity is created
        usersList = db_helper.users_dao().getAllUsers();
        show_notes_on_rcycle = new show_notes_on_rcycle(usersList);
        rcycle_show_notes.setAdapter(show_notes_on_rcycle);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_notes);

                //find id
                EditText title = dialog.findViewById(R.id.title);
                EditText description = dialog.findViewById(R.id.description);
                add_notes = dialog.findViewById(R.id.add_notes_success);


                add_notes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title_text = title.getText().toString();
                        String description_text = description.getText().toString();
                        long button_click_time = System.currentTimeMillis();// get current time

                        //after clicking on add button value set on database
                        if(!title_text.isEmpty() && !description_text.isEmpty()){

                            //get current date_time
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a" , Locale.getDefault());
                            String current_date_time = simpleDateFormat.format(button_click_time);
                            // insert data on database
                            Users users = new Users(title_text,description_text);
                            db_helper.users_dao().insert_data(users);

                            usersList.add(users);
                            show_notes_on_rcycle.notifyItemInserted(usersList.size()-1);
                            Toast.makeText(MainActivity.this, "Data inserted Successfully..", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            //show data on recycler view after clicking on add button
                            usersList = db_helper.users_dao().getAllUsers();
                            show_notes_on_rcycle = new show_notes_on_rcycle(usersList);
                            rcycle_show_notes.setAdapter(show_notes_on_rcycle);
                        }
                        else{
                            //custom toast design
                            View customtoast = getLayoutInflater().inflate(R.layout.toast_design , null);
                            TextView toast_msg = customtoast.findViewById(R.id.toast_msg);
//
                            Toast toast = new Toast(MainActivity.this);
                            toast.setView(customtoast);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM , 0 , 40);
                            toast.show();


//                            Toast.makeText(MainActivity.this, "All fields are required..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();

            }
        });

    }
}