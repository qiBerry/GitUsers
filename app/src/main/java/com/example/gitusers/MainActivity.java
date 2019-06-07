package com.example.gitusers;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import java.io.IOException;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted{
    //Initialization of ui`s variables
    RecyclerView rv_main;
    UserCardAdapter adapter;
    ProgressBar pb_main;
    ArrayList<User> userList = new ArrayList<User>();
    ImageButton ibMainLeftPage, ibMainRightPage;

    //Statics is needed to realize pagination, because not all ids of users is used
    //Current page number
    public static int pageNum = 0;
    //Saves startId of page to have an abbility to backpage
    static ArrayList<Integer> startPageIds = new ArrayList<Integer>();
    //Saves endId to show next page
    static int endPageId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Association ui`s elements with variables
        pb_main = (ProgressBar) findViewById(R.id.pb_main);
        ibMainLeftPage = (ImageButton) findViewById(R.id.bt_main_left_page);
        ibMainLeftPage.setVisibility(View.INVISIBLE);
        ibMainLeftPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageNum > 0) {
                    pageNum--;
                    getPrimary(startPageIds.get(pageNum), true);
                }
            }
        });
        ibMainRightPage = (ImageButton) findViewById(R.id.bt_main_right_page);
        ibMainRightPage.setVisibility(View.INVISIBLE);
        ibMainRightPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                getPrimary(endPageId, false);
            }
        });
        rv_main = (RecyclerView) findViewById(R.id.rv_main);

        //Initialization of recycleview object
        rv_main.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewMargin decoration = new RecyclerViewMargin(20, 1);
        rv_main.addItemDecoration(decoration);
        adapter = new UserCardAdapter(this, userList);
        rv_main.setAdapter(adapter);

        //if application has been opened right now
        if (startPageIds.size() == 0)
            startPageIds.add(0);

        //Downloading and making list of users
        getPrimary(endPageId, false);
    }

    //Func, that download and make primary list of users from git
    public void getPrimary(final int since, final boolean isBack) {
        class GetAsyncTask extends AsyncTask<Integer, ArrayList<User>, Void> {
            private OnTaskCompleted listener;

            public GetAsyncTask(OnTaskCompleted listener){
                this.listener = listener;
            }
            @Override
            protected Void doInBackground(Integer... integers) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Settings.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                GitInterface service = retrofit.create(GitInterface.class);


                //If page new, adds startId
                if ((pageNum != 0)&&(!isBack) && (startPageIds.size() < pageNum)) {
                    startPageIds.add(userList.get(0).getId() - 1);
                }

                Call<ArrayList<User>> call = service.getUsersSinceId(since);
                try {
                    Response<ArrayList<User>> userResponse = call.execute();
                    userList = userResponse.body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                listener.OnTaskCompleted();
            }
        }
        GetAsyncTask getAsyncTask = new GetAsyncTask(MainActivity.this);
        getAsyncTask.execute(since);
    }

    @Override
    public void OnTaskCompleted() {
        pb_main.setVisibility(View.GONE);
        ibMainLeftPage.setVisibility(View.VISIBLE);
        ibMainRightPage.setVisibility(View.VISIBLE);
        adapter = new UserCardAdapter(this, userList);
        rv_main.setAdapter(adapter);
        endPageId = userList.get(userList.size() - 1).getId();
    }

}