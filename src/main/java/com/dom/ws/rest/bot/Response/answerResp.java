/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Response;

import com.dom.ws.rest.bot.vo.msgError;
import com.dom.ws.rest.bot.vo.questionsVO;

/**
 *
 * @author MIGUEL
 */
public class answerResp {
    private msgError error;          
    private String whatsapp;
    private String raspi;
    private questionsVO question;

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getRaspi() {
        return raspi;
    }

    public void setRaspi(String raspi) {
        this.raspi = raspi;
    }

    public questionsVO getQuestion() {
        return question;
    }

    public void setQuestion(questionsVO question) {
        this.question = question;
    }

    public answerResp() {
    }

    public answerResp(msgError error, String whatsapp, String raspi, questionsVO question) {
        this.error = error;
        this.whatsapp = whatsapp;
        this.raspi = raspi;
        this.question = question;
    }
    
    
    

   

}
