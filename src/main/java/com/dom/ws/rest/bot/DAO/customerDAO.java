/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.Conexion.interfaces;
import com.dom.ws.rest.bot.DTO.customerDTO;
import com.dom.ws.rest.bot.vo.msgError;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author MIGUEL
 */
public class customerDAO implements interfaces<customerDTO> {

    private static final String SQL_READONE = "SELECT * FROM registroCliente WHERE cedulaCliente = ? ";
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
    static Logger log = Logger.getLogger(customerDAO.class);

    @Override
    public boolean create(customerDTO dto) {
        return false;
    }

    @Override
    public boolean update(customerDTO dto) {
        return false;
    }

    @Override
    public boolean delete(customerDTO dto) {
        return false;
    }

    @Override
    public customerDTO readOne(customerDTO dto) {
        log.info("*** Start customerDAO readOne ***");
        PreparedStatement ps;
        ResultSet res;
        @SuppressWarnings("LocalVariableHidesMemberVariable")
        msgError error = new msgError();
        customerDTO customer = new customerDTO();
        try {
            ps = con.getCnn().prepareStatement(SQL_READONE);
            ps.setString(1, dto.getCedulaCliente());
            res = ps.executeQuery();
            int i = 0;
            while (res.next()) {
                customer = new customerDTO(res.getString(1), res.getString(2),
                        res.getString(3), res.getString(4), res.getTimestamp(5), res.getString(6), res.getString(7),
                        res.getString(8), res.getInt(9), res.getString(10), res.getString(11), res.getString(12), res.getString(13), res.getString(14), res.getString(15), res.getString(16),res.getInt(17)
                        ,res.getString(18),res.getString(19),res.getString(20));
                i++;
            }
            if (i == 0) {
                error.setCode(1);
                error.setMessage("No existe número registrado");
            }

        } catch (SQLException ex) {
            log.error("Error create customerDAO " + ex);
            error.setCode(-1);
            error.setMessage("Error: " + ex);

        } finally {
            con.cerrarConexion();
        }
        customer.setError(error);
        log.info("*** End customerDAO readOne ***");
        return customer;
    }

    @Override
    public List<customerDTO> readMany(customerDTO dto) {
        return null;
    }

    @Override
    public List<customerDTO> readAll() {
        return null;
    }

}
