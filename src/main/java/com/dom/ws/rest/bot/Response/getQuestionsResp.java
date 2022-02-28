/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Response;

import com.dom.ws.rest.bot.DTO.answerDTO;
import com.dom.ws.rest.bot.DTO.questionsDTO;
import com.dom.ws.rest.bot.vo.msgError;
import java.util.List;

/**
 *
 * @author Teletrabajo
 */
public class getQuestionsResp {
    private msgError error; 
    private List<questionsDTO> questions;
    private List<answerDTO> answers;

    public getQuestionsResp() {
    }

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

    public List<answerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<answerDTO> answers) {
        this.answers = answers;
    }

    
}
