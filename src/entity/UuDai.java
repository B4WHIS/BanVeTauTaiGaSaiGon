package entity;

import java.math.BigDecimal;

public class UuDai {
    private String maUuDai;
    private LoaiUuDai loaiUuDai; // Dùng enum
    private BigDecimal mucGiamGia;
    private String dieuKienApDung;

    public UuDai() {}
    public enum LoaiUuDai {
        TRE_EM("Trẻ em"),
        SINH_VIEN("Sinh viên"),
        NGUOI_CAO_TUOI("Người cao tuổi"),
        NGUOI_LON("Người lớn");

        private final String tenHienThi;

        LoaiUuDai(String tenHienThi) {
            this.tenHienThi = tenHienThi;
        }

        public String getTenHienThi() {
            return tenHienThi;
        }

        @Override
        public String toString() {
            return tenHienThi;
        }
    }

    public UuDai(String maUuDai, LoaiUuDai loaiUuDai, BigDecimal mucGiamGia, String dieuKienApDung) {
        setMaUuDai(maUuDai);
        setLoaiUuDai(loaiUuDai);
        setMucGiamGia(mucGiamGia);
        setDieuKienApDung(dieuKienApDung);
    }

    public String getMaUuDai() {
        return maUuDai;
    }

    public void setMaUuDai(String maUuDai) {
        if (maUuDai == null || !maUuDai.matches("^UD-\\d{2}$"))
            throw new IllegalArgumentException("Mã ưu đãi phải có dạng UD-XX");
        this.maUuDai = maUuDai;
    }

    public LoaiUuDai getLoaiUuDai() {
        return loaiUuDai;
    }

    public void setLoaiUuDai(LoaiUuDai loaiUuDai) {
        if (loaiUuDai == null)
            throw new IllegalArgumentException("Loại ưu đãi không được null");
        this.loaiUuDai = loaiUuDai;
    }

    public BigDecimal getMucGiamGia() {
        return mucGiamGia;
    }

    public void setMucGiamGia(BigDecimal mucGiamGia) {
        if (mucGiamGia == null || mucGiamGia.compareTo(BigDecimal.ZERO) < 0 || mucGiamGia.compareTo(new BigDecimal(100)) > 0)
            throw new IllegalArgumentException("Mức giảm giá phải từ 0 đến 100%");
        this.mucGiamGia = mucGiamGia;
    }

    public String getDieuKienApDung() {
        return dieuKienApDung;
    }

    public void setDieuKienApDung(String dieuKienApDung) {
        this.dieuKienApDung = dieuKienApDung;
    }

    @Override
    public String toString() {
        return String.format("UuDai[maUuDai=%s, loaiUuDai=%s, mucGiamGia=%s%%]", 
                maUuDai, loaiUuDai, mucGiamGia);
    }
}
