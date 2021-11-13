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
public class customerDTO {
    private String cedulaCliente;
    private String nombreCliente;
    private String telefonoPpal;
    private String telefonoOpc;
    private Timestamp fechaCreacion;
    private String departamento;
    private String ciudad;
    private String direccion;
    private int plan;
    private String ip;
    private String ppoe;
    private String descripcion;
    private String idTecnico;
    private String latitud;
    private String longitud;
    private String nodo;
    private int pago;
    private String correo;
    private String empresa;
    private String raspi;
    private msgError error;

    public customerDTO(String cedulaCliente, String nombreCliente, String telefonoPpal, String nodo, String correo, String empresa, String raspi, msgError error) {
        this.cedulaCliente = cedulaCliente;
        this.nombreCliente = nombreCliente;
        this.telefonoPpal = telefonoPpal;
        this.nodo = nodo;
        this.correo = correo;
        this.empresa = empresa;
        this.raspi = raspi;
        this.error = error;
    }

    public customerDTO(String cedulaCliente, String nombreCliente, String telefonoPpal, String telefonoOpc, Timestamp fechaCreacion, String departamento, String ciudad, String direccion, int plan, String ip, String ppoe, String descripcion, String idTecnico, String latitud, String longitud, String nodo, int pago, String correo, String empresa, String raspi, msgError error) {
        this.cedulaCliente = cedulaCliente;
        this.nombreCliente = nombreCliente;
        this.telefonoPpal = telefonoPpal;
        this.telefonoOpc = telefonoOpc;
        this.fechaCreacion = fechaCreacion;
        this.departamento = departamento;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.plan = plan;
        this.ip = ip;
        this.ppoe = ppoe;
        this.descripcion = descripcion;
        this.idTecnico = idTecnico;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nodo = nodo;
        this.pago = pago;
        this.correo = correo;
        this.empresa = empresa;
        this.raspi = raspi;
        this.error = error;
    }

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }

    public customerDTO(String cedulaCliente, String nombreCliente, String telefonoPpal, String telefonoOpc, Timestamp fechaCreacion, String departamento, String ciudad, String direccion, int plan, String ip, String ppoe, String descripcion, String idTecnico, String latitud, String longitud, String nodo, int pago, String correo, String empresa, String raspi) {
        this.cedulaCliente = cedulaCliente;
        this.nombreCliente = nombreCliente;
        this.telefonoPpal = telefonoPpal;
        this.telefonoOpc = telefonoOpc;
        this.fechaCreacion = fechaCreacion;
        this.departamento = departamento;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.plan = plan;
        this.ip = ip;
        this.ppoe = ppoe;
        this.descripcion = descripcion;
        this.idTecnico = idTecnico;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nodo = nodo;
        this.pago = pago;
        this.correo = correo;
        this.empresa = empresa;
        this.raspi = raspi;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefonoPpal() {
        return telefonoPpal;
    }

    public void setTelefonoPpal(String telefonoPpal) {
        this.telefonoPpal = telefonoPpal;
    }

    public String getTelefonoOpc() {
        return telefonoOpc;
    }

    public void setTelefonoOpc(String telefonoOpc) {
        this.telefonoOpc = telefonoOpc;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPpoe() {
        return ppoe;
    }

    public void setPpoe(String ppoe) {
        this.ppoe = ppoe;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(String idTecnico) {
        this.idTecnico = idTecnico;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getNodo() {
        return nodo;
    }

    public void setNodo(String nodo) {
        this.nodo = nodo;
    }

    public int getPago() {
        return pago;
    }

    public void setPago(int pago) {
        this.pago = pago;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getRaspi() {
        return raspi;
    }

    public void setRaspi(String raspi) {
        this.raspi = raspi;
    }

    public customerDTO() {
    }
}
