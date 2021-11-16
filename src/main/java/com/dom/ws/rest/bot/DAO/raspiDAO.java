/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.Conexion.interfaces;
import static com.dom.ws.rest.bot.DAO.raspiDAO.log;
import com.dom.ws.rest.bot.DTO.raspiDTO;
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
 * @author MIGUEL
 */
public class raspiDAO implements interfaces<raspiDTO> {

    private static final String SQL_READONE = "SELECT * FROM raspi WHERE idRaspi = ? ";
    private static final String SQL_UPDATE = "UPDATE raspi SET nodo = ? "
            + "WHERE idRaspi = ? AND ip = ?";
    private static final String SQL_INSERT = "INSERT INTO raspi (idRaspi, ip, nodo, idGroup) "
            + "VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE = "SELECT * FROM raspi WHERE idRaspi = ?";
    private static final String SQL_READMANY = "SELECT * FROM answer WHERE idQuestion = ? ";
    private static final String SQL_READMANYUSER = "";
    private static final String SQL_READALL = "SELECT * FROM customerWhatsapp";
    msgError error = new msgError();
    private final conexionBD con = conexionBD.saberEstado();
    static final Logger log = Logger.getLogger(raspiDAO.class.getName());

    @Override
    public boolean create(raspiDTO dto) {
        log.info("*** Start raspiDAO create ***");
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT);
            ps.setString(1, dto.getRaspi());
            ps.setString(2, dto.getIp());
            ps.setString(3, dto.getNodeIp());
            ps.setString(4, dto.getGroupId());
            int i = 0;
            i = ps.executeUpdate();

            if (i > 0) {
                valida = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create raspiDAO {0}", ex);

        } finally {
            con.cerrarConexion();
        }
        log.info("*** end raspiDAO create ***");
        return valida;
    }

    @Override
    public boolean update(raspiDTO dto) {
        log.info("*** Start raspiDAO update ***");
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(2, dto.getRaspi());
            ps.setString(3, dto.getIp());
            ps.setString(1, dto.getNodeIp());
            int i = 0;
            i = ps.executeUpdate();

            if (i > 0) {
                valida = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create raspiDAO {0}", ex);

        } finally {
            con.cerrarConexion();
        }
        log.info("*** end raspiDAO update ***");
        return valida;
    }

    @Override
    public boolean delete(raspiDTO dto) {
        return false;
    }

    @Override
    public raspiDTO readOne(raspiDTO dto) {
        return null;
    }

    @Override
    public List<raspiDTO> readMany(raspiDTO dto) {
        log.info("*** end raspiDAO readMany ***");
        PreparedStatement ps;
        ResultSet res;
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        ArrayList<raspiDTO> raspiList = new ArrayList();
        raspiDTO raspiVal = new raspiDTO();
        try {
            ps = con.getCnn().prepareStatement(SQL_READMANY);
            ps.setString(1, dto.getRaspi());
            res = ps.executeQuery();
            int i = 0;
            while (res.next()) {
                raspiList.add(new raspiDTO(res.getString(1), res.getString(2),
                        res.getString(3), res.getString(4)));
                i++;
                if (i > 0) {
                    error.setCode(0);
                    error.setMessage("");
                } else {
                    error.setCode(11);
                    error.setMessage("Error no data found");
                }
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create raspiDAO {0}", ex);
            error.setCode(-1);
            error.setMessage("Error: " + ex);

        } finally {
            con.cerrarConexion();
        }
        raspiVal.setError(error);
        raspiList.add(raspiVal);
        return raspiList;
    }

    @Override
    public List<raspiDTO> readAll() {
        return null;
    }

}
