package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.*;

public class ChiTietHoaDonDAO {

//    Thêm chi tiết hóa đơn mới
    
    public boolean insertChiTietHoaDon(ChiTietHoaDon cthd) {
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maVe, donGia) VALUES (?, ?, ?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, cthd.getHoaDon().getMaHoaDon());
            stmt.setString(2, cthd.getVe().getMaVe());
            stmt.setDouble(3, cthd.getDonGia());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm chi tiết hóa đơn: " + e.getMessage());
        }
        return false;
    }

  
//    Lấy danh sách chi tiết theo mã hóa đơn
    
    public List<ChiTietHoaDon> getChiTietByMaHoaDon(String maHoaDon) {
        List<ChiTietHoaDon> dsChiTiet = new ArrayList<>();
        String sql = """
                SELECT cthd.maHoaDon, cthd.maVe, cthd.donGia,
                       v.maVe AS maVeHD, v.trangThai, v.giaThanhToan
                FROM ChiTietHoaDon cthd
                JOIN Ve v ON cthd.maVe = v.maVe
                WHERE cthd.maHoaDon = ?
                """;
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString("maHoaDon"));

                Ve ve = new Ve();
                ve.setMaVe(rs.getString("maVeHD"));
                ve.setTrangThai(rs.getString("trangThai"));
                ve.setGiaThanhToan(rs.getBigDecimal("giaThanhToan"));

                ChiTietHoaDon cthd = new ChiTietHoaDon(hd, ve, rs.getDouble("donGia"));
                dsChiTiet.add(cthd);
            }

        } catch (SQLException e) {
            System.err.println(" Lỗi đọc chi tiết hóa đơn: " + e.getMessage());
        }
        return dsChiTiet;
    }

//     Xóa chi tiết hóa đơn theo mã hóa đơn và mã vé
    public boolean deleteChiTietHoaDon(String maHoaDon, String maVe) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ? AND maVe = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);
            stmt.setString(2, maVe);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa chi tiết hóa đơn: " + e.getMessage());
        }
        return false;
    }

    
//    Tìm chi tiết hóa đơn theo mã vé
    
    public ChiTietHoaDon findByMaVe(String maVe) {
        String sql = """
                SELECT cthd.maHoaDon, cthd.maVe, cthd.donGia
                FROM ChiTietHoaDon cthd
                WHERE cthd.maVe = ?
                """;
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maVe);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString("maHoaDon"));

                Ve ve = new Ve();
                ve.setMaVe(rs.getString("maVe"));

                return new ChiTietHoaDon(hd, ve, rs.getDouble("donGia"));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi tìm chi tiết hóa đơn theo mã vé: " + e.getMessage());
        }
        return null;
    }

   
//  Tính tổng tiền của hóa đơn (sum(donGia))

    public double tinhTongTien(String maHoaDon) {
        String sql = "SELECT SUM(donGia) AS tong FROM ChiTietHoaDon WHERE maHoaDon = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);
            ResultSet rs = stmt.executeQuery(); 
            if (rs.next()) {
                return rs.getDouble("tong");
            }
        } catch (SQLException e) {
            System.err.println(" Lỗi tính tổng tiền hóa đơn: " + e.getMessage());
        }
        return 0.0;
    }
}
