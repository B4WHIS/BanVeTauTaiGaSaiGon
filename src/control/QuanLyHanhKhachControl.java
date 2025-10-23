package control;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import dao.HanhKhachDAO;
import entity.HanhKhach;

public class QuanLyHanhKhachControl {
	private HanhKhachDAO hkDao;
	
	public QuanLyHanhKhachControl() {
		this.hkDao = new HanhKhachDAO();
	}
	
	public boolean themHanhKhach(HanhKhach hk) throws Exception {
        if (hk.getNgaySinh() == null || hk.getNgaySinh().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày sinh không hợp lệ!");
        }
        try {
            return hkDao.themHanhKhach(hk); 
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key")) {
                 throw new Exception("Lỗi: CMND/CCCD hoặc Số điện thoại đã tồn tại trong hệ thống.");
            }
            throw new Exception("Lỗi CSDL khi thêm hành khách: " + e.getMessage());
        }	
	}
	public List<HanhKhach> layDanhSachHanhKhach() throws SQLException{
		return hkDao.layDanhSachHanhKhach();
	}
	
	public boolean capNhatHanhKhach(HanhKhach hk) throws Exception {
		if(hk.getMaKH() == null) {
			throw new IllegalArgumentException("Mã khách hàng không được rỗng khi cập nhật.");
		}
        if (hk.getNgaySinh() == null || hk.getNgaySinh().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày sinh không hợp lệ!");
        }
        try {
        	return hkDao.capNhatHanhKhach(hk);
  
        }catch (SQLException e) {
			// TODO: handle exception
            throw new Exception("Lỗi CSDL khi cập nhật hành khách: " + e.getMessage());
		}
	}
    public boolean xoaHanhKhach(String maHK) throws SQLException {
        return hkDao.xoaHanhKhach(maHK); 
    }
    public HanhKhach traCuuTheoCMND(String cmndCccd) throws SQLException {
        if (cmndCccd == null || (!cmndCccd.matches("^\\d{9}$|^\\d{12}$"))) {
            throw new IllegalArgumentException("Định dạng CMND/CCCD không hợp lệ!");
        }
        return hkDao.layHanhKhachTheoCMND(cmndCccd); 

    }
    public HanhKhach traCuuTheoSDT(String soDT) throws SQLException {
        if (soDT == null || !soDT.matches("^0\\d{9}$")) { 
            throw new IllegalArgumentException("Định dạng Số điện thoại không hợp lệ (phải gồm 10 chữ số)!");
        }
        return hkDao.layHanhKhachTheoSDT(soDT); // [17]
    }
    
	
}
