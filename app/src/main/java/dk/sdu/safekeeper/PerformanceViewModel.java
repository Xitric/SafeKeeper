package dk.sdu.safekeeper;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

import dk.sdu.safekeeper.repository.performance.PerformanceDatabase;
import dk.sdu.safekeeper.repository.performance.PerformanceMeasureEntity;
import dk.sdu.safekeeper.repository.weather.OpenWeatherService;
import dk.sdu.safekeeper.repository.weather.SimulatedOpenWeatherClient;

public class PerformanceViewModel extends AndroidViewModel {

    private static final int SAMPLES = 200;
    private final OpenWeatherService service;
    private final FusedLocationProviderClient locationManager;
    private final PerformanceDatabase database;
    private final MutableLiveData<Boolean> running;
    private Thread performanceThread;


    public PerformanceViewModel(@NonNull Application application) {
        super(application);

        service = SimulatedOpenWeatherClient.getService(application);
        locationManager = LocationServices.getFusedLocationProviderClient(application);
        database = PerformanceDatabase.getInstance(application);
        running = new MutableLiveData<>(false);
    }

    LiveData<List<PerformanceMeasureEntity>> loadTest(int id) {
        if (running.getValue() != null && running.getValue()) return null;

        running.postValue(true);

        locationManager.getLastLocation().addOnSuccessListener(location -> {
            performanceThread = new Thread(new PerformanceRunner(id, location));
            performanceThread.start();
        });

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
        private final Location location;

        private PerformanceRunner(int runId, Location location) {
            this.runId = runId;
            this.location = location;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < SAMPLES && !Thread.interrupted(); i++) {
                    long start = System.nanoTime();
                    service.getWeatherHere(location.getLatitude(), location.getLongitude()).execute();
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
