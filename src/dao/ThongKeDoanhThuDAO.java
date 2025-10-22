package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.ThongKeDoanhThu;

public class ThongKeDoanhThuDAO {

//     Thêm bản ghi thống kê doanh thu mới
   
    public boolean insertThongKe(ThongKeDoanhThu tk) {
        String sql = """
                INSERT INTO ThongKeDoanhThu 
                (maThongKe, ngayThongKe, maNhanVien, tongDoanhThu, tongSoVe, tongSoHoanDoi)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, tk.getMaThongKe());
            stmt.setDate(2, Date.valueOf(tk.getNgayThongKe()));
            stmt.setString(3, tk.getMaNhanVien());
            stmt.setBigDecimal(4, tk.getTongDoanhThu());
            stmt.setInt(5, tk.getTongSoVe());
            stmt.setInt(6, tk.getTongSoHoanDoi() == null ? 0 : tk.getTongSoHoanDoi());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi thêm thống kê doanh thu: " + e.getMessage());
        }
        return false;
    }

//    Cập nhật thông tin thống kê theo mã thống kê
 
    public boolean updateThongKe(ThongKeDoanhThu tk) {
        String sql = """
                UPDATE ThongKeDoanhThu
                SET ngayThongKe = ?, maNhanVien = ?, tongDoanhThu = ?, tongSoVe = ?, tongSoHoanDoi = ?
                WHERE maThongKe = ?
                """;
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(tk.getNgayThongKe()));
            stmt.setString(2, tk.getMaNhanVien());
            stmt.setBigDecimal(3, tk.getTongDoanhThu());
            stmt.setInt(4, tk.getTongSoVe());
            stmt.setInt(5, tk.getTongSoHoanDoi());
            stmt.setString(6, tk.getMaThongKe());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật thống kê doanh thu: " + e.getMessage());
        }
        return false;
    }

//     Lấy toàn bộ danh sách thống kê
    
    public List<ThongKeDoanhThu> getAllThongKe() {
        List<ThongKeDoanhThu> dsTK = new ArrayList<>();
        String sql = "SELECT * FROM ThongKeDoanhThu ORDER BY ngayThongKe DESC";

        try (Connection con = connectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ThongKeDoanhThu tk = new ThongKeDoanhThu();
                tk.setMaThongKe(rs.getString("maThongKe"));
                tk.setNgayThongKe(rs.getDate("ngayThongKe").toLocalDate());
                tk.setMaNhanVien(rs.getString("maNhanVien"));
                tk.setTongDoanhThu(rs.getBigDecimal("tongDoanhThu"));
                tk.setTongSoVe(rs.getInt("tongSoVe"));
                tk.setTongSoHoanDoi(rs.getInt("tongSoHoanDoi"));
                dsTK.add(tk);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách thống kê doanh thu: " + e.getMessage());
        }
        return dsTK;
    }

//     Tìm thống kê theo mã thống kê
   
    public ThongKeDoanhThu findByMaThongKe(String maThongKe) {
        String sql = "SELECT * FROM ThongKeDoanhThu WHERE maThongKe = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maThongKe);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ThongKeDoanhThu tk = new ThongKeDoanhThu();
                tk.setMaThongKe(rs.getString("maThongKe"));
                tk.setNgayThongKe(rs.getDate("ngayThongKe").toLocalDate());
                tk.setMaNhanVien(rs.getString("maNhanVien"));
                tk.setTongDoanhThu(rs.getBigDecimal("tongDoanhThu"));
                tk.setTongSoVe(rs.getInt("tongSoVe"));
                tk.setTongSoHoanDoi(rs.getInt("tongSoHoanDoi"));
                return tk;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi tìm thống kê theo mã: " + e.getMessage());
        }
        return null;
    }

//    Lấy thống kê doanh thu theo ngày cụ thể
    
    public List<ThongKeDoanhThu> getThongKeTheoNgay(LocalDate ngayThongKe) {
        List<ThongKeDoanhThu> dsTK = new ArrayList<>();
        String sql = "SELECT * FROM ThongKeDoanhThu WHERE ngayThongKe = ?";

        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(ngayThongKe));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ThongKeDoanhThu tk = new ThongKeDoanhThu();
                tk.setMaThongKe(rs.getString("maThongKe"));
                tk.setNgayThongKe(rs.getDate("ngayThongKe").toLocalDate());
                tk.setMaNhanVien(rs.getString("maNhanVien"));
                tk.setTongDoanhThu(rs.getBigDecimal("tongDoanhThu"));
                tk.setTongSoVe(rs.getInt("tongSoVe"));
                tk.setTongSoHoanDoi(rs.getInt("tongSoHoanDoi"));
                dsTK.add(tk);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi lấy thống kê theo ngày: " + e.getMessage());
        }
        return dsTK;
    }

//    Lấy tổng doanh thu trong khoảng thời gian
    public BigDecimal getTongDoanhThuTheoKhoang(LocalDate tuNgay, LocalDate denNgay) {
        String sql = "SELECT SUM(tongDoanhThu) AS tong FROM ThongKeDoanhThu WHERE ngayThongKe BETWEEN ? AND ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(tuNgay));
            stmt.setDate(2, Date.valueOf(denNgay));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("tong") != null ? rs.getBigDecimal("tong") : BigDecimal.ZERO;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi tính tổng doanh thu trong khoảng: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    public ThongKeDoanhThu getThongKeTheoNgayVaNV(LocalDate ngayThongKe, String maNhanVien) {
        String sql = "SELECT * FROM ThongKeDoanhThu WHERE ngayThongKe = ? AND maNhanVien = ?";
        
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
             
            stmt.setDate(1, Date.valueOf(ngayThongKe));
            stmt.setString(2, maNhanVien);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                ThongKeDoanhThu tk = new ThongKeDoanhThu();
                tk.setMaThongKe(rs.getString("maThongKe"));
                tk.setNgayThongKe(rs.getDate("ngayThongKe").toLocalDate());
                tk.setMaNhanVien(rs.getString("maNhanVien"));
                tk.setTongDoanhThu(rs.getBigDecimal("tongDoanhThu"));
                tk.setTongSoVe(rs.getInt("tongSoVe"));
                tk.setTongSoHoanDoi(rs.getInt("tongSoHoanDoi"));
                return tk;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn thống kê theo ngày và nhân viên: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
