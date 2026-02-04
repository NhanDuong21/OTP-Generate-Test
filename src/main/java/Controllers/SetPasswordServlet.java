package Controllers;

import java.io.IOException;
import java.sql.Connection;

import DALs.OtpDAL;
import DALs.TenantDAL;
import Models.OtpCode;
import Models.Tenant;
import Utils.DBContext;
import Utils.HashUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SetPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        String email = req.getParameter("email");
        String otp = req.getParameter("otp");
        String password = req.getParameter("password");

        if (email == null || otp == null || password == null ||
            email.trim().isEmpty() || otp.trim().isEmpty() || password.trim().isEmpty()) {

            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"Email, OTP, Password are required\"}");
            return;
        }

        Connection conn = new DBContext().connection;

        try {
            // ✅ Transaction ON
            conn.setAutoCommit(false);

            TenantDAL tenantDAL = new TenantDAL();
            tenantDAL.connection = conn;

            OtpDAL otpDAL = new OtpDAL();
            otpDAL.connection = conn;

            // ✅ Step 1: Find tenant
            Tenant tenant = tenantDAL.findByEmail(email.trim());

            if (tenant == null) {
                conn.rollback();
                resp.setStatus(404);
                resp.getWriter().write("{\"error\":\"Tenant not found\"}");
                return;
            }

            // ✅ Step 2: Verify OTP
            String otpHash = HashUtil.md5(otp.trim());

            OtpCode otpCode = otpDAL.verifyOtp(tenant.getTenantId(), otpHash);

            if (otpCode == null) {
                conn.rollback();
                resp.setStatus(401);
                resp.getWriter().write("{\"error\":\"Invalid or expired OTP\"}");
                return;
            }

            // ✅ Step 3: Hash password + update tenant
            String passHash = HashUtil.md5(password.trim());

            tenantDAL.setPassword(tenant.getTenantId(), passHash);

            // ✅ Step 4: Mark OTP used
            otpDAL.markUsed(otpCode.getOtpId());

            // ✅ Commit
            conn.commit();

            resp.getWriter().write("{\"status\":\"Password set successfully\"}");

        } catch (Exception e) {

            try {
                conn.rollback();
            } catch (Exception ignore) {}

            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"Server error\"}");

        } finally {

            try {
                conn.close();
            } catch (Exception ignore) {}
        }
    }
}
