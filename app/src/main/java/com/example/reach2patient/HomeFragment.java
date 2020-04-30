package com.example.reach2patient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    private static final String BASE_URL = "http://192.168.1.7:8000/login/";

    private Reach2PatientApi r2pApi;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_home);
        startAsyncTask();
        return view;
    }

    public void startAsyncTask(){
        RestAPIAsyncTask task = new RestAPIAsyncTask(this);
        task.execute();
    }

    private static class RestAPIAsyncTask extends AsyncTask<Void, Integer, ArrayList<RecyclerItem>>{
        private WeakReference<HomeFragment> fragmentWeakReference;

        RestAPIAsyncTask(HomeFragment fragment){
            fragmentWeakReference = new WeakReference<HomeFragment>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            HomeFragment fragment = fragmentWeakReference.get();
            if (fragment == null) {
                return;
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            fragment.r2pApi = retrofit.create(Reach2PatientApi.class);
        }

        @Override
        protected ArrayList<RecyclerItem> doInBackground(Void... voids) {
            HomeFragment fragment = fragmentWeakReference.get();
            ArrayList<RecyclerItem> items = fragment.getPosts();
            return items;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            HomeFragment fragment = fragmentWeakReference.get();

            if (fragment == null) {
                return;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<RecyclerItem> items) {
            super.onPostExecute(items);

            HomeFragment fragment = fragmentWeakReference.get();

            if (isCancelled() || fragment == null) {
                return;
            }

//            if (items.isEmpty()){
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date();
//                String time = formatter.format(date);
//                items.add(new RecyclerItem("No Posts available yet!", time, "contact.reach2patient@gmail.com"));
//            }

            fragment.recyclerView.setHasFixedSize(true);
            fragment.layoutManager = new LinearLayoutManager(fragment.getContext());
            fragment.adapter = new RecyclerAdapter(items);

            fragment.recyclerView.setLayoutManager(fragment.layoutManager);
            fragment.recyclerView.setAdapter(fragment.adapter);

            //updateProgressBar

        }

        @Override
        protected void onCancelled(ArrayList<RecyclerItem> items) {
            super.onCancelled(items);

            HomeFragment fragment = fragmentWeakReference.get();

            if (fragment == null) {
                return;
            }
        }

    }

    public ArrayList<RecyclerItem> getPosts(){
        ArrayList<RecyclerItem> items = new ArrayList<>();

        Call<List<Post>> call = r2pApi.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()){
                    Log.e(TAG, "onResponse: Error: " + response.raw());
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts){
                    Log.d(TAG, "Post Id: " + post.getId());

                    //publishProgress
                    String date = null;
                    String timestamp = post.getTime();
                    String dateTime[] = timestamp.split("T");
                    String time = dateTime[1].substring(0, dateTime[1].length() - 1);
                    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        date = formatter2.format(formatter1.parse(dateTime[0]));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String parsedTime = date + " " + time;

                    items.add(new RecyclerItem(post.getBody(), parsedTime, String.valueOf(post.getPhone())));
                }

                Log.d(TAG, "Item count: " + items.size());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

        return items;
    }

    @Override
    public void onClick(View view) {

    }

}
