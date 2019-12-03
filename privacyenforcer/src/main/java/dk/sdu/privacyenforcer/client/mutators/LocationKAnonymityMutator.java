package dk.sdu.privacyenforcer.client.mutators;

import android.location.Location;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import dk.sdu.privacyenforcer.client.mutators.k_anonymity_client.AnonymizedLocation;
import dk.sdu.privacyenforcer.client.mutators.k_anonymity_client.KAnonymityClient;
import dk.sdu.privacyenforcer.client.mutators.k_anonymity_client.KAnonymityService;
import dk.sdu.privacyenforcer.client.mutators.k_anonymity_client.OriginalLocation;
import retrofit2.Response;

public class LocationKAnonymityMutator extends AbstractLocationMutator {

    private static final int K = 4;
    private static final double DX = 0.1;
    private static final double DY = 0.1;
    private KAnonymityService anonymityService;

    public LocationKAnonymityMutator() {
        anonymityService = KAnonymityClient.getService();
    }

    @Override
    Location getHiddenLocation(double lat, double lon) {
        OriginalLocation originalLocation = new OriginalLocation(UUID.randomUUID().toString(),
                lat, lon, K, DX, DY, new Date().getTime());

        try {
            Response<AnonymizedLocation> response = anonymityService.anonymize(originalLocation).execute();

            if (response.isSuccessful()) {
                AnonymizedLocation kAnonymityArea = response.body();
                assert kAnonymityArea != null;

                return getRandomLocationIn(kAnonymityArea.getLatMin(),
                        kAnonymityArea.getLatMax(),
                        kAnonymityArea.getLonMin(),
                        kAnonymityArea.getLonMax());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationFrom(lat, lon);
    }

    private Location getRandomLocationIn(double latMin, double latMax, double lonMin, double lonMax) {
        double lat = Math.random() * (latMax - latMin) + latMin;
        double lon = Math.random() * (lonMax - lonMin) + lonMin;
        return locationFrom(lat, lon);
    }
}
