package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Ve {
    private String maVe;
    private LocalDateTime ngayDat;
    private TrangThaiVe trangThai; // Sử dụng enum
    private BigDecimal giaVeGoc;
    private BigDecimal giaThanhToan;
    private ChoNgoi maChoNgoi;
    private ChuyenTau maChuyenTau;
    private HanhKhach maHanhkhach;
    private KhuyenMai maKhuyenMai;
    private NhanVien maNhanVien;

    // ===== Enum trạng thái vé =====
    public enum TrangThaiVe {
        CON_KHA_DUNG("Còn khả dụng"),
        DA_SU_DUNG("Đã sử dụng"),
        HET_HAN("Hết hạn"),
        DA_HUY("Đã hủy"),
        DA_DOI("Đã đổi");

        private final String moTa;

        TrangThaiVe(String moTa) {
            this.moTa = moTa;
        }

        public String getMoTa() {
            return moTa;
        }

        @Override
        public String toString() {
            return moTa;
        }
    }

    // ===== Constructor =====
    public Ve(String maVe, LocalDateTime ngayDat, TrangThaiVe trangThai,
              BigDecimal giaVeGoc, BigDecimal giaThanhToan,
              ChoNgoi maChoNgoi, ChuyenTau maChuyenTau,
              HanhKhach maHanhkhach, KhuyenMai maKhuyenMai, NhanVien maNhanVien) throws Exception {

        setMaVe(maVe);
        setNgayDat(ngayDat);
        setTrangThai(trangThai);
        setGiaVeGoc(giaVeGoc);
        setGiaThanhToan(giaThanhToan);
        setMaChoNgoi(maChoNgoi);
        setMaChuyenTau(maChuyenTau);
        setMaHanhkhach(maHanhkhach);
        setMaKhuyenMai(maKhuyenMai);
        setMaNhanVien(maNhanVien);
    }

    public Ve() {
        super();
    }

    // ===== Getter & Setter =====
    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        if (maVe == null || maVe.trim().isEmpty())
            throw new IllegalArgumentException("Mã vé không được rỗng (NOT NULL)");
        this.maVe = maVe;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDateTime ngayDat) {
        if (ngayDat == null)
            throw new IllegalArgumentException("Ngày đặt không được rỗng (NOT NULL)");
        this.ngayDat = ngayDat;
    }

    public TrangThaiVe getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiVe trangThai) {
        if (trangThai == null)
            throw new IllegalArgumentException("Trạng thái vé không được rỗng!");
        this.trangThai = trangThai;
    }

    public BigDecimal getGiaVeGoc() {
        return giaVeGoc;
    }

    public void setGiaVeGoc(BigDecimal giaVeGoc) {
        this.giaVeGoc = giaVeGoc;
    }

    public BigDecimal getGiaThanhToan() {
        return giaThanhToan;
    }

    public void setGiaThanhToan(BigDecimal giaThanhToan) {
        this.giaThanhToan = giaThanhToan;
    }

    public ChoNgoi getMaChoNgoi() {
        return maChoNgoi;
    }

    public void setMaChoNgoi(ChoNgoi maChoNgoi) {
        if (maChoNgoi == null)
            throw new IllegalArgumentException("Mã chỗ ngồi không được rỗng (FK NOT NULL)");
        this.maChoNgoi = maChoNgoi;
    }

    public ChuyenTau getMaChuyenTau() {
        return maChuyenTau;
    }

    public void setMaChuyenTau(ChuyenTau maChuyenTau) {
        if (maChuyenTau == null)
            throw new IllegalArgumentException("Mã chuyến tàu không được rỗng (FK NOT NULL)");
        this.maChuyenTau = maChuyenTau;
    }

    public HanhKhach getMaHanhkhach() {
        return maHanhkhach;
    }

    public void setMaHanhkhach(HanhKhach maHanhkhach) {
        if (maHanhkhach == null)
            throw new IllegalArgumentException("Mã hành khách không được rỗng (FK NOT NULL)");
        this.maHanhkhach = maHanhkhach;
    }

    public KhuyenMai getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(KhuyenMai maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public NhanVien getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(NhanVien maNhanVien) throws Exception {
        if (maNhanVien == null)
            throw new IllegalArgumentException("Mã nhân viên không được để trống");
        this.maNhanVien = maNhanVien;
    }

    // ===== toString() =====
    @Override
    public String toString() {
        return "Ve [" +
                "maVe=" + maVe +
                ", ngayDat=" + ngayDat +
                ", trangThai=" + (trangThai != null ? trangThai.getMoTa() : "null") +
                ", giaVeGoc=" + giaVeGoc +
                ", giaThanhToan=" + giaThanhToan +
                ", maChoNgoi=" + (maChoNgoi != null ? maChoNgoi.toString() : "null") +
                ", maChuyenTau=" + (maChuyenTau != null ? maChuyenTau.toString() : "null") +
                ", maHanhkhach=" + (maHanhkhach != null ? maHanhkhach.toString() : "null") +
                ", maKhuyenMai=" + (maKhuyenMai != null ? maKhuyenMai.toString() : "null") +
                ", maNhanVien=" + (maNhanVien != null ? maNhanVien.toString() : "null") +
                "]";
    }
}
