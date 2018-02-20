package com.pawelkassyk.pawelkassyk;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RemoteApi {

    @GET("Rest-end-points/master/projects_list.json")
    Call<List<ProjectDto>> getProjects();
}
