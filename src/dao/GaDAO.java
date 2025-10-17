package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.Ga;
import connectDB.connectDB;

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
}