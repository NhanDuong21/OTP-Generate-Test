package DALs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Models.Tenant;
import Utils.DBContext;

public class TenantDAL extends DBContext {

    public Tenant registerTenant(String email) throws SQLException {

        String sql = "INSERT INTO TENANT(email) VALUES (?); "
                + "SELECT SCOPE_IDENTITY() AS tenant_id;";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, email);

        boolean hasResult = ps.execute();

        while (!hasResult && ps.getUpdateCount() != -1) {
            hasResult = ps.getMoreResults();
        }

        ResultSet rs = ps.getResultSet();
        if (rs.next()) {
            long tenantId = rs.getLong("tenant_id");

            Tenant tenant = new Tenant();
            tenant.setTenantId(tenantId);
            tenant.setEmail(email);

            return tenant;
        }

        return null;
    }

    public Tenant findByEmail(String email) throws SQLException {

        String sql = "SELECT * FROM TENANT WHERE email = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, email);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Tenant t = new Tenant();
            t.setTenantId(rs.getLong("tenant_id"));
            t.setEmail(rs.getString("email"));
            t.setPasswordHash(rs.getString("password_hash"));
            t.setMustSetPassword(rs.getBoolean("must_set_password"));
            t.setAccountStatus(rs.getString("account_status"));
            t.setCreatedAt(rs.getTimestamp("created_at"));

            return t;
        }

        return null;
    }

    public void setPassword(long tenantId, String passwordHash) throws SQLException {

        String sql = "UPDATE TENANT "
                + "SET password_hash=?, must_set_password=0, account_status='ACTIVE' "
                + "WHERE tenant_id=?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, passwordHash);
        ps.setLong(2, tenantId);

        ps.executeUpdate();
    }
}
