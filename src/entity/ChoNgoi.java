package entity;

public class ChoNgoi {
	private String maChoNgoi;   // Dạng TOAXX-YY
	private String loaiGhe;     // "Ghế mềm", "Giường nằm", ...
	private String trangThai;   // "Trống", "Đã đặt", ...
	private ToaTau toaTau;   

	//đổi trạng thái thành enum
	//toaTau không null
	public ToaTau getToaTau() {
		return toaTau;
	}

	public void setToaTau(ToaTau toaTau) {
		this.toaTau = toaTau;
	}

	// ===== Constructor =====
	public ChoNgoi() {
		super();
	}

	public ChoNgoi(String maChoNgoi, String loaiGhe, String trangThai) {
		super();
		this.maChoNgoi = maChoNgoi;
		this.loaiGhe = loaiGhe;
		this.trangThai = trangThai;
	}

	// ===== Getter & Setter có ràng buộc =====
	public String getMaChoNgoi() {
		return maChoNgoi;
	}

	public void setMaChoNgoi(String maChoNgoi) {
		
		this.maChoNgoi = maChoNgoi;
	}

	public String getLoaiGhe() {
		return loaiGhe;
	}

	public void setLoaiGhe(String loaiGhe) {
		this.loaiGhe = loaiGhe;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		
		this.trangThai = trangThai;
	}

	// ====== Hàm chuẩn hóa chữ (viết hoa chữ cái đầu mỗi từ) ======

	@Override
	public String toString() {
		return String.format("ChoNgoi[maChoNgoi=%s, loaiGhe=%s, trangThai=%s]",
				maChoNgoi, loaiGhe, trangThai);
	}
}
