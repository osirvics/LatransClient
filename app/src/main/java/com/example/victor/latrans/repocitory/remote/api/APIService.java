package com.example.victor.latrans.repocitory.remote.api;


import android.arch.lifecycle.LiveData;

import com.example.victor.latrans.google.ApiResponse;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.repocitory.local.db.entity.Request;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.repocitory.local.model.ConversationResponse;
import com.example.victor.latrans.repocitory.local.model.MessageResponse;
import com.example.victor.latrans.repocitory.local.model.NewUser;
import com.example.victor.latrans.repocitory.local.model.Oauth;
import com.example.victor.latrans.repocitory.local.model.Profile;
import com.example.victor.latrans.repocitory.local.model.Registration;
import com.example.victor.latrans.repocitory.local.model.RequestResponse;
import com.example.victor.latrans.repocitory.local.model.SingleMessageResponse;
import com.example.victor.latrans.repocitory.local.model.Token;
import com.example.victor.latrans.repocitory.local.model.TripResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;



public interface APIService {
    @GET("login")
    Call<NewUser> getLogInCredentials();

    @GET("trips")
    LiveData<ApiResponse<TripResponse>> getTrips();

    @Headers({"Accept: application/json"})
    @POST("trips/{user_id}/new")
    LiveData<ApiResponse<Trip>> postTrips(@Body Trip trip, @Path("user_id") long user_id);

    @GET("trips/{user_id}/users'")
    LiveData<ApiResponse<TripResponse>> getTripsForUser(@Path("user_id") long user_id);


    @GET("conversations/{conversation_id}/messages")
    LiveData<ApiResponse<MessageResponse>> getMessagesInConversation(@Path("conversation_id") long conversation_id);

    @GET("users/{user_id}/messages")
    LiveData<ApiResponse<MessageResponse>> getMessages(@Path("user_id") long user_id);

    @Headers({"Accept: application/json"})
    @PUT("users/{user_id}/modify")
    LiveData<ApiResponse<NewUser>> updateUser(@Body Profile profile, @Path("user_id") long user_id);

    @Headers({"Accept: application/json"})
    @POST("messages/send")
    LiveData<ApiResponse<SingleMessageResponse>> postMessage(@Body Message message);

    @Headers({"Accept: application/json"})
    @POST("users/new")
    Call<NewUser> createUser(@Body Registration data);

    @GET("users/{user_id}/conversations")
    LiveData<ApiResponse<ConversationResponse>> getConversations(@Path("user_id") long user_id);

    @GET("requests")
    LiveData<ApiResponse<RequestResponse>> getRequests();

    @Headers({"Accept: application/json"})
    @POST("requests/{user_id}/new")
    LiveData<ApiResponse<Request>> postResquest(@Path("user_id") long user_id, @Body Request request);

    @GET("requests/{user_id}/users")
    LiveData<ApiResponse<RequestResponse>> getRequestForUser(@Path("user_id") long user_id);

    @POST("oauth/{provider}")
    Call<Token> postOauthCode(@Body Oauth oauth, @Path("provider") String user);
}
