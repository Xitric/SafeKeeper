package dk.sdu.privacyenforcer.client.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MutatorEntity.class}, version = 1, exportSchema = false)
public abstract class LibraryDatabase extends RoomDatabase {

    private static LibraryDatabase instance;

    public static LibraryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), LibraryDatabase.class, "LibraryDatabase").build();
        }
        return instance;
    }

    public abstract MutatorDAO mutatorDAO();
}
