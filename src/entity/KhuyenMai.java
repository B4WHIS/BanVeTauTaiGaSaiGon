package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
//done
public class KhuyenMai {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private BigDecimal mucGiamGia;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String dieuKien;

    public KhuyenMai() {}

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, BigDecimal mucGiamGia,
                     LocalDate ngayBatDau, LocalDate ngayKetThuc, String dieuKien) {
        setMaKhuyenMai(maKhuyenMai);
        setTenKhuyenMai(tenKhuyenMai);
        setMucGiamGia(mucGiamGia);
        setNgayBatDau(ngayBatDau);
        setNgayKetThuc(ngayKetThuc);
        setDieuKien(dieuKien);
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        if (!maKhuyenMai.matches("^[A-Z0-9]{6,15}$"))
            throw new IllegalArgumentException("Mã khuyến mãi không hợp lệ!");
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        if (tenKhuyenMai == null || tenKhuyenMai.trim().isEmpty())
            throw new IllegalArgumentException("Tên khuyến mãi không được rỗng!");
        this.tenKhuyenMai = tenKhuyenMai;
    }


    public BigDecimal getMucGiamGia() {
        return mucGiamGia;
    }
    public void setMucGiamGia(BigDecimal mucGiamGia) {
        if (mucGiamGia == null || mucGiamGia.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Mức giảm giá phải từ 0% trở lên.");
        this.mucGiamGia = mucGiamGia;
    }
    
    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        if (ngayBatDau == null)
            throw new IllegalArgumentException("Ngày bắt đầu không được rỗng!");
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        if (ngayKetThuc == null || ngayKetThuc.isBefore(ngayBatDau))
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu!");
        this.ngayKetThuc = ngayKetThuc;
    }


    public String getDieuKien() {
        return dieuKien;
    }

    public void setDieuKien(String dieuKien) {
        this.dieuKien = dieuKien;
    }

    @Override
    public String toString() {
        return "KhuyenMai {" +
                "maKhuyenMai='" + maKhuyenMai + '\'' +
                ", tenKhuyenMai='" + tenKhuyenMai + '\'' +
                ", mucGiamGia=" + mucGiamGia + "%" +
                ", ngayBatDau=" + ngayBatDau +
                ", ngayKetThuc=" + ngayKetThuc +
                ", dieuKien='" + dieuKien + '\'' +
                '}';
    }
}
