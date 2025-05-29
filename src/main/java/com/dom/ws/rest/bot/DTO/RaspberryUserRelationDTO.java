package com.dom.ws.rest.bot.DTO;

public class RaspberryUserRelationDTO {
    private int raspberryId;
    private String userNumber;
    private String role;
    private String name;
    private String topic;
    private String device;
    private String userId;

    public int getRaspberryId() { return raspberryId; }
    public void setRaspberryId(int raspberryId) { this.raspberryId = raspberryId; }
    public String getUserNumber() { return userNumber; }
    public void setUserNumber(String userNumber) { this.userNumber = userNumber; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getDevice() { return device; }
    public void setDevice(String device) { this.device = device; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
