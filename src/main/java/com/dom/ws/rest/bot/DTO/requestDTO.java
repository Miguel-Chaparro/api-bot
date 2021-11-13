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
public class requestDTO {
    private int id;
    private String nombre;
    private String direccion;
    private String latitud;
    private String longitud;
    private String descripcion;
    private String telefono;
    private String correo;
    private String idWhatsapp;
    private Timestamp datePending;
    private Timestamp dateSolved;
    private msgError error;

    public msgError getError() {
        return error;
    }

    public void setError(msgError error) {
        this.error = error;
    }

    public requestDTO(int id, String nombre, String direccion, String latitud, String longitud, String descripcion, String telefono, String correo, String idWhatsapp, Timestamp datePending, Timestamp dateSolved, msgError error) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.correo = correo;
        this.idWhatsapp = idWhatsapp;
        this.datePending = datePending;
        this.dateSolved = dateSolved;
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getIdWhatsapp() {
        return idWhatsapp;
    }

    public void setIdWhatsapp(String idWhatsapp) {
        this.idWhatsapp = idWhatsapp;
    }

    public Timestamp getDatePending() {
        return datePending;
    }

    public void setDatePending(Timestamp datePending) {
        this.datePending = datePending;
    }

    public Timestamp getDateSolved() {
        return dateSolved;
    }

    public void setDateSolved(Timestamp dateSolved) {
        this.dateSolved = dateSolved;
    }

    public requestDTO(int id, String nombre, String direccion, String latitud, String longitud, String descripcion, String telefono, String correo, String idWhatsapp, Timestamp datePending, Timestamp dateSolved) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.correo = correo;
        this.idWhatsapp = idWhatsapp;
        this.datePending = datePending;
        this.dateSolved = dateSolved;
    }
    
}
