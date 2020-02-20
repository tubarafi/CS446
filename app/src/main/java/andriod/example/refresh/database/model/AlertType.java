package andriod.example.refresh.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alert_type")
public class AlertType {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    @NonNull
    private String name;

    @ColumnInfo(name = "notification_days")
    @NonNull
    private int notificationDays;

    public AlertType(int id, String name, int notificationDays) {
        this.id = id;
        this.name = name;
        this.notificationDays = notificationDays;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNotificationDays() {
        return notificationDays;
    }

    public void setNotificationDays(int notificationDays) {
        this.notificationDays = notificationDays;
    }
}
