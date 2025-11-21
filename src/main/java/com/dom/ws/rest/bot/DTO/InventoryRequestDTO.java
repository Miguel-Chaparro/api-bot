package com.dom.ws.rest.bot.DTO;

import java.math.BigDecimal;
import com.google.gson.annotations.SerializedName;

public class InventoryRequestDTO {
    @SerializedName("inventarioId")
    private Integer inventarioId;
    
    @SerializedName("notas")
    private String notas;
    
    @SerializedName("precioAsignacion")
    private BigDecimal precioAsignacion;

    public InventoryRequestDTO() {}

    public Integer getInventarioId() { return inventarioId; }
    public void setInventarioId(Integer inventarioId) { this.inventarioId = inventarioId; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public BigDecimal getPrecioAsignacion() { return precioAsignacion; }
    public void setPrecioAsignacion(BigDecimal precioAsignacion) { this.precioAsignacion = precioAsignacion; }
    
    // Método auxiliar para parsear strings numéricos de forma segura
    public void setPrecioAsignacionStr(String valor) {
        if (valor != null && !valor.trim().isEmpty()) {
            try {
                this.precioAsignacion = new BigDecimal(valor);
            } catch (NumberFormatException e) {
                this.precioAsignacion = null;
            }
        } else {
            this.precioAsignacion = null;
        }
    }
}
