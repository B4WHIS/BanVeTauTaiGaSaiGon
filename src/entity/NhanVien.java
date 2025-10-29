package entity;

import java.time.LocalDate;
import java.time.Period;
//DONE
public class NhanVien {
    private String maNhanVien;
    private String hoTen;
    private LocalDate ngaySinh;
    private String soDienThoai;
    private String cmndCccd;
    private int IDloaiChucVu;
  
    public NhanVien() {}
    
    public NhanVien(String maNhanVien, String hoTen, LocalDate ngaySinh, String soDienThoai, String cmndCccd,
			int iDloaiChucVu) {
		
		setMaNhanVien(maNhanVien);
		setHoTen(hoTen);
		setNgaySinh(ngaySinh);
		setSoDienThoai(soDienThoai);
		setCmndCccd(cmndCccd);
		setIDloaiChucVu(iDloaiChucVu);
	}

	public NhanVien(String maNhanVien) {
		setMaNhanVien(maNhanVien);
	}

	public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        if (maNhanVien == null || !maNhanVien.matches("^NV-\\d{3}$"))
            throw new IllegalArgumentException("Mã nhân viên phải có dạng NV-XXX");
        this.maNhanVien = maNhanVien;
    }
	public void setMaNhanVien(String maNhanVien) {
	    if (maNhanVien == null || !maNhanVien.matches("^NV-\\d{3}$|^NV\\d{3}$"))
	        throw new IllegalArgumentException("Mã nhân viên phải có dạng NV-XXX hoặc NVXXX");
	    this.maNhanVien = maNhanVien;
	}

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống!");
        }
        // Chuẩn hóa: Loại bỏ số và viết hoa chữ cái đầu từng từ
        String normalized = hoTen.trim().toLowerCase()
                .replaceAll("[0-9]", ""); // Loại bỏ số
        normalized = capitalizeEachWord(normalized); // Viết hoa chữ cái đầu
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Họ tên không hợp lệ! Phải viết hoa chữ cái đầu, không chứa số.");
        }
        this.hoTen = normalized;
    }

    // Phương thức viết hoa chữ cái đầu từng từ
    private String capitalizeEachWord(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase())
                      .append(" ");
            }
        }
        return result.toString().trim();
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        if (ngaySinh == null || Period.between(ngaySinh, LocalDate.now()).getYears() < 18)
            throw new IllegalArgumentException("Nhân viên phải >= 18 tuổi");
        this.ngaySinh = ngaySinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String sdt) {
        if (!sdt.matches("^\\d{10}$"))
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        this.soDienThoai = sdt;
    }

    public String getCmndCccd() {
        return cmndCccd;
    }

    public void setCmndCccd(String cccd) {
        if (!cccd.matches("^(\\d{9}|\\d{12})$"))
            throw new IllegalArgumentException("CMND/CCCD phải có 9 hoặc 12 chữ số");
        this.cmndCccd = cccd;
    }

	public int getIDloaiChucVu() {
		return IDloaiChucVu;
	}

	public void setIDloaiChucVu(int iDloaiChucVu) {
		IDloaiChucVu = iDloaiChucVu;
	}

	@Override
	public String toString() {
		return "NhanVien [maNhanVien=" + maNhanVien + ", hoTen=" + hoTen + ", ngaySinh=" + ngaySinh + ", soDienThoai="
				+ soDienThoai + ", cmndCccd=" + cmndCccd + ", IDloaiChucVu=" + IDloaiChucVu + "]";
	}
	

    
}