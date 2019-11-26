package dk.sdu.safekeeper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.io.IOException;

import dk.sdu.safekeeper.repository.weather.SimulatedOpenWeatherClient;

public class PerformanceActivity extends AppCompatActivity {

    private PerformanceViewModel viewModel;

    private Button beginButton;
    private Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        beginButton = findViewById(R.id.btn_begin);
        stopButton = findViewById(R.id.btn_stop);

        viewModel = ViewModelProviders.of(this).get(PerformanceViewModel.class);
        viewModel.getRunning().observe(this, running -> {
            beginButton.setEnabled(!running);
            stopButton.setEnabled(running);
        });
    }

    public void onBeginAction(View sender) {
        viewModel.loadTest();
    }

    public void onStopAction(View sender) {
        viewModel.stop();
    }

    public void onClearAction(View sender) {
        viewModel.clear();
    }
}
