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
        String sql = "SELECT maChuyenTau, thoiGianKhoiHanh, thoiGianDen, maTau, maLichTrinh, giaChuyen FROM ChuyenTau";
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
        String sql = "SELECT maChuyenTau, thoiGianKhoiHanh, thoiGianDen, maTau, maLichTrinh, giaChuyen FROM ChuyenTau WHERE maChuyenTau = ?";
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

    // Phương thức lấy chuyến tàu theo ngày và ga đi/đến (phương thức phức tạp)
    public List<ChuyenTau> getChuyenTauTheoNgayVaGa(LocalDateTime ngayKhoiHanh, String maGaDi, String maGaDen) {
        List<ChuyenTau> listChuyenTau = new ArrayList<>();
        String sql = "SELECT ct.maChuyenTau, ct.thoiGianKhoiHanh, ct.thoiGianDen, ct.maTau, ct.maLichTrinh, ct.giaChuyen " +
                     "FROM ChuyenTau ct " +
                     "JOIN LichTrinh lt ON ct.maLichTrinh = lt.maLichTrinh " +
                     "WHERE CAST(ct.thoiGianKhoiHanh AS DATE) = ? " +
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

    // Phương thức thêm chuyến tàu mới (maChuyenTau sẽ được tự động tạo bởi DB)
    public boolean addChuyenTau(ChuyenTau ct) {
        if (ct.getGiaChuyen() == null || ct.getGiaChuyen().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá chuyến tàu phải lớn hơn 0");
        }
        String sql = "INSERT INTO ChuyenTau (thoiGianKhoiHanh, thoiGianDen, maTau, maLichTrinh, giaChuyen) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(ct.getThoiGianKhoiHanh()));
            pstmt.setTimestamp(2, Timestamp.valueOf(ct.getThoiGianDen()));
            pstmt.setString(3, ct.getMaTau());
            pstmt.setString(4, ct.getMaLichTrinh());
            pstmt.setBigDecimal(5, ct.getGiaChuyen());
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

    // Phương thức cập nhật chuyến tàu
    public boolean updateChuyenTau(ChuyenTau ct) {
        if (ct.getGiaChuyen() == null || ct.getGiaChuyen().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá chuyến tàu phải lớn hơn 0");
        }
        String sql = "UPDATE ChuyenTau SET thoiGianKhoiHanh = ?, thoiGianDen = ?, maTau = ?, maLichTrinh = ?, giaChuyen = ? WHERE maChuyenTau = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(ct.getThoiGianKhoiHanh()));
            pstmt.setTimestamp(2, Timestamp.valueOf(ct.getThoiGianDen()));
            pstmt.setString(3, ct.getMaTau());
            pstmt.setString(4, ct.getMaLichTrinh());
            pstmt.setBigDecimal(5, ct.getGiaChuyen());
            pstmt.setString(6, ct.getMaChuyenTau());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức xóa chuyến tàu theo mã chuyến tàu
    public boolean deleteChuyenTau(String maChuyenTau) {
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
}