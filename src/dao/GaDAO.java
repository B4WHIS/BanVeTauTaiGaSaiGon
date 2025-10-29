package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.Ga;

public class GaDAO {

    // Phương thức lấy tất cả ga
    public List<Ga> getAllGa() {
        List<Ga> listGa = new ArrayList<>();
        String sql = "SELECT maGa, tenGa, diaChi FROM Ga";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Ga ga = new Ga();
                ga.setMaGa(rs.getString("maGa"));
                ga.setTenGa(rs.getString("tenGa"));
                ga.setDiaChi(rs.getString("diaChi"));
                listGa.add(ga);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listGa;
    }

    // Phương thức lấy ga theo mã ga
    public Ga getGaByMaGa(String maGa) {
        String sql = "SELECT maGa, tenGa, diaChi FROM Ga WHERE maGa = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maGa);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Ga ga = new Ga();
                    ga.setMaGa(rs.getString("maGa"));
                    ga.setTenGa(rs.getString("tenGa"));
                    ga.setDiaChi(rs.getString("diaChi"));
                    return ga;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String getMaGaByTenGa(String tenGa) {
        String maGa = null;
        String sql = "SELECT maGa FROM Ga WHERE tenGa = ?"; 
        
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tenGa); 
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    maGa = rs.getString("maGa"); 
                }
            }
        } catch (SQLException e) {
            System.err.println(" Lỗi truy vấn mã Ga theo tên: " + e.getMessage());
            e.printStackTrace();
        }
        return maGa; 
    }
    
    // Phương thức lấy ga theo tên ga (tìm kiếm chứa tên)
    public List<Ga> getGaByTenGa(String tenGa) {
        List<Ga> listGa = new ArrayList<>();
        String sql = "SELECT maGa, tenGa, diaChi FROM Ga WHERE tenGa LIKE ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + tenGa + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ga ga = new Ga();
                    ga.setMaGa(rs.getString("maGa"));
                    ga.setTenGa(rs.getString("tenGa"));
                    ga.setDiaChi(rs.getString("diaChi"));
                    listGa.add(ga);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listGa;
    }

    // Phương thức thêm ga mới (maGa sẽ được tự động tạo bởi DB)
    public boolean addGa(Ga ga) {
        String sql = "INSERT INTO Ga (tenGa, diaChi) VALUES (?, ?)";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, ga.getTenGa());
            pstmt.setString(2, ga.getDiaChi());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Lấy ID tự động tạo để sinh maGa
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        String generatedMaGa = "GA-" + String.format("%02d", id);
                        ga.setMaGa(generatedMaGa);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức cập nhật ga
    public boolean updateGa(Ga ga) {
        String sql = "UPDATE Ga SET tenGa = ?, diaChi = ? WHERE maGa = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ga.getTenGa());
            pstmt.setString(2, ga.getDiaChi());
            pstmt.setString(3, ga.getMaGa());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức xóa ga theo mã ga
    public boolean deleteGa(String maGa) {
        String sql = "DELETE FROM Ga WHERE maGa = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maGa);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getTenGaDiByMaLichTrinh(String maLichTrinh) throws SQLException {
        String tenGa = null;
        // LT.gaDi là mã ga đi trong bảng LichTrinh [1]. G.maGa là mã ga trong bảng Ga [2].
        String sql = "SELECT G.tenGa FROM LichTrinh LT JOIN Ga G ON LT.gaDi = G.maGa WHERE LT.maLichTrinh = ?";
        
        try (Connection conn = connectDB.getConnection(); // Lấy kết nối [3]
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maLichTrinh);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tenGa = rs.getString("tenGa");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL khi lấy Tên Ga Đi theo Mã Lịch Trình: " + e.getMessage());
            // Ném lỗi để tầng Control xử lý hoặc log
            throw e;
        }
        return tenGa;
    }
    public String getTenGaDenByMaLichTrinh(String maLichTrinh) throws SQLException {
        String tenGa = null;
        // Thay đổi LT.gaDi thành LT.gaDen
        String sql = "SELECT G.tenGa FROM LichTrinh LT JOIN Ga G ON LT.gaDen = G.maGa WHERE LT.maLichTrinh = ?";
        
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maLichTrinh);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tenGa = rs.getString("tenGa");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL khi lấy Tên Ga Đến theo Mã Lịch Trình: " + e.getMessage());
            throw e;
        }
        return tenGa;
    }
}