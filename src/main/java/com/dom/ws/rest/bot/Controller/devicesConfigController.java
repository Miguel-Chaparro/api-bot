/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.ws.rest.bot.Controller;

import com.dom.ws.rest.bot.DAO.raspiDAO;
import com.dom.ws.rest.bot.DTO.raspiDTO;
import com.dom.ws.rest.bot.Request.raspiReq;
import com.dom.ws.rest.bot.Response.raspiResp;
import com.dom.ws.rest.bot.vo.msgError;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Miguel Ch
 */
public class devicesConfigController {
    static final Logger log = Logger.getLogger(devicesConfigController.class.getName());
    
    public raspiResp getConfigDevice(raspiReq req){
        log.info("***start devicesConfigController getConfigDevice***"); 
        raspiResp response = new raspiResp();
        List<raspiDTO> lista = new ArrayList<>();
        msgError errorDAO = new msgError();
        raspiDTO dto = new raspiDTO();
        raspiDAO dao = new raspiDAO();
        dto.setRaspi(req.getIdDevice());
        lista = dao.readMany(dto);
        errorDAO = lista.get(0).getError();
        response.setDevicesConfig(lista);
        response.setError(errorDAO);
        log.info("***End devicesConfigController getConfigDevice***"); 
        return response;
    }
    
    public msgError createConfigDevices (raspiDTO dto){
        log.info("***start devicesConfigController createConfigDevices***");
        msgError response = new msgError();
        raspiDAO dao = new raspiDAO();
        boolean statusCreate = false;
        statusCreate = dao.create(dto);
        if(statusCreate){
            response.setCode(0);
            response.setMessage("");
        }else{            
            response.setCode(-1);
            response.setMessage("Error al momento de crear configuración, por favor contacte al Administrador");
        }
        log.info("***end devicesConfigController createConfigDevices***");
        return response;
    }
}
