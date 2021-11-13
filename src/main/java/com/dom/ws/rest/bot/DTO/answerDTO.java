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
    private msgError error;

    public answerDTO() {
    }

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }

    public answerDTO(String idQuestion, int answerId, String answerDesc, msgError error) {
        this.idQuestion = idQuestion;
        this.answerId = answerId;
        this.answerDesc = answerDesc;
        this.error = error;
    }

    public answerDTO(String idQuestion, int answerId, String answerDesc) {
        this.idQuestion = idQuestion;
        this.answerId = answerId;
        this.answerDesc = answerDesc;
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
