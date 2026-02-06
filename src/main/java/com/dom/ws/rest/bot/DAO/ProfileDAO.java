package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.DTO.ProfileDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileDAO {
    private static final String SQL_GET_USER_PROFILES = "SELECT p.* FROM dommapi.profiles p INNER JOIN dommapi.user_profiles up ON p.id = up.profile_id WHERE up.user_id = ? AND p.active = true";
    private static final String SQL_INSERT_USER_PROFILE = "INSERT INTO dommapi.user_profiles (user_id, profile_id) VALUES (?, ?)";
    private static final String SQL_IS_ADMIN = "SELECT COUNT(*) FROM dommapi.profiles p INNER JOIN dommapi.user_profiles up ON p.id = up.profile_id WHERE up.user_id = ? AND p.name = 'Administrador' AND p.active = true";
    
       /**
     * Helper method to get fresh connection for each operation
     */
    private conexionBD getConnection() {
        return conexionBD.saberEstado();
    }  

    conexionBD con = getConnection();
    static final Logger log = Logger.getLogger(ProfileDAO.class.getName());
    
    public List<ProfileDTO> getUserProfiles(String userId) {
        List<ProfileDTO> profiles = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = con.getCnn().prepareStatement(SQL_GET_USER_PROFILES);
            ps.setString(1, userId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ProfileDTO profile = new ProfileDTO();
                profile.setId(rs.getInt("id"));
                profile.setName(rs.getString("name"));
                profile.setDescription(rs.getString("description"));
                profile.setActive(rs.getBoolean("active"));
                profiles.add(profile);
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error getting user profiles", ex);
        } finally {
            con.cerrarConexion();
        }
        
        return profiles;
    }
    
    public boolean assignProfileToUser(String userId, int profileId) {
        PreparedStatement ps = null;
        boolean success = false;
        
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT_USER_PROFILE);
            ps.setString(1, userId);
            ps.setInt(2, profileId);
            
            success = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error assigning profile to user", ex);
        } finally {
            con.cerrarConexion();
        }
        
        return success;
    }

    public boolean isUserAdmin(String userId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isAdmin = false;
        
        try {
            ps = con.getCnn().prepareStatement(SQL_IS_ADMIN);
            ps.setString(1, userId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                isAdmin = rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error checking if user is admin", ex);
        } finally {
            con.cerrarConexion();
        }
        
        return isAdmin;
    }

    public List<ProfileDTO> getAllActiveProfiles() {
        List<ProfileDTO> profiles = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.getCnn().prepareStatement("SELECT * FROM dommapi.profiles WHERE active = true");
            rs = ps.executeQuery();
            while (rs.next()) {
                ProfileDTO profile = new ProfileDTO();
                profile.setId(rs.getInt("id"));
                profile.setName(rs.getString("name"));
                profile.setDescription(rs.getString("description"));
                profile.setActive(rs.getBoolean("active"));
                profiles.add(profile);
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error getting all active profiles", ex);
        } finally {
            con.cerrarConexion();
        }
        return profiles;
    }

    public List<ProfileDTO> getActiveProfilesByDescription(String description) {
        List<ProfileDTO> profiles = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.getCnn().prepareStatement("SELECT * FROM dommapi.profiles WHERE active = true AND description = ?");
            ps.setString(1, description);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProfileDTO profile = new ProfileDTO();
                profile.setId(rs.getInt("id"));
                profile.setName(rs.getString("name"));
                profile.setDescription(rs.getString("description"));
                profile.setActive(rs.getBoolean("active"));
                profiles.add(profile);
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error getting active profiles by description", ex);
        } finally {
            con.cerrarConexion();
        }
        return profiles;
    }

    public boolean deleteAllProfilesForUser(String userId) {
        PreparedStatement ps = null;
        boolean success = false;
        try {
            ps = con.getCnn().prepareStatement("DELETE FROM dommapi.user_profiles WHERE user_id = ?");
            ps.setString(1, userId);
            success = ps.executeUpdate() >= 0;
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error deleting user profiles", ex);
        } finally {
            con.cerrarConexion();
        }
        return success;
    }
}