package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class HanhKhach {
	private String maKH;
	private String hoTen;
	private String cmndCccd;
	private String soDT;
	private LocalDate ngaySinh;
	private String loaiHanhKhach;
	
	public HanhKhach() {
		super();
	}

	public HanhKhach(String maKH) {
		setMaKH(maKH);
	}

	public HanhKhach(LocalDate ngaySinh, String soDT, String hoTen, String maKH, String cmndCccd, String loaiHanhKhach) {
		super();
		setNgaySinh(ngaySinh);
		setSoDT(soDT);
		setHoTen(hoTen);
		this.maKH = maKH;
		setCmndCccd(cmndCccd);
		setLoaiHanhKhach(loaiHanhKhach);
	}

	// ==== Getter & Setter có ràng buộc ====

	public String getMaKH() {
		return maKH;
	}

	public void setMaKH(String maKH) {
		
		this.maKH = maKH;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
	
		if (hoTen == null || !hoTen.matches("^[A-ZÀ-Ỹ][a-zà-ỹ]*(\\s[A-ZÀ-Ỹ][a-zà-ỹ]*)*$")) {
			throw new IllegalArgumentException("Họ tên không hợp lệ! Phải viết hoa chữ cái đầu, không chứa số hoặc ký tự đặc biệt.");
		}
		this.hoTen = hoTen;
	}

	public String getCmndCccd() {
		return cmndCccd;
	}
	public void setCmndCccd(String cmndCccd) {
		boolean isChild = loaiHanhKhach != null && loaiHanhKhach.equals("Trẻ em");
		
		if (!isChild) {
			if (cmndCccd == null || !cmndCccd.matches("^\\d{9}$|^\\d{12}$")) {
				throw new IllegalArgumentException("Nếu không phải Trẻ em, CMND/CCCD là bắt buộc và phải gồm đúng 9 hoặc 12 chữ số!");
			}
		} else {
			if (cmndCccd != null && !cmndCccd.matches("^\\d{9}$|^\\d{12}$")) {
				throw new IllegalArgumentException("Nếu cung cấp, CMND/CCCD phải gồm đúng 9 hoặc 12 chữ số!");
			}
		}
		this.cmndCccd = cmndCccd;
	}

	public String getSoDT() {
		return soDT;
	}

	public void setSoDT(String soDT) {
		if (soDT == null || !soDT.matches("^0\\d{9}$")) {
			throw new IllegalArgumentException("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0!");
		}
		this.soDT = soDT;
	}

	public LocalDate getNgaySinh() {
		return ngaySinh;
	}


	public void setNgaySinh(LocalDate ngaySinh) {
		
		if (ngaySinh == null || !ngaySinh.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Ngày sinh không hợp lệ! Phải trước ngày hiện tại.");
		}
		
	
		if (loaiHanhKhach != null) {
			int age = Period.between(ngaySinh, LocalDate.now()).getYears();
			if (loaiHanhKhach.equals("Trẻ em") && age >= 12) {
				throw new IllegalArgumentException("Ngày sinh không phù hợp với Trẻ em! Phải dưới 12 tuổi.");
			}
			if (loaiHanhKhach.equals("Người cao tuổi") && age <= 60) {
				throw new IllegalArgumentException("Ngày sinh không phù hợp với Người cao tuổi! Phải trên 60 tuổi.");
			}
		}
		this.ngaySinh = ngaySinh;
	}
	
	public String getLoaiHanhKhach() {
		return loaiHanhKhach;
	}
	
	public void setLoaiHanhKhach(String loaiHanhKhach) {
		if (loaiHanhKhach == null || !Arrays.asList("Trẻ em", "Người cao tuổi", "Sinh viên", "Người lớn").contains(loaiHanhKhach)) {
			throw new IllegalArgumentException("Loại hành khách không hợp lệ! Phải là: Trẻ em, Người cao tuổi, Sinh viên, hoặc Người lớn.");
		}
		
		// Kiểm tra dựa trên ngaySinh nếu đã set
		if (ngaySinh != null) {
			int age = Period.between(ngaySinh, LocalDate.now()).getYears();
			if (loaiHanhKhach.equals("Trẻ em") && age >= 12) {
				throw new IllegalArgumentException("Loại hành khách Trẻ em không phù hợp! Tuổi phải dưới 12.");
			}
			if (loaiHanhKhach.equals("Người cao tuổi") && age <= 60) {
				throw new IllegalArgumentException("Loại hành khách Người cao tuổi không phù hợp! Tuổi phải trên 60.");
			}
		}
		this.loaiHanhKhach = loaiHanhKhach;
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return String.format(
			"HanhKhach[maKH=%s, hoTen=%s, cmndCccd=%s, soDT=%s, ngaySinh=%s, loaiHanhKhach=%s]",
			maKH, hoTen, cmndCccd, soDT, ngaySinh.format(formatter), loaiHanhKhach
		);
	}
}