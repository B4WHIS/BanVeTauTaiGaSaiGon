package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import entity.ChuyenTau;
import connectDB.connectDB;

public class ChuyenTauDAO {

    // Phương thức lấy tất cả chuyến tàu
    public List<ChuyenTau> getAllChuyenTau() {
        List<ChuyenTau> listChuyenTau = new ArrayList<>();
        String sql = "SELECT maChuyenTau, thoiGianKhoiHanh, thoiGianDen, maTau, maLichTrinh, trangThai, giaChuyen FROM ChuyenTau";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức lấy chuyến tàu theo ngày và ga đi/đến (phương thức phức tạp) - Sửa: Tìm theo lịch trình (gaDi/gaDen) và ngày
    public List<ChuyenTau> getChuyenTauTheoNgayVaGa(LocalDateTime ngayKhoiHanh, String maGaDi, String maGaDen) {
        List<ChuyenTau> listChuyenTau = new ArrayList<>();
        String sql = "SELECT ct.maChuyenTau, ct.thoiGianKhoiHanh, ct.thoiGianDen, ct.maTau, ct.maLichTrinh,ct.trangThai, ct.giaChuyen " +
                     "FROM ChuyenTau ct " +
                     "JOIN LichTrinh lt ON ct.maLichTrinh = lt.maLichTrinh " +
                     "WHERE CAST(ct.thoiGianKhoiHanh AS DATE) = ? " +  // Tiêu chí theo ngày
                     "AND lt.gaDi = ? " +
                     "AND lt.gaDen = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, java.sql.Date.valueOf(ngayKhoiHanh.toLocalDate()));
            pstmt.setString(2, maGaDi);
            pstmt.setString(3, maGaDen);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
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
                    listChuyenTau.add(ct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listChuyenTau;
    }

    // Phương thức thêm chuyến tàu mới (maChuyenTau sẽ được tự động tạo bởi DB) - Sửa: Kiểm tra trùng lịch trình + ngày KH + ngày Đến
    public boolean addChuyenTau(ChuyenTau ct) {
        if (ct.getGiaChuyen() == null || ct.getGiaChuyen().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá chuyến tàu phải lớn hơn 0");
        }
        // Kiểm tra trùng chuyến (lịch trình + ngày KH + ngày Đến)
        if (isTrungChuyen(ct.getMaLichTrinh(), ct.getThoiGianKhoiHanh(), ct.getThoiGianDen())) {
            throw new IllegalArgumentException("Chuyến tàu trùng lịch trình và thời gian đã tồn tại!");
        }
        String sql = "INSERT INTO ChuyenTau (thoiGianKhoiHanh, thoiGianDen, maTau, maLichTrinh, trangThai, giaChuyen) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(ct.getThoiGianKhoiHanh()));
            pstmt.setTimestamp(2, Timestamp.valueOf(ct.getThoiGianDen()));
            pstmt.setString(3, ct.getMaTau());
            pstmt.setString(4, ct.getMaLichTrinh());
            pstmt.setString(5,ct.getTrangThai());
            pstmt.setBigDecimal(6, ct.getGiaChuyen());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Lấy ID tự động tạo để sinh maChuyenTau
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        String generatedMaChuyenTau = "C-" + String.format("%03d", id);
                        ct.setMaChuyenTau(generatedMaChuyenTau);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức cập nhật chuyến tàu - Sửa: Chỉ update thoiGianKhoiHanh, thoiGianDen, giaChuyen; Kiểm tra trùng
    public boolean updateChuyenTau(ChuyenTau ct) {
        if (ct.getGiaChuyen() == null || ct.getGiaChuyen().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá chuyến tàu phải lớn hơn 0");
        }
        // Lấy maLichTrinh cũ để kiểm tra trùng (không thay đổi maLichTrinh)
        String maLichTrinhCu = getMaLichTrinhByMaChuyenTau(ct.getMaChuyenTau());
        if (maLichTrinhCu == null) {
            throw new IllegalArgumentException("Chuyến tàu không tồn tại!");
        }
        // Kiểm tra trùng chuyến (trừ chính chuyến đang update)
        if (isTrungChuyen(maLichTrinhCu, ct.getThoiGianKhoiHanh(), ct.getThoiGianDen(), ct.getMaChuyenTau())) {
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

    // Phương thức xóa chuyến tàu theo mã chuyến tàu - Sửa: Kiểm tra vé và phiếu đặt chỗ
    public boolean deleteChuyenTau(String maChuyenTau) {
        // Kiểm tra có vé liên quan không
        if (coVeLienQuan(maChuyenTau)) {
            throw new IllegalArgumentException("Không thể xóa chuyến có vé đã bán!");
        }
        // Kiểm tra có phiếu đặt chỗ liên quan không
        if (coPhieuDatChoLienQuan(maChuyenTau)) {
            throw new IllegalArgumentException("Không thể xóa chuyến có phiếu đặt chỗ!");
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

    // Helper: Kiểm tra trùng chuyến (lịch trình + ngày KH + ngày Đến)
    private boolean isTrungChuyen(String maLichTrinh, LocalDateTime thoiGianKhoiHanh, LocalDateTime thoiGianDen) {
        String sql = "SELECT COUNT(*) FROM ChuyenTau ct " +
                     "JOIN LichTrinh lt ON ct.maLichTrinh = lt.maLichTrinh " +
                     "WHERE lt.maLichTrinh = ? " +
                     "AND CAST(ct.thoiGianKhoiHanh AS DATE) = ? " +
                     "AND CAST(ct.thoiGianDen AS DATE) = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLichTrinh);
            pstmt.setDate(2, java.sql.Date.valueOf(thoiGianKhoiHanh.toLocalDate()));
            pstmt.setDate(3, java.sql.Date.valueOf(thoiGianDen.toLocalDate()));
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

    // Helper: Kiểm tra trùng chuyến (trừ chính chuyến đang update)
    private boolean isTrungChuyen(String maLichTrinh, LocalDateTime thoiGianKhoiHanh, LocalDateTime thoiGianDen, String maChuyenTau) {
        String sql = "SELECT COUNT(*) FROM ChuyenTau ct " +
                     "JOIN LichTrinh lt ON ct.maLichTrinh = lt.maLichTrinh " +
                     "WHERE lt.maLichTrinh = ? " +
                     "AND CAST(ct.thoiGianKhoiHanh AS DATE) = ? " +
                     "AND CAST(ct.thoiGianDen AS DATE) = ? " +
                     "AND ct.maChuyenTau != ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLichTrinh);
            pstmt.setDate(2, java.sql.Date.valueOf(thoiGianKhoiHanh.toLocalDate()));
            pstmt.setDate(3, java.sql.Date.valueOf(thoiGianDen.toLocalDate()));
            pstmt.setString(4, maChuyenTau);
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

    // Helper: Lấy maLichTrinh từ maChuyenTau (cho update)
    private String getMaLichTrinhByMaChuyenTau(String maChuyenTau) {
        String sql = "SELECT maLichTrinh FROM ChuyenTau WHERE maChuyenTau = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maChuyenTau);
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

    // Helper: Kiểm tra có phiếu đặt chỗ liên quan không (qua ChiTietPhieuDatCho)
    private boolean coPhieuDatChoLienQuan(String maChuyenTau) {
        String sql = "SELECT COUNT(*) FROM ChiTietPhieuDatCho ctpd " +
                     "JOIN PhieuDatCho pdc ON ctpd.maPhieuDatCho = pdc.maPhieuDatCho " +
                     "WHERE ctpd.maChuyenTau = ?";
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
}