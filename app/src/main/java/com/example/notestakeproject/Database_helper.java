package com.example.notestakeproject;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Users.class , exportSchema = false , version = 1)
public abstract class Database_helper extends RoomDatabase {

    public static final String DATABASE_NAME = "notes_database";
    public static Database_helper instance;// instance of database

    //create database instance

    public static synchronized Database_helper getDB(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context,Database_helper.class,DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();

        }
        return instance;
    }

    public abstract Users_DAO users_dao();//abstract method
}
