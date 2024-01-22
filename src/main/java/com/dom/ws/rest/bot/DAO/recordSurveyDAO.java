package com.dom.ws.rest.bot.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.Conexion.interfaces;
import com.dom.ws.rest.bot.DTO.recordSurveyDTO;
import com.dom.ws.rest.bot.vo.msgError;

public class recordSurveyDAO implements interfaces<recordSurveyDTO> {

    private static final String SQL_INSERT = "INSERT INTO dommapi.recordsurvey (idWhatsapp ,idFrom ,idProject ,idQuestion ,openAnswer ,answer ,multiAnswer, fecha) VALUES (?,?,?,?,?,?,?,GETDATE())";
    private static final String SQL_UPDATE = "UPDATE dommapi.recordsurvey SET idWhatsapp = ? ,idFrom = ? ,idProject = ? ,idQuestion = ? ,openAnswer = ? ,answer = ? ,multiAnswer = ? WHERE idRecordSurvey = ?";
    private static final String SQL_DELETE = "DELETE FROM dommapi.recordsurvey WHERE idRecordSurvey = ?";
    private static final String SQL_READALL = "SELECT * FROM dommapi.recordsurvey";
    private static final String SQL_READWHATSAPP = "SELECT idRecordSurvey ,idWhatsapp ,idFrom ,idProject ,idQuestion ,openAnswer ,answer ,multiAnswer, fechaFROM dommapi.recordsurvey WHERE idWhatsapp = ?";
    private static final String SQL_READFROM = "SELECT idRecordSurvey ,idWhatsapp ,idFrom ,idProject ,idQuestion ,openAnswer ,answer ,multiAnswer, fecha FROM dommapi.recordsurvey WHERE idFrom = ?";
    private static final String SQL_READ = "SELECT idRecordSurvey ,idWhatsapp ,idFrom ,idProject ,idQuestion ,openAnswer ,answer ,multiAnswer, fecha FROM dommapi.recordsurvey WHERE  idrecordsurvey = ?  ";
    
    msgError error = new msgError();
    private final conexionBD con = conexionBD.saberEstado();
    static final Logger log = Logger.getLogger(answerDAO.class.getName());
    @Override
    public boolean create(recordSurveyDTO dto) {
       log.info("*** Start recordSurveyDAO create ***");
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT);
            ps.setString(1, dto.getIdWhastapp());
            ps.setString(2, dto.getIdFrom());
            ps.setInt(3, dto.getIdProject());
            ps.setString(4, dto.getIdQuestion());
            ps.setString(5, dto.getOpenAnswer());
            ps.setInt(6, dto.getAnswer());
            ps.setString(7, dto.getMultiAnswer());
            int i = 0;
            i = ps.executeUpdate();

            if (i > 0) {
                valida = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create recordSurveyDAO {0}", ex);

        } finally {
            con.cerrarConexion();
        }
        return valida;
    }

    @Override
    public boolean update(recordSurveyDTO dto) {
        log.info("*** Start recordSurveyDAO update ***");
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(1, dto.getIdWhastapp());
            ps.setString(2, dto.getIdFrom());
            ps.setInt(3, dto.getIdProject());
            ps.setString(4, dto.getIdQuestion());
            ps.setString(5, dto.getOpenAnswer());
            ps.setInt(6, dto.getAnswer());
            ps.setString(7, dto.getMultiAnswer());
            ps.setInt(8, dto.getIdRecordSurvey());
            int i = 0;
            i = ps.executeUpdate();

            if (i > 0) {
                valida = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create recordSurveyDAO {0}", ex);

        } finally {
            con.cerrarConexion();
        }

        return valida;

    }

    @Override
    public boolean delete(recordSurveyDTO dto) {
        log.info("*** Start recordSurveyDAO delete ***");
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_DELETE);
            ps.setInt(1, dto.getIdRecordSurvey());
            int i = 0;
            i = ps.executeUpdate();

            if (i > 0) {
                valida = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create recordSurveyDAO {0}", ex);

        } finally {
            con.cerrarConexion();
        }
        return valida;
    }

    @Override
    public recordSurveyDTO readOne(recordSurveyDTO dto) {
        log.info("*** Start recordSurveyDAO readOne ***");
        PreparedStatement ps;
        ResultSet rs;
        recordSurveyDTO dtoRead = null;
        try {
            ps = con.getCnn().prepareStatement(SQL_READ);
            ps.setInt(1, dto.getIdRecordSurvey());
            rs = ps.executeQuery();
            while (rs.next()) {
                dtoRead = new recordSurveyDTO();
                dtoRead.setIdRecordSurvey(rs.getInt("idRecordSurvey"));
                dtoRead.setIdWhastapp(rs.getString("idWhatsapp"));
                dtoRead.setIdFrom(rs.getString("idFrom"));
                dtoRead.setIdProject(rs.getInt("idProject"));
                dtoRead.setIdQuestion(rs.getString("idQuestion"));
                dtoRead.setOpenAnswer(rs.getString("openAnswer"));
                dtoRead.setAnswer(rs.getInt("answer"));
                dtoRead.setMultiAnswer(rs.getString("multiAnswer"));
                dtoRead.setFecha(rs.getTimestamp("fecha"));
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create recordSurveyDAO {0}", ex);

        } finally {
            con.cerrarConexion();
        }
        return dtoRead;
    }

    @Override
    public List<recordSurveyDTO> readMany(recordSurveyDTO dto) {

        log.info("*** Start recordSurveyDAO readMany ***");
        PreparedStatement ps;
        ResultSet rs;
        List<recordSurveyDTO> list = new ArrayList<>();
        try {
            if (dto.getIdWhastapp() != null) {
                ps = con.getCnn().prepareStatement(SQL_READWHATSAPP);
                ps.setString(1, dto.getIdWhastapp());
            } else {
                ps = con.getCnn().prepareStatement(SQL_READFROM);
                ps.setString(1, dto.getIdFrom());
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                recordSurveyDTO dtoRead = new recordSurveyDTO();
                dtoRead.setIdRecordSurvey(rs.getInt("idRecordSurvey"));
                dtoRead.setIdWhastapp(rs.getString("idWhatsapp"));
                dtoRead.setIdFrom(rs.getString("idFrom"));
                dtoRead.setIdProject(rs.getInt("idProject"));
                dtoRead.setIdQuestion(rs.getString("idQuestion"));
                dtoRead.setOpenAnswer(rs.getString("openAnswer"));
                dtoRead.setAnswer(rs.getInt("answer"));
                dtoRead.setMultiAnswer(rs.getString("multiAnswer"));
                dtoRead.setFecha(rs.getTimestamp("fecha"));
                list.add(dtoRead);
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create recordSurveyDAO {0}", ex);

        } finally {
            con.cerrarConexion();
        }
        return list;
    }

    @Override
    public List<recordSurveyDTO> readAll() {
        
        log.info("*** Start recordSurveyDAO readAll ***");
        PreparedStatement ps;
        ResultSet rs;
        List<recordSurveyDTO> list = new ArrayList<>();
        try {
            ps = con.getCnn().prepareStatement(SQL_READALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                recordSurveyDTO dtoRead = new recordSurveyDTO();
                dtoRead.setIdRecordSurvey(rs.getInt("idRecordSurvey"));
                dtoRead.setIdWhastapp(rs.getString("idWhatsapp"));
                dtoRead.setIdFrom(rs.getString("idFrom"));
                dtoRead.setIdProject(rs.getInt("idProject"));
                dtoRead.setIdQuestion(rs.getString("idQuestion"));
                dtoRead.setOpenAnswer(rs.getString("openAnswer"));
                dtoRead.setAnswer(rs.getInt("answer"));
                dtoRead.setMultiAnswer(rs.getString("multiAnswer"));
                dtoRead.setFecha(rs.getTimestamp("fecha"));
                list.add(dtoRead);
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create recordSurveyDAO {0}", ex);

        } finally {
            con.cerrarConexion();
        }
        return list;
    }
    
   
}
