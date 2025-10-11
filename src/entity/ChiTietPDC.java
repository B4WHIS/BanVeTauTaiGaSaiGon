package entity;

public class ChiTietPDC {
	private PhieuDatCho phieuDatCho;
	private ChoNgoi choNgoi;
	private ChuyenTau chuyenTau;
	
	public PhieuDatCho getPhieuDatCho() {
		return phieuDatCho;
	}
	public void setPhieuDatCho(PhieuDatCho phieuDatCho) {
		this.phieuDatCho = phieuDatCho;
	}
	public ChoNgoi getChoNgoi() {
		return choNgoi;
	}
	public void setChoNgoi(ChoNgoi choNgoi) {
		this.choNgoi = choNgoi;
	}
	public ChuyenTau getChuyenTau() {
		return chuyenTau;
	}
	public void setChuyenTau(ChuyenTau chuyenTau) {
		this.chuyenTau = chuyenTau;
	}
	
	public ChiTietPDC(PhieuDatCho phieuDatCho, ChoNgoi choNgoi, ChuyenTau chuyenTau) {
		super();
		this.phieuDatCho = phieuDatCho;
		this.choNgoi = choNgoi;
		this.chuyenTau = chuyenTau;
	}
	
	@Override
	public String toString() {
		return "ChiTietPDC"
				+ "[phieuDatCho=" + phieuDatCho
				+ ", choNgoi="
				+ choNgoi 
				+ ", chuyenTau=" 
				+ chuyenTau + "]";
	}
	
	
}
