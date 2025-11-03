package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import control.QuanLyVeControl;
import entity.LichSuVe;
import entity.NhanVien;

public class GiaoDienHuyVe extends GiaoDienChinh {
    private JPanel jpPhai;
    private CardLayout cardLayout;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblTongTien, lblTongPhi, lblTongHoan;
    private List<String> maVeList;
    private QuanLyVeControl veControl = new QuanLyVeControl();
    private NhanVien nhanVien = new NhanVien("NV-001", "Nguyễn Văn A", LocalDate.of(1985, 05, 15), "0987654321", "001122334455", 1); // Thay bằng đăng nhập thật

    public GiaoDienHuyVe(List<Object[]> danhSachVe, List<String> maVeList) {
        this.maVeList = maVeList;
        setTitle("Hủy vé tàu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents(danhSachVe);
        setupLayout();
        tinhToanTong();
    }

    private void initComponents(List<Object[]> danhSachVe) {
        jpPhai = new JPanel();
        cardLayout = new CardLayout();
        jpPhai.setLayout(cardLayout);

        JPanel step1 = taoBuoc1(danhSachVe);
        jpPhai.add(step1, "Step1");

       
        JPanel step2 = taoBuoc2();
        jpPhai.add(step2, "Step2");

       
        JPanel step3 = taoBuoc3();
        jpPhai.add(step3, "Step3");
    }

    private JPanel taoBuoc1(List<Object[]> danhSachVe) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Chọn vé để hủy", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(74, 140, 103));

        // BẢNG GIỐNG TRA CỨU
        String[] cols = {"#", "Họ tên", "Thông tin vé", "Thành tiền (VNĐ)", "Loại vé", "Trạng thái"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        for (Object[] row : danhSachVe) {
            tableModel.addRow(row);
        }

        table = new JTable(tableModel);
        table.setRowHeight(60);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        JScrollPane scroll = new JScrollPane(table);

        // TỔNG TIỀN
        JPanel panelTong = new JPanel(new GridLayout(1, 3, 20, 0));
        panelTong.setBorder(BorderFactory.createTitledBorder("Tổng kết"));
        lblTongTien = new JLabel("Tổng tiền vé: 0 VNĐ");
        lblTongPhi = new JLabel("Tổng phí hủy: 0 VNĐ");
        lblTongHoan = new JLabel("Tổng hoàn: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongPhi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongHoan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelTong.add(lblTongTien);
        panelTong.add(lblTongPhi);
        panelTong.add(lblTongHoan);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelTong, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel taoBuoc2() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Xác nhận hủy vé", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JTable confirmTable = new JTable(tableModel);
        confirmTable.setEnabled(false);
        JScrollPane scroll = new JScrollPane(confirmTable);

        JLabel note = new JLabel("Kiểm tra kỹ thông tin trước khi xác nhận.");
        note.setForeground(Color.RED);
        note.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(note, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel taoBuoc3() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel success = new JLabel("HỦY VÉ THÀNH CÔNG!", SwingConstants.CENTER);
        success.setFont(new Font("Segoe UI", Font.BOLD, 28));
        success.setForeground(new Color(74, 140, 103));

        JLabel detail = new JLabel("<html><center>Đã hoàn tất thủ tục hủy.<br>Tiền sẽ được hoàn trong 3-5 ngày làm việc.</center></html>");
        detail.setHorizontalAlignment(SwingConstants.CENTER);
        detail.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        panel.add(success, BorderLayout.CENTER);
        panel.add(detail, BorderLayout.SOUTH);
        return panel;
    }

    private void setupLayout() {
        JPanel main = new JPanel(new BorderLayout());
        main.add(taoThanhTienTrinh(), BorderLayout.NORTH);
        main.add(jpPhai, BorderLayout.CENTER);
        main.add(taoDieuHuong(), BorderLayout.SOUTH);
        add(main);
    }

    private JPanel taoThanhTienTrinh() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        panel.setBackground(new Color(74, 140, 103));
        String[] steps = {"Chọn vé", "Xác nhận", "Hoàn tất"};
        for (int i = 0; i < steps.length; i++) {
            JLabel lbl = new JLabel(steps[i]);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            panel.add(lbl);
            if (i < steps.length - 1) {
                JLabel arrow = new JLabel("→");
                arrow.setForeground(Color.WHITE);
                panel.add(arrow);
            }
        }
        return panel;
    }

    private JPanel taoDieuHuong() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));

        JButton btnQuayLai = new JButton("Quay lại");
        JButton btnTiepTheo = new JButton("Tiếp theo");   // nút duy nhất bên phải

        // ---------- NÚT QUAY LẠI ----------
        btnQuayLai.addActionListener(e -> {
            // Bước 1: quay về giao diện tra cứu
            dispose();                                          // đóng cửa sổ hủy vé
            SwingUtilities.invokeLater(() -> new GiaoDienTraCuuVeTau().setVisible(true));
        });

        // ---------- NÚT TIẾP THEO / HOÀN TẤT ----------
        btnTiepTheo.addActionListener(e -> {
            // Kiểm tra đang ở panel nào
            Component current = jpPhai.getComponent(0);   // Step1 luôn là component[0]
            if (current.isVisible()) {
                // Đang ở Bước 1 → chuyển sang Bước 2
                cardLayout.next(jpPhai);
            } else if (jpPhai.getComponent(1).isVisible()) {
                // Đang ở Bước 2 → chuyển sang Bước 3 và đổi tên nút thành "Hoàn tất"
                cardLayout.next(jpPhai);
                btnTiepTheo.setText("Hoàn tất");
            } else {
                // Đang ở Bước 3 → thực hiện hủy vé
                thucHienHuyVe();

                // Sau khi hiện thông báo, tự động quay lại giao diện tra cứu (đợi 2 giây để người dùng đọc)
                new Thread(() -> {
                    try { Thread.sleep(2000); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
                    SwingUtilities.invokeLater(() -> {
                        dispose();
                        new GiaoDienTraCuuVeTau().setVisible(true);
                    });
                }).start();
            }
        });

        panel.add(btnQuayLai);
        panel.add(btnTiepTheo);
        return panel;
    }

    private void tinhToanTong() {
        
        BigDecimal tongVe = BigDecimal.ZERO;
        BigDecimal tongPhi = BigDecimal.ZERO;
        BigDecimal tongHoan = BigDecimal.ZERO;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String thanhTienStr = (String) tableModel.getValueAt(i, 3);
            thanhTienStr = thanhTienStr.replace(".", "");
            BigDecimal tienVe = new BigDecimal(thanhTienStr);
            tongVe = tongVe.add(tienVe);

          
            BigDecimal phi = tienVe.multiply(new BigDecimal("0.1"));
            tongPhi = tongPhi.add(phi);
        }
        tongHoan = tongVe.subtract(tongPhi);

        lblTongTien.setText("Tổng tiền vé: " + String.format("%,.0f", tongVe) + " VNĐ");
        lblTongPhi.setText("Tổng phí hủy: ~" + String.format("%,.0f", tongPhi) + " VNĐ");
        lblTongHoan.setText("Tổng hoàn: ~" + String.format("%,.0f", tongHoan) + " VNĐ");
    }
    private void thucHienHuyVe() {
        List<String> thanhCong = new ArrayList<>();
        List<String> thatBai   = new ArrayList<>();
        BigDecimal tongVe   = BigDecimal.ZERO;
        BigDecimal tongPhi  = BigDecimal.ZERO;
        BigDecimal tongHoan = BigDecimal.ZERO;

        for (String maVe : maVeList) {
            try {
                LichSuVe ls = veControl.xuLyHuyVe(maVe, "Hủy từ giao diện", nhanVien);

                BigDecimal phi   = ls.getPhiXuLy() != null ? ls.getPhiXuLy() : BigDecimal.ZERO;
                BigDecimal hoan  = ls.getTienHoan() != null ? ls.getTienHoan() : BigDecimal.ZERO;
                BigDecimal tienVe = hoan.add(phi);

                thanhCong.add(maVe);
                tongVe   = tongVe.add(tienVe);
                tongPhi  = tongPhi.add(phi);
                tongHoan = tongHoan.add(hoan);
            } catch (Exception ex) {
                thatBai.add(maVe + ": " + ex.getMessage());
            }
        }

        // Cập nhật nhãn tổng kết
        lblTongTien.setText("Tổng tiền vé: " + String.format("%,.0f", tongVe) + " VNĐ");
        lblTongPhi.setText ("Tổng phí hủy: " + String.format("%,.0f", tongPhi) + " VNĐ");
        lblTongHoan.setText("Tổng hoàn: " + String.format("%,.0f", tongHoan) + " VNĐ");

        // Thông báo kết quả
        String msg = "<html><b>Hủy thành công: " + thanhCong.size() + " vé</b><br>";
        if (!thatBai.isEmpty()) {
            msg += "<b>Thất bại:</b><br>" + String.join("<br>", thatBai) + "<br>";
        }
        msg += "<b>Tổng hoàn: " + String.format("%,.0f", tongHoan) + " VNĐ</b></html>";
        JOptionPane.showMessageDialog(this, msg, "Kết quả hủy vé", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new GiaoDienHuyVe(null, null).setVisible(true);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}