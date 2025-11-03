package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
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

        switch (cmd) {
            case "Thêm" -> {
                them();
                try {
                    xuLyThem();  // ← TỰ ĐỘNG LƯU
                } catch (Exception ex) {
                    view.showMessage("Lỗi thêm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
            case "Sửa" -> {
                sua();
                try {
                    xuLySua();   // ← TỰ ĐỘNG LƯU
                } catch (Exception ex) {
                    view.showMessage("Lỗi sửa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
            case "Xóa" -> {
            	
            	try {
            		xoa();
            	} 
            catch (Exception e1){// TODO Auto-generated catch block
            	e1.printStackTrace();
            	}
            }
            case "Làm mới" ->{ 
            	try  
            	{
            		view.refreshData();
            		} catch (Exception e1){// TODO Auto-generated catch block
            			e1.printStackTrace();
            			}
            	}
            case "Xuất Excel" -> xuatExcel();
            case "Tìm" -> {
            	try 
            	{
            		timKiem();
            		} catch (Exception e1){// TODO Auto-generated catch block
            			e1.printStackTrace();
            			}
            	}
            case "Trở về" -> view.dispose();
        }
    }

    public void them() {
        view.resetForm();
        view.enableFormFields(true);
        view.showMessage("Nhập thông tin lịch trình mới → Nhấn THÊM để lưu!", "Hướng dẫn", JOptionPane.INFORMATION_MESSAGE);
    }

    public void sua() {
        String ma = view.getSelectedMaLichTrinh();
        if (ma == null) {
            view.showMessage("Vui lòng chọn lịch trình cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        view.enableFormFields(true);
        view.showMessage("Sửa thông tin → Nhấn SỬA để lưu!", "Hướng dẫn", JOptionPane.INFORMATION_MESSAGE);
    }

    private void xoa() throws Exception {
        String ma = view.getSelectedMaLichTrinh();
        if (ma == null) {
            view.showMessage("Vui lòng chọn lịch trình để XÓA!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LichTrinh lt = dao.getLichTrinhByMaLichTrinh(ma);
        if (lt == null) return;

        int confirm = JOptionPane.showConfirmDialog(view,
            "<html><b>XÁC NHẬN ẨN LỊCH TRÌNH</b><br><br>" +
            "Mã: <font color='blue'><b>" + ma + "</b></font><br>" +
            "Tên: <b>" + lt.getTenLichTrinh() + "</b><br>" +
            "Từ <b>" + lt.getMaGaDi().getTenGa() + "</b> → <b>" + lt.getMaGaDen().getTenGa() + "</b><br><br>" +
            "<i>Lịch trình sẽ <u>không hiển thị</u> trong danh sách chính<br>" +
            "và <u>không dùng để đặt vé</u> nữa.</i></html>",
            "Xóa Lịch Trình", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.deleteLichTrinh(ma)) {
                view.showMessage("Đã ẨN lịch trình thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                view.refreshData();
                view.resetForm();
            } else {
                view.showMessage("Không thể ẩn! Lịch trình đang có chuyến tàu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // === THÊM LỊCH TRÌNH ===
    public void xuLyThem() throws Exception {
        if (!validateInput()) return;
        
        LichTrinh lt = taoLichTrinhTuForm();
        if (dao.addLichTrinh(lt)) {
            view.showMessage("Thêm thành công! Mã: " + lt.getMaLichTrinh(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
            view.refreshData();
            view.resetForm();
            view.enableFormFields(false);
        } else {
            view.showMessage("Thêm thất bại!\nĐã tồn tại lịch trình từ ga này đến ga kia!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // === SỬA LỊCH TRÌNH ===
    public void xuLySua() throws Exception {
        String ma = view.getSelectedMaLichTrinh();
        if (ma == null) {
            view.showMessage("Chưa chọn lịch trình!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validateInput()) return;

        LichTrinh lt = taoLichTrinhTuForm();
        lt.setMaLichTrinh(ma);

        if (dao.updateLichTrinh(lt)) {
            view.showMessage("Sửa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            view.refreshData();
            view.resetForm();
            view.enableFormFields(false);
        } else {
            view.showMessage("Sửa thất bại!\nĐã tồn tại lịch trình từ ga này đến ga kia!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput() {
        String ten = view.getTxtTenLichTrinh().getText().trim();
        String kcStr = view.getTxtKhoangCach().getText().trim();
        String gaDi = (String) view.getCbGaDi().getSelectedItem();
        String gaDen = (String) view.getCbGaDen().getSelectedItem();

        if (ten.isEmpty() || kcStr.isEmpty() || gaDi == null || gaDen == null) {
            view.showMessage("Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        double kc;
        try {
            kc = Double.parseDouble(kcStr);
            if (kc <= 0) throw new Exception();
        } catch (Exception ex) {
            view.showMessage("Khoảng cách phải là số dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String maDi = extractMaGa(gaDi);
        String maDen = extractMaGa(gaDen);
        if (maDi.equals(maDen)) {
            view.showMessage("Ga đi và ga đến phải khác nhau!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Ga gaDiObj = gaDAO.getGaByMaGa(maDi);
        Ga gaDenObj = gaDAO.getGaByMaGa(maDen);
        if (gaDiObj == null || gaDenObj == null) {
            view.showMessage("Ga không tồn tại trong hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private LichTrinh taoLichTrinhTuForm() throws Exception {
        String ten = view.getTxtTenLichTrinh().getText().trim();
        double kc = Double.parseDouble(view.getTxtKhoangCach().getText().trim());
        String maDi = extractMaGa((String) view.getCbGaDi().getSelectedItem());
        String maDen = extractMaGa((String) view.getCbGaDen().getSelectedItem());

        Ga gaDi = gaDAO.getGaByMaGa(maDi);
        Ga gaDen = gaDAO.getGaByMaGa(maDen);

        LichTrinh lt = new LichTrinh();
        lt.setTenLichTrinh(ten);
        lt.setMaGaDi(gaDi);
        lt.setMaGaDen(gaDen);
        lt.setKhoangCach(kc);
        return lt;
    }

    private String extractMaGa(String item) {
        if (item == null) return "";
        int start = item.lastIndexOf("(");
        int end = item.lastIndexOf(")");
        return (start != -1 && end != -1) ? item.substring(start + 1, end) : "";
    }

    private void timKiem() throws Exception {
        String keyword = view.getTxtTenLichTrinh().getText().trim();
        if (keyword.isEmpty()) {
            view.refreshData();
            view.showMessage("Nhập tên lịch trình hoặc ga để tìm!", "Tìm kiếm", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        view.getModelLT().setRowCount(0);
        List<LichTrinh> allLichTrinh = dao.getAllLichTrinh();
        
        List<LichTrinh> ketQuaTim = new ArrayList<>();
        for (LichTrinh lt : allLichTrinh) {
            if (lt.getTenLichTrinh().toLowerCase().contains(keyword.toLowerCase()) ||
                lt.getMaGaDi().getTenGa().toLowerCase().contains(keyword.toLowerCase()) ||
                lt.getMaGaDen().getTenGa().toLowerCase().contains(keyword.toLowerCase())) {
                ketQuaTim.add(lt);
            }
        }
        
        for (LichTrinh lt : ketQuaTim) {
            view.getModelLT().addRow(new Object[]{
                lt.getMaLichTrinh(),
                lt.getTenLichTrinh(),
                lt.getMaGaDi().getTenGa() + " (" + lt.getMaGaDi().getMaGa() + ")",
                lt.getMaGaDen().getTenGa() + " (" + lt.getMaGaDen().getMaGa() + ")",
                lt.getKhoangCach()
            });
        }

        int count = ketQuaTim.size();
        if (count == 0) {
            view.showMessage("Không tìm thấy lịch trình nào phù hợp!", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
        } else {
            view.showMessage("Tìm thấy " + count + " kết quả cho: \"" + keyword + "\"", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void xuatExcel() {
        view.showMessage("Chức năng Xuất Excel đang phát triển!\nSắp có hỗ trợ phân trang + lọc.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void handleTableSelection() throws Exception {
        String ma = view.getSelectedMaLichTrinh();
        if (ma != null) {
            LichTrinh lt = dao.getLichTrinhByMaLichTrinh(ma);
            if (lt != null) {
                view.loadFormData(lt);
                view.enableFormFields(false);
            }
        }
    }
}