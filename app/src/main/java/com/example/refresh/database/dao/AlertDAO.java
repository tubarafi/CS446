package com.example.refresh.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.refresh.database.model.Alert;

@Dao
public interface AlertDAO {
    @Query("SELECT * FROM alert")
    List<Alert> getAll();

    @Query("SELECT * FROM alert where name LIKE  :name ")
    Alert findByName(String name);

    @Query("SELECT COUNT(*) from alert")
    int countAlerts();

    @Insert
    long insertAlert(Alert alert);

    @Update
    void updateAlert(Alert alert);

    @Delete
    void deleteAlert(Alert alert);
}
