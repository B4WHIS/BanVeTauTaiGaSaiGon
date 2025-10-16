package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.Tau;
import connectDB.connectDB;

public class TauDAO {
    // Phương thức lấy tất cả tàu
    public List<Tau> getAllTau() {
        List<Tau> listTau = new ArrayList<>();
        String sql = "SELECT maTau, tenTau, soLuongToa FROM Tau";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Tau tau = new Tau();
                tau.setMaTau(rs.getString("maTau"));
                tau.setTenTau(rs.getString("tenTau"));
                tau.setSoToa(rs.getInt("soLuongToa"));
                listTau.add(tau);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listTau;
    }

    // Phương thức lấy tàu theo mã tàu
    public Tau getTauByMaTau(String maTau) {
        String sql = "SELECT maTau, tenTau, soLuongToa FROM Tau WHERE maTau = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maTau);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Tau tau = new Tau();
                    tau.setMaTau(rs.getString("maTau"));
                    tau.setTenTau(rs.getString("tenTau"));
                    tau.setSoToa(rs.getInt("soLuongToa"));
                    return tau;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức thêm tàu mới (maTau sẽ được tự động tạo bởi DB)
    public boolean addTau(Tau tau) {
        if (tau.getSoToa() <= 0) {
            throw new IllegalArgumentException("Số lượng toa phải lớn hơn 0");
        }
        String sql = "INSERT INTO Tau (tenTau, soLuongToa) VALUES (?, ?)";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, tau.getTenTau());
            pstmt.setInt(2, tau.getSoToa());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Lấy ID tự động tạo để sinh maTau
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        String generatedMaTau = "T-" + String.format("%04d", id);
                        tau.setMaTau(generatedMaTau);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức cập nhật tàu
    public boolean updateTau(Tau tau) {
        if (tau.getSoToa() <= 0) {
            throw new IllegalArgumentException("Số lượng toa phải lớn hơn 0");
        }
        String sql = "UPDATE Tau SET tenTau = ?, soLuongToa = ? WHERE maTau = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tau.getTenTau());
            pstmt.setInt(2, tau.getSoToa());
            pstmt.setString(3, tau.getMaTau());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức xóa tàu theo mã tàu
    public boolean deleteTau(String maTau) {
        String sql = "DELETE FROM Tau WHERE maTau = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maTau);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}