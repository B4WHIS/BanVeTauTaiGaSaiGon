package entity;

import java.math.BigDecimal;

public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private Ve ve;
    private BigDecimal donGia;

    public ChiTietHoaDon() {
        this.donGia = BigDecimal.ZERO;
    }

    public ChiTietHoaDon(HoaDon hoaDon, Ve ve, BigDecimal donGia) {
        setHoaDon(hoaDon);
        setVe(ve);
        setDonGia(donGia);
    }

    public HoaDon getHoaDon() { return hoaDon; }
    public void setHoaDon(HoaDon hoaDon) {
        if (hoaDon == null) throw new IllegalArgumentException("Hóa đơn không được null.");
        this.hoaDon = hoaDon;
    }

    public Ve getVe() { return ve; }
    public void setVe(Ve ve) {
        if (ve == null) throw new IllegalArgumentException("Vé không được null.");
        this.ve = ve;
    }

    public BigDecimal getDonGia() { return donGia; }

    public void setDonGia(BigDecimal donGia) {
        if (donGia == null || donGia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Đơn giá phải lớn hơn 0.");
        }
        this.donGia = donGia;
    }

    @Override
    public String toString() {
        return String.format("ChiTiet[maHD=%s, maVe=%s, donGia=%s]",
                hoaDon.getMaHoaDon(), ve.getMaVe(), donGia);
    }
}