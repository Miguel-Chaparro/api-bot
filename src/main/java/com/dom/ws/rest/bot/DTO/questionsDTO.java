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
public class questionsDTO {
    private String idQuestions;
    private String questions;
    private int idProject; 
    private String idFrom;   
    private int nextQuestion;
    private int multiAnswer;
    private int minQuestions;
    private int openQuestion;
    private int endQuestions;
    private msgError error;

    public questionsDTO() {
        
    }
    
    public int getOpenQuestion() {
        return openQuestion;
    }

    public void setOpenQuestion(int openQuestion) {
        this.openQuestion = openQuestion;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public int getNextQuestion() {
        return nextQuestion;
    }

    public void setNextQuestion(int nextQuestion) {
        this.nextQuestion = nextQuestion;
    }

    public int getMultiAnswer() {
        return multiAnswer;
    }

    public void setMultiAnswer(int multiAnswer) {
        this.multiAnswer = multiAnswer;
    }

    public int getMinQuestions() {
        return minQuestions;
    }

    public void setMinQuestions(int minQuestions) {
        this.minQuestions = minQuestions;
    }

    public int getEndQuestions() {
        return endQuestions;
    }

    public void setEndQuestions(int endQuestions) {
        this.endQuestions = endQuestions;
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

    public questionsDTO(String idQuestions, int idProject, String questions, int nextQuestion, int multiAnswer, int minQuestions, int endQuestions, String idFrom, msgError error) {
        this.idQuestions = idQuestions;
        this.idProject = idProject;
        this.questions = questions;
        this.nextQuestion = nextQuestion;
        this.multiAnswer = multiAnswer;
        this.minQuestions = minQuestions;
        this.endQuestions = endQuestions;
        this.idFrom = idFrom;
        this.error = error;
    }

 

    public questionsDTO(String idQuestions, String questions, int idProject, int nextQuestion, int multiAnswer, int minQuestions,int openQuestion, int endQuestions, String idFrom) {
        this.idQuestions = idQuestions;
        this.idProject = idProject;
        this.questions = questions;
        this.nextQuestion = nextQuestion;
        this.multiAnswer = multiAnswer;
        this.minQuestions = minQuestions;
        this.openQuestion = openQuestion;
        this.endQuestions = endQuestions;
        this.idFrom = idFrom;
    }

    public String getIdQuestions() {
        return idQuestions;
    }

    public void setIdQuestions(String idQuestions) {
        this.idQuestions = idQuestions;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }
    
}
