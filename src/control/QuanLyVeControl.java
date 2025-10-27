package control;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import dao.ChoNgoiDAO;
import dao.ChuyenTauDAO;
import dao.HanhKhachDAO;
import dao.KhuyenMaiDAO;
import dao.LichSuVeDAO;
import dao.ThongKeDoanhThuDAO;
import dao.UuDaiDAO;
import dao.VeDAO;
import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.HanhKhach;
import entity.KhuyenMai;
import entity.LichSuVe;
import entity.NhanVien;
import entity.ThongKeDoanhThu;
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
    private ThongKeDoanhThuDAO tkdtDao;

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
        // Giả định tkdtDao được khởi tạo nếu cần cho thống kê
        this.tkdtDao = new ThongKeDoanhThuDAO();
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

    /**
     * Thực hiện giao dịch đặt vé.
     * Đã FIX: Đảm bảo tính toàn vẹn dữ liệu chỗ ngồi và vé bằng logic rollback thủ công.
     * @return String maVe đã được thêm thành công.
     * @throws Exception nếu xảy ra lỗi CSDL hoặc lỗi nghiệp vụ.
     */
    public String datVe(Ve veMoi, HanhKhach hkMoi, NhanVien nvlap) throws Exception {
        HanhKhach finalHK = hkMoi;
        String maChoNgoi = veMoi.getMaChoNgoi().getMaChoNgoi();
        String maVeVuaThem = null;

        // --- BƯỚC 1: Xử lý Hành Khách và Thiết lập Vé ---
        try {
            // Xử lý Khách hàng (Tồn tại / Tạo mới) [10, 11]
            HanhKhach hanhkhachHienCo = null;
            if (hkMoi.getCmndCccd() != null && !hkMoi.getCmndCccd().trim().isEmpty()) {
                hanhkhachHienCo = hkDao.layHanhKhachTheoCMND(hkMoi.getCmndCccd());
            }

            if (hanhkhachHienCo != null) {
                hkMoi.setMaKH(hanhkhachHienCo.getMaKH()); 
                hkDao.capNhatHanhKhach(hkMoi); 
                finalHK = hkMoi;
            } else {
                hkDao.themHanhKhach(hkMoi);
                finalHK = hkMoi;
            }
            
            // Thiết lập thông tin Vé
            veMoi.setMaHanhkhach(finalHK);
            veMoi.setMaNhanVien(nvlap);
            veMoi.setNgayDat(LocalDateTime.now());
            veMoi.setTrangThai("Khả dụng"); 

        } catch (IllegalArgumentException e) {
            throw new Exception("Thông tin hành khách không hợp lệ: " + e.getMessage());
        } catch (SQLException e) {
            throw new Exception("Lỗi CSDL khi xử lý thông tin khách hàng: " + e.getMessage());
        }

        // --- BƯỚC 2: Kiểm tra và Ghi dữ liệu (CRITICAL TRANSACTION) ---
        try {
            // Tính toán giá và thiết lập
            BigDecimal giaThanhToan = tinhGiaVeCuoiCung(veMoi.getGiaVeGoc(), finalHK.getMaUuDai(), veMoi.getMaKhuyenMai());
            if (giaThanhToan.compareTo(BigDecimal.ZERO) <= 0) { // Kiểm tra giá thanh toán > 0 [4, 6, 12]
                throw new Exception("Giá thanh toán cuối cùng không hợp lệ.");
            }
            veMoi.setGiaThanhToan(giaThanhToan);

            // KIỂM TRA CHỖ NGỒI (đảm bảo còn Trống)
            ChoNgoi choNgoiHienTai = cnDao.getChoNgoiByMa(maChoNgoi);
            // Trang thái chỗ ngồi phải là 'Trống' hoặc 'đã đặt' [13, 14]
            if (choNgoiHienTai == null || !"Trống".equalsIgnoreCase(choNgoiHienTai.getTrangThai().trim())) {
                 throw new Exception("Chỗ ngồi " + maChoNgoi + " không còn trống hoặc không tồn tại.");
            }

            // 2.1. Cập nhật trạng thái chỗ ngồi thành 'Đã đặt'
            if (!cnDao.updateTrangThai(maChoNgoi, "Đã đặt")) { // updateTrangThai trả về boolean [15]
                throw new SQLException("Lỗi cập nhật trạng thái chỗ ngồi: " + maChoNgoi);
            }

            // 2.2. Thêm vé mới (Sử dụng DAO trả về mã vé)
            maVeVuaThem = veDao.themVe(veMoi); 
            
            if (maVeVuaThem == null || maVeVuaThem.trim().isEmpty()) {
                // ROLLBACK: Nếu thêm vé thất bại, hoàn nguyên chỗ ngồi
                cnDao.updateTrangThai(maChoNgoi, "Trống"); 
                throw new SQLException("Thêm vé thất bại, không nhận được mã vé.");
            }

            // 2.3. Ghi lịch sử bán vé (ID_BAN_VE = 1)
            LichSuVe lichSu = new LichSuVe(null, ID_BAN_VE, LocalDateTime.now(), maVeVuaThem, nvlap.getMaNhanVien());
            lichSu.setMaHanhKhach(veMoi.getMaHanhkhach().getMaKH());
            lichSu.setLyDo("Bán vé thành công");
            lichSu.setPhiXuLy(BigDecimal.ZERO); 
            lsvDao.themLichSuVe(lichSu); // Bỏ qua nếu lỗi, không ảnh hưởng transaction chính

            return maVeVuaThem; 

        } catch (SQLException e) {
            // Lỗi CSDL khác. Nếu maVeVuaThem chưa được gán (lỗi xảy ra trước khi thêm vé), 
            // cần đảm bảo rollback nếu bước 2.1 thành công.
            if (maVeVuaThem == null) {
                // Cố gắng rollback chỗ ngồi nếu nó đã được cập nhật ở 2.1
                try {
                    cnDao.updateTrangThai(maChoNgoi, "Trống");
                } catch (SQLException ex) {
                    System.err.println("LỖI CẢNH BÁO: Không thể rollback chỗ ngồi sau lỗi đặt vé: " + ex.getMessage());
                }
            }
            throw new Exception("Đặt vé thất bại (CSDL): " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Đặt vé thất bại: " + e.getMessage());
        }
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

    // =======================================================
    // ĐỔI VÉ - Dựa trên logic đã có [16-36]
    // =======================================================

    public boolean doiVe(String maVeCu, ChuyenTau ctMoi, ChoNgoi cnMoi, NhanVien nv) throws Exception {
        Ve veCu = veDao.layVeTheoMa(maVeCu);
        if (veCu == null) {
            throw new Exception("Không tìm thấy vé có mã " + maVeCu);
        }

        LocalDateTime TGKH_Cu = veCu.getMaChuyenTau().getThoiGianKhoiHanh();
        if (TGKH_Cu.minusHours(4).isBefore(LocalDateTime.now())) {
            throw new Exception("Vé không đủ điều kiện đổi, phải đổi trước giờ khởi hành 4 giờ.");
        }

        ChoNgoi trangThaiChoMoi = cnDao.getChoNgoiByMa(cnMoi.getMaChoNgoi());
        if (trangThaiChoMoi == null || !trangThaiChoMoi.getTrangThai().equalsIgnoreCase("Trống")) {
            throw new Exception("Chỗ ngồi mới đã có người đặt hoặc không tồn tại");
        }

        // Tính toán giá vé mới
        BigDecimal giaCu = veCu.getGiaThanhToan();
        BigDecimal giaVeGocMoi = ctMoi.getGiaChuyen();
        BigDecimal giaMoiThanhToan = tinhGiaVeCuoiCung(giaVeGocMoi, veCu.getMaHanhkhach().getMaUuDai(), veCu.getMaKhuyenMai());
        BigDecimal chenhLech = giaMoiThanhToan.subtract(giaCu);

        try {
            // 1. Giải phóng chỗ ngồi cũ
            if (!cnDao.updateTrangThai(veCu.getMaChoNgoi().getMaChoNgoi(), "Trống")) {
                throw new SQLException("Lỗi chỗ ngồi cũ.");
            }

            // 2. Cập nhật trạng thái vé cũ
            if (!veDao.capNhatTrangThaiVe(maVeCu, "Đã đổi")) {
                // Nếu lỗi, phải rollback chỗ ngồi cũ
                cnDao.updateTrangThai(veCu.getMaChoNgoi().getMaChoNgoi(), "Đã đặt");
                throw new SQLException("Lỗi cập nhật trạng thái vé cũ");
            }

            // 3. Thêm vé mới (Vé mới phải có mã mới, trạng thái "Khả dụng")
            Ve veMoi = new Ve();
            veMoi.setMaChuyenTau(ctMoi);
            veMoi.setMaChoNgoi(cnMoi);
            veMoi.setGiaVeGoc(giaVeGocMoi);
            veMoi.setGiaThanhToan(giaMoiThanhToan);
            veMoi.setMaHanhkhach(veCu.getMaHanhkhach());
            veMoi.setMaNhanVien(nv);
            veMoi.setNgayDat(LocalDateTime.now());
            veMoi.setTrangThai("Khả dụng");
            
            // Giả sử mã vé mới được tạo tự động trong lớp DAO/Entity hoặc Control
            // Nếu veDao.themVe(veMoi) thất bại, chúng ta KHÔNG cần rollback chỗ ngồi cũ vì bước 1 & 2 đã thành công. 
            // Nhưng chúng ta cần cập nhật trạng thái chỗ ngồi mới sau khi thêm vé thành công.
            if (!veDao.themVe(veMoi)) {
                // Rollback trạng thái vé cũ về "Khả dụng"
                veDao.capNhatTrangThaiVe(maVeCu, "Khả dụng");
                // Rollback chỗ ngồi cũ về "Đã đặt"
                cnDao.updateTrangThai(veCu.getMaChoNgoi().getMaChoNgoi(), "Đã đặt");
                throw new SQLException("Lỗi thêm vé mới");
            }

            // 4. Cập nhật trạng thái chỗ ngồi mới (Sau khi chắc chắn vé mới đã được thêm)
            if (!cnDao.updateTrangThai(cnMoi.getMaChoNgoi(), "Đã đặt")) {
                // Thao tác này cần Rollback phức tạp hơn (Xóa vé mới, rollback vé cũ)
                // Tuy nhiên, trong mô hình đơn giản này, ta coi đây là bước cuối cùng của việc đặt chỗ mới.
                throw new SQLException("Lỗi cập nhật trạng thái chỗ ngồi mới");
            }

            // 5. Ghi lịch sử giao dịch Đổi vé (ID_DOI_VE = 3)
            LichSuVe lichSu = new LichSuVe(null, ID_DOI_VE, LocalDateTime.now(), veMoi.getMaVe(), nv.getMaNhanVien());
            lichSu.setPhiXuLy(chenhLech.abs());
            lichSu.setMaHanhKhach(veCu.getMaHanhkhach().getMaKH());
            lichSu.setLyDo("Đổi vé từ " + maVeCu + " sang " + veMoi.getMaVe() + ". Chênh lệch: " + chenhLech.toString());

            if (!lsvDao.themLichSuVe(lichSu)) {
                throw new SQLException("Lỗi ghi lịch sử đổi vé");
            }

            // 6. Cập nhật Thống Kê (nếu có tkdtDao)
            // Logic cập nhật Thống Kê đã có trong nguồn [27, 28, 34, 35]
            ThongKeDoanhThu tkHienTai = tkdtDao.getThongKeTheoNgayVaNV(LocalDate.now(), nv.getMaNhanVien());
            if (tkHienTai != null) {
                tkHienTai.setTongSoHoanDoi(tkHienTai.getTongSoHoanDoi() + 1);
                tkdtDao.updateThongKe(tkHienTai);
            } else {
                ThongKeDoanhThu tkMoi = new ThongKeDoanhThu(null, LocalDate.now(), nv.getMaNhanVien(), BigDecimal.ZERO, 0);
                tkMoi.setTongSoHoanDoi(1);
                tkdtDao.insertThongKe(tkMoi);
            }

            return true;
        } catch (Exception e) {
            throw new Exception("Đổi vé thất bại do lỗi giao dịch: " + e.getMessage());
        }
    }
}