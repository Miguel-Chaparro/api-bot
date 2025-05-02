package com.dom.ws.rest.bot.DTO;

public class EmpresaDTO {
    private int id;
    private String nombre;
    private String nit;
    private String direccion;
    private String telefono;
    private String email;
    private int estado;

    public EmpresaDTO() {}

    public EmpresaDTO(int id, String nombre, String nit, String direccion, String telefono, String email, int estado) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.estado = estado;
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
}
