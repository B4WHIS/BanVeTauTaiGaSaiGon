package control;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.ChiTietPhieuDatChoDAO;
import dao.ChuyenTauDAO;
import dao.GaDAO;
import dao.TauDAO;
import dao.ToaTauDAO;
import dao.VeDAO;
import entity.ChuyenTau;
import entity.Ga;
import entity.ToaTau;
import gui.GiaoDienTraCuuChuyentau;

// CHUẨN HÓA LUỒNG: GUI -> Control -> DAO
public class TraCuuChuyenTauControl {
    private GiaoDienTraCuuChuyentau gui;
    
    // Khởi tạo các DAO cần thiết
    private ChuyenTauDAO chuyenTauDAO;
    private GaDAO gaDAO;
    private ToaTauDAO toaTauDAO;
    private ChiTietPhieuDatChoDAO ctpdcDAO; // Tạm thời dùng để đếm chỗ bị chiếm dụng
    private TauDAO tauDAO;
    private VeDAO veDAO;
    
    public TraCuuChuyenTauControl(GiaoDienTraCuuChuyentau gui) {
        this.gui = gui;
        // Khởi tạo DAO (Không nên khởi tạo trong GUI)
        this.chuyenTauDAO = new ChuyenTauDAO();
        this.gaDAO = new GaDAO();
        this.toaTauDAO = new ToaTauDAO();
        this.ctpdcDAO = new ChiTietPhieuDatChoDAO();
        this.tauDAO = new TauDAO();
        this.veDAO = new VeDAO(); 
    }
    
    // Logic 1: Tải dữ liệu Ga (Ga đi/Ga đến)
    public List<String> loadGaData() {
        List<Ga> danhSachGa = gaDAO.getAllGa();
        List<String> tenGaList = new ArrayList<>();
        for (Ga ga : danhSachGa) {
            tenGaList.add(ga.getTenGa());
        }
        return tenGaList;
    }
    
    // Logic 2: Thực hiện tìm kiếm chuyến tàu
    public List<ChuyenTau> timChuyenTau(String tenGaDi, String tenGaDen, Date date) throws SQLException {
        if (tenGaDi == null || tenGaDen == null || date == null) {
            // Validate đầu vào thô. Validation chi tiết nên đặt ở GUI hoặc tầng này
            return null; 
        }

        String maGaDi = gaDAO.getMaGaByTenGa(tenGaDi);
        String maGaDen = gaDAO.getMaGaByTenGa(tenGaDen);
        
        if (maGaDi == null || maGaDen == null) {
            // Lỗi dữ liệu ga, nên xử lý ở GUI
            throw new IllegalArgumentException("Không tìm thấy mã Ga tương ứng.");
        }

        LocalDate ngayKhoiHanh = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        // Gọi DAO để truy vấn dữ liệu thật [2]
        List<ChuyenTau> ketQua = chuyenTauDAO.getChuyenTauTheoNgayVaGa(ngayKhoiHanh, maGaDi, maGaDen);
        
        return ketQua;
    }

    // Logic 3: Lấy thông tin chi tiết (tên tàu, số chỗ) cho một chuyến tàu
    // Logic này được chuyển từ phương thức laySoLuongGheCuaChuyen trong GUI 
    public Map<String, Object> getThongTinChuyenTau(ChuyenTau ct) throws SQLException {
        Map<String, Object> info = new HashMap<>();
        
        // 1. Lấy Tên Tàu [3]
        String tenTau = tauDAO.getTenTauByMaTau(ct.getMaTau());
        info.put("tenTau", tenTau);
        String maCT = ct.getMaChuyenTau();

        // 2. FIX LOGIC ĐẾM CHỖ ĐÃ ĐẶT (Sử dụng VeDAO)
        
        // VeDAO.getDanhSachChoDaDat: Truy vấn CSDL để lấy danh sách mã chỗ ngồi có trạng thái = N'Đã đặt' [5]
        List<String> danhSachChoDaDat = veDAO.getDanhSachChoDaDat(maCT);
        int slChoDaDat = danhSachChoDaDat.size(); // Số lượng chỗ đã đặt thực tế

        // 3. Tính Tổng số chỗ [4]
        int tongSoCho = 0;
        String maTau = ct.getMaTau();
        List<ToaTau> danhSachToa = toaTauDAO.getAllToaTauByMaTau(maTau); 
        for (ToaTau toa : danhSachToa) {
            tongSoCho += toa.getSoLuongCho();
        }
        
        int slChoTrong = Math.max(0, tongSoCho - slChoDaDat);

        info.put("slChoDaDat", slChoDaDat);
        info.put("slChoTrong", slChoTrong);
        return info;
    }
}