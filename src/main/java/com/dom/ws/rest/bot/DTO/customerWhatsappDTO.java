/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DTO;

import com.dom.ws.rest.bot.vo.msgError;
import java.sql.Timestamp;

/**
 *
 * @author MIGUEL
 */
public class customerWhatsappDTO {
    private String idWhatsapp;
    private String name;
    private String idCustomer;
    private String idQuestions;
    private Timestamp date;
    private int pendingState;
    private String pendingDescription; 
    private int idProject;
    private msgError error;

    public customerWhatsappDTO(String idWhatsapp, String name, String idCustomer, String idQuestions, Timestamp date, int pendingState, String pendingDescription, int idProject, msgError error) {
        this.idWhatsapp = idWhatsapp;
        this.name = name;
        this.idCustomer = idCustomer;
        this.idQuestions = idQuestions;
        this.date = date;
        this.pendingState = pendingState;
        this.pendingDescription = pendingDescription;
        this.idProject = idProject;
        this.error = error;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public customerWhatsappDTO(String idWhatsapp, String name, String idCustomer, String idQuestions, Timestamp date, int pendingState, String pendingDescription) {
        this.idWhatsapp = idWhatsapp;
        this.name = name;
        this.idCustomer = idCustomer;
        this.idQuestions = idQuestions;
        this.date = date;
        this.pendingState = pendingState;
        this.pendingDescription = pendingDescription;
    }

    public customerWhatsappDTO() {
    }

    public customerWhatsappDTO(String idWhatsapp, String name, String idCustomer, String idQuestions, Timestamp date, int pendingState, String pendingDescription, msgError error) {
        this.idWhatsapp = idWhatsapp;
        this.name = name;
        this.idCustomer = idCustomer;
        this.idQuestions = idQuestions;
        this.date = date;
        this.pendingState = pendingState;
        this.pendingDescription = pendingDescription;
        this.error = error;
    }

    public String getIdWhatsapp() {
        return idWhatsapp;
    }

    public void setIdWhatsapp(String idWhatsapp) {
        this.idWhatsapp = idWhatsapp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdQuestions() {
        return idQuestions;
    }

    public void setIdQuestions(String idQuestions) {
        this.idQuestions = idQuestions;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getPendingState() {
        return pendingState;
    }

    public void setPendingState(int pendingState) {
        this.pendingState = pendingState;
    }

    public String getPendingDescription() {
        return pendingDescription;
    }

    public void setPendingDescription(String pendingDescription) {
        this.pendingDescription = pendingDescription;
    }

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }

   
    
    
}
