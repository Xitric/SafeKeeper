package dk.sdu.privacyenforcer.client.mutators;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

import dk.sdu.privacyenforcer.client.RequestUrl;
import dk.sdu.privacyenforcer.location.GeographicManager;
import okhttp3.HttpUrl;

public class LocationDummyMutator implements DataMutator {

    private static final double latitudeObfuscation = 10;
    private static final double longitudeObfuscation = 10;

    @Override
    public void mutate(RequestUrl requestUrl, String[] flaggedParameters) {
        String latString = requestUrl.getUrl().queryParameter(flaggedParameters[0]);
        String lonString = requestUrl.getUrl().queryParameter(flaggedParameters[1]);

        assert latString != null && lonString != null;
        double lat = Double.parseDouble(latString);
        double lon = Double.parseDouble(lonString);

        Location obfuscatedLocation = getObfuscatedLocation(lat, lon);

        HttpUrl newUrl = requestUrl.getUrl().newBuilder()
                .setQueryParameter(flaggedParameters[0], String.valueOf(obfuscatedLocation.getLatitude()))
                .setQueryParameter(flaggedParameters[1], String.valueOf(obfuscatedLocation.getLongitude()))
                .build();

        requestUrl.setUrl(newUrl);
    }

    @Override
    public void mutate(JSONObject body, String[] flaggedElements) {
        double lat = body.optDouble(flaggedElements[0]);
        double lon = body.optDouble(flaggedElements[1]);

        Location obfuscatedLocation = getObfuscatedLocation(lat, lon);

        try {
            body.put(flaggedElements[0], lat);
            body.put(flaggedElements[1], lon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Location getObfuscatedLocation(double lat, double lon) {
        Location originalLocation = new Location("");
        originalLocation.setLatitude(lat);
        originalLocation.setLongitude(lon);

        return GeographicManager.computeFakeLocation(originalLocation, latitudeObfuscation, longitudeObfuscation);
    }
}
