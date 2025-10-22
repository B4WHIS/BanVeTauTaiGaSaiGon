package control;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.ChiTietPhieuDatChoDAO;
import dao.ChoNgoiDAO;
import dao.PhieuDatChoDAO;
import entity.ChiTietPDC;
import entity.PhieuDatCho;

public class QuanLyDatChoControl {

    private PhieuDatChoDAO phieuDatCho;
    private ChoNgoiDAO choNgoi;
    private ChiTietPhieuDatChoDAO chiTietPDC;

    public QuanLyDatChoControl() {
        phieuDatCho = new PhieuDatChoDAO();
        choNgoi = new ChoNgoiDAO();
        chiTietPDC = new ChiTietPhieuDatChoDAO();
    }
    
    ///lấy toàn bộ danh sách phiếu đặt chỗ
    public ArrayList<PhieuDatCho> laydsPDC(){
    	return new ArrayList<>(phieuDatCho.getAllPhieuDatCho());
    }
    
  //cập nhật phiếu đặt chỗ 
    public boolean capnhatPDC(PhieuDatCho pdc) throws SQLException {
    	return phieuDatCho.updatePhieuDatCho(pdc);
    }
    
    //xóa phiếu đặt chỗ
    public boolean xoaPDC(String maPDC) throws SQLException {
    	return phieuDatCho.deletePhieuDatCho(maPDC);
    }
    
    //tìm phiếu đặtc h theo mã 
    public PhieuDatCho timPDCtheoma(String maPDC) throws SQLException {
    	return phieuDatCho.getPhieuDatChoByMa(maPDC);
    }
    
    // tạo phiếu đăt chỗ và câp nhật trạng thái là đã đặt
    public boolean themPDC(PhieuDatCho pdc, ArrayList<ChiTietPDC> ds) throws SQLException {
    	boolean ha = phieuDatCho.insertPhieuDatCho(pdc);
    	if(ha && ds != null) {
    		for (ChiTietPDC chiTietPDC : ds) {
				this.chiTietPDC.themChiTietPDC(chiTietPDC);
				choNgoi.updateTrangThai(chiTietPDC.getMaChoNgoi().toString(),"Đã Đặt" );
			}
    	}
    	return ha;
    }
    
    // Hủy phiếu đặt chỗ và cập nhật trạng thái là Trống cho  chỗ ngồi 
    public boolean huyPDC(String maPDC) throws SQLException {
        PhieuDatCho pdc = phieuDatCho.getPhieuDatChoByMa(maPDC);
        if (pdc == null) {
            return false; 
        }
        ArrayList<ChiTietPDC> dsChiTiet = chiTietPDC.LayChiTietTheoMaPDC(maPDC);
        for (ChiTietPDC ct : dsChiTiet) {
            choNgoi.updateTrangThai(ct.getMaChoNgoi().getMaChoNgoi(), "Trống");
        }
        pdc.setTrangThai("Đã Hủy");
        return capnhatPDC(pdc);
    }
    
    //ds phiếu đặt chỗ theo chuyến tàu
    public ArrayList<PhieuDatCho> dsPDCtheoChuyenTau(String maChuyen) throws SQLException{
    	ArrayList<PhieuDatCho> dsPDC = new ArrayList<>();
    	ArrayList<ChiTietPDC> dsCTPDC = chiTietPDC.LayTatCaChiTietPDC();
    	for (ChiTietPDC chiTietPDC : dsCTPDC) {
			if(chiTietPDC.getMaChuyenTau().getMaChuyenTau().equals(maChuyen)){
				PhieuDatCho PDC = phieuDatCho.getPhieuDatChoByMa(chiTietPDC.getMaPhieuDatCho().getMaPhieuDatCho());
				if(PDC != null && ! dsPDC.contains(PDC)) {
					dsPDC.add(PDC);
				}
			}
		}
    	return dsPDC;
    }
    
    //tìm kiếm phiếu hành khách theo mã khách hàng
    public ArrayList<PhieuDatCho> timPDCtheoMaKhachHang(String maKH) {
        ArrayList<PhieuDatCho> ketQua = new ArrayList<>();
        ArrayList<PhieuDatCho> ds = phieuDatCho.getAllPhieuDatCho();

        for (PhieuDatCho pdc : ds) {
            if (pdc.getMaHanhKhach().getMaKH().equals(maKH)) {
                ketQua.add(pdc);
            }
        }
        return ketQua;
    }
    // 



    

}