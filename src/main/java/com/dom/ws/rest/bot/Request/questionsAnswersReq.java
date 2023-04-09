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
public class questionsAnswersReq {

    private String whatsappId;
    private String answer;
    private String idFrom;
    private String tokenId;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public questionsAnswersReq() {
    }

    public questionsAnswersReq(String whatsappId, String answer, String idFrom) {
        this.whatsappId = whatsappId;
        this.answer = answer;
        this.idFrom = idFrom;
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

    public String getIdFrom() {
        return idFrom;
    }
    
    public void setIdFrom(String idFrom) {
        this.idFrom = idFrom;
    }

}
