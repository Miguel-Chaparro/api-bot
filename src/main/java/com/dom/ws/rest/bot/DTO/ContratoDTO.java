package com.dom.ws.rest.bot.DTO;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class ContratoDTO {
    private Integer id;
    private String usuarioId;
    private Integer planInternetId;
    private String numeroContrato;
    private Date fechaInicio;
    private Date fechaFin;
    private String direccionInstalacion;
    private String estado;
    private BigDecimal precioMensual;
    private Integer diaCorte;
    private String observaciones;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer empresaId;

    public ContratoDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public Integer getPlanInternetId() { return planInternetId; }
    public void setPlanInternetId(Integer planInternetId) { this.planInternetId = planInternetId; }

    public String getNumeroContrato() { return numeroContrato; }
    public void setNumeroContrato(String numeroContrato) { this.numeroContrato = numeroContrato; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public String getDireccionInstalacion() { return direccionInstalacion; }
    public void setDireccionInstalacion(String direccionInstalacion) { this.direccionInstalacion = direccionInstalacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getPrecioMensual() { return precioMensual; }
    public void setPrecioMensual(BigDecimal precioMensual) { this.precioMensual = precioMensual; }

    public Integer getDiaCorte() { return diaCorte; }
    public void setDiaCorte(Integer diaCorte) { this.diaCorte = diaCorte; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Integer getEmpresaId() { return empresaId; }
    public void setEmpresaId(Integer empresaId) { this.empresaId = empresaId; }
}
