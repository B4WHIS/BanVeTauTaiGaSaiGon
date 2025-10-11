package entity;

import java.time.LocalDateTime;

public class Ve {
	private String maVe;
	private LocalDateTime ngayDat;
	// private BigDecimal giaVe;
	private String trangThai;
	private ChoNgoi maChoNgoi;
	private ChuyenTau maChuyenTau;
	private HanhKhach maHanhkhach;
	private KhuyenMai maKhuyenMai;
	private NhanVien maNhanVien;
	//thêm giá gốc với giá thanh toán
	
	public Ve(String maVe, LocalDateTime ngayDat, String trangThai, ChoNgoi maChoNgoi, ChuyenTau maChuyenTau,
			HanhKhach maHanhkhach, KhuyenMai maKhuyenMai, NhanVien maNhanVien) throws Exception {
		super();
		setMaChoNgoi(maChoNgoi);
		setMaChuyenTau(maChuyenTau);
		setMaHanhkhach(maHanhkhach);
		setMaNhanVien(maNhanVien);
		this.maKhuyenMai = maKhuyenMai;
		setMaVe(maVe);
		setNgayDat(ngayDat);
		setTrangThai(trangThai);
	}
	public Ve() {
		super();
		// TODO Auto-generated constructor stub
	}
	 public String getMaVe() {
	        return maVe;
	    }

	    public void setMaVe(String maVe) {
	        if (maVe == null || maVe.trim().isEmpty())
	            throw new IllegalArgumentException("Mã vé không được rỗng (NOT NULL)");
	        this.maVe = maVe;
	    }

	    public LocalDateTime getNgayDat() {
	        return ngayDat;
	    }

	    public void setNgayDat(LocalDateTime ngayDat) {
	        if (ngayDat == null)
	            throw new IllegalArgumentException("Ngày đặt không được rỗng (NOT NULL)");
	        this.ngayDat = ngayDat;
	    }
	    public String getTrangThai() {
	        return trangThai;
	    }

	    public void setTrangThai(String trangThai) {
	        if (trangThai == null || trangThai.trim().isEmpty())
	            throw new IllegalArgumentException("Trạng thái không được rỗng (NOT NULL)");
	        this.trangThai = trangThai;
	    }

	    public ChoNgoi getMaChoNgoi() {
	        return maChoNgoi;
	    }

	    public void setMaChoNgoi(ChoNgoi maChoNgoi) {
	        if (maChoNgoi == null)
	            throw new IllegalArgumentException("Mã chỗ ngồi không được rỗng (FK NOT NULL)");
	        this.maChoNgoi = maChoNgoi;
	    }

	    public ChuyenTau getMaChuyenTau() {
	        return maChuyenTau;
	    }

	    public void setMaChuyenTau(ChuyenTau maChuyenTau) {
	        if (maChuyenTau == null)
	            throw new IllegalArgumentException("Mã chuyến tàu không được rỗng (FK NOT NULL)");
	        this.maChuyenTau = maChuyenTau;
	    }

	    public HanhKhach getMaHanhkhach() {
	        return maHanhkhach;
	    }

	    public void setMaHanhkhach(HanhKhach maHanhkhach) {
	        if (maHanhkhach == null)
	            throw new IllegalArgumentException("Mã hành khách không được rỗng (FK NOT NULL)");
	        this.maHanhkhach = maHanhkhach;
	    }

	    public KhuyenMai getMaKhuyenMai() {
	        return maKhuyenMai;
	    }

	    public void setMaKhuyenMai(KhuyenMai maKhuyenMai) {
	        this.maKhuyenMai = maKhuyenMai; // có thể null vì trong SQL không NOT NULL
	    }

	    public NhanVien getMaNhanVien() {
	        return maNhanVien;
	    }

	    public void setMaNhanVien(NhanVien maNhanVien) throws Exception {
	       if(maNhanVien == null)
	    	   	throw new Exception("Mã nhân viên không được để trống");
	    }

	    // ===== toString() =====
	    @Override
	    public String toString() {
	        return "Ve [maVe=" + maVe + ", ngayDat=" + ngayDat  + ", trangThai=" + trangThai
	                + ", maChoNgoi=" + (maChoNgoi != null ? maChoNgoi.toString() : "null")
	                + ", maChuyenTau=" + (maChuyenTau != null ? maChuyenTau.toString() : "null")
	                + ", maHanhkhach=" + (maHanhkhach != null ? maHanhkhach.toString() : "null")
	                + ", maKhuyenMai=" + (maKhuyenMai != null ? maKhuyenMai.toString() : "null")
	                + ", maNhanVien=" + (maNhanVien != null ? maNhanVien.toString() : "null") + "]";
	    }

	
	
}
