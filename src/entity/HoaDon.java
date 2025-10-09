package entity;

import java.time.LocalDateTime;

public class HoaDon {
	private String maHoaDon;
	private LocalDateTime ngayLap;
//	private HanhKhach hanhKhach;
	
	public HoaDon(String maHoaDon, LocalDateTime ngayLap) {
		this.maHoaDon = maHoaDon;
		this.ngayLap = ngayLap;
	}
	
	public String getMaHoaDon() {
		return maHoaDon;
	}
	public void setMaHoaDon(String maHoaDon) {
		//RÀNG BUỘC
		this.maHoaDon = maHoaDon;
	}
	public LocalDateTime getNgayLap() {
		return ngayLap;
	}
	public void setNgayLap(LocalDateTime ngayLap) {
		if(ngayLap == null || ngayLap.isAfter(LocalDateTime.now())) {
			throw new IllegalArgumentException("Ngày lập không được lớn hơn ngày hiện tại");
		}
		this.ngayLap = ngayLap;
	}

	@Override
	public String toString() {
		return "HoaDon [maHoaDon=" + maHoaDon + ", ngayLap=" + ngayLap + "]";
	}	
}

