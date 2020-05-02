package com.example.reach2patient;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Reach2PatientApi {

    @Headers("Authorization: Token 9b1155fbd0d5db4f5a21dd013e8882ab0ee41cd7")
    @GET("posts/{id}/")
    Call<List<Post>> getPosts(@Path("id") Integer id);

    @Headers("Authorization: Token 9b1155fbd0d5db4f5a21dd013e8882ab0ee41cd7")
    @POST("posts/")
    Call<Post> createPost(@Body Post post);

    @Headers("Authorization: Token 9b1155fbd0d5db4f5a21dd013e8882ab0ee41cd7")
    @POST("donations/")
    Call<Donate> submitDonateForm(@Body Donate formData);

    @Headers("Authorization: Token 9b1155fbd0d5db4f5a21dd013e8882ab0ee41cd7")
    @POST("requests/")
    Call<Request> submitRequestForm(@Body Request formData);

    @Headers("Authorization: Token 9b1155fbd0d5db4f5a21dd013e8882ab0ee41cd7")
    @POST("tests/")
    Call<Test> submitTestForm(@Body Test formData);

    @Headers("Authorization: Token 9b1155fbd0d5db4f5a21dd013e8882ab0ee41cd7")
    @POST("deletions/")
    Call<Delete> submitDeleteRequest(@Body Delete formData);

}
