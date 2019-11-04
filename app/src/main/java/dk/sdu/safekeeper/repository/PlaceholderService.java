package dk.sdu.safekeeper.repository;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PlaceholderService {

    @POST("api/values")
    Call<ServerResponse> postsomeStuff(@Body PlaceholderData data);
}
