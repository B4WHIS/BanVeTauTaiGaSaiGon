package entity;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ThongKeDoanhThu {
    private String maThongKe;
    private LocalDate ngayThongKe;
    private String maNhanVien; 
    private BigDecimal tongDoanhThu;
    private int tongSoVe;
    private Integer tongSoHoanDoi; 

    public ThongKeDoanhThu() {}

    public ThongKeDoanhThu(String maThongKe, LocalDate ngayThongKe, String maNhanVien, BigDecimal tongDoanhThu, int tongSoVe) {
        setMaThongKe(maThongKe);
        setNgayThongKe(ngayThongKe);
        setMaNhanVien(maNhanVien);
        setTongDoanhThu(tongDoanhThu);
        setTongSoVe(tongSoVe);
        this.tongSoHoanDoi = 0;
    }    
    
    public String getMaThongKe() {
		return maThongKe;
	}

	public void setMaThongKe(String maThongKe) {
		this.maThongKe = maThongKe;
	}

	public LocalDate getNgayThongKe() {
		return ngayThongKe;
	}

	public void setNgayThongKe(LocalDate ngayThongKe) {
		this.ngayThongKe = ngayThongKe;
	}
  
    public BigDecimal getTongDoanhThu() {
    	return tongDoanhThu;
    }
    public void setTongDoanhThu(BigDecimal tongDoanhThu) {
        if (tongDoanhThu == null || tongDoanhThu.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Tổng doanh thu không được âm.");
        }
        this.tongDoanhThu = tongDoanhThu;
    }

    public int getTongSoVe() {
    	return tongSoVe;
    }
    public void setTongSoVe(int tongSoVe) {
        if (tongSoVe < 0) {
            throw new IllegalArgumentException("Tổng số vé không được âm.");
        }
        this.tongSoVe = tongSoVe;
    }
    
    public Integer getTongSoHoanDoi() {
    	return tongSoHoanDoi;
    }
    public void setTongSoHoanDoi(Integer tongSoHoanDoi) {

        if (tongSoHoanDoi != null && tongSoHoanDoi < 0) {
            throw new IllegalArgumentException("Tổng số hoàn đổi không được âm.");
        }
        this.tongSoHoanDoi = (tongSoHoanDoi == null) ? 0 : tongSoHoanDoi;
    }
    
    public String getMaNhanVien() {
    	return maNhanVien;
    }
    public void setMaNhanVien(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống.");
        }
        this.maNhanVien = maNhanVien;
    }
    
    @Override
    public String toString() {
        return "ThongKeDoanhThu [maThongKe=" + maThongKe + ", ngayThongKe=" + ngayThongKe +
               ", maNhanVien=" + maNhanVien + ", tongDoanhThu=" + tongDoanhThu +
               ", tongSoVe=" + tongSoVe + ", tongSoHoanDoi=" + tongSoHoanDoi + "]";
    }
}