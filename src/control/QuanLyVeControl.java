package control;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
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
    private ThongKeDoanhThuDAO tkdtDao;
    private ChoNgoiDAO choNgoiDAO = new ChoNgoiDAO();
	private GaDAO gaDao;
	private TauDAO tauDao;
	private ChoNgoiDAO loaiGheDao = new ChoNgoiDAO(); 
	private NhanVien nvlap = new NhanVien();
	
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
        this.tkdtDao = new ThongKeDoanhThuDAO();
        this.tauDao = new TauDAO();
        this.gaDao = new GaDAO();
        this.nvlap = new NhanVien();
    }

    public BigDecimal tinhGiaVeCuoiCung(BigDecimal giaVeGoc, String maUuDai, KhuyenMai kmDinhKem) throws Exception {
        if (giaVeGoc == null || giaVeGoc.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Giá vé gốc không hợp lệ.");
        }

        BigDecimal giaThanhToan = giaVeGoc;

        // 1. Áp dụng Ưu đãi
        UuDai ud = udDao.timUuDaiTheoMa(maUuDai);
        if (ud != null) {
            BigDecimal mucGiamPT = ud.getMucGiamGia().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            BigDecimal soTienGiam = giaVeGoc.multiply(mucGiamPT);
            giaThanhToan = giaVeGoc.subtract(soTienGiam);
        }

        // 2. Áp dụng Khuyến mãi (nếu hợp lệ)
        if (kmDinhKem != null && kmDinhKem.getMaKhuyenMai() != null) {
            KhuyenMai km = kmDao.TimKhuyenMaiTheoMa(kmDinhKem.getMaKhuyenMai());
            if (km != null && km.getNgayKetThuc().isAfter(LocalDate.now())) {
                BigDecimal giamKM = km.getMucGiamGia().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                giaThanhToan = giaThanhToan.subtract(giaThanhToan.multiply(giamKM));
            }
        }

        return giaThanhToan.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Thực hiện giao dịch đặt vé.
     * Đã FIX: Đảm bảo tính toàn vẹn dữ liệu chỗ ngồi và vé bằng logic rollback thủ công.
     * @return String maVe đã được thêm thành công.
     * @throws Exception nếu xảy ra lỗi CSDL hoặc lỗi nghiệp vụ.
     */
    public String datVe(Ve veMoi, HanhKhach hkMoi, NhanVien nvlap) throws Exception {
        if (nvlap == null || nvlap.getMaNhanVien() == null || nvlap.getMaNhanVien().trim().isEmpty()) {
            throw new IllegalArgumentException("Nhân viên lập không hợp lệ.");
        }

        String maChoNgoi = veMoi.getMaChoNgoi().getMaChoNgoi();
        String maChuyenTau = veMoi.getMaChuyenTau().getMaChuyenTau();

        // KIỂM TRA TRẠNG THÁI ĐỘNG: ĐÃ CÓ VÉ NÀO ĐẶT CHỖ NÀY TRONG CHUYẾN NÀY CHƯA?
        if (veDao.kiemTraChoNgoiDaDuocDat(maChoNgoi, maChuyenTau)) {
            throw new Exception("Chỗ ngồi " + maChoNgoi + " không còn trống.");
        }

        Connection conn = null;
        String maVeVuaThem = null;

        try {
            conn = connectDB.getConnection();
            conn.setAutoCommit(false);

            veMoi.setTrangThai("Đã đặt");
            veMoi.setMaNhanVien(nvlap);

            if (!cnDao.updateTrangThai(maChoNgoi, "Đã đặt", conn)) {
                throw new SQLException("Cập nhật trạng thái chỗ ngồi thất bại.");
            }

            maVeVuaThem = veDao.themVe(veMoi, conn);
            if (maVeVuaThem == null || maVeVuaThem.trim().isEmpty()) {
                throw new SQLException("Thêm vé thất bại.");
            }

            LichSuVe lichSu = new LichSuVe(null, ID_BAN_VE, LocalDateTime.now(), maVeVuaThem, nvlap.getMaNhanVien());
            lichSu.setMaHanhKhach(veMoi.getMaHanhkhach().getMaKH());
            lichSu.setLyDo("Bán vé thành công");
            lichSu.setPhiXuLy(BigDecimal.ZERO);
            lsvDao.themLichSuVe(lichSu, conn);

            conn.commit();
            return maVeVuaThem;

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
            throw new Exception("Đặt vé thất bại: " + e.getMessage());
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
        }
    }
    
    public String getTenGaDen(String maLichTrinh) throws SQLException { return gaDao.getTenGaDenByMaLichTrinh(maLichTrinh); }
    public String getTenGaDi(String maLichTrinh) throws SQLException { return gaDao.getTenGaDiByMaLichTrinh(maLichTrinh); }
    public String getTenTau(String maTau) throws SQLException {
        Tau tau = tauDao.getTauByMaTau(maTau);
        return (tau != null) ? tau.getTenTau() : maTau;
    }
    public boolean capNhatTrangThaiChoNgoi(String maChoNgoi, String trangThai) throws SQLException {
        return cnDao.updateTrangThai(maChoNgoi, trangThai);
    }
    
    private BigDecimal tinhPhiHuy(BigDecimal giaGoc, LocalDateTime thoiGianKhoiHanh) {
        LocalDateTime thoiGianNOW = LocalDateTime.now();
        Duration duration = Duration.between(thoiGianNOW, thoiGianKhoiHanh); // [17]
        long gioConLai = duration.toHours(); // [17]
        
        if (gioConLai < 4) { // Điều kiện hủy tối thiểu 4 giờ trước khởi hành [17]
            return null;
        }
        
        BigDecimal tiLePhi;
        if (gioConLai >= 24) {
            tiLePhi = new BigDecimal("0.10"); // 10% [14]
        } else if (gioConLai >= 12) {
            tiLePhi = new BigDecimal("0.20"); // 20% [14]
        } else {
            tiLePhi = new BigDecimal("0.30"); // 30% [14]
        }
        
        // Tính phí và làm tròn đến 2 chữ số thập phân [14]
        return giaGoc.multiply(tiLePhi).setScale(2, BigDecimal.ROUND_HALF_UP); 
    }


    public LichSuVe xuLyHuyVe(String maVe, String liDoHuy, NhanVien nvThucHien) throws Exception {
        Ve ve = veDao.layVeTheoMa(maVe); // [15]
        
        // 1. Kiểm tra vé
        if (ve == null) {
            throw new Exception("Không tìm thấy vé có mã: " + maVe); // [15]
        }
        if (ve.getTrangThai().equalsIgnoreCase("Đã hủy") || ve.getTrangThai().equalsIgnoreCase("Đã sử dụng")) {
            throw new Exception("Vé đã được hủy hoặc đã sử dụng"); // [15]
        }
        
        // 2. Lấy thông tin chuyến tàu
        ChuyenTau chuyenTau =
            ctDao.getChuyenTauByMaChuyenTau(ve.getMaChuyenTau().getMaChuyenTau()); // [15]
        if (chuyenTau == null) {
            throw new Exception("Không tìm thấy thông tin chuyến tàu liên quan."); // [16]
        }
        
        // 3. Tính phí hủy và tiền hoàn lại
        BigDecimal giaVeGoc = ve.getGiaVeGoc(); // [16]
        LocalDateTime thoiGianKhoiHanh = chuyenTau.getThoiGianKhoiHanh(); // [16]
        
        // Tinh phi huy (phải được định nghĩa trong class này) [14, 17]
        BigDecimal phiHuy = tinhPhiHuy(giaVeGoc, thoiGianKhoiHanh); // [16] 
        
        if (phiHuy == null) {
            // Điều kiện hủy tối thiểu 4 giờ trước khởi hành [16, 17]
            throw new Exception("Vé không đủ điều kiện hủy (phải hủy trước giờ khởi hành ít nhất 4 giờ)."); // [16]
        }
        
        BigDecimal tienHoanLai = ve.getGiaThanhToan().subtract(phiHuy); // Tính tiền hoàn lại [16]
        if (tienHoanLai.compareTo(BigDecimal.ZERO) < 0) {
            tienHoanLai = BigDecimal.ZERO; // Đảm bảo tiền hoàn không âm [16]
        }

        // 4. THỰC HIỆN GIAO DỊCH (Update trạng thái Vé và Chỗ Ngồi) [18]
        try {
            // Cập nhật trạng thái Vé thành "Đã hủy"
            if (!veDao.capNhatTrangThaiVe(maVe, "Đã hủy")) { // [18]
                throw new SQLException("Lỗi cập nhật trạng thái vé");
            }
            
            // Cập nhật trạng thái Chỗ Ngồi thành "Trống"
            String maChoNgoi = ve.getMaChoNgoi().getMaChoNgoi(); // [18]
            if (!cnDao.updateTrangThai(maChoNgoi, "Trống")) { // [18]
                throw new SQLException("Lỗi cập nhật trạng thái chỗ ngồi");
            }
            
            // Ghi lại lịch sử giao dịch Hủy vé (ID_HUY_VE = 2) [19, 20]
            LichSuVe lichSu = new LichSuVe(null, 2, LocalDateTime.now(), maVe, // 2 là ID_HUY_VE
                nvThucHien.getMaNhanVien()); // [20]
            
            lichSu.setLyDo(liDoHuy + " | tiền hoàn lại: " + tienHoanLai.toString()); // [20]
            lichSu.setPhiXuLy(phiHuy); // [20]
            lichSu.setMaHanhKhach(ve.getMaHanhkhach().getMaKH()); // [20]
            
            if (!lsvDao.themLichSuVe(lichSu)) { // [20]
                throw new SQLException("Lỗi ghi lịch sử hủy vé");
            }
            
            return lichSu; // Trả về đối tượng lịch sử để lấy Phi và Hoàn tiền [10, 20]
        } catch (SQLException e) {
            // Ném lỗi CSDL lên tầng trên
            throw new Exception("Lỗi CSDL khi hủy vé: " + e.getMessage()); // [20]
        }
    }

	public String getTenLoaiGhe(int IDloaiGhe) throws SQLException {
	    return loaiGheDao.getTenLoaiGheByID(IDloaiGhe);
	}
    
}