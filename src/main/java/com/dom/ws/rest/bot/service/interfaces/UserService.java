package com.dom.ws.rest.bot.service.interfaces;

import com.dom.ws.rest.bot.DTO.ProfileDTO;
import com.dom.ws.rest.bot.DTO.UserDTO;
import com.dom.ws.rest.bot.Response.LoginResponse;
import com.google.firebase.auth.FirebaseToken;

import java.util.List;

public interface UserService extends BaseService<UserDTO> {
    boolean exists(String id);
    List<ProfileDTO> findByProfile(String profileId);
    void assignProfiles(String userId, List<Integer> profileIds);
    boolean isAdmin(String userId);
    LoginResponse login(FirebaseToken decodedToken);
    UserDTO createUser(UserDTO newUser, String creatorId);
    UserDTO updateUser(UserDTO userToUpdate, String updaterId);
    List<UserDTO> getUsers(String requesterId);
}