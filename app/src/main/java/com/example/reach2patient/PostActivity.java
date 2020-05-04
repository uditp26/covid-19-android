package com.example.reach2patient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PostActivity";
    private static final String BASE_URL = "http://192.168.1.7:8000/login/";

    private EditText emailET, postET, phoneET, cityET, stateET, countryET;

    private String body = null, email = null, city = null, state = null, country = null;
    private long phone;

    private Reach2PatientApi r2pApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        emailET = findViewById(R.id.post_email);
        postET = findViewById(R.id.post_content);
        phoneET = findViewById(R.id.post_phone);
        cityET = findViewById(R.id.post_city);
        stateET = findViewById(R.id.post_state);
        countryET = findViewById(R.id.post_country);

        Button submit = findViewById(R.id.post_submit);
        submit.setOnClickListener(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        r2pApi = retrofit.create(Reach2PatientApi.class);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {

        try {
            body = postET.getText().toString();
            if(emailET.getText() == null)
                email = "";
            else
                email = emailET.getText().toString();
            phone = Long.parseLong(phoneET.getText().toString());
            city = cityET.getText().toString();
            state = stateET.getText().toString();
            country = countryET.getText().toString();

            boolean status = createPost();

            if (status){
                clearForm();
                Toast.makeText(this, "Post created", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Check network connection", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Log.e(TAG, "onClick: " + e.getMessage());
            Toast.makeText(this, "Missing field value", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean createPost() {
        if (isNetworkConnected()){
            Post post = new Post(body, email, phone, city, state, country);

            Call<Post> call = r2pApi.createPost(post);

            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {

                    if (!response.isSuccessful()){
                        Log.e(TAG, "onResponse: Error: " + response.raw());
                        Toast.makeText(emailET.getRootView().getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Post postResponse = response.body();

                    Log.d(TAG, "Post Id: " + postResponse.getId());
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(emailET.getRootView().getContext(), "Server Unavailable", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }
        return false;
    }

    private void clearForm(){
        emailET.setText(null);
        cityET.setText(null);
        phoneET.setText(null);
        postET.setText(null);
        stateET.setText(null);
        countryET.setText(null);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
