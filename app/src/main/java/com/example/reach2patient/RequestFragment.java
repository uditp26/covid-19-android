package com.example.reach2patient;

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

public class RequestFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = "RequestFragment";

    private EditText ageET, phoneET, cityET, hphoneET, hemailET;
    private Spinner bloodGroupSp, recoveryStatusSp;
    private Button submit;

    public static RequestFragment newInstance(){
        RequestFragment fragment = new RequestFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
        radapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSp.setAdapter(badapter);

        ageET = view.findViewById(R.id.request_age);
        cityET = view.findViewById(R.id.request_city);
        phoneET = view.findViewById(R.id.request_phone);
        hphoneET = view.findViewById(R.id.request_hphone);
        hemailET = view.findViewById(R.id.request_hemail);

        submit = view.findViewById(R.id.request_submit);
        submit.setOnClickListener(this);

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
        int age;
        long phone, hphone;
        String hemail = null, city = null, bloodGroup = null, recoveryStatusText = null;

        short recoveryStatus;

        try {
            age = Integer.parseInt(ageET.getText().toString());
            phone = Long.parseLong(phoneET.getText().toString());
            hphone = Long.parseLong(hphoneET.getText().toString());
            hemail = hemailET.getText().toString();
            city = cityET.getText().toString();
            bloodGroup = bloodGroupSp.getSelectedItem().toString();

            recoveryStatusText  = recoveryStatusSp.getSelectedItem().toString();

            if (recoveryStatusText.compareTo("Recovered patient") == 0){
                recoveryStatus = 1;
            }
            else{
                recoveryStatus = 0;
            }
        }
        catch (Exception e){
            Log.e(TAG, "onClick: " + e.getMessage());
            Toast.makeText(getActivity(), "Missing field value", Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "onClick: " + city);

    }
}
