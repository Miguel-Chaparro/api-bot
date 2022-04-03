/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Request;

/**
 *
 * @author Miguel Ch
 */
public class answerReq {

    private int idProject;
    private String idQuestion;

    public answerReq() {
    }

    public answerReq(int idProject, String idQuestion) {
        this.idProject = idProject;
        this.idQuestion = idQuestion;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

}
