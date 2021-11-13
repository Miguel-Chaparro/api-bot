/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.Conexion.interfaces;
import com.dom.ws.rest.bot.DTO.answerDTO;
import com.dom.ws.rest.bot.vo.msgError;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author MIGUEL
 */
public class answerDAO implements interfaces<answerDTO> {

    private static final String SQL_READONE = "SELECT * FROM answer WHERE idQuestion = ? ";
    private static final String SQL_UPDATE = "UPDATE answer SET answerCode = ?, answerdes = ?, "
            + "pendingState = ?, pendingDescription = ? "
            + "WHERE idQuestion = ?";
    private static final String SQL_INSERT = "INSERT INTO answer (idQuestion, answerCode, answerdes) "
            + "VALUES (?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM customerWhatsapp WHERE idWhatsapp = ?";
    private static final String SQL_READMANY = "SELECT * FROM answer WHERE idQuestion = ? ";
    private static final String SQL_READMANYUSER = "";
    private static final String SQL_READALL = "SELECT * FROM customerWhatsapp";
    msgError error = new msgError();
    private final conexionBD con = conexionBD.saberEstado();
    static Logger log = Logger.getLogger(answerDAO.class);

    @Override
    public boolean create(answerDTO dto) {
        log.info("*** Start answerDAO create ***");
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT);
            ps.setString(1, dto.getIdQuestion());
            ps.setInt(2, dto.getAnswerId());
            ps.setString(3, dto.getAnswerDesc());
            int i = 0;
            i = ps.executeUpdate();

            if (i > 0) {
                valida = true;
            }
        } catch (SQLException ex) {
            log.error("Error create customerWhatsappDTO " + ex);

        } finally {
            con.cerrarConexion();
        }
        log.info("*** end answerDAO create ***");
        return valida;
    }

    @Override
    public boolean update(answerDTO dto) {
        log.info("*** Start answerDAO update ***");
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(3, dto.getIdQuestion());
            ps.setInt(1, dto.getAnswerId());
            ps.setString(2, dto.getAnswerDesc());
            int i = 0;
            i = ps.executeUpdate();

            if (i > 0) {
                valida = true;
            }
        } catch (SQLException ex) {
            log.error("Error create customerWhatsappDTO " + ex);

        } finally {
            con.cerrarConexion();
        }
        log.info("*** end answerDAO update ***");
        return valida;
    }

    @Override
    public boolean delete(answerDTO dto) {
        log.info("*** end answerDAO delete ***");
        return false;
    }

    @Override
    public answerDTO readOne(answerDTO dto) {
        log.info("*** end answerDAO delete ***");
        return null;
    }

    @Override
    public List<answerDTO> readMany(answerDTO dto) {
        log.info("*** end answerDAO readMany ***");
        PreparedStatement ps;
        ResultSet res;
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        ArrayList<answerDTO> answerList = new ArrayList();
        answerDTO answerVal = new answerDTO();
        try {
            ps = con.getCnn().prepareStatement(SQL_READMANY);
            ps.setString(1, dto.getIdQuestion());
            res = ps.executeQuery();
            int i = 0;
            while (res.next()) {
                answerList.add(new answerDTO(res.getString(1), res.getInt(2),
                        res.getString(3)));
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
            log.error("Error create customerWhatsappDTO " + ex);
            error.setCode(-1);
            error.setMessage("Error: " + ex);

        } finally {
            con.cerrarConexion();
        }
        answerVal.setError(error);
        answerList.add(answerVal);
        return answerList;
    }

    @Override
    public List<answerDTO> readAll() {
        log.info("*** end answerDAO delete ***");
        return null;
    }

}
