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
    private String idFrom;
    private String name;
    private String idCustomer;
    private String idQuestions;
    private Timestamp date;
    private int pendingState;
    private String pendingDescription;
    private int idProject;
    private int flg_devices;
    private String devices;
    private String command;
    private msgError error;

    public customerWhatsappDTO(String idWhatsapp, String name, String idCustomer, String idQuestions, Timestamp date,
            int pendingState, String pendingDescription, int idProject, int flg_devices, String device, String command, String idFrom,
            msgError error) {
        this.idWhatsapp = idWhatsapp;
        this.name = name;
        this.idCustomer = idCustomer;
        this.idQuestions = idQuestions;
        this.date = date;
        this.pendingState = pendingState;
        this.pendingDescription = pendingDescription;
        this.idProject = idProject;
        this.flg_devices = flg_devices;
        this.devices = device;
        this.command = command;
        this.idFrom = idFrom;
        this.error = error;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public customerWhatsappDTO(String idWhatsapp, String name, String idCustomer, String idQuestions, Timestamp date,
            int pendingState, String pendingDescription, int idProject, int flg_devices, String device, String command, String idFrom) {
        this.idWhatsapp = idWhatsapp;
        this.name = name;
        this.idCustomer = idCustomer;
        this.idQuestions = idQuestions;
        this.date = date;
        this.pendingState = pendingState;
        this.pendingDescription = pendingDescription;
        this.idProject = idProject;
        this.flg_devices = flg_devices;
        this.devices = device;
        this.command = command;
        this.idFrom = idFrom;
        
    }

    public customerWhatsappDTO() {
    }

    public customerWhatsappDTO(String idWhatsapp, String name, String idCustomer, String idQuestions, Timestamp date,
            int pendingState, String pendingDescription, String IdFrom, msgError error) {
        this.idWhatsapp = idWhatsapp;
        this.name = name;
        this.idCustomer = idCustomer;
        this.idQuestions = idQuestions;
        this.date = date;
        this.pendingState = pendingState;
        this.pendingDescription = pendingDescription;
        this.idFrom = IdFrom;
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

    public int getFlg_devices() {
        return flg_devices;
    }

    public void setFlg_devices(int flg_devices) {
        this.flg_devices = flg_devices;
    }

    public String getDevices() {
        return devices;
    }

    public void setDevices(String devices) {
        this.devices = devices;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(String idFrom) {
        this.idFrom = idFrom;
    }

    public String toString() {
        return "customerWhatsappDTO{" + "idWhatsapp=" + idWhatsapp + ", name=" + name + ", idCustomer=" + idCustomer
                + ", idQuestions=" + idQuestions + ", date=" + date + ", pendingState=" + pendingState
                + ", pendingDescription=" + pendingDescription + ", idProject=" + idProject + ", flg_devices="
                + flg_devices + ", devices=" + devices + ", command=" + command + ", error=" + error + ", idFrom=" + idFrom + '}';
    }
}
