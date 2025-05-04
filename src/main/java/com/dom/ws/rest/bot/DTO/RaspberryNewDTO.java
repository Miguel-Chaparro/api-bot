package com.dom.ws.rest.bot.DTO;

import java.util.List;

public class RaspberryNewDTO {
    public static class RaspiUserDTO {
        private String number;
        private String topic;
        private String name;
        private String device;
        // getters y setters
        public String getNumber() { return number; }
        public void setNumber(String number) { this.number = number; }
        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDevice() { return device; }
        public void setDevice(String device) { this.device = device; }
    }
    public static class RaspiIpDTO {
        private String ip;
        private String nodo;
        private String group;
        private int id;
        // getters y setters
        public String getIp() { return ip; }
        public void setIp(String ip) { this.ip = ip; }
        public String getNodo() { return nodo; }
        public void setNodo(String nodo) { this.nodo = nodo; }
        public String getGroup() { return group; }
        public void setGroup(String group) { this.group = group; }
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
    }
    public static class RaspiDeviceDTO {
        private String device;
        private String ip;
        private String port;
        private String user;
        private String password;
        private String service;
        private boolean typeMicrotik;
        // getters y setters
        public String getDevice() { return device; }
        public void setDevice(String device) { this.device = device; }
        public String getIp() { return ip; }
        public void setIp(String ip) { this.ip = ip; }
        public String getPort() { return port; }
        public void setPort(String port) { this.port = port; }
        public String getUser() { return user; }
        public void setUser(String user) { this.user = user; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getService() { return service; }
        public void setService(String service) { this.service = service; }
        public boolean isTypeMicrotik() { return typeMicrotik; }
        public void setTypeMicrotik(boolean typeMicrotik) { this.typeMicrotik = typeMicrotik; }
    }

    private List<RaspiUserDTO> admin;
    private List<RaspiUserDTO> operator;
    private List<RaspiIpDTO> ips;
    private List<RaspiDeviceDTO> devices;
    private boolean mikrotik;
    private String nameDeviceAdmin;
    private String nameDeviceTec;

    // getters y setters
    public List<RaspiUserDTO> getAdmin() { return admin; }
    public void setAdmin(List<RaspiUserDTO> admin) { this.admin = admin; }
    public List<RaspiUserDTO> getOperator() { return operator; }
    public void setOperator(List<RaspiUserDTO> operator) { this.operator = operator; }
    public List<RaspiIpDTO> getIps() { return ips; }
    public void setIps(List<RaspiIpDTO> ips) { this.ips = ips; }
    public List<RaspiDeviceDTO> getDevices() { return devices; }
    public void setDevices(List<RaspiDeviceDTO> devices) { this.devices = devices; }
    public boolean isMikrotik() { return mikrotik; }
    public void setMikrotik(boolean mikrotik) { this.mikrotik = mikrotik; }
    public String getNameDeviceAdmin() { return nameDeviceAdmin; }
    public void setNameDeviceAdmin(String nameDeviceAdmin) { this.nameDeviceAdmin = nameDeviceAdmin; }
    public String getNameDeviceTec() { return nameDeviceTec; }
    public void setNameDeviceTec(String nameDeviceTec) { this.nameDeviceTec = nameDeviceTec; }
}
