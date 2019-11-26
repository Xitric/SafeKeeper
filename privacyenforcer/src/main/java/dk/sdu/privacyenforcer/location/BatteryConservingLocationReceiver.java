package dk.sdu.privacyenforcer.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

public class BatteryConservingLocationReceiver {

    private final Context applicationContext;
    private final RawLocationReceiver rawLocationReceiver;

    //Allow for the location to be wrong by 1 km
    private float allowableError = 1000;

    private FusedLocationProviderClient client;
    private LocationManager locationManager;
    private LocationWindow window = new LocationWindow();
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            window.addLocation(locationResult.getLastLocation());
        }
    };

    /**
     * Creates a new object for obtaining the user's location at a minimal energy footprint. The
     * location obtained from this object is subject to a configurable error.
     *
     * @param context the application context
     */
    public BatteryConservingLocationReceiver(Context context) {
        this.applicationContext = context;
        this.locationManager = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);
        this.rawLocationReceiver = new RawLocationReceiver(context);

        ensureRegisteredForLocation();
    }

    private void ensureRegisteredForLocation() {
        if (hasPermission()) {
            return;
        }

        LocationRequest req = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_NO_POWER);

        LocationSettingsRequest setReq = new LocationSettingsRequest.Builder()
                .addLocationRequest(req)
                .build();

        LocationServices.getSettingsClient(applicationContext).checkLocationSettings(setReq).addOnSuccessListener(response -> {
            client = LocationServices.getFusedLocationProviderClient(applicationContext);
            client.requestLocationUpdates(req, locationCallback, null);
        });
    }

    private boolean hasPermission() {
        return client != null;
    }

    /**
     * Get the error that is allowed for locations retrieved by this object.
     *
     * @return the error allowed on location objects, in meters
     */
    public float getAllowableError() {
        return allowableError;
    }

    /**
     * Set the error that is allowed for locations retrieved by this object.
     *
     * @param allowableError the error to be allowed on location objects, in meters
     */
    public void setAllowableError(float allowableError) {
        this.allowableError = allowableError;
    }

    /**
     * Get the location of the device. The location retrieved by this method is subject to a
     * configurable error. This method is synchronous, so it should not be called from the main
     * thread.
     *
     * @return the location of the device
     */
    public Location getLocation() {
        ensureRegisteredForLocation();

        //Consider buffered location or most recent from the LocationManager depending on which one
        //is newest
        Location mostRecentLocation = window.getMostRecentLocation();
        if (applicationContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (mostRecentLocation == null || lastLocation.getTime() > mostRecentLocation.getTime()) {
                mostRecentLocation = lastLocation;
            }
        }

        //Return only in-memory location if it is not too uncertain
        if (isLocationSufficient(mostRecentLocation)) {
            return mostRecentLocation;
        }

        //Query GPS as a last resort
        synchronized (rawLocationReceiver) {
            rawLocationReceiver.getSingleGpsLocation(new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    locationCallback.onLocationResult(locationResult);
                    synchronized (rawLocationReceiver) {
                        rawLocationReceiver.notify();
                    }
                }
            });
            try {
                rawLocationReceiver.wait();
            } catch (InterruptedException ignored) {
            }
        }
        return window.getMostRecentLocation();
    }

    private boolean isLocationSufficient(Location location) {
        return getError(location) < allowableError;
    }

    private float getError(Location location) {
        if (location == null) return 0;

        float age = (System.currentTimeMillis() - location.getTime()) / 1000f;
        float speed = window.getAverageSpeed();

        return speed * age;
    }
}
