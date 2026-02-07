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

    private static final String SQL_UPDATE = "UPDATE dommapi.answer SET  answerdes = ?, command = ?, flg_end = ?, flg_command = ? "
            + "WHERE idQuestion = ? AND answerCode = ? AND idProject =? AND idfrom = ?";
    private static final String SQL_INSERT = "INSERT INTO dommapi.answer (idQuestion, answerCode, answerdes, idproject, command, flg_end, flg_command, idFrom) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM dommapi.answer WHERE idQuestion = ? AND answerCode = ? AND idProject =? AND idfrom = ?";
    private static final String SQL_READALL = "SELECT * FROM dommapi.answer WHERE idproject = ?";
    private static final String SQL_READMANY = "SELECT idQuestion, answerCode, answerdes, idfrom, idProject, command, flg_End, flg_Command FROM dommapi.answer WHERE idQuestion = ? AND idproject = ? AND idfrom = ? ";
    private static final String SQL_READMANYPROJECT = "SELECT * FROM dommapi.answer WHERE  idproject = ? ";
    msgError error = new msgError();
       /**
     * Helper method to get fresh connection for each operation
     */
   

    static final Logger log = Logger.getLogger(answerDAO.class.getName());
    
    private conexionBD getConnection() throws SQLException {
        return conexionBD.saberEstado();
    }
    @Override
    public boolean create(answerDTO dto) {
        log.info("*** Start answerDAO create ***");
        try {
            PreparedStatement ps;
            conexionBD con = getConnection();
            boolean valida = false;
            try {
                ps = con.getCnn().prepareStatement(SQL_INSERT);
                ps.setString(1, dto.getIdQuestion());
                ps.setInt(2, dto.getAnswerId());
                ps.setString(3, dto.getAnswerDesc());
                ps.setInt(4, dto.getIdProject());
                ps.setString(5, dto.getCommand());
                ps.setInt(6, dto.getFlgEnd());
                ps.setInt(7, dto.getFlgCommand());
                ps.setString(8, dto.getIdFrom());
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
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Connection pool timeout or error in answerDAO create: {0}", ex);
            return false;
        }
    }

    @Override
    public boolean update(answerDTO dto) {
        log.info("*** Start answerDAO update ***");
        try {
            PreparedStatement ps;
            conexionBD con = getConnection();
            boolean valida = false;
            try {
                ps = con.getCnn().prepareStatement(SQL_UPDATE);
                ps.setString(1, dto.getAnswerDesc());
                ps.setString(2, dto.getCommand());
                ps.setInt(3, dto.getFlgEnd());
                ps.setInt(4, dto.getFlgCommand());
                ps.setString(5, dto.getIdQuestion());
                ps.setInt(6, dto.getAnswerId());
                ps.setInt(7, dto.getIdProject());
                ps.setString(8, dto.getIdFrom());
                int i = 0;
                i = ps.executeUpdate();

                if (i > 0) {
                    valida = true;
                }
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "Error Update answerDAO {0}", ex);

            } finally {
                con.cerrarConexion();
            }
            log.info("*** end answerDAO update ***");
            return valida;
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Connection pool timeout or error in answerDAO update: {0}", ex);
            return false;
        }
    }

    @Override
    public boolean delete(answerDTO dto) {
        log.info("*** Start answerDAO delete ***");
        try {
            conexionBD con = getConnection();
            PreparedStatement ps;
            boolean valida = false;
            try {
                ps = con.getCnn().prepareStatement(SQL_DELETE);
                ps.setString(1, dto.getIdQuestion());
                ps.setInt(3, dto.getIdProject());
                ps.setInt(2, dto.getAnswerId());
                ps.setString(4, dto.getIdFrom());
                int i = 0;
                i = ps.executeUpdate();

                if (i > 0) {
                    valida = true;
                }
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "Error create customer answerDAO {0}", ex);
            } finally {
                con.cerrarConexion();
            }
            log.info("*** end answerDAO delete ***");
            return valida;
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Connection pool timeout or error in answerDAO delete: {0}", ex);
            return false;
        }
    }

    @Override
    public answerDTO readOne(answerDTO dto) {
        log.info("*** start answerDAO readOne ***");
        log.info("*** End answerDAO readOne ***");
        return null;
    }

    @Override
    public List<answerDTO> readMany(answerDTO dto) {
        log.info("*** Start answerDAO readMany ***");
        ArrayList<answerDTO> answerList = new ArrayList<>();
        try {
            conexionBD con = getConnection();
            PreparedStatement ps;
            ResultSet res;
            answerDTO answerVal = new answerDTO();
            int i = 0;
            try {

                if (dto.getAnswerId() == 100) {
                    ps = con.getCnn().prepareStatement(SQL_READMANYPROJECT);
                    ps.setInt(1, dto.getIdProject());
                } else {
                    log.info("**** readMany****** " + dto.getIdQuestion() + " " + dto.getIdProject() + " " + dto.getIdFrom());
                    ps = con.getCnn().prepareStatement(SQL_READMANY);
                    ps.setString(1, dto.getIdQuestion());
                    ps.setInt(2, dto.getIdProject());
                    ps.setString(3, dto.getIdFrom());
                }

                res = ps.executeQuery();

                while (res.next()) {
                    answerList.add(new answerDTO(res.getString(1), res.getInt(2),
                            res.getString(3), res.getInt(5), res.getString(6), res.getInt(7), res.getInt(8),
                            res.getString(4)));
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

            if (i == 0) {
                answerVal.setError(error);
                answerList.add(answerVal);
            } else {
                answerVal = answerList.get(0);
                answerVal.setError(error);
                answerList.set(0, answerVal);
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Connection pool timeout or error in answerDAO readMany: {0}", ex);
            answerDTO errorDto = new answerDTO();
            msgError errorMsg = new msgError();
            errorMsg.setCode(-1);
            errorMsg.setMessage("Connection pool timeout: " + ex.getMessage());
            errorDto.setError(errorMsg);
            answerList.add(errorDto);
        }
        log.info("*** End answerDAO readMany ***");
        return answerList;
    }

    @Override
    public List<answerDTO> readAll() {
        log.info("*** Start answerDAO readAll ***");
        ArrayList<answerDTO> answerList = new ArrayList<>();
        try {
            conexionBD con = getConnection();
            PreparedStatement ps;
            ResultSet res;
            answerDTO answerVal = new answerDTO();
            try {
                ps = con.getCnn().prepareStatement(SQL_READALL);

                res = ps.executeQuery();
                int i = 0;
                while (res.next()) {
                    answerList.add(new answerDTO(res.getString(1), res.getInt(2), res.getString(3), res.getInt(5),
                            res.getString(6), res.getInt(7), res.getInt(8), res.getString(4)));
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
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Connection pool timeout or error in answerDAO readAll: {0}", ex);
            answerDTO errorDto = new answerDTO();
            msgError errorMsg = new msgError();
            errorMsg.setCode(-1);
            errorMsg.setMessage("Connection pool timeout: " + ex.getMessage());
            errorDto.setError(errorMsg);
            answerList.add(errorDto);
        }
        log.info("*** end answerDAO readAll ***");
        return answerList;
    }

}
