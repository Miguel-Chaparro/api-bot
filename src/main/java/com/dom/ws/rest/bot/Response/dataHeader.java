/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Response;

/**
 *
 * @author Teletrabajo
 */
public class dataHeader {
    private String state;
    private int code;
    private String msjResponse;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsjResponse() {
        return msjResponse;
    }

    public void setMsjResponse(String msjResponse) {
        this.msjResponse = msjResponse;
    }

    public dataHeader(String state, int code, String msjResponse) {
        this.state = state;
        this.code = code;
        this.msjResponse = msjResponse;
    }

    public dataHeader() {
    }
    
    
}
