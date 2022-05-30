/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Response;

import com.dom.ws.rest.bot.DTO.questionsDTO;
import com.dom.ws.rest.bot.vo.msgError;
import java.util.List;

/**
 *
 * @author Miguel Ch
 */
public class createQuestionsResp {
    private msgError error; 
    private List<questionsDTO> questions;

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }

    public List<questionsDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<questionsDTO> questions) {
        this.questions = questions;
    }

    public createQuestionsResp() {
    }

    public createQuestionsResp(msgError error, List<questionsDTO> questions) {
        this.error = error;
        this.questions = questions;
    }
    
}
