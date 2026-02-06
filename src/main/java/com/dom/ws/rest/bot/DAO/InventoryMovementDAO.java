package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.DTO.InventoryMovementDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InventoryMovementDAO {
    // New table dommapi.movimientos_inventario
    private static final String SQL_INSERT = "INSERT INTO dommapi.movimientos_inventario (empresa_id, inventario_id, empleado_id, cliente_id, tipo_movimiento, fecha_movimiento, notas, movimiento_relacionado_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

       /**
     * Helper method to get fresh connection for each operation
     */

    static final Logger log = Logger.getLogger(InventoryMovementDAO.class.getName());
     private conexionBD getConnection() {
        return conexionBD.saberEstado();
    }
    public boolean create(InventoryMovementDTO dto) {
        log.info("*** Start InventoryMovementDAO create ***");
        conexionBD con = getConnection();
        PreparedStatement ps = null;
        boolean ok = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            if (dto.getEmpresaId() != null) ps.setObject(1, dto.getEmpresaId()); else ps.setNull(1, java.sql.Types.INTEGER);
            if (dto.getInventarioId() != null) ps.setObject(2, dto.getInventarioId()); else ps.setNull(2, java.sql.Types.INTEGER);
            ps.setString(3, dto.getEmpleadoId());
            if (dto.getClienteId() != null) ps.setString(4, dto.getClienteId()); else ps.setNull(4, java.sql.Types.VARCHAR);
            ps.setString(5, dto.getTipoMovimiento());
            if (dto.getFechaMovimiento() != null) ps.setTimestamp(6, dto.getFechaMovimiento()); else ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.setString(7, dto.getNotas());
            if (dto.getMovimientoRelacionadoId() != null) ps.setObject(8, dto.getMovimientoRelacionadoId()); else ps.setNull(8, java.sql.Types.INTEGER);

            int result = ps.executeUpdate();
            if (result > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) dto.setId(keys.getInt(1));
                ok = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error creating inventory movement: {0}", ex);
            ok = false;
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End InventoryMovementDAO create ***");
        return ok;
    }
}
