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
    private boolean isAddMode = false;  
    private TaiKhoan selectedTaiKhoan;  

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
    
 private void handleThem() {
     view.resetForm();                    
     isAddMode = true;
     selectedTaiKhoan = null;

     
     view.enableFormFields(true);
     view.getBtnLuu().setEnabled(true);

    
     view.getBtnSua().setEnabled(false);
     view.getBtnXoa().setEnabled(false);

  
     view.getTxtMatKhau().requestFocus();

     view.showMessage(
         "Chế độ **THÊM MỚI**. Chọn nhân viên, nhập mật khẩu và nhấn **Lưu**.",
         "Thêm tài khoản", JOptionPane.INFORMATION_MESSAGE);
 }

 
 private void handleLuu() {
	    try {
	        if (!validateForm()) return;

	        TaiKhoan tk = createTaiKhoanFromForm();

	        boolean ok;
	        if (isAddMode) {
	            NhanVien nv = tk.getNhanVien();
	            if (dao.getTaiKhoanByTenDangNhap(nv.getSoDienThoai()) != null) {
	                view.showMessage(
	                    "Nhân viên '" + nv.getHoTen() + "' đã có tài khoản (SDT: " + nv.getSoDienThoai() + ")!",
	                    "Lỗi", JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	            ok = dao.addTaiKhoan(tk);
	        } else {
	            ok = dao.updateTaiKhoan(tk);
	        }

	        if (ok) {
	            view.refreshData();
	            view.showMessage(isAddMode ? "Thêm tài khoản thành công!" : "Cập nhật thành công!",
	                             "Thành công", JOptionPane.INFORMATION_MESSAGE);
	            view.resetForm();
	            view.getBtnSua().setEnabled(false);
	            view.getBtnXoa().setEnabled(false);
	            view.getBtnLuu().setEnabled(false);
	            view.enableFormFields(false);
	        } else {
	            view.showMessage("Lưu thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (IllegalArgumentException ex) {
	        view.showMessage(ex.getMessage(), "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        view.showMessage("Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	    }
	}
 
 private boolean validateForm() {
     if (view.getCbNhanVien().getSelectedIndex() < 0) {
         view.showMessage("Vui lòng chọn nhân viên.", "Lỗi", JOptionPane.WARNING_MESSAGE);
         return false;
     }
     String mk = new String(view.getTxtMatKhau().getPassword());
     if (mk.isEmpty()) {
         view.showMessage("Vui lòng nhập mật khẩu.", "Lỗi", JOptionPane.WARNING_MESSAGE);
         return false;
     }
     return true;
 }


 private TaiKhoan createTaiKhoanFromForm() {
     TaiKhoan tk = new TaiKhoan();

     // ---- nhân viên ----
     NhanVien nv = (NhanVien) view.getCbNhanVien().getSelectedItem();
     if (nv == null) throw new IllegalArgumentException("Không tìm thấy nhân viên.");
     tk.setNhanVien(nv);                

     // ---- mật khẩu ----
     String mk = new String(view.getTxtMatKhau().getPassword());
     tk.setMatKhau(mk);                 

     return tk;
 }
   
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
                view.enableFormFields(false); 
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

       
        NhanVien currentNv = selectedTaiKhoan.getNhanVien();
        if (currentNv != null && view.getCbNhanVien().getSelectedItem() == null) {
            view.getCbNhanVien().addItem(currentNv);
        }

        view.loadFormData(selectedTaiKhoan);
        view.enableFormFields(true);  // Enable fields cho edit

       
        view.getCbNhanVien().setEnabled(false);

        view.getBtnLuu().setEnabled(true);
        view.showMessage("Chế độ sửa. Sửa mật khẩu và nhấn Lưu.", "Sửa tài khoản", JOptionPane.INFORMATION_MESSAGE);

        
        view.getTxtMatKhau().requestFocus();
    }

    
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

    
    private void handleReset() {
        System.out.println("Làm mới dữ liệu.");
        view.refreshData();
        view.getBtnSua().setEnabled(false);
        view.getBtnXoa().setEnabled(false);
        view.getBtnLuu().setEnabled(false);
        view.enableFormFields(false);  
    }

    
    private void handleExport() {
        System.out.println("Xuất Excel (chưa implement).");
        view.showMessage("Chức năng xuất Excel đang được phát triển.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

   
    private void handleTroVe() {
        System.out.println("Thoát form.");
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn thoát?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose();
        }
    }

}