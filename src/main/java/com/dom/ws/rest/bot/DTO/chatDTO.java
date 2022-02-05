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
public class chatDTO {
    private int id;
    private String idWhatsapp;
    private String message;
    private String idCustomer;
    private Timestamp time;
    private msgError error;

    public chatDTO(int id, String idWhatsapp, String message, String idCustomer, Timestamp time) {
        this.id = id;
        this.idWhatsapp = idWhatsapp;
        this.message = message;
        this.idCustomer = idCustomer;
        this.time = time;
    }

    public chatDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }

    public chatDTO(String idWhatsapp, String message, String idCustomer, Timestamp time, msgError error) {
        this.idWhatsapp = idWhatsapp;
        this.message = message;
        this.idCustomer = idCustomer;
        this.time = time;
        this.error = error;
    }

    public chatDTO(int id, String idWhatsapp, String message, String idCustomer, Timestamp time, msgError error) {
        this.id = id;
        this.idWhatsapp = idWhatsapp;
        this.message = message;
        this.idCustomer = idCustomer;
        this.time = time;
        this.error = error;
    }
    

 
    
    public String getIdWhatsapp() {
        return idWhatsapp;
    }

    public void setIdWhatsapp(String idWhatsapp) {
        this.idWhatsapp = idWhatsapp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public chatDTO(String idWhatsapp, String message, String idCustomer) {
        this.idWhatsapp = idWhatsapp;
        this.message = message;
        this.idCustomer = idCustomer;
    }
}
