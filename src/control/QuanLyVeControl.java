package control;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import dao.ChoNgoiDAO;
import dao.ChuyenTauDAO;
import dao.HanhKhachDAO;
import dao.KhuyenMaiDAO;
import dao.LichSuVeDAO;
import dao.ThongKeDoanhThuDAO;
import dao.UuDaiDAO;
import dao.VeDAO;
import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.HanhKhach;
import entity.KhuyenMai;
import entity.LichSuVe;
import entity.NhanVien;
import entity.ThongKeDoanhThu;
import entity.UuDai;
import entity.Ve;

public class QuanLyVeControl {
	private VeDAO veDao;
	private HanhKhachDAO hkDao;
	private ChoNgoiDAO cnDao;
	private ChuyenTauDAO ctDao;
	private UuDaiDAO udDao;
	private LichSuVeDAO lsvDao;
	private KhuyenMaiDAO kmDao;
	private LichSuVeDAO lsv;
	private ThongKeDoanhThuDAO tkdtDao;
	private static final int ID_BAN_VE = 1;
	private static final int ID_HUY_VE = 2;
	private static final int ID_DOI_VE = 3;
	
	public QuanLyVeControl() {
		this.veDao = new VeDAO();
		this.hkDao = new HanhKhachDAO();
		this.cnDao = new ChoNgoiDAO();
		this.ctDao = new ChuyenTauDAO();
		this.udDao = new UuDaiDAO();
		this.lsvDao = new LichSuVeDAO();
		this.kmDao = new KhuyenMaiDAO();
	}
	
	public boolean datVe(Ve veMoi, HanhKhach hkMoi, NhanVien nvlap) throws Exception {
		HanhKhach finalHK = hkMoi;
		
		try {
			HanhKhach hanhkhachHienCo = null;
			//xử lí và xác thực
			if (hkMoi.getCmndCccd() != null && !hkMoi.getCmndCccd().trim().isEmpty()) {
				hanhkhachHienCo = hkDao.layHanhKhachTheoCMND(hkMoi.getCmndCccd());
			}
			//khách cũ
			if (hanhkhachHienCo != null) {
				hkMoi.setMaKH(hanhkhachHienCo.getMaKH()); //giữ lại mã
				hkDao.capNhatHanhKhach(hkMoi);
				finalHK = hkMoi;
			}else {
				//khách mới
				hkDao.themHanhKhach(hkMoi);
				finalHK = hkMoi;
			}
			veMoi.setMaHanhkhach(finalHK);
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			throw new IllegalAccessException("Thông tin hành khách không hợp lệ: " + e.getMessage());
			
		}catch (SQLException e) {
			// TODO: handle exception
			throw new Exception("Lỗi CSDL" + e.getMessage());
		}
		//Tính toán
	    try {
	        BigDecimal giaThanhToan = tinhGiaVeCuoiCung(veMoi.getGiaVeGoc(), finalHK.getMaUuDai(), veMoi.getMaKhuyenMai());
	        
	        if (giaThanhToan.compareTo(BigDecimal.ZERO) <= 0) {
	            throw new Exception("Giá thanh toán cuối cùng không hợp lệ.");
	        }
	        veMoi.setGiaThanhToan(giaThanhToan);
	        
	        //cập nhật chỗ ngồi
	        String maChoNgoi = veMoi.getMaChoNgoi().getMaChoNgoi();
	        cnDao.updateTrangThai(maChoNgoi, "Đã đặt"); 
	        
	        return veDao.themVe(veMoi); 
	        
	    } catch (Exception e) {
	        throw new Exception("Lỗi trong quá trình tính toán giá" + e.getMessage());
	    }
	}
	
	//HỦY VÉ
	public BigDecimal tinhPhiHuy(BigDecimal giaGoc, LocalDateTime thoiGianKhoiHanh) {
		LocalDateTime thoiGianNOW = LocalDateTime.now();
		
		Duration duration = Duration.between(thoiGianNOW, thoiGianKhoiHanh);
		
		long gioConLai = duration.toHours();
		
		//không được hủy
		if(gioConLai < 4) {
			return null; 
		}
		
		BigDecimal tiLePhi;
		
		if(gioConLai >= 24) {
			tiLePhi = new BigDecimal("0.10");
		}else if(gioConLai >= 12) {
			tiLePhi = new BigDecimal("0.20");
		}else {
			tiLePhi = new BigDecimal("0.30");
		}
		return giaGoc.multiply(tiLePhi).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public LichSuVe xuLyHuyVe(String maVe, String liDoHuy, NhanVien nvThucHien) throws Exception {
		//timVe
		Ve ve = veDao.layVeTheoMa(maVe);
		if(ve == null) {
			throw new Exception("Không tìm thấy vé có mã: "+ maVe);
		}
		
		if (ve.getTrangThai().equalsIgnoreCase("Đã hủy") ||ve.getTrangThai().equalsIgnoreCase("Đã sử dụng")) {
			throw new Exception("Vé đã được hủy hoặc đã sử dụng");
		}
		
		//lấy thông tin ct
	    ChuyenTau chuyenTau = ctDao.getChuyenTauByMaChuyenTau(ve.getMaChuyenTau().getMaChuyenTau());

	    if (chuyenTau == null) {
	        throw new Exception("Không tìm thấy thông tin chuyến tàu liên quan.");
	    }
	    
	    BigDecimal giaVeGoc = ve.getGiaVeGoc();
	    LocalDateTime thoiGianKhoiHanh = chuyenTau.getThoiGianKhoiHanh();
	    
	    BigDecimal phiHuy = tinhPhiHuy(giaVeGoc, thoiGianKhoiHanh);
	    
	    if (phiHuy == null) {
	        throw new Exception("Vé không đủ điều kiện hủy (phải hủy trước giờ khởi hành ít nhất 4 giờ).");
	    }

	    // Tính số tiền hoàn lại
	    BigDecimal tienHoanLai = ve.getGiaThanhToan().subtract(phiHuy);
	    if (tienHoanLai.compareTo(BigDecimal.ZERO) < 0) {
	        tienHoanLai = BigDecimal.ZERO;
	    }
	    
	    // 4. THỰC HIỆN GIAO DỊCH (Update)
	    try {
	    	if(!veDao.capNhatTrangThaiVe(maVe, "Đã hủy")) {
	    		throw new SQLException("Lỗi cập nhật trạng thái vé");
	    	}
	    	  
	        String maChoNgoi = ve.getMaChoNgoi().getMaChoNgoi();
	        
	        if(!cnDao.updateTrangThai(maChoNgoi, "Trống")) {
	        	throw new SQLException("Lỗi cập nhật trạng thái chỗ ngồi");
	        }
	        
	        LichSuVe lichSu = new LichSuVe(null, ID_HUY_VE, LocalDateTime.now(), maVe, nvThucHien.getMaNhanVien()); 
	        lichSu.setLyDo(liDoHuy + " | tiền hoàn lại: " + tienHoanLai.toString());
	        lichSu.setPhiXuLy(phiHuy);
	        lichSu.setMaHanhKhach(ve.getMaHanhkhach().getMaKH());
	        
	        if(!lsvDao.themLichSuVe(lichSu)) {
	        	throw new SQLException("Lỗi ghi lịch sử hủy vé");
	        }
	        return lichSu;
	        
	 
	        
	    } catch (SQLException e) {
	        throw new Exception("Lỗi CSDL khi hủy vé: " + e.getMessage());
	    }
	    
	}
	
	//Đổi vé
    public boolean doiVe(String maVeCu, ChuyenTau ctMoi, ChoNgoi cnMoi, NhanVien nv) throws Exception {
    	
    	//kiểm tra vé cũ
    	Ve veCu = veDao.layVeTheoMa(maVeCu);
    	if (veCu == null) {
    		throw new Exception("Không tìm thấy vé có mã " + maVeCu);
    	}
    	
    	//kiểm tra thời gian
    	LocalDateTime TGKH_Cu = veCu.getMaChuyenTau().getThoiGianKhoiHanh();
    	if(TGKH_Cu.minusHours(4).isBefore(LocalDateTime.now())) {
    		throw new Exception("Vé không đủ điều kiện đổi, phải đổi trước giờ khởi hành 4 giờ.");
    	}
    	
    	//Kiểm tra chỗ ngồi mới
    	ChoNgoi trangThaiChoMoi = cnDao.getChoNgoiByMa(cnMoi.getMaChoNgoi());
    	if (trangThaiChoMoi == null || !trangThaiChoMoi.getTrangThai().equalsIgnoreCase("Trống")) {
			throw new Exception("Chỗ ngồi mới đã có người đặt hoặc không tồn tại");
		}
    	
    	//tính toán giá vé mới
    	BigDecimal giaCu = veCu.getGiaThanhToan();
    	BigDecimal giaVeGocMoi = ctMoi.getGiaChuyen();
    	
    	BigDecimal giaMoiThanhToan = tinhGiaVeCuoiCung(giaVeGocMoi, veCu.getMaHanhkhach().getMaUuDai(), veCu.getMaKhuyenMai());
    	
    	BigDecimal chenhLech = giaMoiThanhToan.subtract(giaCu);
    	
    	try {
	    	//Giao dịch
	    	if(!cnDao.updateTrangThai(veCu.getMaChoNgoi().getMaChoNgoi(), "Trống")) {
	    		throw new SQLException("Lỗi chỗ ngồi cũ.");
	    	}
	    	
	    	if(!veDao.capNhatTrangThaiVe(maVeCu,"Đã đổi")) {
	    		throw new SQLException("Lỗi cập nhật trạng thái vé cũ");
	    	}
	    	
	    	//Thêm vé mới
	        Ve veMoi = new Ve();
	        
	        veMoi.setMaChuyenTau(ctMoi); 
	        veMoi.setMaChoNgoi(cnMoi); 
	        veMoi.setGiaVeGoc(giaVeGocMoi); 
	        veMoi.setGiaThanhToan(giaMoiThanhToan);
	        veMoi.setMaHanhkhach(veCu.getMaHanhkhach()); 
	        veMoi.setMaNhanVien(nv); 
	        veMoi.setNgayDat(LocalDateTime.now());
	        veMoi.setTrangThai("Khả dụng"); 
	        
	        if(!veDao.themVe(veMoi)) {
	        	throw new SQLException("Lỗi thêm vé mới");
	        }
	        if(!cnDao.updateTrangThai(cnMoi.getMaChoNgoi(), "Đã đặt")) {
	        	throw new SQLException("Lỗi cập nhật trạng thái vé");
	        }
	        
	        //ghi lịch sửa
	        LichSuVe lichSu = new LichSuVe(null, ID_DOI_VE, LocalDateTime.now(), veMoi.getMaVe(), nv.getMaNhanVien());
	        lichSu.setPhiXuLy(chenhLech.abs());
	        lichSu.setMaHanhKhach(veCu.getMaHanhkhach().getMaKH());
	        lichSu.setLyDo("Đổi vé từ " + maVeCu + "sang " + veMoi.getMaVe() + ". Chênh lệch: " + chenhLech.toString()); 
	        if(!lsvDao.themLichSuVe(lichSu)) {
	        	throw new SQLException("Lỗi ghi lịch sử đổi vé");
	        }
	        
	        ThongKeDoanhThu tkHienTai = tkdtDao.getThongKeTheoNgayVaNV(LocalDate.now(), nv.getMaNhanVien());
	        if(tkHienTai != null) {
	        	tkHienTai.setTongSoHoanDoi(tkHienTai.getTongSoHoanDoi() + 1);
	        	tkdtDao.updateThongKe(tkHienTai);
	        }else {
	        	ThongKeDoanhThu tkMoi = new ThongKeDoanhThu(null, LocalDate.now(), nv.getMaNhanVien(), BigDecimal.ZERO, 0);
	        	tkMoi.setTongSoHoanDoi(1);
	        	tkdtDao.insertThongKe(tkMoi);
	        }
	        
	        //Thông báo chênh lệch
	        if(chenhLech.compareTo(BigDecimal.ZERO) > 0) {
	        	System.out.println("Cần thanh toán thêm: " + chenhLech.toString());
	        }else if(chenhLech.compareTo(BigDecimal.ZERO) < 0) {
	        	System.out.println("Cần hoàn tiền lại: " + chenhLech.abs().toString());
	        }
	        return true;
    	}  catch (Exception e) {
			// TODO: handle exception
    		throw new Exception("Đổi vé thất bại do lỗi giao dịch: "+ e.getMessage());
		} 
    }

	private BigDecimal tinhGiaVe(ChuyenTau ctMoi, ChoNgoi cnMoi, KhuyenMai maKhuyenMai) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public BigDecimal tinhGiaVeCuoiCung(BigDecimal giaGoc, String maUuDai, KhuyenMai kmDinhKem) throws Exception {
	    if (giaGoc == null || giaGoc.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new Exception("Giá vé gốc không hợp lệ.");
	    }

	    BigDecimal giaThanhToan = giaGoc;

	    UuDai ud = udDao.timUuDaiTheoMa(maUuDai);
	    if (ud != null) {
	        BigDecimal mucGiamGiaPT = ud.getMucGiamGia().divide(new BigDecimal("100"));
	        BigDecimal soTienGiam = giaGoc.multiply(mucGiamGiaPT);
	        giaThanhToan = giaGoc.subtract(soTienGiam);
	    }
	    
	    if (kmDinhKem != null && kmDinhKem.getMaKhuyenMai() != null) {
	        KhuyenMai kmKhaDung = kmDao.TimKhuyenMaiTheoMa(kmDinhKem.getMaKhuyenMai());
	        
	        if (kmKhaDung != null && kmKhaDung.getNgayKetThuc().isAfter(LocalDate.now())) {
	            BigDecimal mucGiamGiaKM = kmKhaDung.getMucGiamGia().divide(new BigDecimal("100"));
	            giaThanhToan = giaThanhToan.subtract(giaThanhToan.multiply(mucGiamGiaKM));
	        }
	    }
	    return giaThanhToan.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	public class MockData {
	    public static NhanVien taoNhanVienTest() {
	        try {
	            // Giả lập nhân viên NV001, 20 tuổi [8]
	            return new NhanVien("NV001", "Nguyen Van B", LocalDate.now().minusYears(20), "0901234567", "123456789", 1); 
	        } catch (Exception e) {
	            System.err.println("Lỗi tạo NV giả lập: " + e.getMessage());
	            return null;
	        }
	    }

	    public static KhuyenMai taoKhuyenMaiGiaLap(boolean hopLe) {
	        try {
	            // KM001: Giảm 10%, Hạn cuối ngày hôm nay hoặc đã hết hạn
	            LocalDate ngayKetThuc = LocalDate.now().plusMonths(1); // Kiểm tra ràng buộc ngày kết thúc [9]
	            return new KhuyenMai("KMTEST001", "Giam 10%", new BigDecimal("10"), LocalDate.now().minusDays(10), ngayKetThuc, "Khong dieu kien");
	        } catch (Exception e) {
	            System.err.println("Lỗi tạo KM giả lập: " + e.getMessage());
	            return null;
	        }
	    }
	}
	 private UuDai timUuDaiTest(String maUuDai) {
         if ("UD001".equals(maUuDai)) {
             try {
                 // Ưu đãi giảm 10% [13]
                 return new UuDai("UD001", 1, new BigDecimal("10"), "Khach hang than thiet"); 
             } catch (Exception e) {
                 return null;
             }
         }
         return null; // Không tìm thấy ưu đãi
     }
	
	
}
