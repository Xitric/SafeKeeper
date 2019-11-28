package dk.sdu.safekeeper;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.List;

import dk.sdu.safekeeper.repository.performance.PerformanceDatabase;
import dk.sdu.safekeeper.repository.performance.PerformanceMeasureEntity;
import dk.sdu.safekeeper.repository.weather.OpenWeatherService;
import dk.sdu.safekeeper.repository.weather.SimulatedOpenWeatherClient;

public class PerformanceViewModel extends AndroidViewModel {

    private static final int SAMPLES = 200;
    private final OpenWeatherService service;
    private final PerformanceDatabase database;
    private final MutableLiveData<Boolean> running;
    private Thread performanceThread;

    public PerformanceViewModel(@NonNull Application application) {
        super(application);

        service = SimulatedOpenWeatherClient.getService(application);
        database = PerformanceDatabase.getInstance(application);
        running = new MutableLiveData<>(false);
    }

    LiveData<List<PerformanceMeasureEntity>> loadTest(int id) {
        if (running.getValue() != null && running.getValue()) return null;

        running.postValue(true);

        performanceThread = new Thread(new PerformanceRunner(id));
        performanceThread.start();

        return database.performanceDao().getPerformanceMeasures(id);
    }

    void stop() {
        if (performanceThread != null) {
            performanceThread.interrupt();
        }
    }

    void clear() {
        new Thread(() -> database.performanceDao().clear()).start();
    }

    LiveData<Boolean> getRunning() {
        return running;
    }

    LiveData<List<Integer>> getAllRunIds() {
        return database.performanceDao().getAllRunIds();
    }

    LiveData<Integer> getMaxId() {
        return database.performanceDao().getMaxRunId();
    }

    LiveData<List<PerformanceMeasureEntity>> getPerformanceMeasures(int runId) {
        return database.performanceDao().getPerformanceMeasures(runId);
    }

    @Override
    protected void onCleared() {
        stop();
    }

    private final class PerformanceRunner implements Runnable {

        private final int runId;

        private PerformanceRunner(int runId) {
            this.runId = runId;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < SAMPLES && !Thread.interrupted(); i++) {
                    long start = System.nanoTime();
                    //TODO: Use actual user position queried from GPS
                    service.getWeatherHere(0, 0).execute();
                    int elapsed = (int) ((System.nanoTime() - start) / 1000000);

                    PerformanceMeasureEntity measure = new PerformanceMeasureEntity(runId, i, elapsed);
                    database.performanceDao().savePerformanceMeasure(measure);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            running.postValue(false);
        }
    }
}
