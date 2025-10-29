package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.ChuyenTau;

public class ChuyenTauDAO {

    // Các trạng thái hợp lệ theo CHECK constraint trong DB
    private static final String[] TRANG_THAI_HOP_LE = {
        "Chưa khởi hành", "Đang khởi hành", "Đã hoàn thành", "Đã hủy"
    };

    // Phương thức lấy tất cả chuyến tàu
    public List<ChuyenTau> getAllChuyenTau() {
        List<ChuyenTau> listChuyenTau = new ArrayList<>();
        String sql = "SELECT maChuyenTau, thoiGianKhoiHanh, thoiGianDen, maTau, maLichTrinh, trangThai, giaChuyen FROM ChuyenTau ORDER BY thoiGianKhoiHanh";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ChuyenTau ct = taoChuyenTauTuResultSet(rs);
                listChuyenTau.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listChuyenTau;
    }
    // Phương thức lấy chuyến tàu theo mã chuyến tàu
    public ChuyenTau getChuyenTauByMaChuyenTau(String maChuyenTau) {
        String sql = "SELECT maChuyenTau, thoiGianKhoiHanh, thoiGianDen, maTau, maLichTrinh, trangThai, giaChuyen FROM ChuyenTau WHERE maChuyenTau = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maChuyenTau);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return taoChuyenTauTuResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ChuyenTau> getChuyenTauTheoNgayVaGa(LocalDate ngayKhoiHanh, String maGaDi, String maGaDen) {
        List<ChuyenTau> listChuyenTau = new ArrayList<>();
        
        // Đảm bảo SELECT tất cả các cột cần thiết cho taoChuyenTauTuResultSet
        String sql = "SELECT ct.maChuyenTau, ct.thoiGianKhoiHanh, ct.thoiGianDen, ct.maTau, "
                     + "ct.maLichTrinh, ct.trangThai, ct.giaChuyen " // [5]
                     + "FROM ChuyenTau ct "
                     + "JOIN LichTrinh lt ON ct.maLichTrinh = lt.maLichTrinh "
                     // Lọc theo khoảng thời gian 24h
                     + "WHERE ct.thoiGianKhoiHanh >= ? AND ct.thoiGianKhoiHanh < ? " 
                     + "AND lt.gaDi = ? AND lt.gaDen = ? "
                     + "AND ct.trangThai = N'Chưa khởi hành' " // Lọc trạng thái [8]
                     + "ORDER BY ct.thoiGianKhoiHanh";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Tham số 1: Thời gian bắt đầu ngày đó (00:00:00)
            pstmt.setTimestamp(1, java.sql.Timestamp.valueOf(ngayKhoiHanh.atStartOfDay())); 
            // Tham số 2: Thời gian bắt đầu ngày hôm sau (24:00:00)
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(ngayKhoiHanh.plusDays(1).atStartOfDay())); 
            pstmt.setString(3, maGaDi); 
            pstmt.setString(4, maGaDen); 

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // SỬ DỤNG HELPER để ánh xạ TẤT CẢ các trường (kể cả thoiGianDen)
                    ChuyenTau ct = taoChuyenTauTuResultSet(rs); 
                    listChuyenTau.add(ct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return listChuyenTau;
    }


    
    
    
    // Phương thức thêm chuyến tàu mới (maChuyenTau tự động tạo bởi DB)
    public boolean addChuyenTau(ChuyenTau ct) {
        // Kiểm tra FK: maTau tồn tại
        if (!tonTaiTau(ct.getMaTau())) {
            throw new IllegalArgumentException("Mã tàu không tồn tại!");
        }
    
        kiemTraTrangThaiHopLe(ct.getTrangThai());
        // Kiểm tra trùng chuyến (cùng lịch trình và ngày KH)
      
        String sql = "INSERT INTO ChuyenTau (thoiGianKhoiHanh, thoiGianDen, maTau, maLichTrinh, trangThai, giaChuyen) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(ct.getThoiGianKhoiHanh()));
            pstmt.setTimestamp(2, Timestamp.valueOf(ct.getThoiGianDen()));
            pstmt.setString(3, ct.getMaTau());
            pstmt.setString(4, ct.getMaLichTrinh());
            pstmt.setString(5, ct.getTrangThai());
            pstmt.setBigDecimal(6, ct.getGiaChuyen());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Lấy ID để sinh maChuyenTau
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        String generatedMa = "C-" + String.format("%03d", id);
                        ct.setMaChuyenTau(generatedMa);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức cập nhật chuyến tàu (không thay đổi maTau, maLichTrinh)
    public boolean updateChuyenTau(ChuyenTau ct) {
        ChuyenTau ctCu = getChuyenTauByMaChuyenTau(ct.getMaChuyenTau());
        if (ctCu == null) {
            throw new IllegalArgumentException("Chuyến tàu không tồn tại!");
        }
        // Kiểm tra trạng thái hợp lệ
        kiemTraTrangThaiHopLe(ct.getTrangThai());
        // Kiểm tra trùng chuyến (cùng lịch trình và ngày KH, trừ chính nó)
        if (isTrungChuyen(ctCu.getMaLichTrinh(), ct.getThoiGianKhoiHanh().toLocalDate(), ct.getMaChuyenTau())) {
            throw new IllegalArgumentException("Thời gian cập nhật trùng với chuyến khác!");
        }
        String sql = "UPDATE ChuyenTau SET thoiGianKhoiHanh = ?, thoiGianDen = ?, trangThai = ?, giaChuyen = ? WHERE maChuyenTau = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(ct.getThoiGianKhoiHanh()));
            pstmt.setTimestamp(2, Timestamp.valueOf(ct.getThoiGianDen()));
            pstmt.setString(3, ct.getTrangThai());
            pstmt.setBigDecimal(4, ct.getGiaChuyen());
            pstmt.setString(5, ct.getMaChuyenTau());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức xóa chuyến tàu theo mã chuyến tàu
    public boolean deleteChuyenTau(String maChuyenTau) {
        // Kiểm tra có vé liên quan
        if (coVeLienQuan(maChuyenTau)) {
            throw new IllegalArgumentException("Không thể xóa chuyến có vé đã bán!");
        }
       
    
        String sql = "DELETE FROM ChuyenTau WHERE maChuyenTau = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maChuyenTau);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức lấy tất cả tên tàu (cho ComboBox hiển thị)
    public List<String> getAllTenTau() {
        List<String> listTenTau = new ArrayList<>();
        String sql = "SELECT tenTau FROM Tau ORDER BY tenTau";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                listTenTau.add(rs.getString("tenTau"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listTenTau;
    }

    // Phương thức lấy mã tàu theo tên tàu
    public String getMaTauByTenTau(String tenTau) {
        String sql = "SELECT maTau FROM Tau WHERE tenTau = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenTau);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("maTau");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức lấy tất cả tên lịch trình (cho ComboBox hiển thị)
    public List<String> getAllTenLichTrinh() {
        List<String> listTenLichTrinh = new ArrayList<>();
        String sql = "SELECT tenLichTrinh FROM LichTrinh ORDER BY tenLichTrinh";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                listTenLichTrinh.add(rs.getString("tenLichTrinh"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listTenLichTrinh;
    }

    // Phương thức lấy mã lịch trình theo tên lịch trình
    public String getMaLichTrinhByTenLichTrinh(String tenLichTrinh) {
        String sql = "SELECT maLichTrinh FROM LichTrinh WHERE tenLichTrinh = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenLichTrinh);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("maLichTrinh");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper: Tạo ChuyenTau từ ResultSet
    private ChuyenTau taoChuyenTauTuResultSet(ResultSet rs) throws SQLException {
        ChuyenTau ct = new ChuyenTau();
        ct.setMaChuyenTau(rs.getString("maChuyenTau"));
        ct.setThoiGianKhoiHanh(rs.getTimestamp("thoiGianKhoiHanh").toLocalDateTime());
        ct.setThoiGianDen(rs.getTimestamp("thoiGianDen").toLocalDateTime());
        ct.setMaTau(rs.getString("maTau"));
        ct.setMaLichTrinh(rs.getString("maLichTrinh"));
        ct.setTrangThai(rs.getString("trangThai"));
        BigDecimal gia = rs.getBigDecimal("giaChuyen");
        if (gia != null) {
            ct.setGiaChuyen(gia);
        }
        return ct;
    }

    // Helper: Kiểm tra trạng thái hợp lệ
    private void kiemTraTrangThaiHopLe(String trangThai) {
        if (trangThai == null) {
            throw new IllegalArgumentException("Trạng thái không được null!");
        }
        for (String tt : TRANG_THAI_HOP_LE) {
            if (tt.equals(trangThai)) {
                return;
            }
        }
        throw new IllegalArgumentException("Trạng thái không hợp lệ: " + trangThai);
    }

    // Helper: Kiểm tra tàu tồn tại
    private boolean tonTaiTau(String maTau) {
        String sql = "SELECT COUNT(*) FROM Tau WHERE maTau = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maTau);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper: Kiểm tra lịch trình tồn tại
    private boolean tonTaiLichTrinh(String maLichTrinh) {
        String sql = "SELECT COUNT(*) FROM LichTrinh WHERE maLichTrinh = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLichTrinh);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper: Kiểm tra trùng chuyến (cùng lịch trình và ngày KH)
    private boolean isTrungChuyen(String maLichTrinh, LocalDate ngayKhoiHanh) {
        String sql = "SELECT COUNT(*) FROM ChuyenTau ct " +
                     "WHERE ct.maLichTrinh = ? AND CAST(ct.thoiGianKhoiHanh AS DATE) = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLichTrinh);
            pstmt.setDate(2, java.sql.Date.valueOf(ngayKhoiHanh));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper: Kiểm tra trùng chuyến (trừ chính chuyến, cho update)
    private boolean isTrungChuyen(String maLichTrinh, LocalDate ngayKhoiHanh, String maChuyenTau) {
        String sql = "SELECT COUNT(*) FROM ChuyenTau ct " +
                     "WHERE ct.maLichTrinh = ? AND CAST(ct.thoiGianKhoiHanh AS DATE) = ? AND ct.maChuyenTau != ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLichTrinh);
            pstmt.setDate(2, java.sql.Date.valueOf(ngayKhoiHanh));
            pstmt.setString(3, maChuyenTau);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper: Kiểm tra có vé liên quan không
    private boolean coVeLienQuan(String maChuyenTau) {
        String sql = "SELECT COUNT(*) FROM Ve WHERE maChuyenTau = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maChuyenTau);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

  

    // Method tự động cập nhật trạng thái dựa trên thời gian thực (gọi định kỳ)
    public void updateTrangThaiTuThoiGian() {
        LocalDateTime now = LocalDateTime.now();
        String sql = "UPDATE ChuyenTau SET trangThai = CASE " +
                     "WHEN thoiGianKhoiHanh > ? THEN N'Chưa khởi hành' " +  // Dùng N'' cho nvarchar
                     "WHEN thoiGianKhoiHanh <= ? AND thoiGianDen > ? THEN N'Đang khởi hành' " +
                     "WHEN thoiGianDen <= ? THEN N'Đã hoàn thành' " +
                     "ELSE N'Chưa khởi hành' END " +  // FIX: ELSE set mặc định hợp lệ thay vì giữ nguyên
                     "WHERE trangThai != N'Đã hủy'";  // Dùng N'' cho nvarchar
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(now));
            pstmt.setTimestamp(2, Timestamp.valueOf(now));
            pstmt.setTimestamp(3, Timestamp.valueOf(now));
            pstmt.setTimestamp(4, Timestamp.valueOf(now));
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("DAO auto-update trạng thái: Cập nhật " + rowsAffected + " chuyến.");
        } catch (SQLException e) {
            System.out.println("Lỗi auto-update trạng thái: " + e.getMessage());
            e.printStackTrace();
            // Có thể thêm log chi tiết hoặc thông báo GUI nếu cần
        }
    }

    // Method update trạng thái cho 1 chuyến cụ thể (nếu cần)
    public boolean updateTrangThaiMotChuyen(String maChuyenTau) {
        ChuyenTau ct = getChuyenTauByMaChuyenTau(maChuyenTau);
        if (ct == null) return false;
        
        LocalDateTime now = LocalDateTime.now();
        String trangThaiMoi;
        if (now.isBefore(ct.getThoiGianKhoiHanh())) {
            trangThaiMoi = "Chưa khởi hành";
        } else if (now.isBefore(ct.getThoiGianDen())) {
            trangThaiMoi = "Đang khởi hành";
        } else {
            trangThaiMoi = "Đã hoàn thành";
        }
        
        if (trangThaiMoi.equals(ct.getTrangThai())) return true;  // Không thay đổi
        
        String sql = "UPDATE ChuyenTau SET trangThai = ? WHERE maChuyenTau = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trangThaiMoi);
            pstmt.setString(2, maChuyenTau);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("DAO update trạng thái chuyến " + maChuyenTau + ": " + trangThaiMoi + " (affected: " + rowsAffected + ")");
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi update trạng thái chuyến: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}