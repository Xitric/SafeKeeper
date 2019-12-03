package dk.sdu.safekeeper.repository.performance;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = PerformanceMeasureEntity.class, version = 1)
public abstract class PerformanceDatabase extends RoomDatabase {

    private static PerformanceDatabase instance;

    public static PerformanceDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), PerformanceDatabase.class, "performance-database")
                    .build();
        }
        return instance;
    }

    public abstract PerformanceDao performanceDao();
}
