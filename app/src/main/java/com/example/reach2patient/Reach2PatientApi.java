package com.example.reach2patient;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Reach2PatientApi {

    @GET("posts/")
    Call<List<Post>> getPosts();

    @POST("posts/")
    Call<Post> createPost(@Body Post post);

    @POST("donations/")
    Call<Donate> submitDonateForm(@Body Donate formData);

    @POST("requests/")
    Call<Request> submitRequestForm(@Body Request formData);

    @POST("tests/")
    Call<Test> submitTestForm(@Body Test formData);

    @POST("deletions/")
    Call<Delete> submitDeleteRequest(@Body Delete formData);

}
