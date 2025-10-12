package entity;

import java.time.LocalDateTime;

public class Ve {
	private String maVe;
	private LocalDateTime ngayDat;
	private String trangThai;
	private double giaVeGoc;
	private double giaThanhToan;
	private ChoNgoi maChoNgoi;
	private ChuyenTau maChuyenTau;
	private HanhKhach maHanhkhach;
	private KhuyenMai maKhuyenMai;
	private NhanVien maNhanVien;

	public Ve(String maVe, LocalDateTime ngayDat, String trangThai, double giaVeGoc, double giaThanhToan, ChoNgoi maChoNgoi, ChuyenTau maChuyenTau,
			HanhKhach maHanhkhach, KhuyenMai maKhuyenMai, NhanVien maNhanVien) throws Exception {
		super();
		setMaChoNgoi(maChoNgoi);
		setMaChuyenTau(maChuyenTau);
		setMaHanhkhach(maHanhkhach);
		setMaNhanVien(maNhanVien);
		setMaKhuyenMai(maKhuyenMai);
		setMaVe(maVe);
		setNgayDat(ngayDat);
		setTrangThai(trangThai);
		setGiaVeGoc(giaVeGoc);
		setGiaThanhToan(giaThanhToan);
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

	    public double getGiaVeGoc() {
	        return giaVeGoc;
	    }

	    public void setGiaVeGoc(double giaVeGoc) {
	        if (giaVeGoc <= 0)
	            throw new IllegalArgumentException("Giá vé gốc phải lớn hơn 0");
	        this.giaVeGoc = giaVeGoc;
	    }

	    public double getGiaThanhToan() {
	        return giaThanhToan;
	    }

	    public void setGiaThanhToan(double giaThanhToan) {
	        if (giaThanhToan <= 0)
	            throw new IllegalArgumentException("Giá thanh toán phải lớn hơn 0");
	        this.giaThanhToan = giaThanhToan;
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
	    	   	throw new IllegalArgumentException("Mã nhân viên không được để trống");
	       this.maNhanVien = maNhanVien;
	    }

	    // ===== toString() =====
	    @Override
	    public String toString() {
	        return "Ve [maVe=" + maVe + ", ngayDat=" + ngayDat  + ", trangThai=" + trangThai
	                + ", giaVeGoc=" + giaVeGoc + ", giaThanhToan=" + giaThanhToan
	                + ", maChoNgoi=" + (maChoNgoi != null ? maChoNgoi.toString() : "null")
	                + ", maChuyenTau=" + (maChuyenTau != null ? maChuyenTau.toString() : "null")
	                + ", maHanhkhach=" + (maHanhkhach != null ? maHanhkhach.toString() : "null")
	                + ", maKhuyenMai=" + (maKhuyenMai != null ? maKhuyenMai.toString() : "null")
	                + ", maNhanVien=" + (maNhanVien != null ? maNhanVien.toString() : "null") + "]";
	    }
}