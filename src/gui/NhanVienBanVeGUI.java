package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import entity.NhanVien;

public class NhanVienBanVeGUI extends JFrame implements ActionListener {
    private JPanel pnlChucNang;
    private JPanel pnlChinh; // Assumed to be initialized in constructor
    private JButton btnDatVe;
    private JButton btnHuyVe;
    private JButton btnDoiVe;
    private JButton btnTimChuyenTau;
    private JButton btnDangXuat;
    private NhanVien nhanVienHienTai;

    public NhanVienBanVeGUI(NhanVien nhanVien) throws IOException {
        this.nhanVienHienTai = nhanVien != null ? nhanVien : new NhanVien("NV-001");
        initializeUI();
        addListeners();
    }

    private void initializeUI() throws IOException {
        setTitle("Menu Nhân Viên Bán Vé");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        pnlChinh = new JPanel(new BorderLayout());
        pnlChinh.setBackground(new Color(221, 218, 208));

        // Title
        JLabel lblTieuDe = new JLabel("HỆ THỐNG BÁN VÉ TÀU", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTieuDe.setForeground(new Color(74, 140, 103));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        pnlChinh.add(lblTieuDe, BorderLayout.NORTH);

        // Function panel
        pnlChucNang = taoPanelMenuChinh();
        pnlChinh.add(pnlChucNang, BorderLayout.CENTER);

        add(pnlChinh);

        // Set Nimbus Look and Feel
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel taoPanelMenuChinh() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 20, 20));
        panel.setBackground(new Color(221, 218, 208));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        Color mauDatVe = new Color(74, 140, 103);
        Color mauHuyVe = new Color(229, 115, 115);
        Color mauDoiVe = new Color(93, 156, 236);
        Color mauTimChuyen = new Color(155, 93, 224);
        Color mauDangXuat = Color.ORANGE;

        btnDatVe = new JButton("ĐẶT VÉ");
        btnDatVe.setBackground(Color.WHITE);
        btnDatVe.setFont(new Font("Segoe UI", Font.BOLD, 30));
        btnDatVe.setForeground(mauDatVe);
        try {
            ImageIcon icDatVe = new ImageIcon(getClass().getResource("/img/tickets_icon.png"));
            btnDatVe.setIcon(icDatVe);
        } catch (Exception e) {
            System.err.println("Không thể tải icon tickets_icon.png: " + e.getMessage());
        }

        btnHuyVe = new JButton("HỦY VÉ");
        btnHuyVe.setBackground(Color.WHITE);
        btnHuyVe.setFont(new Font("Segoe UI", Font.BOLD, 30));
        btnHuyVe.setForeground(mauHuyVe);
        try {
            ImageIcon icHuyVe = new ImageIcon(getClass().getResource("/img/cancel2.png"));
            btnHuyVe.setIcon(icHuyVe);
        } catch (Exception e) {
            System.err.println("Không thể tải icon cancel2.png: " + e.getMessage());
        }

        btnDoiVe = new JButton("ĐỔI VÉ");
        btnDoiVe.setBackground(Color.WHITE);
        btnDoiVe.setFont(new Font("Segoe UI", Font.BOLD, 30));
        btnDoiVe.setForeground(mauDoiVe);
        try {
            ImageIcon icDoiVe = new ImageIcon(getClass().getResource("/img/exchange.png"));
            btnDoiVe.setIcon(icDoiVe);
        } catch (Exception e) {
            System.err.println("Không thể tải icon exchange.png: " + e.getMessage());
        }

        btnTimChuyenTau = new JButton("TÌM CHUYẾN TÀU");
        btnTimChuyenTau.setBackground(Color.WHITE);
        btnTimChuyenTau.setFont(new Font("Segoe UI", Font.BOLD, 30));
        btnTimChuyenTau.setForeground(mauTimChuyen);
        try {
            ImageIcon icTimChuyen = new ImageIcon(getClass().getResource("/img/search.png"));
            btnTimChuyenTau.setIcon(icTimChuyen);
        } catch (Exception e) {
            System.err.println("Không thể tải icon search.png: " + e.getMessage());
        }

        btnDangXuat = new JButton("ĐĂNG XUẤT");
        btnDangXuat.setBackground(Color.WHITE);
        btnDangXuat.setFont(new Font("Segoe UI", Font.BOLD, 30));
        btnDangXuat.setForeground(mauDangXuat);
        try {
            ImageIcon icDangXuat = new ImageIcon(getClass().getResource("/img/export.png"));
            btnDangXuat.setIcon(icDangXuat);
        } catch (Exception e) {
            System.err.println("Không thể tải icon export.png: " + e.getMessage());
        }

        panel.add(btnDatVe);
        panel.add(btnHuyVe);
        panel.add(btnDoiVe);
        panel.add(btnTimChuyenTau);
        panel.add(new JPanel()); // Empty panel for layout balance
        panel.add(btnDangXuat);

        addHoverEffect(btnDatVe, mauDatVe);
        addHoverEffect(btnHuyVe, mauHuyVe);
        addHoverEffect(btnDoiVe, mauDoiVe);
        addHoverEffect(btnTimChuyenTau, mauTimChuyen);
        addHoverEffect(btnDangXuat, mauDangXuat);

        return panel;
    }

    private void addHoverEffect(JButton button, Color hoverColor) {
        Color originalColor = button.getBackground();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
                button.setForeground(hoverColor);
            }
        });
    }

    private void addListeners() {
        btnDatVe.addActionListener(this);
        btnHuyVe.addActionListener(this);
        btnDoiVe.addActionListener(this);
        btnTimChuyenTau.addActionListener(this);
        btnDangXuat.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == btnDatVe || e.getSource() == btnTimChuyenTau) {
                SwingUtilities.invokeLater(() -> {
                    new TraCuuChuyenTauGUI(nhanVienHienTai).setVisible(true);
                    dispose();
                });
            } else if (e.getSource() == btnHuyVe) {
                SwingUtilities.invokeLater(() -> {
                    new GiaoDienHuyVe().setVisible(true);
                    dispose();
                });
            } else if (e.getSource() == btnDoiVe) {
                SwingUtilities.invokeLater(() -> {
                    new TraCuuVeTauGUI().setVisible(true);
                    dispose();
                });
            } else if (e.getSource() == btnDangXuat) {
                SwingUtilities.invokeLater(() -> {
                    try {
						new DangNhapGUI().setVisible(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    dispose();
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể mở giao diện: " + ex.getMessage(), "Lỗi Hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}