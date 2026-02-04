package Models;

import java.sql.Timestamp;

public class OtpCode {

    private long otpId;
    private long tenantId;
    private String otpHash;
    private String purpose;
    private Timestamp expiresAt;
    private Timestamp usedAt;

    public OtpCode() {
    }

    public OtpCode(long otpId, long tenantId, String otpHash,
                   String purpose, Timestamp expiresAt, Timestamp usedAt) {
        this.otpId = otpId;
        this.tenantId = tenantId;
        this.otpHash = otpHash;
        this.purpose = purpose;
        this.expiresAt = expiresAt;
        this.usedAt = usedAt;
    }

    public long getOtpId() {
        return otpId;
    }

    public void setOtpId(long otpId) {
        this.otpId = otpId;
    }

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public void setOtpHash(String otpHash) {
        this.otpHash = otpHash;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Timestamp getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(Timestamp usedAt) {
        this.usedAt = usedAt;
    }
}
