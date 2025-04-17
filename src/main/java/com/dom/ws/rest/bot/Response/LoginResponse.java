package com.dom.ws.rest.bot.Response;

import com.dom.ws.rest.bot.DTO.ProfileDTO;
import com.dom.ws.rest.bot.vo.msgError;
import java.util.List;

public class LoginResponse {
    private boolean isNewUser;
    private boolean created;
    private List<ProfileDTO> profiles;
    private msgError error;

    public LoginResponse() {
    }

    public LoginResponse(boolean isNewUser, boolean created, List<ProfileDTO> profiles) {
        this.isNewUser = isNewUser;
        this.created = created;
        this.profiles = profiles;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public List<ProfileDTO> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ProfileDTO> profiles) {
        this.profiles = profiles;
    }

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }
}