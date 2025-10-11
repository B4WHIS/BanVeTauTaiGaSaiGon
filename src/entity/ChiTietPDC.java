package entity;

public class ChiTietPDC {
	private PhieuDatCho phieuDatCho;
	private ChoNgoi choNgoi;
	private ChuyenTau chuyenTau;
	
	public ChiTietPDC() {}
	    
	public ChiTietPDC(PhieuDatCho phieuDatCho, ChoNgoi choNgoi, ChuyenTau chuyenTau) {
	    setPhieuDatCho(phieuDatCho);
	    setChoNgoi(choNgoi);
	    setChuyenTau(chuyenTau);
	}
	
	public PhieuDatCho getPhieuDatCho() {
		return phieuDatCho;
	}
    public void setPhieuDatCho(PhieuDatCho phieuDatCho) {
        if (phieuDatCho == null) {
            throw new IllegalArgumentException("Chi tiết phải thuộc về một Phiếu đặt chỗ.");
        }
        this.phieuDatCho = phieuDatCho;
    }
    
    public ChoNgoi getChoNgoi() {
		return choNgoi;
	}
    public void setChoNgoi(ChoNgoi choNgoi) {
        if (choNgoi == null) {
            throw new IllegalArgumentException("Chi tiết phải gắn với Chỗ ngồi.");
        }
        this.choNgoi = choNgoi;
    }

    public void setChuyenTau(ChuyenTau chuyenTau) {
        if (chuyenTau == null) {
            throw new IllegalArgumentException("Chi tiết phải gắn với Chuyến tàu.");
        }
        this.chuyenTau = chuyenTau;
    }
	
	public ChuyenTau getChuyenTau() {
		return chuyenTau;
	}
	
    @Override
    public String toString() {
        return "ChiTietPhieuDatCho"
               + "[maPhieuDatCho=" + (phieuDatCho != null ? phieuDatCho.getMaPhieuDatCho() : "null")
               + ", maChoNgoi=" + (choNgoi != null ? choNgoi.getMaChoNgoi() : "null")
               + ", maChuyenTau=" + (chuyenTau != null ? chuyenTau.getMaChuyenTau() : "null") + "]";
    }
}
