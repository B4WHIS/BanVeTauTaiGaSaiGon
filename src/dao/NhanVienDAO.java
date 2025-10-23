package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.NhanVien;
import connectDB.connectDB;

public class NhanVienDAO {

    // Phương thức lấy tất cả tên nhân viên (cho ComboBox) - Chỉ hoTen
    public List<String> getAllTenNhanVien() {
        List<String> listTenNV = new ArrayList<>();
        String sql = "SELECT hoTen FROM NhanVien ORDER BY hoTen";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                listTenNV.add(rs.getString("hoTen"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listTenNV;
    }

    // Phương thức lấy nhân viên theo tên (hoTen)
    public NhanVien getNhanVienByTen(String hoTen) {
        String sql = "SELECT * FROM NhanVien WHERE hoTen = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hoTen);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return taoNhanVienTuResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức lấy tên nhân viên theo mã
    public String getTenNhanVienByMa(String maNhanVien) {
        String sql = "SELECT hoTen FROM NhanVien WHERE maNhanVien = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNhanVien);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("hoTen");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức lấy nhân viên theo mã
    public NhanVien getNhanVienByMa(String maNhanVien) {
        String sql = "SELECT * FROM NhanVien WHERE maNhanVien = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNhanVien);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return taoNhanVienTuResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper: Tạo NhanVien từ ResultSet - Cập nhật để load IDloaiChucVu
    private NhanVien taoNhanVienTuResultSet(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();
        nv.setMaNhanVien(rs.getString("maNhanVien"));
        nv.setHoTen(rs.getString("hoTen"));
        nv.setNgaySinh(rs.getDate("ngaySinh").toLocalDate());
        nv.setSoDienThoai(rs.getString("soDienThoai"));
        if (rs.getString("cmndCccd") != null) {
            nv.setCmndCccd(rs.getString("cmndCccd"));
        }
        nv.setIDloaiChucVu(rs.getInt("IDloaiChucVu"));  // Load IDloaiChucVu
        return nv;
    }
}