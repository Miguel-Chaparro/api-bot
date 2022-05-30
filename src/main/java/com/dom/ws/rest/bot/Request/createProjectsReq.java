/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Request;

/**
 *
 * @author Teletrabajo
 */
public class createProjectsReq {
    
    private String idUser;
    private int idCodeProject;
    private String tokenId;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
    
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getIdCodeProject() {
        return idCodeProject;
    }

    public void setIdCodeProject(int idCodeProject) {
        this.idCodeProject = idCodeProject;
    }

    public createProjectsReq(String idUser, int idCodeProject) {
        this.idUser = idUser;
        this.idCodeProject = idCodeProject;
    }

    public createProjectsReq() {
    }
    
}
