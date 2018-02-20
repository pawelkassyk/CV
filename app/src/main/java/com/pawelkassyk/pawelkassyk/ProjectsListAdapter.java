package com.pawelkassyk.pawelkassyk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ProjectsListAdapter extends BaseAdapter {
    private List<ProjectDto> projectsList;
    private Context context;

    public ProjectsListAdapter(List<ProjectDto> projectsList, Context context) {
        this.projectsList = projectsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return projectsList.size();
    }

    @Override
    public Object getItem(int i) {
        return projectsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_element, container, false);
        }
        final ProjectDto project = projectsList.get(position);

        TextView nameView = view.findViewById(R.id.projectName);
        TextView descriptionView = view.findViewById(R.id.projectDescription);
        TextView githubUrlView = view.findViewById(R.id.githubUrl);
        TextView playStoreView = view.findViewById(R.id.playstoreUrl);

        nameView.setText(project.getName());
        descriptionView.setText(project.getDescription());
        githubUrlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebsite(project.getGithubUrl());
            }
        });
        playStoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebsite(project.getPlayStoreUrl());
            }
        });

        if (isEmpty(project.getGithubUrl())) {
            githubUrlView.setVisibility(View.GONE);
        }
        if (isEmpty(project.getPlayStoreUrl())) {
            playStoreView.setVisibility(View.GONE);
        }

        return view;
    }

    private void openWebsite(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    private boolean isEmpty(String string) {
        return string == null || string.equals("");
    }
}