package control;  

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dao.GaDAO;
import dao.LichTrinhDAO;
import dao.TauDAO;
import dao.ToaTauDAO;
import entity.Ga;
import entity.LichTrinh;
import entity.Tau;
import entity.ToaTau;
import exception.BusinessException;

public class QuanLyTaiNguyenControl {
    private TauDAO tauDAO;
    private ToaTauDAO toaTauDAO;
    private GaDAO gaDAO;
    private LichTrinhDAO lichTrinhDAO;

  
    public QuanLyTaiNguyenControl(TauDAO tauDAO, ToaTauDAO toaTauDAO, GaDAO gaDAO, LichTrinhDAO lichTrinhDAO) {
        this.tauDAO = tauDAO;
        this.toaTauDAO = toaTauDAO;
        this.gaDAO = gaDAO;
        this.lichTrinhDAO = lichTrinhDAO;
    }

    // ========== QUẢN LÝ TÀU ==========
    public List<Tau> layTatCaTau() {
        return tauDAO.getAllTau();
    }

    public Tau layTauTheoMa(String maTau) {
        Tau tau = tauDAO.getTauByMaTau(maTau);
        if (tau == null) {
            throw new BusinessException("Không tìm thấy tàu với mã: " + maTau);
        }
        return tau;
    }

    // Cập nhật: Thêm tham số configLoaiToa (Map<IDloaiGhe, soLuongToaChoLoaiDo>) để linh hoạt cấu hình loại toa
    // Ví dụ: {1: 4, 3: 8} nghĩa là 4 toa ghế cứng, 8 toa giường nằm (tổng = soLuongToa của tàu)
    public boolean themTauMoi(Tau tau, Map<Integer, Integer> configLoaiToa) {
        try {
            // Validation: Tổng soLuongToa từ config phải khớp với tau.getSoToa()
            int tongSoLuongToa = configLoaiToa.values().stream().mapToInt(Integer::intValue).sum();
            if (tongSoLuongToa != tau.getSoToa()) {
                throw new BusinessException("Tổng số toa từ config phải bằng số lượng toa của tàu (" + tau.getSoToa() + ")");
            }
            if (!tauDAO.addTau(tau)) {
                throw new BusinessException("Thêm tàu thất bại!");
            }
            // Business logic: Tự động tạo toa tàu theo config
            taoToaTauTuTauMoi(tau, configLoaiToa);
            return true;
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Dữ liệu tàu không hợp lệ: " + e.getMessage());
        }
    }

    public boolean capNhatTau(Tau tau) {
        try {
            return tauDAO.updateTau(tau);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Cập nhật tàu thất bại: " + e.getMessage());
        }
    }

    public boolean xoaTau(String maTau) {
        Tau tau = layTauTheoMa(maTau);
        // Kiểm tra ràng buộc: Không xóa nếu có chuyến tàu đang dùng
        if (!lichTrinhDAO.getAllLichTrinh().stream().anyMatch(lt -> lt.getMaLichTrinh().contains(maTau))) {  // Giả sử kiểm tra
            return tauDAO.deleteTau(maTau);
        } else {
            throw new BusinessException("Không thể xóa tàu đang được sử dụng!");
        }
    }

    // ========== QUẢN LÝ TÒA TÀU ==========
    public List<ToaTau> layTatCaToaTau() {
        return toaTauDAO.getAllToaTau();
    }

    public ToaTau layToaTauTheoMa(String maToa) {
        ToaTau toa = toaTauDAO.getToaTauByMaToa(maToa);
        if (toa == null) {
            throw new BusinessException("Không tìm thấy toa tàu với mã: " + maToa);
        }
        return toa;
    }

    public boolean themToaTauMoi(ToaTau toaTau) {
        try {
            return toaTauDAO.addToaTau(toaTau);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Dữ liệu toa tàu không hợp lệ: " + e.getMessage());
        }
    }

    // Business logic: Tự động tạo toa khi thêm tàu mới theo config (Map<IDloaiGhe, soLuongToa>)
    // Số lượng chỗ: Ghế cứng/mềm (1,2): 40, Giường nằm (3): 28
    // HeSoGia: Ghế cứng (1): 1.0, Ghế nệm (2): 1.5, Giường nằm (3): 2.0
    private void taoToaTauTuTauMoi(Tau tau, Map<Integer, Integer> configLoaiToa) {
        int soThuTuHienTai = 1;  // Số thứ tự toa tăng dần
        for (Map.Entry<Integer, Integer> entry : configLoaiToa.entrySet()) {
            int idLoaiGhe = entry.getKey();
            int soLuongToaLoai = entry.getValue();
            int soLuongCho = (idLoaiGhe == 3) ? 28 : 40;  
            BigDecimal heSoGia;
            switch (idLoaiGhe) {
                case 1:  // Ghế cứng
                    heSoGia = new BigDecimal("1.00");
                    break;
                case 2:  // Ghế nệm (mềm)
                    heSoGia = new BigDecimal("1.50");
                    break;
                case 3:  // Giường nằm
                    heSoGia = new BigDecimal("2.00");
                    break;
                default:
                    heSoGia = new BigDecimal("1.00");  
                    break;
            }
            for (int j = 0; j < soLuongToaLoai; j++) {
                ToaTau toa = new ToaTau();
                toa.setSoThuTu(soThuTuHienTai++);
                toa.setSoLuongCho(soLuongCho);
                toa.setHeSoGia(heSoGia);
                toa.setTau(tau);
                toaTauDAO.addToaTau(toa);
            }
        }
    }

    // ========== QUẢN LÝ GA ==========
    public List<Ga> layTatCaGa() {
        return gaDAO.getAllGa();
    }

    public Ga layGaTheoMa(String maGa) {
        Ga ga = gaDAO.getGaByMaGa(maGa);
        if (ga == null) {
            throw new BusinessException("Không tìm thấy ga với mã: " + maGa);
        }
        return ga;
    }

    public List<Ga> timGaTheoTen(String tenGa) {
        return gaDAO.getGaByTenGa(tenGa);
    }

    public boolean themGaMoi(Ga ga) {
        try {
            return gaDAO.addGa(ga);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Dữ liệu ga không hợp lệ: " + e.getMessage());
        }
    }

    // ========== QUẢN LÝ LỊCH TRÌNH ==========
    public List<LichTrinh> layTatCaLichTrinh() {
        return lichTrinhDAO.getAllLichTrinh();
    }

    public LichTrinh layLichTrinhTheoMa(String maLichTrinh) {
        LichTrinh lt = lichTrinhDAO.getLichTrinhByMaLichTrinh(maLichTrinh);
        if (lt == null) {
            throw new BusinessException("Không tìm thấy lịch trình với mã: " + maLichTrinh);
        }
        return lt;
    }

    // Phương thức mới: Lấy lịch trình theo tên (tìm kiếm chứa tên)
    public List<LichTrinh> layLichTrinhTheoTen(String tenLichTrinh) {
        return lichTrinhDAO.getAllLichTrinh().stream()
                .filter(lt -> lt.getTenLichTrinh().toLowerCase().contains(tenLichTrinh.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Phương thức phức tạp: Lấy lịch trình theo ga đi/đến và khoảng cách (ví dụ: > 100km)
    public List<LichTrinh> layLichTrinhTheoGaVaKhoangCach(String maGaDi, String maGaDen, double khoangCachMin) {
        return lichTrinhDAO.getAllLichTrinh().stream()
                .filter(lt -> lt.getMaGaDi().getMaGa().equals(maGaDi)
                        && lt.getMaGaDen().getMaGa().equals(maGaDen)
                        && lt.getKhoangCach() >= khoangCachMin)
                .collect(Collectors.toList());
    }

    public boolean themLichTrinhMoi(LichTrinh lt) {
        try {
            // Mặc định ga đi là Ga Sài Gòn (GA-01) nếu chưa có
            if (lt.getMaGaDi() == null || lt.getMaGaDi().getMaGa() == null || lt.getMaGaDi().getMaGa().trim().isEmpty()) {
                Ga gaSaiGon = new Ga();
                gaSaiGon.setMaGa("GA-01");
                gaSaiGon.setTenGa("Ga Sài Gòn");
                gaSaiGon.setDiaChi("Quận 3, TP. Hồ Chí Minh");
                lt.setMaGaDi(gaSaiGon);
            }
            // Validation business: GaDi và GaDen không trùng
            if (lt.getMaGaDi().getMaGa().equals(lt.getMaGaDen().getMaGa())) {
                throw new BusinessException("Ga đi và ga đến không được trùng nhau!");
            }
            return lichTrinhDAO.addLichTrinh(lt);
        } catch (Exception e) {
            throw new BusinessException("Thêm lịch trình thất bại: " + e.getMessage());
        }
    }


}