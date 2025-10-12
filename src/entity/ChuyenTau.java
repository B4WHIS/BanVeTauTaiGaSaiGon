package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ChuyenTau {

    private Integer IDct;                        
    private String maChuyenTau;                  
    private LocalDateTime thoiGianKhoiHanh;      
    private LocalDateTime thoiGianDen;          
    private String maTau;                        
    private String maLichTrinh;                  
    private BigDecimal giaChuyen;               

    public ChuyenTau() {
    	
    }

    public ChuyenTau(Integer IDct, String maChuyenTau, LocalDateTime thoiGianKhoiHanh,
                     LocalDateTime thoiGianDen, String maTau, String maLichTrinh, BigDecimal giaChuyen) {
        setId(IDct);
        setMaChuyenTau(maChuyenTau);
        setThoiGianKhoiHanh(thoiGianKhoiHanh);
        setThoiGianDen(thoiGianDen);
        setMaTau(maTau);
        setMaLichTrinh(maLichTrinh);
        setGiaChuyen(giaChuyen);
    }

    public Integer getId() {
        return IDct;
    }

    public void setId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID chuyến tàu không được để trống");
        }
        if (id < 1 || id > 999) {
            throw new IllegalArgumentException("ID chuyến tàu phải nằm trong khoảng 001 - 999");
        }
        this.IDct = id;
    }

    public String getMaChuyenTau() {
        return maChuyenTau;
    }

    public void setMaChuyenTau(String maChuyenTau) {
        if (maChuyenTau == null || maChuyenTau.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã chuyến tàu không được để trống");
        }
        if (maChuyenTau.length() > 5) {
            throw new IllegalArgumentException("Mã chuyến tàu không được vượt quá 5 ký tự");
        }
        this.maChuyenTau = maChuyenTau.trim();
    }

    public LocalDateTime getThoiGianKhoiHanh() {
        return thoiGianKhoiHanh;
    }

    public void setThoiGianKhoiHanh(LocalDateTime thoiGianKhoiHanh) {
        if (thoiGianKhoiHanh == null) {
            throw new IllegalArgumentException("Thời gian khởi hành không được để trống");
        } else if (thoiGianKhoiHanh.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Thời gian khởi hành không được trước hiện tại");
        } else if (this.thoiGianDen != null && thoiGianKhoiHanh.isAfter(this.thoiGianDen)) {
            throw new IllegalArgumentException("Thời gian khởi hành không được sau thời gian đến");
        }
        this.thoiGianKhoiHanh = thoiGianKhoiHanh;
    }

    public LocalDateTime getThoiGianDen() {
        return thoiGianDen;
    }

    public void setThoiGianDen(LocalDateTime thoiGianDen) {
        if (thoiGianDen == null) {
            throw new IllegalArgumentException("Thời gian đến không được để trống");
        } else if (this.thoiGianKhoiHanh != null && thoiGianDen.isBefore(this.thoiGianKhoiHanh)) {
            throw new IllegalArgumentException("Thời gian đến phải sau thời gian khởi hành");
        }
        this.thoiGianDen = thoiGianDen;
    }

    public String getMaTau() {
        return maTau;
    }

    public void setMaTau(String maTau) {
        if (maTau == null || maTau.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã tàu không được để trống.");
        }
        if (maTau.length() > 6) {
            throw new IllegalArgumentException("Mã tàu không được vượt quá 6 ký tự.");
        }
        this.maTau = maTau.trim();
    }

    public String getMaLichTrinh() {
        return maLichTrinh;
    }

    public void setMaLichTrinh(String maLichTrinh) {
        if (maLichTrinh == null || maLichTrinh.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch trình không được để trống.");
        }
        if (maLichTrinh.length() > 6) {
            throw new IllegalArgumentException("Mã lịch trình không được vượt quá 6 ký tự.");
        }
        this.maLichTrinh = maLichTrinh.trim();
    }

    public BigDecimal getGiaChuyen() {
        return giaChuyen;
    }

    public void setGiaChuyen(BigDecimal giaChuyen) {
        if (giaChuyen == null) {
            throw new IllegalArgumentException("Giá chuyến tàu không được để trống.");
        }
        if (giaChuyen.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá chuyến tàu phải lớn hơn 0.");
        }
        this.giaChuyen = giaChuyen;
    }


    @Override
    public String toString() {
        return "ChuyenTau [" +
                "IDct=" + IDct +
                ", maChuyenTau='" + maChuyenTau + '\'' +
                ", thoiGianKhoiHanh=" + thoiGianKhoiHanh +
                ", thoiGianDen=" + thoiGianDen +
                ", maTau='" + maTau + '\'' +
                ", maLichTrinh='" + maLichTrinh + '\'' +
                ", giaChuyen=" + giaChuyen +
                ']';
    }
}

