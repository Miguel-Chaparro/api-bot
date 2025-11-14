package com.dom.ws.rest.bot.DTO;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ContratoServicioDetalleDTO {
    private Integer id;
    private Integer contratoServicioId;
    private Integer inventarioId;
    private Timestamp fechaAsignacion;
    private BigDecimal precioAsignacion;

    public ContratoServicioDetalleDTO() {}

    public ContratoServicioDetalleDTO(Integer contratoServicioId, Integer inventarioId, 
                                       Timestamp fechaAsignacion, BigDecimal precioAsignacion) {
        this.contratoServicioId = contratoServicioId;
        this.inventarioId = inventarioId;
        this.fechaAsignacion = fechaAsignacion;
        this.precioAsignacion = precioAsignacion;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getContratoServicioId() { return contratoServicioId; }
    public void setContratoServicioId(Integer contratoServicioId) { this.contratoServicioId = contratoServicioId; }

    public Integer getInventarioId() { return inventarioId; }
    public void setInventarioId(Integer inventarioId) { this.inventarioId = inventarioId; }

    public Timestamp getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(Timestamp fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }

    public BigDecimal getPrecioAsignacion() { return precioAsignacion; }
    public void setPrecioAsignacion(BigDecimal precioAsignacion) { this.precioAsignacion = precioAsignacion; }
}
