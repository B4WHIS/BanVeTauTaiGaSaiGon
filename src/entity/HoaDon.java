package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDon {
	private String maHoaDon;
	private LocalDateTime ngayLap;
	private HanhKhach maHanhKhach;
	private NhanVien maNhanVien;
	private final double tongThanhToan; 
	private List<ChiTietHoaDon> danhSachChiTiet;

	public HoaDon() {
        this.tongThanhToan = 0.0;
        this.danhSachChiTiet = new ArrayList<>();
    }
	public HoaDon(String maHoaDon, LocalDateTime ngayLap, HanhKhach maHanhKhach, NhanVien maNhanVien, 
            List<ChiTietHoaDon> danhSachChiTiet) {
		setMaHoaDon(maHoaDon);
		setNgayLap(ngayLap);
		setMaHanhKhach(maHanhKhach);
		setMaNhanVien(maNhanVien);

  		this.danhSachChiTiet = (danhSachChiTiet != null) ? danhSachChiTiet : new ArrayList<>();
  		this.tongThanhToan = tinhTongTien();
	}
    
    public double getTongThanhToan() {
        return tongThanhToan;
    }
    
	public String getMaHoaDon() {
		return maHoaDon;
	}
	public void setMaHoaDon(String maHoaDon) {
	    if (maHoaDon == null || maHoaDon.trim().isEmpty()) {
	        throw new IllegalArgumentException("Mã hóa đơn không được rỗng.");
	    }
	   
	    this.maHoaDon = maHoaDon;
	}
	
	public LocalDateTime getNgayLap() {
		return ngayLap;
	}
	
	public void setNgayLap(LocalDateTime ngayLap) {
	    if (ngayLap == null) {
	        throw new IllegalArgumentException("Ngày lập không được rỗng.");
	    }
	    if (ngayLap.isAfter(LocalDateTime.now())) {
	        throw new IllegalArgumentException("Ngày lập không được lớn hơn ngày hiện tại.");
	    }
	    this.ngayLap = ngayLap;
	}
	
	public HanhKhach getMaHanhKhach() {
		return maHanhKhach;
	}
	public void setMaHanhKhach(HanhKhach maHanhKhach) {
	    if (maHanhKhach == null) {
	        throw new IllegalArgumentException("Thông tin Khách hàng (người lập Hóa đơn) không được rỗng.");
	    }
	    this.maHanhKhach = maHanhKhach;
	}
	
	public NhanVien getMaNhanVien() {
		return maNhanVien;
	}
	
	public void setMaNhanVien(NhanVien maNhanVien) {
	    if (maNhanVien == null) {
	        throw new IllegalArgumentException("Thông tin Nhân viên không được rỗng.");
	    }
	    this.maNhanVien = maNhanVien;
	}
	
	public void setDanhSachChiTiet(List<ChiTietHoaDon> danhSachChiTiet) {
	    if (danhSachChiTiet == null) { 
	        this.danhSachChiTiet = new ArrayList<>();
	        return;
	    }
	    this.danhSachChiTiet = danhSachChiTiet;
	}

	public List<ChiTietHoaDon> getDanhSachChiTiet() {
	    if (danhSachChiTiet == null) {
	        danhSachChiTiet = new ArrayList<>();
	    }
	    return danhSachChiTiet;
	}

    public double tinhTongTien() {
        double tong = 0.0;
        if (danhSachChiTiet != null) {
             for (ChiTietHoaDon cthd : danhSachChiTiet) {
                 tong += cthd.getDonGia();
             }
        }
        return tong;
    }
	@Override
    public String toString() {
        
        return "HoaDon [maHoaDon=" + maHoaDon + ", ngayLap=" + ngayLap + 
               ", tongThanhToan=" + tongThanhToan + 
               ", maHanhKhach=" + (maHanhKhach != null ? maHanhKhach.getMaKH() : "null") + 
               ", maNhanVien=" + (maNhanVien != null ? maNhanVien.getMaNhanVien() : "null") + "]";
    }


	
}

