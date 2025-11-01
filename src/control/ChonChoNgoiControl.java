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

public class ChonChoNgoiControl {
    
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
    
    public List<ToaTau> layDanhSachToa(String maTau) throws SQLException {
        return toaTauDAO.getAllToaTauByMaTau(maTau); 
    }
    
    public List<ChoNgoi> layDanhSachChoNgoi(String maToa) throws SQLException {
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
    
    public String layLoaiCho(BigDecimal heSoGia) {
        if (heSoGia == null) return "Ghế Thường";

        if (heSoGia.doubleValue() >= 2.0) return "Giường Nằm"; 
        if (heSoGia.doubleValue() >= 1.1) return "Ghế Mềm"; 
        return "Ghế Thường";
    }

    public int demSoChoTrong(List<ChoNgoi> danhSachCho) {
         if (danhSachCho == null) return 0;
         
         return (int) danhSachCho.stream().filter(c -> {
             String trangThai = (c.getTrangThai() != null) ? c.getTrangThai().trim() : "";

             return !"Đã đặt".equalsIgnoreCase(trangThai); 
         }).count();
    }
}