package dk.sdu.privacyenforcer.client.mutators;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

import dk.sdu.privacyenforcer.client.RequestUrl;
import okhttp3.HttpUrl;

/**
 * Convenience implementation of {@link DataMutator} for location data to reduce the amount of
 * duplicated code in subclasses.
 */
abstract class AbstractLocationMutator implements DataMutator {

    /**
     * Template method to be overridden by subclasses to generate a fake location given an original
     * location fix.
     *
     * @param lat the latitude of the original location fix
     * @param lon the longitude of the original location fix
     * @return a fake location
     */
    abstract Location getHiddenLocation(double lat, double lon);

    @Override
    public void mutate(RequestUrl requestUrl, String[] flaggedParameters) {
        String latString = requestUrl.getUrl().queryParameter(flaggedParameters[0]);
        String lonString = requestUrl.getUrl().queryParameter(flaggedParameters[1]);

        assert latString != null && lonString != null;
        double lat = Double.parseDouble(latString);
        double lon = Double.parseDouble(lonString);

        Location hiddenLocation = getHiddenLocation(lat, lon);

        HttpUrl newUrl = requestUrl.getUrl().newBuilder()
                .setQueryParameter(flaggedParameters[0], String.valueOf(hiddenLocation.getLatitude()))
                .setQueryParameter(flaggedParameters[1], String.valueOf(hiddenLocation.getLongitude()))
                .build();

        requestUrl.setUrl(newUrl);
    }

    @Override
    public void mutate(JSONObject body, String[] flaggedElements) {
        double lat = body.optDouble(flaggedElements[0]);
        double lon = body.optDouble(flaggedElements[1]);

        Location hiddenLocation = getHiddenLocation(lat, lon);

        try {
            body.put(flaggedElements[0], hiddenLocation.getLatitude());
            body.put(flaggedElements[1], hiddenLocation.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Location locationFrom(double lat, double lon) {
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lon);
        return location;
    }
}
