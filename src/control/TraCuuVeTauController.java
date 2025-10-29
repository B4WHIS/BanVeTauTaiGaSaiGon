// File: src/control/TraCuuVeTauController.java
package control;

import connectDB.connectDB;
import gui.GiaoDienHuyVe;
import gui.TraCuuVeTauGUI;
import gui.TraCuuChuyenTauGUI;
import entity.LichSuVe;
import entity.NhanVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import dao.VeDAO;
import entity.Ve;

public class TraCuuVeTauController implements ActionListener {
    private final TraCuuVeTauGUI view;
    private final QuanLyVeControl quanLyVeControl;
    private final DefaultTableModel tableModel;

    public TraCuuVeTauController(TraCuuVeTauGUI view) {
        this.view = view;
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
                (!sdt.isEmpty() ? " AND hk.soDienThoai = ? " : "");

            PreparedStatement ps = conn.prepareStatement(sql);
            int paramIndex = 1;
            if (!hoTen.isEmpty()) ps.setString(paramIndex++, "%" + hoTen + "%");
            if (!cmnd.isEmpty()) ps.setString(paramIndex++, cmnd);
            if (!sdt.isEmpty()) ps.setString(paramIndex++, sdt);

            ResultSet rs = ps.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            int rowIndex = 0;
            while (rs.next()) {
                rowIndex++;
                String hoTenStr = rs.getString("hoTen");
                String maVe = rs.getString("maVe");
                String gaDi = rs.getString("gaDi");
                String gaDen = rs.getString("gaDen");
                String thoiGianKH = sdf.format(rs.getTimestamp("thoiGianKhoiHanh"));
                String maChoNgoi = rs.getString("maChoNgoi");
                String loaiGhe = rs.getString("loaiGhe");
                int toaSo = rs.getInt("toaSo");
                String thongTinVe = maVe + "\n" +
                                    gaDi + " → " + gaDen + "\n" +
                                    thoiGianKH + "\n" +
                                    "Toa " + toaSo + " - Ghế " + maChoNgoi + " (" + loaiGhe + ")";
                String thanhTien = String.format("%,.0f VNĐ", rs.getBigDecimal("giaThanhToan").doubleValue());
                String trangThai = rs.getString("trangThai");

                boolean choPhepHuy = false;
                if ("Khả dụng".equalsIgnoreCase(trangThai)) {
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
        GiaoDienHuyVe huyVeGUI = new GiaoDienHuyVe(danhSachVe, danhSachMaVe);
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

        if (danhSachMaVe.size() != 1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn đúng một vé để đổi!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maVe = danhSachMaVe.get(0);
        VeDAO veDAO = new VeDAO();
        Ve veCu = null;
        try {
            veCu = veDAO.timVeTongHop(maVe, null, null, null, null).get(0); // Giả sử method trả list, lấy đầu
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Lỗi tải vé: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mở màn hình tra cứu chuyến tàu với vé cũ
        new TraCuuChuyenTauGUI(view, veCu).setVisible(true);
        view.dispose();
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