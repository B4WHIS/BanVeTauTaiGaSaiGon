package entity;

import java.time.LocalDate;
//done
public class HanhKhach {
    private String maKH;
    private String hoTen;
    private String cmndCccd;
    private String soDT;
    private LocalDate ngaySinh;
    private String maUuDai;

    public HanhKhach(String hoTen, LocalDate ngaySinh, String soDT, String cmndCccd, String maUuDai) {
        
        this.hoTen = hoTen; 
        this.ngaySinh = ngaySinh;
        this.soDT = soDT;
        this.cmndCccd = cmndCccd;
        this.maUuDai = maUuDai;
    }
    public HanhKhach() {

    }
    public HanhKhach(String maKH, String hoTen, String cmndCccd, String soDT, LocalDate ngaySinh, String maUuDai) {
        super();
        this.maKH = maKH; // Cho phép null tạm thời
        setHoTen(hoTen);
        setCmndCccd(cmndCccd);
        setSoDT(soDT);
        setNgaySinh(ngaySinh);
        setMaUuDai(maUuDai);
    }
//    public HanhKhach(String maKH, String hoTen, String cmndCccd, String soDT, LocalDate ngaySinh, String maUuDai) {
//		super();
//		setMaKH(maKH);
//		setHoTen(hoTen);
//		setCmndCccd(cmndCccd);
//		setSoDT(soDT);
//		setNgaySinh(ngaySinh);
//		setMaUuDai(maUuDai);
//	}
    
	public HanhKhach(String maKH) {
		setMaKH(maKH);
	}

	public String getMaUuDai() {
        return maUuDai;
    }

    public void setMaUuDai(String maUuDai) {
        if (maUuDai == null || maUuDai.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã ưu đãi không được để trống.");
        }
        this.maUuDai = maUuDai;
    }
    
    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        // Ràng buộc DB: HK-XXXXX (2 kí tự đầu cố định, 5 kí tự số) [1]
        if (maKH == null || !maKH.matches("^HK-\\d{5}$")) 
        {
            throw new IllegalArgumentException("Mã khách hàng không hợp lệ! Phải có dạng HK-XXXXX.");
        }
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }
        // Biểu thức mới: Cho phép chữ cái tiếng Việt, mỗi từ bắt đầu bằng in hoa, không chứa số
        if (!hoTen.matches("^[A-ZÀ-Ỹ][a-zà-ỹA-ZÀ-Ỹ]*(?:\\s[A-ZÀ-Ỹ][a-zà-ỹA-ZÀ-Ỹ]*)*$")) {
            throw new IllegalArgumentException("Họ tên không hợp lệ! Phải viết hoa chữ cái đầu, không chứa số.");
        }
        this.hoTen = hoTen;
    }

    public String getCmndCccd() {
        return cmndCccd;
    }

    public void setCmndCccd(String cmndCccd) {
        if (cmndCccd != null && !cmndCccd.matches("^\\d{9}$|^\\d{12}$"))
            throw new IllegalArgumentException("CMND/CCCD phải gồm đúng 9 hoặc 12 chữ số!");
        this.cmndCccd = cmndCccd;
    }

    public String getSoDT() {
        return soDT;
    }
    public void setSoDT(String soDT) {
        
        if (soDT != null && !soDT.trim().isEmpty()) {
            if (!soDT.matches("^0\\d{9}$"))
                throw new IllegalArgumentException("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0!");
            this.soDT = soDT;
        } else {
            this.soDT = null;
        }
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        if (ngaySinh == null || !ngaySinh.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Ngày sinh không hợp lệ! Phải trước ngày hiện tại.");
        this.ngaySinh = ngaySinh;
    }

    
	@Override
	public String toString() {
		return "HanhKhach [maKH=" + maKH + ", hoTen=" + hoTen + ", cmndCccd=" + cmndCccd + ", soDT=" + soDT
				+ ", ngaySinh=" + ngaySinh + ", maUuDai=" + maUuDai + "]";
	}

   

  
}
