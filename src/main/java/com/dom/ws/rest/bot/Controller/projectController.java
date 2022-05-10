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
import com.dom.ws.rest.bot.vo.msgError;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Teletrabajo
 */
public class projectController {

    static final Logger log = Logger.getLogger(projectController.class.getName());

    public projectsResp getProjectUser(projectsReq req) {
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

    public projectDTO createProjects(projectDTO req) {
        log.info("***start projectController createProjects***");
        projectDTO dto, dtoReq, response = new projectDTO();
        projectsDAO dao = new projectsDAO();
        msgError error = new msgError();
        boolean statusCreate = false, statusError = false;
        dtoReq = req;
        dto = dao.readOne(dtoReq);

        if (dto.getError().getCode() == 0) {
            error.setCode(-1);
            error.setMessage("No es posible crear proyecto con el mismo nombre, favor valida e intenta nuevamente");
            response = dto;
            response.setError(error);
            statusError = true;
        } else {
            statusCreate = dao.create(req);
        }

        if (statusCreate) {
            dto = req;
            response = dao.readOne(dto);
        } else {
            error.setCode(-10);
            error.setMessage("Upss... No fue posible crear el proyecto, Favor contacta el Administrador");
            statusError = true;
        }
        if (statusError) {
            response.setError(error);
        }
        log.info("***End projectController createProjects***");

        return response;
    }
}
