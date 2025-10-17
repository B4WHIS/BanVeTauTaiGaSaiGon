package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.ToaTau;
import entity.Tau;
import connectDB.connectDB;

public class ToaTauDAO {

    // Phương thức lấy tất cả toa tàu
    public List<ToaTau> getAllToaTau() {
        List<ToaTau> listToaTau = new ArrayList<>();
        String sql = "SELECT tt.maToa, tt.soThuTu, tt.soLuongCho, tt.heSoGia, tt.maTau, t.tenTau, t.soLuongToa " +
                     "FROM ToaTau tt " +
                     "JOIN Tau t ON tt.maTau = t.maTau";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Tau tau = new Tau(rs.getString("maTau"), rs.getString("tenTau"), rs.getInt("soLuongToa"));
                ToaTau toaTau = new ToaTau();
                toaTau.setMaToa(rs.getString("maToa"));
                toaTau.setSoThuTu(rs.getInt("soThuTu"));
                toaTau.setSoLuongCho(rs.getInt("soLuongCho"));
                toaTau.setHeSoGia(rs.getBigDecimal("heSoGia"));
                toaTau.setTau(tau);
                listToaTau.add(toaTau);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listToaTau;
    }

    // Phương thức lấy toa tàu theo mã toa
    public ToaTau getToaTauByMaToa(String maToa) {
        String sql = "SELECT tt.maToa, tt.soThuTu, tt.soLuongCho, tt.heSoGia, tt.maTau, t.tenTau, t.soLuongToa " +
                     "FROM ToaTau tt " +
                     "JOIN Tau t ON tt.maTau = t.maTau " +
                     "WHERE tt.maToa = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maToa);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Tau tau = new Tau(rs.getString("maTau"), rs.getString("tenTau"), rs.getInt("soLuongToa"));
                    ToaTau toaTau = new ToaTau();
                    toaTau.setMaToa(rs.getString("maToa"));
                    toaTau.setSoThuTu(rs.getInt("soThuTu"));
                    toaTau.setSoLuongCho(rs.getInt("soLuongCho"));
                    toaTau.setHeSoGia(rs.getBigDecimal("heSoGia"));
                    toaTau.setTau(tau);
                    return toaTau;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức thêm toa tàu mới (maToa sẽ được tự động tạo bởi DB)
    public boolean addToaTau(ToaTau toaTau) {
        if (toaTau.getSoThuTu() <= 0) {
            throw new IllegalArgumentException("Số thứ tự toa phải lớn hơn 0");
        }
        if (toaTau.getSoLuongCho() <= 0) {
            throw new IllegalArgumentException("Số lượng chỗ phải lớn hơn 0");
        }
        if (toaTau.getHeSoGia() == null || toaTau.getHeSoGia().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Hệ số giá phải lớn hơn 0");
        }
        String sql = "INSERT INTO ToaTau (soThuTu, soLuongCho, heSoGia, maTau) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, toaTau.getSoThuTu());
            pstmt.setInt(2, toaTau.getSoLuongCho());
            pstmt.setBigDecimal(3, toaTau.getHeSoGia());
            pstmt.setString(4, toaTau.getTau().getMaTau());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Lấy ID tự động tạo để sinh maToa
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        String generatedMaToa = "TOA-" + String.format("%02d", id);
                        toaTau.setMaToa(generatedMaToa);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức cập nhật toa tàu
    public boolean updateToaTau(ToaTau toaTau) {
        if (toaTau.getSoThuTu() <= 0) {
            throw new IllegalArgumentException("Số thứ tự toa phải lớn hơn 0");
        }
        if (toaTau.getSoLuongCho() <= 0) {
            throw new IllegalArgumentException("Số lượng chỗ phải lớn hơn 0");
        }
        if (toaTau.getHeSoGia() == null || toaTau.getHeSoGia().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Hệ số giá phải lớn hơn 0");
        }
        String sql = "UPDATE ToaTau SET soThuTu = ?, soLuongCho = ?, heSoGia = ?, maTau = ? WHERE maToa = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, toaTau.getSoThuTu());
            pstmt.setInt(2, toaTau.getSoLuongCho());
            pstmt.setBigDecimal(3, toaTau.getHeSoGia());
            pstmt.setString(4, toaTau.getTau().getMaTau());
            pstmt.setString(5, toaTau.getMaToa());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức xóa toa tàu theo mã toa
    public boolean deleteToaTau(String maToa) {
        String sql = "DELETE FROM ToaTau WHERE maToa = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maToa);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}