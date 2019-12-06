package dk.sdu.privacyenforcer.client.mutators.k_anonymity_client;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface KAnonymityService {

    /**
     * Get an anonymous area from the K-anonymity server, given an original location fix.
     *
     * @param location the original location fix
     * @return the anonymous area
     */
    @POST("anonymity")
    Call<AnonymizedLocation> anonymize(@Body OriginalLocation location);
}
