package control;

import connectDB.connectDB;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuanLyLoaiGheControl {

    // Tải dữ liệu (bao gồm cả đã xóa mềm)
    public void loadData(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT IDloaiGhe, tenLoai, trangThai FROM LoaiGhe ORDER BY IDloaiGhe";
        try (Connection con = connectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean coDuLieu = false;
            while (rs.next()) {
                coDuLieu = true;
                int id = rs.getInt("IDloaiGhe");
                String ten = rs.getString("tenLoai");
                String trangThai = rs.getString("trangThai");
                model.addRow(new Object[]{id, ten, trangThai});
            }

            if (!coDuLieu) {
                model.addRow(new Object[]{"", "Chưa có loại ghế nào.", ""});
            }

        } catch (SQLException e) {
            model.setRowCount(0);
            model.addRow(new Object[]{"", "Lỗi tải dữ liệu: " + e.getMessage(), ""});
            e.printStackTrace();
        }
    }

    // Thêm loại ghế
    public boolean themLoaiGhe(String tenLoai) {
        if (kiemTraTrungTen(tenLoai)) {
            JOptionPane.showMessageDialog(null, "Tên loại ghế đã tồn tại!", "Lỗi trùng lặp", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String sql = "INSERT INTO LoaiGhe (tenLoai, trangThai) VALUES (?, N'Hoạt động')";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenLoai);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm loại ghế: " + e.getMessage());
            return false;
        }
    }

    // Sửa loại ghế
    public boolean suaLoaiGhe(int id, String tenLoai) {
        if (kiemTraTrungTen(tenLoai, id)) {
            JOptionPane.showMessageDialog(null, "Tên loại ghế đã tồn tại!", "Lỗi trùng lặp", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String sql = "UPDATE LoaiGhe SET tenLoai = ? WHERE IDloaiGhe = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenLoai);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật loại ghế: " + e.getMessage());
            return false;
        }
    }

    // XÓA MỀM: Đánh dấu Ngừng hoạt động
    public boolean xoaMemLoaiGhe(int id) {
        String sql = "UPDATE LoaiGhe SET trangThai = N'Ngừng hoạt động' WHERE IDloaiGhe = ? AND trangThai = N'Hoạt động'";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa loại ghế: " + e.getMessage());
            return false;
        }
    }

    // KHÔI PHỤC: Đưa về Hoạt động
    public boolean khoiPhucLoaiGhe(int id) {
        String sql = "UPDATE LoaiGhe SET trangThai = N'Hoạt động' WHERE IDloaiGhe = ? AND trangThai = N'Ngừng hoạt động'";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi khôi phục loại ghế: " + e.getMessage());
            return false;
        }
    }

    // Lấy mã tiếp theo
    public int layMaLoaiGheTiepTheo() {
        String sql = "SELECT COALESCE(MAX(IDloaiGhe), 0) + 1 AS IDMoi FROM LoaiGhe";
        try (Connection con = connectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("IDMoi");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi lấy ID tiếp theo: " + e.getMessage());
            e.printStackTrace();
        }
        return 1;
    }

    // Kiểm tra trùng tên
    public boolean kiemTraTrungTen(String tenLoai) {
        return kiemTraTrungTen(tenLoai, -1);
    }

    public boolean kiemTraTrungTen(String tenLoai, int idLoaiTru) {
        String sql = "SELECT COUNT(*) FROM LoaiGhe WHERE tenLoai = ? AND IDloaiGhe != ?";
        if (idLoaiTru == -1) {
            sql = "SELECT COUNT(*) FROM LoaiGhe WHERE tenLoai = ?";
        }

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenLoai);
            if (idLoaiTru != -1) ps.setInt(2, idLoaiTru);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi kiểm tra trùng tên: " + e.getMessage());
        }
        return false;
    }

    // Tìm kiếm
    public List<Object[]> timLoaiGhe(String ten) {
        List<Object[]> ketQua = new ArrayList<>();
        String sql = "SELECT IDloaiGhe, tenLoai, trangThai FROM LoaiGhe WHERE tenLoai LIKE ? ORDER BY IDloaiGhe";

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + ten + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ketQua.add(new Object[]{
                        rs.getInt("IDloaiGhe"),
                        rs.getString("tenLoai"),
                        rs.getString("trangThai")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return ketQua;
    }
}