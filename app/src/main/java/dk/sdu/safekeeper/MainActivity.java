package dk.sdu.safekeeper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import dk.sdu.privacyenforcer.ui.Privacy;
import dk.sdu.privacyenforcer.ui.PrivacyActivity;
import dk.sdu.safekeeper.repository.PlaceholderClient;
import dk.sdu.safekeeper.repository.PlaceholderData;
import dk.sdu.safekeeper.repository.ServerResponse;
import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends PrivacyActivity {

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

    public void onRequestPermissionsAction(View sender) {
        //Request permissions for sending information over a network
        requestSendPermissions(
                new String[]{Privacy.Permission.SEND_LOCATION},
                new String[]{"For informing analytics frameworks about where you are so we can stalk you!"});
    }

    //Called when the requested permissions have been configured by the user
    @Override
    public void onRequestSendPermissionsResult(String[] permissions, Privacy.Mutation[] results) {
        for (int i = 0; i < permissions.length; i++) {
            Log.i("PermissionResult", "For the permission " + permissions[i] + " the user selected " + results[i]);
        }
    }
}
