package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectDB {

    public static Connection getConnection() {
        try {
            
            String url = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;"
                       + "databaseName=QuanLyBanVeTau;"
                       + "encrypt=false;"
                       + "trustServerCertificate=true;";

            String user = "sa";       
            String password = "sa";   

            return DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối CSDL: " + e.getMessage());
            throw new RuntimeException("Không thể kết nối đến cơ sở dữ liệu QuanLyBanVeTau", e);
        }
    }
}
