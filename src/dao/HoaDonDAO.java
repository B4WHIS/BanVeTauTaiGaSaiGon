package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.*;

public class HoaDonDAO {

  
    public boolean insertHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (ngayLap, tongTien, maHanhKhach, maNhanVien) VALUES (?, ?, ?, ?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(hd.getNgayLap()));
            stmt.setDouble(2, hd.tinhTongTien());
            stmt.setString(3, hd.getMaHanhKhach().getMaKH());
            stmt.setString(4, hd.getMaNhanVien().getMaNhanVien());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Lỗi thêm hóa đơn: " + e.getMessage());
        }
        return false;
    }

   
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> dsHD = new ArrayList<>();
        String sql = """
                SELECT hd.maHoaDon, hd.ngayLap, hd.tongTien,
                       hk.maHanhKhach, hk.hoTen AS tenHanhKhach,
                       nv.maNhanVien, nv.hoTen AS tenNhanVien
                FROM HoaDon hd
                JOIN HanhKhach hk ON hd.maHanhKhach = hk.maHanhKhach
                JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
                ORDER BY hd.ngayLap DESC
                """;
        try (Connection con = connectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
               
                HanhKhach hk = new HanhKhach();
                hk.setMaKH(rs.getString("maHanhKhach"));
                hk.setHoTen(rs.getString("tenHanhKhach"));

                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));
                nv.setHoTen(rs.getString("tenNhanVien"));

              
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString("maHoaDon"));
                hd.setNgayLap(rs.getTimestamp("ngayLap").toLocalDateTime());
                hd.setMaHanhKhach(hk);
                hd.setMaNhanVien(nv);
                hd.setTongTien(rs.getDouble("tongTien"));

                dsHD.add(hd);
            }

        } catch (SQLException e) {
            System.err.println(" Lỗi đọc danh sách hóa đơn: " + e.getMessage());
        }
        return dsHD;
    }

   
    public HoaDon findByMaHoaDon(String maHD) {
        String sql = """
                SELECT hd.maHoaDon, hd.ngayLap, hd.tongTien,
                       hk.maHanhKhach, hk.hoTen AS tenHanhKhach,
                       nv.maNhanVien, nv.hoTen AS tenNhanVien
                FROM HoaDon hd
                JOIN HanhKhach hk ON hd.maHanhKhach = hk.maHanhKhach
                JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
                WHERE hd.maHoaDon = ?
                """;
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maHD);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                HanhKhach hk = new HanhKhach();
                hk.setMaKH(rs.getString("maHanhKhach"));
                hk.setHoTen(rs.getString("tenHanhKhach"));

                NhanVien nv = new NhanVien();
                nv.setMaNhanVien(rs.getString("maNhanVien"));
                nv.setHoTen(rs.getString("tenNhanVien"));

                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString("maHoaDon"));
                hd.setNgayLap(rs.getTimestamp("ngayLap").toLocalDateTime());
                hd.setMaHanhKhach(hk);
                hd.setMaNhanVien(nv);
                hd.setTongTien(rs.getDouble("tongTien"));
                return hd;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi tìm hóa đơn theo mã: " + e.getMessage());
        }
        return null;
    }

    
    public List<HoaDon> getHoaDonByHanhKhach(String maHanhKhach) {
        List<HoaDon> dsHD = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE maHanhKhach = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maHanhKhach);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHoaDon(rs.getString("maHoaDon"));
                hd.setNgayLap(rs.getTimestamp("ngayLap").toLocalDateTime());
                hd.setTongTien(rs.getDouble("tongTien"));
                dsHD.add(hd);
            }

        } catch (SQLException e) {
            System.err.println(" Lỗi đọc hóa đơn theo hành khách: " + e.getMessage());
        }
        return dsHD;
    }
}
