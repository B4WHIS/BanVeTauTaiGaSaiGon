package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectDB {

    public static Connection getConnection() {
    	try {
                // Thông tin kết nối SQL Server
            	String url = "jdbc:sqlserver://localhost:1433;"
            	        + "databaseName=QuanLyBanVeTau;"
            	        + "encrypt=false;"
            	        + "trustServerCertificate=true";

                String user = "sa";
                String password = "sapassword";


                return DriverManager.getConnection(url, user, password);
               
            } catch (SQLException e) {
                System.err.println(" Lỗi kết nối CSDL: " + e.getMessage());
                throw new RuntimeException("Không thể kết nối đến cơ sở dữ liệu QuanLyBanVeTau", e);
        }
      
       
    }

//    // Phương thức đóng kết nối
//   public static void closeConnection() {
//        if (connection != null) {
//            try {
//                connection.close();
//                connection = null;
//                System.out.println("Đã đóng kết nối CSDL.");
//            } catch (SQLException e) {
//               System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
//            }
//        }
//    }
//
//    // Test nhanh kết nối
//   public static void main(String[] args) {
//        getConnection();
//        closeConnection();
//    }
}
