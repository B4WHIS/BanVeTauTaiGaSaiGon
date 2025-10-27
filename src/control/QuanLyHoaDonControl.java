package control;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import connectDB.connectDB;
import dao.ChiTietHoaDonDAO;
import dao.HanhKhachDAO;
import dao.HoaDonDAO;
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
            // 1. Thêm vé
            for (Ve ve : dsVe) {
                String maVe = veDAO.themVe(ve);
                ve.setMaVe(maVe);
            }

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
            hd.setTongTien(tongSauVAT); // DÙNG setTongTien

            // 4. Thêm vào DB → SQL sinh maHoaDon
            String maHoaDon = hoaDonDAO.insert(hd);

            // 5. Thêm chi tiết
            for (Ve ve : dsVe) {
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setHoaDon(hd);
                cthd.setVe(ve);
                cthd.setDonGia(ve.getGiaThanhToan());
                chiTietDAO.insert(cthd);
            }

            conn.commit();
            return hd;

        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }
}