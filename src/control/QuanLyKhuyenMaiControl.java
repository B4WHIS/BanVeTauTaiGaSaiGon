package control;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;
import gui.QuanLykhuyenMai;

public class QuanLyKhuyenMaiControl {
    private KhuyenMaiDAO dao;
    private QuanLykhuyenMai view;

    public QuanLyKhuyenMaiControl(QuanLykhuyenMai view) {
        this.dao = new KhuyenMaiDAO();
        this.view = view;
    }

    // === MỞ FORM THÊM ===
    public void them() {
        view.resetForm();
        view.enableFormFields(true);
        view.txtMaKM.setEnabled(true);     // BẬT Ô MÃ
        view.txtMaKM.setText("");          // XÓA TRỐNG
        view.txtMaKM.requestFocus();       // FOCUS VÀO MÃ
        view.showMessage("Nhập MÃ + thông tin → Nhấn THÊM để lưu!", "Hướng dẫn", JOptionPane.INFORMATION_MESSAGE);
    }

    // === MỞ FORM SỬA ===
    public void sua() {
        String maKM = view.getSelectedMaKM();
        if (maKM == null) {
            view.showMessage("Vui lòng chọn khuyến mãi để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        view.enableFormFields(true);
        view.txtMaKM.setEnabled(false);    // KHÓA MÃ KHI SỬA
        view.showMessage("Sửa thông tin → Nhấn SỬA để lưu!", "Hướng dẫn", JOptionPane.INFORMATION_MESSAGE);
    }

    // === XỬ LÝ THÊM ===
    public void xuLyThem() throws Exception {
        KhuyenMai km = taoKhuyenMaiTuForm();
        if (km == null) return;

        if (dao.addKhuyenMai(km)) {
            view.showMessage("Thêm thành công!\nMã: <b>" + km.getMaKhuyenMai() + "</b>", 
                           "Thành công", JOptionPane.INFORMATION_MESSAGE);
            view.refreshData();
        } else {
            view.showMessage("Thêm thất bại!\nMã đã tồn tại hoặc trùng tên/ngày.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // === XỬ LÝ SỬA ===
    public void xuLySua() throws Exception {
        String maKM = view.getSelectedMaKM();
        if (maKM == null) {
            view.showMessage("Chưa chọn khuyến mãi để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        KhuyenMai km = taoKhuyenMaiTuForm();
        if (km == null) return;
        km.setMaKhuyenMai(maKM); // DÙNG MÃ CŨ

        if (dao.updateKhuyenMai(km)) {
            view.showMessage("Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            view.refreshData();
        } else {
            view.showMessage("Cập nhật thất bại!\nTrùng tên hoặc ngày.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // === XÓA MỀM ===
    public void xoa() throws Exception {
        String maKM = view.getSelectedMaKM();
        if (maKM == null) {
            view.showMessage("Vui lòng chọn khuyến mãi để ẩn!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        KhuyenMai km = dao.TimKhuyenMaiTheoMa(maKM);
        if (km == null) {
            view.showMessage("Không tìm thấy khuyến mãi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
            "<html><b>ẨN KHUYẾN MÃI</b><br><br>" +
            "Mã: <b><font color='blue'>" + maKM + "</font></b><br>" +
            "Tên: <b>" + km.getTenKhuyenMai() + "</b><br>" +
            "Từ <b>" + km.getNgayBatDau() + "</b> → <b>" + km.getNgayKetThuc() + "</b><br><br>" +
            "<i>Sẽ <u>không hiển thị</u> và <u>không áp dụng</u> nữa!</i></html>",
            "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.deleteKhuyenMai(maKM)) {
                view.showMessage("Ẩn thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                view.refreshData();
            } else {
                view.showMessage("Không thể ẩn! Có thể đang áp dụng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // === TẠO KM TỪ FORM (CÓ MÃ) ===
    private KhuyenMai taoKhuyenMaiTuForm() {
        String maKM = view.txtMaKM.getText().trim().toUpperCase();
        String ten = view.txtTenKM.getText().trim();
        String giamStr = view.txtMucGiamGia.getText().trim();
        String dk = view.txtDieuKien.getText().trim();

        LocalDate bd = view.dateBD.getDate() != null ?
            view.dateBD.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null;
        LocalDate kt = view.dateKT.getDate() != null ?
            view.dateKT.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null;

        // === VALIDATE MÃ ===
        if (maKM.isEmpty()) {
            view.showMessage("Mã khuyến mãi không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (!maKM.matches("[A-Z0-9-]{3,15}")) {
            view.showMessage("Mã chỉ chứa chữ cái in hoa, số, gạch ngang (3-15 ký tự)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // === VALIDATE TÊN ===
        if (ten.isEmpty()) {
            view.showMessage("Tên khuyến mãi không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // === VALIDATE NGÀY ===
        if (bd == null || kt == null) {
            view.showMessage("Chọn đầy đủ ngày bắt đầu và kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (bd.isAfter(kt)) {
            view.showMessage("Ngày bắt đầu không được sau ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // === VALIDATE GIẢM GIÁ ===
        BigDecimal giam;
        try {
            giam = new BigDecimal(giamStr);
            if (giam.compareTo(BigDecimal.ZERO) <= 0 || giam.compareTo(new BigDecimal("100")) >= 100) {
                throw new Exception();
            }
        } catch (Exception e) {
            view.showMessage("Mức giảm phải từ 1% đến 99%!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        KhuyenMai km = new KhuyenMai();
        km.setMaKhuyenMai(maKM);
        km.setTenKhuyenMai(ten);
        km.setMucGiamGia(giam);
        km.setNgayBatDau(bd);
        km.setNgayKetThuc(kt);
        km.setDieuKien(dk);
        return km;
    }

    // === TẢI DỮ LIỆU ===
    public void loadDataToTable() {
        view.getModelKM().setRowCount(0);
        ArrayList<KhuyenMai> ds = dao.getAllKhuyenMai();
        for (KhuyenMai km : ds) {
            view.getModelKM().addRow(new Object[]{
                km.getMaKhuyenMai(),
                km.getTenKhuyenMai(),
                km.getMucGiamGia() + "%",
                km.getNgayBatDau(),
                km.getNgayKetThuc(),
                km.getDieuKien()
            });
        }
    }

    // === CLICK TABLE ===
    public void handleTableSelection() throws Exception {
        String maKM = view.getSelectedMaKM();
        if (maKM != null) {
            KhuyenMai km = dao.TimKhuyenMaiTheoMa(maKM);
            if (km != null) {
                view.loadFormData(km);
                view.enableFormFields(false);
                view.txtMaKM.setEnabled(false); // KHÓA MÃ
            }
        }
    }

    // === LẤY TẤT CẢ (CHO TÌM KIẾM) ===
    public ArrayList<KhuyenMai> dsKhuyenMai() {
        return dao.getAllKhuyenMai();
    }

   
    
}