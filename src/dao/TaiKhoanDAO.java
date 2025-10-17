package dao;

import connectDB.connectDB;
import entity.NhanVien;
import entity.TaiKhoan;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    // Lấy tất cả tài khoản
    public List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> dsTK = new ArrayList<>();
        String sql = "SELECT tk.tenDangNhap, tk.matKhau, nv.* " +
                     "FROM TaiKhoan tk JOIN NhanVien nv ON tk.tenDangNhap = nv.soDienThoai";

        try (Connection con = connectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("hoTen"),
                        rs.getDate("ngaySinh").toLocalDate(),
                        rs.getString("soDienThoai"),
                        rs.getString("cmndCccd"),
                        rs.getInt("IDloaiChucVu")
                );

                TaiKhoan tk = new TaiKhoan(
                        rs.getString("matKhau"),
                        nv
                );

                dsTK.add(tk);
            }
        } catch (SQLException e) {
            System.err.println(" Lỗi lấy danh sách tài khoản: " + e.getMessage());
        }
        return dsTK;
    }

    // Thêm tài khoản
    public boolean insertTaiKhoan(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan (tenDangNhap, matKhau, maNhanVien) VALUES (?, ?, ?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, tk.getTenDangNhap());
            stmt.setString(2, tk.getMatKhau());
            stmt.setString(3, tk.getNhanVien().getMaNhanVien());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Lỗi thêm tài khoản: " + e.getMessage());
        }
        return false;
    }

    // Cập nhật mật khẩu
    public boolean updateMatKhau(String tenDangNhap, String matKhauMoi) {
        String sql = "UPDATE TaiKhoan SET matKhau=? WHERE tenDangNhap=?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, matKhauMoi);
            stmt.setString(2, tenDangNhap);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật mật khẩu: " + e.getMessage());
        }
        return false;
    }

    // Xóa tài khoản
    public boolean deleteTaiKhoan(String tenDangNhap) {
        String sql = "DELETE FROM TaiKhoan WHERE tenDangNhap=?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, tenDangNhap);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi xóa tài khoản: " + e.getMessage());
        }
        return false;
    }

    // Tìm tài khoản theo tên đăng nhập
    public TaiKhoan findByTenDangNhap(String tenDangNhap) {
        String sql = "SELECT tk.tenDangNhap, tk.matKhau, nv.* " +
                     "FROM TaiKhoan tk JOIN NhanVien nv ON tk.tenDangNhap = nv.soDienThoai " +
                     "WHERE tk.tenDangNhap = ?";

        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, tenDangNhap);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("hoTen"),
                        rs.getDate("ngaySinh").toLocalDate(),
                        rs.getString("soDienThoai"),
                        rs.getString("cmndCccd"),
                        rs.getInt("IDloaiChucVu")
                );

                return new TaiKhoan(
                        rs.getString("matKhau"),
                        nv
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm tài khoản: " + e.getMessage());
        }
        return null;
    }

    // Xác thực đăng nhập
    public TaiKhoan authenticate(String tenDangNhap, String matKhau) {
        String sql = "SELECT tk.tenDangNhap, tk.matKhau, nv.* " +
                     "FROM TaiKhoan tk JOIN NhanVien nv ON tk.tenDangNhap = nv.soDienThoai " +
                     "WHERE tk.tenDangNhap = ? AND tk.matKhau = ?";

        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, tenDangNhap);
            stmt.setString(2, matKhau);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("hoTen"),
                        rs.getDate("ngaySinh").toLocalDate(),
                        rs.getString("soDienThoai"),
                        rs.getString("cmndCccd"),
                        rs.getInt("IDloaiChucVu")
                );

                return new TaiKhoan(
                        rs.getString("matKhau"),
                        nv
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi xác thực đăng nhập: " + e.getMessage());
        }
        return null;
    }
}
