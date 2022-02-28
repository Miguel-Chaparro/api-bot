/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DTO;

import com.dom.ws.rest.bot.vo.msgError;
import java.sql.Timestamp;

/**
 *
 * @author Teletrabajo
 */
public class projectDTO {
    private int idProject;
    private String user;
    private String projectDesc;
    private Timestamp dateProject;
    private int openProject;
    private Timestamp endProject;
    private msgError error;

    public Timestamp getDateProject() {
        return dateProject;
    }

    public void setDateProject(Timestamp dateProject) {
        this.dateProject = dateProject;
    }

    public int getOpenProject() {
        return openProject;
    }

    public void setOpenProject(int openProject) {
        this.openProject = openProject;
    }

    public Timestamp getEndProject() {
        return endProject;
    }

    public void setEndProject(Timestamp endProject) {
        this.endProject = endProject;
    }

    public projectDTO(int idProject, String user, String projectDesc, Timestamp dateProject, int openProject, Timestamp endProject, msgError error) {
        this.idProject = idProject;
        this.user = user;
        this.projectDesc = projectDesc;
        this.dateProject = dateProject;
        this.openProject = openProject;
        this.endProject = endProject;
        this.error = error;
    }

    public projectDTO(int idProject, String user, String projectDesc, Timestamp dateProject, int openProject, Timestamp endProject) {
        this.idProject = idProject;
        this.user = user;
        this.projectDesc = projectDesc;
        this.dateProject = dateProject;
        this.openProject = openProject;
        this.endProject = endProject;
    }

    
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
