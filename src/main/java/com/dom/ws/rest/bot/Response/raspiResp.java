/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dom.ws.rest.bot.Response;

import com.dom.ws.rest.bot.DTO.raspiDTO;
import com.dom.ws.rest.bot.vo.msgError;
import java.util.List;

/**
 *
 * @author Miguel Ch
 */
public class raspiResp {

    private msgError error;
    private List<raspiDTO> devicesConfig;

    public raspiResp() {
    }

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }

    public List<raspiDTO> getDevicesConfig() {
        return devicesConfig;
    }

    public void setDevicesConfig(List<raspiDTO> devicesConfig) {
        this.devicesConfig = devicesConfig;
    }

}
