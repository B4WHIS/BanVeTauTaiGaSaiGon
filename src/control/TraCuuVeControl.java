package control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.HanhKhachDAO;
import dao.VeDAO;
import entity.HanhKhach;
import entity.Ve;

public class TraCuuVeControl {
	private VeDAO vedao;
	private HanhKhachDAO hkdao;
	
	public TraCuuVeControl() {
		this.vedao = new VeDAO();
		this.hkdao = new HanhKhachDAO();
		
		
	}
	
	public Ve timVeTheoMa(String maVe) throws Exception {
        if (maVe == null || maVe.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã vé không được rỗng!");
        }
        try {
            return vedao.layVeTheoMa(maVe); 
        } catch (SQLException e) {
            throw new Exception("Lỗi CSDL khi tìm vé theo mã: " + e.getMessage());
        }
	}
	
	public List<Ve> timVeTheoCMND(String cmndCccd) throws Exception {
	        if (cmndCccd == null || (!cmndCccd.matches("^\\d{9}$|^\\d{12}$"))) {
	            throw new IllegalArgumentException("Định dạng CMND/CCCD không hợp lệ!"); 
	        }
	
	        HanhKhach hk = hkdao.layHanhKhachTheoCMND(cmndCccd); 
	
	        if (hk != null) {
	            return vedao.layDanhSachVeTheoMaHanhKhach(hk.getMaKH());
	        } else {
	            return new ArrayList<>(); 
	        }
	}
	public List<Ve> timVeTheoSDT(String soDT) throws Exception {
	        if (soDT == null || !soDT.matches("^0\\d{9}$")) {
	            throw new IllegalArgumentException("Định dạng Số điện thoại không hợp lệ!");
	        }

	        HanhKhach hk = hkdao.layHanhKhachTheoSDT(soDT); // [13]

	        if (hk != null) {
	            return vedao.layDanhSachVeTheoMaHanhKhach(hk.getMaKH());
	        } else {
	            return new ArrayList<>(); 
	        }
	}

	public String xacThucTrangThaiVe(String maVe) throws Exception {
	        Ve ve = timVeTheoMa(maVe);
	        if (ve == null) {
	            throw new Exception("Vé không tồn tại trong hệ thống.");
	        }
	        return ve.getTrangThai(); 
	}
	public List<Ve> timVeTongHop(String maVe, String hoTen, String cmnd, String sdt, String loaiVe) throws Exception {
	    if (cmnd != null && !cmnd.trim().isEmpty() && !cmnd.matches("^\\d{9}$|^\\d{12}$")) {
	        throw new IllegalArgumentException("Định dạng CMND/CCCD không hợp lệ!");
	    }
	    if (sdt != null && !sdt.trim().isEmpty() && !sdt.matches("^0\\d{9}$")) {
	        throw new IllegalArgumentException("Định dạng Số điện thoại không hợp lệ!");
	    }
	    
	    try {
	        return vedao.timVeTongHop(maVe, hoTen, cmnd, sdt, loaiVe);
	    } catch (SQLException e) {
	        throw new Exception("Lỗi truy vấn dữ liệu khi tìm vé tổng hợp: " + e.getMessage());
	    }
	}
	
	
}
