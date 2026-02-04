package Controllers;

import java.io.IOException;
import java.sql.Connection;

import DALs.OtpDAL;
import DALs.TenantDAL;
import Models.Tenant;
import Utils.DBContext;
import Utils.EmailUtil;
import Utils.HashUtil;
import Utils.OtpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegisterTenantServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String email = req.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"Email is required\"}");
            return;
        }

        Connection conn = new DBContext().connection;

        final int TTL_MINUTES = 5;

        try {
            conn.setAutoCommit(false);

            TenantDAL tenantDAL = new TenantDAL();
            tenantDAL.connection = conn;

            OtpDAL otpDAL = new OtpDAL();
            otpDAL.connection = conn;

            // 1) create tenant
            Tenant tenant = tenantDAL.registerTenant(email.trim());
            if (tenant == null) {
                conn.rollback();
                resp.setStatus(500);
                resp.getWriter().write("{\"error\":\"Cannot create tenant\"}");
                return;
            }

            // 2) generate otp + hash
            String otp = OtpUtil.generate6Digits();
            String otpHash = HashUtil.md5(otp);

            // 3) save otp
            otpDAL.createOtp(tenant.getTenantId(), otpHash);

            // 4) send email (gửi trước commit hay sau commit?)
            // => gửi TRƯỚC commit có rủi ro email đi nhưng DB rollback
            // => gửi SAU commit có rủi ro DB ok nhưng email fail
            // Thực tế dùng queue/retry. Ở demo: commit trước, rồi gửi.
            conn.commit();

            EmailUtil.sendOtpEmail(email.trim(), otp, TTL_MINUTES);

            resp.getWriter().write("{\"status\":\"OTP sent to email\"}");

        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ignore) {}

            // nếu email trùng (unique) thì trả 409 cho rõ
            String msg = (e.getMessage() == null) ? "" : e.getMessage().toLowerCase();
            if (msg.contains("duplicate") || msg.contains("unique")) {
                resp.setStatus(409);
                resp.getWriter().write("{\"error\":\"Email already exists\"}");
            } else {
                resp.setStatus(500);
                resp.getWriter().write("{\"error\":\"Server error\"}");
            }

        } finally {
            try { conn.close(); } catch (Exception ignore) {}
        }
    }
}
