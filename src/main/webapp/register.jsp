<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Register Tenant</title>
</head>
<body>
  <h2>Register tenant → generate temp OTP</h2>

  <form method="post">
    Email:
    <input type="email" name="email" required />
    <button type="submit">Register</button>
  </form>

  <p><a href="index.jsp">← Back</a></p>

<%
    String method = request.getMethod();
    if ("POST".equalsIgnoreCase(method)) {
        String email = request.getParameter("email");

        // URL servlet register (tùy context path)
        String base = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String urlStr = base + request.getContextPath() + "/register";

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            String payload = "email=" + URLEncoder.encode(email, "UTF-8");
            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes("UTF-8"));
            }

            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 400) ? conn.getInputStream() : conn.getErrorStream();

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }

%>
            <hr/>
            <h3>Result</h3>
            <p><b>POST</b> <%= urlStr %></p>
            <p><b>Status:</b> <%= code %></p>
            <pre style="padding:10px; border:1px solid #ccc;"><%= sb.toString() %></pre>

            <p>
              Nếu status 200, bạn lấy <b>tempOtp</b> ở đây để qua trang Set Password.
            </p>
<%
        } catch (Exception e) {
%>
            <hr/>
            <h3 style="color:red;">Error</h3>
            <pre><%= e.toString() %></pre>
<%
        }
    }
%>

</body>
</html>
