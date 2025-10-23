package connectDB;

import java.sql.Connection;

public class TestConnect {
    public static void main(String[] args) {
        try {
            Connection conn = connectDB.getConnection();
            if (conn != null) {
                System.out.println(" Kết nối CSDL thành công!");
                conn.close(); 
            } else {
                System.out.println(" Kết nối thất bại!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
