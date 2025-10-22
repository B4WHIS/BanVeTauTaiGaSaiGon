package control;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import dao.ChuyenTauDAO;
import entity.ChuyenTau;
import exception.BusinessException;

public class QuanLyChuyenTauControl {
    private ChuyenTauDAO chuyenTauDAO;

    // Constructor injection
    public QuanLyChuyenTauControl(ChuyenTauDAO chuyenTauDAO) {
        this.chuyenTauDAO = chuyenTauDAO;
    }

    // ========== QUẢN LÝ CHUYẾN TÀU ==========
    public List<ChuyenTau> layTatCaChuyenTau() {
        return chuyenTauDAO.getAllChuyenTau();
    }

    public ChuyenTau layChuyenTauTheoMa(String maChuyenTau) {
        ChuyenTau ct = chuyenTauDAO.getChuyenTauByMaChuyenTau(maChuyenTau);
        if (ct == null) {
            throw new BusinessException("Không tìm thấy chuyến tàu với mã: " + maChuyenTau);
        }
        return ct;
    }

    public boolean themChuyenTauMoi(ChuyenTau ct) {
        try {
            // Business logic: Kiểm tra ràng buộc thêm (ví dụ: thời gian khởi hành trong tương lai)
            if (ct.getThoiGianKhoiHanh().isBefore(LocalDateTime.now())) {
                throw new BusinessException("Thời gian khởi hành phải trong tương lai!");
            }
            if (!chuyenTauDAO.addChuyenTau(ct)) {
                throw new BusinessException("Thêm chuyến tàu thất bại!");
            }
            return true;
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Dữ liệu chuyến tàu không hợp lệ: " + e.getMessage());
        }
    }

    public boolean capNhatChuyenTau(ChuyenTau ct) {
        try {
            // Business logic: Kiểm tra không cập nhật nếu chuyến đã khởi hành
            if (ct.getThoiGianKhoiHanh().isBefore(LocalDateTime.now())) {
                throw new BusinessException("Không thể cập nhật chuyến tàu đã khởi hành!");
            }
            return chuyenTauDAO.updateChuyenTau(ct);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Cập nhật chuyến tàu thất bại: " + e.getMessage());
        }
    }

    public boolean xoaChuyenTau(String maChuyenTau) {
        ChuyenTau ct = layChuyenTauTheoMa(maChuyenTau);
        // Kiểm tra ràng buộc: Không xóa nếu có vé hoặc phiếu đặt chỗ đang dùng chuyến này
        // (Giả sử kiểm tra qua query khác, ở đây đơn giản hóa)
        if (ct.getThoiGianKhoiHanh().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Không thể xóa chuyến tàu đã khởi hành!");
        }
        return chuyenTauDAO.deleteChuyenTau(maChuyenTau);
    }

    // Phương thức phức tạp: Lấy chuyến tàu theo ngày và ga đi/đến (sử dụng từ DAO)
    public List<ChuyenTau> layChuyenTauTheoNgayVaGa(LocalDateTime ngayKhoiHanh, String maGaDi, String maGaDen) {
        return chuyenTauDAO.getChuyenTauTheoNgayVaGa(ngayKhoiHanh, maGaDi, maGaDen);
    }

    // Phương thức khác: Lấy chuyến tàu theo thời gian (ví dụ: theo ngày cụ thể, như 21/10/2025)
    public List<ChuyenTau> layChuyenTauTheoThoiGian(LocalDateTime ngayKhoiHanh) {
        // Có thể implement query mới trong DAO nếu cần, ở đây giả sử filter từ all
        return chuyenTauDAO.getAllChuyenTau().stream()
                .filter(ct -> ct.getThoiGianKhoiHanh().toLocalDate().equals(ngayKhoiHanh.toLocalDate()))
                .collect(Collectors.toList());
    }
}
