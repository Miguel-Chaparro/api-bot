/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Response;

import com.dom.ws.rest.bot.DTO.answerDTO;
import com.dom.ws.rest.bot.vo.msgError;
import java.util.List;

/**
 *
 * @author Miguel Ch
 */
public class getAnswerResp {
    private msgError error; 
    private List<answerDTO> answers;

    public getAnswerResp() {
    }

    public getAnswerResp(msgError error, List<answerDTO> answers) {
        this.error = error;
        this.answers = answers;
    }

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }

    public List<answerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<answerDTO> answers) {
        this.answers = answers;
    }
    
}
