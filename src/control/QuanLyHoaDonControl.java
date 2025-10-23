package control;

import java.time.LocalDateTime;
import java.util.List;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import entity.ChiTietHoaDon;
import entity.HanhKhach;
import entity.HoaDon;
import entity.NhanVien;
import entity.Ve;

public class QuanLyHoaDonControl {
    private HoaDonDAO hoaDonDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;

    public QuanLyHoaDonControl() {
        hoaDonDAO = new HoaDonDAO();
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
    }

    public boolean lapHoaDon(HanhKhach hanhKhach, NhanVien nhanVien, List<Ve> danhSachVe) {
        if (hanhKhach == null || nhanVien == null || danhSachVe == null || danhSachVe.isEmpty()) {
            System.err.println(" Dữ liệu lập hóa đơn không hợp lệ!");
            return false;
        }

        try {
            //  Tạo hóa đơn
            HoaDon hoaDon = new HoaDon();
            hoaDon.setMaHoaDon(generateMaHoaDon());
            hoaDon.setNgayLap(LocalDateTime.now());
            hoaDon.setMaHanhKhach(hanhKhach);
            hoaDon.setMaNhanVien(nhanVien);

            // 2️⃣ Tạo chi tiết hóa đơn cho từng vé
            for (Ve ve : danhSachVe) {
                ChiTietHoaDon cthd = new ChiTietHoaDon(hoaDon, ve, ve.getGiaThanhToan().doubleValue());
                hoaDon.getDanhSachChiTiet().add(cthd);
            }

            // 3️⃣ Tính tổng tiền
            double tongTien = hoaDon.tinhTongTien();
            hoaDon.setTongTien(tongTien);

            // 4️⃣ Ghi vào DB
            boolean hoaDonInserted = hoaDonDAO.insertHoaDon(hoaDon);
            if (!hoaDonInserted) {
                System.err.println(" Lỗi khi thêm Hóa đơn!");
                return false;
            }

            // 5️⃣ Thêm chi tiết hóa đơn
            for (ChiTietHoaDon cthd : hoaDon.getDanhSachChiTiet()) {
                boolean added = chiTietHoaDonDAO.insertChiTietHoaDon(cthd);
                if (!added) {
                    System.err.println(" Lỗi khi thêm Chi tiết hóa đơn cho vé: " + cthd.getVe().getMaVe());
                    return false;
                }
            }

            System.out.println(" Lập hóa đơn thành công! Tổng tiền: " + tongTien);
            return true;

        } catch (Exception e) {
            System.err.println("Lỗi khi lập hóa đơn: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sinh mã hóa đơn tự động (ví dụ: HD20251022001)
     */
    private String generateMaHoaDon() {
        String prefix = "HD";
        String datePart = LocalDateTime.now().toLocalDate().toString().replace("-", "");
        int random = (int) (Math.random() * 900 + 100); // 3 số ngẫu nhiên
        return prefix + datePart + random;
    }

    /**
     * Lấy danh sách hóa đơn (hiển thị trong giao diện)
     */
    public List<HoaDon> getDanhSachHoaDon() {
        return hoaDonDAO.getAllHoaDon();
    }

    /**
     * Xem chi tiết một hóa đơn
     */
    public List<ChiTietHoaDon> getChiTietHoaDon(String maHoaDon) {
        return chiTietHoaDonDAO.getChiTietByMaHoaDon(maHoaDon);
    }

    /**
     * Xóa một hóa đơn (và toàn bộ chi tiết của nó)
     */
    public boolean xoaHoaDon(String maHoaDon) {
        try {
            List<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.getChiTietByMaHoaDon(maHoaDon);
            for (ChiTietHoaDon cthd : chiTietList) {
                chiTietHoaDonDAO.deleteChiTietHoaDon(maHoaDon, cthd.getVe().getMaVe());
            }

            // Xóa hóa đơn chính
            // (cần có phương thức deleteHoaDon() trong HoaDonDAO nếu bạn muốn thêm)
            System.out.println("Đã xóa chi tiết, giờ có thể xóa Hóa đơn: " + maHoaDon);
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa hóa đơn: " + e.getMessage());
            return false;
        }
    }
}
