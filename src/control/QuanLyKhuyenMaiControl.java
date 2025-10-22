package control;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;

public class QuanLyKhuyenMaiControl {
    private KhuyenMaiDAO khuyenmai;
 
    public QuanLyKhuyenMaiControl() {
        khuyenmai = new KhuyenMaiDAO();
    }

    // Thêm khuyến mãi
    public boolean themKhuyenMai(KhuyenMai km) {
        if(km == null) {
            System.out.println("không thể thêm khuyến mãi rỗng");
            return false;
        }
        if(this.khuyenmai.TimKhuyenMaiTheoMa(km.getMaKhuyenMai()) != null) {
            System.out.println("khuyến mãi đã tồn tại, vui lòng thêm khuyến mãi mới!");
            return false;
        }
        if(km.getNgayBatDau().isAfter(km.getNgayKetThuc())) {
            System.out.println("ngày bắt đầu không được sau ngày kết thúc!");
            return false;
        }
        return khuyenmai.ThemKhuyenMai(km);
    }

    // Xóa khuyến mãi
    public boolean xoaKhuyenMai(String makm) {
        if(makm == null || makm.isEmpty()) {
            return false; 		
        }
        if(khuyenmai.TimKhuyenMaiTheoMa(makm) == null) {
            System.out.println("không tìm thấy khuyến mãi");
            return false;
        }
        return khuyenmai.XoaKhuyenMai(makm);
    }

    // Tìm khuyến mãi theo mã
    public KhuyenMai timKhuyenMaiTheoMa(String makm) {
        if(makm == null || makm.isEmpty()) {
            System.out.println("mã khuyến mãi rỗng");
            return null;
        }
        return khuyenmai.TimKhuyenMaiTheoMa(makm);
    }

    // Cập nhật khuyến mãi
    public boolean capNhatKhuyenMai(KhuyenMai km) {
        if(km == null || km.getMaKhuyenMai() == null) {
            System.out.println("khuyến mãi rỗng");
            return false;
        }
        if(khuyenmai.TimKhuyenMaiTheoMa(km.getMaKhuyenMai()) == null) {
            System.out.println("khuyến mãi không tồn tại");
            return false;
        }
        if(km.getNgayBatDau().isAfter(km.getNgayKetThuc())) {
            System.out.println("ngày bắt đầu không được sau ngày kết thúc");
            return false;
        }
        return khuyenmai.CapNhatKhuyenMai(km);
    }

    // Lấy danh sách tất cả khuyến mãi
    public ArrayList<KhuyenMai> dsKhuyenMai() {
        return khuyenmai.LayTatCaKhuyenMai();
    }

    // Lấy danh sách khuyến mãi đang hoạt động
    public ArrayList<KhuyenMai> dsKhuyenMaiHD() {
    	ArrayList<KhuyenMai> ds = khuyenmai.LayKhuyenMaiDangHoatDong();
    	ArrayList<KhuyenMai> dschua = new ArrayList<>();
    	LocalDate ngayhientai = LocalDate.now();
    	for (KhuyenMai km : ds) {
    	    if((km.getNgayBatDau().isBefore(ngayhientai) || km.getNgayBatDau().equals(ngayhientai)) && (km.getNgayKetThuc().isAfter(ngayhientai) || km.getNgayKetThuc().equals(ngayhientai))) {
    	        dschua.add(km);
    	    }
    	}
    	return dschua;

    }

    // Liệt kê khuyến mãi theo mức giảm cụ thể
    public ArrayList<KhuyenMai> timKhuyenMaiTheoMucGiam(BigDecimal mucGiam) {
        if(mucGiam == null) mucGiam = BigDecimal.ZERO;
        return khuyenmai.lietkeKhuyenMaiTheoMucGiam(mucGiam);
    }

    // Liệt kê khuyến mãi theo tên
    public ArrayList<KhuyenMai> timKhuyenMaiTheoTen(String ten) {
        if(ten == null) {
        	System.out.println("vui lòng nhập tên");
        	return null;
        }
        return khuyenmai.lietkeKhuyenMaiTheoTen(ten);
    }
}
