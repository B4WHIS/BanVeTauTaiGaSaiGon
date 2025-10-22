package dao;

import java.sql.*;
import java.util.ArrayList;

import connectDB.connectDB;
import entity.KhuyenMai;

public class KhuyenMaiDAO {

    // Thêm khuyến mãi
    public boolean ThemKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, mucGiamGia, ngayBatDau, ngayKetThuc, dieuKien) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
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
            System.err.println("lỗi thêm: " +e.getMessage());
        }
        return false;
    }

    //  Cập nhật khuyến mãi
    public boolean CapNhatKhuyenMai(KhuyenMai khuyenMai) {
        String sql = "UPDATE KhuyenMai SET tenKhuyenMai = ?, mucGiamGia = ?, ngayBatDau = ?, ngayKetThuc = ?, dieuKien = ? "
                   + "WHERE maKhuyenMai = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, khuyenMai.getTenKhuyenMai());
            stmt.setBigDecimal(2, khuyenMai.getMucGiamGia());
            stmt.setDate(3, Date.valueOf(khuyenMai.getNgayBatDau()));
            stmt.setDate(4, Date.valueOf(khuyenMai.getNgayKetThuc()));
            stmt.setString(5, khuyenMai.getDieuKien());
            stmt.setString(6, khuyenMai.getMaKhuyenMai());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" cập nhật thất bại");
        }
        return false;
    }

    
    
    // Xóa khuyến mãi
    public boolean XoaKhuyenMai(String maKhuyenMai) {
        String sql = "DELETE FROM KhuyenMai WHERE maKhuyenMai = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maKhuyenMai);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Lỗi Xóa: " + e.getMessage());
        }
        return false;
    }

    // Tìm khuyến mãi theo mã
    public KhuyenMai TimKhuyenMaiTheoMa(String maKhuyenMai) {
        String sql = "SELECT * FROM KhuyenMai WHERE maKhuyenMai = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maKhuyenMai);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new KhuyenMai(
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        rs.getBigDecimal("mucGiamGia"),
                        rs.getDate("ngayBatDau").toLocalDate(),
                        rs.getDate("ngayKetThuc").toLocalDate(),
                        rs.getString("dieuKien")
                );
            }
        } catch (SQLException e) {
            System.err.println("lỗi tìm kiếm:  " + e.getMessage());
        }
        return null;
    }
    
    
    
 // Lấy ds khuyến mãi đang cònđang hoạt động
    public ArrayList<KhuyenMai> LayKhuyenMaiDangHoatDong() {
        ArrayList<KhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE GETDATE() BETWEEN ngayBatDau AND ngayKetThuc";
        try (Connection con = connectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ds.add(new KhuyenMai(
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        rs.getBigDecimal("mucGiamGia"),
                        rs.getDate("ngayBatDau").toLocalDate(),
                        rs.getDate("ngayKetThuc").toLocalDate(),
                        rs.getString("dieuKien")
                ));
            }
        } catch (SQLException e) {
            System.err.println("lỗi rồi" + e.getMessage());
        }
        return ds;
    }
    
}
