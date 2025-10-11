package entity;

public class TaiKhoan {
    private String tenDangNhap;
    private String matKhau;
    private NhanVien nhanVien;
    private String vaiTro;

// Đảm bảo vai trò với chức vụ luôn đồng bộ 
    public TaiKhoan() {}

    public TaiKhoan(String tenDangNhap, String matKhau, NhanVien nhanVien, String vaiTro) {
        setTenDangNhap(tenDangNhap);
        setMatKhau(matKhau);
        setNhanVien(nhanVien);
        setVaiTro(vaiTro);
    }

    public TaiKhoan(String tenDangNhap, String matKhau) {
        setTenDangNhap(tenDangNhap);
        setMatKhau(matKhau);
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        if (!tenDangNhap.matches("^\\d{8}$"))
            throw new IllegalArgumentException("Tên đăng nhập không hợp lệ! Phải là 8 chữ số.");
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        if (!matKhau.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$"))
            throw new IllegalArgumentException("Mật khẩu không hợp lệ!");
        this.matKhau = matKhau;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        if (nhanVien == null)
            throw new IllegalArgumentException("Nhân viên không được rỗng!");
        this.nhanVien = nhanVien;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        if (!vaiTro.equals("Nhân viên bán vé") && !vaiTro.equals("Nhân viên quản lý"))
            throw new IllegalArgumentException("Vai trò không hợp lệ!");
        this.vaiTro = vaiTro;
    }

    @Override
    public String toString() {
        return "TaiKhoan {" +
                "tenDangNhap='" + tenDangNhap + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", nhanVien=" + nhanVien +
                ", vaiTro='" + vaiTro + '\'' +
                '}';
    }
}
