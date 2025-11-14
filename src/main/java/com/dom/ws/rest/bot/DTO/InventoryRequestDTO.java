package com.dom.ws.rest.bot.DTO;

import java.math.BigDecimal;

public class InventoryRequestDTO {
    private Integer inventarioId;
    private String notas;
    private BigDecimal precioAsignacion;

    public InventoryRequestDTO() {}

    public Integer getInventarioId() { return inventarioId; }
    public void setInventarioId(Integer inventarioId) { this.inventarioId = inventarioId; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public BigDecimal getPrecioAsignacion() { return precioAsignacion; }
    public void setPrecioAsignacion(BigDecimal precioAsignacion) { this.precioAsignacion = precioAsignacion; }
}
