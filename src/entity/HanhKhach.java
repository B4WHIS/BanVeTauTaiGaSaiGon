package entity;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class HanhKhach {
    private String maKH;
    private String hoTen;
    private String cmndCccd;
    private String soDT;
    private LocalDate ngaySinh;
    private UuDai maUuDai; // liên kết ưu đãi

    public HanhKhach() {}

    public HanhKhach(String maKH, String hoTen, String cmndCccd, String soDT, LocalDate ngaySinh, UuDai maUuDai) {
        setMaKH(maKH);
        setHoTen(hoTen);
        setCmndCccd(cmndCccd);
        setSoDT(soDT);
        setNgaySinh(ngaySinh);
        setUuDai(maUuDai);
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        if (maKH == null || !maKH.matches("^KH\\d{3}$"))
            throw new IllegalArgumentException("Mã khách hàng phải có dạng KHXXX");
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        if (hoTen == null || !hoTen.matches("^[A-ZÀ-Ỹ][a-zà-ỹ]*(\\s[A-ZÀ-Ỹ][a-zà-ỹ]*)*$"))
            throw new IllegalArgumentException("Họ tên không hợp lệ! Phải viết hoa chữ cái đầu, không chứa số hoặc ký tự đặc biệt.");
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
        if (soDT == null || !soDT.matches("^0\\d{9}$"))
            throw new IllegalArgumentException("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0!");
        this.soDT = soDT;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        if (ngaySinh == null || !ngaySinh.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Ngày sinh không hợp lệ! Phải trước ngày hiện tại.");
        this.ngaySinh = ngaySinh;
    }

    public UuDai getUuDai() {
        return maUuDai;
    }

    public void setUuDai(UuDai uuDai) {
        if (uuDai == null)
            throw new IllegalArgumentException("Ưu đãi không được null!");
        this.maUuDai = uuDai;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return String.format("HanhKhach[maKH=%s, hoTen=%s, cmndCccd=%s, soDT=%s, ngaySinh=%s, uuDai=%s]",
                maKH, hoTen, cmndCccd, soDT, ngaySinh.format(formatter),
                maUuDai != null ? maUuDai.getLoaiUuDai().getTenHienThi() : "Không có");
    }

  
}
