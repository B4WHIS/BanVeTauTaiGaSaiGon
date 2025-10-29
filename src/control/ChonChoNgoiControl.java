package control;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import dao.ChoNgoiDAO;
import dao.GaDAO;
import dao.TauDAO;
import dao.ToaTauDAO;
import entity.ChoNgoi;
import entity.ToaTau;

// Control chịu trách nhiệm giao tiếp với DAO và xử lý nghiệp vụ
public class ChonChoNgoiControl {
    
    // Khai báo các DAO cần thiết
    private ToaTauDAO toaTauDAO;
    private ChoNgoiDAO choNgoiDAO;
    private TauDAO tauDao;
    private GaDAO gaDAO;
    
    public ChonChoNgoiControl() {
        this.toaTauDAO = new ToaTauDAO();
        this.choNgoiDAO = new ChoNgoiDAO();
        this.tauDao = new TauDAO(); 
        this.gaDAO = new GaDAO();
    }
    
    /**
     * Lấy danh sách Toa tàu theo Mã tàu.
     * @param maTau Mã tàu của chuyến tàu được chọn.
     * @return Danh sách ToaTau.
     * @throws SQLException 
     */
    public List<ToaTau> layDanhSachToa(String maTau) throws SQLException {
        // Gọi DAO để lấy dữ liệu thực [3]
        return toaTauDAO.getAllToaTauByMaTau(maTau); 
    }
    
    /**
     * Lấy danh sách Chỗ ngồi và trạng thái (Đã đặt/Trống) theo Mã toa.
     * CHÚ Ý: Logic này hiện tại dựa vào cột trangThai trong ChoNgoi [4, 5].
     * Nếu chuyển sang bán vé trực tiếp, trạng thái này phải được cập nhật dựa trên bảng Vé.
     * 
     * @param maToa Mã toa tàu.
     * @return Danh sách ChoNgoi.
     * @throws SQLException
     */
    public List<ChoNgoi> layDanhSachChoNgoi(String maToa) throws SQLException {
        // Gọi DAO để lấy dữ liệu thực [6]
        return choNgoiDAO.getSeatsByMaToa(maToa);
    }
    
    public String layTenTau(String maTau) throws SQLException {
        return tauDao.getTenTauByMaTau(maTau); 
    }

    public String layTenGaDi(String maLichTrinh) throws SQLException {
        return gaDAO.getTenGaDiByMaLichTrinh(maLichTrinh);
    }

    public String layTenGaDen(String maLichTrinh) throws SQLException {
        return gaDAO.getTenGaDenByMaLichTrinh(maLichTrinh);
    }
    
    /**
     * Logic nghiệp vụ: Xác định loại chỗ dựa trên hệ số giá của Toa tàu.
     * (Logic này được chuyển từ GUI [7] sang Control)
     */
    public String layLoaiCho(BigDecimal heSoGia) {
        if (heSoGia == null) return "Ghế Thường";
        // Giữ logic xác định loại chỗ dựa trên hệ số giá (từ logic cũ) [7]
        if (heSoGia.doubleValue() >= 2.0) return "Giường Nằm"; 
        if (heSoGia.doubleValue() >= 1.1) return "Ghế Mềm"; 
        return "Ghế Thường";
    }

    /**
     * Logic nghiệp vụ: Đếm số chỗ trống trong danh sách chỗ ngồi.
     * @param danhSachCho Danh sách ChoNgoi.
     * @return Số chỗ trống.
     */
    public int demSoChoTrong(List<ChoNgoi> danhSachCho) {
         if (danhSachCho == null) return 0;
         
         // Lọc các chỗ KHÔNG phải là "Đã đặt" (hoặc "Đã bán") [8-10]
         return (int) danhSachCho.stream().filter(c -> {
             String trangThai = (c.getTrangThai() != null) ? c.getTrangThai().trim() : "";
             // Kiểm tra nếu trạng thái KHÔNG PHẢI là "Đã đặt" (occupied) [8]
             return !"Đã đặt".equalsIgnoreCase(trangThai); 
         }).count();
    }
}