package com.dom.ws.rest.bot.DAO;

import com.dom.ws.rest.bot.DTO.RaspberryNewDTO;
import java.sql.*;
import java.util.*;

public class RaspberryNewDAO {
    private final Connection conn;
    public RaspberryNewDAO(Connection conn) {
        this.conn = conn;
    }

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
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("raspberry_id");
        }
        return null;
    }

    private Integer getRaspberryIdByUserNumber(String number) throws SQLException {
        String sql = "SELECT raspberry_id FROM dommapi.raspberry_user WHERE user_number = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, raspberryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBoolean("mikrotik");
        }
        return false;
    }

    private String getNameDevice(int raspberryId, boolean admin) throws SQLException {
        String sql = admin ? "SELECT name_admin FROM dommapi.raspberry WHERE id = ?" : "SELECT name_tec FROM dommapi.raspberry WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, raspberryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return admin ? rs.getString("name_admin") : rs.getString("name_tec");
        }
        return null;
    }

    public boolean createOrUpdateRaspberryWithUsers(RaspberryNewDTO dto) throws SQLException {
        conn.setAutoCommit(false);
        try {
            // 1. Insertar o actualizar raspberry principal (por nameDeviceAdmin como clave única)
            Integer raspberryId = null;
            String selectRaspberry = "SELECT id FROM dommapi.raspberry WHERE name_admin = ?";
            try (PreparedStatement ps = conn.prepareStatement(selectRaspberry)) {
                ps.setString(1, dto.getNameDeviceAdmin());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    raspberryId = rs.getInt("id");
                }
            }
            if (raspberryId == null) {
                String insertRaspberry = "INSERT INTO dommapi.raspberry (name_admin, name_tec, topic, mikrotik) VALUES (?, ?, ?, ?) RETURNING id";
                try (PreparedStatement ps = conn.prepareStatement(insertRaspberry)) {
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
                try (PreparedStatement ps = conn.prepareStatement(updateRaspberry)) {
                    ps.setString(1, dto.getNameDeviceTec());
                    ps.setBoolean(2, dto.isMikrotik());
                    ps.setInt(3, raspberryId);
                    ps.executeUpdate();
                }
            }
            if (raspberryId == null) throw new SQLException("No se pudo obtener el id de la raspberry");

            // 2. Limpiar y volver a insertar usuarios asociados
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM dommapi.raspberry_user WHERE raspberry_id = ?")) {
                ps.setInt(1, raspberryId);
                ps.executeUpdate();
            }
            if (dto.getAdmin() != null) {
                for (RaspberryNewDTO.RaspiUserDTO user : dto.getAdmin()) {
                    String insertUser = "INSERT INTO dommapi.raspberry_user (raspberry_id, user_number, role, name, topic, device) VALUES (?, ?, 'admin', ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertUser)) {
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
                    try (PreparedStatement ps = conn.prepareStatement(insertUser)) {
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
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM dommapi.raspberry_ip WHERE raspberry_id = ?")) {
                ps.setInt(1, raspberryId);
                ps.executeUpdate();
            }
            if (dto.getIps() != null) {
                for (RaspberryNewDTO.RaspiIpDTO ip : dto.getIps()) {
                    String insertIp = "INSERT INTO dommapi.raspberry_ip (raspberry_id, ip, nodo, grupo) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertIp)) {
                        ps.setInt(1, raspberryId);
                        ps.setString(2, ip.getIp());
                        ps.setString(3, ip.getNodo());
                        ps.setString(4, ip.getGroup());
                        ps.executeUpdate();
                    }
                }
            }

            // 4. Limpiar y volver a insertar dispositivos
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM dommapi.raspberry_device WHERE raspberry_id = ?")) {
                ps.setInt(1, raspberryId);
                ps.executeUpdate();
            }
            if (dto.getDevices() != null) {
                for (RaspberryNewDTO.RaspiDeviceDTO dev : dto.getDevices()) {
                    String insertDev = "INSERT INTO dommapi.raspberry_device (raspberry_id, device, ip, port, username, password, service, type_mikrotik) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertDev)) {
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

            conn.commit();
            return true;
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // Method to fetch all Raspberry devices
    public List<RaspberryNewDTO> getAllRaspberrys() throws SQLException {
        List<RaspberryNewDTO> raspberryList = new ArrayList<>();
        String sql = "SELECT id FROM dommapi.raspberry";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int raspberryId = rs.getInt("id");
                RaspberryNewDTO dto = buildRaspberryResponse(raspberryId);
                raspberryList.add(dto);
            }
        }
        return raspberryList;
    }
}
