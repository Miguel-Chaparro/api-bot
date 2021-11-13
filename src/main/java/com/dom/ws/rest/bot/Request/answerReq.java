/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Request;

/**
 *
 * @author MIGUEL
 */
public class answerReq {
  private String whatsappId;
  private String answer;

    public answerReq() {
    }

    public answerReq(String whatsappId, String answer) {
        this.whatsappId = whatsappId;
        this.answer = answer;
    }

    public String getWhatsappId() {
        return whatsappId;
    }

    public void setWhatsappId(String whatsappId) {
        this.whatsappId = whatsappId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
  
}
