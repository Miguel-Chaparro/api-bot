package com.dom.ws.rest.bot.DTO;

public class EmpresaDTO {
    private int id;
    private String nombre;
    private String nit;
    private String direccion;
    private String telefono;
    private String email;
    private int estado;
    private String numeroChatbot;
    private Double precio;
    private Boolean usaPasarela;
    private Double tarifaFijaPasarela;
    private Double porcentajePasarela;
    private Boolean cobrarPasarela;
    private String host;

    public EmpresaDTO() {}

    public EmpresaDTO(int id, String nombre, String nit, String direccion, String telefono, String email, int estado, String numeroChatbot) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.estado = estado;
        this.numeroChatbot = numeroChatbot;
    }

    public EmpresaDTO(int id, String nombre, String nit, String direccion, String telefono, String email, int estado, String numeroChatbot, Double precio, Boolean usaPasarela, Double tarifaFijaPasarela, Double porcentajePasarela, Boolean cobrarPasarela, String host) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.estado = estado;
        this.numeroChatbot = numeroChatbot;
        this.precio = precio;
        this.usaPasarela = usaPasarela;
        this.tarifaFijaPasarela = tarifaFijaPasarela;
        this.porcentajePasarela = porcentajePasarela;
        this.cobrarPasarela = cobrarPasarela;
        this.host = host;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public String getNumeroChatbot() { return numeroChatbot; }
    public void setNumeroChatbot(String numeroChatbot) { this.numeroChatbot = numeroChatbot; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Boolean getUsaPasarela() { return usaPasarela; }
    public void setUsaPasarela(Boolean usaPasarela) { this.usaPasarela = usaPasarela; }

    public Double getTarifaFijaPasarela() { return tarifaFijaPasarela; }
    public void setTarifaFijaPasarela(Double tarifaFijaPasarela) { this.tarifaFijaPasarela = tarifaFijaPasarela; }

    public Double getPorcentajePasarela() { return porcentajePasarela; }
    public void setPorcentajePasarela(Double porcentajePasarela) { this.porcentajePasarela = porcentajePasarela; }

    public Boolean getCobrarPasarela() { return cobrarPasarela; }
    public void setCobrarPasarela(Boolean cobrarPasarela) { this.cobrarPasarela = cobrarPasarela; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
}
