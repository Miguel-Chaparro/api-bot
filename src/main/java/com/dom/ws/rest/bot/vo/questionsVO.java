/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.vo;

import com.dom.ws.rest.bot.DTO.answerDTO;
import java.util.List;

/**
 *
 * @author MIGUEL
 */
public class questionsVO {
    private String idQuestion;
    private String questionDesc;
    private List<answerDTO> options;

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getQuestionDesc() {
        return questionDesc;
    }

    public void setQuestionDesc(String questionDesc) {
        this.questionDesc = questionDesc;
    }

    public List<answerDTO> getOptions() {
        return options;
    }

    public void setOptions(List<answerDTO> options) {
        this.options = options;
    }

    public questionsVO(String idQuestion, String questionDesc, List<answerDTO> options) {
        this.idQuestion = idQuestion;
        this.questionDesc = questionDesc;
        this.options = options;
    }

    public questionsVO() {
    }

    public String toString() {
        return "idQuestion: " + idQuestion + " questionDesc: " + questionDesc + " options: " + options + " Options size: "
                + options.size() + "first option: " + options.get(0).getAnswerDesc() ;
    }
}
