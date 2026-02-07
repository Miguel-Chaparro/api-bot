package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.Conexion.interfaces;
import com.dom.ws.rest.bot.DTO.UserDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO implements interfaces<UserDTO> {
    private static final String SQL_INSERT = "INSERT INTO dommapi.users (id, email, display_name, photo_url, phone_number, provider_id, creation_time, last_sign_in_time, email_verified, custom_claims, disabled, empresa_id, id_raspi, created_by, plan_internet, plan_paneles, ppoe, ciudad, departamento, direccion, tipo_identificacion, numero_identificacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE dommapi.users SET email=?, display_name=?, photo_url=?, phone_number=?, provider_id=?, update_time=?, last_sign_in_time=?, email_verified=?, custom_claims=?, disabled=?, empresa_id=?, id_raspi=?, created_by=?, plan_internet=?, plan_paneles=?, ppoe=?, ciudad=?, departamento=?, direccion=?, tipo_identificacion=?, numero_identificacion=? WHERE id=?";
    private static final String SQL_EXISTS = "SELECT COUNT(*) FROM dommapi.users WHERE id = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM dommapi.users ORDER BY creation_time DESC";
    
    static final Logger log = Logger.getLogger(UserDAO.class.getName());

    /**
     * Helper method to get fresh connection for each operation
     * Returns null if connection pool timeout - caller must check for null
     */
    private conexionBD getConnection() {
        try {
            return conexionBD.saberEstado();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Connection pool timeout - all connections busy: {0}", ex.getMessage());
            return null;  // Signal that connection couldn't be obtained
        }
    }

    public boolean exists(String id) {
        log.info("*** Start UserDAO exists ***");
        conexionBD con = getConnection();
        if (con == null) {
            log.severe("Cannot connect to database - pool exhausted");
            return false;
        }
        
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
        } catch (Exception ex) {
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
        conexionBD con = getConnection();
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
            ps.setString(13, dto.getIdRaspi());
            ps.setString(14, dto.getCreatedBy());
            ps.setString(15, dto.getPlanInternet());
            ps.setString(16, dto.getPlanPaneles());
            ps.setString(17, dto.getPpoe());
            ps.setString(18, dto.getCiudad());
            ps.setString(19, dto.getDepartamento());
            ps.setString(20, dto.getDireccion());
            ps.setString(21, dto.getTipoIdentificacion());
            ps.setString(22, dto.getNumeroIdentificacion());
            
            int result = ps.executeUpdate();
            success = result > 0;
            
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error creating user {0}", ex);
        } finally {
            con.cerrarConexion();
        }
        
        log.info("*** End UserDAO create ***");
        return success;
    }

    @Override
    public boolean update(UserDTO dto) {
        log.info("*** Start UserDAO update ***");
        conexionBD con = getConnection();
        PreparedStatement ps;
        boolean success = false;
        
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(1, dto.getEmail());
            ps.setString(2, dto.getDisplayName());
            ps.setString(3, dto.getPhotoUrl());
            ps.setString(4, dto.getPhoneNumber());
            ps.setString(5, dto.getProviderId());
            ps.setTimestamp(6, dto.getCreationTime());
            ps.setTimestamp(7, dto.getLastSignInTime());
            ps.setBoolean(8, dto.isEmailVerified());
            ps.setString(9, dto.getCustomClaims());
            ps.setBoolean(10, dto.isDisabled());
            ps.setObject(11, dto.getEmpresaId());
            ps.setString(12, dto.getIdRaspi());
            ps.setString(13, dto.getCreatedBy());
            ps.setString(14, dto.getPlanInternet());
            ps.setString(15, dto.getPlanPaneles());
            ps.setString(16, dto.getPpoe());
            ps.setString(17, dto.getCiudad());
            ps.setString(18, dto.getDepartamento());
            ps.setString(19, dto.getDireccion());
            ps.setString(20, dto.getTipoIdentificacion());
            ps.setString(21, dto.getNumeroIdentificacion());
            ps.setString(22, dto.getId());
            
            int result = ps.executeUpdate();
            success = result > 0;
            
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error updating user {0}", ex);
        } finally {
            con.cerrarConexion();
        }
        
        log.info("*** End UserDAO update ***");
        return success;
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
        conexionBD con = getConnection();
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
                user.setIdRaspi(rs.getString("id_raspi"));
                user.setCreatedBy(rs.getString("created_by"));
                user.setPlanInternet(rs.getString("plan_internet"));
                user.setPlanPaneles(rs.getString("plan_paneles"));
                user.setPpoe(rs.getString("ppoe"));
                user.setCiudad(rs.getString("ciudad"));
                user.setDepartamento(rs.getString("departamento"));
                user.setDireccion(rs.getString("direccion"));
                user.setTipoIdentificacion(rs.getString("tipo_identificacion"));
                user.setNumeroIdentificacion(rs.getString("numero_identificacion"));
                users.add(user);
            }
        } catch (Exception ex) {
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
        conexionBD con = getConnection();
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
                user.setIdRaspi(rs.getString("id_raspi"));
                user.setCreatedBy(rs.getString("created_by"));
                user.setPlanInternet(rs.getString("plan_internet"));
                user.setPlanPaneles(rs.getString("plan_paneles"));
                user.setPpoe(rs.getString("ppoe"));
                user.setCiudad(rs.getString("ciudad"));
                user.setDepartamento(rs.getString("departamento"));
                user.setDireccion(rs.getString("direccion"));
                user.setTipoIdentificacion(rs.getString("tipo_identificacion"));
                user.setNumeroIdentificacion(rs.getString("numero_identificacion"));
                // salir del while
                return user;
            }
        } catch (Exception ex) {
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
        conexionBD con = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.getCnn().prepareStatement("SELECT * FROM dommapi.users WHERE empresa_id = ? ORDER BY creation_time DESC");
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
                user.setIdRaspi(rs.getString("id_raspi"));
                user.setCreatedBy(rs.getString("created_by"));
                user.setPlanInternet(rs.getString("plan_internet"));
                user.setPlanPaneles(rs.getString("plan_paneles"));
                user.setPpoe(rs.getString("ppoe"));
                user.setCiudad(rs.getString("ciudad"));
                user.setDepartamento(rs.getString("departamento"));
                user.setDireccion(rs.getString("direccion"));
                user.setTipoIdentificacion(rs.getString("tipo_identificacion"));
                user.setNumeroIdentificacion(rs.getString("numero_identificacion"));
                users.add(user);
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error getting users by empresa_id", ex);
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End UserDAO readAllByEmpresaId ***");
        return users;
    }
}