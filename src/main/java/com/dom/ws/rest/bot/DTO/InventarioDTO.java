package com.dom.ws.rest.bot.DTO;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class InventarioDTO {
    private Integer id;
    private Integer productoId;
    private Integer empresaId;
    private Integer proveedorId;
    private String numeroSerie;
    private String macAddress;
    private String estado; // 'en_bodega', 'prestado', 'en_reparacion', 'de_baja', 'vendido'
    private String ubicacionBodega;
    private Date fechaCompra;
    private BigDecimal costoAdquisicion;
    private String notas;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public InventarioDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }

    public Integer getEmpresaId() { return empresaId; }
    public void setEmpresaId(Integer empresaId) { this.empresaId = empresaId; }

    public Integer getProveedorId() { return proveedorId; }
    public void setProveedorId(Integer proveedorId) { this.proveedorId = proveedorId; }

    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }

    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUbicacionBodega() { return ubicacionBodega; }
    public void setUbicacionBodega(String ubicacionBodega) { this.ubicacionBodega = ubicacionBodega; }

    public Date getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(Date fechaCompra) { this.fechaCompra = fechaCompra; }

    public BigDecimal getCostoAdquisicion() { return costoAdquisicion; }
    public void setCostoAdquisicion(BigDecimal costoAdquisicion) { this.costoAdquisicion = costoAdquisicion; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
