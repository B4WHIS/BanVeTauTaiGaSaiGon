package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HanhKhach {
	private String maKH;
	private String hoTen;
	private String cmndCccd;
	private String soDT;
	private LocalDate ngaySinh;
//  Thêm: loaiHanhKhach
//	Sửa: Ràng buộc setCmndCccd
//	Nếu không phải Trẻ em, cmndCccd là bắt buộc và phải đúng định dạng (9 hoặc 12 chữ số).
	public HanhKhach() {
		super();
	}

	public HanhKhach(String maKH) {
		setMaKH(maKH);
	}

	public HanhKhach(LocalDate ngaySinh, String soDT, String hoTen, String maKH, String cmndCccd) {
		super();
		setNgaySinh(ngaySinh);
		setSoDT(soDT);
		setHoTen(hoTen);
		this.maKH = maKH;
		setCmndCccd(cmndCccd);
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
		// Viết hoa chữ cái đầu, không chứa số hoặc ký tự đặc biệt
		if (hoTen == null || !hoTen.matches("^[A-ZÀ-Ỹ][a-zà-ỹ]*(\\s[A-ZÀ-Ỹ][a-zà-ỹ]*)*$")) {
			throw new IllegalArgumentException("Họ tên không hợp lệ! Phải viết hoa chữ cái đầu, không chứa số hoặc ký tự đặc biệt.");
		}
		this.hoTen = hoTen;
	}

	public String getCmndCccd() {
		return cmndCccd;
	}
	
	//chấp nhận cả 9 chữ số nữa
	public void setCmndCccd(String cmndCccd) {
		// Gồm 12 ký tự số
		if (cmndCccd == null || !cmndCccd.matches("^\\d{12}$")) {
			throw new IllegalArgumentException("CMND/CCCD phải gồm đúng 12 ký tự số!");
		}
		this.cmndCccd = cmndCccd;
	}

	public String getSoDT() {
		return soDT;
	}

	public void setSoDT(String soDT) {
		// Gồm 10 ký tự số, bắt đầu bằng 0
		if (soDT == null || !soDT.matches("^0\\d{9}$")) {
			throw new IllegalArgumentException("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0!");
		}
		this.soDT = soDT;
	}

	public LocalDate getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(LocalDate ngaySinh) {
		// Không phải ngày hiện tại hoặc sau hiện tại
		if (ngaySinh == null || !ngaySinh.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Ngày sinh không hợp lệ! Phải trước ngày hiện tại.");
		}
		this.ngaySinh = ngaySinh;
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return String.format(
			"HanhKhach[maKH=%s, hoTen=%s, cmndCccd=%s, soDT=%s, ngaySinh=%s]",
			maKH, hoTen, cmndCccd, soDT, ngaySinh.format(formatter)
		);
	}
}
