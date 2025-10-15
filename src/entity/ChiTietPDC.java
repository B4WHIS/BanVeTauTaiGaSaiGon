package entity;
//DONE
public class ChiTietPDC {
	private PhieuDatCho maPhieuDatCho;
	private ChoNgoi maChoNgoi;
	private ChuyenTau maChuyenTau;
	
	public ChiTietPDC() {}

	public ChiTietPDC(PhieuDatCho maPhieuDatCho, ChoNgoi maChoNgoi, ChuyenTau maChuyenTau) {
		super();
		setMaPhieuDatCho(maPhieuDatCho);
		setMaChoNgoi(maChoNgoi);
		setMaChuyenTau(maChuyenTau);
	}
	
	public PhieuDatCho getMaPhieuDatCho() {
		return maPhieuDatCho;
	}
    public void setMaPhieuDatCho(PhieuDatCho maPhieuDatCho) {
        if (maPhieuDatCho == null) {
            throw new IllegalArgumentException("Chi tiết phải thuộc về một Phiếu đặt chỗ.");
        }
        this.maPhieuDatCho = maPhieuDatCho;
    }
    
    public ChoNgoi getMaChoNgoi() {
		return maChoNgoi;
	}
    public void setMaChoNgoi(ChoNgoi maChoNgoi) {
        if (maChoNgoi == null) {
            throw new IllegalArgumentException("Chi tiết phải gắn với Chỗ ngồi.");
        }
        this.maChoNgoi = maChoNgoi;
    }

    public void setMaChuyenTau(ChuyenTau maChuyenTau) {
        if (maChuyenTau == null) {
            throw new IllegalArgumentException("Chi tiết phải gắn với Chuyến tàu.");
        }
        this.maChuyenTau = maChuyenTau;
    }
	
	public ChuyenTau getMaChuyenTau() {
		return maChuyenTau;
	}
	
	
}
