// File: src/dao/DoiVeDAO.java
package dao;

import connectDB.connectDB;
import entity.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DoiVeDAO {
    private final VeDAO veDAO = new VeDAO();
    private final ChoNgoiDAO choNgoiDAO = new ChoNgoiDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final ChuyenTauDAO chuyenTauDAO = new ChuyenTauDAO();

    // Kiểm tra: chỉ đổi trước 4 tiếng
    public boolean kiemTraThoiGianDoi(Ve veCu) throws SQLException {
        ChuyenTau chuyenCu = chuyenTauDAO.getChuyenTauByMaChuyenTau(veCu.getMaChuyenTau().getMaChuyenTau());
        LocalDateTime khoiHanh = chuyenCu.getThoiGianKhoiHanh();
        return LocalDateTime.now().isBefore(khoiHanh.minusHours(4));
    }

    // Phí đổi: 10% giá vé gốc
    public BigDecimal tinhPhiDoi(Ve veCu) {
        return veCu.getGiaVeGoc().multiply(new BigDecimal("0.1")).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    // Giao dịch đổi vé (transaction)
    public boolean doiVe(Ve veCu, ChuyenTau chuyenMoi, ChoNgoi choMoi, NhanVien nhanVien, HanhKhach hanhKhachThanhToan) throws SQLException {
        Connection conn = null;
        try {
            conn = connectDB.getConnection();
            conn.setAutoCommit(false);

            if (!kiemTraThoiGianDoi(veCu)) {
                throw new IllegalStateException("Chỉ được đổi vé trước 4 tiếng khởi hành!");
            }

            BigDecimal phiDoi = tinhPhiDoi(veCu);

            // 1. Trả chỗ cũ
            choNgoiDAO.updateTrangThai(veCu.getMaChoNgoi().getMaChoNgoi(), "Trống");

            // 2. Đặt chỗ mới
            choNgoiDAO.updateTrangThai(choMoi.getMaChoNgoi(), "Đã đặt");

            // 3. Cập nhật vé cũ
            veCu.setMaChoNgoi(choMoi);
            veCu.setMaChuyenTau(chuyenMoi);
            veCu.setGiaThanhToan(veCu.getGiaThanhToan().add(phiDoi));
            veCu.setTrangThai("Đã đổi");
            veDAO.updateVe(veCu);

            // 4. Tạo hóa đơn phí đổi
            HoaDon hoaDonPhi = new HoaDon();
            hoaDonPhi.setNgayLap(LocalDateTime.now());
            hoaDonPhi.setTongTien(phiDoi);
            hoaDonPhi.setMaNhanVien(nhanVien);
            hoaDonPhi.setMaHanhKhach(hanhKhachThanhToan);
            hoaDonDAO.insert(hoaDonPhi);

            conn.commit();
            return true;
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new SQLException("Lỗi đổi vé: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}