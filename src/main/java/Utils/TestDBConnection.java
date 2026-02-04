package Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Author: Duong Thien Nhan - CE190741 Created on: 2026-01-12
 */
public class TestDBConnection {

    public static void main(String[] args) {
        try {
            DBContext db = new DBContext();
            Statement stmt = db.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1");

            if (rs.next()) {
                System.out.println("Ket noi db thanh cong");
            }
        } catch (SQLException e) {
            System.out.println("Im lang nao Ruby, connect fail roi");
            e.printStackTrace();
        }
    }
}
