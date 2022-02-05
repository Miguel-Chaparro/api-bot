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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MIGUEL
 */
public class answerDAO implements interfaces<answerDTO> {

    private static final String SQL_UPDATE = "UPDATE dommapi.answer SET  answerdes = ?"
            + "WHERE idQuestion = ? AND answerCode = ? AND idProject =? ";
    private static final String SQL_INSERT = "INSERT INTO dommapi.answer (idQuestion, answerCode, answerdes, idproject) "
            + "VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM dommapi.answer WHERE idQuestion = ? AND answerCode = ? AND idProject =?";
    private static final String SQL_READMANY = "SELECT * FROM dommapi.answer WHERE idQuestion = ? AND idproject = ? ";
    private static final String SQL_READALL = "SELECT * FROM cdommapi.answer";
    msgError error = new msgError();
    private final conexionBD con = conexionBD.saberEstado();
    static final Logger log = Logger.getLogger(answerDAO.class.getName());

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
            ps.setInt(4, dto.getIdProject());
            int i = 0;
            i = ps.executeUpdate();

            if (i > 0) {
                valida = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create customerWhatsappDTO {0}", ex);

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

            ps.setInt(3, dto.getAnswerId());
            ps.setString(2, dto.getIdQuestion());
            ps.setString(1, dto.getAnswerDesc());
            ps.setInt(4, dto.getIdProject());
            int i = 0;
            i = ps.executeUpdate();

            if (i > 0) {
                valida = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create answerDAO {0}", ex);

        } finally {
            con.cerrarConexion();
        }
        log.info("*** end answerDAO update ***");
        return valida;
    }

    @Override
    public boolean delete(answerDTO dto) {
        log.info("*** Start answerDAO delete ***");
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setString(1, dto.getIdQuestion());
            ps.setInt(3, dto.getIdProject());
            ps.setInt(2, dto.getAnswerId());
            int i = 0;
            i = ps.executeUpdate();

            if (1 > 0) {
                valida = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create customer answerDAO {0}", ex);
        } finally {
            con.cerrarConexion();
        }
        log.info("*** end answerDAO delete ***");
        return valida;
    }

    @Override
    public answerDTO readOne(answerDTO dto) {
        log.info("*** start answerDAO readOne ***");
        log.info("*** End answerDAO readOne ***");
        return null;
    }

    @Override
    public List<answerDTO> readMany(answerDTO dto) {
        log.info("*** end answerDAO readMany ***");
        PreparedStatement ps;
        ResultSet res;
        ArrayList<answerDTO> answerList = new ArrayList();
        answerDTO answerVal = new answerDTO();
        try {
            ps = con.getCnn().prepareStatement(SQL_READMANY);
            ps.setString(1, dto.getIdQuestion());
            ps.setInt(2, dto.getIdProject());
            res = ps.executeQuery();
            int i = 0;
            while (res.next()) {
                answerList.add(new answerDTO(res.getString(1), res.getInt(2),
                        res.getString(3), res.getInt(4)));
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
        log.info("*** Start answerDAO readAll ***");
        
        PreparedStatement ps;
        ResultSet res;
        ArrayList<answerDTO> answerList = new ArrayList();
        answerDTO answerVal = new answerDTO();
        try {
            ps = con.getCnn().prepareStatement(SQL_READALL);
            res = ps.executeQuery();
            int i = 0;
            while (res.next()) {
                answerList.add(new answerDTO(res.getString(1), res.getInt(2), res.getString(3), res.getInt(4)));
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
            error.setCode(-1);
            error.setMessage("Error: " + ex);
        } finally {
            con.cerrarConexion();
        }
        answerVal.setError(error);
        answerList.add(answerVal);
        log.info("*** end answerDAO readAll ***");
        return answerList;
    }

}
