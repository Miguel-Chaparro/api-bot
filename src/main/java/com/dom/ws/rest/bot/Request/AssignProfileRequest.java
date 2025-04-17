package com.dom.ws.rest.bot.Request;

public class AssignProfileRequest {
    private String userId;
    private int profileId;

    public AssignProfileRequest() {
    }

    public AssignProfileRequest(String userId, int profileId) {
        this.userId = userId;
        this.profileId = profileId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }
}