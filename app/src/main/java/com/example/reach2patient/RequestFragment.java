package com.example.reach2patient;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class RequestFragment extends Fragment implements AdapterView.OnItemSelectedListener {

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

        Spinner recov = view.findViewById(R.id.rrecov_spinner);
        ArrayAdapter<CharSequence> radapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.request_recovery, android.R.layout.simple_spinner_item);
        radapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recov.setAdapter(radapter);

        Spinner bgroup = view.findViewById(R.id.rbgroup_spinner);
        ArrayAdapter<CharSequence> badapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.request_bgroup, android.R.layout.simple_spinner_item);
        radapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bgroup.setAdapter(badapter);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
