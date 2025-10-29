package control;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import connectDB.connectDB;
import dao.ChoNgoiDAO;
import dao.ChuyenTauDAO;
import dao.GaDAO;
import dao.HanhKhachDAO;
import dao.KhuyenMaiDAO;
import dao.LichSuVeDAO;
import dao.TauDAO;
import dao.ThongKeDoanhThuDAO;
import dao.UuDaiDAO;
import dao.VeDAO;
import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.HanhKhach;
import entity.KhuyenMai;
import entity.LichSuVe;
import entity.NhanVien;
import entity.Tau;
import entity.UuDai;
import entity.Ve;

public class QuanLyVeControl {
    private VeDAO veDao;
    private HanhKhachDAO hkDao;
    private ChoNgoiDAO cnDao;
    private ChuyenTauDAO ctDao;
    private UuDaiDAO udDao;
    private LichSuVeDAO lsvDao;
    private KhuyenMaiDAO kmDao;
    private LichSuVeDAO lsv;
    private ThongKeDoanhThuDAO tkdtDao;
    private ThongKeDoanhThuDAO tkdtDao;
    private ChoNgoiDAO choNgoiDAO = new ChoNgoiDAO();
	private GaDAO gaDao;
	private TauDAO tauDao;
	private ChoNgoiDAO loaiGheDao = new ChoNgoiDAO(); 
    private static final int ID_BAN_VE = 1;
    private static final int ID_HUY_VE = 2;
    private static final int ID_DOI_VE = 3;

    public QuanLyVeControl() {
        this.veDao = new VeDAO();
        this.hkDao = new HanhKhachDAO();
        this.cnDao = new ChoNgoiDAO();
        this.ctDao = new ChuyenTauDAO();
        this.udDao = new UuDaiDAO();
        this.lsvDao = new LichSuVeDAO();
        this.kmDao = new KhuyenMaiDAO();
    }

    public boolean datVe(Ve veMoi, HanhKhach hkMoi, NhanVien nvlap) throws Exception {
        HanhKhach finalHK = hkMoi;
        try {
            HanhKhach hanhkhachHienCo = null;
            // xử lí và xác thực
            if (hkMoi.getCmndCccd() != null && !hkMoi.getCmndCccd().trim().isEmpty()) {
                hanhkhachHienCo = hkDao.layHanhKhachTheoCMND(hkMoi.getCmndCccd());
            }
            // khách cũ
            if (hanhkhachHienCo != null) {
                hkMoi.setMaKH(hanhkhachHienCo.getMaKH()); // giữ lại mã
                hkDao.capNhatHanhKhach(hkMoi);
                finalHK = hkMoi;
            } else {
                // khách mới
                hkDao.themHanhKhach(hkMoi);
                finalHK = hkMoi;
            }
            veMoi.setMaHanhkhach(finalHK);
        } catch (IllegalArgumentException e) {
            throw new IllegalAccessException("Thông tin hành khách không hợp lệ: " + e.getMessage());
        } catch (SQLException e) {
            throw new Exception("Lỗi CSDL" + e.getMessage());
        }
        // Tính toán
        try {
            BigDecimal giaThanhToan = tinhGiaVeCuoiCung(veMoi.getGiaVeGoc(), finalHK.getMaUuDai(), veMoi.getMaKhuyenMai());

            if (giaThanhToan.compareTo(BigDecimal.ZERO) <= 0) {
                throw new Exception("Giá thanh toán cuối cùng không hợp lệ.");
            }
            veMoi.setGiaThanhToan(giaThanhToan);

            // cập nhật chỗ ngồi
            String maChoNgoi = veMoi.getMaChoNgoi().getMaChoNgoi();
            cnDao.updateTrangThai(maChoNgoi, "Đã đặt");

            return veDao.themVe(veMoi);

        } catch (Exception e) {
            throw new Exception("Lỗi trong quá trình tính toán giá" + e.getMessage());
        }
    }

    // =====================================================================
    // CHỈ SỬA PHẦN HỦY VÉ – BẮT ĐẦU TỪ ĐÂY
    // =====================================================================

    public BigDecimal tinhPhiHuy(BigDecimal giaGoc, LocalDateTime thoiGianKhoiHanh) {
        LocalDateTime thoiGianNOW = LocalDateTime.now();
        Duration duration = Duration.between(thoiGianNOW, thoiGianKhoiHanh);
        long gioConLai = duration.toHours();

        // Không cho phép hủy nếu < 4 giờ
        if (gioConLai < 4) {
            return null;
        }

        BigDecimal tiLePhi;
        if (gioConLai >= 24) {
            tiLePhi = new BigDecimal("0.10"); // 10%
        } else if (gioConLai >= 12) {
            tiLePhi = new BigDecimal("0.20"); // 20%
        } else {
            tiLePhi = new BigDecimal("0.30"); // 30%
        }

        return giaGoc.multiply(tiLePhi).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    public LichSuVe xuLyHuyVe(String maVe, String liDoHuy, NhanVien nvThucHien) throws Exception {
        // 1. Tìm vé
        Ve ve = veDao.layVeTheoMa(maVe);
        if (ve == null) {
            throw new Exception("Không tìm thấy vé có mã: " + maVe);
        }

        // 2. Kiểm tra trạng thái: chỉ hủy được vé "Khả dụng"
        if (!"Khả dụng".equalsIgnoreCase(ve.getTrangThai())) {
            throw new Exception("Vé đã hủy hoặc đã sử dụng, không thể hủy lại!");
        }

        // 3. Lấy thông tin chuyến tàu
        ChuyenTau chuyenTau = ctDao.getChuyenTauByMaChuyenTau(ve.getMaChuyenTau().getMaChuyenTau());
        if (chuyenTau == null) {
            throw new Exception("Không tìm thấy thông tin chuyến tàu.");
        }

        // 4. Tính phí hủy theo điều kiện
        BigDecimal phiHuy = tinhPhiHuy(ve.getGiaVeGoc(), chuyenTau.getThoiGianKhoiHanh());
        if (phiHuy == null) {
            throw new Exception("Không thể hủy: còn dưới 4 giờ trước giờ tàu chạy!");
        }

        // 5. Tính tiền hoàn
        BigDecimal tienHoan = ve.getGiaThanhToan().subtract(phiHuy);
        if (tienHoan.compareTo(BigDecimal.ZERO) < 0) {
            tienHoan = BigDecimal.ZERO;
        }

        // 6. GIAO DỊCH: Cập nhật DB
        try {
            // Cập nhật trạng thái vé
            if (!veDao.capNhatTrangThaiVe(maVe, "Đã hủy")) {
                throw new SQLException("Lỗi cập nhật trạng thái vé");
            }

            // Giải phóng ghế
            String maChoNgoi = ve.getMaChoNgoi().getMaChoNgoi();
            if (!cnDao.updateTrangThai(maChoNgoi, "Trống")) {
                throw new SQLException("Lỗi cập nhật trạng thái chỗ ngồi");
            }

            // GHI LỊCH SỬ HỦY VÉ – ĐÚNG VỚI ENTITY BẠN GỬI
            LichSuVe lichSu = new LichSuVe(
                null, 
                ID_HUY_VE, 
                LocalDateTime.now(), 
                maVe, 
                nvThucHien != null ? nvThucHien.getMaNhanVien() : "NV001"
            );

            lichSu.setMaHanhKhach(ve.getMaHanhkhach().getMaKH());
            lichSu.setLyDo(liDoHuy + " | Hoàn: " + tienHoan + " VNĐ");
            lichSu.setPhiXuLy(phiHuy);
            // Nếu entity có field tienHoan → thêm setter
            lichSu.setTienHoan(tienHoan);

            if (!lsvDao.themLichSuVe(lichSu)) {
                throw new SQLException("Lỗi ghi lịch sử hủy vé");
            }

            return lichSu;

        } catch (SQLException e) {
            throw new Exception("Lỗi CSDL khi hủy vé: " + e.getMessage());
        }
    }
    // =====================================================================
    // KẾT THÚC PHẦN HỦY VÉ – KHÔNG ĐỘNG GÌ DƯỚI ĐÂY
    // =====================================================================

    public boolean doiVe(String maVeCu, ChuyenTau ctMoi, ChoNgoi cnMoi, NhanVien nv) throws Exception {
        //kiểm tra vé cũ
        Ve veCu = veDao.layVeTheoMa(maVeCu);
        if (veCu == null) {
            throw new Exception("Không tìm thấy vé có mã " + maVeCu);
        }

        //kiểm tra thời gian
        LocalDateTime TGKH_Cu = veCu.getMaChuyenTau().getThoiGianKhoiHanh();
        if(TGKH_Cu.minusHours(4).isBefore(LocalDateTime.now())) {
            throw new Exception("Vé không đủ điều kiện đổi, phải đổi trước giờ khởi hành 4 giờ.");
        }

        //Kiểm tra chỗ ngồi mới
        ChoNgoi trangThaiChoMoi = cnDao.getChoNgoiByMa(cnMoi.getMaChoNgoi());
        if (trangThaiChoMoi == null || !trangThaiChoMoi.getTrangThai().equalsIgnoreCase("Trống")) {
            throw new Exception("Chỗ ngồi mới đã có người đặt hoặc không tồn tại");
        }

        //tính toán giá vé mới
        BigDecimal giaCu = veCu.getGiaThanhToan();
        BigDecimal giaVeGocMoi = ctMoi.getGiaChuyen();

        BigDecimal giaMoiThanhToan = tinhGiaVeCuoiCung(giaVeGocMoi, veCu.getMaHanhkhach().getMaUuDai(), veCu.getMaKhuyenMai());

        BigDecimal chenhLech = giaMoiThanhToan.subtract(giaCu);

        try {
            //Giao dịch
            if(!cnDao.updateTrangThai(veCu.getMaChoNgoi().getMaChoNgoi(), "Trống")) {
                throw new SQLException("Lỗi chỗ ngồi cũ.");
            }

            if(!veDao.capNhatTrangThaiVe(maVeCu,"Đã đổi")) {
                throw new SQLException("Lỗi cập nhật trạng thái vé cũ");
            }

            //Thêm vé mới
            Ve veMoi = new Ve();

            veMoi.setMaChuyenTau(ctMoi);
            veMoi.setMaChoNgoi(cnMoi);
            veMoi.setGiaVeGoc(giaVeGocMoi);
            veMoi.setGiaThanhToan(giaMoiThanhToan);
            veMoi.setMaHanhkhach(veCu.getMaHanhkhach());
            veMoi.setMaNhanVien(nv);
            veMoi.setNgayDat(LocalDateTime.now());
            veMoi.setTrangThai("Khả dụng");

            if(!veDao.themVe(veMoi)) {
                throw new SQLException("Lỗi thêm vé mới");
            }
            if(!cnDao.updateTrangThai(cnMoi.getMaChoNgoi(), "Đã đặt")) {
                throw new SQLException("Lỗi cập nhật trạng thái vé");
            }

            //ghi lịch sửa
            LichSuVe lichSu = new LichSuVe(null, ID_DOI_VE, LocalDateTime.now(), veMoi.getMaVe(), nv.getMaNhanVien());
            lichSu.setPhiXuLy(chenhLech.abs());
            lichSu.setMaHanhKhach(veCu.getMaHanhkhach().getMaKH());
            lichSu.setLyDo("Đổi vé từ " + maVeCu + "sang " + veMoi.getMaVe() + ". Chênh lệch: " + chenhLech.toString());
            if(!lsvDao.themLichSuVe(lichSu)) {
                throw new SQLException("Lỗi ghi lịch sử đổi vé");
            }

            ThongKeDoanhThu tkHienTai = tkdtDao.getThongKeTheoNgayVaNV(LocalDate.now(), nv.getMaNhanVien());
            if(tkHienTai != null) {
                tkHienTai.setTongSoHoanDoi(tkHienTai.getTongSoHoanDoi() + 1);
                tkdtDao.updateThongKe(tkHienTai);
            }else {
                ThongKeDoanhThu tkMoi = new ThongKeDoanhThu(null, LocalDate.now(), nv.getMaNhanVien(), BigDecimal.ZERO, 0);
                tkMoi.setTongSoHoanDoi(1);
                tkdtDao.insertThongKe(tkMoi);
            }

            //Thông báo chênh lệch
            if(chenhLech.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("Cần thanh toán thêm: " + chenhLech.toString());
            }else if(chenhLech.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Cần hoàn tiền lại: " + chenhLech.abs().toString());
            }
            return true;
        } catch (Exception e) {
            throw new Exception("Đổi vé thất bại do lỗi giao dịch: "+ e.getMessage());
        }
    }

    private BigDecimal tinhGiaVe(ChuyenTau ctMoi, ChoNgoi cnMoi, KhuyenMai maKhuyenMai) {
        return null;
    }

    public BigDecimal tinhGiaVeCuoiCung(BigDecimal giaGoc, String maUuDai, KhuyenMai kmDinhKem) throws Exception {
        if (giaGoc == null || giaGoc.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Giá vé gốc không hợp lệ.");
        }
        BigDecimal giaThanhToan = giaGoc;
        UuDai ud = udDao.timUuDaiTheoMa(maUuDai);
        if (ud != null) {
            BigDecimal mucGiamGiaPT = ud.getMucGiamGia().divide(new BigDecimal("100"));
            BigDecimal soTienGiam = giaGoc.multiply(mucGiamGiaPT);
            giaThanhToan = giaGoc.subtract(soTienGiam);
        }

        if (kmDinhKem != null && kmDinhKem.getMaKhuyenMai() != null) {
            KhuyenMai kmKhaDung = kmDao.TimKhuyenMaiTheoMa(kmDinhKem.getMaKhuyenMai());

            if (kmKhaDung != null && kmKhaDung.getNgayKetThuc().isAfter(LocalDate.now())) {
                BigDecimal mucGiamGiaKM = kmKhaDung.getMucGiamGia().divide(new BigDecimal("100"));
                giaThanhToan = giaThanhToan.subtract(giaThanhToan.multiply(mucGiamGiaKM));
        this.tkdtDao = new ThongKeDoanhThuDAO();
        this.tauDao = new TauDAO();
        this.gaDao = new GaDAO();
    }

    public BigDecimal tinhGiaVeCuoiCung(BigDecimal giaVeGoc, String maUuDai, KhuyenMai kmDinhKem) throws Exception {
        if (giaVeGoc == null || giaVeGoc.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Giá vé gốc không hợp lệ."); // [5, 6]
        }
        BigDecimal giaThanhToan = giaVeGoc;
        
        // 1. Áp dụng Ưu đãi
        UuDai ud = udDao.timUuDaiTheoMa(maUuDai); // Giả định tồn tại udDao
        if (ud != null) {
            BigDecimal mucGiamGiaPT = ud.getMucGiamGia().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP); // [7]
            BigDecimal soTienGiam = giaVeGoc.multiply(mucGiamGiaPT);
            giaThanhToan = giaVeGoc.subtract(soTienGiam); 
        }

        // 2. Áp dụng Khuyến Mãi (Nếu Khuyến mãi hợp lệ)
        if (kmDinhKem != null && kmDinhKem.getMaKhuyenMai() != null) {
            KhuyenMai kmKhaDung = kmDao.TimKhuyenMaiTheoMa(kmDinhKem.getMaKhuyenMai()); // Giả định tồn tại kmDao
            if (kmKhaDung != null && kmKhaDung.getNgayKetThuc().isAfter(LocalDate.now())) { // Kiểm tra ngày kết thúc [8, 9]
                BigDecimal mucGiamGiaKM = kmKhaDung.getMucGiamGia().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                giaThanhToan = giaThanhToan.subtract(giaThanhToan.multiply(mucGiamGiaKM)); // [7, 9]
            }
        }
        return giaThanhToan.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public class MockData {
        public static NhanVien taoNhanVienTest() {
            try {
                return new NhanVien("NV001", "Nguyen Van B", LocalDate.now().minusYears(20), "0901234567", "123456789", 1);
            } catch (Exception e) {
                System.err.println("Lỗi tạo NV giả lập: " + e.getMessage());
                return null;
            }
        }
        public static KhuyenMai taoKhuyenMaiGiaLap(boolean hopLe) {
            try {
                LocalDate ngayKetThuc = LocalDate.now().plusMonths(1);
                return new KhuyenMai("KMTEST001", "Giam 10%", new BigDecimal("10"), LocalDate.now().minusDays(10), ngayKetThuc, "Khong dieu kien");
            } catch (Exception e) {
                System.err.println("Lỗi tạo KM giả lập: " + e.getMessage());
                return null;
            }
        }
    }

    private UuDai timUuDaiTest(String maUuDai) {
        if ("UD001".equals(maUuDai)) {
            try {
                return new UuDai("UD001", 1, new BigDecimal("10"), "Khach hang than thiet");
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
    /**
     * Thực hiện giao dịch đặt vé.
     * Đã FIX: Đảm bảo tính toàn vẹn dữ liệu chỗ ngồi và vé bằng logic rollback thủ công.
     * @return String maVe đã được thêm thành công.
     * @throws Exception nếu xảy ra lỗi CSDL hoặc lỗi nghiệp vụ.
     */
    public String datVe(Ve veMoi, HanhKhach hkMoi, NhanVien nvlap) throws Exception {
        HanhKhach finalHK = hkMoi;
        String maChoNgoi = veMoi.getMaChoNgoi().getMaChoNgoi();
        
        ChoNgoi choNgoiHienTai = cnDao.getChoNgoiByMa(maChoNgoi);
        
        String maVeVuaThem = null;
        Connection conn = null;
        
        if (choNgoiHienTai == null || !"Trống".equalsIgnoreCase(choNgoiHienTai.getTrangThai().trim())) {
            throw new Exception("Chỗ ngồi " + maChoNgoi + " không còn trống hoặc không tồn tại.");
        }
        veMoi.setTrangThai("Đã đặt"); 
        try {
            // 2.0 LẤY KẾT NỐI VÀ VÔ HIỆU HÓA AUTO-COMMIT
            conn = connectDB.getConnection();
            conn.setAutoCommit(false); // Bắt đầu giao dịch [4, 6]

            // 2.1. Cập nhật trạng thái chỗ ngồi thành 'Đã đặt' (Cần DAO nhận Connection)
            // GIẢ ĐỊNH: cnDao.updateTrangThai(maChoNgoi, "Đã đặt", conn);
            if (!cnDao.updateTrangThai(maChoNgoi, "Đã đặt", conn)) {
                throw new SQLException("Lỗi cập nhật trạng thái chỗ ngồi: " + maChoNgoi);
            }
            
            maVeVuaThem = veDao.themVe(veMoi, conn); 
            if (maVeVuaThem == null || maVeVuaThem.trim().isEmpty()) {
                throw new SQLException("Thêm vé thất bại, không nhận được mã vé.");
            }
            
            LichSuVe lichSu = new LichSuVe(null, ID_BAN_VE, LocalDateTime.now(), maVeVuaThem,
                    nvlap.getMaNhanVien());
                lichSu.setMaHanhKhach(veMoi.getMaHanhkhach().getMaKH());
                lichSu.setLyDo("Bán vé thành công");
                lichSu.setPhiXuLy(BigDecimal.ZERO);
                lsvDao.themLichSuVe(lichSu, conn); 

            // NẾU TẤT CẢ THÀNH CÔNG: COMMIT GIAO DỊCH
            conn.commit(); 
            return maVeVuaThem;

        } catch (SQLException e) {
            // Nếu có bất kỳ lỗi CSDL nào (ví dụ: mất kết nối, lỗi FK, lỗi sequence), ROLLBACK
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException exRollback) {
                    System.err.println("LỖI CẢNH BÁO: Không thể rollback giao dịch: " + exRollback.getMessage());
                }
            }
            // Ném ngoại lệ nghiệp vụ sau khi rollback
            throw new Exception("Đặt vé thất bại (Đã Rollback CSDL): " + e.getMessage());
        } catch (Exception e) {
            // Xử lý các lỗi logic/nghiệp vụ khác
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException exRollback) {}
            }
            throw new Exception("Đặt vé thất bại: " + e.getMessage());
        } finally {
            // Đảm bảo kết nối luôn được đóng
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Trả lại chế độ mặc định nếu cần
                    conn.close();
                } catch (SQLException exClose) {}
            }
        }
    }
    public String getTenGaDen(String maLichTrinh) throws SQLException {
        // Gọi thẳng đến phương thức đã có trong GaDAO [1, 2]
        return gaDao.getTenGaDenByMaLichTrinh(maLichTrinh); 
    }
    public String getTenTau(String maTau) throws SQLException {
        // GIẢ ĐỊNH: TauDAO có phương thức getTenTauByMa(maTau)
        // Nếu chưa có, bạn cần thêm phương thức này vào TauDAO, truy vấn cột tenTau.
        // Tạm thời, ta gọi phương thức getTauByMa và lấy tên tàu:
        Tau tau = tauDao.getTauByMaTau(maTau); // Giả định TauDAO có getTauByMa
        return (tau != null) ? tau.getTenTau() : maTau; 
    }
    
    public boolean capNhatTrangThaiChoNgoi(String maChoNgoi, String trangThai) throws SQLException {
        // Gọi DAO để cập nhật (sử dụng ChoNgoiDAO [9])
        return choNgoiDAO.updateTrangThai(maChoNgoi, trangThai); 
    }


    // =======================================================
    // HỦY VÉ (HOÀN VÉ) - Dựa trên logic đã có [1-15]
    // =======================================================

    private BigDecimal tinhPhiHuy(BigDecimal giaGoc, LocalDateTime thoiGianKhoiHanh) {
        LocalDateTime thoiGianNOW = LocalDateTime.now();
        Duration duration = Duration.between(thoiGianNOW, thoiGianKhoiHanh);
        long gioConLai = duration.toHours();

        if (gioConLai < 4) { // Điều kiện hủy tối thiểu 4 giờ trước khởi hành
            return null;
        }
        BigDecimal tiLePhi;
        if (gioConLai >= 24) {
            tiLePhi = new BigDecimal("0.10"); // 10%
        } else if (gioConLai >= 12) {
            tiLePhi = new BigDecimal("0.20"); // 20%
        } else {
            tiLePhi = new BigDecimal("0.30"); // 30%
        }
        return giaGoc.multiply(tiLePhi).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public LichSuVe xuLyHuyVe(String maVe, String liDoHuy, NhanVien nvThucHien) throws Exception {
        Ve ve = veDao.layVeTheoMa(maVe);

        if (ve == null) {
            throw new Exception("Không tìm thấy vé có mã: " + maVe);
        }
        if (ve.getTrangThai().equalsIgnoreCase("Đã hủy") || ve.getTrangThai().equalsIgnoreCase("Đã sử dụng")) {
            throw new Exception("Vé đã được hủy hoặc đã sử dụng");
        }

        ChuyenTau chuyenTau = ctDao.getChuyenTauByMaChuyenTau(ve.getMaChuyenTau().getMaChuyenTau());
        if (chuyenTau == null) {
            throw new Exception("Không tìm thấy thông tin chuyến tàu liên quan.");
        }

        BigDecimal giaVeGoc = ve.getGiaVeGoc();
        LocalDateTime thoiGianKhoiHanh = chuyenTau.getThoiGianKhoiHanh();
        BigDecimal phiHuy = tinhPhiHuy(giaVeGoc, thoiGianKhoiHanh);

        if (phiHuy == null) {
            throw new Exception("Vé không đủ điều kiện hủy (phải hủy trước giờ khởi hành ít nhất 4 giờ).");
        }

        BigDecimal tienHoanLai = ve.getGiaThanhToan().subtract(phiHuy);
        if (tienHoanLai.compareTo(BigDecimal.ZERO) < 0) {
            tienHoanLai = BigDecimal.ZERO;
        }

        // 4. THỰC HIỆN GIAO DỊCH (Không sửa Hóa đơn, chỉ update trạng thái Vé và CN)
        try {
            if (!veDao.capNhatTrangThaiVe(maVe, "Đã hủy")) {
                throw new SQLException("Lỗi cập nhật trạng thái vé");
            }
            String maChoNgoi = ve.getMaChoNgoi().getMaChoNgoi();
            if (!cnDao.updateTrangThai(maChoNgoi, "Trống")) {
                throw new SQLException("Lỗi cập nhật trạng thái chỗ ngồi");
            }

            // Ghi lại lịch sử giao dịch Hủy vé (ID_HUY_VE = 2)
            LichSuVe lichSu = new LichSuVe(null, ID_HUY_VE, LocalDateTime.now(), maVe, nvThucHien.getMaNhanVien());
            lichSu.setLyDo(liDoHuy + " | tiền hoàn lại: " + tienHoanLai.toString());
            lichSu.setPhiXuLy(phiHuy);
            lichSu.setMaHanhKhach(ve.getMaHanhkhach().getMaKH());

            if (!lsvDao.themLichSuVe(lichSu)) {
                throw new SQLException("Lỗi ghi lịch sử hủy vé");
            }
            return lichSu;
        } catch (SQLException e) {
            throw new Exception("Lỗi CSDL khi hủy vé: " + e.getMessage());
        }
    }

	public String getTenGaDi(String maLichTrinh) throws SQLException {
		// TODO Auto-generated method stub
		 return gaDao.getTenGaDiByMaLichTrinh(maLichTrinh); 
	}
	public String getTenLoaiGhe(int IDloaiGhe) throws SQLException {
	    return loaiGheDao.getTenLoaiGheByID(IDloaiGhe);
	}
    // =======================================================
    // ĐỔI VÉ - Dựa trên logic đã có [16-36]
    // =======================================================

//    public boolean doiVe(String maVeCu, ChuyenTau ctMoi, ChoNgoi cnMoi, NhanVien nv) throws Exception {
//        Ve veCu = veDao.layVeTheoMa(maVeCu);
//        if (veCu == null) {
//            throw new Exception("Không tìm thấy vé có mã " + maVeCu);
//        }
//
//        LocalDateTime TGKH_Cu = veCu.getMaChuyenTau().getThoiGianKhoiHanh();
//        if (TGKH_Cu.minusHours(4).isBefore(LocalDateTime.now())) {
//            throw new Exception("Vé không đủ điều kiện đổi, phải đổi trước giờ khởi hành 4 giờ.");
//        }
//
//        ChoNgoi trangThaiChoMoi = cnDao.getChoNgoiByMa(cnMoi.getMaChoNgoi());
//        if (trangThaiChoMoi == null || !trangThaiChoMoi.getTrangThai().equalsIgnoreCase("Trống")) {
//            throw new Exception("Chỗ ngồi mới đã có người đặt hoặc không tồn tại");
//        }
//
//        // Tính toán giá vé mới
//        BigDecimal giaCu = veCu.getGiaThanhToan();
//        BigDecimal giaVeGocMoi = ctMoi.getGiaChuyen();
//        BigDecimal giaMoiThanhToan = tinhGiaVeCuoiCung(giaVeGocMoi, veCu.getMaHanhkhach().getMaUuDai(), veCu.getMaKhuyenMai());
//        BigDecimal chenhLech = giaMoiThanhToan.subtract(giaCu);
//
//        try {
//            // 1. Giải phóng chỗ ngồi cũ
//            if (!cnDao.updateTrangThai(veCu.getMaChoNgoi().getMaChoNgoi(), "Trống")) {
//                throw new SQLException("Lỗi chỗ ngồi cũ.");
//            }
//
//            // 2. Cập nhật trạng thái vé cũ
//            if (!veDao.capNhatTrangThaiVe(maVeCu, "Đã đổi")) {
//                // Nếu lỗi, phải rollback chỗ ngồi cũ
//                cnDao.updateTrangThai(veCu.getMaChoNgoi().getMaChoNgoi(), "Đã đặt");
//                throw new SQLException("Lỗi cập nhật trạng thái vé cũ");
//            }
//
//            // 3. Thêm vé mới (Vé mới phải có mã mới, trạng thái "Khả dụng")
//            Ve veMoi = new Ve();
//            veMoi.setMaChuyenTau(ctMoi);
//            veMoi.setMaChoNgoi(cnMoi);
//            veMoi.setGiaVeGoc(giaVeGocMoi);
//            veMoi.setGiaThanhToan(giaMoiThanhToan);
//            veMoi.setMaHanhkhach(veCu.getMaHanhkhach());
//            veMoi.setMaNhanVien(nv);
//            veMoi.setNgayDat(LocalDateTime.now());
//            veMoi.setTrangThai("Khả dụng");
//            
//            // Giả sử mã vé mới được tạo tự động trong lớp DAO/Entity hoặc Control
//            // Nếu veDao.themVe(veMoi) thất bại, chúng ta KHÔNG cần rollback chỗ ngồi cũ vì bước 1 & 2 đã thành công. 
//            // Nhưng chúng ta cần cập nhật trạng thái chỗ ngồi mới sau khi thêm vé thành công.
//            if (!veDao.themVe(veMoi)) {
//                // Rollback trạng thái vé cũ về "Khả dụng"
//                veDao.capNhatTrangThaiVe(maVeCu, "Khả dụng");
//                // Rollback chỗ ngồi cũ về "Đã đặt"
//                cnDao.updateTrangThai(veCu.getMaChoNgoi().getMaChoNgoi(), "Đã đặt");
//                throw new SQLException("Lỗi thêm vé mới");
//            }
//
//            // 4. Cập nhật trạng thái chỗ ngồi mới (Sau khi chắc chắn vé mới đã được thêm)
//            if (!cnDao.updateTrangThai(cnMoi.getMaChoNgoi(), "Đã đặt")) {
//                // Thao tác này cần Rollback phức tạp hơn (Xóa vé mới, rollback vé cũ)
//                // Tuy nhiên, trong mô hình đơn giản này, ta coi đây là bước cuối cùng của việc đặt chỗ mới.
//                throw new SQLException("Lỗi cập nhật trạng thái chỗ ngồi mới");
//            }
//
//            // 5. Ghi lịch sử giao dịch Đổi vé (ID_DOI_VE = 3)
//            LichSuVe lichSu = new LichSuVe(null, ID_DOI_VE, LocalDateTime.now(), veMoi.getMaVe(), nv.getMaNhanVien());
//            lichSu.setPhiXuLy(chenhLech.abs());
//            lichSu.setMaHanhKhach(veCu.getMaHanhkhach().getMaKH());
//            lichSu.setLyDo("Đổi vé từ " + maVeCu + " sang " + veMoi.getMaVe() + ". Chênh lệch: " + chenhLech.toString());
//
//            if (!lsvDao.themLichSuVe(lichSu)) {
//                throw new SQLException("Lỗi ghi lịch sử đổi vé");
//            }
//
//            // 6. Cập nhật Thống Kê (nếu có tkdtDao)
//            // Logic cập nhật Thống Kê đã có trong nguồn [27, 28, 34, 35]
//            ThongKeDoanhThu tkHienTai = tkdtDao.getThongKeTheoNgayVaNV(LocalDate.now(), nv.getMaNhanVien());
//            if (tkHienTai != null) {
//                tkHienTai.setTongSoHoanDoi(tkHienTai.getTongSoHoanDoi() + 1);
//                tkdtDao.updateThongKe(tkHienTai);
//            } else {
//                ThongKeDoanhThu tkMoi = new ThongKeDoanhThu(null, LocalDate.now(), nv.getMaNhanVien(), BigDecimal.ZERO, 0);
//                tkMoi.setTongSoHoanDoi(1);
//                tkdtDao.insertThongKe(tkMoi);
//            }
//
//            return true;
//        } catch (Exception e) {
//            throw new Exception("Đổi vé thất bại do lỗi giao dịch: " + e.getMessage());
//        }
//    }
}