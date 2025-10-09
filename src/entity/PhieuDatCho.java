package entity;

import java.time.LocalDateTime;
import java.util.Date;

public class PhieuDatCho {
	private String maPhieuDatCho;
	private LocalDateTime ngayDat; //ngày đặt vé
	private VeStatus trangThai;
	
	public PhieuDatCho(String maPhieuDatCho, LocalDateTime ngayDat, VeStatus trangThai) {
		this.maPhieuDatCho = maPhieuDatCho;
		this.ngayDat = ngayDat;
		this.trangThai = trangThai;
	}
	public PhieuDatCho() {}
	public String getMaPhieuDatCho() {
		return maPhieuDatCho;
	}
	public void setMaPhieuDatCho(String maPhieuDatCho) {
		//KIỂM TRA LOGIC
		this.maPhieuDatCho = maPhieuDatCho;
	}
	public LocalDateTime getNgayDat() {
		return ngayDat;
	}
	public void setNgayDat(LocalDateTime ngayDat) {
		if(ngayDat == null || ngayDat.isAfter(LocalDateTime.now())) {
			throw new IllegalArgumentException("Ngày đặt không được lớn hơn ngày hiện tại");
		}
		this.ngayDat = ngayDat;
	}
	public VeStatus getTrangThai() {
		return trangThai;
	}
	
	public void setTrangThai(VeStatus trangThai) {
		if(trangThai == null) {
			throw new IllegalArgumentException("Trạng thái vé không được null");
		}
		this.trangThai = trangThai;
	}
	
	@Override
	public String toString() {
		return "PhieuDatCho [maPhieuDatCho=" + maPhieuDatCho + ", ngayDat=" + ngayDat + ", trangThai=" + trangThai
				+ "]";
	}
	
}
