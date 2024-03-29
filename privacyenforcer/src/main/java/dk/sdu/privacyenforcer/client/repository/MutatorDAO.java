package dk.sdu.privacyenforcer.client.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MutatorDAO {

    @Query("SELECT * FROM MutatorEntity WHERE type IN (:type)")
    LiveData<List<MutatorEntity>> loadAllMutatorsIdsByType(String type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MutatorEntity> mutatorEntities);
}
