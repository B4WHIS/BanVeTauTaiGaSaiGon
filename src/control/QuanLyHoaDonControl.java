package control;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import dao.ChiTietHoaDonDAO;
import dao.HanhKhachDAO;
import dao.HoaDonDAO;
import dao.LichSuVeDAO;
import dao.VeDAO;
import entity.ChiTietHoaDon;
import entity.HanhKhach;
import entity.HoaDon;
import entity.NhanVien;
import entity.Ve;

public class QuanLyHoaDonControl {

    private final VeDAO veDAO = new VeDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final ChiTietHoaDonDAO chiTietDAO = new ChiTietHoaDonDAO();
    private final HanhKhachDAO hanhKhachDAO = new HanhKhachDAO();

    private static final BigDecimal VAT = new BigDecimal("0.1");

    public HoaDon lapHoaDon(HanhKhach hk, NhanVien nv, List<Ve> dsVe) throws Exception {
        if (dsVe == null || dsVe.isEmpty())
            throw new IllegalArgumentException("Danh sách vé rỗng!");
        if (hk == null || nv == null)
            throw new IllegalArgumentException("Thiếu thông tin!");

        Connection conn = connectDB.getConnection();
        conn.setAutoCommit(false);

        try {
            // 2. Tính tổng + VAT
            BigDecimal tongTruocVAT = dsVe.stream()
                    .map(Ve::getGiaThanhToan)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal tongSauVAT = tongTruocVAT.multiply(BigDecimal.ONE.add(VAT));

            // 3. Tạo hóa đơn
            HoaDon hd = new HoaDon();
            hd.setMaHanhKhach(hk);
            hd.setMaNhanVien(nv);
            hd.setNgayLap(LocalDateTime.now());
            hd.setTongTien(tongSauVAT); 
            
            String maHoaDon = hoaDonDAO.insert(hd, conn); // 
            hd.setMaHoaDon(maHoaDon);

            // 5. Thêm chi tiết
            for (Ve ve : dsVe) {
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setHoaDon(hd);
                cthd.setVe(ve);
                cthd.setDonGia(ve.getGiaThanhToan());
                chiTietDAO.insert(cthd, conn); //
            }
            for (Ve ve : dsVe) {
                LichSuVeDAO lsvDao = new LichSuVeDAO(); 
                lsvDao.updateMaHoaDonByMaVe(ve.getMaVe(), maHoaDon, conn);
            }

            conn.commit();
            return hd;

        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Tìm kiếm hóa đơn tổng hợp theo thông tin nhập
     * @param maHD mã hóa đơn (có thể để trống)
     * @param tenHanhKhach tên hành khách (có thể để trống)
     * @param tenNhanVien tên nhân viên lập (có thể để trống)
     * @param ngayLap ngày lập (có thể null)
     * @param tongTien tổng tiền (có thể null)
     * @return danh sách hóa đơn khớp điều kiện
     */
    public List<HoaDon> timKiemHoaDon(String maHD, String tenHanhKhach, String tenNhanVien,
                                      LocalDateTime ngayLap, BigDecimal tongTien) {
        try {
            List<HoaDon> ds = hoaDonDAO.getAll();
            List<HoaDon> ketQua = new ArrayList<>();

            for (HoaDon hd : ds) {
                boolean match = true;

                if (maHD != null && !maHD.isEmpty() && !hd.getMaHoaDon().toLowerCase().contains(maHD.toLowerCase())) {
                    match = false;
                }
                if (tenHanhKhach != null && !tenHanhKhach.isEmpty() &&
                        !hd.getMaHanhKhach().getHoTen().toLowerCase().contains(tenHanhKhach.toLowerCase())) {
                    match = false;
                }
                if (tenNhanVien != null && !tenNhanVien.isEmpty() &&
                        !hd.getMaNhanVien().getHoTen().toLowerCase().contains(tenNhanVien.toLowerCase())) {
                    match = false;
                }
                if (ngayLap != null && !hd.getNgayLap().toLocalDate().equals(ngayLap.toLocalDate())) {
                    match = false;
                }
                if (tongTien != null && hd.getTongTien().compareTo(tongTien) != 0) {
                    match = false;
                }

                if (match) {
                    ketQua.add(hd);
                }
            }
            return ketQua;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
