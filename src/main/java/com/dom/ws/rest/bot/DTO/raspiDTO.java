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
    private String nodeIp;
    private String groupId;
    private String topic;
    private int idChannel;
    private String idChat;
    private int flgMain;
    private int idDevices;
    private msgError error;

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }

    public int getIdDevices() {
        return idDevices;
    }

    public void setIdDevices(int idDevices) {
        this.idDevices = idDevices;
    }
    

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getIdChannel() {
        return idChannel;
    }

    public void setIdChannel(int idChannel) {
        this.idChannel = idChannel;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public int getFlgMain() {
        return flgMain;
    }

    public void setFlgMain(int flgMain) {
        this.flgMain = flgMain;
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

    public raspiDTO() {
    }

    public raspiDTO(String raspi, String ip, String nodeIp, String groupId, String topic, int idChannel, String idChat, int flgMain, int idDevices, msgError error) {
        this.raspi = raspi;
        this.ip = ip;
        this.nodeIp = nodeIp;
        this.groupId = groupId;
        this.topic = topic;
        this.idChannel = idChannel;
        this.idChat = idChat;
        this.flgMain = flgMain;
        this.idDevices = idDevices;
        this.error = error;
    }

    public raspiDTO(String raspi, String ip, String nodeIp, String groupId, String topic, int idChannel, String idChat, int flgMain, int idDevices) {
        this.raspi = raspi;
        this.ip = ip;
        this.nodeIp = nodeIp;
        this.groupId = groupId;
        this.topic = topic;
        this.idChannel = idChannel;
        this.idChat = idChat;
        this.flgMain = flgMain;
        this.idDevices = idDevices;
    }



}
