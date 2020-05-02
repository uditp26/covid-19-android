package com.example.reach2patient;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "HomeFragment";
    private static final String BASE_URL = "http://185.201.9.188:81/C19/login/";

    private Retrofit retrofit;

    private Reach2PatientApi r2pApi;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<RecyclerItem> items;

    private TextView emptyView;

    private SwipeRefreshLayout refreshLayout;

    PostDBHelper postDBHelper;

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
        emptyView = view.findViewById(R.id.no_posts_view);
        items = new ArrayList<>();
        refreshLayout = view.findViewById(R.id.swipe_to_refresh);
        refreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        postDBHelper = new PostDBHelper(getContext());
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        r2pApi = retrofit.create(Reach2PatientApi.class);
        if(items.isEmpty()){
            disableRecycler();
        }
        else{
            enableRecycler();
        }
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(items);
        recyclerView.setAdapter(adapter);

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                startAsyncTask();
            }
        });

        return view;
    }

    public void startAsyncTask(){
        RestAPIAsyncTask task = new RestAPIAsyncTask(this);
        task.execute();
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "Layout refreshed!");
        startAsyncTask();
    }

    private static class RestAPIAsyncTask extends AsyncTask<Void, Void, Void>{
        private WeakReference<HomeFragment> fragmentWeakReference;

        RestAPIAsyncTask(HomeFragment fragment){
            fragmentWeakReference = new WeakReference<HomeFragment>(fragment);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HomeFragment fragment = fragmentWeakReference.get();
            fragment.getPostsFromServer();
            return null;
        }
    }

    public void getPostsFromServer(){
        refreshLayout.setRefreshing(true);

        ArrayList<RecyclerItem> localItems;
        int pid;

        localItems = fetchBackup();    // Check Local DB

        if(localItems.isEmpty()){        // Local DB is empty
            pid = 0;
        }
        else{
            pid = localItems.get(0).getId();
        }

        if(isNetworkConnected()){

            Call<List<Post>> call = r2pApi.getPosts(pid);

            call.enqueue(new Callback<List<Post>>() {
                ArrayList<RecyclerItem> freshItems;
                boolean flag = false;

                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    if (!response.isSuccessful()){
                        Log.e(TAG, "onResponse: Error: " + response.raw());
                        Toast.makeText(recyclerView.getRootView().getContext(), "Server refused connection", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Post> posts = response.body();

                    if (!posts.isEmpty()){      // New posts from server fetched
                        for (Post post : posts){
                            Log.d(TAG, "Post Id: " + post.getId());
                            String parsedTime = parseDateTime(post);
                            postDBHelper.backupPost(post.getId(), post.getBody(), Long.toString(post.getPhone()), parsedTime);     // Add the new fetched post to local DB
                        }
                        freshItems = fetchBackup();
                        flag = true;
                    }
                    if(flag){
                        Log.d(TAG, "New items fetched from server. Item count: " + freshItems.size());
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                enableRecycler();
                                adapter = new RecyclerAdapter(freshItems);
                                recyclerView.setAdapter(adapter);
                            }
                        });

                    }
                    else{
                        Log.d(TAG, "Using cached items from local DB. Item count: " + localItems.size());

                        if(localItems.isEmpty()){

                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    disableRecycler();
                                }
                            });

                        }
                        else {

                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    enableRecycler();
                                    adapter = new RecyclerAdapter(localItems);
                                    recyclerView.setAdapter(adapter);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(recyclerView.getRootView().getContext(), "Server Unavailable", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{
            if (pid == 0){
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        disableRecycler();
                    }
                });
            }
            else{
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        enableRecycler();
                        adapter = new RecyclerAdapter(localItems);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }

        refreshLayout.setRefreshing(false);
    }

    private String parseDateTime(Post post){
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
        return date + " " + time;
    }

    private ArrayList<RecyclerItem> fetchBackup(){
        ArrayList<RecyclerItem> items = new ArrayList<>();
        Cursor cursor = postDBHelper.getPostsFromBackup();
        try {
            while (cursor.moveToNext()) {
                int pid = cursor.getInt(cursor.getColumnIndex(PostDBHelper.BACKUP_COLUMN_PID));
                String post = cursor.getString(cursor.getColumnIndex(PostDBHelper.BACKUP_COLUMN_BODY));
                String timestamp = cursor.getString(cursor.getColumnIndex(PostDBHelper.BACKUP_COLUMN_TIMESTAMP));
                String contact = cursor.getString(cursor.getColumnIndex(PostDBHelper.BACKUP_COLUMN_PHONE));
                RecyclerItem item = new RecyclerItem(pid, post, timestamp, contact);
                items.add(item);
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void enableRecycler(){
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    private void disableRecycler(){
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {

    }

}
