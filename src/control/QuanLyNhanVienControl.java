
package control;

import gui.QuanLyNhanVien;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.List;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import connectDB.connectDB;
import dao.NhanVienDAO;
import entity.NhanVien;

public class QuanLyNhanVienControl implements ActionListener {
    private QuanLyNhanVien view;
    private DefaultTableModel model;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private NhanVienDAO nhanVienDAO;

    public QuanLyNhanVienControl(QuanLyNhanVien view) {
        this.view = view;
        this.model = view.getModelNV();

        // Gắn sự kiện
        view.getBtnThem().addActionListener(this);
        view.getBtnSua().addActionListener(this);
        view.getBtnXoa().addActionListener(this);
        view.getBtnReset().addActionListener(this);
        view.getBtnExport().addActionListener(this);
        view.getBtnTroVe().addActionListener(this);

        // Click bảng → điền form
        view.getTblNhanVien().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTblNhanVien().getSelectedRow();
                if (row >= 0) {
                    view.getTxtHoTen().setText(model.getValueAt(row, 1).toString());
                    view.getDateChooserNgaySinh().setToolTipText(model.getValueAt(row, 2).toString());
                    view.getTxtCMND().setText(model.getValueAt(row, 3).toString());
                    view.getTxtDienThoai().setText(model.getValueAt(row, 4).toString()); // SỐ ĐT
                    view.getCbChucVu().setSelectedItem(model.getValueAt(row, 5)); // CHỨC VỤ
                }
            }
        });

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        String sql = """
            SELECT 
                nv.maNhanVien,
                nv.hoTen,
                CONVERT(varchar, nv.ngaySinh, 103) AS ngaySinh,
                nv.cmndCccd,
                nv.soDienThoai,  -- SỬA TỪ sDienThoai → soDienThoai
                lcv.tenLoai
            FROM NhanVien nv
            JOIN LoaiChucVu lcv ON nv.IDloaiChucVu = lcv.IDloaiCV
            ORDER BY nv.IDnv
            """;

        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("maNhanVien"),
                    rs.getString("hoTen"),
                    rs.getString("ngaySinh"),
                    rs.getString("cmndCccd"),
                    rs.getString("soDienThoai") == null ? "" : rs.getString("soDienThoai"), // SỬA Ở ĐÂY
                    rs.getString("tenLoai")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == view.getBtnThem()) them();
        else if (src == view.getBtnSua()) sua();
        else if (src == view.getBtnXoa()) xoa();
        else if (src == view.getBtnReset()) reset();
        else if (src == view.getBtnTroVe()) view.dispose();
    }

    private void them() {
        if (!validate()) return;

        String sql = "INSERT INTO NhanVien (hoTen, ngaySinh, cmndCccd, soDienThoai, IDloaiChucVu) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, view.getTxtHoTen().getText().trim());
            ps.setDate(2, Date.valueOf(LocalDate.parse(view.getDateChooserNgaySinh().getToolTipText().trim(), dtf)));
            ps.setString(3, view.getTxtCMND().getText().trim());
            String sdt = view.getTxtDienThoai().getText().trim();
            ps.setString(4, sdt.isEmpty() ? null : sdt); // Cho phép NULL
            ps.setInt(5, getIDChucVu((String) view.getCbChucVu().getSelectedItem()));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idnv = rs.getInt(1);
                        String maNV = "NV-" + String.format("%03d", idnv);
                        JOptionPane.showMessageDialog(view, 
                            "Thêm thành công!\nMã nhân viên: " + maNV);
                    }
                }
                loadData();
                reset();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi thêm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sua() {
        int row = view.getTblNhanVien().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn nhân viên!");
            return;
        }
        if (!validate()) return;

        String maNV = (String) model.getValueAt(row, 0);

        String sql = "UPDATE NhanVien SET hoTen = ?, ngaySinh = ?, cmndCccd = ?, soDienThoai = ?, IDloaiChucVu = ? WHERE maNhanVien = ?";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, view.getTxtHoTen().getText().trim());
            ps.setDate(2, Date.valueOf(LocalDate.parse(view.getDateChooserNgaySinh().getToolTipText().trim(), dtf)));
            ps.setString(3, view.getTxtCMND().getText().trim());
            String sdt = view.getTxtDienThoai().getText().trim();
            ps.setString(4, sdt.isEmpty() ? null : sdt);
            ps.setInt(5, getIDChucVu((String) view.getCbChucVu().getSelectedItem()));
            ps.setString(6, maNV);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(view, "Sửa thành công!");
                loadData();
                reset();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi sửa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoa() {
        int row = view.getTblNhanVien().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn nhân viên!");
            return;
        }

        String maNV = (String) model.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(view, 
                "Xóa nhân viên: " + maNV + "?", "Xác nhận", 
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM NhanVien WHERE maNhanVien = ?";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(view, "Xóa thành công!");
            loadData();
            reset();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Lỗi xóa: " + ex.getMessage());
        }
    }

    private void reset() {
        view.getTxtHoTen().setText("");
        view.getDateChooserNgaySinh().setToolTipText("dd/MM/yyyy");
        view.getTxtCMND().setText("");
        view.getTxtDienThoai().setText(""); // Reset số điện thoại
        view.getCbChucVu().setSelectedIndex(0);
        view.getTblNhanVien().clearSelection();
    }

    private boolean validate() {
        String ten = view.getTxtHoTen().getText().trim();
        String ns = view.getDateChooserNgaySinh().getToolTipText().trim();
        String cmnd = view.getTxtCMND().getText().trim();
        String sdt = view.getTxtDienThoai().getText().trim();

        if (ten.isEmpty() || cmnd.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập Họ tên và CMND/CCCD!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!ns.matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(view, "Ngày sinh phải đúng định dạng dd/MM/yyyy!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            LocalDate.parse(ns, dtf);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(view, "Ngày sinh không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!cmnd.matches("\\d{9}|\\d{12}")) {
            JOptionPane.showMessageDialog(view, "CMND/CCCD phải có 9 hoặc 12 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!sdt.isEmpty() && !sdt.matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(view, "Số điện thoại phải có 10-11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private int getIDChucVu(String ten) {
        String sql = "SELECT IDloaiCV FROM LoaiChucVu WHERE tenLoai = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ten);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // mặc định
    }
    public List<NhanVien> searchNhanVien(String hoTen, String soDienThoai, String cmndCccd, Integer idLoaiChucVu) {
        return nhanVienDAO.searchNhanVien(hoTen, soDienThoai, cmndCccd, idLoaiChucVu);
    }
}
