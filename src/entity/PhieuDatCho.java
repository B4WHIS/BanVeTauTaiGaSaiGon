package entity;

import java.time.LocalDateTime;

public class PhieuDatCho {
	private String maPhieuDatCho;
	private LocalDateTime ngayDat; //ngày đặt vé
	private TrangThai trangThai;
	private HanhKhach maHanhKhach;
	private NhanVien maNhanVien;
	 public PhieuDatCho(String maPhieuDatCho, LocalDateTime ngayDat, TrangThai trangThai,
             HanhKhach maHanhKhach, NhanVien maNhanVien) {
		
		setMaPhieuDatCho(maPhieuDatCho);
		setNgayDat(ngayDat);
		setTrangThai(trangThai);
		setMaHanhKhach(maHanhKhach); 
		setMaNhanVien(maNhanVien);   
	 }
	 public enum TrangThai{
		 DA_THANH_TOAN("Đã thanh toán"),
		 CHUA_THANH_TOAN("Chưa thanh toán");
		 private final String display;
			
			TrangThai(String display) {
				this.display = display;
			}
			
			public String getDisplay() {
				return display;
			}
	 }
	
	public PhieuDatCho() {}

	public String getMaPhieuDatCho() {
		return maPhieuDatCho;
	}

	public void setMaPhieuDatCho(String maPhieuDatCho) {
	    if (maPhieuDatCho == null || maPhieuDatCho.trim().isEmpty()) {
	        throw new IllegalArgumentException("Mã phiếu đặt chỗ không được rỗng.");
	    }
	    
	    this.maPhieuDatCho = maPhieuDatCho;
	}

	public LocalDateTime getNgayDat() {
		return ngayDat;
	}

	public void setNgayDat(LocalDateTime ngayDat) {
	    if (ngayDat == null) {
	        throw new IllegalArgumentException("Ngày đặt không được rỗng.");
	    }
	    if (ngayDat.isAfter(LocalDateTime.now())) {
	        throw new IllegalArgumentException("Ngày đặt không được lớn hơn ngày hiện tại.");
	    }
	    this.ngayDat = ngayDat;
	}

	public TrangThai getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(TrangThai trangThai) {
	    if (trangThai == null) {
	        throw new IllegalArgumentException("Trạng thái vé không được null.");
	    }
	    this.trangThai = trangThai;
	}

	public HanhKhach getMaHanhKhach() {
		return maHanhKhach;
	}

	public void setMaHanhKhach(HanhKhach maHanhKhach) {
	    if (maHanhKhach == null) {
	        throw new IllegalArgumentException("Mã hành khách không được rỗng.");
	    }
	    this.maHanhKhach = maHanhKhach;
	}

	public NhanVien getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(NhanVien maNhanVien) {
	    if (maNhanVien == null) {
	        throw new IllegalArgumentException("Mã nhân viên không được rỗng.");
	    }
	    this.maNhanVien = maNhanVien;
	}
	 @Override
	 public String toString() {
	     return "PhieuDatCho [maPhieuDatCho=" + maPhieuDatCho + ", ngayDat=" + ngayDat +
	            ", trangThai=" + trangThai +
	            ", maHanhKhach=" + (maHanhKhach != null ? maHanhKhach.getMaKH() : "null") + 
	            ", maNhanVien=" + (maNhanVien != null ? maNhanVien.getMaNhanVien() : "null") + "]";
	    }
}
