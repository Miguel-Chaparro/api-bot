/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DTO;

import com.dom.ws.rest.bot.vo.msgError;

/**
 *
 * @author MIGUEL
 */
public class answerDTO {
    private String idQuestion;
    private int answerId;
    private String answerDesc;
    private int idProject;
    private String command;
    private int flgEnd;
    private int flgCommand;
    private msgError error;

    public answerDTO() {
    }

    public answerDTO(String idQuestion, int answerId, String answerDesc, int idProject, String command, int flgEnd, int flgCommand, msgError error) {
        this.idQuestion = idQuestion;
        this.answerId = answerId;
        this.answerDesc = answerDesc;
        this.idProject = idProject;
        this.command = command;
        this.flgEnd = flgEnd;
        this.flgCommand = flgCommand;
        this.error = error;
    }

    public answerDTO(String idQuestion, int answerId, String answerDesc, int idProject, String command, int flgEnd, int flgCommand) {
        this.idQuestion = idQuestion;
        this.answerId = answerId;
        this.answerDesc = answerDesc;
        this.idProject = idProject;
        this.command = command;
        this.flgEnd = flgEnd;
        this.flgCommand = flgCommand;
    }
    
    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getFlgEnd() {
        return flgEnd;
    }

    public void setFlgEnd(int flgEnd) {
        this.flgEnd = flgEnd;
    }

    public int getFlgCommand() {
        return flgCommand;
    }

    public void setFlgCommand(int flgCommand) {
        this.flgCommand = flgCommand;
    }

   

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getAnswerDesc() {
        return answerDesc;
    }

    public void setAnswerDesc(String answerDesc) {
        this.answerDesc = answerDesc;
    }


    
}
