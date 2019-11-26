package dk.sdu.safekeeper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import dk.sdu.safekeeper.repository.weather.SimulatedOpenWeatherClient;

public class PerformanceActivity extends AppCompatActivity {

    private static final int SAMPLES = 200;
    private Thread performanceThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
    }

    public void onBeginAction(View sender) {
        if (performanceThread != null) {
            performanceThread.interrupt();
        }

        performanceThread = new Thread(() -> {
            try {
                for (int i = 0; i < SAMPLES && !Thread.interrupted(); i++) {
                    long start = System.nanoTime();
                    //TODO: Use actual user position queried from GPS
                    SimulatedOpenWeatherClient.getService(this).getWeatherHere(0, 0).execute();
                    long elapsed = System.nanoTime() - start;

                    //TODO: graph using livedata, use a view model too!
                    Log.v("PerformanceTestResult", String.valueOf(elapsed / 1000000));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        performanceThread.start();
    }
}
