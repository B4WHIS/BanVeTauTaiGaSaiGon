package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class NhanVienQuanLyGUI extends GiaoDienChinh implements ActionListener {

    public NhanVienQuanLyGUI() {
        super();
        pnlChucNang = taoPanelMenuChinh();
        pnlChinh.add(pnlChucNang);

        // Gắn sự kiện cho các nút
        btnDatVe.addActionListener(this);
        btnHuyVe.addActionListener(this);
        btnDoiVe.addActionListener(this);
        btnTimChuyenTau.addActionListener(this);
        btnDangXuat.addActionListener(this);
    }

    public JPanel taoPanelMenuChinh() {
        pnlChucNang = new JPanel();
        pnlChucNang.setLayout(new GridLayout(0, 2, 0, 10));
        pnlChucNang.setBackground(new Color(221, 218, 208));

        // === Nút Quản lý nhân viên ===
        btnDatVe = new JButton("QUẢN LÝ NHÂN VIÊN");
        btnDatVe.setBackground(Color.white);
        btnDatVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnDatVe.setForeground(new Color(93, 156, 236));
        btnDatVe.setIcon(chinhKichThuoc("/img/group.png", 60, 60));
        themHieuUngHover(btnDatVe, new Color(93, 156, 236));

        // === Nút Quản lý hành khách ===
        btnHuyVe = new JButton("QUẢN LÝ HÀNH KHÁCH");
        btnHuyVe.setBackground(Color.white);
        btnHuyVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnHuyVe.setForeground(new Color(74, 140, 103));
        btnHuyVe.setIcon(chinhKichThuoc("/img/passenger.png", 55, 55));
        themHieuUngHover(btnHuyVe, new Color(74, 140, 103));

        // === Nút Quản lý chuyến tàu ===
        btnDoiVe = new JButton("QUẢN LÝ CHUYẾN TÀU");
        btnDoiVe.setBackground(Color.white);
        btnDoiVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnDoiVe.setForeground(new Color(247, 82, 112));
        btnDoiVe.setIcon(chinhKichThuoc("/img/QLtrain.png", 55, 55));
        themHieuUngHover(btnDoiVe, new Color(247, 82, 112));

        // === Nút Quản lý khuyến mãi ===
        btnTimChuyenTau = new JButton("QUẢN LÝ KHUYẾN MÃI");
        btnTimChuyenTau.setBackground(Color.white);
        btnTimChuyenTau.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnTimChuyenTau.setForeground(new Color(228, 90, 146));
        btnTimChuyenTau.setIcon(chinhKichThuoc("/img/coupon.png", 65, 65));
        themHieuUngHover(btnTimChuyenTau, new Color(228, 90, 146));

        // === Nút Đăng xuất ===
        btnDangXuat = new JButton("ĐĂNG XUẤT");
        btnDangXuat.setBackground(Color.white);
        btnDangXuat.setFont(new Font("Segoe UI", Font.BOLD, 30));
        btnDangXuat.setForeground(Color.orange);
        btnDangXuat.setIcon(chinhKichThuoc("/img/export.png", 55, 55));
        themHieuUngHover(btnDangXuat, Color.orange);

        pnlChucNang.add(btnDatVe);
        pnlChucNang.add(btnHuyVe);
        pnlChucNang.add(btnDoiVe);
        pnlChucNang.add(btnTimChuyenTau);
        pnlChucNang.add(btnDangXuat);

        pnlChucNang.setPreferredSize(new Dimension(350, 70));
        return pnlChucNang;
    }

    /** 
     * Thêm hiệu ứng hover đổi nền nhẹ 
     */
    private void themHieuUngHover(JButton btn, Color color) {
        btn.setFocusPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.white);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnDatVe) {
            new QuanLyNhanVien().setVisible(true);
            this.dispose();
        } 
        else if (src == btnHuyVe) {
            new QuanLyHanhKhach().setVisible(true);
            this.dispose();
        } 
        else if (src == btnDoiVe) {
            new QuanLyChuyenTau().setVisible(true);
            this.dispose();
        } 
        else if (src == btnTimChuyenTau) {
            new DanhSachKhuyenMai().setVisible(true);
            this.dispose();
        } 
        else if (src == btnDangXuat) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn đăng xuất không?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
					new DangNhapGUI().setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                this.dispose();
            }
        }
    }
}
