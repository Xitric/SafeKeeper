package dk.sdu.safekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import dk.sdu.privacyenforcer.client.Privacy;
import dk.sdu.privacyenforcer.client.PrivacyEnforcingClient;
import dk.sdu.privacyenforcer.ui.PreferenceActivity;
import dk.sdu.privacyenforcer.ui.PrivacyActivity;

public class MainActivity extends PrivacyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new PrivacyEnforcingClient(this);
    }

    //Called when the requested permissions have been configured by the user
    @Override
    public void onRequestSendPermissionsResult(String[] permissions, Privacy.Mutation[] results) {
        if (results.length == 0) {
            Log.i("PermissionResult", "User cancelled the permissions request");
            return;
        }

        for (int i = 0; i < permissions.length; i++) {
            Log.i("PermissionResult", "For the permission " + permissions[i] + " the user selected " + results[i]);
        }
    }

    public void onSettingsAction(View sender) {
        Intent startPreferences = new Intent(this, PreferenceActivity.class);
        startActivity(startPreferences);
    }


    public void onWeatherAction(View sender) {
        Intent startWeather = new Intent(this, WeatherActivity.class);
        startActivity(startWeather);
    }

    //TODO: Redo
    public void onLocalObfuscationAction(View sender) {
//        setLocalObfuscationArea();
    }

    public void onRequestPermissionsAction(View sender) {
        //Request permissions for sending information over a network
        requestSendPermissions(
                new String[]{
                        Privacy.Permission.SEND_LOCATION,
                        Privacy.Permission.SEND_ACCELERATION,
                        Privacy.Permission.SEND_CONTACTS
                }, new String[]{
                        "For informing analytics frameworks about where you are so we can stalk you!",
                        "For inferring your passwords so we can hack your bank account!",
                        "For sending spam e-mails to all of your friends!"
                });
    }

    public void onPerformanceAction(View sender) {
        Intent startPerformance = new Intent(this, PerformanceActivity.class);
        startActivity(startPerformance);
    }

    public void onMessengerAction(View sender) {
        Intent startMessenger = new Intent(this, MessengerActivity.class);
        startActivity(startMessenger);
    }
}
