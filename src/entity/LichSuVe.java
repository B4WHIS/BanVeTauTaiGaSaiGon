package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LichSuVe {
    private String maLichSuVe;
    private int IDloaiGiaoDich;
    private LocalDateTime ngayGiaoDich;
    private String lyDo;
    private BigDecimal phiXuLy; 
    private String maVe;
    private String maNhanVien;
    private String maHoaDon;
    private String maHanhKhach;
    private BigDecimal tienHoan;
 
    public LichSuVe() {
         
    }
    public LichSuVe(String maLichSuVe, int IDloaiGiaoDich, LocalDateTime ngayGiaoDich, String maVe, String maNhanVien) {
        setMaLichSuVe(maLichSuVe);
        setIDloaiGiaoDich(IDloaiGiaoDich);
        setNgayGiaoDich(ngayGiaoDich);
        setMaVe(maVe);
        setMaNhanVien(maNhanVien);
        this.phiXuLy = BigDecimal.ZERO; 
    }
    
	
	public String getMaLichSuVe() {
		return maLichSuVe;
	}
	public void setMaLichSuVe(String maLichSuVe) {
		this.maLichSuVe = maLichSuVe;
	}
    public int getIDloaiGiaoDich() {
    	return IDloaiGiaoDich;
    }
    public void setIDloaiGiaoDich(int IDloaiGiaoDich) {
        if (IDloaiGiaoDich <= 0) {
            throw new IllegalArgumentException("ID loại giao dịch phải dương.");
        }
        this.IDloaiGiaoDich = IDloaiGiaoDich;
    }
    
	public LocalDateTime getNgayGiaoDich() {
		return ngayGiaoDich;
	}
	public void setNgayGiaoDich(LocalDateTime ngayGiaoDich) {
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
        if (phiXuLy != null && phiXuLy.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Phí xử lý không được âm.");
        }
        this.phiXuLy = (phiXuLy == null) ? BigDecimal.ZERO : phiXuLy;
    }
    public String getMaVe() {
    	return maVe;
    }
    public void setMaVe(String maVe) {
        if (maVe == null || maVe.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã vé không được để trống.");
        }
        this.maVe = maVe;
    }
	public String getMaNhanVien() {
		return maNhanVien;
	}
	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}
	public String getMaHoaDon() {
		return maHoaDon;
	}
	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}
	public String getMaHanhKhach() {
		return maHanhKhach;
	}
	public void setMaHanhKhach(String maHanhKhach) {
		this.maHanhKhach = maHanhKhach;
	}
	public BigDecimal getTienHoan() { return tienHoan; }
	public void setTienHoan(BigDecimal tienHoan) {
	    this.tienHoan = (tienHoan == null) ? BigDecimal.ZERO : tienHoan;
	}
	
	
    
}
