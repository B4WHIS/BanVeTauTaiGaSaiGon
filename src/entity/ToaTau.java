package entity;

public class ToaTau {
    private String maToa;     
    private int soLuongCho;  
    private int soThuTu;      
    private Tau tau;          

    public ToaTau() {
    }

    public ToaTau(String maToa, int soLuongCho, int soThuTu, Tau tau) {
        setMaToa(maToa);
        setSoLuongCho(soLuongCho);
        setSoThuTu(soThuTu);
        setTau(tau);
    }

    public String getMaToa() {
        return maToa;
    }

    public void setMaToa(String maToa) {
        if (maToa == null || maToa.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã toa không được để trống");
        }
        if (maToa.length() > 6) {
            throw new IllegalArgumentException("Mã toa không được vượt quá 6 ký tự");
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
            throw new IllegalArgumentException("Số thứ tự toa phải lớn hơn 0.");
        }
        this.soThuTu = soThuTu;
    }

    public Tau getTau() {
        return tau;
    }

    public void setTau(Tau tau) {
        if (tau == null) {
            throw new IllegalArgumentException("Toa tàu phải thuộc về một tàu cụ thể.");
        }

        this.tau = tau;
    }

    @Override
    public String toString() {
        return "ToaTau [maToa=" + maToa + 
               ", soLuongCho=" + soLuongCho + 
               ", soThuTu=" + soThuTu + 
               ", maTau=" + (tau != null ? tau.getMaTau() : "Lỗi") + "]";
    }
}
