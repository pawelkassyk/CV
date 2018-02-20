package com.pawelkassyk.pawelkassyk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<ProjectDto> projectsList;
    private Context context;

    public MyAdapter(List<ProjectDto> projectsList, Context context) {
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

        ProjectDto project = projectsList.get(position);

        TextView name = view.findViewById(R.id.projectName);
        name.setText(project.getName());

        TextView description = view.findViewById(R.id.projectDescription);
        description.setText(project.getDescription());

        TextView githubUrl = view.findViewById(R.id.githubUrl);
        githubUrl.setText(project.getGithubUrl());

        return view;
    }
}