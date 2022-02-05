/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.interfaces;
import com.dom.ws.rest.bot.DTO.requestDTO;
import java.util.List;

/**
 *
 * @author MIGUEL
 */
public class requestDAO implements interfaces<requestDTO>{
    
    private static String SQL_READMANY = "";
    private static String SQL_INSERT = "";
    private static String SQL_UPDATE = "";
    private static String SQL_READALL = "";
    

    @Override
    public boolean create(requestDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update(requestDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(requestDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public requestDTO readOne(requestDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<requestDTO> readMany(requestDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<requestDTO> readAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
