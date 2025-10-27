package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDon {
    private String maHoaDon;
    private LocalDateTime ngayLap;
    private HanhKhach maHanhKhach;
    private NhanVien maNhanVien;
    private BigDecimal tongTien;
    private List<ChiTietHoaDon> danhSachChiTiet = new ArrayList<>();

    public HoaDon() {
        this.tongTien = BigDecimal.ZERO;
        this.danhSachChiTiet = new ArrayList<>();
    }

    public HoaDon(String maHoaDon, LocalDateTime ngayLap, HanhKhach maHanhKhach, NhanVien maNhanVien,
                  List<ChiTietHoaDon> danhSachChiTiet) {
        setMaHoaDon(maHoaDon);
        setNgayLap(ngayLap);
        setMaHanhKhach(maHanhKhach);
        setMaNhanVien(maNhanVien);
        setDanhSachChiTiet(danhSachChiTiet);
    }

    // --- GETTER / SETTER ---

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        if (maHoaDon == null || maHoaDon.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã hóa đơn không được rỗng.");
        }
        this.maHoaDon = maHoaDon;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        if (ngayLap == null) {
            throw new IllegalArgumentException("Ngày lập không được rỗng.");
        }
        if (ngayLap.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Ngày lập không được lớn hơn ngày hiện tại.");
        }
        this.ngayLap = ngayLap;
    }

    public HanhKhach getMaHanhKhach() {
        return maHanhKhach;
    }

    public void setMaHanhKhach(HanhKhach maHanhKhach) {
        if (maHanhKhach == null) {
            throw new IllegalArgumentException("Thông tin khách hàng không được rỗng.");
        }
        this.maHanhKhach = maHanhKhach;
    }

    public NhanVien getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(NhanVien maNhanVien) {
        if (maNhanVien == null) {
            throw new IllegalArgumentException("Thông tin nhân viên không được rỗng.");
        }
        this.maNhanVien = maNhanVien;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    // CHO PHÉP SET THỦ CÔNG (có VAT, khuyến mãi, v.v.)
    public void setTongTien(BigDecimal tongTien) {
        if (tongTien == null || tongTien.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tổng tiền phải lớn hơn 0.");
        }
        this.tongTien = tongTien;
    }

    // TỰ ĐỘNG TÍNH TỪ DANH SÁCH CHI TIẾT
    public void capNhatTongTien() {
        this.tongTien = tinhTongTien();
    }

    public List<ChiTietHoaDon> getDanhSachChiTiet() {
        if (danhSachChiTiet == null) {
            danhSachChiTiet = new ArrayList<>();
        }
        return danhSachChiTiet;
    }

    public void setDanhSachChiTiet(List<ChiTietHoaDon> danhSachChiTiet) {
        this.danhSachChiTiet = (danhSachChiTiet != null) ? danhSachChiTiet : new ArrayList<>();
        capNhatTongTien(); // Tự động cập nhật
    }

    // CHÍNH XÁC 100% – DÙNG BigDecimal
    public BigDecimal tinhTongTien() {
        return getDanhSachChiTiet().stream()
                .map(ChiTietHoaDon::getDonGia)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return String.format("HoaDon[maHD=%s, ngay=%s, khach=%s, nv=%s, tongTien=%s, chiTiet=%d]",
                maHoaDon, ngayLap, maHanhKhach.getMaKH(), maNhanVien.getMaNhanVien(),
                tongTien, danhSachChiTiet.size());
    }
}