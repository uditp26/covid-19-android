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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TestActivity";

    private static final String BASE_URL = "http://185.201.9.188:81/C19/login/";

    private EditText ageET, phoneET, cityET;

    private int age;
    private String city = null;
    private long phone;

    private Reach2PatientApi r2pApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ageET = findViewById(R.id.test_age);
        phoneET = findViewById(R.id.test_phone);
        cityET = findViewById(R.id.test_city);

        Button submit = findViewById(R.id.test_submit);
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
            age = Integer.parseInt(ageET.getText().toString());
            phone = Long.parseLong(phoneET.getText().toString());
            city = cityET.getText().toString();

            boolean status = submitTestForm();

            if (status){
                clearForm();
                Toast.makeText(this, "Request submitted", Toast.LENGTH_SHORT).show();
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

    private boolean submitTestForm(){
        if (isNetworkConnected()){
            Test test = new Test(age, phone, city);

            Call<Test> call = r2pApi.submitTestForm(test);

            call.enqueue(new Callback<Test>() {
                @Override
                public void onResponse(Call<Test> call, Response<Test> response) {

                    if (!response.isSuccessful()){
                        Log.e(TAG, "onResponse: Error: " + response.raw());
                        Toast.makeText(ageET.getRootView().getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Test testResponse = response.body();

                    Log.d(TAG, "Post Id: " + testResponse.getId());
                }

                @Override
                public void onFailure(Call<Test> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(ageET.getRootView().getContext(), "Server Unavailable", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }
        return false;
    }

    private void clearForm(){
        ageET.setText(null);
        cityET.setText(null);
        phoneET.setText(null);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
