package com.example.notestakeproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Users_DAO {

    @Query("select * from users")
    List<Users> getAllUsers();

    @Insert
    void insert_data(Users users);

    @Delete
    void delete_data(Users users);

    @Update
    void update_data(Users users);
}
