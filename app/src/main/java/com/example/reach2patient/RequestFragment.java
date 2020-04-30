package com.example.reach2patient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = "RequestFragment";

    private static final String BASE_URL = "http://192.168.1.7:8000/login/";

    private EditText ageET, phoneET, cityET, hphoneET, hemailET;
    private Spinner bloodGroupSp, recoveryStatusSp;

    private int age;
    private long phone, hphone;
    private String hemail = null;
    private String city = null;
    private String bloodGroup = null;

    private Boolean recoveryStatus;

    private Reach2PatientApi r2pApi;

    public static RequestFragment newInstance(){
        RequestFragment fragment = new RequestFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        recoveryStatusSp = view.findViewById(R.id.rrecov_spinner);
        ArrayAdapter<CharSequence> radapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.request_recovery, android.R.layout.simple_spinner_item);
        radapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recoveryStatusSp.setAdapter(radapter);

        bloodGroupSp = view.findViewById(R.id.rbgroup_spinner);
        ArrayAdapter<CharSequence> badapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.request_bgroup, android.R.layout.simple_spinner_item);
        badapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSp.setAdapter(badapter);

        ageET = view.findViewById(R.id.request_age);
        cityET = view.findViewById(R.id.request_city);
        phoneET = view.findViewById(R.id.request_phone);
        hphoneET = view.findViewById(R.id.request_hphone);
        hemailET = view.findViewById(R.id.request_hemail);

        Button submit = view.findViewById(R.id.request_submit);
        submit.setOnClickListener(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        r2pApi = retrofit.create(Reach2PatientApi.class);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {


        try {
            age = Integer.parseInt(ageET.getText().toString());
            phone = Long.parseLong(phoneET.getText().toString());
            hphone = Long.parseLong(hphoneET.getText().toString());
            hemail = hemailET.getText().toString();
            city = cityET.getText().toString();
            bloodGroup = bloodGroupSp.getSelectedItem().toString();

            String recoveryStatusText = recoveryStatusSp.getSelectedItem().toString();

            if (recoveryStatusText.compareTo("Recovered patient") == 0){
                recoveryStatus = true;
            }
            else{
                recoveryStatus = false;
            }

            boolean status = submitRequestForm();

            if (status){
                clearForm();
                Toast.makeText(getActivity(), "Request submitted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(), "Check network connection", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Log.e(TAG, "onClick: " + e.getMessage());
            Toast.makeText(getActivity(), "Missing field value", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean submitRequestForm(){
        if(isNetworkConnected()){
            Request formData = new Request(age, recoveryStatus, bloodGroup, phone, hphone, hemail, city);

            Call<Request> call = r2pApi.submitRequestForm(formData);

            call.enqueue(new Callback<Request>() {
                @Override
                public void onResponse(Call<Request> call, Response<Request> response) {

                    if (!response.isSuccessful()) {
                        Log.e(TAG, "onResponse: Error: " + response.raw());
                        return;
                    }

                    Request donateResponse = response.body();

                    Log.d(TAG, "RequestForm Id: " + donateResponse.getId());

                }

                @Override
                public void onFailure(Call<Request> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
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
        hphoneET.setText(null);
        hemailET.setText(null);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
