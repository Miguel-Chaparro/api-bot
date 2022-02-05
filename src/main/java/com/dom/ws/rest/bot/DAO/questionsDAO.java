/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.Conexion.interfaces;
import com.dom.ws.rest.bot.DTO.questionsDTO;
import com.dom.ws.rest.bot.vo.msgError;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MIGUEL
 */
public class questionsDAO implements interfaces<questionsDTO> {
 
    private static final String SQL_READONE = "SELECT * FROM dommapi.questions WHERE id = ? ";
    private static final String SQL_UPDATE = "UPDATE dommapi.questions SET question = ?, nextQuestion = ?, multiAnswer = ?, minQuestion =?, openQuestion =?, endQuestion =? "
            + "WHERE id = ? AND idProject = ?";
    private static final String SQL_INSERT = "INSERT INTO dommapi.questions (id, question, idProject, nextQuestion, multiAnswer, minsQuestion, openQuestion, endQuestion ) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM dommapi.questions WHERE idWhatsapp = ?";
    private static final String SQL_READMANY = "SELECT * FROM dommapi.questions answer WHERE idQuestion = ? ";
    private static final String SQL_READMANYUSER = "";
    private static final String SQL_READALL = "SELECT * FROM dommapi.questions ";
    msgError error = new msgError();
    private final conexionBD con = conexionBD.saberEstado();
    static final Logger log = Logger.getLogger(questionsDAO.class.getName());

    @Override
    public boolean create(questionsDTO dto) {
        log.info("*** Start questionsDAO create ***");
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT);
            ps.setString(1, dto.getIdQuestions());
            ps.setString(2, dto.getQuestions());
            ps.setInt(3, dto.getIdProject());
            ps.setInt(4, dto.getNextQuestion());
            ps.setInt(5, dto.getMultiAnswer());
            ps.setInt(6, dto.getMinQuestions());
            ps.setInt(7, dto.getOpenQuestion());
            ps.setInt(8, dto.getEndQuestions());
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
        log.info("*** end questionsDAO create ***");
        return valida;
    }

    @Override
    public boolean update(questionsDTO dto) {
        log.info("*** Start questionsDAO update ***");
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(1, dto.getQuestions());
            ps.setString(2, dto.getIdQuestions());
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
        log.info("*** end questionsDAO update ***");
        return valida;
    }

    @Override
    public boolean delete(questionsDTO dto) {
        return false;
    }

    @Override
    public questionsDTO readOne(questionsDTO dto) {
        log.info("*** Start questionsDAO readOne ***");
        PreparedStatement ps;
        ResultSet res;
        @SuppressWarnings("LocalVariableHidesMemberVariable")
        msgError error = new msgError();
        questionsDTO questions = new questionsDTO();
        try {
            ps = con.getCnn().prepareStatement(SQL_READONE);
            ps.setString(1, dto.getIdQuestions());
            log.log(Level.SEVERE, "Consulta  {0}\n", ps);
            res = ps.executeQuery();
            int i = 0;
            while (res.next()) {
                questions = new questionsDTO(res.getString(1),res.getInt(2), res.getString(3),res.getInt(4),res.getInt(5),res.getInt(6),res.getInt(7),res.getInt(8));
                i++;
            }
            
            if (i == 0) {
                error.setCode(1);
                error.setMessage("No existe n�mero registrado");
            }

        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create customerWhatsappDTO {0}", ex);
            error.setCode(-1);
            error.setMessage("Error: " + ex);

        } finally {
            con.cerrarConexion();
        }
        questions.setError(error);
         log.info("*** End questionsDAO readOne ***");
        return questions;
    }

    @Override
    public List<questionsDTO> readMany(questionsDTO dto) {
        return null;
    }

    @Override
    public List<questionsDTO> readAll() {
        return null;
    }

}
