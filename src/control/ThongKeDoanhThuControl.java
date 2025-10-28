package control;

import dao.ThongKeDoanhThuDAO;
import java.util.*;

public class ThongKeDoanhThuControl {
    private final ThongKeDoanhThuDAO doanhthu;

    public ThongKeDoanhThuControl() {
        doanhthu = new ThongKeDoanhThuDAO();
    }

    // thoogs kê
    public Map<String, Double> thongKeDoanhThuTheoNgay(Date start, Date end) {
        return doanhthu.thongKeTheoNgay(start, end);
    }

    public Map<String, Double> thongKeDoanhThuTheoThang(int year) {
        return doanhthu.thongKeTheoThang(year);
    }

    public Map<String, Double> thongKeDoanhThuTheoNam() {
        return doanhthu.thongKeTheoNam();
    }

    // lấy thông tin nhân viên và khách hàng
    public List<Object[]> thongKeChiTietNhanVien() {
        return doanhthu.thongKeChiTietNhanVien();
    }

    public List<Object[]> thongKeChiTietKhachHang() {
        return doanhthu.thongKeChiTietKhachHang();
    }
    //đếm vé 
    public int tongVeDaBan() {
    	return doanhthu.tongVeDaBan(); 
    	}
    public int tongVeDaHuy() { 
    	return doanhthu.tongVeDaHuy(); 
    	}
    public int tongVeDaHoan() { 
    	return doanhthu.tongVeDaHoan(); 
    	}

    // lấy hóa đơn
    public List<Object[]> getHoaDonTheoKhoangNgay(Date bd, Date kt) {
        return doanhthu.getHoaDonTheoKhoangNgay(bd, kt);
    }

    public List<Object[]> getHoaDonTheoNam(int nam) {
        return doanhthu.getHoaDonTheoNam(nam);
    }

    public List<Object[]> getTatCaHoaDon() {
        return doanhthu.getTatCaHoaDon();
    }
}
