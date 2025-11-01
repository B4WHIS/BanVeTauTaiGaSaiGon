package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import dao.GaDAO;
import dao.LichTrinhDAO;
import entity.Ga;
import entity.LichTrinh;
import gui.QuanLyLichTrinh;

public class QuanLyLichTrinhController implements ActionListener {
    private QuanLyLichTrinh view;
    private LichTrinhDAO dao;
    private GaDAO gaDAO;

    public QuanLyLichTrinhController(QuanLyLichTrinh view) {
        this.view = view;
        this.dao = new LichTrinhDAO();
        this.gaDAO = new GaDAO();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Thêm")) them();
        else if (cmd.equals("Sửa")) sua();
        else if (cmd.equals("Xóa")) xoa();
        else if (cmd.equals("Làm mới")) view.refreshData();
        else if (cmd.equals("Lưu"))
			try {
				luu();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else if (cmd.equals("Xuất Excel")) xuatExcel();
        else if (cmd.equals("Trở về")) view.dispose();
    }

    private void them() {
        view.resetForm();
        view.enableFormFields(true);
        view.getBtnLuu().setEnabled(true);
        view.getBtnLuu().putClientProperty("action", "add");
    }

    private void sua() {
        if (view.getSelectedMaLichTrinh() == null) {
            view.showMessage("Chọn lịch trình để sửa!", "Cảnh báo", 2);
            return;
        }
        view.enableFormFields(true);
        view.getBtnLuu().setEnabled(true);
        view.getBtnLuu().putClientProperty("action", "edit");
    }

    private void xoa() {
        String ma = view.getSelectedMaLichTrinh();
        if (ma == null) {
            view.showMessage("Chọn lịch trình để xóa!", "Cảnh báo", 2);
            return;
        }
        int c = JOptionPane.showConfirmDialog(view, "Xóa lịch trình " + ma + "?", "Xác nhận", 0);
        if (c == 0 && dao.deleteLichTrinh(ma)) {
            view.showMessage("Xóa thành công!", "Thành công", 1);
            view.refreshData();
        } else if (c == 0) {
            view.showMessage("Xóa thất bại!", "Lỗi", 0);
        }
    }

    private void luu() throws Exception {
        String action = (String) view.getBtnLuu().getClientProperty("action");
        String tenLT = view.getTxtTenLichTrinh().getText().trim();
        String kcStr = view.getTxtKhoangCach().getText().trim();
        String gaDiItem = (String) view.getCbGaDi().getSelectedItem();
        String gaDenItem = (String) view.getCbGaDen().getSelectedItem();

        if (tenLT.isEmpty() || kcStr.isEmpty() || gaDiItem == null || gaDenItem == null) {
            view.showMessage("Điền đầy đủ thông tin!", "Lỗi", 0);
            return;
        }

        double kc;
        try {
            kc = Double.parseDouble(kcStr);
            if (kc < 0) throw new Exception();
        } catch (Exception ex) {
            view.showMessage("Khoảng cách phải là số >= 0!", "Lỗi", 0);
            return;
        }

        String maGaDi = extractMaGa(gaDiItem);
        String maGaDen = extractMaGa(gaDenItem);
        if (maGaDi.equals(maGaDen)) {
            view.showMessage("Ga đi và ga đến phải khác nhau!", "Lỗi", 0);
            return;
        }

        Ga gaDi = gaDAO.getGaByMaGa(maGaDi);
        Ga gaDen = gaDAO.getGaByMaGa(maGaDen);
        LichTrinh lt = new LichTrinh();
        try {
            lt.setTenLichTrinh(tenLT);
            lt.setMaGaDi(gaDi);
            lt.setMaGaDen(gaDen);
            lt.setKhoangCach(kc);
        } catch (Exception ex) {
            view.showMessage("Lỗi: " + ex.getMessage(), "Lỗi", 0);
            return;
        }

        boolean success = false;
        if ("add".equals(action)) {
            success = dao.addLichTrinh(lt);
            if (success) view.showMessage("Thêm thành công! Mã: " + lt.getMaLichTrinh(), "Thành công", 1);
        } else {
            lt.setMaLichTrinh(view.getSelectedMaLichTrinh());
            success = dao.updateLichTrinh(lt);
            if (success) view.showMessage("Sửa thành công!", "Thành công", 1);
        }

        if (success) view.refreshData();
        else view.showMessage("Thao tác thất bại!", "Lỗi", 0);
    }

    private String extractMaGa(String item) {
        return item.substring(item.indexOf("(") + 1, item.indexOf(")"));
    }

    private void xuatExcel() {
        view.showMessage("Chức năng đang phát triển!", "Thông báo", 1);
    }

    public void handleTableSelection() {
        String ma = view.getSelectedMaLichTrinh();
        if (ma != null) {
            LichTrinh lt = dao.getLichTrinhByMaLichTrinh(ma);
            view.loadFormData(lt);
        }
    }
}