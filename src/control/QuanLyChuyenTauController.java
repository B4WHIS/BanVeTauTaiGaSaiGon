package control;

import gui.DanhSachChuyenTau;
import dao.ChuyenTauDAO;
import entity.ChuyenTau;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class QuanLyChuyenTauController implements ActionListener {
    private DanhSachChuyenTau view;
    private ChuyenTauDAO dao;
    private boolean isAddMode = false;  // Mode: true = add, false = edit
    private ChuyenTau selectedChuyenTau;  // Lưu chuyến được select để edit

    // DateTimeFormatter giống view
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public QuanLyChuyenTauController(DanhSachChuyenTau view) {
        this.view = view;
        this.dao = new ChuyenTauDAO();
        System.out.println("Controller khởi tạo thành công.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Nút nhấn: " + e.getActionCommand());  // Log nút nào
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
        String maChuyenTau = view.getSelectedMaChuyenTau();
        System.out.println("Chọn row: " + maChuyenTau);
        if (maChuyenTau != null) {
            selectedChuyenTau = dao.getChuyenTauByMaChuyenTau(maChuyenTau);
            if (selectedChuyenTau != null) {
                loadFormData(selectedChuyenTau);
                isAddMode = false;
                view.getBtnSua().setEnabled(true);
                view.getBtnXoa().setEnabled(true);
                view.getBtnLuu().setEnabled(false);
                view.enableFormFields(false);  // Disable fields (view-only)
                System.out.println("Load form view-only cho: " + maChuyenTau);
            } else {
                System.out.println("Không tìm thấy chuyến tàu trong DB: " + maChuyenTau);
            }
        } else {
            view.resetForm();
            view.getBtnSua().setEnabled(false);
            view.getBtnXoa().setEnabled(false);
            view.getBtnLuu().setEnabled(false);
            System.out.println("Bỏ chọn row.");
        }
    }

    // Chỉ load data vào form, không enable btnLuu hay fields (gọi từ handleTableSelection hoặc handleSua)
    private void loadFormData(ChuyenTau ct) {
        view.getTxtMaChuyenTau().setText(ct.getMaChuyenTau());
        // Set tên tàu và lịch trình vào combo (sử dụng stream để tìm reverse mapping)
        List<String> tenTauList = dao.getAllTenTau();
        String tenTau = tenTauList.stream()
                .filter(t -> dao.getMaTauByTenTau(t) != null && dao.getMaTauByTenTau(t).equals(ct.getMaTau()))
                .findFirst()
                .orElse(ct.getMaTau());  // Fallback
        view.getCbTenTau().setSelectedItem(tenTau);

        List<String> tenLichTrinhList = dao.getAllTenLichTrinh();
        String tenLichTrinh = tenLichTrinhList.stream()
                .filter(lt -> dao.getMaLichTrinhByTenLichTrinh(lt) != null && dao.getMaLichTrinhByTenLichTrinh(lt).equals(ct.getMaLichTrinh()))
                .findFirst()
                .orElse(ct.getMaLichTrinh());  // Fallback
        view.getCbTenLichTrinh().setSelectedItem(tenLichTrinh);

        view.getTxtThoiGianKhoiHanh().setText(ct.getThoiGianKhoiHanh().format(dateTimeFormatter));
        view.getTxtThoiGianDen().setText(ct.getThoiGianDen().format(dateTimeFormatter));
        view.getTxtGiaChuyen().setText(ct.getGiaChuyen().toString());
        view.getCbTrangThai().setSelectedItem(ct.getTrangThai());
    }

    // Xử lý Thêm: Reset form, enable fields và btnLuu
    private void handleThem() {
        System.out.println("Bắt đầu chế độ THÊM.");
        view.resetForm();
        isAddMode = true;
        selectedChuyenTau = null;
        view.enableFormFields(true);  // Enable fields cho add
        view.getBtnLuu().setEnabled(true);
        view.getBtnSua().setEnabled(false);
        view.getBtnXoa().setEnabled(false);
        view.showMessage("Chế độ thêm mới. Nhập thông tin và nhấn Lưu.", "Thêm chuyến tàu", JOptionPane.INFORMATION_MESSAGE);
    }

    // Xử lý Sửa: Load data nếu cần, enable fields và btnLuu
    private void handleSua() {
        String maChuyenTau = view.getSelectedMaChuyenTau();
        System.out.println("Bắt đầu SỬA: " + maChuyenTau);
        if (maChuyenTau == null) {
            view.showMessage("Vui lòng chọn một chuyến tàu để sửa.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedChuyenTau == null) {
            selectedChuyenTau = dao.getChuyenTauByMaChuyenTau(maChuyenTau);
        }
        if (selectedChuyenTau == null) {
            view.showMessage("Không tìm thấy dữ liệu chuyến tàu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        isAddMode = false;
        loadFormData(selectedChuyenTau);
        view.enableFormFields(true);  // Enable fields cho edit
        view.getBtnLuu().setEnabled(true);
        view.showMessage("Chế độ sửa. Sửa thông tin và nhấn Lưu.", "Sửa chuyến tàu", JOptionPane.INFORMATION_MESSAGE);
    }

    // Xử lý Xóa
    private void handleXoa() {
        String maChuyenTau = view.getSelectedMaChuyenTau();
        System.out.println("Bắt đầu XÓA: " + maChuyenTau);
        if (maChuyenTau == null) {
            view.showMessage("Vui lòng chọn một chuyến tàu để xóa.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa chuyến tàu này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dao.deleteChuyenTau(maChuyenTau)) {
                    view.refreshData();
                    view.showMessage("Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Xóa OK: " + maChuyenTau);
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

    // Xử lý Lưu (add hoặc update) - SỬA: Thêm log chi tiết cho edit mode
    private void handleLuu() {
        System.out.println("Bắt đầu LƯU - Mode: " + (isAddMode ? "THÊM" : "SỬA"));
        try {
            // Validate form
            if (!validateForm()) {
                System.out.println("Validate FAIL.");
                return;
            }
            System.out.println("Validate OK.");

            ChuyenTau ct = createChuyenTauFromForm();
            System.out.println("Tạo entity OK: " + ct.getMaTau() + " - " + ct.getMaLichTrinh() + " - Giá: " + ct.getGiaChuyen());

            boolean success;
            if (isAddMode) {
                System.out.println("Gọi DAO addChuyenTau.");
                success = dao.addChuyenTau(ct);
                if (success) {
                    System.out.println("Thêm OK - Mã mới: " + ct.getMaChuyenTau());
                    view.showMessage("Thêm thành công! Mã chuyến: " + ct.getMaChuyenTau(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.out.println("Thêm FAIL - DAO trả false.");
                }
            } else {
                // SỬA: Log giá cũ từ selectedChuyenTau để so sánh
                System.out.println("Giá cũ (từ selected): " + selectedChuyenTau.getGiaChuyen());
                System.out.println("Gọi DAO updateChuyenTau với maChuyenTau: " + ct.getMaChuyenTau());
                success = dao.updateChuyenTau(ct);
                if (success) {
                    System.out.println("Cập nhật OK - Giá mới đã lưu: " + ct.getGiaChuyen());
                    // Reload selected để confirm
                    selectedChuyenTau = dao.getChuyenTauByMaChuyenTau(ct.getMaChuyenTau());
                    System.out.println("Giá sau update từ DB: " + selectedChuyenTau.getGiaChuyen());
                    view.showMessage("Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.out.println("Cập nhật FAIL - DAO trả false. Kiểm tra maChuyenTau match DB.");
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
            System.out.println("Lỗi validate/DAO: " + ex.getMessage());
            view.showMessage(ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            System.out.println("Lỗi hệ thống: " + ex.getMessage());
            ex.printStackTrace();
            view.showMessage("Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Validate form data
    private boolean validateForm() {
        System.out.println("Bắt đầu validate:");
        if (view.getCbTenTau().getSelectedIndex() < 0) {
            System.out.println("Validate FAIL: Không chọn tên tàu.");
            view.showMessage("Vui lòng chọn tên tàu.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (view.getCbTenLichTrinh().getSelectedIndex() < 0) {
            System.out.println("Validate FAIL: Không chọn lịch trình.");
            view.showMessage("Vui lòng chọn lịch trình.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            LocalDateTime.parse(view.getTxtThoiGianKhoiHanh().getText(), dateTimeFormatter);
            LocalDateTime.parse(view.getTxtThoiGianDen().getText(), dateTimeFormatter);
            System.out.println("Validate thời gian OK.");
        } catch (DateTimeParseException ex) {
            System.out.println("Validate FAIL: Thời gian sai format - " + ex.getMessage());
            view.showMessage("Định dạng thời gian không hợp lệ: yyyy-MM-dd HH:mm", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            BigDecimal gia = new BigDecimal(view.getTxtGiaChuyen().getText());
            if (gia.compareTo(BigDecimal.ZERO) <= 0) {
                throw new NumberFormatException("Giá <= 0");
            }
            System.out.println("Validate giá OK: " + gia);
        } catch (NumberFormatException ex) {
            System.out.println("Validate FAIL: Giá sai - " + ex.getMessage());
            view.showMessage("Giá chuyến phải là số lớn hơn 0.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        System.out.println("Validate hoàn tất OK.");
        return true;
    }

    // Tạo ChuyenTau từ form data - SỬA: Thêm log text giá từ form
    private ChuyenTau createChuyenTauFromForm() {
        System.out.println("Tạo entity từ form:");
        ChuyenTau ct = new ChuyenTau();

        // Mã chuyến (cho edit)
        if (!isAddMode) {
            ct.setMaChuyenTau(view.getTxtMaChuyenTau().getText());
            System.out.println("Set mã chuyến (edit): " + ct.getMaChuyenTau());
        }

        // Lấy mã từ tên trong combo
        String tenTau = (String) view.getCbTenTau().getSelectedItem();
        String maTau = dao.getMaTauByTenTau(tenTau);
        if (maTau == null) {
            throw new IllegalArgumentException("Không tìm thấy mã tàu cho tên: " + tenTau);
        }
        ct.setMaTau(maTau);
        System.out.println("Set mã tàu: " + maTau + " (từ tên: " + tenTau + ")");

        String tenLichTrinh = (String) view.getCbTenLichTrinh().getSelectedItem();
        String maLichTrinh = dao.getMaLichTrinhByTenLichTrinh(tenLichTrinh);
        if (maLichTrinh == null) {
            throw new IllegalArgumentException("Không tìm thấy mã lịch trình cho tên: " + tenLichTrinh);
        }
        ct.setMaLichTrinh(maLichTrinh);
        System.out.println("Set mã lịch trình: " + maLichTrinh + " (từ tên: " + tenLichTrinh + ")");

        // Thời gian
        LocalDateTime thoiGianKH = LocalDateTime.parse(view.getTxtThoiGianKhoiHanh().getText(), dateTimeFormatter);
        ct.setThoiGianKhoiHanh(thoiGianKH);
        LocalDateTime thoiGianDen = LocalDateTime.parse(view.getTxtThoiGianDen().getText(), dateTimeFormatter);
        ct.setThoiGianDen(thoiGianDen);
        System.out.println("Set thời gian KH: " + thoiGianKH + " - Đến: " + thoiGianDen);

        // SỬA: Log text giá từ form trước parse
        String giaText = view.getTxtGiaChuyen().getText();
        System.out.println("Text giá từ form: '" + giaText + "'");
        ct.setGiaChuyen(new BigDecimal(giaText));
        System.out.println("Set giá: " + ct.getGiaChuyen());

        // Trạng thái
        ct.setTrangThai((String) view.getCbTrangThai().getSelectedItem());
        System.out.println("Set trạng thái: " + ct.getTrangThai());

        System.out.println("Tạo entity hoàn tất.");
        return ct;
    }
}