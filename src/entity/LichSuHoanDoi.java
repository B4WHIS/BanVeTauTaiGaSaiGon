package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LichSuHoanDoi {
	private String maLichSuHoanDoi;
	private LoaiGiaoDich loaiGiaoDich;
	private LocalDateTime ngayGiaoDich;
	private String lyDo;
	private BigDecimal phiXuLy;
	
	private Ve maVe;
	private NhanVien maNhanVien;
	private HoaDon maHoaDon;
	private HanhKhach maHanhKhach;
	
	public enum LoaiGiaoDich{
		HUY_VE("Hủy vé"),
		DOI_VE("Đổi vé");
		
		private String display;
		
		LoaiGiaoDich(String display) {
			this.display = display;
		}
		
        public String getDisplay() {
            return display;
        }

	}

	public LichSuHoanDoi() {}
	


	public String getMaLichSuHoanDoi() {
		return maLichSuHoanDoi;
	}

	public void setMaLichSuHoanDoi(String maLichSuHoanDoi) {
		this.maLichSuHoanDoi = maLichSuHoanDoi;
	}

	public LoaiGiaoDich getLoaiGiaoDich() {
		return loaiGiaoDich;
	}

	public void setLoaiGiaoDich(LoaiGiaoDich loaiGiaoDich) {
		if(loaiGiaoDich == null) {
			throw new IllegalArgumentException("Loại giao dịch không được rỗng");
		}
		this.loaiGiaoDich = loaiGiaoDich;
	}

	public LocalDateTime getNgayGiaoDich() {
		return ngayGiaoDich;
	}

	public void setNgayGiaoDich(LocalDateTime ngayGiaoDich) {
		if(ngayGiaoDich == null) {
			throw new IllegalArgumentException("Ngày giao dịch không được rỗng");
		}
		this.ngayGiaoDich = ngayGiaoDich;
	}

	public String getLyDo() {
		return lyDo;
	}

	public void setLyDo(String lyDo) {
		this.lyDo = lyDo;
	}

	public BigDecimal getPhiXuLy() {
		return phiXuLy;
	}

	public void setPhiXuLy(BigDecimal phiXuLy) {
		if(phiXuLy == null || phiXuLy.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Phí xử lý phải >= 0");
		}
		this.phiXuLy = phiXuLy;
	}

	public Ve getMaVe() {
		return maVe;
	}

	public void setMaVe(Ve maVe) {
		if(maVe == null) {
			throw new IllegalArgumentException("Mã vé không được rỗng");
		}
		this.maVe = maVe;
	}

	public NhanVien getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(NhanVien maNhanVien) {
		if(maNhanVien == null) {
			throw new IllegalArgumentException("Mã nhân viên không được rỗng");
		}
		this.maNhanVien = maNhanVien;
	}

	public HoaDon getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(HoaDon maHoaDon) {
		if(maHoaDon == null) {
			throw new IllegalArgumentException("Mã hóa đơn không được rỗng");
		}
		this.maHoaDon = maHoaDon;
	}

	public HanhKhach getMaHanhKhach() {
		return maHanhKhach;
	}

	public void setMaHanhKhach(HanhKhach maHanhKhach) {
		if(maHanhKhach == null) {
			throw new IllegalArgumentException("Mã hành khách không được rỗng");
		}
		this.maHanhKhach = maHanhKhach;
	}

	@Override
	public String toString() {
		return "LichSuHoanDoi [maLichSuHoanDoi=" + maLichSuHoanDoi + ", loaiGiaoDich=" + loaiGiaoDich
				+ ", ngayGiaoDich=" + ngayGiaoDich + ", lyDo=" + lyDo + ", phiXuLy=" + phiXuLy + ", maVe=" + maVe
				+ ", maNhanVien=" + maNhanVien + ", maHoaDon=" + maHoaDon + ", maHanhKhach=" + maHanhKhach + "]";
	}	
}
