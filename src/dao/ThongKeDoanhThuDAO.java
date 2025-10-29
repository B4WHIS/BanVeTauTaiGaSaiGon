package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import connectDB.connectDB;

public class ThongKeDoanhThuDAO {

    public Map<String, Double> thongKeTheoNgay(java.util.Date ngaybatdau, java.util.Date ngayketthuc) {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = """
            SELECT CAST(ngayLap AS DATE) AS Ngay, SUM(tongTien) AS Tong
            FROM HoaDon
            WHERE ngayLap BETWEEN ? AND ?
            GROUP BY CAST(ngayLap AS DATE)
            ORDER BY CAST(ngayLap AS DATE)
        """;
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(ngaybatdau.getTime()));
            ps.setDate(2, new java.sql.Date(ngayketthuc.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getDate("Ngay").toString(), rs.getDouble("Tong"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Double> thongKeTheoThang(int nam) {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = """
            SELECT MONTH(ngayLap) AS Thang, SUM(tongTien) AS Tong
            FROM HoaDon
            WHERE YEAR(ngayLap) = ?
            GROUP BY MONTH(ngayLap)
            ORDER BY MONTH(ngayLap)
        """;
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put("Tháng " + rs.getInt("Thang"), rs.getDouble("Tong"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Double> thongKeTheoNam() {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = """
            SELECT YEAR(ngayLap) AS Nam, SUM(tongTien) AS Tong
            FROM HoaDon
            GROUP BY YEAR(ngayLap)
            ORDER BY YEAR(ngayLap)
        """;
        try (Connection con = connectDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                result.put(String.valueOf(rs.getInt("Nam")), rs.getDouble("Tong"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Object[]> thongKeChiTietNhanVien() {
        List<Object[]> ds = new ArrayList<>();
        String sql = """
            SELECT nv.maNhanVien, nv.hoTen, nv.ngaySinh,
                   COALESCE(SUM(hd.tongTien), 0) AS tongDoanhThu
            FROM NhanVien nv
            LEFT JOIN HoaDon hd ON nv.maNhanVien = hd.maNhanVien
            GROUP BY nv.maNhanVien, nv.hoTen, nv.ngaySinh
            ORDER BY tongDoanhThu DESC
        """;
        try (Connection con = connectDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Object[] row = {
                    rs.getString("maNhanVien"),
                    rs.getString("hoTen"),
                    rs.getDate("ngaySinh"),
                    rs.getDouble("tongDoanhThu")
                };
                ds.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public List<Object[]> thongKeChiTietKhachHang() {
        List<Object[]> ds = new ArrayList<>();
        String sql = """
            SELECT hk.maHanhKhach, hk.hoTen,
                   COALESCE(SUM(hd.tongTien), 0) AS tongDoanhThu
            FROM HanhKhach hk
            LEFT JOIN HoaDon hd ON hk.maHanhKhach = hd.maHanhKhach
            GROUP BY hk.maHanhKhach, hk.hoTen
            ORDER BY tongDoanhThu DESC
        """;
        try (Connection con = connectDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Object[] row = {
                    rs.getString("maHanhKhach"),
                    rs.getString("hoTen"),
                    rs.getDouble("tongDoanhThu")
                };
                ds.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    private int demVeTheoTrangThai(String trangThai) {
        String sql = "SELECT COUNT(*) FROM Ve WHERE trangThai = N'" + trangThai + "'";
        try (Connection con = connectDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public List<Object[]> getHoaDonTheoKhoangNgay(java.util.Date bd, java.util.Date kt) {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT hd.maHoaDon, hd.ngayLap, nv.hoTen AS tenNhanVien, kh.hoTen AS tenKhachHang, hd.tongTien
            FROM HoaDon hd
            JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
            JOIN HanhKhach kh ON hd.maHanhKhach = kh.maHanhKhach
            WHERE CAST(hd.ngayLap AS DATE) BETWEEN ? AND ?
            ORDER BY hd.ngayLap ASC
        """;
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(bd.getTime()));
            ps.setDate(2, new java.sql.Date(kt.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("maHoaDon"),
                    rs.getDate("ngayLap"),
                    rs.getString("tenNhanVien"),
                    rs.getString("tenKhachHang"),
                    rs.getDouble("tongTien")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getHoaDonTheoNam(int nam) {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT hd.maHoaDon, hd.ngayLap, nv.hoTen AS tenNhanVien, kh.hoTen AS tenKhachHang, hd.tongTien
            FROM HoaDon hd
            JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
            JOIN HanhKhach kh ON hd.maHanhKhach = kh.maHanhKhach
            WHERE YEAR(hd.ngayLap) = ?
            ORDER BY hd.ngayLap ASC
        """;
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("maHoaDon"),
                    rs.getDate("ngayLap"),
                    rs.getString("tenNhanVien"),
                    rs.getString("tenKhachHang"),
                    rs.getDouble("tongTien")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getTatCaHoaDon() {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT hd.maHoaDon, hd.ngayLap, nv.hoTen AS tenNhanVien, kh.hoTen AS tenKhachHang, hd.tongTien
            FROM HoaDon hd
            JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
            JOIN HanhKhach kh ON hd.maHanhKhach = kh.maHanhKhach
            ORDER BY hd.ngayLap ASC
        """;
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("maHoaDon"),
                    rs.getDate("ngayLap"),
                    rs.getString("tenNhanVien"),
                    rs.getString("tenKhachHang"),
                    rs.getDouble("tongTien")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public int tongVeDaBan() {
    	return demVeTheoTrangThai("Đã đặt");
    	}
    public int tongVeDaHuy() { 
    	return demVeTheoTrangThai("Đã hủy");
    	}
    public int tongVeDaHoan() {
    	return demVeTheoTrangThai("Đã hoàn"); 
    	}
}
