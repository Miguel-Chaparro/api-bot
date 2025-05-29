package com.dom.ws.rest.bot.DTO;

public class RaspberryDTO {
    private int id;
    private String nameDeviceAdmin;
    private String nameDeviceTec;
    private String topic;
    private boolean mikrotik;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNameDeviceAdmin() { return nameDeviceAdmin; }
    public void setNameDeviceAdmin(String nameDeviceAdmin) { this.nameDeviceAdmin = nameDeviceAdmin; }
    public String getNameDeviceTec() { return nameDeviceTec; }
    public void setNameDeviceTec(String nameDeviceTec) { this.nameDeviceTec = nameDeviceTec; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public boolean isMikrotik() { return mikrotik; }
    public void setMikrotik(boolean mikrotik) { this.mikrotik = mikrotik; }
}
