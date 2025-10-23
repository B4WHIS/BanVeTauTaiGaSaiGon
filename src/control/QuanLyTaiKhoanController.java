package control;


import gui.QuanLyTaiKhoan;
import dao.TaiKhoanDAO;
import dao.NhanVienDAO;
import entity.TaiKhoan;
import entity.NhanVien;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuanLyTaiKhoanController implements ActionListener {
    private QuanLyTaiKhoan view;
    private TaiKhoanDAO dao;
    private NhanVienDAO nhanVienDAO;
    private boolean isAddMode = false;  // Mode: true = add, false = edit
    private TaiKhoan selectedTaiKhoan;  // Lưu tài khoản được select để edit

    public QuanLyTaiKhoanController(QuanLyTaiKhoan view) {
        this.view = view;
        this.dao = new TaiKhoanDAO();
        this.nhanVienDAO = new NhanVienDAO();
        System.out.println("Controller khởi tạo thành công.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Nút nhấn: " + e.getActionCommand());
        if (e.getSource() == view.getBtnThem()) {
            handleThem();
        } else if (e.getSource() == view.getBtnSua()) {
            handleSua();
        } else if (e.getSource() == view.getBtnXoa()) {
            handleXoa();
        } else if (e.getSource() == view.getBtnReset()) {
            handleReset();
        } else if (e.getSource() == view.getBtnExport()) {
            handleExport();
        } else if (e.getSource() == view.getBtnTroVe()) {
            handleTroVe();
        } else if (e.getSource() == view.getBtnLuu()) {
            handleLuu();
        }
    }

    // Xử lý chọn row trong table: Load form view-only, enable btnSua/Xoa, disable fields
    public void handleTableSelection() {
        String tenDangNhap = view.getSelectedTenDangNhap();
        System.out.println("Chọn row: " + tenDangNhap);
        if (tenDangNhap != null) {
            selectedTaiKhoan = dao.getTaiKhoanByTenDangNhap(tenDangNhap);
            if (selectedTaiKhoan != null) {
                view.loadFormData(selectedTaiKhoan);
                isAddMode = false;
                view.getBtnSua().setEnabled(true);
                view.getBtnXoa().setEnabled(true);
                view.getBtnLuu().setEnabled(false);
                view.enableFormFields(false);  // Disable fields (view-only)
                System.out.println("Load form view-only cho: " + tenDangNhap + " - NV: " + selectedTaiKhoan.getNhanVien().toString());
            } else {
                System.out.println("Không tìm thấy tài khoản trong DB: " + tenDangNhap);
            }
        } else {
            view.resetForm();
            view.getBtnSua().setEnabled(false);
            view.getBtnXoa().setEnabled(false);
            view.getBtnLuu().setEnabled(false);
            System.out.println("Bỏ chọn row.");
        }
    }

    // Xử lý Thêm: Reset form, enable fields và btnLuu
    private void handleThem() {
        System.out.println("Bắt đầu chế độ THÊM.");
        view.resetForm();
        isAddMode = true;
        selectedTaiKhoan = null;
        view.enableFormFields(true);  // Enable fields cho add
        view.getBtnLuu().setEnabled(true);
        view.getBtnSua().setEnabled(false);
        view.getBtnXoa().setEnabled(false);
        view.showMessage("Chế độ thêm mới. Chọn nhân viên, nhập mật khẩu và nhấn Lưu.", "Thêm tài khoản", JOptionPane.INFORMATION_MESSAGE);
    }

    // Xử lý Sửa: Load data nếu cần, enable fields và btnLuu
    private void handleSua() {
        String tenDangNhap = view.getSelectedTenDangNhap();
        System.out.println("Bắt đầu SỬA: " + tenDangNhap);
        if (tenDangNhap == null) {
            view.showMessage("Vui lòng chọn một tài khoản để sửa.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedTaiKhoan == null) {
            selectedTaiKhoan = dao.getTaiKhoanByTenDangNhap(tenDangNhap);
        }
        if (selectedTaiKhoan == null) {
            view.showMessage("Không tìm thấy dữ liệu tài khoản.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        isAddMode = false;
        view.loadFormData(selectedTaiKhoan);
        view.enableFormFields(true);  // Enable fields cho edit
        view.getBtnLuu().setEnabled(true);
        view.showMessage("Chế độ sửa. Sửa mật khẩu và nhấn Lưu.", "Sửa tài khoản", JOptionPane.INFORMATION_MESSAGE);
    }

    // Xử lý Xóa
    private void handleXoa() {
        String tenDangNhap = view.getSelectedTenDangNhap();
        System.out.println("Bắt đầu XÓA: " + tenDangNhap);
        if (tenDangNhap == null) {
            view.showMessage("Vui lòng chọn một tài khoản để xóa.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa tài khoản này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dao.deleteTaiKhoan(tenDangNhap)) {
                    view.refreshData();
                    view.showMessage("Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Xóa OK: " + tenDangNhap);
                } else {
                    view.showMessage("Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Xóa FAIL: Không ảnh hưởng row.");
                }
            } catch (IllegalArgumentException ex) {
                view.showMessage(ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                System.out.println("Xóa lỗi: " + ex.getMessage());
            }
        }
    }

    // Xử lý Làm mới: Refresh data, disable buttons và fields
    private void handleReset() {
        System.out.println("Làm mới dữ liệu.");
        view.refreshData();
        view.getBtnSua().setEnabled(false);
        view.getBtnXoa().setEnabled(false);
        view.getBtnLuu().setEnabled(false);
        view.enableFormFields(false);  // Disable fields
    }

    // Xử lý Xuất Excel (stub)
    private void handleExport() {
        System.out.println("Xuất Excel (chưa implement).");
        view.showMessage("Chức năng xuất Excel đang được phát triển.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    // Xử lý Trở về
    private void handleTroVe() {
        System.out.println("Thoát form.");
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn thoát?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose();
        }
    }

    // Xử lý Lưu (add hoặc update)
    private void handleLuu() {
        System.out.println("Bắt đầu LƯU - Mode: " + (isAddMode ? "THÊM" : "SỬA"));
        try {
            // Validate form
            if (!validateForm()) {
                System.out.println("Validate FAIL.");
                return;
            }
            System.out.println("Validate OK.");

            TaiKhoan tk = createTaiKhoanFromForm();
            System.out.println("Tạo entity OK: " + tk.getTenDangNhap() + " - NV: " + tk.getNhanVien().toString());

            boolean success;
            if (isAddMode) {
                System.out.println("Gọi DAO addTaiKhoan.");
                success = dao.addTaiKhoan(tk);
                if (success) {
                    System.out.println("Thêm OK - Tên đăng nhập: " + tk.getTenDangNhap());
                    view.showMessage("Thêm thành công! Tên đăng nhập: " + tk.getTenDangNhap(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.out.println("Thêm FAIL - DAO trả false.");
                }
            } else {
                System.out.println("Gọi DAO updateTaiKhoan với tên đăng nhập: " + tk.getTenDangNhap());
                success = dao.updateTaiKhoan(tk);
                if (success) {
                    System.out.println("Cập nhật OK - Mật khẩu mới đã lưu.");
                    view.showMessage("Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.out.println("Cập nhật FAIL - DAO trả false.");
                }
            }

            if (success) {
                view.refreshData();
                view.resetForm();
                view.getBtnSua().setEnabled(false);
                view.getBtnXoa().setEnabled(false);
                view.getBtnLuu().setEnabled(false);
                view.enableFormFields(false);  // Disable sau lưu
                System.out.println("Refresh và reset form OK.");
            } else {
                view.showMessage("Lưu thất bại! Kiểm tra console.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                System.out.println("Lưu FAIL tổng quát.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Lỗi validate/DAO/Entity: " + ex.getMessage());  // Log từ entity validation
            view.showMessage(ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            System.out.println("Lỗi hệ thống: " + ex.getMessage());
            ex.printStackTrace();
            view.showMessage("Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Validate form data (entity sẽ validate chi tiết khi set)
    private boolean validateForm() {
        System.out.println("Bắt đầu validate:");
        if (view.getCbNhanVien().getSelectedIndex() < 0) {
            System.out.println("Validate FAIL: Không chọn nhân viên.");
            view.showMessage("Vui lòng chọn nhân viên.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String matKhauText = new String(view.getTxtMatKhau().getPassword());
        if (matKhauText.isEmpty()) {
            System.out.println("Validate FAIL: Mật khẩu rỗng.");
            view.showMessage("Vui lòng nhập mật khẩu.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        System.out.println("Validate form cơ bản OK. Entity sẽ validate chi tiết.");
        return true;
    }

    // Tạo TaiKhoan từ form data (entity sẽ throw nếu NV/matKhau không hợp lệ)
    private TaiKhoan createTaiKhoanFromForm() {
        System.out.println("Tạo entity từ form:");
        TaiKhoan tk = new TaiKhoan();

        // Lấy NV từ combo (entity sẽ validate khi setNhanVien)
        String tenNV = (String) view.getCbNhanVien().getSelectedItem();
        NhanVien nv = nhanVienDAO.getNhanVienByTen(tenNV);
        if (nv == null) {
            throw new IllegalArgumentException("Không tìm thấy nhân viên: " + tenNV);
        }
        tk.setNhanVien(nv);  // Trigger validation entity (tuổi, SDT, v.v.)

        // Mật khẩu (entity sẽ validate regex)
        String matKhauText = new String(view.getTxtMatKhau().getPassword());
        tk.setMatKhau(matKhauText);

        System.out.println("Tạo entity hoàn tất (validated).");
        return tk;
    }
}