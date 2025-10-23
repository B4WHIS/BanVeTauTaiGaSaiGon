package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.NhanVien;

public class NhanVienDAO {
	private NhanVienDAO nhanVienDAO = new NhanVienDAO();

    // Lấy toàn bộ danh sách nhân viên
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> dsNV = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
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
                dsNV.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsNV;
    }

    // Thêm nhân viên mới
    public boolean insertNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (maNhanVien, hoTen, ngaySinh, soDienThoai, cmndCccd, IDloaiChucVu) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, nv.getMaNhanVien());
            stmt.setString(2, nv.getHoTen());
            stmt.setDate(3, Date.valueOf(nv.getNgaySinh()));
            stmt.setString(4, nv.getSoDienThoai());
            stmt.setString(5, nv.getCmndCccd());
            stmt.setInt(6, nv.getIDloaiChucVu());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi thêm nhân viên: " + e.getMessage());
        }
        return false;
    }

    // Cập nhật thông tin nhân viên
    public boolean updateNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET hoTen=?, ngaySinh=?, soDienThoai=?, cmndCccd=?, IDloaiChucVu=? WHERE maNhanVien=?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, nv.getHoTen());
            stmt.setDate(2, Date.valueOf(nv.getNgaySinh()));
            stmt.setString(3, nv.getSoDienThoai());
            stmt.setString(4, nv.getCmndCccd());
            stmt.setInt(5, nv.getIDloaiChucVu());
            stmt.setString(6, nv.getMaNhanVien());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật nhân viên: " + e.getMessage());
        }
        return false;
    }

    // Xóa nhân viên theo mã
    public boolean deleteNhanVien(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE maNhanVien=?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maNV);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Lỗi xóa nhân viên: " + e.getMessage());
        }
        return false;
    }

    // Tìm nhân viên theo CMND/CCCD
    public NhanVien findByCMND(String cmndCccd) {
        String sql = "SELECT * FROM NhanVien WHERE cmndCccd = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, cmndCccd);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new NhanVien(
                    rs.getString("maNhanVien"),
                    rs.getString("hoTen"),
                    rs.getDate("ngaySinh").toLocalDate(),
                    rs.getString("soDienThoai"),
                    rs.getString("cmndCccd"),
                    rs.getInt("IDloaiChucVu")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm nhân viên theo CMND/CCCD: " + e.getMessage());
        }
        return null;
    }
    public List<Object[]> getDanhSachLoaiChucVu() {
        List<Object[]> ds = new ArrayList<>();
        String sql = """
            SELECT DISTINCT lcv.IDloaiChucVu, lcv.tenLoaiCV
            FROM NhanVien nv
            JOIN LoaiChucVu lcv ON nv.IDloaiChucVu = lcv.IDloaiChucVu
        """;
        try (Connection con = connectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ds.add(new Object[] {
                    rs.getInt("IDloaiChucVu"),
                    rs.getString("tenLoaiCV")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}
