/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Controller;

import com.dom.ws.rest.bot.DAO.projectsDAO;
import com.dom.ws.rest.bot.DTO.projectDTO;
import com.dom.ws.rest.bot.Request.projectsReq;
import com.dom.ws.rest.bot.Response.projectsResp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Teletrabajo
 */
public class projectController {
    static final Logger log = Logger.getLogger(projectController.class.getName());
    
    public projectsResp getProjectUser(projectsReq req){
        log.info("***start projectController getProjectUser***");
        projectsResp resp = new projectsResp();
        List<projectDTO> array = new ArrayList();
        projectDTO dto = new projectDTO();
        dto.setUser(req.getIdUser());
        projectsDAO dao = new projectsDAO();
        array = dao.readMany(dto);
        resp.setProjects(array);
        resp.setError(array.get(0).getError());
        log.info("***end projectController getProjectUser***");
        return resp;
    }
}
