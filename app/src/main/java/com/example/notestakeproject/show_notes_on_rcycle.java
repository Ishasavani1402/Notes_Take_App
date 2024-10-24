package com.example.notestakeproject;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class show_notes_on_rcycle extends RecyclerView.Adapter<show_notes_on_rcycle.ViewHolder>  {
    Context context;
    List<Users> structureArrayList;
    public show_notes_on_rcycle(List<Users> structureArrayList){
        this.structureArrayList = structureArrayList;
    }

    @NonNull
    @Override
    public show_notes_on_rcycle.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_rycle , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull show_notes_on_rcycle.ViewHolder holder, int position) {
        int current_position = holder.getAdapterPosition();
        holder.title.setText(structureArrayList.get(current_position).getTitle());
        holder.description.setText(structureArrayList.get(current_position).getDescription());
        //set longpress on any notes
        holder.rcycle_card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //initialize dialog
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.delete_notes);

                //gey the dilog window(for position)
                Window window = dialog.getWindow();
                if(window!=null){
                    window.setGravity(Gravity.BOTTOM);
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);// set layout for dialog
                    window.setWindowAnimations(android.R.style.Animation_Dialog);
                }

                AppCompatButton btn_cencle = dialog.findViewById(R.id.btn_cencle);
                AppCompatButton btn_delete = dialog.findViewById(R.id.btn_delete);

                //delete button click
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Users users = structureArrayList.get(current_position);//get notes
                        Database_helper db_helper = Database_helper.getDB(view.getContext());
                        db_helper.users_dao().delete_data(users);//delete notes from database
                        structureArrayList.remove(current_position);//remove notes from list
                        notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });
                //cancle button
                btn_cencle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                //i want to show dilog on bottom side of screen
                dialog.show();
                return true;
            }
        });

        //update notes on
        holder.rcycle_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.add_notes);
                //find id
                TextView et_title = dialog.findViewById(R.id.title);
                TextView et_description = dialog.findViewById(R.id.description);
                AppCompatButton add_notes = dialog.findViewById(R.id.add_notes_success);
                et_title.setText("Update notes");
                add_notes.setText("Update");
                int adapter_position = holder.getAdapterPosition();

                et_title.setText(structureArrayList.get(adapter_position).getTitle());
                et_description.setText(structureArrayList.get(adapter_position).getDescription());
                //click on add buttons
                add_notes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = structureArrayList.get(adapter_position).getId();
                        String title = et_title.getText().toString();
                        String description = et_description.getText().toString();
                        if(!title.isEmpty() && !description.isEmpty()){
                            Database_helper db_helper = Database_helper.getDB(view.getContext());

                            //get current data
                            Users user = structureArrayList.get(adapter_position);
                            db_helper.users_dao().update_data(new Users(structureArrayList.get(adapter_position).getId() , title , description));
                            structureArrayList.set(adapter_position , new Users(title , description));
                            notifyDataSetChanged();
                            dialog.dismiss();

                        }
                        else{
                            Toast.makeText(view.getContext(), "Please enter title and description", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            };
        });
    }

    @Override
    public int getItemCount() {
        return structureArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title , description , data_time;
        LinearLayout rcycle_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rycle_title);
            description = itemView.findViewById(R.id.rycle_description);
            rcycle_card = itemView.findViewById(R.id.rcycle_card);

        }
    }

}
