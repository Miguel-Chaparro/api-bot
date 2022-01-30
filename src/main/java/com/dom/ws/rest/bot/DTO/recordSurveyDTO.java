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
public class recordSurveyDTO {
    private String idWhastapp;
    private int idProject;
    private int idRecordSurvey;
    private String idQuestion;
    private String answer;
    private msgError error;

    public recordSurveyDTO() {
    }

    public recordSurveyDTO(String idWhastapp, int idProject, int idRecordSurvey, String idQuestion, String answer) {
        this.idWhastapp = idWhastapp;
        this.idProject = idProject;
        this.idRecordSurvey = idRecordSurvey;
        this.idQuestion = idQuestion;
        this.answer = answer;
    }

    public recordSurveyDTO(String idWhastapp, int idProject, int idRecordSurvey, String idQuestion, String answer, msgError error) {
        this.idWhastapp = idWhastapp;
        this.idProject = idProject;
        this.idRecordSurvey = idRecordSurvey;
        this.idQuestion = idQuestion;
        this.answer = answer;
        this.error = error;
    }

    public String getIdWhastapp() {
        return idWhastapp;
    }

    public void setIdWhastapp(String idWhastapp) {
        this.idWhastapp = idWhastapp;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public int getIdRecordSurvey() {
        return idRecordSurvey;
    }

    public void setIdRecordSurvey(int idRecordSurvey) {
        this.idRecordSurvey = idRecordSurvey;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }
}
