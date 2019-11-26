package dk.sdu.privacyenforcer.client.filters;

import android.location.Location;

import org.json.JSONObject;

import java.util.regex.Pattern;

import dk.sdu.privacyenforcer.client.PrivacyViolation;
import dk.sdu.privacyenforcer.client.PrivacyViolationUrl;
import dk.sdu.privacyenforcer.client.RequestUrl;
import dk.sdu.privacyenforcer.client.ViolationCollection;
import dk.sdu.privacyenforcer.client.mutators.DataMutator;
import dk.sdu.privacyenforcer.client.mutators.LocationKAnonymityMutator;
import dk.sdu.privacyenforcer.location.BatteryConservingLocationReceiver;
import okhttp3.HttpUrl;

public class FineLocationFilter extends AbstractFilter {

    private final float FILTERED_DISTANCE = 10000;
    private final BatteryConservingLocationReceiver locationReceiver;
    private final DataMutator mutator = new LocationKAnonymityMutator();
    private final Pattern floatPattern = Pattern.compile("^[-+]?[0-9]*\\.?,?[0-9]+([eE][-+]?[0-9]+)?$");

    public FineLocationFilter(BatteryConservingLocationReceiver locationReceiver) {
        this.locationReceiver = locationReceiver;
    }

    @Override
    public void filter(RequestUrl requestUrl, ViolationCollection violations) {
        HttpUrl url = requestUrl.getUrl();
        if (shouldSkip()) return;

        //Generate all possible pairs of query parameters as floats
        for (int i = 0; i < url.querySize() - 1; i++) {
            if (!floatPattern.matcher(url.queryParameterValue(i)).matches()) continue;
            float a = Float.parseFloat(url.queryParameterValue(i));

            for (int j = i + 1; j < url.querySize(); j++) {
                if (!floatPattern.matcher(url.queryParameterValue(j)).matches()) continue;
                float b = Float.parseFloat(url.queryParameterValue(j));

                if (isCloseBy(a, b) || isCloseBy(b, a)) {
                    if (shouldAbort()) {
                        violations.abort();
                        return;
                    }

                    PrivacyViolation violation = new PrivacyViolationUrl(mutator, requestUrl, new String[]{
                            url.queryParameterName(i),
                            url.queryParameterName(j)
                    });
                    violations.addViolation(violation);
                }
            }
        }
    }

    @Override
    public void filter(JSONObject body, ViolationCollection violations) {
        if (shouldSkip()) return;

        //TODO
    }

    private boolean isCloseBy(float latGuess, float lonGuess) {
        Location deviceLocation = locationReceiver.getLocation();

        Location guessedLocation = new Location("");
        guessedLocation.setLatitude(latGuess);
        guessedLocation.setLongitude(lonGuess);

        return deviceLocation.distanceTo(guessedLocation) <= FILTERED_DISTANCE;
    }

}
