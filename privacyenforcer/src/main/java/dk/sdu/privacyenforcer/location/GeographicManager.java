package dk.sdu.privacyenforcer.location;

import android.location.Location;

/**
 * Calculations inspired from: https://stackoverflow.com/questions/7477003/calculating-new-longitude-latitude-from-old-n-meters
 */

public class GeographicManager {

    private static final int EARTH_RADIUS = 6378;

    /**
     * @param location    the current location
     * @param latitudeKm  the latitude to be added to the current location, in kilometers
     * @param longitudeKm the longitude to be added to the current location, in kilometers
     * @return a fake location
     */
    public static Location computeFakeLocation(Location location, double latitudeKm, double longitudeKm) {
        double randomAreaWidth = (Math.random() * 2 * latitudeKm - latitudeKm) / 2;
        double randomAreaHeight = (Math.random() * 2 * longitudeKm - longitudeKm) / 2;
        double latitude = computeNewLatitude(location, randomAreaWidth);
        double longitude = computeNewLongitude(location, randomAreaHeight);

        Location fakeLocation = new Location("GeographicManager");
        fakeLocation.setLatitude(latitude);
        fakeLocation.setLongitude(longitude);
        return fakeLocation;
    }

    /**
     * A latitude value can be between -90 to 90
     *
     * @param location   the current location of the user
     * @param latitudeKm number of kilometers to modify current latitude
     * @return a new modified latitude
     */
    private static double computeNewLatitude(Location location, double latitudeKm) {
        double latitude = location.getLatitude() + (latitudeKm / EARTH_RADIUS) * (180 / Math.PI);
        if (latitude < -90) {
            latitude = latitude % 90 + 90;
        } else if (latitude > 90) {
            latitude = latitude % 90 - 90;
        }
        return latitude;
    }

    /**
     * A longitude can be between -180 to 180
     *
     * @param location    the current location of the user
     * @param longitudeKm number of kilometers to modify the current longitude
     * @return a new modified longitude
     */
    private static double computeNewLongitude(Location location, double longitudeKm) {
        double longitude = location.getLongitude() + (longitudeKm / EARTH_RADIUS) * (180 / Math.PI)
                / Math.cos(location.getLatitude() * Math.PI / 180);
        if (longitude < -180) {
            longitude = longitude % 180 + 180;
        } else if (longitude > 180) {
            longitude = longitude % 180 - 180;
        }
        return longitude;
    }
}
