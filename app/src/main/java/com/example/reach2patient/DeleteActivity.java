package com.example.reach2patient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeleteActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DeleteActivity";
    private static final String BASE_URL = "http://192.168.1.7:8000/login/";

    private EditText phoneET, hospitalET, hcityET, dateET;
    private Spinner bloodGroupSp;

    private String hospital = null, hcity = null, date = null, bloodGroup = null;
    private long phone;

    private Reach2PatientApi r2pApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bloodGroupSp = findViewById(R.id.delbgroup_spinner);
        ArrayAdapter<CharSequence> badapter = ArrayAdapter.createFromResource(Objects.requireNonNull(this), R.array.delete_bgroup, android.R.layout.simple_spinner_item);
        badapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSp.setAdapter(badapter);

        phoneET = findViewById(R.id.delete_phone);
        hospitalET = findViewById(R.id.delete_hname);
        hcityET = findViewById(R.id.delete_hcity);
        dateET = findViewById(R.id.delete_date);

        Button submit = findViewById(R.id.delete_submit);
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
            phone = Long.parseLong(phoneET.getText().toString());
            hospital = hospitalET.getText().toString();
            hcity = hcityET.getText().toString();
            date = dateET.getText().toString();
            bloodGroup = bloodGroupSp.getSelectedItem().toString();

            boolean status = deleteRequest();

            if (status){
                clearForm();
                Toast.makeText(this, "Request created", Toast.LENGTH_SHORT).show();
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

    private boolean deleteRequest() {
        if (isNetworkConnected()){
            Delete delete = new Delete(hospital, bloodGroup, phone, hcity, date);

            Call<Delete> call = r2pApi.submitDeleteRequest(delete);

            call.enqueue(new Callback<Delete>() {
                @Override
                public void onResponse(Call<Delete> call, Response<Delete> response) {

                    if (!response.isSuccessful()){
                        Log.e(TAG, "onResponse: Error: " + response.raw());
                        return;
                    }

                    Delete delResponse = response.body();

                    Log.d(TAG, "Post Id: " + delResponse.getId());
                }

                @Override
                public void onFailure(Call<Delete> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
            return true;
        }
        return false;
    }

    private void clearForm(){
        phoneET.setText(null);
        hospitalET.setText(null);
        hcityET.setText(null);
        dateET.setText(null);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
