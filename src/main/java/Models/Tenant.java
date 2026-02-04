package Models;

import java.sql.Timestamp;

public class Tenant {

    private long tenantId;
    private String email;
    private String passwordHash;
    private boolean mustSetPassword;
    private String accountStatus;
    private Timestamp createdAt;

    public Tenant() {
    }

    public Tenant(long tenantId, String email, String passwordHash,
                  boolean mustSetPassword, String accountStatus,
                  Timestamp createdAt) {
        this.tenantId = tenantId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.mustSetPassword = mustSetPassword;
        this.accountStatus = accountStatus;
        this.createdAt = createdAt;
    }

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isMustSetPassword() {
        return mustSetPassword;
    }

    public void setMustSetPassword(boolean mustSetPassword) {
        this.mustSetPassword = mustSetPassword;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
