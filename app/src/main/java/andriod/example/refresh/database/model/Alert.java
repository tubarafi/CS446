package andriod.example.refresh.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "alert")
public class Alert {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String name;

    @ColumnInfo(name = "expiry_date")
    @NonNull
    private String expiryDate;

    @ColumnInfo(name = "alert_type_id")
    @NonNull
    private long alertTypeId;

    public Alert(long id, String name, String expiryDate, long alertTypeId) {
        this.id = id;
        this.name = name;
        this.expiryDate = expiryDate;
        this.alertTypeId = alertTypeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public long getAlertTypeId() {
        return alertTypeId;
    }

    public void setAlertTypeId(long alertTypeId) {
        this.alertTypeId = alertTypeId;
    }
}
