package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.DTO.InventarioStockDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InventarioStockDAO {
    private static final String SQL_UPDATE_CANTIDAD = "UPDATE dommapi.inventario_stock SET cantidad_en_bodega = cantidad_en_bodega - ? WHERE producto_id = ? AND empresa_id = ?";
    private static final String SQL_READ_ONE = "SELECT id, producto_id, empresa_id, cantidad_en_bodega, costo_promedio, ubicacion_bodega, updated_at FROM dommapi.inventario_stock WHERE producto_id = ? AND empresa_id = ?";

    private final conexionBD con = conexionBD.saberEstado();
    static final Logger log = Logger.getLogger(InventarioStockDAO.class.getName());

    public boolean decrementarCantidad(Integer productoId, Integer empresaId, java.math.BigDecimal cantidad) {
        log.info("*** Start InventarioStockDAO decrementarCantidad ***");
        PreparedStatement ps = null;
        boolean ok = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE_CANTIDAD);
            ps.setBigDecimal(1, cantidad);
            ps.setInt(2, productoId);
            ps.setInt(3, empresaId);
            int result = ps.executeUpdate();
            ok = (result > 0);
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error updating stock cantidad: {0}", ex);
            ok = false;
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End InventarioStockDAO decrementarCantidad ***");
        return ok;
    }

    public InventarioStockDTO readOne(Integer productoId, Integer empresaId) {
        log.info("*** Start InventarioStockDAO readOne ***");
        PreparedStatement ps = null;
        ResultSet rs = null;
        InventarioStockDTO dto = null;
        try {
            ps = con.getCnn().prepareStatement(SQL_READ_ONE);
            ps.setInt(1, productoId);
            ps.setInt(2, empresaId);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = new InventarioStockDTO();
                dto.setId(rs.getInt("id"));
                dto.setProductoId(rs.getInt("producto_id"));
                dto.setEmpresaId(rs.getInt("empresa_id"));
                dto.setCantidadEnBodega(rs.getBigDecimal("cantidad_en_bodega"));
                dto.setCostoPromedio(rs.getBigDecimal("costo_promedio"));
                dto.setUbicacionBodega(rs.getString("ubicacion_bodega"));
                dto.setUpdatedAt(rs.getTimestamp("updated_at"));
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error reading inventario_stock: {0}", ex);
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End InventarioStockDAO readOne ***");
        return dto;
    }
}
