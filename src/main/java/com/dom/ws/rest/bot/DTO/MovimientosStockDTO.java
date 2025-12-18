package com.dom.ws.rest.bot.DTO;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class MovimientosStockDTO {
    private Integer id;
    private Integer productoId;
    private Integer empresaId;
    private String empleadoId; // ID del usuario que ejecuta la petición
    private String clienteId;  // ID del usuario customer (opcional)
    private String tipoMovimiento; // 'compra', 'asignacion_cliente', 'devolucion_cliente', 'ajuste_positivo', 'ajuste_negativo', 'venta_directa'
    private BigDecimal cantidad;
    private BigDecimal costoUnitario;
    private Timestamp fechaMovimiento;
    private String notas;

    public MovimientosStockDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }

    public Integer getEmpresaId() { return empresaId; }
    public void setEmpresaId(Integer empresaId) { this.empresaId = empresaId; }

    public String getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(String empleadoId) { this.empleadoId = empleadoId; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    public BigDecimal getCostoUnitario() { return costoUnitario; }
    public void setCostoUnitario(BigDecimal costoUnitario) { this.costoUnitario = costoUnitario; }

    public Timestamp getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(Timestamp fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
