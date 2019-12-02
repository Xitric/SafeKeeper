package dk.sdu.privacyenforcer.client.mutators;

import android.location.Location;

import dk.sdu.privacyenforcer.location.GeographicManager;

public class LocationDummyMutator extends AbstractLocationMutator {

    private static final double latitudeObfuscation = 10;
    private static final double longitudeObfuscation = 10;

    @Override
    Location getHiddenLocation(double lat, double lon) {
        Location originalLocation = locationFrom(lat, lon);
        return GeographicManager.computeFakeLocation(originalLocation, latitudeObfuscation, longitudeObfuscation);
    }
}
