package dao;

import java.sql.*;
import connectDB.connectDB;
import entity.HanhKhach;
import entity.NhanVien;
import entity.PhieuDatCho;

public class PhieuDatChoDAO {


    //  Thêm phiếu đặt chỗ mới
    public boolean insertPhieuDatCho(PhieuDatCho pdc) {
        Connection conn = connectDB.getConnection();
        String sql = "INSERT INTO PhieuDatCho (ngayDat, trangThai, maHanhKhach, maNhanVien) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(pdc.getNgayDat()));
            ps.setString(2, pdc.getTrangThai());
            ps.setString(3, pdc.getMaHanhKhach().getMaKH());
            ps.setString(4, pdc.getMaNhanVien().getMaNhanVien());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm phiếu đặt chỗ: " + e.getMessage());
            return false;
        }
    }

    // Cập nhật thông tin phiếu đặt chỗ
    public boolean updatePhieuDatCho(PhieuDatCho pdc) {
        Connection conn = connectDB.getConnection();
        String sql = "UPDATE PhieuDatCho SET ngayDat = ?, trangThai = ?, maHanhKhach = ?, maNhanVien = ? WHERE maPhieuDatCho = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(pdc.getNgayDat()));
            ps.setString(2, pdc.getTrangThai());
            ps.setString(3, pdc.getMaHanhKhach().getMaKH());
            ps.setString(4, pdc.getMaNhanVien().getMaNhanVien());
            ps.setString(5, pdc.getMaPhieuDatCho());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật phiếu đặt chỗ: " + e.getMessage());
            return false;
        }
    }

    // Xóa phiếu đặt chỗ theo mã
    public boolean deletePhieuDatCho(String maPhieuDatCho) {
        Connection conn = connectDB.getConnection();
        String sql = "DELETE FROM PhieuDatCho WHERE maPhieuDatCho = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuDatCho);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa phiếu đặt chỗ: " + e.getMessage());
            return false;
        }
    }

    // Tìm phiếu đặt chỗ theo mã
    public PhieuDatCho getPhieuDatChoByMa(String maPhieuDatCho) {
        Connection conn = connectDB.getConnection();
        String sql = "SELECT * FROM PhieuDatCho WHERE maPhieuDatCho = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieuDatCho);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new PhieuDatCho(
                        rs.getString("maPhieuDatCho"),
                        rs.getTimestamp("ngayDat").toLocalDateTime(),
                        rs.getString("trangThai"),
                        new HanhKhach(rs.getString("maHanhKhach")),
                        new NhanVien(rs.getString("maNhanVien"))
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm phiếu đặt chỗ: " + e.getMessage());
        }
        return null;
    }

    
}
