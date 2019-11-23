package dk.sdu.privacyenforcer.client.mutators;

import android.location.Location;

/**
 * Calculations inspired from: https://stackoverflow.com/questions/7477003/calculating-new-longitude-latitude-from-old-n-meters
 */

public class GeographicManager {

    private static final int EARTH_RADIUS = 6378;

    /**
     * @param location    the current location
     * @param latitudeKM  the latitude to be added to the current location
     * @param longitudeKM the longitude to be added to the current locaion
     * @return a fake location
     */
    public static Location computeFakeLocation(Location location, double latitudeKM, double longitudeKM) {
        double latitude = computeNewLatitude(location, latitudeKM);
        double longitude = computeNewLongitude(location, longitudeKM);

        Location l = new Location("GeographicManager");
        l.setLatitude(latitude);
        l.setLongitude(longitude);
        return l;
    }

    /**
     * A latitude value can be between -90 to 90
     *
     * @param location   the current location of the user
     * @param latitudeKM number of KMs to modify current latitude
     * @return a new modified latitude
     */
    private static double computeNewLatitude(Location location, double latitudeKM) {
        double latitude = location.getLatitude() + (latitudeKM / EARTH_RADIUS) * (180 / Math.PI);
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
     * @param longitudeKM number of KMs to modify the current longitude
     * @return a new modified longitude
     */
    private static double computeNewLongitude(Location location, double longitudeKM) {
        double longitude = location.getLongitude() + (longitudeKM / EARTH_RADIUS) * (180 / Math.PI)
                / Math.cos(location.getLatitude() * Math.PI / 180);
        if (longitude < -180) {
            longitude = longitude % 180 + 180;
        } else if (longitude > 180) {
            longitude = longitude % 180 - 180;
        }
        return longitude;
    }

}
