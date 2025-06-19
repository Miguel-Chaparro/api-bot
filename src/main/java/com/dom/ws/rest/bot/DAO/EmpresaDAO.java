package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.Conexion.interfaces;
import com.dom.ws.rest.bot.DTO.EmpresaDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmpresaDAO implements interfaces<EmpresaDTO> {
    private static final String SQL_INSERT = "INSERT INTO dommapi.empresa (nombre, nit, direccion, telefono, email, estado, numero_chatbot) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE dommapi.empresa SET nombre=?, nit=?, direccion=?, telefono=?, email=?, estado=?, numero_chatbot=? WHERE id=?";
    private static final String SQL_DELETE = "DELETE FROM dommapi.empresa WHERE id=?";
    private static final String SQL_GET_ALL = "SELECT * FROM dommapi.empresa";
    private static final String SQL_GET_ONE = "SELECT * FROM dommapi.empresa WHERE id=?";

    private final conexionBD con = conexionBD.saberEstado();
    static final Logger log = Logger.getLogger(EmpresaDAO.class.getName());

    @Override
    public boolean create(EmpresaDTO dto) {
        log.info("*** Start EmpresaDAO create ***");
        PreparedStatement ps;
        boolean success = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT);
            ps.setString(1, dto.getNombre());
            ps.setString(2, dto.getNit());
            ps.setString(3, dto.getDireccion());
            ps.setString(4, dto.getTelefono());
            ps.setString(5, dto.getEmail());
            ps.setInt(6, dto.getEstado());
            ps.setString(7, dto.getNumeroChatbot());
            int result = ps.executeUpdate();
            success = result > 0;
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error creating empresa {0}", ex);
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End EmpresaDAO create ***");
        return success;
    }

    @Override
    public boolean update(EmpresaDTO dto) {
        log.info("*** Start EmpresaDAO update ***");
        PreparedStatement ps;
        boolean success = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(1, dto.getNombre());
            ps.setString(2, dto.getNit());
            ps.setString(3, dto.getDireccion());
            ps.setString(4, dto.getTelefono());
            ps.setString(5, dto.getEmail());
            ps.setInt(6, dto.getEstado());
            ps.setString(7, dto.getNumeroChatbot());
            ps.setInt(8, dto.getId());
            int result = ps.executeUpdate();
            success = result > 0;
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error updating empresa {0}", ex);
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End EmpresaDAO update ***");
        return success;
    }

    @Override
    public boolean delete(EmpresaDTO dto) {
        log.info("*** Start EmpresaDAO delete ***");
        PreparedStatement ps;
        boolean success = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setInt(1, dto.getId());
            int result = ps.executeUpdate();
            success = result > 0;
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error deleting empresa {0}", ex);
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End EmpresaDAO delete ***");
        return success;
    }

    @Override
    public EmpresaDTO readOne(EmpresaDTO dto) {
        log.info("*** Start EmpresaDAO readOne ***");
        EmpresaDTO empresa = null;
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = con.getCnn().prepareStatement(SQL_GET_ONE);
            ps.setInt(1, dto.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                empresa = new EmpresaDTO(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("nit"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getInt("estado"),
                    rs.getString("numero_chatbot")
                );
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error reading empresa {0}", ex);
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End EmpresaDAO readOne ***");
        return empresa;
    }

    @Override
    public List<EmpresaDTO> readMany(EmpresaDTO dto) {
        return new ArrayList<>(); // Implementar si se necesita
    }

    @Override
    public List<EmpresaDTO> readAll() {
        log.info("*** Start EmpresaDAO readAll ***");
        List<EmpresaDTO> empresas = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = con.getCnn().prepareStatement(SQL_GET_ALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                EmpresaDTO empresa = new EmpresaDTO(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("nit"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getInt("estado"),
                    rs.getString("numero_chatbot")
                );
                empresas.add(empresa);
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error getting all empresas", ex);
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End EmpresaDAO readAll ***");
        return empresas;
    }
}
