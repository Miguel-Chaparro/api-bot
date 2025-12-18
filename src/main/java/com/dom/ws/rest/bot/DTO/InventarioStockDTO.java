package com.dom.ws.rest.bot.DTO;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class InventarioStockDTO {
    private Integer id;
    private Integer productoId;
    private Integer empresaId;
    private BigDecimal cantidadEnBodega;
    private BigDecimal costoPromedio;
    private String ubicacionBodega;
    private Timestamp updatedAt;

    public InventarioStockDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }

    public Integer getEmpresaId() { return empresaId; }
    public void setEmpresaId(Integer empresaId) { this.empresaId = empresaId; }

    public BigDecimal getCantidadEnBodega() { return cantidadEnBodega; }
    public void setCantidadEnBodega(BigDecimal cantidadEnBodega) { this.cantidadEnBodega = cantidadEnBodega; }

    public BigDecimal getCostoPromedio() { return costoPromedio; }
    public void setCostoPromedio(BigDecimal costoPromedio) { this.costoPromedio = costoPromedio; }

    public String getUbicacionBodega() { return ubicacionBodega; }
    public void setUbicacionBodega(String ubicacionBodega) { this.ubicacionBodega = ubicacionBodega; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
