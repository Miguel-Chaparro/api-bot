/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.DTO;

import com.dom.ws.rest.bot.vo.msgError;

/**
 *
 * @author MIGUEL
 */
public class raspiDTO {
    private String raspi;
    private String ip;
    private String groupId;
    private String nodeIp;
    private msgError error;

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }

    public raspiDTO(String raspi, String ip, String groupId, String nodeIp, msgError error) {
        this.raspi = raspi;
        this.ip = ip;
        this.groupId = groupId;
        this.nodeIp = nodeIp;
        this.error = error;
    }

    public raspiDTO() {
    }

    public raspiDTO(String raspi, String ip, String groupId, String nodeIp) {
        this.raspi = raspi;
        this.ip = ip;
        this.groupId = groupId;
        this.nodeIp = nodeIp;
    }

    public String getRaspi() {
        return raspi;
    }

    public void setRaspi(String raspi) {
        this.raspi = raspi;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }
}
