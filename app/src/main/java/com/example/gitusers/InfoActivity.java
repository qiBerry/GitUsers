package com.example.gitusers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class InfoActivity extends AppCompatActivity {
    //Initialization of ui`s variables
    TextView tvInfoMain, tvInfoLogin;
    ImageView ivInfoMain;
    ProgressBar pbInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //Association ui`s elements with variables
        tvInfoLogin = (TextView) findViewById(R.id.tv_info_login);
        tvInfoMain = (TextView) findViewById(R.id.tv_info_main);
        ivInfoMain = (ImageView) findViewById(R.id.iv_info_main);
        pbInfo = (ProgressBar) findViewById(R.id.pb_info);

        //Accepting user`s login from card
        String userLogin = (String) getIntent().getSerializableExtra("userLogin");

        GetAsyncTask getAsyncTask = new GetAsyncTask();
        getAsyncTask.execute(userLogin);
    }
    //GetAsyncTask download full data about user
    class GetAsyncTask extends AsyncTask<String, Void, Void> {
        private User user;
        public GetAsyncTask(){ }
        @Override
        protected Void doInBackground(String... strings) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Settings.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            GitInterface service = retrofit.create(GitInterface.class);
            Call<User> call = service.getUserData(strings[0]);
            try {
                Response<User> userResponse = call.execute();
                user = userResponse.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tvInfoMain.setText(user.toString());
            tvInfoLogin.setText(user.getLogin());
            DownloadImageTask downloadImageTask = new DownloadImageTask();
            downloadImageTask.execute(user.getAvatarUrl());

        }
    }

    //DownloadImageTask download user`s avatar
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            pbInfo.setVisibility(View.GONE);
            ivInfoMain.setImageBitmap(result);
        }
    }
}
