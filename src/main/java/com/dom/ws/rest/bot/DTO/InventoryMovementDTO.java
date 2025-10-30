package com.dom.ws.rest.bot.DTO;

import java.sql.Timestamp;

public class InventoryMovementDTO {
    private Integer id;
    private Integer empresaId;
    private Integer inventarioId;
    private String empleadoId; // quien realiza la accion (firebase uid)
    private String clienteId; // cliente nuevo (firebase uid)
    private String tipoMovimiento; // enum values as string
    private Timestamp fechaMovimiento;
    private String notas;
    private Integer movimientoRelacionadoId;

    public InventoryMovementDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getEmpresaId() { return empresaId; }
    public void setEmpresaId(Integer empresaId) { this.empresaId = empresaId; }

    public Integer getInventarioId() { return inventarioId; }
    public void setInventarioId(Integer inventarioId) { this.inventarioId = inventarioId; }

    public String getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(String empleadoId) { this.empleadoId = empleadoId; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public Timestamp getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(Timestamp fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public Integer getMovimientoRelacionadoId() { return movimientoRelacionadoId; }
    public void setMovimientoRelacionadoId(Integer movimientoRelacionadoId) { this.movimientoRelacionadoId = movimientoRelacionadoId; }
}
