package entity;

public class TaiKhoan {
    private String tenDangNhap;
    private String matKhau;
    private NhanVien nhanVien;
//	DONE
    public TaiKhoan() {}

    public TaiKhoan(String matKhau, NhanVien nhanVien) {
        setNhanVien(nhanVien);
        this.tenDangNhap = nhanVien.getSoDienThoai();
        setMatKhau(matKhau);
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        if (!tenDangNhap.matches("^\\d{10}$"))
            throw new IllegalArgumentException("Tên đăng nhập không hợp lệ! Phải là 10 chữ số (số điện thoại).");
        this.tenDangNhap = tenDangNhap;
    }
    
    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        if (!matKhau.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$"))
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự, gồm 1 chữ hoa, 1 số và 1 ký tự đặc biệt.");
        this.matKhau = matKhau;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        if (nhanVien == null)
            throw new IllegalArgumentException("Nhân viên không được rỗng!");

        this.tenDangNhap = nhanVien.getSoDienThoai();
        this.nhanVien = nhanVien;
    }

    @Override
    public String toString() {
        return "TaiKhoan [" +
                "tenDangNhap='" + tenDangNhap + '\'' +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNhanVien() : "null") +
                ']';
    }
}
