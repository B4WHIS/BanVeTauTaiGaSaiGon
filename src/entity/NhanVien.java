package entity;

import java.time.LocalDate;
import java.time.Period;

public class NhanVien {
    private String maNhanVien;
    private String hoTen;
    private LocalDate ngaySinh;
    private String soDienThoai;
    private String cmndCccd;
    private String chucVu;

    // Constructor mặc định
    public NhanVien() {}

    // Constructor đầy đủ
    public NhanVien(String maNV, String hoTen, LocalDate ngaySinh, String sdt, String cccd, String chucVu) {
        setMaNhanVien(maNV);
        setHoTen(hoTen);
        setNgaySinh(ngaySinh);
        setSoDienThoai(sdt);
        setCmndCccd(cccd);
        setChucVu(chucVu);
    }

    // Getter - Setter với kiểm tra ràng buộc
    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        if (maNhanVien == null || !maNhanVien.matches("^NV\\d{3}$"))
            throw new IllegalArgumentException("Mã nhân viên phải có dạng NVXXX");
        this.maNhanVien = maNhanVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty())
            throw new IllegalArgumentException("Họ tên không được rỗng");
        this.hoTen = hoTen;
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

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        if (!chucVu.equals("Nhân viên bán vé") && !chucVu.equals("Nhân viên quản lý"))
            throw new IllegalArgumentException("Chức vụ không hợp lệ");
        this.chucVu = chucVu;
    }

    @Override
    public String toString() {
        return "NhanVien {" +
                "maNhanVien='" + maNhanVien + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", cmndCccd='" + cmndCccd + '\'' +
                ", chucVu='" + chucVu + '\'' +
                '}';
    }
    
}
