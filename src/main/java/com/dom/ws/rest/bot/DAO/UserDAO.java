package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.Conexion.interfaces;
import com.dom.ws.rest.bot.DTO.UserDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO implements interfaces<UserDTO> {
    private static final String SQL_INSERT = "INSERT INTO dommapi.users (id, email, display_name, photo_url, phone_number, provider_id, creation_time, last_sign_in_time, email_verified, custom_claims, disabled, empresa_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_EXISTS = "SELECT COUNT(*) FROM dommapi.users WHERE id = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM dommapi.users";
    
    private final conexionBD con = conexionBD.saberEstado();
    static final Logger log = Logger.getLogger(UserDAO.class.getName());

    public boolean exists(String id) {
        log.info("*** Start UserDAO exists ***");
        PreparedStatement ps;
        ResultSet res;
        boolean exists = false;
        
        try {
            ps = con.getCnn().prepareStatement(SQL_EXISTS);
            ps.setString(1, id);
            res = ps.executeQuery();
            
            if (res.next()) {
                exists = res.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error checking if user exists {0}", ex);
        } finally {
            con.cerrarConexion();
        }
        
        log.info("*** End UserDAO exists ***");
        return exists;
    }

    @Override
    public boolean create(UserDTO dto) {
        log.info("*** Start UserDAO create ***");
        PreparedStatement ps;
        boolean success = false;
        
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT);
            ps.setString(1, dto.getId());
            ps.setString(2, dto.getEmail());
            ps.setString(3, dto.getDisplayName());
            ps.setString(4, dto.getPhotoUrl());
            ps.setString(5, dto.getPhoneNumber());
            ps.setString(6, dto.getProviderId());
            ps.setTimestamp(7, dto.getCreationTime());
            ps.setTimestamp(8, dto.getLastSignInTime());
            ps.setBoolean(9, dto.isEmailVerified());
            ps.setString(10, dto.getCustomClaims());
            ps.setBoolean(11, dto.isDisabled());
            ps.setObject(12, dto.getEmpresaId());
            
            int result = ps.executeUpdate();
            success = result > 0;
            
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error creating user {0}", ex);
        } finally {
            con.cerrarConexion();
        }
        
        log.info("*** End UserDAO create ***");
        return success;
    }

    @Override
    public boolean update(UserDTO dto) {
        return false; // Implementar si se necesita
    }

    @Override
    public boolean delete(UserDTO dto) {
        return false; // Implementar si se necesita
    }

    @Override
    public UserDTO readOne(UserDTO dto) {
        return null; // Implementar si se necesita
    }

    @Override
    public List<UserDTO> readMany(UserDTO dto) {
        return new ArrayList<>(); // Implementar si se necesita
    }

    @Override
    public List<UserDTO> readAll() {
        log.info("*** Start UserDAO readAll ***");
        List<UserDTO> users = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.getCnn().prepareStatement(SQL_GET_ALL);
            rs = ps.executeQuery();

            while (rs.next()) {
                UserDTO user = new UserDTO();
                user.setId(rs.getString("id"));
                user.setEmail(rs.getString("email"));
                user.setDisplayName(rs.getString("display_name"));
                user.setPhotoUrl(rs.getString("photo_url"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setProviderId(rs.getString("provider_id"));
                user.setCreationTime(rs.getTimestamp("creation_time"));
                user.setLastSignInTime(rs.getTimestamp("last_sign_in_time"));
                user.setEmailVerified(rs.getBoolean("email_verified"));
                user.setCustomClaims(rs.getString("custom_claims"));
                user.setDisabled(rs.getBoolean("disabled"));
                user.setEmpresaId((Integer)rs.getObject("empresa_id"));
                users.add(user);
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error getting all users", ex);
        } finally {
            con.cerrarConexion();
        }

        log.info("*** End UserDAO readAll ***");
        return users;
    }
    
    public UserDTO readOneById(String id) {
        log.info("*** Start UserDAO readOneById ***");
        UserDTO user = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.getCnn().prepareStatement("SELECT * FROM dommapi.users WHERE id = ?");
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = new UserDTO();
                user.setId(rs.getString("id"));
                user.setEmail(rs.getString("email"));
                user.setDisplayName(rs.getString("display_name"));
                user.setPhotoUrl(rs.getString("photo_url"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setProviderId(rs.getString("provider_id"));
                user.setCreationTime(rs.getTimestamp("creation_time"));
                user.setLastSignInTime(rs.getTimestamp("last_sign_in_time"));
                user.setEmailVerified(rs.getBoolean("email_verified"));
                user.setCustomClaims(rs.getString("custom_claims"));
                user.setDisabled(rs.getBoolean("disabled"));
                user.setEmpresaId((Integer)rs.getObject("empresa_id"));
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error reading user by id", ex);
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End UserDAO readOneById ***");
        return user;
    }

    public List<UserDTO> readAllByEmpresaId(Integer empresaId) {
        log.info("*** Start UserDAO readAllByEmpresaId ***");
        List<UserDTO> users = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.getCnn().prepareStatement("SELECT * FROM dommapi.users WHERE empresa_id = ?");
            ps.setObject(1, empresaId);
            rs = ps.executeQuery();
            while (rs.next()) {
                UserDTO user = new UserDTO();
                user.setId(rs.getString("id"));
                user.setEmail(rs.getString("email"));
                user.setDisplayName(rs.getString("display_name"));
                user.setPhotoUrl(rs.getString("photo_url"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setProviderId(rs.getString("provider_id"));
                user.setCreationTime(rs.getTimestamp("creation_time"));
                user.setLastSignInTime(rs.getTimestamp("last_sign_in_time"));
                user.setEmailVerified(rs.getBoolean("email_verified"));
                user.setCustomClaims(rs.getString("custom_claims"));
                user.setDisabled(rs.getBoolean("disabled"));
                user.setEmpresaId((Integer)rs.getObject("empresa_id"));
                users.add(user);
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error getting users by empresa_id", ex);
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End UserDAO readAllByEmpresaId ***");
        return users;
    }
}