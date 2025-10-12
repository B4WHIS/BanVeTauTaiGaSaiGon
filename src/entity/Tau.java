package entity;

public class Tau {
   
    private String maTau;    
    private String tenTau;   
    private Integer soToa;       


    public Tau() {
    	
    }

    public Tau( String maTau, String tenTau, int soToa) {      
        setMaTau(maTau);
        setTenTau(tenTau);
        setSoToa(soToa);
    }


    public Tau(String tenTau, int soToa) {
        setTenTau(tenTau);
        setSoToa(soToa);
    }

    public String getMaTau() {
        return maTau;
    }

    public void setMaTau(String maTau) {
        if (maTau == null || maTau.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã tàu không được để trống");
        }
        if (maTau.length() > 6) { 
            throw new IllegalArgumentException("Mã tàu không được vượt quá 6 ký tự.");
        }
        this.maTau = maTau;
    }

    public String getTenTau() {
        return tenTau;
    }

    public void setTenTau(String tenTau) {
        if (tenTau == null || tenTau.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên tàu không được để trống");
        }
        if (tenTau.length() > 100) {
            throw new IllegalArgumentException("Tên tàu không được vượt quá 100 ký tự");
        }
        this.tenTau = tenTau;
    }

    public Integer getSoToa() {
        return soToa;
    }

    public void setSoToa(int soToa) {
        if (soToa <= 0) {
            throw new IllegalArgumentException("Số lượng toa phải lớn hơn 0");
        }
        this.soToa = soToa;
    }

    @Override
    public String toString() {
        return "Tau [ maTau=" + maTau + ", tenTau=" + tenTau + ", soToa=" + soToa + "]";
    }
}
