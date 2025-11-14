package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.DTO.ContratoServicioDetalleDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContratoServicioDetalleDAO {
    private static final String SQL_INSERT = "INSERT INTO dommapi.contratos_servicio_detalle (contrato_servicio_id, inventario_id, fecha_asignacion, precio_asignacion) VALUES (?, ?, ?, ?)";

    private final conexionBD con = conexionBD.saberEstado();
    static final Logger log = Logger.getLogger(ContratoServicioDetalleDAO.class.getName());

    public boolean create(ContratoServicioDetalleDTO dto) {
        log.info("*** Start ContratoServicioDetalleDAO create ***");
        PreparedStatement ps = null;
        boolean ok = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getContratoServicioId());
            ps.setInt(2, dto.getInventarioId());
            ps.setTimestamp(3, dto.getFechaAsignacion() != null ? dto.getFechaAsignacion() : new java.sql.Timestamp(System.currentTimeMillis()));
            if (dto.getPrecioAsignacion() != null) {
                ps.setBigDecimal(4, dto.getPrecioAsignacion());
            } else {
                ps.setBigDecimal(4, java.math.BigDecimal.ZERO);
            }

            int result = ps.executeUpdate();
            if (result > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    dto.setId(keys.getInt(1));
                }
                ok = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error creating contrato servicio detalle: {0}", ex);
            ok = false;
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End ContratoServicioDetalleDAO create ***");
        return ok;
    }
}
