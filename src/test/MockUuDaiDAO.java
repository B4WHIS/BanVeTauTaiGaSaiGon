package test; // Đặt trong package test hoặc tương đương

import java.math.BigDecimal;
import java.time.LocalDate;

import entity.KhuyenMai;
import entity.UuDai;

// Giả lập các DAO cần thiết để QuanLyVeControl có thể hoạt động
class MockUuDaiDAO {
    // Giả lập ưu đãi 10% (UDTEST01)
    public UuDai timUuDaiTheoMa(String maUuDai) {
        if ("UDTEST01".equals(maUuDai)) {
            // Giảm 10% (Giá trị 10.00 cho Big Decimal)
            return new UuDai("UDTEST01", 3, new BigDecimal("10.00"), "Khách hàng thân thiết"); // [9, 10]
        }
        return null;
    }
}

class MockKhuyenMaiDAO {
    // Giả lập khuyến mãi 10%
    public KhuyenMai TimKhuyenMaiTheoMa(String maKhuyenMai) {
        try {
            if ("KMTEST001".equals(maKhuyenMai)) {
                // TC-G3: Khuyến mãi Hợp lệ (Ngày kết thúc là 30 ngày sau)
                LocalDate ngayKetThucHopLe = LocalDate.now().plusDays(30); 
                // MucGiamGia = 10%
                return new KhuyenMai("KMTEST001", "KM Mùa Hè", new BigDecimal("10.00"), 
                                     LocalDate.now().minusDays(5), ngayKetThucHopLe, "Test"); // [11, 12]
            }
            if ("KMTEST002".equals(maKhuyenMai)) {
                // TC-G4: Khuyến mãi HẾT HẠN (Ngày kết thúc là hôm qua)
                LocalDate ngayKetThucHetHan = LocalDate.now().minusDays(1); 
                return new KhuyenMai("KMTEST002", "KM Hết Hạn", new BigDecimal("10.00"), 
                                     LocalDate.now().minusDays(10), ngayKetThucHetHan, "Test");
            }
        } catch (Exception e) {
            System.err.println("Lỗi trong quá trình tạo đối tượng KM Mock: " + e.getMessage());
        }
        return null;
    }
}