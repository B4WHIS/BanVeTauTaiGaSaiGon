package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import connectDB.connectDB;
import dao.HanhKhachDAO;
import dao.VeDAO;
import entity.HanhKhach;
import entity.NhanVien;
import entity.Ve;
import gui.GiaoDienHuyVe;
import gui.GiaoDienTraCuuChuyentau;
import gui.GiaoDienTraCuuVeTau;

public class TraCuuVeTauController implements ActionListener {
 private final GiaoDienTraCuuVeTau view;
 private final QuanLyVeControl quanLyVeControl;
 private final DefaultTableModel tableModel;
 private NhanVien nhanVien;
 private DoiVeControl doiVeControl = new DoiVeControl();
 private VeDAO veDAO = new VeDAO();
 private HanhKhachDAO hanhKhachDAO = new HanhKhachDAO();

// public TraCuuVeTauController(GiaoDienTraCuuVeTau view, NhanVien nv) {
//     this.view = view;
//     this.nhanVien = nv;
//     this.quanLyVeControl = new QuanLyVeControl();
//     this.tableModel = view.getTableModel();
// }
 public TraCuuVeTauController(GiaoDienTraCuuVeTau view, NhanVien nv) {
	    this.view = view;
	    this.nhanVien = nv;
	    if (this.nhanVien == null) {  // Thêm check an toàn
	        System.err.println("Cảnh báo: NhanVien null trong TraCuuVeTauController - Kiểm tra login");
	    }
	    this.quanLyVeControl = new QuanLyVeControl();
	    this.tableModel = view.getTableModel();
	}
 @Override
 public void actionPerformed(ActionEvent e) {
     String cmd = e.getActionCommand();
     switch (cmd) {
         case "Tìm" -> timKiemVe();
         case "Làm mới" -> lamMoiForm();
         case "Trở về" -> troVe();
         case "In vé" -> inVe();
         case "Hủy vé" -> huyVe();
         case "Đổi vé" -> doiVe();
     }
 }

 private void timKiemVe() {
     String hoTen = view.getTxtHoTen().getText().trim();
     String cmnd = view.getTxtCMND().getText().trim();
     String sdt = view.getTxtSDT().getText().trim();

     if (hoTen.isEmpty() && cmnd.isEmpty() && sdt.isEmpty()) {
         JOptionPane.showMessageDialog(view, "Vui lòng nhập ít nhất một thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
         return;
     }

     tableModel.setRowCount(0);
     Connection conn = null;

     try {
         conn = connectDB.getConnection();
         String sql = """
             SELECT DISTINCT
                 hk.hoTen, v.maVe, v.giaThanhToan, v.trangThai,
                 ct.thoiGianKhoiHanh, ct.maChuyenTau,
                 gd.tenGa AS gaDi, gden.tenGa AS gaDen,
                 cg.maChoNgoi, lg.tenLoai AS loaiGhe, tt.soThuTu AS toaSo
             FROM Ve v
             JOIN HanhKhach hk ON v.maHanhKhach = hk.maHanhKhach
             JOIN ChuyenTau ct ON v.maChuyenTau = ct.maChuyenTau
             JOIN LichTrinh lt ON ct.maLichTrinh = lt.maLichTrinh
             JOIN Ga gd ON lt.gaDi = gd.maGa
             JOIN Ga gden ON lt.gaDen = gden.maGa
             JOIN ChoNgoi cg ON v.maChoNgoi = cg.maChoNgoi
             JOIN LoaiGhe lg ON cg.IDloaiGhe = lg.IDloaiGhe
             JOIN ToaTau tt ON cg.maToa = tt.maToa
             WHERE 1=1
             """ +
             (!hoTen.isEmpty() ? " AND hk.hoTen LIKE ? " : "") +
             (!cmnd.isEmpty() ? " AND hk.cmndCccd = ? " : "") +
             (!sdt.isEmpty() ? " AND hk.soDienThoai = ? " : "") +
             " ORDER BY ct.thoiGianKhoiHanh DESC, v.maVe";

         PreparedStatement pstmt = conn.prepareStatement(sql);
         int idx = 1;
         if (!hoTen.isEmpty()) pstmt.setString(idx++, "%" + hoTen + "%");
         if (!cmnd.isEmpty()) pstmt.setString(idx++, cmnd);
         if (!sdt.isEmpty()) pstmt.setString(idx++, sdt);

         ResultSet rs = pstmt.executeQuery();
         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
         String currentGroup = "";
         int rowIndex = 0;

         while (rs.next()) {
             String maVe = rs.getString("maVe");
             String gaDi = rs.getString("gaDi");
             String gaDen = rs.getString("gaDen");
             String thoiGian = sdf.format(rs.getTimestamp("thoiGianKhoiHanh"));
             String maChuyenTau = rs.getString("maChuyenTau");

             String groupKey = gaDi + " - " + gaDen + " " + thoiGian + "|" + maChuyenTau;
             if (!groupKey.equals(currentGroup)) {
                 currentGroup = groupKey;
                 tableModel.addRow(new Object[]{
                     "", 
                     "<html><b>" + gaDi + " - " + gaDen + "<br>" + thoiGian + "</b></html>",
                     "", "", "", "", 
                     null  // DÒNG NÀY: không có checkbox
                 });
             }

             rowIndex++;
             String hoTenStr = rs.getString("hoTen") + "\nSố ghế: " + rs.getString("maChoNgoi");
             String thongTinVe = String.format(
                 "%s\nToa: %d - Ghế: %s\n%s",
                 maVe, rs.getInt("toaSo"), rs.getString("maChoNgoi"), rs.getString("loaiGhe")
             );
             String thanhTien = String.format("%,.0f", rs.getDouble("giaThanhToan"));
             String trangThai = rs.getString("trangThai");

             boolean choPhepHuy = false;
             if ("Đã đặt".equalsIgnoreCase(trangThai)) {
                 LocalDateTime khoiHanh = rs.getTimestamp("thoiGianKhoiHanh")
                     .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                 choPhepHuy = Duration.between(LocalDateTime.now(), khoiHanh).toHours() >= 4;
             }

             tableModel.addRow(new Object[]{
             	    rowIndex,
             	    hoTenStr,
             	    thongTinVe,
             	    thanhTien,
             	    "Bình thường",
             	    trangThai,
             	    choPhepHuy ? Boolean.FALSE : null  // ĐÚNG: FALSE = chưa chọn, NULL = không cho chọn
             	});
         }

         if (rowIndex == 0) {
             tableModel.addRow(new Object[]{"", "Không tìm thấy vé nào!", "", "", "", "", null});
         }

     } catch (Exception ex) {
         ex.printStackTrace();
         JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
     } finally {
         try { if (conn != null) conn.close(); } catch (Exception ignored) {}
     }
 }
// Trong huyVe()
 private void huyVe() {
     List<String> danhSachMaVe = new ArrayList<>();
     List<Object[]> danhSachVe = new ArrayList<>();

     for (int i = 0; i < tableModel.getRowCount(); i++) {
         Boolean checked = (Boolean) tableModel.getValueAt(i, 6);
         if (checked != null && checked) {
             String maVe = ((String) tableModel.getValueAt(i, 2)).split("\n")[0];
             danhSachMaVe.add(maVe);
             danhSachVe.add(new Object[]{
                 tableModel.getValueAt(i, 0),
                 tableModel.getValueAt(i, 1),
                 tableModel.getValueAt(i, 2),
                 tableModel.getValueAt(i, 3),
                 tableModel.getValueAt(i, 4),
                 tableModel.getValueAt(i, 5)
             });
         }
     }

     if (danhSachMaVe.isEmpty()) {
         JOptionPane.showMessageDialog(view, "Vui lòng chọn ít nhất một vé để hủy!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
         return;
     }

     // MỞ GIAO DIỆN HỦY VÉ
     GiaoDienHuyVe huyVeGUI = new GiaoDienHuyVe(danhSachVe, danhSachMaVe, nhanVien);
     huyVeGUI.setVisible(true);
     view.dispose(); // Đóng tra cứu
 }

 private void doiVe() {
	    List<String> danhSachMaVe = new ArrayList<>();
	    for (int i = 0; i < tableModel.getRowCount(); i++) {
	        Boolean checked = (Boolean) tableModel.getValueAt(i, 6);
	        if (checked != null && checked) {
	            String maVe = ((String) tableModel.getValueAt(i, 2)).split("\n")[0];
	            danhSachMaVe.add(maVe);
	        }
	    }

	    if (danhSachMaVe.isEmpty()) {
	        JOptionPane.showMessageDialog(view, "Vui lòng chọn ít nhất một vé để đổi!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    if (danhSachMaVe.size() > 1) {
	        JOptionPane.showMessageDialog(view, "Chỉ hỗ trợ đổi 1 vé một lúc!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    String maVeCu = danhSachMaVe.get(0);
	    try {
	        Ve veCu = veDAO.layVeTheoMa(maVeCu); 
	       
	        if (!doiVeControl.kiemTraCoTheDoiVe(veCu)) {
	            JOptionPane.showMessageDialog(view, "Vé không thể đổi (đã hủy hoặc thời gian <4h)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        HanhKhach hanhKhachCu = hanhKhachDAO.layHanhKhachTheoMa(veCu.getMaHanhkhach().getMaKH());
	        if (hanhKhachCu == null || hanhKhachCu.getHoTen() == null || hanhKhachCu.getHoTen().trim().isEmpty()) {  // Check null và field chính
	            JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin hành khách cho vé cũ! Kiểm tra dữ liệu DB hoặc vé không hợp lệ.", "Lỗi Đổi Vé", JOptionPane.ERROR_MESSAGE);
	            return;  
	        }
	        if (this.nhanVien == null) {  
	            JOptionPane.showMessageDialog(view, "Thông tin nhân viên không hợp lệ. Vui lòng đăng nhập lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        GiaoDienTraCuuChuyentau traCuuChuyen = new GiaoDienTraCuuChuyentau(view, this.nhanVien, veCu, hanhKhachCu);
	        traCuuChuyen.setVisible(true);
	        view.dispose();
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(view, "Lỗi khi đổi vé: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	    }
	}

 private void lamMoiForm() {
     view.getTxtHoTen().setText("");
     view.getTxtCMND().setText("");
     view.getTxtSDT().setText("");
     tableModel.setRowCount(0);
     view.setupEmptyTable();
 }

 private void troVe() { view.dispose(); }

 private void inVe() {
     JOptionPane.showMessageDialog(view, "Chức năng in vé đang phát triển...", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
 }
}