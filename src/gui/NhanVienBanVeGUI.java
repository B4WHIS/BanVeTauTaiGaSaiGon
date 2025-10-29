package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entity.NhanVien;

public class NhanVienBanVeGUI extends GiaoDienChinh implements ActionListener {
    
    private JButton btnDatVe;
    private JButton btnHuyVe;
    private JButton btnTimChuyenTau;
    private JButton btnDangXuat;
    private JButton btnDoiVe;

   
    public NhanVienBanVeGUI(NhanVien nhanVien) throws IOException {
        super(nhanVien); // TRUYỀN NHÂN VIÊN CHO GIAO DIỂN CHÍNH
        pnlChucNang = taoPanelMenuChinh();
        pnlChinh.add(pnlChucNang, BorderLayout.CENTER);
    }

  
    public NhanVienBanVeGUI() throws IOException {
        this(null);
    }

    public JPanel taoPanelMenuChinh() {
        pnlChucNang = new JPanel();
        pnlChucNang.setLayout(new GridLayout(0, 2, 0, 10));
        pnlChucNang.setBackground(new Color(221, 218, 208));
        
        Color mauDatVe = new Color(74, 140, 103); 
        Color mauHuyVe = new Color(229, 115, 115); 
        Color mauDoiVe = new Color(93, 156, 236); 
        Color mauTimChuyen = new Color(155, 93, 224); 
        Color mauDangXuat = Color.orange; 
        
        // **NÚT ĐẶT VÉ**
        btnDatVe = new JButton("ĐẶT VÉ");
        btnDatVe.setBackground(Color.white);
        btnDatVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnDatVe.setForeground(new Color(74, 140, 103));
        ImageIcon icDatVe = chinhKichThuoc("/img/tickets_icon.png", 60, 60);
        btnDatVe.setIcon(icDatVe);
        
        // **NÚT HỦY VÉ**
        btnHuyVe = new JButton("HỦY VÉ");
        btnHuyVe.setBackground(Color.white);
        btnHuyVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnHuyVe.setForeground(new Color(229, 115, 115));
        ImageIcon icHuyve = chinhKichThuoc("/img/cancel2.png", 55, 55);
        btnHuyVe.setIcon(icHuyve);

        // **NÚT ĐỔI VÉ**
        btnDoiVe = new JButton("ĐỔI VÉ");
        btnDoiVe.setBackground(Color.white);
        btnDoiVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnDoiVe.setForeground(new Color(93, 156, 236));
        ImageIcon icDoiVe = chinhKichThuoc("/img/exchange.png", 55, 55);
        btnDoiVe.setIcon(icDoiVe);
        
        // **NÚT TÌM CHUYẾN**
        btnTimChuyenTau = new JButton("TÌM CHUYẾN");
        btnTimChuyenTau.setBackground(Color.white);
        btnTimChuyenTau.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnTimChuyenTau.setForeground(new Color(155, 93, 224));
        ImageIcon icTimChuyen = chinhKichThuoc("/img/search.png", 65, 65);
        btnTimChuyenTau.setIcon(icTimChuyen);
        
        // **NÚT ĐĂNG XUẤT**
        btnDangXuat = new JButton("ĐĂNG XUẤT");
        btnDangXuat.setBackground(Color.white);
        btnDangXuat.setFont(new Font("Segoe UI", Font.BOLD, 30));
        btnDangXuat.setForeground(Color.orange);
        ImageIcon icDangXuat = chinhKichThuoc("/img/export.png", 55, 55);
        btnDangXuat.setIcon(icDangXuat);
        
        // **THÊM NÚT VÀO PANEL**
        pnlChucNang.add(btnDatVe);
        pnlChucNang.add(btnHuyVe);
        pnlChucNang.add(btnDoiVe);
        pnlChucNang.add(btnTimChuyenTau);
        pnlChucNang.add(btnDangXuat);
        pnlChucNang.setPreferredSize(new Dimension(350, 70));
        
        // **THÊM HOVER EFFECT**
        addHoverEffect(btnDatVe, mauDatVe);
        addHoverEffect(btnHuyVe, mauHuyVe);
        addHoverEffect(btnDoiVe, mauDoiVe);
        addHoverEffect(btnTimChuyenTau, mauTimChuyen);
        addHoverEffect(btnDangXuat, mauDangXuat);
        
        // **GẮN SỰ KIỆN**
        btnDatVe.addActionListener(this);
        btnHuyVe.addActionListener(this);
        btnDoiVe.addActionListener(this);
        btnTimChuyenTau.addActionListener(this);
        btnDangXuat.addActionListener(this);
        
        return pnlChucNang;
    }

    
    @Override
    protected void addHoverEffect(JButton btn, Color mainColor) {
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(mainColor.brighter());
                btn.setBorder(BorderFactory.createLineBorder(mainColor, 3));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.white);
                btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand(); 
        try {
            if (command.equals("ĐẶT VÉ") || e.getSource() == btnDatVe) {
                new TraCuuChuyenTauGUI().setVisible(true); 
            } else if (command.equals("HỦY VÉ") || e.getSource() == btnHuyVe) {
                new TraCuuVeTauGUI().setVisible(true); 
            } else if (command.equals("ĐỔI VÉ") || e.getSource() == btnDoiVe) {
                new TraCuuVeTauGUI().setVisible(true);
            } else if (command.equals("TÌM CHUYẾN") || e.getSource() == btnTimChuyenTau) {
                new TraCuuChuyenTauGUI().setVisible(true); 
            } else if (command.equals("ĐĂNG XUẤT") || e.getSource() == btnDangXuat) {
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn đăng xuất không?",
                    "Xác nhận đăng xuất",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        new DangNhapGUI().setVisible(true);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    this.dispose();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể mở giao diện: " + ex.getMessage(), "Lỗi Hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new NhanVienBanVeGUI().setVisible(true);
    }
}