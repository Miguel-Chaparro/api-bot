package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.DTO.InventarioDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InventarioDAO {
    private static final String SQL_UPDATE_ESTADO = "UPDATE dommapi.inventario SET estado = ? WHERE id = ?";
    private static final String SQL_GET_ONE = "SELECT id, producto_id, empresa_id, proveedor_id, numero_serie, mac_address, estado, ubicacion_bodega, fecha_compra, costo_adquisicion, notas, created_at, updated_at FROM dommapi.inventario WHERE id = ?";

       /**
     * Helper method to get fresh connection for each operation
     */
    private conexionBD getConnection() {
        return conexionBD.saberEstado();
    }

    conexionBD con = getConnection();
    static final Logger log = Logger.getLogger(InventarioDAO.class.getName());

    /**
     * Actualiza el estado de un item del inventario
     * 
     * @param inventarioId ID del item del inventario
     * @param nuevoEstado Nuevo estado ('en_bodega', 'prestado', 'en_reparacion', 'de_baja', 'vendido')
     * @return true si se actualizó exitosamente, false en caso contrario
     */
    public boolean updateEstado(Integer inventarioId, String nuevoEstado) {
        log.info("*** Start InventarioDAO updateEstado ***");
        PreparedStatement ps = null;
        boolean ok = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_UPDATE_ESTADO);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, inventarioId);
            
            int result = ps.executeUpdate();
            ok = result > 0;
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error updating inventario estado: {0}", ex);
            ok = false;
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End InventarioDAO updateEstado ***");
        return ok;
    }

    /**
     * Obtiene un item del inventario por su ID
     */
    public InventarioDTO readOne(Integer inventarioId) {
        log.info("*** Start InventarioDAO readOne ***");
        PreparedStatement ps = null;
        ResultSet rs = null;
        InventarioDTO dto = null;
        try {
            ps = con.getCnn().prepareStatement(SQL_GET_ONE);
            ps.setInt(1, inventarioId);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = new InventarioDTO();
                dto.setId(rs.getInt("id"));
                dto.setProductoId(rs.getInt("producto_id"));
                dto.setEmpresaId(rs.getInt("empresa_id"));
                dto.setProveedorId(rs.getObject("proveedor_id") != null ? rs.getInt("proveedor_id") : null);
                dto.setNumeroSerie(rs.getString("numero_serie"));
                dto.setMacAddress(rs.getString("mac_address"));
                dto.setEstado(rs.getString("estado"));
                dto.setUbicacionBodega(rs.getString("ubicacion_bodega"));
                dto.setFechaCompra(rs.getDate("fecha_compra"));
                dto.setCostoAdquisicion(rs.getBigDecimal("costo_adquisicion"));
                dto.setNotas(rs.getString("notas"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                dto.setUpdatedAt(rs.getTimestamp("updated_at"));
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error reading inventario: {0}", ex);
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End InventarioDAO readOne ***");
        return dto;
    }
}
