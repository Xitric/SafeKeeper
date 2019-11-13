package dk.sdu.safekeeper;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import dk.sdu.safekeeper.repository.PlaceholderClient;
import dk.sdu.safekeeper.repository.PlaceholderData;
import dk.sdu.safekeeper.repository.ServerResponse;
import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlaceholderClient.getService(getApplicationContext()).postsomeStuff(new PlaceholderData("Kasper", 24, 1.96)).enqueue(new Callback<ServerResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                System.out.println();
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }
}
