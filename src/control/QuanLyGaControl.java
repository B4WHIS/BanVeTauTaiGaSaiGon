package control;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import dao.GaDAO;
import entity.Ga;
import gui.QuanLyGa;

public class QuanLyGaControl {
    private GaDAO gaDao;
    private QuanLyGa view;

    public QuanLyGaControl() {
        this.gaDao = new GaDAO();
    }

    // Constructor có view để giao tiếp ngược lại
    public QuanLyGaControl(QuanLyGa view) {
        this();
        this.view = view;
    }

    // Lấy danh sách ga hoạt động (hiện tại tất cả)
    public List<Ga> layDanhSachGa() throws SQLException {
        return gaDao.getAllGa();
    }

    // Thêm ga mới
    public boolean themGa(Ga ga) {
        if (ga.getTenGa() == null || ga.getTenGa().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Tên ga không được để trống!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (ga.getDiaChi() == null || ga.getDiaChi().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Địa chỉ không được để trống!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra trùng tên ga
		List<Ga> dsGa = gaDao.getGaByTenGa(ga.getTenGa());
		if (!dsGa.isEmpty()) {
		    JOptionPane.showMessageDialog(view, "Tên ga đã tồn tại!", "Lỗi trùng lặp", JOptionPane.WARNING_MESSAGE);
		    return false;
		}

		boolean ketQua = gaDao.addGa(ga);
		if (ketQua) {
		    // Cập nhật mã ga tự sinh từ DB
		    String maGaMoi = ga.getMaGa(); // Đã được sinh trong DAO
		    JOptionPane.showMessageDialog(view, "Thêm ga thành công! Mã ga: " + maGaMoi, "Thành công", JOptionPane.INFORMATION_MESSAGE);
		}
		return ketQua;
    }

    // Cập nhật ga
    public boolean capNhatGa(Ga ga) {
        if (ga.getMaGa() == null || ga.getMaGa().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Mã ga không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (ga.getTenGa() == null || ga.getTenGa().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Tên ga không được để trống!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (ga.getDiaChi() == null || ga.getDiaChi().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Địa chỉ không được để trống!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra trùng tên ga (ngoại trừ chính nó)
		List<Ga> dsGa = gaDao.getGaByTenGa(ga.getTenGa());
		for (Ga g : dsGa) {
		    if (!g.getMaGa().equals(ga.getMaGa())) {
		        JOptionPane.showMessageDialog(view, "Tên ga đã tồn tại!", "Lỗi trùng lặp", JOptionPane.WARNING_MESSAGE);
		        return false;
		    }
		}

		boolean ketQua = gaDao.updateGa(ga);
		if (ketQua) {
		    JOptionPane.showMessageDialog(view, "Cập nhật ga thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
		}
		return ketQua;
    }

    // Xóa ga (có kiểm tra ràng buộc)
    public boolean xoaGa(String maGa) {
        if (maGa == null || maGa.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Mã ga không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        boolean ketQua = gaDao.deleteGa(maGa);
		if (ketQua) {
		    JOptionPane.showMessageDialog(view, "Xóa ga thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
		} else {
		    JOptionPane.showMessageDialog(view, "Không tìm thấy ga để xóa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		}
		return ketQua;
    }

    // Tìm kiếm ga (theo tên hoặc địa chỉ)
    public void timKiemGa(String tenGa, String diaChi, DefaultTableModel model) {
        model.setRowCount(0);
        try {
            List<Ga> dsTim = gaDao.getAllGa(); // Lấy tất cả trước
            boolean coKetQua = false;

            for (Ga ga : dsTim) {
                boolean match = true;

                if (tenGa != null && !tenGa.trim().isEmpty()) {
                    match = match && ga.getTenGa().toLowerCase().contains(tenGa.toLowerCase());
                }
                if (diaChi != null && !diaChi.trim().isEmpty()) {
                    match = match && ga.getDiaChi().toLowerCase().contains(diaChi.toLowerCase());
                }

                if (match) {
                    Object[] row = {
                        ga.getMaGa(),
                        ga.getTenGa(),
                        ga.getDiaChi()
                    };
                    model.addRow(row);
                    coKetQua = true;
                }
            }

            if (!coKetQua) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy ga nào phù hợp.", "Kết quả tìm kiếm", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Lấy ga theo mã
    public Ga layGaTheoMa(String maGa) throws SQLException {
        return gaDao.getGaByMaGa(maGa);
    }
}