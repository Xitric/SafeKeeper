package dk.sdu.privacyenforcer.client.mutators.k_anonymity_client;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface KAnonymityService {

    @POST("anonymity")
    Call<AnonymizedLocation> anonymize(@Body OriginalLocation location);
}
