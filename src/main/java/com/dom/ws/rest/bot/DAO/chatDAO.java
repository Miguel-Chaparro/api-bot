/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.Conexion.interfaces;
import com.dom.ws.rest.bot.DTO.chatDTO;
import com.dom.ws.rest.bot.vo.msgError;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author MIGUEL
 */
public class chatDAO implements interfaces<chatDTO> {

    //private static final String SQL_READONE = "SELECT * FROM dommapi.chat WHERE idWhatsapp = ? ";
    private static final String SQL_UPDATE = "UPTADE dommapi.chat SET chatmessage =?, idcustomer =?, time=? WHERE idWhatsapp =? ";
    private static final String SQL_INSERT = "INSERT INTO dommapi.chat ( idWhatsapp, chatmessage, idcustomer, time )"
            + "VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM dommapi.chat chatmessage WHERE idWhatsapp = ?";
    private static final String SQL_READMANY = "SELECT * FROM dommapi.chat WHERE idWhatssap = ? ";
    private static final String SQL_READALL = "SELECT * FROM customerWhatsapp ";
    msgError error = new msgError();
       /**
     * Helper method to get fresh connection for each operation
     */
    /**
     * Helper method to get fresh connection for each operation
     * Returns null if connection pool timeout - caller must check for null
     */
    private conexionBD getConnection() {
        try {
            return conexionBD.saberEstado();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(chatDAO.class.getName())
                .log(java.util.logging.Level.SEVERE, "Connection pool timeout: {0}", ex.getMessage());
            return null;
        }
    }

    static final Logger log = Logger.getLogger(chatDAO.class.getName());

    @Override
    public boolean create(chatDTO dto) {
        log.info("*** Start chatDAO create ***");
        try {
            conexionBD con = getConnection();
            PreparedStatement ps;
            boolean valida = false;
            try {
                ps = con.getCnn().prepareStatement(SQL_INSERT);
                ps.setString(1, dto.getIdWhatsapp());
                int i = 0;
                i = ps.executeUpdate();

                if (i > 0) {
                    valida = true;
                }
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "Error create customer chatDTO {0}", ex);

            } finally {
                con.cerrarConexion();
            }
            log.info("*** end chatDAO create ***");
            return valida;
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Connection pool timeout or error in chatDAO create: {0}", ex);
            return false;
        }
    }

    @Override
    public boolean update(chatDTO dto) {
        log.info("*** Start chatDAO update ***");
        try {
            conexionBD con = getConnection();
            PreparedStatement ps;
            boolean valida = false;
            try {
                ps = con.getCnn().prepareStatement(SQL_UPDATE);
                ps.setString(1, dto.getMessage());
                ps.setString(2, dto.getIdCustomer());
                ps.setTimestamp(3, dto.getTime());
                ps.setString(4, dto.getIdWhatsapp());
                int i = 0;
                i = ps.executeUpdate();

                if (i > 0) {
                    valida = true;
                }
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "Error create customer chatDTO {0}", ex);

            } finally {
                con.cerrarConexion();
            }
            log.info("*** end questionsDAO update ***");
            return valida;
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Connection pool timeout or error in chatDAO update: {0}", ex);
            return false;
        }
    }

    @Override
    public boolean delete(chatDTO dto) {
        log.info("*** Start chatDAO update ***");
        try {
            conexionBD con = getConnection();
            PreparedStatement ps;
            boolean valida = false;
            try {
                ps = con.getCnn().prepareStatement(SQL_DELETE);
                ps.setString(1, dto.getIdWhatsapp());
                int i = 0;
                i = ps.executeUpdate();

                if (i > 0) {
                    valida = true;
                }
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "Error create customer chatDTO {0}", ex);

            } finally {
                con.cerrarConexion();
            }
            log.info("*** end questionsDAO update ***");
            return valida;
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Connection pool timeout or error in chatDAO delete: {0}", ex);
            return false;
        }
    }

    @Override
    public chatDTO readOne(chatDTO dto) {
        log.info("*** Start chatDAO readOne ***");
        log.info("*** End chatDao readOne ***");
        return null;

    }

    @Override
    public List<chatDTO> readMany(chatDTO dto) {
        log.info("***start chatDAO readMany***");
        ArrayList<chatDTO> answerList = new ArrayList<>();
        try {
            conexionBD con = getConnection();
            PreparedStatement ps;
            ResultSet res;
            chatDTO answerVal = new chatDTO();
            try {
                ps = con.getCnn().prepareStatement(SQL_READMANY);
                ps.setString(1, dto.getIdWhatsapp());
                res = ps.executeQuery();
                int i = 0;
                while (res.next()) {
                    answerList.add(new chatDTO(res.getInt(1), res.getString(2), res.getString(3), res.getString(4), res.getTimestamp(5)));
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
                log.log(Level.SEVERE, "Error create customerWhatsappDTO {0}", ex);
                error.setCode(-1);
                error.setMessage("Error: " + ex);

            } finally {
                con.cerrarConexion();
            }
            answerVal.setError(error);
            answerList.add(answerVal);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Connection pool timeout or error in chatDAO readMany: {0}", ex);
            chatDTO errorDto = new chatDTO();
            msgError errorMsg = new msgError();
            errorMsg.setCode(-1);
            errorMsg.setMessage("Connection pool timeout: " + ex.getMessage());
            errorDto.setError(errorMsg);
            answerList.add(errorDto);
        }
        return answerList;
    }

    @Override
    public List<chatDTO> readAll() {
        log.info("*** end answerDAO readMany ***");
        ArrayList<chatDTO> answerList = new ArrayList<>();
        try {
            conexionBD con = getConnection();
            PreparedStatement ps;
            ResultSet res;
            @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
            chatDTO answerVal = new chatDTO();
            try {
                ps = con.getCnn().prepareStatement(SQL_READALL);

                res = ps.executeQuery();
                int i = 0;
                while (res.next()) {
                    answerList.add(new chatDTO(res.getInt(1), res.getString(2), res.getString(3), res.getString(4), res.getTimestamp(5)));
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
                log.log(Level.SEVERE, "Error create customerWhatsappDTO {0}", ex);
                error.setCode(-1);
                error.setMessage("Error: " + ex);

            } finally {
                con.cerrarConexion();
            }
            answerVal.setError(error);
            answerList.add(answerVal);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Connection pool timeout or error in chatDAO readAll: {0}", ex);
            chatDTO errorDto = new chatDTO();
            msgError errorMsg = new msgError();
            errorMsg.setCode(-1);
            errorMsg.setMessage("Connection pool timeout: " + ex.getMessage());
            errorDto.setError(errorMsg);
            answerList.add(errorDto);
        }
        return answerList;
    }

}
