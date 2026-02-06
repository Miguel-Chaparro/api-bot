/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.Conexion.interfaces;
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

    // private static final String SQL_READONE = "SELECT * FROM dommapi.raspi WHERE
    // idRaspi = ? ";
    private static final String SQL_UPDATE = "UPDATE raspi SET idRaspi = ?, ip = ?, nodo = ?, idGroup = ?, topic = ?, id_channel = ?, id_chat = ?, flg_main = ?, desc_device = ? "
            + "WHERE id_device = ?";
    private static final String SQL_INSERT = "INSERT INTO dommapi.raspi (idRaspi, ip, nodo, idGroup, topic, id_channel, id_chat, flg_main, desc_device) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    // private static final String SQL_DELETE = "SELECT * FROM dommapi.raspi WHERE
    // idRaspi = ?";
    private static final String SQL_READMANY = "SELECT * FROM dommapi.raspi WHERE idRaspi = ? ";
    private static final String SQL_READMANY_CHAT = "SELECT * FROM dommapi.raspi WHERE id_chat = ? ";
    /*
     * private static final String SQL_READMANYUSER = "";
     * private static final String SQL_READALL = "SELECT * FROM customerWhatsapp";
     */
    msgError error = new msgError();
       /**
     * Helper method to get fresh connection for each operation
     */
    private conexionBD getConnection() {
        return conexionBD.saberEstado();
    }

    conexionBD con = getConnection();
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
            ps.setString(5, dto.getTopic());
            ps.setInt(6, dto.getIdChannel());
            ps.setString(7, dto.getIdChat());
            ps.setInt(8, dto.getFlgMain());
            ps.setString(9, dto.getDescDevices());
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
            ps.setString(1, dto.getRaspi());
            ps.setString(2, dto.getIp());
            ps.setString(3, dto.getNodeIp());
            ps.setString(4, dto.getGroupId());
            ps.setString(5, dto.getTopic());
            ps.setInt(6, dto.getIdChannel());
            ps.setString(7, dto.getIdChat());
            ps.setInt(8, dto.getFlgMain());
            ps.setString(9, dto.getDescDevices());
            ps.setInt(10, dto.getIdDevices());
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
        ArrayList<raspiDTO> raspiList = new ArrayList<>();
        raspiDTO raspiVal = new raspiDTO();
        int i = 0;
        try {

            if (dto.getRaspi() != null) {
                ps = con.getCnn().prepareStatement(SQL_READMANY);
                ps.setString(1, dto.getRaspi());
            } else {
                ps = con.getCnn().prepareStatement(SQL_READMANY_CHAT);
                ps.setString(1, dto.getIdChat());
            }
            res = ps.executeQuery();

            while (res.next()) {
                raspiList.add(new raspiDTO(res.getString(1), res.getString(2),
                        res.getString(3), res.getString(4), res.getString(5), res.getInt(6), res.getString(7),
                        res.getInt(8), res.getInt(9), res.getString(10)));
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
            log.log(Level.SEVERE, "Error readMany raspiDAO {0}", ex);
            error.setCode(-1);
            error.setMessage("Error: " + ex);

        } finally {
            con.cerrarConexion();
        }
        if (i == 0) {
            raspiVal.setError(error);
            raspiList.add(raspiVal);
        } else {
            raspiVal = raspiList.get(0);
            raspiVal.setError(error);
            raspiList.set(0, raspiVal);
        }

        return raspiList;
    }

    @Override
    public List<raspiDTO> readAll() {
        return null;
    }

}
