package com.dom.ws.rest.bot.DTO;

import com.dom.ws.rest.bot.vo.msgError;
import java.sql.Timestamp;

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

    public UserDTO() {
    }

    public UserDTO(String id, String email, String displayName, String photoUrl, 
                  String phoneNumber, String providerId, Timestamp creationTime, 
                  Timestamp lastSignInTime, boolean emailVerified, 
                  String customClaims, boolean disabled, String lastLoginIp) {
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
}