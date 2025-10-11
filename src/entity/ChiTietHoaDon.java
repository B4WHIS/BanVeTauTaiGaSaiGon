package entity;

public class ChiTietHoaDon {
	private HoaDon hoaDon;
 	private Ve ve;
	private double donGia;

	public ChiTietHoaDon(){}

	public ChiTietHoaDon(HoaDon hoaDon, Ve ve, double donGia) {
        setHoaDon(hoaDon);
        setVe(ve);
        setDonGia(donGia); 
    }
	
    public HoaDon getHoaDon() {
        return hoaDon;
    }
    public void setHoaDon(HoaDon hoaDon) {
        if (hoaDon == null) {
            throw new IllegalArgumentException("Chi tiết phải thuộc về một Hóa đơn.");
        }
        this.hoaDon = hoaDon;
    }
    
    public Ve getVe() {
        return ve;
    }
    public void setVe(Ve ve) {
        if (ve == null) {
            throw new IllegalArgumentException("Chi tiết phải có liên kết đến Vé.");
        }
        this.ve = ve;
    }
    
    public double getDonGia() {
        return donGia;
    }
    public void setDonGia(double donGia) {
        if (donGia < 0) {
            throw new IllegalArgumentException("Đơn giá phải lớn hơn hoặc bằng 0.");
        }
        this.donGia = donGia;
    }
    

	@Override
    public String toString() {
        return "ChiTietHoaDon [maHoaDon=" + (hoaDon != null ? hoaDon.getMaHoaDon() : "null") + 
               ", maVe=" + (ve != null ? ve.getMaVe() : "null") + 
               ", donGia=" + donGia + "]";
    }

}
