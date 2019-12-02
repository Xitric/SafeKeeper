package dk.sdu.safekeeper;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;

import dk.sdu.privacyenforcer.client.Privacy;
import dk.sdu.privacyenforcer.client.PrivacyEnforcingClient;
import dk.sdu.privacyenforcer.ui.PrivacyActivity;

public class MainActivity extends PrivacyActivity implements MainFragment.OnMainFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_containerx, new MainFragment());
        Log.i("CONTAINER_ID", "" + R.id.fragment_containerx);
        fragmentTransaction.commit();

        new PrivacyEnforcingClient(this); // TODO temp
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


    @Override
    public void onLocalObfuscation() {
        setLocalObfuscationArea();
    }

    @Override
    public void onRequestPermissions(String[] permissions, String[] explanations) {
        requestSendPermissions(permissions, explanations);
    }
}
