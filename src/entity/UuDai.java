package entity;

import java.math.BigDecimal;

public class UuDai {
    private String maUuDai;
    private int IDloaiUuDai; 
    private BigDecimal mucGiamGia;
    private String dieuKienApDung;

    public UuDai() {}
    
    public UuDai(String maUuDai, int loaiUuDai, BigDecimal mucGiamGia, String dieuKienApDung) {
        setMaUuDai(maUuDai);
        setIDloaiUuDai(loaiUuDai);
        setMucGiamGia(mucGiamGia);
        setDieuKienApDung(dieuKienApDung);
    }

    public String getMaUuDai() { return maUuDai; }
    public void setMaUuDai(String maUuDai) { 
        if (maUuDai == null || maUuDai.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã ưu đãi không được để trống.");
        }
        this.maUuDai = maUuDai; 
    }

    public int getIDloaiUuDai() {
    	return IDloaiUuDai;
    }
    
    public void setIDloaiUuDai(int IDloaiUuDai) { 
        if (IDloaiUuDai <= 0) {
            throw new IllegalArgumentException("ID loại ưu đãi phải dương.");
        }
        this.IDloaiUuDai = IDloaiUuDai; 
    }

    public BigDecimal getMucGiamGia() {
        return mucGiamGia;
    }

    public void setMucGiamGia(BigDecimal mucGiamGia) {
        if (mucGiamGia == null) {
            throw new IllegalArgumentException("Mức giảm giá không được null.");
        }
        if (mucGiamGia.compareTo(BigDecimal.ZERO) < 0 || mucGiamGia.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Mức giảm giá phải nằm trong khoảng 0% đến 100%.");
        }
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
        return "UuDai [maUuDai=" + maUuDai + ", IDloaiUuDai=" + IDloaiUuDai +
               ", mucGiamGia=" + mucGiamGia + ", dieuKienApDung=" + dieuKienApDung + "]";
    }
}
