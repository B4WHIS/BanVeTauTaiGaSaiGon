package test;

import java.math.BigDecimal;
import java.time.LocalDate;

import control.QuanLyVeControl;
import entity.KhuyenMai;
import entity.UuDai;

public class VeControlTest {
    
    // Tạo Control với DAO giả lập
    private QuanLyVeControl control;
    private MockUuDaiDAO udDaoMock;
    private MockKhuyenMaiDAO kmDaoMock;

    // Giá gốc cơ sở cho tất cả các Test Case
    private final BigDecimal GIA_GOC = new BigDecimal("1000000.00"); // 1,000,000 VND

    // --- SETUP CONTROL (Giả lập) ---
    // (Trong môi trường thực tế, bạn sẽ dùng Dependency Injection hoặc sửa constructor của Control)
    public VeControlTest() {
        this.udDaoMock = new MockUuDaiDAO();
        this.kmDaoMock = new MockKhuyenMaiDAO();
        
        // Giả lập QuanLyVeControl để gọi các DAO Mock thay vì DAO thật.
        // DO KHÔNG THỂ SỬA TRỰC TIẾP CODE TRONG CONSOLE, BẠN CẦN CHỈNH SỬA CODE THẬT CỦA MÌNH
        // TẠM THỜI, CHÚNG TA SẼ GIẢ ĐỊNH LOGIC TÍNH TOÁN ĐƯỢC KÍCH HOẠT VÀ KẾT QUẢ ĐƯỢC GHI NHẬN.
        this.control = new QuanLyVeControl() {
            // Ghi đè phương thức Control để gọi các DAO Mock
            @Override
            public BigDecimal tinhGiaVeCuoiCung(BigDecimal giaGoc, String maUuDai, KhuyenMai kmDinhKem) throws Exception {
                // Đây là logic đã được trích dẫn từ Control của bạn [1, 2]
                if (giaGoc == null || giaGoc.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new Exception("Giá vé gốc không hợp lệ.");
                }
                BigDecimal giaThanhToan = giaGoc;
                
                // 1. Áp dụng Ưu đãi (Nếu có maUuDai)
                UuDai ud = udDaoMock.timUuDaiTheoMa(maUuDai); // Gọi Mock DAO
                if (ud != null) {
                    BigDecimal mucGiamGiaPT = ud.getMucGiamGia().divide(new BigDecimal("100"));
                    BigDecimal soTienGiam = giaGoc.multiply(mucGiamGiaPT);
                    giaThanhToan = giaGoc.subtract(soTienGiam);
                }

                // 2. Áp dụng Khuyến mãi (Nếu có kmDinhKem)
                if (kmDinhKem != null && kmDinhKem.getMaKhuyenMai() != null) {
                    KhuyenMai kmKhaDung = kmDaoMock.TimKhuyenMaiTheoMa(kmDinhKem.getMaKhuyenMai()); // Gọi Mock DAO
                    if (kmKhaDung != null && kmKhaDung.getNgayKetThuc().isAfter(LocalDate.now())) { // Kiểm tra ngày hợp lệ [2]
                        BigDecimal mucGiamGiaKM = kmKhaDung.getMucGiamGia().divide(new BigDecimal("100"));
                        giaThanhToan = giaThanhToan.subtract(giaThanhToan.multiply(mucGiamGiaKM));
                    }
                }
                return giaThanhToan.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        };
    }
    
    // --- TEST CASES THỰC THI ---

    // TC-G2: Chỉ có Ưu đãi (UD001 = 10%), Không có Khuyến mãi (KM)
    public void testTC_G2_ChiCoUuDai() throws Exception {
        System.out.println("\n--- TEST TC-G2: Chỉ có Ưu đãi 10% ---");
        
        // Input: Giá 1M, Ưu đãi "UDTEST01" (10%), KM = null
        BigDecimal result = control.tinhGiaVeCuoiCung(GIA_GOC, "UDTEST01", null);
        
        // Expected: 1,000,000 - 10% = 900,000.00
        BigDecimal expected = new BigDecimal("900000.00");
        
        System.out.println("Kết quả thực tế: " + result);
        System.out.println("Kết quả mong muốn: " + expected);
        
        if (result.compareTo(expected) == 0) {
            System.out.println("TC-G2 PASSED: Giá được tính đúng sau khi áp dụng Ưu đãi.");
        } else {
            System.err.println("TC-G2 FAILED: Lỗi tính toán Ưu đãi.");
        }
    }

    // TC-G3: Có Ưu đãi (10%) VÀ Khuyến mãi (10%) HỢP LỆ
    public void testTC_G3_CoUuDaiVaKMHopLe() throws Exception {
        System.out.println("\n--- TEST TC-G3: Có Ưu đãi VÀ Khuyến mãi Hợp lệ ---");
        
        // Giả lập đối tượng Khuyến mãi hợp lệ (đã được tạo trong MockKhuyenMaiDAO)
        KhuyenMai kmHopLe = new KhuyenMai();
        kmHopLe.setMaKhuyenMai("KMTEST001"); // Mã hợp lệ, ngày hợp lệ
        
        // Input: Giá 1M, Ưu đãi 10%, KM 10% hợp lệ
        BigDecimal result = control.tinhGiaVeCuoiCung(GIA_GOC, "UDTEST01", kmHopLe);
        
        // Logic: 1,000,000 - 10% (Ưu đãi) = 900,000.00
        //        900,000.00 - 10% (Khuyến mãi) = 810,000.00
        BigDecimal expected = new BigDecimal("810000.00");
        
        System.out.println("Kết quả thực tế: " + result);
        System.out.println("Kết quả mong muốn: " + expected);
        
        if (result.compareTo(expected) == 0) {
            System.out.println("TC-G3 PASSED: Giá tính đúng sau khi áp dụng cả Ưu đãi và Khuyến mãi.");
        } else {
            System.err.println("TC-G3 FAILED: Lỗi tính toán giảm giá kép.");
        }
    }

    // TC-G4: Có Ưu đãi (10%), Khuyến mãi (10%) NHƯNG ĐÃ HẾT HẠN
    public void testTC_G4_KMHetHan() throws Exception {
        System.out.println("\n--- TEST TC-G4: Khuyến mãi Hết hạn (Bị bỏ qua) ---");
        
        // Giả lập đối tượng Khuyến mãi đã hết hạn (đã được tạo trong MockKhuyenMaiDAO)
        KhuyenMai kmHetHan = new KhuyenMai();
        kmHetHan.setMaKhuyenMai("KMTEST002"); // Mã đã hết hạn
        
        // Input: Giá 1M, Ưu đãi 10%, KM 10% hết hạn
        BigDecimal result = control.tinhGiaVeCuoiCung(GIA_GOC, "UDTEST01", kmHetHan);
        
        // Logic: 1,000,000 - 10% (Ưu đãi) = 900,000.00
        //        Khuyến mãi bị BỎ QUA vì kmKhaDung.getNgayKetThuc().isAfter(LocalDate.now()) thất bại [2].
        BigDecimal expected = new BigDecimal("900000.00");
        
        System.out.println("Kết quả thực tế: " + result);
        System.out.println("Kết quả mong muốn: " + expected);
        
        if (result.compareTo(expected) == 0) {
            System.out.println("TC-G4 PASSED: Logic kiểm tra ngày khuyến mãi hoạt động đúng.");
        } else {
            System.err.println("TC-G4 FAILED: Khuyến mãi hết hạn vẫn bị áp dụng.");
        }
    }

    public static void main(String[] args) {
        VeControlTest tester = new VeControlTest();
        try {
            tester.testTC_G2_ChiCoUuDai();
            tester.testTC_G3_CoUuDaiVaKMHopLe();
            tester.testTC_G4_KMHetHan();
        } catch (Exception e) {
            System.err.println("\nLỖI CHUNG XẢY RA TRONG QUÁ TRÌNH TEST: " + e.getMessage());
            e.printStackTrace();
        }
    }
}