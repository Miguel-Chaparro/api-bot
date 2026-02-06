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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author MIGUEL
 */
public class registroClienteDAO implements interfaces<customerDTO> {

    private static final String SQL_READONE = "SELECT * FROM dommapi.registroCliente WHERE cedulaCliente = ? ";
    private static final String SQL_UPDATE = "UPDATE dommapi.registrocliente SET nodo = ? "
            + "WHERE idRaspi = ? AND ip = ?";
    private static final String SQL_INSERT = "INSERT INTO dommapi.registrocliente (cedulaCliente, nombreCliente, telefonoPpal, telefonoOpc, fechaCreacion, departamento, ciudad,"
            + "direccion, plan, ip, ppoe, descripcion, idTecnico, latitud, longitud, nodo,  pago, correo, empresa, raspi) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*     private static final String SQL_DELETE = "SELECT * FROM dommapi.registrocliente WHERE idRaspi = ?";
    private static final String SQL_READMANY = "SELECT * FROM dommapi.registrocliente WHERE idQuestion = ? ";
    private static final String SQL_READMANYUSER = ""; */
    private static final String SQL_READALL = "SELECT * FROM dommapi.registrocliente";
    msgError error = new msgError();
       /**
     * Helper method to get fresh connection for each operation
     */
    static final Logger log = Logger.getLogger(registroClienteDAO.class.getName());
    private conexionBD getConnection() {
        return conexionBD.saberEstado();
    } 
    @Override
    public boolean create(customerDTO dto){
        log.info("***Start registroClienteDAO create***");
        conexionBD con = getConnection();
        PreparedStatement ps;
        boolean valida= false;
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT);
            ps.setString(1, dto.getCedulaCliente());
            ps.setString(2, dto.getNombreCliente());
            ps.setString(3, dto.getTelefonoPpal());
            ps.setString(4, dto.getTelefonoOpc());
            ps.setString(5, dto.getDepartamento());
            ps.setTimestamp(6, dto.getFechaCreacion());
            ps.setString(7, dto.getCiudad());
            ps.setString(8, dto.getDireccion());
            ps.setInt(9, dto.getPlan());
            ps.setString(10, dto.getIp());
            ps.setString(11, dto.getPpoe());
            ps.setString(12, dto.getDescripcion());
            ps.setString(13, dto.getIdTecnico());
            ps.setString(14, dto.getLatitud());
            ps.setString(15, dto.getLongitud());
            ps.setString(16, dto.getNodo());
            ps.setInt(17,dto.getPago());
            ps.setString(18, dto.getCorreo());
            ps.setString(19, dto.getEmpresa());
            ps.setString(20, dto.getRaspi());
            
            int i = 0;
            i=ps.executeUpdate();
            
            if (i>0) {
                valida = true;
            }
        }catch (SQLException ex) {
            log.log(Level.SEVERE, "end registroClienteDAO {0}", ex);
            
        }finally {
            con.cerrarConexion();
        }
        log.info("*** end registroClienteDAO create ***");
        return valida;
    }

    @Override
    public boolean update(customerDTO dto) {
        log.info("*** Start registroClienteDAO uptade ***");
        conexionBD con = getConnection();
        PreparedStatement ps;
        boolean valida= false;
        try{
            ps= con.getCnn().prepareStatement(SQL_UPDATE);
            ps.setString(1, dto.getNodo());
            ps.setString(2, dto.getRaspi());
            ps.setString(3, dto.getIp());
            int i = 0;
            i = ps.executeUpdate();
            
            if (i>0) {
                valida= true;
            }
            
        }catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create registroClienteDAO", ex);
           
        } finally {
            con.cerrarConexion();
        }
        log.info("***end registroClienteDAO uptade***");
        return valida;
    }        
            

    @Override
    public boolean delete(customerDTO dto) {
        log.info("*** Start registroClienteDAO delete ***");
        conexionBD con = getConnection();
        PreparedStatement ps;
        boolean valida = false;
        try {
            ps=con.getCnn().prepareStatement(SQL_INSERT);
            ps.setString(1, dto.getCedulaCliente());
            ps.setString(2, dto.getNombreCliente());
            ps.setString(3, dto.getTelefonoPpal());
            ps.setString(4, dto.getTelefonoOpc());
            ps.setString(5, dto.getDepartamento());
            ps.setTimestamp(6, dto.getFechaCreacion());
            ps.setString(7, dto.getCiudad());
            ps.setString(8, dto.getDireccion());
            ps.setInt(9, dto.getPlan());
            ps.setString(10, dto.getIp());
            ps.setString(11, dto.getPpoe());
            ps.setString(12, dto.getDescripcion());
            ps.setString(13, dto.getIdTecnico());
            ps.setString(14, dto.getLatitud());
            ps.setString(15, dto.getLongitud());
            ps.setString(16, dto.getNodo());
            ps.setInt(17,dto.getPago());
            ps.setString(18, dto.getCorreo());
            ps.setString(19, dto.getEmpresa());
            ps.setString(20, dto.getRaspi());
            int i= 0;
            i = ps.executeUpdate();
            
            if (i>0) {
                valida =true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE,"Error delete registroClienteDAO {0}", ex);
            
        }finally {
            con.cerrarConexion();
        }
        log.info("***end registroClienteDAO uptade ***");
        return valida;
    }

    @Override
    public customerDTO readOne(customerDTO dto) {
        log.info("*** Start registroclienteDAO readOne ***");
        conexionBD con = getConnection();
        PreparedStatement ps;
        ResultSet res;
        //msgError Error = new msgError();
        customerDTO customer = new customerDTO();
        try {
            ps=con.getCnn().prepareStatement(SQL_READONE);
            ps.setString(1, dto.getCedulaCliente());
            res = ps.executeQuery();
            int i = 0;
            while(res.next()) {
                customer = new customerDTO(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getTimestamp(6), res.getString(5),
                res.getString(7), res.getString(8), res.getInt(9), res.getString(10), res.getString(11), res.getString(12), res.getString(13), res.getString(14), 
                res.getString(15), res.getString(16), res.getInt(17), res.getString(18), res.getString(19), res.getString(20));
                i++; 
            }
            if (i==0) {
                error.setCode(1);
                error.setMessage("No existe numero registrado");
            }else {
                error.setCode(0);
                error.setMessage("");
            }
            
        }catch(SQLException ex){
            log.log(Level.SEVERE, "Error create customerDTO {0}", ex);
            error.setCode(-1);
            error.setMessage("error: " + ex);
                  
        }finally{
            con.cerrarConexion();
        }
        customer.setError(error);
        log.info("*** End registroClienteDAO readOne***");
        return customer;
            
        }
        
    

    @Override
    public List<customerDTO> readMany(customerDTO dto) {
        log.info("***Start registroclienteDAO readmany***");
        return null;
    }
    


    @Override
    public List<customerDTO> readAll() {
            log.info("*** Start customerDAO delete");
        
        conexionBD con = getConnection();
        PreparedStatement ps;
        ResultSet res;
        
        ArrayList<customerDTO> answerList = new ArrayList<>();
        customerDTO customerVal = new customerDTO ();
        try{
            ps= con.getCnn().prepareStatement(SQL_READALL);
            
            res = ps.executeQuery();
            int i= 0;
            while (res.next()) {
                answerList.add(new customerDTO (res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getTimestamp(6), res.getString(5),
                res.getString(7), res.getString(8), res.getInt(9), res.getString(10), res.getString(11), res.getString(12), res.getString(13), res.getString(14), 
                res.getString(15), res.getString(16), res.getInt(17), res.getString(18), res.getString(19), res.getString(20)));
                i++;
                if (i>0) {
                    error.setCode(0);
                    error.setMessage("");
                } else {
                    error.setCode(11);
                    error.setMessage("Error no data found");
                }
            }
        }catch (SQLException ex) {
            log.log(Level.SEVERE, "Error create customerDTO {0}", ex);
            error.setCode(-1);
            error.setMessage("Error: " + ex);
        }finally {
            con.cerrarConexion();
        }
        customerVal.setError(error);
        answerList.add(customerVal);
        log.info("*** end registroclienteDAO readAll ***");
        return answerList;    
    }

}        
    




