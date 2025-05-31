package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import com.dom.ws.rest.bot.DTO.RaspberryDTO;
import com.dom.ws.rest.bot.DTO.RaspberryNewDTO;
import com.dom.ws.rest.bot.DTO.RaspberryUserRelationDTO;
import java.sql.*;
import java.util.*;

public class RaspberryNewDAO {
    private final conexionBD conn = conexionBD.saberEstado();
    

    public RaspberryNewDTO getRaspberryByUserId(String userId) throws SQLException {
        Integer raspberryId = getRaspberryIdByUserId(userId);
        if (raspberryId == null) return null;
        return buildRaspberryResponse(raspberryId);
    }

    public RaspberryNewDTO getRaspberryByUserNumber(String number) throws SQLException {
        Integer raspberryId = getRaspberryIdByUserNumber(number);
        if (raspberryId == null) return null;
        return buildRaspberryResponse(raspberryId);
    }

    private Integer getRaspberryIdByUserId(String userId) throws SQLException {
        String sql = "SELECT raspberry_id FROM dommapi.raspberry_user WHERE user_id = ? LIMIT 1";
        try (PreparedStatement ps = conn.getCnn().prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("raspberry_id");
        }
        return null;
    }

    private Integer getRaspberryIdByUserNumber(String number) throws SQLException {
        String sql = "SELECT raspberry_id FROM dommapi.raspberry_user WHERE user_number = ? LIMIT 1";
        try (PreparedStatement ps = conn.getCnn().prepareStatement(sql)) {
            ps.setString(1, number);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("raspberry_id");
        }
        return null;
    }

    private RaspberryNewDTO buildRaspberryResponse(int raspberryId) throws SQLException {
        RaspberryNewDTO dto = new RaspberryNewDTO();
        dto.setAdmin(getUsersByRole(raspberryId, "admin"));
        dto.setOperator(getUsersByRole(raspberryId, "operator"));
        dto.setIps(getIps(raspberryId));
        dto.setDevices(getDevices(raspberryId));
        dto.setMikrotik(isMikrotik(raspberryId));
        dto.setNameDeviceAdmin(getNameDevice(raspberryId, true));
        dto.setNameDeviceTec(getNameDevice(raspberryId, false));
        return dto;
    }

    private List<RaspberryNewDTO.RaspiUserDTO> getUsersByRole(int raspberryId, String role) throws SQLException {
        String sql = "SELECT user_number, topic, name, device FROM dommapi.raspberry_user WHERE raspberry_id = ? AND role = ?";
        List<RaspberryNewDTO.RaspiUserDTO> list = new ArrayList<>();
        try (PreparedStatement ps = conn.getCnn().prepareStatement(sql)) {
            ps.setInt(1, raspberryId);
            ps.setString(2, role);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RaspberryNewDTO.RaspiUserDTO u = new RaspberryNewDTO.RaspiUserDTO();
                u.setNumber(rs.getString("user_number"));
                u.setTopic(rs.getString("topic"));
                u.setName(rs.getString("name"));
                u.setDevice(rs.getString("device"));
                list.add(u);
            }
        }
        return list;
    }

    private List<RaspberryNewDTO.RaspiIpDTO> getIps(int raspberryId) throws SQLException {
        String sql = "SELECT id, ip, nodo, grupo FROM dommapi.raspberry_ip WHERE raspberry_id = ?";
        List<RaspberryNewDTO.RaspiIpDTO> list = new ArrayList<>();
        try (PreparedStatement ps = conn.getCnn().prepareStatement(sql)) {
            ps.setInt(1, raspberryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RaspberryNewDTO.RaspiIpDTO ip = new RaspberryNewDTO.RaspiIpDTO();
                ip.setId(rs.getInt("id"));
                ip.setIp(rs.getString("ip"));
                ip.setNodo(rs.getString("nodo"));
                ip.setGroup(rs.getString("grupo"));
                list.add(ip);
            }
        }
        return list;
    }

    private List<RaspberryNewDTO.RaspiDeviceDTO> getDevices(int raspberryId) throws SQLException {
        String sql = "SELECT device, ip, port, username, password, service, type_mikrotik FROM dommapi.raspberry_device WHERE raspberry_id = ?";
        List<RaspberryNewDTO.RaspiDeviceDTO> list = new ArrayList<>();
        try (PreparedStatement ps = conn.getCnn().prepareStatement(sql)) {
            ps.setInt(1, raspberryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RaspberryNewDTO.RaspiDeviceDTO d = new RaspberryNewDTO.RaspiDeviceDTO();
                d.setDevice(rs.getString("device"));
                d.setIp(rs.getString("ip"));
                d.setPort(rs.getString("port"));
                d.setUser(rs.getString("username"));
                d.setPassword(rs.getString("password"));
                d.setService(rs.getString("service"));
                d.setTypeMicrotik(rs.getBoolean("type_mikrotik"));
                list.add(d);
            }
        }
        return list;
    }

    private boolean isMikrotik(int raspberryId) throws SQLException {
        String sql = "SELECT mikrotik FROM dommapi.raspberry WHERE id = ?";
        try (PreparedStatement ps = conn.getCnn().prepareStatement(sql)) {
            ps.setInt(1, raspberryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBoolean("mikrotik");
        }
        return false;
    }

    private String getNameDevice(int raspberryId, boolean admin) throws SQLException {
        String sql = admin ? "SELECT name_admin FROM dommapi.raspberry WHERE id = ?" : "SELECT name_tec FROM dommapi.raspberry WHERE id = ?";
        try (PreparedStatement ps = conn.getCnn().prepareStatement(sql)) {
            ps.setInt(1, raspberryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return admin ? rs.getString("name_admin") : rs.getString("name_tec");
        }
        return null;
    }

    public boolean createOrUpdateRaspberryWithUsers(RaspberryNewDTO dto) throws SQLException {
        conn.getCnn().setAutoCommit(false);
        try {
            // 1. Insertar o actualizar raspberry principal (por nameDeviceAdmin como clave única)
            Integer raspberryId = null;
            String selectRaspberry = "SELECT id FROM dommapi.raspberry WHERE name_admin = ?";
            try (PreparedStatement ps = conn.getCnn().prepareStatement(selectRaspberry)) {
                ps.setString(1, dto.getNameDeviceAdmin());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    raspberryId = rs.getInt("id");
                }
            }
            if (raspberryId == null) {
                String insertRaspberry = "INSERT INTO dommapi.raspberry (name_admin, name_tec, topic, mikrotik) VALUES (?, ?, ?, ?) RETURNING id";
                try (PreparedStatement ps = conn.getCnn().prepareStatement(insertRaspberry)) {
                    ps.setString(1, dto.getNameDeviceAdmin());
                    ps.setString(2, dto.getNameDeviceTec());
                    String topic = (dto.getAdmin() != null && !dto.getAdmin().isEmpty()) ? dto.getAdmin().get(0).getTopic() : null;
                    ps.setString(3, topic);
                    ps.setBoolean(4, dto.isMikrotik());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        raspberryId = rs.getInt(1);
                    }
                }
            } else {
                String updateRaspberry = "UPDATE dommapi.raspberry SET name_tec = ?, mikrotik = ? WHERE id = ?";
                try (PreparedStatement ps = conn.getCnn().prepareStatement(updateRaspberry)) {
                    ps.setString(1, dto.getNameDeviceTec());
                    ps.setBoolean(2, dto.isMikrotik());
                    ps.setInt(3, raspberryId);
                    ps.executeUpdate();
                }
            }
            if (raspberryId == null) throw new SQLException("No se pudo obtener el id de la raspberry");

            // 2. Limpiar y volver a insertar usuarios asociados
            try (PreparedStatement ps = conn.getCnn().prepareStatement("DELETE FROM dommapi.raspberry_user WHERE raspberry_id = ?")) {
                ps.setInt(1, raspberryId);
                ps.executeUpdate();
            }
            if (dto.getAdmin() != null) {
                for (RaspberryNewDTO.RaspiUserDTO user : dto.getAdmin()) {
                    String insertUser = "INSERT INTO dommapi.raspberry_user (raspberry_id, user_number, role, name, topic, device) VALUES (?, ?, 'admin', ?, ?, ?)";
                    try (PreparedStatement ps = conn.getCnn().prepareStatement(insertUser)) {
                        ps.setInt(1, raspberryId);
                        ps.setString(2, user.getNumber());
                        ps.setString(3, user.getName());
                        ps.setString(4, user.getTopic());
                        ps.setString(5, user.getDevice());
                        ps.executeUpdate();
                    }
                }
            }
            if (dto.getOperator() != null) {
                for (RaspberryNewDTO.RaspiUserDTO user : dto.getOperator()) {
                    String insertUser = "INSERT INTO dommapi.raspberry_user (raspberry_id, user_number, role, name, topic, device) VALUES (?, ?, 'operator', ?, ?, ?)";
                    try (PreparedStatement ps = conn.getCnn().prepareStatement(insertUser)) {
                        ps.setInt(1, raspberryId);
                        ps.setString(2, user.getNumber());
                        ps.setString(3, user.getName());
                        ps.setString(4, user.getTopic());
                        ps.setString(5, user.getDevice());
                        ps.executeUpdate();
                    }
                }
            }

            // 3. Limpiar y volver a insertar IPs
            try (PreparedStatement ps = conn.getCnn().prepareStatement("DELETE FROM dommapi.raspberry_ip WHERE raspberry_id = ?")) {
                ps.setInt(1, raspberryId);
                ps.executeUpdate();
            }
            if (dto.getIps() != null) {
                for (RaspberryNewDTO.RaspiIpDTO ip : dto.getIps()) {
                    String insertIp = "INSERT INTO dommapi.raspberry_ip (raspberry_id, ip, nodo, grupo) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.getCnn().prepareStatement(insertIp)) {
                        ps.setInt(1, raspberryId);
                        ps.setString(2, ip.getIp());
                        ps.setString(3, ip.getNodo());
                        ps.setString(4, ip.getGroup());
                        ps.executeUpdate();
                    }
                }
            }

            // 4. Limpiar y volver a insertar dispositivos
            try (PreparedStatement ps = conn.getCnn().prepareStatement("DELETE FROM dommapi.raspberry_device WHERE raspberry_id = ?")) {
                ps.setInt(1, raspberryId);
                ps.executeUpdate();
            }
            if (dto.getDevices() != null) {
                for (RaspberryNewDTO.RaspiDeviceDTO dev : dto.getDevices()) {
                    String insertDev = "INSERT INTO dommapi.raspberry_device (raspberry_id, device, ip, port, username, password, service, type_mikrotik) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.getCnn().prepareStatement(insertDev)) {
                        ps.setInt(1, raspberryId);
                        ps.setString(2, dev.getDevice());
                        ps.setString(3, dev.getIp());
                        ps.setString(4, dev.getPort());
                        ps.setString(5, dev.getUser());
                        ps.setString(6, dev.getPassword());
                        ps.setString(7, dev.getService());
                        ps.setBoolean(8, dev.isTypeMicrotik());
                        ps.executeUpdate();
                    }
                }
            }

            conn.getCnn().commit();
            return true;
        } catch (Exception e) {
            conn.getCnn().rollback();
            throw e;
        } finally {
            conn.getCnn().setAutoCommit(true);
        }
    }

    // Method to fetch all Raspberry devices
    public List<RaspberryNewDTO> getAllRaspberrys() throws SQLException {
        List<RaspberryNewDTO> raspberryList = new ArrayList<>();
        String sql = "SELECT id FROM dommapi.raspberry";
        try (PreparedStatement ps = conn.getCnn().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int raspberryId = rs.getInt("id");
                RaspberryNewDTO dto = buildRaspberryResponse(raspberryId);
                raspberryList.add(dto);
            }
        }
        return raspberryList;
    }

    // Fixed unused variable issue by accessing fields of admin and operator
    public boolean createUserRaspberryRelation(String userId, RaspberryNewDTO dto) throws SQLException {
        String sql = "INSERT INTO dommapi.raspberry_user (user_id, raspberry_id, role, name, device) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.getCnn().prepareStatement(sql)) {
            for (RaspberryNewDTO.RaspiUserDTO admin : dto.getAdmin()) {
                ps.setString(1, userId);
                ps.setInt(2, dto.getId());
                ps.setString(3, "admin");
                ps.setString(4, admin.getName());
                ps.setString(5, admin.getDevice());
                ps.addBatch();
            }
            for (RaspberryNewDTO.RaspiUserDTO operator : dto.getOperator()) {
                ps.setString(1, userId);
                ps.setInt(2, dto.getId());
                ps.setString(3, "operator");
                ps.setString(4, operator.getName());
                ps.setString(5, operator.getDevice());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        }
    }

    // Método para crear una Raspberry solo con los campos de la tabla dommapi.raspberry
    public boolean createRaspberry(RaspberryDTO dto) throws SQLException {
        String insertRaspberry = "INSERT INTO dommapi.raspberry (name_admin, name_tec, topic, mikrotik) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.getCnn().prepareStatement(insertRaspberry)) {
            ps.setString(1, dto.getNameDeviceAdmin());
            ps.setString(2, dto.getNameDeviceTec());
            ps.setString(3, dto.getTopic());
            ps.setBoolean(4, dto.isMikrotik());
            return ps.executeUpdate() > 0;
        }
    }

    // Método para obtener todas las Raspberrys solo con los campos de la tabla dommapi.raspberry
    public List<RaspberryDTO> getAllRaspberrysSimple() throws SQLException {
        List<RaspberryDTO> raspberryList = new ArrayList<>();
        String sql = "SELECT id, name_admin, name_tec, topic, mikrotik FROM dommapi.raspberry";
        try (PreparedStatement ps = conn.getCnn().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RaspberryDTO dto = new RaspberryDTO();
                dto.setId(rs.getInt("id"));
                dto.setNameDeviceAdmin(rs.getString("name_admin"));
                dto.setNameDeviceTec(rs.getString("name_tec"));
                dto.setTopic(rs.getString("topic"));
                dto.setMikrotik(rs.getBoolean("mikrotik"));
                raspberryList.add(dto);
            }
        }
        return raspberryList;
    }

    // Método para crear la relación usuario-raspberry solo con los campos de la tabla dommapi.raspberry_user
    public boolean createRaspberryUserRelation(RaspberryUserRelationDTO dto) throws SQLException {
        String sql = "INSERT INTO dommapi.raspberry_user (raspberry_id, user_number, user_id, role, name, topic, device) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.getCnn().prepareStatement(sql)) {
            ps.setInt(1, dto.getRaspberryId());
            ps.setString(2, dto.getUserNumber());
            ps.setString(3, dto.getUserId());
            ps.setString(4, dto.getRole());
            ps.setString(5, dto.getName());
            ps.setString(6, dto.getTopic());
            ps.setString(7, dto.getDevice());
            return ps.executeUpdate() > 0;
        }
    }

    // Método para actualizar una Raspberry solo con los campos de la tabla dommapi.raspberry
    public boolean updateRaspberry(RaspberryDTO dto) throws SQLException {
        String updateRaspberry = "UPDATE dommapi.raspberry SET name_admin = ?, name_tec = ?, topic = ?, mikrotik = ? WHERE id = ?";
        try (PreparedStatement ps = conn.getCnn().prepareStatement(updateRaspberry)) {
            ps.setString(1, dto.getNameDeviceAdmin());
            ps.setString(2, dto.getNameDeviceTec());
            ps.setString(3, dto.getTopic());
            ps.setBoolean(4, dto.isMikrotik());
            ps.setInt(5, dto.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // Método para obtener las relaciones usuario-raspberry filtradas por raspberry_id
    public List<RaspberryUserRelationDTO> getRaspberryUserRelationsByRaspberryId(int raspberryId) throws SQLException {
        List<RaspberryUserRelationDTO> relations = new ArrayList<>();
        String sql = "SELECT id, raspberry_id, user_number, user_id, role, name, topic, device FROM dommapi.raspberry_user WHERE raspberry_id = ?";
        try (PreparedStatement ps = conn.getCnn().prepareStatement(sql)) {
            ps.setInt(1, raspberryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RaspberryUserRelationDTO dto = new RaspberryUserRelationDTO();
                dto.setId(rs.getInt("id"));
                dto.setRaspberryId(rs.getInt("raspberry_id"));
                dto.setUserNumber(rs.getString("user_number"));
                dto.setUserId(rs.getString("user_id"));
                dto.setRole(rs.getString("role"));
                dto.setName(rs.getString("name"));
                dto.setTopic(rs.getString("topic"));
                dto.setDevice(rs.getString("device"));
                relations.add(dto);
            }
        }
        return relations;
    }

    // Método para obtener todas las Raspberrys relacionadas a un usuario (por user_id en dommapi.raspberry_user)
    public List<RaspberryDTO> getRaspberrysSimpleByUserId(String userId) throws SQLException {
        List<RaspberryDTO> raspberryList = new ArrayList<>();
        String sql = "SELECT DISTINCT r.id, r.name_admin, r.name_tec, r.topic, r.mikrotik FROM dommapi.raspberry r INNER JOIN dommapi.raspberry_user ru ON r.id = ru.raspberry_id WHERE ru.user_id = ?";
        try (PreparedStatement ps = conn.getCnn().prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RaspberryDTO dto = new RaspberryDTO();
                dto.setId(rs.getInt("id"));
                dto.setNameDeviceAdmin(rs.getString("name_admin"));
                dto.setNameDeviceTec(rs.getString("name_tec"));
                dto.setTopic(rs.getString("topic"));
                dto.setMikrotik(rs.getBoolean("mikrotik"));
                raspberryList.add(dto);
            }
        }
        return raspberryList;
    }
}
