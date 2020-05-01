package com.example.reach2patient;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Reach2PatientApi {

    @Headers("Authorization: Token b148f948dffafb61a9f5421335e41a6f39dcd91d")
    @GET("posts/{id}/")
    Call<List<Post>> getPosts(@Path("id") Integer id);

    @Headers("Authorization: Token b148f948dffafb61a9f5421335e41a6f39dcd91d")
    @POST("posts/")
    Call<Post> createPost(@Body Post post);

    @Headers("Authorization: Token b148f948dffafb61a9f5421335e41a6f39dcd91d")
    @POST("donations/")
    Call<Donate> submitDonateForm(@Body Donate formData);

    @Headers("Authorization: Token b148f948dffafb61a9f5421335e41a6f39dcd91d")
    @POST("requests/")
    Call<Request> submitRequestForm(@Body Request formData);

    @Headers("Authorization: Token b148f948dffafb61a9f5421335e41a6f39dcd91d")
    @POST("tests/")
    Call<Test> submitTestForm(@Body Test formData);

    @Headers("Authorization: Token b148f948dffafb61a9f5421335e41a6f39dcd91d")
    @POST("deletions/")
    Call<Delete> submitDeleteRequest(@Body Delete formData);

}
