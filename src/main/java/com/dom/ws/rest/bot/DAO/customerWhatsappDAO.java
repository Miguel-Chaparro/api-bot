/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.Conexion.interfaces;
import com.dom.ws.rest.bot.DTO.customerWhatsappDTO;
import com.dom.ws.rest.bot.vo.msgError;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MIGUEL
 */
public class customerWhatsappDAO implements interfaces<customerWhatsappDTO> {

    private static final String SQL_READONE = "SELECT * FROM dommapi.customerWhatsapp WHERE idWhatsapp = ? ";
    private static final String SQL_UPDATE = "UPDATE dommapi.customerWhatsapp SET questionId = ?, date = ?,"
            + "pendingState = ?, pendingDescription = ? WHERE idWhatsapp = ?";
//    private static final String SQL_UPDATE = "UPDATE dommapi.customerWhatsapp SET questionId = ?, date = ?, nameWAP =?, idCustomer =?, idProject =? "
//            + "pendingState = ?, pendingDescription = ? "
//            + "WHERE idWhatsapp = ?";
    private static final String SQL_INSERT = "INSERT INTO dommapi.customerWhatsapp (idWhatsapp, idCustomer, nameWAP, questionId) "
            + "VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM dommapi.customerWhatsapp WHERE idWhatsapp = ?";
    private static final String SQL_READMANY = "SELECT * FROM dommapi.customerWhatsapp WHERE idCustomer = ? ";
    private static final String SQL_READMANYUSER = "";
    private static final String SQL_READALL = "SELECT * FROM dommapi.customerWhatsapp";
    private final conexionBD con = conexionBD.saberEstado();
    static final Logger log = Logger.getLogger(customerWhatsappDAO.class.getName());

    @Override
    public boolean create(customerWhatsappDTO dto) {
        log.info("*** Start customerWhatsappDAO create ***");
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT);
            ps.setString(1, dto.getIdWhatsapp());
            ps.setString(2, dto.getIdCustomer());
            ps.setString(3, dto.getName());
            ps.setString(4, "1");
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
        log.info("*** end customerWhatsappDAO create ***");
        return valida;
    }

    @Override
    public boolean update(customerWhatsappDTO dto) {
        log.info("*** Start customerWhatsappDAO update ***");
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(1, dto.getIdQuestions());
            ps.setTimestamp(2, dto.getDate());
            ps.setInt(3, 0);
            ps.setString(4, dto.getPendingDescription());
            ps.setString(5, dto.getIdWhatsapp());
            log.info("Script "+ ps);
            int i = 0;
            i = ps.executeUpdate();

            if (i > 0) {
                valida = true;
            }

        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error update customerWhatsappDTO {0}", ex);

        } finally {
            con.cerrarConexion();
        }
        log.info("*** end customerWhatsappDAO update ***");
        return valida;
    }

    @Override
    public boolean delete(customerWhatsappDTO dto) {
        log.info("*** start customerWhatsappDAO delete ***");
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps=con.getCnn().prepareStatement(SQL_INSERT);
            ps.setString(1, dto.getIdWhatsapp());
            int i= 0;
            i = ps.executeUpdate();
            
            if (i>0) {
                valida =true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error delete customerWhatsappDAO {0}", ex);
            
        }finally {
            con.cerrarConexion();
        }
        log.info("***end customerWhatsappDAO uptade ***");
        return valida;
    }

    @Override
    public customerWhatsappDTO readOne(customerWhatsappDTO dto) {
        log.info("*** Start customerWhatsappDAO update ***");
        PreparedStatement ps;
        ResultSet res;
        msgError error = new msgError();
        customerWhatsappDTO customer = new customerWhatsappDTO();
        try {
            ps = con.getCnn().prepareStatement(SQL_READONE);
            ps.setString(1, dto.getIdWhatsapp());
            res = ps.executeQuery();
            int i = 0;
            while (res.next()) {
                customer = new customerWhatsappDTO(res.getString(1), res.getString(2),
                        res.getString(3), res.getString(4), res.getTimestamp(5), res.getInt(6), res.getString(7));
                i++;
            }
            if (i == 0) {
                error.setCode(1);
                error.setMessage("No existe numero registrado");
            } else {
                error.setCode(0);
                error.setMessage("");
            }

        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create customerWhatsappDTO {0}", ex);
            error.setCode(-1);
            error.setMessage("Error: " + ex);

        } finally {
            con.cerrarConexion();
        }
        customer.setError(error);
        return customer;
    }

    @Override
    public List<customerWhatsappDTO> readMany(customerWhatsappDTO dto) {
        log.info("*** end customerWhatsappDAO readMany ***");
        return null;
    }

    @Override
    public List<customerWhatsappDTO> readAll() {
        log.info("*** end customerWhatsappDAO readAll ***");
        return null;
    }

}
