package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.HanhKhach;
import entity.HoaDon;
import entity.NhanVien;

public class HoaDonDAO {

    private HoaDon getHoaDonFromRS(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHoaDon(rs.getString("maHoaDon"));
        hd.setNgayLap(rs.getTimestamp("ngayLap").toLocalDateTime());

        HanhKhach hk = new HanhKhach();
        hk.setMaKH(rs.getString("maHanhKhach"));
        hk.setHoTen(rs.getString("tenHanhKhach"));
        hd.setMaHanhKhach(hk);

        NhanVien nv = new NhanVien();
        nv.setMaNhanVien(rs.getString("maNhanVien"));
        nv.setHoTen(rs.getString("tenNhanVien"));
        hd.setMaNhanVien(nv);

        hd.setTongTien(rs.getBigDecimal("tongTien"));
        return hd;
    }

    public String insert(HoaDon hd) throws SQLException {
        String sql = """
            INSERT INTO HoaDon (ngayLap, tongTien, maNhanVien, maHanhKhach)
            OUTPUT INSERTED.maHoaDon
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, hd.getNgayLap());
            stmt.setBigDecimal(2, hd.getTongTien()); // ← DÙNG setTongTien
            stmt.setString(3, hd.getMaNhanVien().getMaNhanVien());
            stmt.setString(4, hd.getMaHanhKhach().getMaKH());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String maHoaDon = rs.getString(1);
                    hd.setMaHoaDon(maHoaDon);
                    return maHoaDon;
                }
            }
        }
        throw new SQLException("Không thể tạo hóa đơn.");
    }

    public List<HoaDon> getAll() throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        String sql = """
            SELECT hd.maHoaDon, hd.ngayLap, hd.tongTien,
                   hk.maHanhKhach, hk.hoTen AS tenHanhKhach,
                   nv.maNhanVien, nv.hoTen AS tenNhanVien
            FROM HoaDon hd
            JOIN HanhKhach hk ON hd.maHanhKhach = hk.maHanhKhach
            JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
            ORDER BY hd.ngayLap DESC
            """;

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(getHoaDonFromRS(rs));
            }
        }
        return list;
    }

    public HoaDon findByMa(String maHD) throws SQLException {
        String sql = """
            SELECT hd.maHoaDon, hd.ngayLap, hd.tongTien,
                   hk.maHanhKhach, hk.hoTen AS tenHanhKhach,
                   nv.maNhanVien, nv.hoTen AS tenNhanVien
            FROM HoaDon hd
            JOIN HanhKhach hk ON hd.maHanhKhach = hk.maHanhKhach
            JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
            WHERE hd.maHoaDon = ?
            """;

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maHD);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return getHoaDonFromRS(rs);
                }
            }
        }
        return null;
    }

    public List<HoaDon> getByHanhKhach(String maHK) throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT maHoaDon, ngayLap, tongTien FROM HoaDon WHERE maHanhKhach = ?";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maHK);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMaHoaDon(rs.getString("maHoaDon"));
                    hd.setNgayLap(rs.getTimestamp("ngayLap").toLocalDateTime());
                    hd.setTongTien(rs.getBigDecimal("tongTien"));
                    list.add(hd);
                }
            }
        }
        return list;
    }
}