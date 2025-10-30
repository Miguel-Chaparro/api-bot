package com.dom.ws.rest.bot.DTO;

public class InventoryRequestDTO {
    private Integer inventarioId;
    private String notas;

    public InventoryRequestDTO() {}

    public Integer getInventarioId() { return inventarioId; }
    public void setInventarioId(Integer inventarioId) { this.inventarioId = inventarioId; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
