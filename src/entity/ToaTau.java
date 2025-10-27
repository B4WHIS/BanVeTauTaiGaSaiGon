package entity;

import java.math.BigDecimal;
//done
public class ToaTau {
    private String maToa;
    private int soLuongCho;
    private int soThuTu;
    private BigDecimal heSoGia; 
    private Tau tau;

    public ToaTau() {
    	
    }

    public ToaTau(String maToa, int soLuongCho, int soThuTu, BigDecimal heSoGia, Tau tau) {
        setMaToa(maToa);
        setSoLuongCho(soLuongCho);
        setSoThuTu(soThuTu);
        setHeSoGia(heSoGia);
        setTau(tau);
    }
    public ToaTau(String maToa, int soThuTuToa, int soLuongCho, String maTau) {
        setMaToa(maToa);
        setSoThuTu(soThuTuToa); 
        setSoLuongCho(soLuongCho);

        this.tau = new Tau(maTau); 
    }
    public ToaTau(String maToa) {
    	setMaToa(maToa);
    }

    public String getMaToa() {
        return maToa;
    }

    public void setMaToa(String maToa) {
        if (maToa == null || maToa.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã toa không được để trống");
        }
        if (maToa.length() > 7) {
            throw new IllegalArgumentException("Mã toa không được vượt quá 7 ký tự");
        }
        this.maToa = maToa;
    }

    public int getSoLuongCho() {
        return soLuongCho;
    }

    public void setSoLuongCho(int soLuongCho) {
        if (soLuongCho <= 0) {
            throw new IllegalArgumentException("Số lượng chỗ phải lớn hơn 0");
        }
        this.soLuongCho = soLuongCho;
    }

    public int getSoThuTu() {
        return soThuTu;
    }

    public void setSoThuTu(int soThuTu) {
        if (soThuTu <= 0) {
            throw new IllegalArgumentException("Số thứ tự toa phải lớn hơn 0");
        }
        this.soThuTu = soThuTu;
    }

    public BigDecimal getHeSoGia() {
        return heSoGia;
    }

    public void setHeSoGia(BigDecimal heSoGia) {
    	if (heSoGia == null) {
         	throw new IllegalArgumentException("Hệ số giá không được để trống");
         }
    	else  if (heSoGia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Hệ số giá phải lớn hơn 0");
        }
        this.heSoGia = heSoGia;
    }

    public Tau getTau() {
        return tau;
    }

    public void setTau(Tau tau) {
        if (tau == null) {
            throw new IllegalArgumentException("Toa tàu phải thuộc về một tàu cụ thể");
        }
        this.tau = tau;
    }

    @Override
    public String toString() {
        return "ToaTau [maToa=" + maToa +
               ", soLuongCho=" + soLuongCho +
               ", soThuTu=" + soThuTu +
               ", heSoGia=" + heSoGia +
               ", maTau=" + (tau != null ? tau.getMaTau() : "trống") + "]";
    }
}
