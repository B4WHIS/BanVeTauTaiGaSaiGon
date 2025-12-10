package control;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

import dao.ChoNgoiDAO;
import dao.ChuyenTauDAO;
import dao.VeDAO;
import entity.ChuyenTau;
import entity.Ve;

public class DoiVeControl {
    private VeDAO veDAO = new VeDAO();
    private ChoNgoiDAO choNgoiDAO = new ChoNgoiDAO();
    private ChuyenTauDAO chuyenTauDAO = new ChuyenTauDAO();

    public boolean kiemTraCoTheDoiVe(Ve ve) throws SQLException {
        if (!"Đã đặt".equalsIgnoreCase(ve.getTrangThai())) {
            return false;
        }
        String maChuyenTau = ve.getMaChuyenTau().getMaChuyenTau(); // Lấy mã
        ChuyenTau chuyenTauFull = chuyenTauDAO.getChuyenTauByMaChuyenTau(maChuyenTau); 
        if (chuyenTauFull == null || chuyenTauFull.getThoiGianKhoiHanh() == null) {
            throw new SQLException("Không tìm thấy chuyến tàu hoặc thời gian khởi hành null cho mã: " + maChuyenTau);
        }
        LocalDateTime khoiHanh = chuyenTauFull.getThoiGianKhoiHanh(); // Bây giờ an toàn
        return Duration.between(LocalDateTime.now(), khoiHanh).toHours() >= 4;
    }
    public BigDecimal calculatePhiDoiVe(BigDecimal giaVeMoi) {
        if (giaVeMoi == null || giaVeMoi.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá vé mới không hợp lệ (null hoặc <=0).");
        }
        return giaVeMoi.multiply(new BigDecimal("0.1"));  // 10%
    }
    // Hủy vé cũ sau khi đổi thành công
    public void huyVeCu(String maVe) throws SQLException {
        Ve ve = veDAO.layVeTheoMa(maVe); 
        veDAO.capNhatTrangThaiVe(maVe, "Đã Đổi");
        choNgoiDAO.capNhatTrangThaiCho(ve.getMaChoNgoi().getMaChoNgoi(), "Khả dụng");
    }
}