package dk.sdu.safekeeper.repository.performance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class PerformanceDao {

    @Insert(onConflict = REPLACE)
    public abstract void savePerformanceMeasure(PerformanceMeasureEntity measure);

    @Query("SELECT * FROM PerformanceMeasureEntity WHERE runId = :runId")
    public abstract LiveData<List<PerformanceMeasureEntity>> getPerformanceMeasures(int runId);

    @Query("SELECT DISTINCT runId FROM PerformanceMeasureEntity")
    public abstract LiveData<List<Integer>> getAllRunIds();

    @Query("DELETE FROM PerformanceMeasureEntity")
    public abstract void clear();

    @Query("SELECT MAX(runId) FROM PerformanceMeasureEntity")
    public abstract LiveData<Integer> getMaxRunId();
}
