/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Response;

import com.dom.ws.rest.bot.DTO.projectDTO;
import com.dom.ws.rest.bot.vo.msgError;
import java.util.List;

/**
 *
 * @author Teletrabajo
 */
public class projectsResp {
    private msgError error; 
    private List<projectDTO> projects;

    public projectsResp() {
    }

    public projectsResp(msgError error, List<projectDTO> projects) {
        this.error = error;
        this.projects = projects;
    }

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }

    public List<projectDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<projectDTO> projects) {
        this.projects = projects;
    }
    
}
