package entity;

public class ChoNgoi {
	private String maChoNgoi;   
	private LoaiGhe loaiGhe;     // "Ghế mềm", "Giường nằm", ...
	private TrangThai trangThai;   // Enum cho trạng thái
	private ToaTau toaTau;   

	public enum LoaiGhe{
		GHE_MEM("Ghế mềm"),
		GIUONG_NAM("Giường nằm");
		private final String display;
		 LoaiGhe(String display) {
			// TODO Auto-generated constructor stub
			 this.display = display;
		}
		 public String getDisplay() {
			 return display;
		 }
	}
	// Enum cho trạng thái
	public enum TrangThai {
		TRONG("Trống"),
		DA_DAT("Đã đặt");
		
		private final String display;
		
		TrangThai(String display) {
			this.display = display;
		}
		
		public String getDisplay() {
			return display;
		}
	}
	
	// toaTau không null
	public ToaTau getToaTau() {
		return toaTau;
	}

	public void setToaTau(ToaTau toaTau) {
		if (toaTau == null) {
			throw new IllegalArgumentException("ToaTau không được null!");
		}
		this.toaTau = toaTau;
	}

	// ===== Constructor =====
	public ChoNgoi() {
		super();
	}

	public ChoNgoi(String maChoNgoi, LoaiGhe loaiGhe, TrangThai trangThai) {
		super();
		this.maChoNgoi = maChoNgoi;
		setLoaiGhe(loaiGhe);
		this.trangThai = trangThai;
	}

	public ChoNgoi(String maChoNgoi, LoaiGhe loaiGhe, TrangThai trangThai, ToaTau toaTau) {
		this(maChoNgoi, loaiGhe, trangThai);
		setToaTau(toaTau);
	}

	// ===== Getter & Setter có ràng buộc =====
	public String getMaChoNgoi() {
		return maChoNgoi;
	}

	public void setMaChoNgoi(String maChoNgoi) {
		
		this.maChoNgoi = maChoNgoi;
	}

	public LoaiGhe getLoaiGhe() {
		return loaiGhe;
	}

	public void setLoaiGhe(LoaiGhe loaiGhe) {
		if (loaiGhe == null || !(loaiGhe.equals("Ghế mềm") || loaiGhe.equals("Giường nằm"))) {
			throw new IllegalArgumentException("Loại ghế không hợp lệ! Phải là 'Ghế mềm' hoặc 'Giường nằm'.");
		}
		this.loaiGhe = loaiGhe;
	}

	public TrangThai getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(TrangThai trangThai) {
		
		this.trangThai = trangThai;
	}

	// ====== Hàm chuẩn hóa chữ (viết hoa chữ cái đầu mỗi từ) ======

	@Override
	public String toString() {
		return String.format("ChoNgoi[maChoNgoi=%s, loaiGhe=%s, trangThai=%s, toaTau=%s]",
				maChoNgoi, loaiGhe, trangThai != null ? trangThai.getDisplay() : null, toaTau != null ? toaTau.getMaToa() : null);
	}
}