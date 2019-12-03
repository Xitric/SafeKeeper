package dk.sdu.privacyenforcer.client.filters;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import dk.sdu.privacyenforcer.client.Privacy;
import dk.sdu.privacyenforcer.client.PrivacyViolation;
import dk.sdu.privacyenforcer.client.PrivacyViolationBody;
import dk.sdu.privacyenforcer.client.PrivacyViolationUrl;
import dk.sdu.privacyenforcer.client.RequestUrl;
import dk.sdu.privacyenforcer.client.ViolationCollection;
import dk.sdu.privacyenforcer.client.mutators.DataMutator;
import dk.sdu.privacyenforcer.client.mutators.LocationDummyMutator;
import dk.sdu.privacyenforcer.client.mutators.LocationKAnonymityMutator;
import dk.sdu.privacyenforcer.location.BatteryConservingLocationReceiver;
import okhttp3.HttpUrl;

public class FineLocationFilter extends AbstractFilter {

    private final float FILTERED_DISTANCE = 10000;
    private final BatteryConservingLocationReceiver locationReceiver;
    private final Pattern floatPattern = Pattern.compile("^[-+]?[0-9]*\\.?,?[0-9]+([eE][-+]?[0-9]+)?$");

    public FineLocationFilter(BatteryConservingLocationReceiver locationReceiver) {
        this.locationReceiver = locationReceiver;
        mutators.put(Privacy.LocationMutators.LOCAL_OBFUSCATION, new LocationDummyMutator());
        mutators.put(Privacy.LocationMutators.K_ANONYMITY, new LocationKAnonymityMutator());
        mutator = mutators.get(Privacy.LocationMutators.LOCAL_OBFUSCATION);
    }

    @Override
    public void filter(RequestUrl requestUrl, ViolationCollection violations) {
        HttpUrl url = requestUrl.getUrl();
        if (shouldSkip()) return;

        //Generate all possible pairs of query parameters as doubles
        for (int i = 0; i < url.querySize() - 1; i++) {
            if (!floatPattern.matcher(url.queryParameterValue(i)).matches()) continue;
            double a = Double.parseDouble(url.queryParameterValue(i));

            for (int j = i + 1; j < url.querySize(); j++) {
                if (!floatPattern.matcher(url.queryParameterValue(j)).matches()) continue;
                double b = Double.parseDouble(url.queryParameterValue(j));

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

        findViolationsRecursively(body, violations);
    }

    private void findViolationsRecursively(JSONObject body, ViolationCollection violations) {
        List<String> doubleElements = new ArrayList<>();

        //For each object recursively, extract all doubles
        for (Iterator<String> keys = body.keys(); keys.hasNext(); ) {
            String next = keys.next();

            JSONObject nestedObject = body.optJSONObject(next);
            if (nestedObject != null) {
                findViolationsRecursively(nestedObject, violations);
                continue;
            }

            JSONArray nestedArray = body.optJSONArray(next);
            if (nestedArray != null) {
                for (int i = 0; i < nestedArray.length(); i++) {
                    JSONObject arrayObject = nestedArray.optJSONObject(i);
                    if (arrayObject != null) {
                        findViolationsRecursively(arrayObject, violations);
                        continue;
                    }

                    double detectedDouble = nestedArray.optDouble(i);
                    if (!Double.isNaN(detectedDouble)) {
                        doubleElements.add(next);
                    }
                }
                continue;
            }

            double detectedDouble = body.optDouble(next);
            if (!Double.isNaN(detectedDouble)) {
                doubleElements.add(next);
            }
        }

        //Test all possible pairs
        for (int i = 0; i < doubleElements.size(); i++) {
            double a = body.optDouble(doubleElements.get(i));

            for (int j = i + 1; j < doubleElements.size(); j++) {
                double b = body.optDouble(doubleElements.get(j));

                if (isCloseBy(a, b) || isCloseBy(b, a)) {
                    if (shouldAbort()) {
                        violations.abort();
                        return;
                    }

                    PrivacyViolation violation = new PrivacyViolationBody(mutator, body, new String[]{
                            doubleElements.get(i),
                            doubleElements.get(j)
                    });
                    violations.addViolation(violation);
                }
            }
        }
    }

    @Override
    public Map<String, DataMutator> getMutators() {
        return mutators;
    }

    @Override
    public void setDataMutator(String mutatorId) {
        if (mutatorId != null) {
            this.mutator = mutators.get(mutatorId);
        }
    }

    private boolean isCloseBy(double latGuess, double lonGuess) {
        Location deviceLocation = locationReceiver.getLocation();

        Location guessedLocation = new Location("");
        guessedLocation.setLatitude(latGuess);
        guessedLocation.setLongitude(lonGuess);

        return deviceLocation.distanceTo(guessedLocation) <= FILTERED_DISTANCE;
    }
}
