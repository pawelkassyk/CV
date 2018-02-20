package com.pawelkassyk.pawelkassyk;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScrollingActivity extends AppCompatActivity implements Callback<List<ProjectDto>> {
    // todo: handle failed respose (with retry)
    // todo: style list view + add play store links
    // todo: remove unsued menu
    // todo: add projects
    // todo: add website
    // todo: add play store dev account link
    // todo: add api call
    // todo: write espresso test via DSL designed for CV app
    // todo: add splash screen
    // todo: add app icon
    // todo: release
    // todo: refactor java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/pawelkassyk"));
                startActivity(browserIntent);
            }
        });

        TextView phoneButton = findViewById(R.id.phone);
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });

        TextView mailButton = findViewById(R.id.mail);
        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });

        callRemoteApi();
    }

    private void sendMail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:pkassyk@gmail.com"));
        startActivity(emailIntent);
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:727926473"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
            return;
        }
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Żadna aplikacja nie może obsłużyć wykonania połączenia", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_linkedin) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/pawe%C5%82-kassyk-859a2653/"));
            startActivity(browserIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    static final String BASE_URL = "https://raw.githubusercontent.com/pawelkassyk/";

    public void callRemoteApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RemoteApi remoteApi = retrofit.create(RemoteApi.class);

        Call<List<ProjectDto>> call = remoteApi.getProjects();
        call.enqueue(this);
    }

    public void onSuccess(List<ProjectDto> projectsList) {
        ProgressBar progressBar = findViewById(R.id.loadingBar);
        progressBar.setVisibility(View.GONE);
        MyAdapter adapter = new MyAdapter(projectsList, this);
        ListView projectsListView = findViewById(R.id.projectsList);
        projectsListView.setAdapter(adapter);
    }

    public void displayFailed() {
        final ProgressBar progressBar = findViewById(R.id.loadingBar);
        progressBar.setVisibility(View.GONE);
        final Button retryButton = findViewById(R.id.retryButton);
        retryButton.setVisibility(View.VISIBLE);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.GONE);
                callRemoteApi();
            }
        }) ;
    }

    @Override
    public void onResponse(Call<List<ProjectDto>> call, Response<List<ProjectDto>> response) {
        if (response.isSuccessful()) {
            List<ProjectDto> projects = response.body();
            onSuccess(projects);
        } else {
            displayFailed();
        }
    }

    @Override
    public void onFailure(Call<List<ProjectDto>> call, Throwable t) {
        displayFailed();
    }
}
