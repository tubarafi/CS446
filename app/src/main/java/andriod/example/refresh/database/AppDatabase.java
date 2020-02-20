package andriod.example.refresh.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import andriod.example.refresh.database.dao.AlertDAO;
import andriod.example.refresh.database.dao.AlertTypeDAO;
import andriod.example.refresh.database.model.Alert;
import andriod.example.refresh.database.model.AlertType;

@Database(entities = {Alert.class, AlertType.class},version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static  AppDatabase INSTANCE;

    public abstract AlertTypeDAO alertTypeDAO();
    public abstract AlertDAO alertDAO();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "refresh-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void cleanUp() {
        INSTANCE = null;
    }
}

