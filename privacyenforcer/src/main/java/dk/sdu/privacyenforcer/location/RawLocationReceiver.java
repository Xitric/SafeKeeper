package dk.sdu.privacyenforcer.location;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

class RawLocationReceiver {

    private Context applicationContext;
    private FusedLocationProviderClient client;

    /**
     * Constructs a new object used to receive accurate location updates from the Android system.
     *
     * @param context the application context, which is required to access location services
     */
    RawLocationReceiver(Context context) {
        applicationContext = context;
    }

    /**
     * Register to receive a single GPS location update.
     *
     * @param callback method to handle the resulting location result
     */
    void getSingleGpsLocation(LocationCallback callback) {
        LocationRequest req = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest setReq = new LocationSettingsRequest.Builder()
                .addLocationRequest(req)
                .build();

        LocationCallback internalCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                client.removeLocationUpdates(this);
                Log.i("UniqueTag", "Received accurate GPS location");
                callback.onLocationResult(locationResult);
            }
        };

        LocationServices.getSettingsClient(applicationContext).checkLocationSettings(setReq).addOnSuccessListener(response -> {
            client = LocationServices.getFusedLocationProviderClient(applicationContext);
            client.requestLocationUpdates(req, internalCallback, null);
        });
    }
}
