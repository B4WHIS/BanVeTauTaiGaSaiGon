package control;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import dao.UuDaiDAO;
import entity.UuDai;

public class QuanLyUuDaiControl { 

    private UuDaiDAO udDao;

    public QuanLyUuDaiControl() {
        this.udDao = new UuDaiDAO();
    }


    public List<UuDai> layTatCaUuDai()  {
        return udDao.layTatCaUuDai(); 
    }

    public boolean themUuDai(UuDai ud) throws Exception {
        if (ud.getMucGiamGia() == null || ud.getMucGiamGia().compareTo(BigDecimal.ZERO) < 0 
            || ud.getMucGiamGia().compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Mức giảm giá phải nằm trong khoảng 0% đến 100%!");
        }

        return udDao.themUuDai(ud);
    }

    public boolean capNhatUuDai(UuDai ud) throws Exception {
        if (ud.getMaUuDai() == null || ud.getMaUuDai().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã Ưu đãi không được rỗng khi cập nhật.");
        }
        if (ud.getMucGiamGia() == null || ud.getMucGiamGia().compareTo(BigDecimal.ZERO) < 0 
            || ud.getMucGiamGia().compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Mức giảm giá phải nằm trong khoảng 0% đến 100%!");
        }

        return udDao.capNhatUuDai(ud);
    }

    public boolean xoaUuDai(String maUD) throws SQLException {
        return udDao.xoaUuDai(maUD); 
    }
    
    public Map<Integer, String> layTatCaLoaiUuDai() throws SQLException {
        return udDao.layTatCaLoaiUuDai(); 
    }
    public boolean themLoaiUuDai(String tenLoai) throws Exception {
        if (tenLoai == null || tenLoai.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên loại ưu đãi không được rỗng.");
        }
        
        try {
            return udDao.themLoaiUuDai(tenLoai); 
        } catch (SQLException e) {
            throw new Exception("Lỗi CSDL khi thêm Loại Ưu đãi: " + e.getMessage());
        }
    }
    public boolean capNhatLoaiUuDai(int idLoaiUD, String tenLoaiMoi) throws Exception {
        if (idLoaiUD <= 0) {
            throw new IllegalArgumentException("ID Loại Ưu đãi không hợp lệ.");
        }
        if (tenLoaiMoi == null || tenLoaiMoi.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên loại ưu đãi không được rỗng.");
        }
        
        try {
            return udDao.capNhatLoaiUuDai(idLoaiUD, tenLoaiMoi);
        } catch (SQLException e) {
            throw new Exception("Lỗi CSDL khi cập nhật Loại Ưu đãi: " + e.getMessage());
        }
    }
    
    public boolean xoaLoaiUuDai(int idLoaiUD) throws Exception {
        if (idLoaiUD <= 0) {
            throw new IllegalArgumentException("ID Loại Ưu đãi không hợp lệ.");
        }
        
        try {
            return udDao.xoaLoaiUuDai(idLoaiUD);
        } catch (SQLException e) {
            throw new Exception("Xóa Loại Ưu đãi thất bại: " + e.getMessage()); // Bao bọc thông báo lỗi FK từ DAO
        }
    }
}
