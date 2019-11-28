package dk.sdu.safekeeper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import dk.sdu.safekeeper.repository.performance.PerformanceMeasureEntity;

public class PerformanceActivity extends AppCompatActivity {

    private PerformanceViewModel viewModel;
    private LiveData<List<PerformanceMeasureEntity>> currentMeasures;

    private Button beginButton;
    private Button stopButton;
    private LineChart chart;
    private LineData chartData;

    private int[] colors = new int[]{
            R.color.colorGraph1,
            R.color.colorGraph2,
            R.color.colorGraph3,
            R.color.colorGraph4,
            R.color.colorGraph5,
            R.color.colorGraph6,
            R.color.colorGraph7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        beginButton = findViewById(R.id.btn_begin);
        stopButton = findViewById(R.id.btn_stop);
        chart = findViewById(R.id.lnc_chart);

        chartData = new LineData();
        chart.setScaleEnabled(true);
        chart.getXAxis().setAxisMinimum(1);
        chart.getXAxis().setAxisMaximum(200);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
        Description description = new Description();
        description.setText("Running mean of response times");
        chart.setDescription(description);

        viewModel = ViewModelProviders.of(this).get(PerformanceViewModel.class);
        viewModel.getRunning().observe(this, running -> {
            beginButton.setEnabled(!running);
            stopButton.setEnabled(running);
        });

        LiveData<List<Integer>> allRunIds = viewModel.getAllRunIds();
        allRunIds.observe(this, runIds -> {
            allRunIds.removeObservers(this);

            for (Integer runId : runIds) {
                viewModel.getPerformanceMeasures(runId).observe(this, this::display);
            }

            if (runIds.size() == 0) {
                clear();
            }
        });
    }

    private void display(List<PerformanceMeasureEntity> run) {
        if (run.size() == 0) return;
        removeRunDataSet(run.get(0).getRunId());

        List<Entry> runEntries = new ArrayList<>();
        float runningSum = 0;
        for (PerformanceMeasureEntity measure : run) {
            runningSum += measure.getElapsedTime();
            float runningMean = runningSum / (measure.getIteration() + 1);
            runEntries.add(new Entry(measure.getIteration() + 1, runningMean));
        }

        LineDataSet runDataSet = new LineDataSet(runEntries, "Run: " + run.get(0).getRunId());
        runDataSet.setDrawValues(false);
        runDataSet.setDrawCircles(false);
        runDataSet.setColor(getResources().getColor(colors[run.get(0).getRunId() % colors.length], getTheme()));

        chartData.addDataSet(runDataSet);
        chart.setData(chartData);
        chart.invalidate();
    }

    private void removeRunDataSet(int runId) {
        ILineDataSet toRemove = null;

        for (ILineDataSet dataSet : chartData.getDataSets()) {
            if (dataSet.getLabel().endsWith(" " + runId)) {
                toRemove = dataSet;
                break;
            }
        }

        if (toRemove != null) {
            chartData.removeDataSet(toRemove);
        }
    }

    private void clear() {
        chartData.clearValues();
        chart.setData(chartData);
        chart.invalidate();
    }

    public void onBeginAction(View sender) {
        if (currentMeasures != null) currentMeasures.removeObservers(this);

        LiveData<Integer> maxId = viewModel.getMaxId();
        maxId.observe(this, id -> {
            maxId.removeObservers(this);

            currentMeasures = viewModel.loadTest(id == null ? 0 : id + 1);
            currentMeasures.observe(this, this::display);
        });
    }

    public void onStopAction(View sender) {
        viewModel.stop();
    }

    public void onClearAction(View sender) {
        viewModel.clear();
        clear();
    }
}
