package control;

import connectDB.connectDB;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.JOptionPane;

public class QuanLyLoaiChucVuControl {

    // ==== LOAD DỮ LIỆU ====
    public void loadData(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT IDloaiCV, tenLoai FROM LoaiChucVu ORDER BY IDloaiCV";
        try (Connection con = connectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("IDloaiCV");
                String ten = rs.getString("tenLoai");
                model.addRow(new Object[]{id, ten});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Lỗi khi tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==== THÊM DỮ LIỆU (TỰ ĐỘNG TẠO ID) ====
    public boolean themLoaiChucVu(String tenLoai) {
        // ✅ KIỂM TRA TRÙNG TÊN TRƯỚC KHI THÊM
        if (kiemTraTrungTen(tenLoai)) {
            JOptionPane.showMessageDialog(null, "❌ Tên loại chức vụ đã tồn tại!", "Lỗi trùng lặp", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String sql = "INSERT INTO LoaiChucVu (tenLoai) VALUES (?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenLoai);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Lỗi khi thêm loại chức vụ: " + e.getMessage());
            return false;
        }
    }

    // ==== CẬP NHẬT DỮ LIỆU ====
    public boolean suaLoaiChucVu(int id, String tenLoai) {
        // ✅ KIỂM TRA TRÙNG TÊN (TRỪ BẢN GHI ĐANG SỬA)
        if (kiemTraTrungTen(tenLoai, id)) {
            JOptionPane.showMessageDialog(null, "❌ Tên loại chức vụ đã tồn tại!", "Lỗi trùng lặp", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String sql = "UPDATE LoaiChucVu SET tenLoai = ? WHERE IDloaiCV = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenLoai);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Lỗi khi cập nhật loại chức vụ: " + e.getMessage());
            return false;
        }
    }

    // ==== XÓA DỮ LIỆU ====
    public boolean xoaLoaiChucVu(int id) {
        String sql = "DELETE FROM LoaiChucVu WHERE IDloaiCV = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Lỗi khi xóa loại chức vụ: " + e.getMessage());
            return false;
        }
    }

    // ==== ✅ METHOD MỚI: LẤY ID TIẾP THEO (TỰ ĐỘNG +1) ====
    public int layMaLoaiCVTiepTheo() {
        String sql = "SELECT COALESCE(MAX(IDloaiCV), 0) + 1 AS IDMoi FROM LoaiChucVu";
        try (Connection con = connectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("IDMoi");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Lỗi khi lấy ID tiếp theo: " + e.getMessage());
            e.printStackTrace();
        }
        return 1; // Nếu bảng rỗng, bắt đầu từ 1
    }

    // ==== KIỂM TRA TÊN TRÙNG (CẢI TIẾN) ====
    public boolean kiemTraTrungTen(String tenLoai) {
        return kiemTraTrungTen(tenLoai, -1); // -1 = không loại trừ ID nào
    }

    // ✅ OVERLOAD: KIỂM TRA TRÙNG TÊN (LOẠI TRỪ ID ĐANG SỬA)
    public boolean kiemTraTrungTen(String tenLoai, int idLoaiTru) {
        String sql = "SELECT COUNT(*) FROM LoaiChucVu WHERE tenLoai = ? AND IDloaiCV != ?";
        if (idLoaiTru == -1) {
            sql = "SELECT COUNT(*) FROM LoaiChucVu WHERE tenLoai = ?";
        }
        
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenLoai);
            if (idLoaiTru != -1) {
                ps.setInt(2, idLoaiTru);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Lỗi khi kiểm tra trùng tên: " + e.getMessage());
        }
        return false;
    }
}