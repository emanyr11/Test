package com.GetMeThere.GetMeThereLogin;

import com.GetMeThere.GetMeThereLogin.models.ServerRequest;
import com.GetMeThere.GetMeThereLogin.models.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("index.php/")
    Call<ServerResponse> operation(@Body ServerRequest request);

}
