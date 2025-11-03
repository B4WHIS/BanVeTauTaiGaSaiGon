package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import connectDB.connectDB;
import entity.KhuyenMai;

public class KhuyenMaiDAO {

    // === KIỂM TRA TRÙNG TÊN ===
    private boolean isTrungTen(String tenKM, String maHienTai) {
        String sql = "SELECT COUNT(*) FROM KhuyenMai WHERE tenKhuyenMai = ? AND trangThai = N'Hoạt động'";
        if (maHienTai != null) sql += " AND maKhuyenMai != ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, tenKM);
            if (maHienTai != null) stmt.setString(2, maHienTai);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // === KIỂM TRA TRÙNG NGÀY ===
    private boolean isTrungNgay(LocalDate batDau, LocalDate ketThuc, String maHienTai) {
        String sql = "SELECT COUNT(*) FROM KhuyenMai " +
                     "WHERE (ngayBatDau <= ? AND ngayKetThuc >= ?) AND trangThai = N'Hoạt động'";
        if (maHienTai != null) sql += " AND maKhuyenMai != ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(ketThuc));
            stmt.setDate(2, Date.valueOf(batDau));
            if (maHienTai != null) stmt.setString(3, maHienTai);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // === KIỂM TRA TRÙNG MÃ ===
    private boolean isTrungMa(String maKM) {
        String sql = "SELECT COUNT(*) FROM KhuyenMai WHERE maKhuyenMai = ? AND trangThai = N'Hoạt động'";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKM);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // === THÊM (NHẬP MÃ THỦ CÔNG) ===
    public boolean addKhuyenMai(KhuyenMai km) {
        if (km.getMaKhuyenMai() == null || km.getMaKhuyenMai().trim().isEmpty()) {
            System.err.println("Lỗi: Mã khuyến mãi không được để trống!");
            return false;
        }
        if (isTrungMa(km.getMaKhuyenMai())) {
            System.err.println("Lỗi: Mã '" + km.getMaKhuyenMai() + "' đã tồn tại!");
            return false;
        }
        if (isTrungTen(km.getTenKhuyenMai(), null)) return false;
        if (isTrungNgay(km.getNgayBatDau(), km.getNgayKetThuc(), null)) return false;

        String sql = "INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, mucGiamGia, ngayBatDau, ngayKetThuc, dieuKien, trangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?, N'Hoạt động')";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, km.getMaKhuyenMai());
            stmt.setString(2, km.getTenKhuyenMai());
            stmt.setBigDecimal(3, km.getMucGiamGia());
            stmt.setDate(4, Date.valueOf(km.getNgayBatDau()));
            stmt.setDate(5, Date.valueOf(km.getNgayKetThuc()));
            stmt.setString(6, km.getDieuKien());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // === SỬA ===
    public boolean updateKhuyenMai(KhuyenMai km) {
        if (isTrungTen(km.getTenKhuyenMai(), km.getMaKhuyenMai())) return false;
        if (isTrungNgay(km.getNgayBatDau(), km.getNgayKetThuc(), km.getMaKhuyenMai())) return false;

        String sql = "UPDATE KhuyenMai SET tenKhuyenMai = ?, mucGiamGia = ?, ngayBatDau = ?, ngayKetThuc = ?, dieuKien = ? " +
                     "WHERE maKhuyenMai = ? AND trangThai = N'Hoạt động'";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, km.getTenKhuyenMai());
            stmt.setBigDecimal(2, km.getMucGiamGia());
            stmt.setDate(3, Date.valueOf(km.getNgayBatDau()));
            stmt.setDate(4, Date.valueOf(km.getNgayKetThuc()));
            stmt.setString(5, km.getDieuKien());
            stmt.setString(6, km.getMaKhuyenMai());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi sửa: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // === XÓA MỀM ===
    public boolean deleteKhuyenMai(String maKM) {
        String sql = "UPDATE KhuyenMai SET trangThai = N'Ngừng hoạt động' WHERE maKhuyenMai = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKM);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // === LẤY TẤT CẢ ===
    public ArrayList<KhuyenMai> getAllKhuyenMai() {
        ArrayList<KhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE trangThai = N'Hoạt động' ORDER BY ngayBatDau DESC";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) ds.add(taoKhuyenMai(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // === TÌM THEO MÃ ===
    public KhuyenMai TimKhuyenMaiTheoMa(String maKM) {
        String sql = "SELECT * FROM KhuyenMai WHERE maKhuyenMai = ? AND trangThai = N'Hoạt động'";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKM);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return taoKhuyenMai(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // === TẠO ENTITY ===
    private KhuyenMai taoKhuyenMai(ResultSet rs) throws SQLException {
        return new KhuyenMai(
            rs.getString("maKhuyenMai"),
            rs.getString("tenKhuyenMai"),
            rs.getBigDecimal("mucGiamGia"),
            rs.getDate("ngayBatDau").toLocalDate(),
            rs.getDate("ngayKetThuc").toLocalDate(),
            rs.getString("dieuKien")
        );
    }
}