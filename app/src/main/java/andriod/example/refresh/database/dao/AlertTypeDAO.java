package andriod.example.refresh.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import andriod.example.refresh.database.model.AlertType;


@Dao
public interface AlertTypeDAO {
    @Query("SELECT * FROM alert_type")
    List<AlertType> getAll();

    @Query("SELECT * FROM alert_type where name LIKE  :name ")
    AlertType findByName(String name);

    @Query("SELECT COUNT(*) from alert_type")
    int countTypes();

    @Insert
    long insertAlertType(AlertType type);

    @Update
    void updateAlertType(AlertType type);

    @Delete
    void deleteAlertType(AlertType type);
}
