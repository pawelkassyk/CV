package com.pawelkassyk.pawelkassyk;

public class ProjectDto {

    private String name;

    private String githubUrl;

    private String playStoreUrl;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getPlayStoreUrl() {
        return playStoreUrl;
    }

    public void setPlayStoreUrl(String playStoreUrl) {
        this.playStoreUrl = playStoreUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
