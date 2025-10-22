package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class connectDB {
    private static final Logger LOGGER = Logger.getLogger(connectDB.class.getName());
    
    // Thông tin kết nối SQL Server - Cập nhật nếu cần
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://localhost:1433;" +
                                      "databaseName=QuanLyBanVeTau;" +
                                      "encrypt=false;" +
                                      "trustServerCertificate=true;" +
                                      "loginTimeout=30";  // Thêm timeout để tránh treo
    private static final String USER = "sa";
    private static final String PASS = "sapassword";  // Thay bằng pass thực tế nếu khác

    // Tải driver một lần (static block)
    static {
        try {
            Class.forName(DRIVER);
            LOGGER.info("SQL Server JDBC Driver đã tải thành công.");
        } catch (ClassNotFoundException e) {
            LOGGER.severe("Không tìm thấy SQL Server JDBC Driver! Thêm jar mssql-jdbc vào classpath.");
            throw new RuntimeException(e);
        }
    }

    // Tạo kết nối MỚI mỗi lần gọi (không dùng static connection)
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Không thể tạo kết nối hợp lệ.");
            }
            LOGGER.info("Kết nối database thành công.");
            return conn;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi kết nối: " + e.getMessage(), e);
            throw new SQLException("Lỗi kết nối database: " + e.getMessage() + 
                                   "\nKiểm tra: SQL Server chạy chưa? Thông tin đăng nhập đúng? Database tồn tại?", e);
        }
    }

    // Không cần closeConnection() nữa vì mỗi lần dùng try-with-resources sẽ tự đóng
    // Nhưng nếu muốn đóng thủ công (không khuyến khích), giữ lại:
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                LOGGER.info("Đã đóng kết nối.");
            } catch (SQLException e) {
                LOGGER.severe("Lỗi đóng kết nối: " + e.getMessage());
            }
        }
    }

    // Test kết nối nhanh
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Test kết nối: THÀNH CÔNG!");
        } catch (SQLException e) {
            System.err.println("Test kết nối: THẤT BẠI - " + e.getMessage());
        }
    }
}