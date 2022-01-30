/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DTO;

import com.dom.ws.rest.bot.vo.msgError;

/**
 *
 * @author Teletrabajo
 */
public class projectDTO {
    private int idProject;
    private String user;
    private String projectDesc;
    private msgError error;

    public projectDTO(int idProject, String user, String projectDesc) {
        this.idProject = idProject;
        this.user = user;
        this.projectDesc = projectDesc;
    }

    public projectDTO(int idProject, String user, String projectDesc, msgError error) {
        this.idProject = idProject;
        this.user = user;
        this.projectDesc = projectDesc;
        this.error = error;
    }

    public projectDTO() {
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }
    
}
