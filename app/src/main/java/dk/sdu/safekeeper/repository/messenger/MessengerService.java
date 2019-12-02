package dk.sdu.safekeeper.repository.messenger;

import dk.sdu.safekeeper.repository.messenger.vo.MessengerLocationPayload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MessengerService {

    @POST("me")
    Call<MessengerLocationPayload> sendLocationData(@Body MessengerLocationPayload data);
}
