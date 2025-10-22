package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import control.DangNhapController;

public class DangNhapGUI extends JFrame {
    
    // Các thành phần UI (giữ nguyên)
    private JPanel pnlChinh;
    private JPanel pnlTrai;
    private JLabel lblImg;
    private JPanel pnlPhai;
    private JLabel lblDn;
    private JLabel lblTenDN;
    private JTextField txtTenDN;
    private JLabel lblMatKhau;
    private JPasswordField txtMatKhau;
    private JButton btnDn;
    private JLabel lblGsg;
    private JPanel pnlForm;
    private JPanel pnlNutBam;
    private JButton btnThoat;
    private JPanel pnlGop;

    //Màu sắc (làm public để Controller truy cập nếu cần)
    public final Color COLOR_PRIMARY = new Color(74, 140, 103);    
    public final Color COLOR_BG_LIGHT = new Color(250, 247, 243);    
    public final Color COLOR_LOGIN_BUTTON = new Color(93, 156, 236); 
    public final Color COLOR_EXIT_BUTTON = new Color(229, 115, 115); 
    
    // Controller
    private DangNhapController controller;
    
    public DangNhapGUI() throws IOException {
        setTitle("Đăng nhập");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Khởi tạo UI
        setupUI();
        
        // Tạo Controller và attach listener (SỬA Ở ĐÂY: Truyền controller hai lần)
        controller = new DangNhapController(this);
        attachListeners(controller, controller);  // <-- SỬA: Thêm tham số thứ hai
    }
    
    private void setupUI() throws IOException {
        //Panel Chính
        pnlChinh = new JPanel(new GridLayout(1, 2));
        pnlTrai = new JPanel();
        pnlPhai = new JPanel();
        pnlTrai.setBackground(COLOR_BG_LIGHT);
        pnlPhai.setBackground(COLOR_BG_LIGHT);
        
        //Panel Trái
        pnlTrai.setLayout(new GridBagLayout());
        pnlGop = new JPanel();
        pnlGop.setLayout(new BoxLayout(pnlGop, BoxLayout.Y_AXIS));
        pnlGop.setBackground(COLOR_BG_LIGHT);
        
        // Thành phần trái (SỬA: Set text cho lblGsg nếu cần)
        lblGsg = new JLabel("GA SÀI GÒN");  // <-- THÊM: Set text để tránh rỗng
        lblGsg.setFont(new Font("Segoe UI", Font.BOLD, 60));
        lblGsg.setForeground(COLOR_PRIMARY);
        lblGsg.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon TauIC = GiaoDienChinh.chinhKichThuoc("/img/trainDN.png", 500, 290);
        lblImg = new JLabel(TauIC);
        lblImg.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        pnlGop.add(lblGsg);
        pnlGop.add(lblImg);
        pnlTrai.add(pnlGop);
        
        // Panel Phải (Form Đăng nhập)
        pnlPhai.setLayout(new BorderLayout(10, 10));
        pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(COLOR_BG_LIGHT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Đăng Nhập Title
        lblDn = new JLabel("ĐĂNG NHẬP");
        lblDn.setFont(new Font("Segoe UI", Font.BOLD, 45));
        lblDn.setForeground(COLOR_PRIMARY);
        gbc.gridx= 0; gbc.gridy= 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        pnlForm.add(lblDn,gbc);
        
        // Tên DN Label
        lblTenDN = new JLabel("Tên đăng nhập");
        lblTenDN.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTenDN.setForeground(new Color(66, 66, 66));
        gbc.gridx= 0; gbc.gridy= 1; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.LINE_END;
        pnlForm.add(lblTenDN,gbc);
        
        // Tên DN Field
        txtTenDN = new JTextField(20);
        txtTenDN.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx= 1; gbc.gridy= 1; gbc.anchor = GridBagConstraints.LINE_START;
        pnlForm.add(txtTenDN,gbc);
        
        // Mật khẩu Label
        lblMatKhau = new JLabel("Mật khẩu");
        lblMatKhau.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblMatKhau.setForeground(new Color(66, 66, 66));
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx= 0; gbc.gridy= 2;
        pnlForm.add(lblMatKhau,gbc);
        
        // Mật khẩu Field
        txtMatKhau = new JPasswordField(20);
        txtMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx= 1; gbc.gridy= 2;
        pnlForm.add(txtMatKhau,gbc);

        // Nút bấm Panel
        pnlNutBam = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Nút Thoát
        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThoat.setBackground(COLOR_EXIT_BUTTON);
        btnThoat.setForeground(Color.WHITE);
        ImageIcon iconThoat = GiaoDienChinh.chinhKichThuoc("/img/thoaticon.png", 25, 25);
        btnThoat.setIcon(iconThoat);
        pnlNutBam.add(btnThoat);

        // Nút Đăng nhập
        btnDn = new JButton("Đăng nhập");
        btnDn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDn.setBackground(COLOR_LOGIN_BUTTON);
        btnDn.setForeground(Color.WHITE);
        ImageIcon iconDN = GiaoDienChinh.chinhKichThuoc("/img/loginicon.png", 25, 25);
        btnDn.setIcon(iconDN);
        pnlNutBam.add(btnDn);

        // Thêm các panel con vào panel chính
        gbc.gridx= 1; gbc.gridy= 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.CENTER;
        pnlForm.add(pnlNutBam,gbc);
        pnlPhai.add(pnlForm);
        pnlChinh.add(pnlTrai);
        pnlChinh.add(pnlPhai);
        add(pnlChinh);
    }
    
    // Phương thức attach listener (SỬA: Giữ nguyên signature, nhưng gọi đúng cách ở constructor)
    private void attachListeners(ActionListener actionListener, MouseListener mouseListener) {
        txtTenDN.addActionListener(actionListener);
        txtMatKhau.addActionListener(actionListener);
        btnDn.addActionListener(actionListener);
        btnThoat.addActionListener(actionListener);
        
        btnDn.addMouseListener(mouseListener);
        btnThoat.addMouseListener(mouseListener);
    }
    
    // Getter để Controller truy cập UI components (giữ nguyên)
    public JTextField getTxtTenDN() { return txtTenDN; }
    public JPasswordField getTxtMatKhau() { return txtMatKhau; }
    public JButton getBtnDn() { return btnDn; }
    public JButton getBtnThoat() { return btnThoat; }
    
    public static void main(String[] args) throws IOException {
        DangNhapGUI dn = new DangNhapGUI();
        dn.setVisible(true);
    }
}