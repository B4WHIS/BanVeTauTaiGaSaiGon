package entity;

import java.time.LocalDate;

public class KhuyenMai {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private int mucGiamGia;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String dieuKien;

    public KhuyenMai() {}

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, int mucGiamGia,
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

    public int getMucGiamGia() {
        return mucGiamGia;
    }

    public void setMucGiamGia(int mucGiamGia) {
        if (mucGiamGia < 0 || mucGiamGia > 100)
            throw new IllegalArgumentException("Mức giảm giá phải từ 0 đến 100%");
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
        if (ngayKetThuc == null || ngayKetThuc.isBefore(ngayBatDau) || ngayKetThuc.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Ngày kết thúc không hợp lệ!");
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getDieuKien() {
        return dieuKien;
    }

    public void setDieuKien(String dieuKien) {
        if (dieuKien == null || dieuKien.trim().isEmpty())
            throw new IllegalArgumentException("Điều kiện áp dụng không được rỗng!");
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
