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
    private String idUser;
    private String projectDesc;
    private Timestamp dateProject;
    private int openProject;
    private Timestamp endProject;
    private int statusProject;
    private int flgEndProject;
    private msgError error;
    private String tokenId;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public int getStatusProject() {
        return statusProject;
    }

    public void setStatusProject(int statusProject) {
        this.statusProject = statusProject;
    }

    public int getFlgEndProject() {
        return flgEndProject;
    }

    public void setFlgEndProject(int flgEndProject) {
        this.flgEndProject = flgEndProject;
    }

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



    
    public projectDTO(int idProject, String idUser, String projectDesc) {
        this.idProject = idProject;
        this.idUser = idUser;
        this.projectDesc = projectDesc;
    }

    public projectDTO(int idProject, String idUser, String projectDesc, msgError error) {
        this.idProject = idProject;
        this.idUser = idUser;
        this.projectDesc = projectDesc;
        this.error = error;
    }

    public projectDTO(int idProject, String idUser, String projectDesc, Timestamp dateProject, int openProject, Timestamp endProject, int statusProject, int flgEndProject, msgError error) {
        this.idProject = idProject;
        this.idUser = idUser;
        this.projectDesc = projectDesc;
        this.dateProject = dateProject;
        this.openProject = openProject;
        this.endProject = endProject;
        this.statusProject = statusProject;
        this.flgEndProject = flgEndProject;
        this.error = error;
    }

    public projectDTO(int idProject, String idUser, String projectDesc, Timestamp dateProject, int openProject, Timestamp endProject, int statusProject, int flgEndProject) {
        this.idProject = idProject;
        this.idUser = idUser;
        this.projectDesc = projectDesc;
        this.dateProject = dateProject;
        this.openProject = openProject;
        this.endProject = endProject;
        this.statusProject = statusProject;
        this.flgEndProject = flgEndProject;
    }
    
    

    public projectDTO() {
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setUser(String idUser) {
        this.idUser = idUser;
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
