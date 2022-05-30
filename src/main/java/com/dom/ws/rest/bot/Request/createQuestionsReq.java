/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Request;

import com.dom.ws.rest.bot.DTO.questionsDTO;
import java.util.List;

/**
 *
 * @author Miguel Ch
 */
public class createQuestionsReq {
    private int idProject;
    private String previousQuestion;
    private String previousAnswer;
    private String tokenId;
    private List<questionsDTO> questions;

    public createQuestionsReq(int idProject, String previousQuestion, String previousAnswer, String tokenId, List<questionsDTO> questions) {
        this.idProject = idProject;
        this.previousQuestion = previousQuestion;
        this.previousAnswer = previousAnswer;
        this.tokenId = tokenId;
        this.questions = questions;
    }

    public createQuestionsReq() {
    }

    public String getPreviousAnswer() {
        return previousAnswer;
    }

    public void setPreviousAnswer(String previousAnswer) {
        this.previousAnswer = previousAnswer;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
 
    public String getPreviousQuestion() {
        return previousQuestion;
    }

    public void setPreviousQuestion(String previousQuestion) {
        this.previousQuestion = previousQuestion;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public List<questionsDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<questionsDTO> questions) {
        this.questions = questions;
    }
    

}
