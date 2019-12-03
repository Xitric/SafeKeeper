package dk.sdu.safekeeper.repository.performance;

import androidx.room.Entity;

@Entity(primaryKeys = {"runId", "iteration"})
public class PerformanceMeasureEntity {

    private int runId;
    private int iteration;
    private int elapsedTime;

    public PerformanceMeasureEntity(int runId, int iteration, int elapsedTime) {
        this.runId = runId;
        this.iteration = iteration;
        this.elapsedTime = elapsedTime;
    }

    public int getRunId() {
        return runId;
    }

    public int getIteration() {
        return iteration;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }
}
