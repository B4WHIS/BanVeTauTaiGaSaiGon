package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import dao.GaDAO;
import entity.Ga;
import gui.QuanLyGa;

public class QuanLyGaController implements ActionListener {
    private QuanLyGa view;
    private GaDAO dao;

    public QuanLyGaController(QuanLyGa view) {
        this.view = view;
        this.dao = new GaDAO();
    }



	@Override
    public void actionPerformed(ActionEvent e) {
        String src = e.getActionCommand();
        if (src.equals("Thêm")) {
            themGa();
        } else if (src.equals("Sửa")) {
            suaGa();
        } else if (src.equals("Xóa")) {
            xoaGa();
        } else if (src.equals("Làm mới")) {
            view.refreshData();
        } else if (src.equals("Lưu")) {
            luuGa();
        } else if (src.equals("Xuất Excel")) {
            xuatExcel();
        } else if (src.equals("Trở về")) {
            view.dispose();
        }
    }

    private void themGa() {
        view.resetForm();
        view.enableFormFields(true);
        view.getBtnLuu().setEnabled(true);
        view.getBtnLuu().putClientProperty("action", "add");
    }

    private void suaGa() {
        String maGa = view.getSelectedMaGa();
        if (maGa == null) {
            view.showMessage("Chọn ga để sửa!", "Cảnh báo", 2);
            return;
        }
        view.enableFormFields(true);
        view.getBtnLuu().setEnabled(true);
        view.getBtnLuu().putClientProperty("action", "edit");
    }

    private void xoaGa() {
        String maGa = view.getSelectedMaGa();
        if (maGa == null) {
            view.showMessage("Chọn ga để xóa!", "Cảnh báo", 2);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view, "Xóa ga " + maGa + "?", "Xác nhận", 0);
        if (confirm == 0) {
            if (dao.deleteGa(maGa)) {
                view.showMessage("Xóa thành công!", "Thành công", 1);
                view.refreshData();
            } else {
                view.showMessage("Xóa thất bại!", "Lỗi", 0);
            }
        }
    }

    private void luuGa() {
        String action = (String) view.getBtnLuu().getClientProperty("action");
        String tenGa = view.getTxtTenGa().getText().trim();
        String diaChi = view.getTxtDiaChi().getText().trim();

        if (tenGa.isEmpty() || diaChi.isEmpty()) {
            view.showMessage("Vui lòng điền đầy đủ!", "Lỗi", 0);
            return;
        }

        Ga ga = new Ga();
        ga.setTenGa(tenGa);
        ga.setDiaChi(diaChi);

        try {
            if ("add".equals(action)) {
                if (dao.addGa(ga)) {
                    view.showMessage("Thêm thành công! Mã ga: " + ga.getMaGa(), "Thành công", 1);
                    view.refreshData();
                } else {
                    view.showMessage("Thêm thất bại!", "Lỗi", 0);
                }
            } else if ("edit".equals(action)) {
                ga.setMaGa(view.getSelectedMaGa());
                if (dao.updateGa(ga)) {
                    view.showMessage("Sửa thành công!", "Thành công", 1);
                    view.refreshData();
                } else {
                    view.showMessage("Sửa thất bại!", "Lỗi", 0);
                }
            }
        } catch (Exception ex) {
            view.showMessage("Lỗi validation: " + ex.getMessage(), "Lỗi", 0);
        }
    }

    private void xuatExcel() {
        // TODO: Dùng Apache POI hoặc JTable export
        view.showMessage("Chức năng đang phát triển!", "Thông báo", 1);
    }

    public void handleTableSelection() {
        String maGa = view.getSelectedMaGa();
        if (maGa != null) {
            Ga ga = dao.getGaByMaGa(maGa);
            view.loadFormData(ga);
        }
    }
}