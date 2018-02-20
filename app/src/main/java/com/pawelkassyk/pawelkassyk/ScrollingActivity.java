package com.pawelkassyk.pawelkassyk;

import android.Manifest;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScrollingActivity extends AppCompatActivity implements Callback<List<ProjectDto>> {

    static final String BASE_URL = "https://raw.githubusercontent.com/pawelkassyk/";

    private RemoteApi remoteApi;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        initViews();
        initiateRemoteApi();
        callRemoteApi();
    }

    private void initViews() {
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
                dialPhone();
            }
        });

        TextView mailButton = findViewById(R.id.mail);
        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });

        progressBar = findViewById(R.id.loadingBar);
    }

    private void sendMail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:pkassyk@gmail.com"));
        startActivity(emailIntent);
    }

    private void dialPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:727926473"));
        if (isDialPermissionRequired()) {
            requestDialPermission();
            return;
        }
        startActivity(intent);
    }

    private void requestDialPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE}, 1);
    }

    private boolean isDialPermissionRequired() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED;
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
            openLinkedIn();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openLinkedIn() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/pawe%C5%82-kassyk-859a2653/"));
        startActivity(browserIntent);
    }

    public void callRemoteApi() {
        Call<List<ProjectDto>> call = remoteApi.getProjects();
        call.enqueue(this);
    }

    private void initiateRemoteApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        remoteApi = retrofit.create(RemoteApi.class);
    }

    public void displayProjects(List<ProjectDto> projectsList) {
        progressBar.setVisibility(View.GONE);
        ProjectsListAdapter adapter = new ProjectsListAdapter(projectsList, this);
        ListView projectsListView = findViewById(R.id.projectsList);
        projectsListView.setAdapter(adapter);
    }

    public void displayProjectsError() {
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
        });
    }

    @Override
    public void onResponse(Call<List<ProjectDto>> call, Response<List<ProjectDto>> response) {
        if (response.isSuccessful()) {
            List<ProjectDto> projects = response.body();
            displayProjects(projects);
        } else {
            displayProjectsError();
        }
    }

    @Override
    public void onFailure(Call<List<ProjectDto>> call, Throwable t) {
        displayProjectsError();
    }
}
