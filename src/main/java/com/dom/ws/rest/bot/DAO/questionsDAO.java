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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MIGUEL
 */
public class questionsDAO implements interfaces<questionsDTO> {
 
    private static final String SQL_READONE = "SELECT id,question,idFrom,idProject,nextQuestion,multiAnswer,minQuestion,openQuestion,endQuestion FROM dommapi.questions WHERE id = ? AND idProject = ? AND idFrom = ?";
    private static final String SQL_UPDATE = "UPDATE dommapi.questions SET question = ?, nextQuestion = ?, multiAnswer = ?, minQuestion =?, openQuestion =?, endQuestion =? "
            + "WHERE id = ? AND idProject = ?";
    private static final String SQL_INSERT = "INSERT INTO dommapi.questions (id, question, idProject, nextQuestion, multiAnswer, minQuestion, openQuestion, endQuestion ) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
   // private static final String SQL_DELETE = "DELETE FROM dommapi.questions WHERE idWhatsapp = ?";
    private static final String SQL_READMANY = "SELECT id,question,idFrom,idProject,nextQuestion,multiAnswer,minQuestion,openQuestion,endQuestion FROM dommapi.questions  WHERE idProject = ? ";
    /* private static final String SQL_READMANYUSER = "";
    private static final String SQL_READALL = "SELECT * FROM dommapi.questions "; */
    msgError error = new msgError();
       /**
     * Helper method to get fresh connection for each operation
     */
    static final Logger log = Logger.getLogger(questionsDAO.class.getName());

     private conexionBD getConnection() {
        return conexionBD.saberEstado();
    }

    @Override
    public boolean create(questionsDTO dto) {
        log.info("*** Start questionsDAO create ***");
        conexionBD con = getConnection();
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
            log.log(Level.SEVERE, "Error Create questionsDAO {0}", ex);

        } finally {
            con.cerrarConexion();
        }
        log.info("*** end questionsDAO create ***");
        return valida;
    }

    @Override
    public boolean update(questionsDTO dto) {
        log.info("*** Start questionsDAO update ***");
        conexionBD con = getConnection();
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(1, dto.getQuestions());
            ps.setInt(2, dto.getNextQuestion());
            ps.setInt(3, dto.getMultiAnswer());
            ps.setInt(4, dto.getMinQuestions());
            ps.setInt(5, dto.getOpenQuestion());
            ps.setInt(6, dto.getEndQuestions());
            ps.setString(7, dto.getIdQuestions());
            ps.setInt(8, dto.getIdProject());
            int i = 0;
            i = ps.executeUpdate();

            if (i > 0) {
                valida = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create questionsDAO {0}", ex);

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
        conexionBD con = getConnection();
        PreparedStatement ps;
        ResultSet res;
        msgError errorRead = new msgError();
        questionsDTO questions = new questionsDTO();
        try {
            ps = con.getCnn().prepareStatement(SQL_READONE);
            ps.setString(1, dto.getIdQuestions());
            ps.setInt(2, dto.getIdProject());
            ps.setString(3, dto.getIdFrom());
            
            log.log(Level.SEVERE, "Consulta  {0}\n", ps);
            res = ps.executeQuery();
            int i = 0;
            while (res.next()) {
                questions = new questionsDTO(res.getString(1), res.getString(2), res.getInt(4), res.getInt(5), res.getInt(6), res.getInt(7),res.getInt(8),res.getInt(9),res.getString(3));
                i++;
            }
            
            if (i == 0) {
                errorRead.setCode(1);
                errorRead.setMessage("No existe pregunta");
            }

        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error Read customerWhatsappDTO {0}", ex);
            errorRead.setCode(-1);
            errorRead.setMessage("Error: " + ex);

        } finally {
            con.cerrarConexion();
        }
        questions.setError(errorRead);
         log.info("*** End questionsDAO readOne ***");
        return questions;
    }

    @Override
    public List<questionsDTO> readMany(questionsDTO dto) {
        log.info("***start questionsDAO readMany***");
        conexionBD con = getConnection();
        PreparedStatement ps;
        ResultSet res;
        List<questionsDTO> resp = new ArrayList<>();
        questionsDTO objDto = new questionsDTO();
        msgError errorQuery = new msgError();
        int i = 0;
        try {
            ps = con.getCnn().prepareStatement(SQL_READMANY);
            ps.setInt(1, dto.getIdProject());
            res = ps.executeQuery();

            while (res.next()) {
                resp.add(new questionsDTO(res.getString(1), res.getString(2), res.getInt(4), res.getInt(5), res.getInt(6), res.getInt(7),res.getInt(8),res.getInt(9),res.getString(3)));
                i++;

            }
            if (i > 0) {
                errorQuery.setCode(0);
                errorQuery.setMessage("");
            } else {
                errorQuery.setCode(11);
                errorQuery.setMessage("Error no data found");

            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create customerWhatsappDTO {0}", ex);
            errorQuery.setCode(ex.getErrorCode());
            errorQuery.setMessage("Error ORA: " + ex);

        } finally {
            con.cerrarConexion();
        }
        if (i == 0) {
            objDto.setError(errorQuery);
            resp.add(objDto);
        } else {
            objDto = resp.get(0);
            objDto.setError(errorQuery);
            resp.set(0, objDto);
        }
        log.info("***End questionsDAO readMany***");
        return resp;
    }
    

    @Override
    public List<questionsDTO> readAll() {
        return null;
    }

}
