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
    private String flgCommand;
    private String command;
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

    public answerResp(msgError error, String whatsapp, String raspi, String flgCommand, String command, questionsVO question) {
        this.error = error;
        this.whatsapp = whatsapp;
        this.raspi = raspi;
        this.flgCommand = flgCommand;
        this.command = command;
        this.question = question;
    }

    public String getFlgCommand() {
        return flgCommand;
    }

    public void setFlgCommand(String flgCommand) {
        this.flgCommand = flgCommand;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String toString() {
        return "answerResp [error=" + error + ", whatsapp=" + whatsapp + ", raspi=" + raspi + ", flgCommand=" + flgCommand
                + ", command=" + command + ", question=" + question + "+ " + question.toString() + "]";
    }
    
}
