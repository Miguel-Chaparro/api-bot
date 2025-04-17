package com.dom.ws.rest.bot.service.interfaces;

import com.dom.ws.rest.bot.DTO.ProfileDTO;
import com.dom.ws.rest.bot.DTO.UserDTO;
import java.util.List;

public interface UserService extends BaseService<UserDTO> {
    boolean exists(String id);
    List<ProfileDTO> findByProfile(String profileId);
    boolean assignProfile(String userId, int profileId);
    boolean isAdmin(String userId);
}