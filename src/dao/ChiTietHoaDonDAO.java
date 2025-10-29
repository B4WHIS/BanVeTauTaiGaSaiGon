package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.Ve;

public class ChiTietHoaDonDAO {

    private ChiTietHoaDon getChiTietFromRS(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHoaDon(rs.getString("maHoaDon"));

        Ve ve = new Ve();
        ve.setMaVe(rs.getString("maVe"));

        // Lấy thêm thông tin vé nếu có
        try {
            if (rs.findColumn("trangThai") > 0) {
                ve.setTrangThai(rs.getString("trangThai"));
            }
            if (rs.findColumn("giaThanhToan") > 0) {
                ve.setGiaThanhToan(rs.getBigDecimal("giaThanhToan"));
            }
        } catch (SQLException ignored) {}

        BigDecimal donGia = rs.getBigDecimal("donGia");
        return new ChiTietHoaDon(hd, ve, donGia);
    }

    public boolean insert(ChiTietHoaDon cthd) throws SQLException {
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maVe, donGia) VALUES (?, ?, ?)";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cthd.getHoaDon().getMaHoaDon());
            stmt.setString(2, cthd.getVe().getMaVe());
            stmt.setBigDecimal(3, cthd.getDonGia());

            return stmt.executeUpdate() > 0;
        }
    }

    public List<ChiTietHoaDon> getByMaHoaDon(String maHoaDon) throws SQLException {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = """
            SELECT cthd.maHoaDon, cthd.maVe, cthd.donGia,
                   v.trangThai, v.giaThanhToan
            FROM ChiTietHoaDon cthd
            JOIN Ve v ON cthd.maVe = v.maVe
            WHERE cthd.maHoaDon = ?
            """;

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(getChiTietFromRS(rs));
                }
            }
        }
        return list;
    }

    public boolean delete(String maHoaDon, String maVe) throws SQLException {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ? AND maVe = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);
            stmt.setString(2, maVe);
            return stmt.executeUpdate() > 0;
        }
    }

    public ChiTietHoaDon findByMaVe(String maVe) throws SQLException {
        String sql = "SELECT maHoaDon, maVe, donGia FROM ChiTietHoaDon WHERE maVe = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maVe);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return getChiTietFromRS(rs);
                }
            }
        }
        return null;
    }

    public BigDecimal tinhTongTien(String maHoaDon) throws SQLException {
        String sql = "SELECT SUM(donGia) AS tong FROM ChiTietHoaDon WHERE maHoaDon = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("tong");
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public boolean insert(ChiTietHoaDon cthd, Connection conn) throws SQLException { // <-- THÊM THAM SỐ CONNECTION
        // Bảng ChiTietHoaDon chứa maHoaDon, maVe, donGia [5, 6]
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maVe, donGia) VALUES (?, ?, ?)";
        
        // Sử dụng Connection được truyền vào (conn)
        try (PreparedStatement ps = conn.prepareStatement(sql)) { 
            ps.setString(1, cthd.getHoaDon().getMaHoaDon()); 
            ps.setString(2, cthd.getVe().getMaVe());
            ps.setBigDecimal(3, cthd.getDonGia()); 
            
            // executeUpdate() thực hiện INSERT và trả về số hàng bị ảnh hưởng [7]
            return ps.executeUpdate() > 0;
        }
    }
}