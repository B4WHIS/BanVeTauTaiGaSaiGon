package entity;

public class TaiKhoan {
    private String tenDangNhap;
    private String matKhau;
    private NhanVien nhanVien;
   

    public TaiKhoan() {}

    // Constructor có đầy đủ tham số
    public TaiKhoan(String matKhau, NhanVien nhanVien) {
        setNhanVien(nhanVien);
        setMatKhau(matKhau);
        // tenDangNhap và vaiTro sẽ tự động đồng bộ từ nhanVien
    }

    // Constructor đăng nhập (chỉ cần tên đăng nhập + mật khẩu)
    public TaiKhoan(String tenDangNhap, String matKhau) {
        setTenDangNhap(tenDangNhap);
        setMatKhau(matKhau);
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    // Chỉ cho phép set nếu không có nhân viên gắn kèm
    public void setTenDangNhap(String tenDangNhap) {
        if (nhanVien != null)
            throw new IllegalStateException("Không thể đặt tên đăng nhập thủ công khi đã có nhân viên!");
        if (!tenDangNhap.matches("^\\d{10}$"))
            throw new IllegalArgumentException("Tên đăng nhập không hợp lệ! Phải là số điện thoại 10 chữ số.");
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
            throw new IllegalArgumentException("Nhân viên không được null!");

        this.nhanVien = nhanVien;

        // ✅ Tự động lấy số điện thoại làm tên đăng nhập
        this.tenDangNhap = nhanVien.getSoDienThoai();

        
    }

    @Override
    public String toString() {
        return "TaiKhoan {" +
                "tenDangNhap='" + tenDangNhap + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getHoTen() : "null") +
                +
                '}';
    }
}
