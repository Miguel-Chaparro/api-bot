/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Controller;

import com.dom.ws.rest.bot.DAO.projectsDAO;
import com.dom.ws.rest.bot.DTO.projectDTO;
import com.dom.ws.rest.bot.Request.createProjectsReq;
import com.dom.ws.rest.bot.Response.projectsResp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Teletrabajo
 */
public class getProjectController {
    static final Logger logg = Logger.getLogger(getProjectController.class.getName());
    
    public projectsResp getProjects (createProjectsReq req){
        projectsResp resp = new projectsResp();
        projectsDAO dao = new projectsDAO();
        projectDTO dto = new projectDTO();
        List<projectDTO> projects = new ArrayList<>();
        dto.setIdProject(req.getIdCodeProject());
        dto.setUser(req.getIdUser());
        projects = dao.readMany(dto);
        
        resp.setProjects(projects);
        resp.setError(projects.get(0).getError());
        return resp;
    }
}
