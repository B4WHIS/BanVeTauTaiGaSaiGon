package entity;

public class ChiTietHoaDon {
	private HoaDon hoaDon;
// 	private Ve ve;
	private double donGia;
	private int soLuong;

	public ChiTietHoaDon(){}

	public HoaDon getHoaDon() {
		return hoaDon;
	}
	public void setHoaDon(HoaDon hoaDon) {
		if(hoaDon == null) {
			throw new NullPointerException("Hóa đơn không được null");
		}
		this.hoaDon = hoaDon;
	}
	
//	public void setHoaDon(Ve ve) {
//		if(ve == null) {
//			throw new NullPointerException("Vé không được null");
//		}
//		this.Ve = ve;
//	}
	
	public double getDonGia() {
		return donGia;
	}
	
	public void setDonGia(double donGia) {
		if(donGia < 0) {
			throw new IllegalArgumentException("Đơn giá phải lớn hơn hoặc bằng 0");
		}
		this.donGia = donGia;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		if(soLuong <= 0) {
			throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
		}
		this.soLuong = soLuong;
	}
	
	public double tinhThanhTien() {
		return this.soLuong * this.donGia;
	}

}
