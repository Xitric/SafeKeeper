package dk.sdu.safekeeper.repository.performance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface PerformanceDao {

    @Insert(onConflict = REPLACE)
    void savePerformanceMeasure(PerformanceMeasureEntity measure);

    @Query("SELECT * FROM PerformanceMeasureEntity WHERE runId = :runId")
    LiveData<List<PerformanceMeasureEntity>> getPerformanceMeasures(int runId);

    @Query("SELECT * FROM PERFORMANCEMEASUREENTITY GROUP BY runId")
    LiveData<List<List<PerformanceMeasureEntity>>> getAllPerformanceMeasures();

    @Query("DELETE FROM PerformanceMeasureEntity")
    void clear();

    @Query("SELECT MAX(runId) FROM PerformanceMeasureEntity")
    LiveData<Integer> getMaxRunId();
}
