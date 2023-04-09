/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DTO;

import java.sql.Timestamp;

import com.dom.ws.rest.bot.vo.msgError;

/**
 *
 * @author Teletrabajo
 */
public class recordSurveyDTO {
    private String idWhastapp;
    private int idProject;
    private int idRecordSurvey;
    private String idFrom;
    private String idQuestion;
    private String openAnswer;
    private int answer;
    private String multiAnswer;
    private Timestamp fecha;
    private msgError error;

    public recordSurveyDTO() {
    }

    public recordSurveyDTO(String idWhastapp, String idFrom, int idProject, int idRecordSurvey, String idQuestion, String openAnswer, int answer, String multiAnswer, Timestamp fecha) {
        this.idWhastapp = idWhastapp;
        this.idProject = idProject;
        this.idRecordSurvey = idRecordSurvey;
        this.idQuestion = idQuestion;
        this.answer = answer;
        this.openAnswer = openAnswer;
        this.idFrom = idFrom;
        this.multiAnswer = multiAnswer;
        this.fecha = fecha;
    }

    public recordSurveyDTO(String idWhastapp, String idFrom, int idProject, int idRecordSurvey, String idQuestion, String openAnswer, int answer,String multiAnswer, Timestamp fecha, msgError error) {
        this.idWhastapp = idWhastapp;
        this.idProject = idProject;
        this.idRecordSurvey = idRecordSurvey;
        this.idQuestion = idQuestion;
        this.openAnswer = openAnswer;
        this.answer = answer;
        this.idFrom = idFrom;
        this.multiAnswer = multiAnswer;
        this.fecha = fecha;
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

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }
    public String getIdFrom() {
        return idFrom;
    }
    public void setIdFrom(String idFrom) {
        this.idFrom = idFrom;
    }

    public String getOpenAnswer() {
        return openAnswer;
    }

    public void setOpenAnswer(String openAnswer) {
        this.openAnswer = openAnswer;
    }

    public String getMultiAnswer() {
        return multiAnswer;
    }

    public void setMultiAnswer(String multiAnswer) {
        this.multiAnswer = multiAnswer;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
