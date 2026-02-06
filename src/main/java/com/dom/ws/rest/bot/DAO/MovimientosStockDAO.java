package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.DTO.MovimientosStockDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovimientosStockDAO {
    private static final String SQL_INSERT = "INSERT INTO dommapi.movimientos_stock (producto_id, empresa_id, empleado_id, cliente_id, tipo_movimiento, cantidad, costo_unitario, fecha_movimiento, notas) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

       /**
     * Helper method to get fresh connection for each operation
     */
    
    static final Logger log = Logger.getLogger(MovimientosStockDAO.class.getName());
     private conexionBD getConnection() {
        return conexionBD.saberEstado();
    }
    public boolean create(MovimientosStockDTO dto) {
        log.info("*** Start MovimientosStockDAO create ***");
        conexionBD con = getConnection();
        PreparedStatement ps = null;
        boolean ok = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getProductoId());
            ps.setInt(2, dto.getEmpresaId());
            ps.setString(3, dto.getEmpleadoId());
            ps.setString(4, dto.getClienteId());
            ps.setString(5, dto.getTipoMovimiento());
            ps.setBigDecimal(6, dto.getCantidad());
            if (dto.getCostoUnitario() != null) {
                ps.setBigDecimal(7, dto.getCostoUnitario());
            } else {
                ps.setNull(7, java.sql.Types.DECIMAL);
            }
            ps.setTimestamp(8, dto.getFechaMovimiento());
            ps.setString(9, dto.getNotas());

            int result = ps.executeUpdate();
            if (result > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    dto.setId(keys.getInt(1));
                }
                ok = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error creating movimiento_stock: {0}", ex);
            ok = false;
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End MovimientosStockDAO create ***");
        return ok;
    }
}
