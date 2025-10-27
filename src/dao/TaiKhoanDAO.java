package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.TaiKhoan;
import entity.NhanVien;
import connectDB.connectDB;

public class TaiKhoanDAO {

    // Phương thức lấy tất cả tài khoản
    public List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> listTaiKhoan = new ArrayList<>();
        String sql = "SELECT tk.tenDangNhap, tk.matKhau, tk.maNhanVien FROM TaiKhoan tk ORDER BY tk.tenDangNhap";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                TaiKhoan tk = taoTaiKhoanTuResultSet(rs);
                listTaiKhoan.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listTaiKhoan;
    }

    // Phương thức lấy tài khoản theo tên đăng nhập
    public TaiKhoan getTaiKhoanByTenDangNhap(String tenDangNhap) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
            return null;
        }
        String sql = "SELECT tk.tenDangNhap, tk.matKhau, tk.maNhanVien FROM TaiKhoan tk WHERE tk.tenDangNhap = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenDangNhap.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return taoTaiKhoanTuResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức thêm tài khoản mới
    public boolean addTaiKhoan(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan (tenDangNhap, matKhau, maNhanVien) VALUES (?, ?, ?)";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tk.getTenDangNhap().trim());
            pstmt.setString(2, tk.getMatKhau());
            pstmt.setString(3, tk.getNhanVien().getMaNhanVien());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức cập nhật tài khoản (chỉ mật khẩu, vì tenDangNhap là key)
    public boolean updateTaiKhoan(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE tenDangNhap = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tk.getMatKhau());
            pstmt.setString(2, tk.getTenDangNhap().trim());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức xóa tài khoản theo tên đăng nhập
    public boolean deleteTaiKhoan(String tenDangNhap) {
        String sql = "DELETE FROM TaiKhoan WHERE tenDangNhap = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenDangNhap.trim());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper: Tạo TaiKhoan từ ResultSet (load NhanVien đầy đủ với IDloaiChucVu)
    private TaiKhoan taoTaiKhoanTuResultSet(ResultSet rs) throws SQLException {
        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap(rs.getString("tenDangNhap"));
        tk.setMatKhau(rs.getString("matKhau"));
        String maNV = rs.getString("maNhanVien");
        NhanVien nv = new NhanVienDAO().getNhanVienByMa(maNV);  // Load NV đầy đủ
        if (nv != null) {
            tk.setNhanVien(nv);  // Entity sẽ validate và set tenDangNhap nếu cần
        } else {
            throw new SQLException("Không load được nhân viên cho tài khoản: " + maNV);
        }
        return tk;
    }
}