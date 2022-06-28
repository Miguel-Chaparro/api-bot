/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Request;

import com.dom.ws.rest.bot.DTO.answerDTO;
import java.util.List;

/**
 *
 * @author Miguel Ch
 */
public class createAnswersReq {
    private int idProject;
    private String idQuestion;
    private String tokenId;
    private List<answerDTO> answers;

    public createAnswersReq() {
    }

    public createAnswersReq(int idProject, String idQuestion, String tokenId, List<answerDTO> answers) {
        this.idProject = idProject;
        this.idQuestion = idQuestion;
        this.tokenId = tokenId;
        this.answers = answers;
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

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public List<answerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<answerDTO> answers) {
        this.answers = answers;
    }
    
    
}
