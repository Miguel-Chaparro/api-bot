package com.dom.ws.rest.bot.service.impl;

import com.dom.ws.rest.bot.DAO.UserDAO;
import com.dom.ws.rest.bot.DAO.ProfileDAO;
import com.dom.ws.rest.bot.DTO.ProfileDTO;
import com.dom.ws.rest.bot.DTO.UserDTO;
import com.dom.ws.rest.bot.service.interfaces.UserService;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final ProfileDAO profileDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAO();
        this.profileDAO = new ProfileDAO();
    }

    @Override
    public UserDTO create(UserDTO entity) {
        boolean created = userDAO.create(entity);
        if (created) {
            return entity;
        }
        return null;
    }

    @Override
    public UserDTO update(UserDTO entity) {
        boolean updated = userDAO.update(entity);
        if (updated) {
            return entity;
        }
        return null;
    }

    @Override
    public void delete(String id) {
        UserDTO dto = new UserDTO();
        dto.setId(id);
        userDAO.delete(dto);
    }

    @Override
    public UserDTO findById(String id) {
        UserDTO dto = new UserDTO();
        dto.setId(id);
        return userDAO.readOne(dto);
    }

    @Override
    public List<UserDTO> findAll() {
        return userDAO.readAll();
    }

    @Override
    public boolean exists(String id) {
        return userDAO.exists(id);
    }

    public List<UserDTO> findByProfile(int profileId) {
        // TODO: Implementar método para buscar usuarios por perfil
        return null;
    }

    @Override
    public boolean assignProfile(String userId, int profileId) {
        return profileDAO.assignProfileToUser(userId, profileId);
    }

    @Override
    public boolean isAdmin(String userId) {
        return profileDAO.isUserAdmin(userId);
    }

    @Override
    public List<ProfileDTO> findByProfile(String profileId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByProfile'");
    }
}