/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.Conexion.interfaces;
import static com.dom.ws.rest.bot.DAO.chatDAO.log;
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

    private static final String SQL_READMANY = "SELECT * FROM dommapi.project WHERE [user] = ? ";
    private final conexionBD con = conexionBD.saberEstado();
    static final Logger log = Logger.getLogger(projectsDAO.class.getName());

    @Override
    public boolean create(projectDTO dto) {
        return false;
    }

    @Override
    public boolean update(projectDTO dto) {
        return false;
    }

    @Override
    public boolean delete(projectDTO dto) {
        return false;
    }

    @Override
    public projectDTO readOne(projectDTO dto) {
        projectDTO resp = new projectDTO();
        return resp;
    }

    @Override
    public List<projectDTO> readMany(projectDTO dto) {
        log.info("***start projectDAO readMany***");
        PreparedStatement ps;
        ResultSet res;
        ArrayList<projectDTO> resp = new ArrayList();
        projectDTO objDto = new projectDTO();
        msgError error = new msgError();
        int i = 0;
        try {
            ps = con.getCnn().prepareStatement(SQL_READMANY);
            ps.setString(1, dto.getUser());
            res = ps.executeQuery();

            while (res.next()) {
                resp.add(new projectDTO(res.getInt(1), res.getString(2), res.getString(3), res.getTimestamp(4), res.getInt(5), res.getTimestamp(6)));
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
        List<projectDTO> resp = new ArrayList();
        return resp;
    }
}
