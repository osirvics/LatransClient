package com.example.victor.latrans.repocitory.remote.api;


import android.arch.lifecycle.LiveData;

import com.example.victor.latrans.google.ApiResponse;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.repocitory.local.model.ConversationResponse;
import com.example.victor.latrans.repocitory.local.model.MessageResponse;
import com.example.victor.latrans.repocitory.local.model.NewUser;
import com.example.victor.latrans.repocitory.local.model.Oauth;
import com.example.victor.latrans.repocitory.local.model.Registration;
import com.example.victor.latrans.repocitory.local.model.Token;
import com.example.victor.latrans.repocitory.local.model.TripResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Victor on 09/08/2017.
 */

public interface APIService {
    @GET("login")
    Call<NewUser> getLogInCredentials();

    @GET("trips")
    LiveData<ApiResponse<TripResponse>> getTrips();

    @GET("users/{user_id}/conversations")
    LiveData<ApiResponse<ConversationResponse>> getConversations(@Path("user_id") long user_id);

    @GET("users/{user_id}/messages")
    LiveData<ApiResponse<MessageResponse>> getMessages(@Path("user_id") long user_id);


    @POST("items")
    Call<Message> postItems(@Body Message item);

    @Headers({"Accept: application/json"})
    @POST("users/new")
    Call<NewUser> createUser(@Body Registration data);


    @POST("oauth/{provider}")
    Call<Token> postOauthCode(@Body Oauth oauth, @Path("provider") String user);
}
