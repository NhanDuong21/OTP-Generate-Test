package DALs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Models.OtpCode;
import Utils.DBContext;

public class OtpDAL extends DBContext {

    public void createOtp(long tenantId, String otpHash) throws SQLException {

        String sql
                = "INSERT INTO OTP_CODE(tenant_id, otp_hash, purpose, expires_at) "
                + "VALUES (?, ?, 'REGISTER', DATEADD(MINUTE, 5, SYSDATETIME()))";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setLong(1, tenantId);
        ps.setString(2, otpHash);

        ps.executeUpdate();
    }

    public OtpCode verifyOtp(long tenantId, String otpHash) throws SQLException {

        String sql
                = "SELECT TOP 1 * FROM OTP_CODE "
                + "WHERE tenant_id=? AND purpose='REGISTER' "
                + "AND used_at IS NULL "
                + "AND expires_at > SYSDATETIME() "
                + "AND otp_hash=?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setLong(1, tenantId);
        ps.setString(2, otpHash);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            OtpCode code = new OtpCode();
            code.setOtpId(rs.getLong("otp_id"));
            code.setTenantId(rs.getLong("tenant_id"));
            code.setOtpHash(rs.getString("otp_hash"));
            code.setPurpose(rs.getString("purpose"));
            code.setExpiresAt(rs.getTimestamp("expires_at"));
            code.setUsedAt(rs.getTimestamp("used_at"));
            return code;
        }

        return null;
    }

    public void markUsed(long otpId) throws SQLException {

        String sql = "UPDATE OTP_CODE SET used_at = SYSDATETIME() WHERE otp_id=?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setLong(1, otpId);

        ps.executeUpdate();
    }
}
