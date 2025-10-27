package dao;
//check
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.NhanVien;

public class NhanVienDAO {
	

    // Phương thức lấy tất cả tên nhân viên (cho ComboBox) - Chỉ hoTen
    public List<String> getAllTenNhanVien() {
        List<String> listTenNV = new ArrayList<>();
        String sql = "SELECT hoTen FROM NhanVien ORDER BY hoTen";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                listTenNV.add(rs.getString("hoTen"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listTenNV;
    }

    // Phương thức lấy nhân viên theo tên (hoTen)
    public NhanVien getNhanVienByTen(String hoTen) {
        String sql = "SELECT * FROM NhanVien WHERE hoTen = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hoTen);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return taoNhanVienTuResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method mới: Lấy tất cả nhân viên chưa có tài khoản
    public List<NhanVien> getAllNhanVienWithoutTaiKhoan() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien nv WHERE NOT EXISTS (SELECT 1 FROM TaiKhoan tk WHERE tk.maNhanVien = nv.maNhanVien) ORDER BY hoTen";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(taoNhanVienTuResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Phương thức lấy danh sách loại chức vụ
    public List<Object[]> getDanhSachLoaiChucVu() {
        // Danh sách chứa kết quả trả về
        List<Object[]> dsLoaiChucVu = new ArrayList<>();
        
        // Comment: Truy vấn lấy tất cả Loại Chức Vụ đang được gán cho Nhân Viên
        String sql = """
            SELECT DISTINCT lcv.IDloaiCV, lcv.tenLoai
            FROM NhanVien nv
            JOIN LoaiChucVu lcv ON nv.IDloaiChucVu = lcv.IDloaiCV 
        """;
        
        // Sử dụng try-with-resources để tự động đóng Connection và Statement
        try (Connection ketNoi = connectDB.getConnection();
             Statement lenhSQL = ketNoi.createStatement();
             ResultSet ketQua = lenhSQL.executeQuery(sql)) {

            // Lặp qua các dòng kết quả
            while (ketQua.next()) {
                dsLoaiChucVu.add(new Object[] {
                    // Sửa tên cột để khớp với schema thực tế (IDloaiCV, tenLoai) [5]
                    ketQua.getInt("IDloaiCV"),
                    ketQua.getString("tenLoai")
                });
            }
        
        // Xử lý lỗi SQL nếu có
        } catch (SQLException e) {
            // Comment: Bắt lỗi khi truy vấn CSDL
            System.err.println("Lỗi truy vấn danh sách Loại Chức Vụ: " + e.getMessage());
            e.printStackTrace(); 
        }
        
        // Trả về danh sách kết quả (có thể là danh sách rỗng nếu xảy ra lỗi)
        return dsLoaiChucVu;
    }

    

    // Phương thức lấy tên nhân viên theo mã
    public String getTenNhanVienByMa(String maNhanVien) {
        String sql = "SELECT hoTen FROM NhanVien WHERE maNhanVien = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNhanVien);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("hoTen");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

   

    // Phương thức lấy nhân viên theo mã
    public NhanVien getNhanVienByMa(String maNhanVien) {
        String sql = "SELECT * FROM NhanVien WHERE maNhanVien = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maNhanVien);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return taoNhanVienTuResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // === LẤY TẤT CẢ NHÂN VIÊN ===
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien ORDER BY hoTen";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = taoNhanVienTuResultSet(rs);
                list.add(nv);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách nhân viên: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Helper: Tạo NhanVien từ ResultSet - Cập nhật để load IDloaiChucVu
    private NhanVien taoNhanVienTuResultSet(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();
        nv.setMaNhanVien(rs.getString("maNhanVien"));
        nv.setHoTen(rs.getString("hoTen"));
        nv.setNgaySinh(rs.getDate("ngaySinh").toLocalDate());
        nv.setSoDienThoai(rs.getString("soDienThoai"));
        if (rs.getString("cmndCccd") != null) {
            nv.setCmndCccd(rs.getString("cmndCccd"));
        }
        nv.setIDloaiChucVu(rs.getInt("IDloaiChucVu"));  // Load IDloaiChucVu
        return nv;
    }
}