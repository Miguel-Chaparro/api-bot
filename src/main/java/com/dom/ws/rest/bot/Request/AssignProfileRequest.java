package com.dom.ws.rest.bot.Request;

import java.util.List;

public class AssignProfileRequest {
    private String userId;
    private List<Integer> profileIds;

    public AssignProfileRequest() {
    }

    public AssignProfileRequest(String userId, List<Integer> profileIds) {
        this.userId = userId;
        this.profileIds = profileIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Integer> getProfileIds() {
        return profileIds;
    }

    public void setProfileIds(List<Integer> profileIds) {
        this.profileIds = profileIds;
    }
}