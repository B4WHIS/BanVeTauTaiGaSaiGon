package control;

import java.sql.SQLException;
import java.util.List;

import dao.HanhKhachDAO;
import entity.HanhKhach;

public class QuanLyHanhKhachControl {
    private HanhKhachDAO hanhKhachDAO;

    public QuanLyHanhKhachControl() {
        hanhKhachDAO = new HanhKhachDAO();
    }

    public List<HanhKhach> layDanhSachHanhKhachHoatDong() throws SQLException {
        return hanhKhachDAO.getAllHanhKhachHoatDong();
    }

    public boolean themHanhKhach(HanhKhach hk) throws SQLException {
        return hanhKhachDAO.themHanhKhach(hk);
    }

    public boolean capNhatHanhKhach(HanhKhach hk) throws SQLException {
        return hanhKhachDAO.capNhatHanhKhach(hk);
    }

    public boolean xoaMemHanhKhach(String maKH) throws SQLException {
        return hanhKhachDAO.xoaMemHanhKhach(maKH);
    }

    public boolean khoiPhucHanhKhach(String maKH) throws SQLException {
        return hanhKhachDAO.khoiPhucHanhKhach(maKH);
    }

    public List<HanhKhach> timKiemHanhKhach(String tuKhoa) throws SQLException {
        return hanhKhachDAO.timKiemHanhKhach(tuKhoa);
    }
    public List<HanhKhach> timHanhKhach(String ten, String cmnd, String sdt) throws SQLException {
        return hanhKhachDAO.timHanhKhachTheoDieuKien(ten, cmnd, sdt);
    }
    
    public boolean anHanhKhach(String maHK) throws Exception {
        // 1. Kiểm tra ràng buộc nghiệp vụ (Khách hàng này còn vé/phiếu đặt chỗ không?)
        // Nếu còn, mình phải báo lỗi trước khi ẩn (tương tự logic DELETE cũ)

        // Ví dụ: Giả định có hàm check rang buộc trong DAO/Control
        // if (coVeLienQuan(maHK)) {
        //    throw new Exception("Không thể ẩn khách hàng này vì còn vé liên quan.");
        // }

        try {
            HanhKhach hkCanAn = hanhKhachDAO.layHanhKhachTheoMa(maHK); // [7]
            if (hkCanAn == null) return false;

            hkCanAn.setTrangThai("Ngừng hoạt động"); 

            return hanhKhachDAO.capNhatHanhKhach(hkCanAn); 

        } catch (SQLException e) {
            throw new Exception("Lỗi CSDL khi ẩn hành khách: " + e.getMessage());
        }
    }

}
