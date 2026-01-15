package com.dom.ws.rest.bot.DTO;

import com.dom.ws.rest.bot.vo.msgError;
import java.sql.Timestamp;
import java.util.List;

public class UserDTO {
    private String id;              // UID de Firebase
    private String email;           
    private String displayName;     
    private String photoUrl;        
    private String phoneNumber;     
    private String providerId;      
    private Timestamp creationTime; 
    private Timestamp lastSignInTime;
    private boolean emailVerified;  
    private String customClaims;    
    private boolean disabled;       
    private msgError error;
    private String lastLoginIp; // Nueva propiedad
    private Integer empresaId; // Relación con empresa
    private String idRaspi;
    private String createdBy;
    private String planInternet;
    private String planPaneles;
    private Integer planInternetId; // nuevo: id del plan de internet
    private java.math.BigDecimal precioMensual; // nuevo: precio mensual del plan
    private String ppoe;
    private String ciudad;
    private String departamento;
    private String direccion;
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private String tipoPerfil; // Por
    // Lista de movimientos solicitados al crear el usuario: inventario id + notas
    private List<InventoryRequestDTO> inventoryRequests;
    
    // Campos para contrato de servicios
    private String tipoServicio; // 'internet', 'energia_solar', 'evento', 'otro'
    private String ppoEPassword; // Contraseña PPoE para internet (ppoe es el usuario)
    private String energiaTipoPanel; // Tipo de panel solar
    private String eventoTipo; // Tipo de evento
    
    // Nuevos campos para internet en contrato
    private String tipoInternet; // ENUM: 'sin_servicio', 'fibra', 'antena'
    private String puerto; // Puerto de fibra (requerido si tipoInternet es 'fibra')
    private String caja; // Caja de instalación de fibra (requerido si tipoInternet es 'fibra')
    private String nodo; // Antena (requerido si tipoInternet es 'antena')

    public UserDTO() {
    }

    public UserDTO(String id, String email, String displayName, String photoUrl, 
                  String phoneNumber, String providerId, Timestamp creationTime, 
                  Timestamp lastSignInTime, boolean emailVerified, 
                  String customClaims, boolean disabled, String lastLoginIp, Integer empresaId) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
        this.phoneNumber = phoneNumber;
        this.providerId = providerId;
        this.creationTime = creationTime;
        this.lastSignInTime = lastSignInTime;
        this.emailVerified = emailVerified;
        this.customClaims = customClaims;
        this.disabled = disabled;
        this.lastLoginIp = lastLoginIp;
        this.empresaId = empresaId;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public Timestamp getCreationTime() { return creationTime; }
    public void setCreationTime(Timestamp creationTime) { this.creationTime = creationTime; }

    public Timestamp getLastSignInTime() { return lastSignInTime; }
    public void setLastSignInTime(Timestamp lastSignInTime) { this.lastSignInTime = lastSignInTime; }

    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }

    public String getCustomClaims() { return customClaims; }
    public void setCustomClaims(String customClaims) { this.customClaims = customClaims; }

    public boolean isDisabled() { return disabled; }
    public void setDisabled(boolean disabled) { this.disabled = disabled; }

    public msgError getError() { return error; }
    public void setError(msgError error) { this.error = error; }

    public String getLastLoginIp() { return lastLoginIp; }
    public void setLastLoginIp(String lastLoginIp) { this.lastLoginIp = lastLoginIp; }

    public Integer getEmpresaId() { return empresaId; }
    public void setEmpresaId(Integer empresaId) { this.empresaId = empresaId; }

    public String getIdRaspi() { return idRaspi; }
    public void setIdRaspi(String idRaspi) { this.idRaspi = idRaspi; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getPlanInternet() { return planInternet; }
    public void setPlanInternet(String planInternet) { this.planInternet = planInternet; }

    public String getPlanPaneles() { return planPaneles; }
    public void setPlanPaneles(String planPaneles) { this.planPaneles = planPaneles; }

    public Integer getPlanInternetId() { return planInternetId; }
    public void setPlanInternetId(Integer planInternetId) { this.planInternetId = planInternetId; }

    public java.math.BigDecimal getPrecioMensual() { return precioMensual; }
    public void setPrecioMensual(java.math.BigDecimal precioMensual) { this.precioMensual = precioMensual; }

    public String getPpoe() { return ppoe; }
    public void setPpoe(String ppoe) { this.ppoe = ppoe; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }

    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    public String getTipoPerfil() { return tipoPerfil; }
    public void setTipoPerfil(String tipoPerfil) { this.tipoPerfil = tipoPerfil; }

    public List<InventoryRequestDTO> getInventoryRequests() { return inventoryRequests; }
    public void setInventoryRequests(List<InventoryRequestDTO> inventoryRequests) { this.inventoryRequests = inventoryRequests; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public String getPpoEPassword() { return ppoEPassword; }
    public void setPpoEPassword(String ppoEPassword) { this.ppoEPassword = ppoEPassword; }

    public String getEnergiaTipoPanel() { return energiaTipoPanel; }
    public void setEnergiaTipoPanel(String energiaTipoPanel) { this.energiaTipoPanel = energiaTipoPanel; }

    public String getEventoTipo() { return eventoTipo; }
    public void setEventoTipo(String eventoTipo) { this.eventoTipo = eventoTipo; }

    public String getTipoInternet() { return tipoInternet; }
    public void setTipoInternet(String tipoInternet) { this.tipoInternet = tipoInternet; }

    public String getPuerto() { return puerto; }
    public void setPuerto(String puerto) { this.puerto = puerto; }

    public String getCaja() { return caja; }
    public void setCaja(String caja) { this.caja = caja; }

    public String getNodo() { return nodo; }
    public void setNodo(String nodo) { this.nodo = nodo; }
}