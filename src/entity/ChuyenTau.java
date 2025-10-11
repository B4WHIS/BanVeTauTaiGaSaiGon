package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ChuyenTau {

    private String maChuyenTau;   
    private LocalDate ngayKhoiHanh; 
    private LocalTime gioKhoiHanh;  
    private Tau tau;               
    private LichTrinh lichTrinh;    
    private BigDecimal giaChuyen;  


    public ChuyenTau() {
    }

    public ChuyenTau(String maChuyenTau, LocalDate ngayKhoiHanh, LocalTime gioKhoiHanh,
                     Tau tau, LichTrinh lichTrinh, BigDecimal giaChuyen) {
        setMaChuyenTau(maChuyenTau);
        setNgayKhoiHanh(ngayKhoiHanh);
        setGioKhoiHanh(gioKhoiHanh);
        setTau(tau);
        setLichTrinh(lichTrinh);
        setGiaChuyen(giaChuyen);
    }

  
    public String getMaChuyenTau() {
        return maChuyenTau;
    }

    public void setMaChuyenTau(String maChuyenTau) {
        if (maChuyenTau == null || maChuyenTau.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã chuyến tàu không được để trống.");
        }
        if (maChuyenTau.length() > 6) {
            throw new IllegalArgumentException("Mã chuyến tàu không được vượt quá 6 ký tự.");
        }
        this.maChuyenTau = maChuyenTau.trim();
    }

    public LocalDate getNgayKhoiHanh() {
        return ngayKhoiHanh;
    }

    public void setNgayKhoiHanh(LocalDate ngayKhoiHanh) {
        if (ngayKhoiHanh == null) {
            throw new IllegalArgumentException("Ngày khởi hành không được để trống.");
        }
        if (ngayKhoiHanh.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày khởi hành phải từ hôm nay trở đi.");
        }
        this.ngayKhoiHanh = ngayKhoiHanh;
    }

    public LocalTime getGioKhoiHanh() {
        return gioKhoiHanh;
    }

    public void setGioKhoiHanh(LocalTime gioKhoiHanh) {
        if (gioKhoiHanh == null) {
            throw new IllegalArgumentException("Giờ khởi hành không được để trống.");
        }
        this.gioKhoiHanh = gioKhoiHanh;
    }

    public Tau getTau() {
        return tau;
    }

    public void setTau(Tau tau) {
        if (tau == null) {
            throw new IllegalArgumentException("Mỗi chuyến tàu phải gắn với một tàu cụ thể.");
        }
        this.tau = tau;
    }

    public LichTrinh getLichTrinh() {
        return lichTrinh;
    }

    public void setLichTrinh(LichTrinh lichTrinh) {
        if (lichTrinh == null) {
            throw new IllegalArgumentException("Mỗi chuyến tàu phải thuộc về một lịch trình cụ thể.");
        }
        this.lichTrinh = lichTrinh;
    }

    public BigDecimal getGiaChuyen() {
        return giaChuyen;
    }

    public void setGiaChuyen(BigDecimal giaChuyen) {
        if (giaChuyen == null || giaChuyen.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá chuyến tàu phải lớn hơn 0.");
        }
        this.giaChuyen = giaChuyen;
    }

    @Override
    public String toString() {
        return "ChuyenTau [" +
                "maChuyenTau='" + maChuyenTau + '\'' +
                ", ngayKhoiHanh=" + ngayKhoiHanh +
                ", gioKhoiHanh=" + gioKhoiHanh +
                ", maTau=" + (tau != null ? tau.getMaTau() : "null") +
                ", maLichTrinh=" + (lichTrinh != null ? lichTrinh.getMaLichTrinh() : "Lỗi") +
                ", giaChuyen=" + giaChuyen +
                ']';
    }
}
