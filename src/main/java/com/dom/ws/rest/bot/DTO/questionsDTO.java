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
    private msgError error;

    public questionsDTO() {
        
    }

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }

    public questionsDTO(String idQuestions, String questions, msgError error) {
        this.idQuestions = idQuestions;
        this.questions = questions;
        this.error = error;
    }

    public questionsDTO(String idQuestions, String questions) {
        this.idQuestions = idQuestions;
        this.questions = questions;
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
