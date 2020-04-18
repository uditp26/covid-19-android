package com.example.reach2patient;

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

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PostActivity";

    private EditText nameET, emailET, postET, phoneET, cityET, stateET, countryET;
    private Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nameET = findViewById(R.id.post_name);
        emailET = findViewById(R.id.post_email);
        postET = findViewById(R.id.post_content);
        phoneET = findViewById(R.id.post_phone);
        cityET = findViewById(R.id.post_city);
        stateET = findViewById(R.id.post_state);
        countryET = findViewById(R.id.post_country);

        submit = findViewById(R.id.post_submit);
        submit.setOnClickListener(this);
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
        String post = null, name = null, email = null, city = null, state = null, country = null;
        long phone;

        try {
            post = postET.getText().toString();
            name = nameET.getText().toString();
            email = emailET.getText().toString();
            phone = Long.parseLong(phoneET.getText().toString());
            city = cityET.getText().toString();
            state = stateET.getText().toString();
            country = countryET.getText().toString();
        }
        catch (Exception e){
            Log.e(TAG, "onClick: " + e.getMessage());
            Toast.makeText(this, "Missing field value", Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "onClick: " + name);

    }
}
