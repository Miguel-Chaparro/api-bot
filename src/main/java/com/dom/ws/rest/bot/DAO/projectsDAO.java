/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.Conexion.interfaces;
import com.dom.ws.rest.bot.DTO.projectDTO;
import com.dom.ws.rest.bot.vo.msgError;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel Ch
 */
public class projectsDAO implements interfaces<projectDTO> {

    private static final String SQL_READMANY = "SELECT * FROM dommapi.project WHERE idUser = ? ";
    private static final String SQL_READ_VALIDATE = "SELECT * FROM dommapi.project WHERE idUser = ? AND projectDesc = ? AND idFrom = ?";
    private static final String SQL_READ = "SELECT * FROM dommapi.project WHERE idFrom = ? AND projectDesc = ? ";
    private static final String SQL_READ_ID = "SELECT * FROM dommapi.project WHERE idproject = ?";
    private static final String SQL_CREATE = "INSERT INTO dommapi.project (idUser ,projectDesc ,dateProject ,openProject ,endProject ,statusProject ,flgEndProject, idFrom) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE dommapi.project SET idUser = ?,projectDesc  = ?,dateProject  = ?,openProject  = ?,endProject  = ?,statusProject  = ?,flgEndProject = ? "
            + "WHERE idproject = ? AND idFrom = ?";
    private final conexionBD con = conexionBD.saberEstado();
    static final Logger log = Logger.getLogger(projectsDAO.class.getName());

    @Override
    public boolean create(projectDTO dto) {
        log.info("*** Start projectsDAO create ***");
        boolean status = false;
        PreparedStatement ps;
        try {
            ps = con.getCnn().prepareStatement(SQL_CREATE);
            ps.setString(1, dto.getIdUser());
            ps.setString(2, dto.getProjectDesc());
            ps.setTimestamp(3, dto.getDateProject());
            ps.setInt(4, dto.getOpenProject());
            ps.setTimestamp(5, dto.getEndProject());
            ps.setInt(6, dto.getStatusProject());
            ps.setInt(7, dto.getFlgEndProject());
            ps.setString(8, dto.getIdFrom());
            int i = 0;
            i = ps.executeUpdate();

            if (i > 0) {
                status = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create projectsDAO {0}", ex);
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End projectsDAO create ***");
        return status;
    }

    @Override
    public boolean update(projectDTO dto) {
        log.info("*** Start projectsDAO Update ***");
        boolean status = false;
        PreparedStatement ps;
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(1, dto.getIdUser());
            ps.setString(2, dto.getProjectDesc());
            ps.setTimestamp(3, dto.getDateProject());
            ps.setInt(4, dto.getOpenProject());
            ps.setTimestamp(5, dto.getEndProject());
            ps.setInt(6, dto.getStatusProject());
            ps.setInt(7, dto.getFlgEndProject());
            ps.setInt(8, dto.getIdProject());
            ps.setString(9, dto.getIdFrom());
            int i = 0;
            i = ps.executeUpdate();

            if (i > 0) {
                status = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error update projectsDAO {0}", ex);
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End projectsDAO update ***");
        return status;
    }

    @Override
    public boolean delete(projectDTO dto) {
        return false;
    }

    @Override
    public projectDTO readOne(projectDTO dto) {
        log.info("*** Start projectsDAO readOne ***");
        projectDTO resp = new projectDTO();
        PreparedStatement ps;
        ResultSet res;
        msgError error = new msgError();

        try {
            if (dto.getTokenId() == "1") {
                ps = con.getCnn().prepareStatement(SQL_READ);                
                ps.setString(1, dto.getIdFrom());
                ps.setString(2, dto.getProjectDesc());
            } else if (dto.getTokenId() == "2"){
                ps = con.getCnn().prepareStatement(SQL_READ_ID);
                ps.setInt(1, dto.getIdProject());
            } else {
                ps = con.getCnn().prepareStatement(SQL_READ_VALIDATE);
                ps.setString(1, dto.getIdUser());
                ps.setString(2, dto.getProjectDesc());
                ps.setString(3, dto.getIdFrom());
            }

            res = ps.executeQuery();
            int i = 0;
            while (res.next()) {
                resp = new projectDTO(res.getInt(2), res.getString(3), res.getString(4), res.getTimestamp(5),
                        res.getInt(6), res.getTimestamp(7), res.getInt(8), res.getInt(9), res.getString(1));
                i++;
            }
            if (i == 0) {
                log.info("-----Concidencias 0");
                error.setCode(1);
                error.setMessage("No hay coincidencias");
                resp.setIdProject(1);
            } else {
                log.info("-----Concidencias 1");
                error.setCode(0);
                error.setMessage("Ok");
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create customerWhatsappDTO {0}", ex);
            error.setCode(-1);
            error.setMessage("Error: " + ex);
        } finally {
            con.cerrarConexion();
        }
        resp.setError(error);
        log.info("*** End projectsDAO readOne ***");
        return resp;
    }

    @Override
    public List<projectDTO> readMany(projectDTO dto) {
        log.info("***start projectDAO readMany***");
        PreparedStatement ps;
        ResultSet res;
        ArrayList<projectDTO> resp = new ArrayList<>();
        projectDTO objDto = new projectDTO();
        msgError error = new msgError();
        int i = 0;
        try {
            ps = con.getCnn().prepareStatement(SQL_READMANY);
            ps.setString(1, dto.getIdUser());
            res = ps.executeQuery();

            while (res.next()) {
                resp.add(new projectDTO(res.getInt(2), res.getString(3), res.getString(4), res.getTimestamp(5),
                        res.getInt(6), res.getTimestamp(7), res.getInt(8), res.getInt(9), res.getString(1)));
                i++;

            }
            if (i > 0) {
                error.setCode(0);
                error.setMessage("");
            } else {
                error.setCode(11);
                error.setMessage("Error no data found");

            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create customerWhatsappDTO {0}", ex);
            error.setCode(ex.getErrorCode());
            error.setMessage("Error ORA: " + ex);

        } finally {
            con.cerrarConexion();
        }
        if (i == 0) {
            objDto.setError(error);
            resp.add(objDto);
        } else {
            objDto = resp.get(0);
            objDto.setError(error);
            resp.set(0, objDto);
        }
        log.info("***End projectDAO readMany***");
        return resp;
    }

    @Override
    public List<projectDTO> readAll() {
        log.info("***start projectsDAO readAll***");
        PreparedStatement ps;
        ResultSet res;
        ArrayList<projectDTO> resp = new ArrayList<>();
        msgError error = new msgError();
        int i = 0;
        try {
            ps = con.getCnn().prepareStatement("SELECT * FROM dommapi.project");
            res = ps.executeQuery();
            while (res.next()) {
                resp.add(new projectDTO(res.getInt(2), res.getString(3), res.getString(4), res.getTimestamp(5),
                        res.getInt(6), res.getTimestamp(7), res.getInt(8), res.getInt(9), res.getString(1)));
                i++;
            }
            if (i > 0) {
                error.setCode(0);
                error.setMessage("");
            } else {
                error.setCode(11);
                error.setMessage("Error no data found");
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error projectsDAO readAll {0}", ex);
            error.setCode(ex.getErrorCode());
            error.setMessage("Error ORA: " + ex);
        } finally {
            con.cerrarConexion();
        }
        if (i == 0) {
            projectDTO objDto = new projectDTO();
            objDto.setError(error);
            resp.add(objDto);
        } else {
            projectDTO objDto = resp.get(0);
            objDto.setError(error);
            resp.set(0, objDto);
        }
        log.info("***End projectsDAO readAll***");
        return resp;
    }
}
