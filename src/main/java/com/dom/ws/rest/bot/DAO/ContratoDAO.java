package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.DTO.ContratoDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContratoDAO {
    private static final String SQL_INSERT = "INSERT INTO dommapi.contratos_servicio (usuario_id, plan_internet_id, numero_contrato, fecha_inicio, fecha_fin, direccion_instalacion, estado, precio_mensual, dia_corte, observaciones, empresa_id, tipo_servicio, internet_ppoe_usuario, internet_ppoe_password, energia_tipo_panel, evento_tipo, device, tipo_id, num_id, contrato_nombre, phone_number, created_by, tipo_internet, puerto, caja, nodo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_MAX_SEQ = "SELECT COALESCE(MAX(id),0) as maxid FROM dommapi.contratos_servicio WHERE empresa_id = ?";

       /**
     * Helper method to get fresh connection for each operation
     */
    private conexionBD getConnection() {
        return conexionBD.saberEstado();
    }

    conexionBD con = getConnection();
    static final Logger log = Logger.getLogger(ContratoDAO.class.getName());

    public boolean create(ContratoDTO dto) {
        log.info("*** Start ContratoDAO create ***");
        PreparedStatement ps = null;
        boolean ok = false;
        try {
            ps = con.getCnn().prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getUsuarioId());
            if (dto.getPlanInternetId() != null) ps.setObject(2, dto.getPlanInternetId()); else ps.setNull(2, java.sql.Types.INTEGER);
            ps.setString(3, dto.getNumeroContrato());
            ps.setDate(4, dto.getFechaInicio());
            if (dto.getFechaFin() != null) ps.setDate(5, dto.getFechaFin()); else ps.setNull(5, java.sql.Types.DATE);
            ps.setString(6, dto.getDireccionInstalacion());
            ps.setString(7, dto.getEstado());
            if (dto.getPrecioMensual() != null) ps.setBigDecimal(8, dto.getPrecioMensual()); else ps.setNull(8, java.sql.Types.DECIMAL);
            if (dto.getDiaCorte() != null) ps.setObject(9, dto.getDiaCorte()); else ps.setNull(9, java.sql.Types.INTEGER);
            ps.setString(10, dto.getObservaciones());
            if (dto.getEmpresaId() != null) ps.setObject(11, dto.getEmpresaId()); else ps.setNull(11, java.sql.Types.INTEGER);
            
            // Nuevos campos
            ps.setString(12, dto.getTipoServicio() != null ? dto.getTipoServicio() : "internet");
            ps.setString(13, dto.getInternetPpoEUsuario());
            ps.setString(14, dto.getInternetPpoEPassword());
            ps.setString(15, dto.getEnergiaTipoPanel());
            ps.setString(16, dto.getEventoTipo());
            ps.setString(17, dto.getDevice());
            
            // Campos de identificación del contratante
            ps.setString(18, dto.getTipoId() != null ? dto.getTipoId() : "Cedula de Ciudadania");
            ps.setString(19, dto.getNumId());
            ps.setString(20, dto.getContratoNombre());
            ps.setString(21, dto.getPhoneNumber());
            ps.setString(22, dto.getCreatedBy());
            
            // Nuevos campos de internet
            ps.setString(23, dto.getTipoInternet() != null ? dto.getTipoInternet() : "sin_servicio");
            ps.setString(24, dto.getPuerto());
            ps.setString(25, dto.getCaja());
            ps.setString(26, dto.getNodo());

            int result = ps.executeUpdate();
            if (result > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    dto.setId(keys.getInt(1));
                }
                ok = true;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error creating contrato: {0}", ex);
            ok = false;
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End ContratoDAO create ***");
        return ok;
    }

    public int getNextSeqForEmpresa(Integer empresaId) {
        log.info("*** Start ContratoDAO getNextSeqForEmpresa ***");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int next = 1;
        try {
            ps = con.getCnn().prepareStatement(SQL_MAX_SEQ);
            ps.setObject(1, empresaId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int max = rs.getInt("maxid");
                next = max + 1;
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error getting next seq for empresa: {0}", ex);
        } finally {
            con.cerrarConexion();
        }
        log.info("*** End ContratoDAO getNextSeqForEmpresa ***");
        return next;
    }
}
