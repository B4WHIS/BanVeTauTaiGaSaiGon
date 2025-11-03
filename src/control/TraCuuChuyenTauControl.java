package control;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Khởi tạo các DAO
    private final ChuyenTauDAO chuyenTauDAO = new ChuyenTauDAO();
    private final GaDAO gaDAO = new GaDAO();
    private final ToaTauDAO toaTauDAO = new ToaTauDAO();
    private final VeDAO veDAO = new VeDAO();
    private final TauDAO tauDAO = new TauDAO();

    public TraCuuChuyenTauControl(GiaoDienTraCuuChuyentau gui) {
        this.gui = gui;
    }

    // 1. Tải danh sách tên ga
    public List<String> loadGaData() {
        List<Ga> danhSachGa = gaDAO.getAllGa();
        List<String> tenGaList = new ArrayList<>();
        for (Ga ga : danhSachGa) {
            tenGaList.add(ga.getTenGa());
        }
        return tenGaList;
    }

    // 2. Validate input (ga đi ≠ ga đến, ngày hợp lệ)
    public boolean validateInput(String gaDi, String gaDen, Date date) {
        if (gaDi == null || gaDen == null || date == null) return false;
        if (gaDi.trim().isEmpty() || gaDen.trim().isEmpty()) return false;
        if (gaDi.equals(gaDen)) return false;

        LocalDate ngayDi = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate homNay = LocalDate.now();
        return !ngayDi.isBefore(homNay);
    }

    // 3. Lấy mã ga theo tên (ẩn DAO)
    public String getMaGaByTen(String tenGa) {
        if (tenGa == null || tenGa.trim().isEmpty()) return null;
        return gaDAO.getMaGaByTenGa(tenGa.trim());
    }

    // 4. Tìm chuyến tàu theo ga + ngày
    public List<ChuyenTau> timChuyenTau(String tenGaDi, String tenGaDen, Date date) throws SQLException {
        if (!validateInput(tenGaDi, tenGaDen, date)) {
            return new ArrayList<>(); // Trả rỗng nếu input sai
        }

        String maGaDi = getMaGaByTen(tenGaDi);
        String maGaDen = getMaGaByTen(tenGaDen);

        if (maGaDi == null || maGaDen == null) {
            return new ArrayList<>();
        }

        LocalDate ngayKhoiHanh = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return chuyenTauDAO.getChuyenTauTheoNgayVaGa(ngayKhoiHanh, maGaDi, maGaDen);
    }

    // 5. Lấy thông tin hiển thị: tên tàu, số chỗ đã đặt, chỗ trống
    public Map<String, Object> getThongTinChuyenTau(ChuyenTau ct) throws SQLException {
        Map<String, Object> info = new HashMap<>();

        // Tên tàu
        String tenTau = tauDAO.getTenTauByMaTau(ct.getMaTau());
        info.put("tenTau", tenTau != null ? tenTau : ct.getMaChuyenTau());

        String maCT = ct.getMaChuyenTau();

        // Số chỗ đã đặt
        List<String> danhSachChoDaDat = veDAO.getDanhSachChoDaDat(maCT);
        int slChoDaDat = danhSachChoDaDat != null ? danhSachChoDaDat.size() : 0;

        // Tổng số chỗ
        int tongSoCho = 0;
        List<ToaTau> danhSachToa = toaTauDAO.getAllToaTauByMaTau(ct.getMaTau());
        if (danhSachToa != null) {
            for (ToaTau toa : danhSachToa) {
                tongSoCho += toa.getSoLuongCho();
            }
        }

        int slChoTrong = Math.max(0, tongSoCho - slChoDaDat);

        info.put("slChoDaDat", slChoDaDat);
        info.put("slChoTrong", slChoTrong);
        return info;
    }
}