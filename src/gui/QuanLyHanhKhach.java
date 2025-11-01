package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import entity.NhanVien;

public class QuanLyHanhKhach extends JFrame implements ActionListener {
    private JTable tblHanhKhach;
    private DefaultTableModel modelHK;
    private JTextField txtMaHK, txtHoTen, txtNgaySinh, txtCMND, txtSoDT;
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTroVe;
    private NhanVien nhanVienHienTai; 
    public QuanLyHanhKhach() {
        setTitle("Quản lý hành khách");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

//        setJMenuBar(taoMenuBar());

      
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

       
        JLabel lblTitle = new JLabel("QUẢN LÝ HÀNH KHÁCH", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103,192,144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(450, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        JLabel lblLeftTitle_mn = new JLabel("THÔNG TIN HÀNH KHÁCH", SwingConstants.CENTER);
        lblLeftTitle_mn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLeftTitle_mn.setForeground(new Color(103,192,144));
        lblLeftTitle_mn.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setBackground(Color.WHITE);

        Font lblFont = new Font("Segoe UI", Font.BOLD, 19);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 20);

        JLabel lblMaHK = new JLabel("Mã HK:");
        JLabel lblHoTen = new JLabel("Họ tên:");
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        JLabel lblCMND = new JLabel("CMND/CCCD:");
        JLabel lblSoDT = new JLabel("Số điện thoại:");

        JLabel[] labels = {lblMaHK, lblHoTen, lblNgaySinh, lblCMND, lblSoDT};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        txtMaHK = new JTextField();
        txtHoTen = new JTextField();
        txtNgaySinh = new JTextField("dd/MM/yyyy");
        txtCMND = new JTextField();
        txtSoDT = new JTextField();

        JTextField[] textFields = {txtMaHK, txtHoTen, txtNgaySinh, txtCMND, txtSoDT};
        for (JTextField txt : textFields) {
            txt.setFont(txtFont);
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            pnlForm.add(labels[i], gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            pnlForm.add(textFields[i], gbc);
        }

        
        btnThem = taoButton("Thêm", new Color(46, 204, 113), "/img/plus.png");
        btnSua = taoButton("Sửa", new Color(241, 196, 15), "/img/maintenance.png");
        btnXoa = taoButton("Xóa", new Color(231, 76, 60), "/img/bin.png");
        btnReset = taoButton("Làm mới", new Color(52, 152, 219), "/img/reset.png");
        btnExport = taoButton("Xuất Excel", new Color(155, 89, 182), "/img/export.png");

        JPanel pnlButtons = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnReset);
        pnlButtons.add(btnExport);

        pnlLeft.add(lblLeftTitle_mn, BorderLayout.NORTH);
        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        // ===== RIGHT PANEL =====
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(new Color(245, 247, 250));

        String[] colHeader = {"Mã HK", "Họ tên", "Ngày sinh", "CMND/CCCD", "Số điện thoại"};
        modelHK = new DefaultTableModel(colHeader, 0);
        tblHanhKhach = new JTable(modelHK);
        tblHanhKhach.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblHanhKhach.setRowHeight(28);
        tblHanhKhach.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblHanhKhach.setSelectionBackground(new Color(173, 216, 230));

        JScrollPane scroll = new JScrollPane(tblHanhKhach);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnlRight.add(scroll, BorderLayout.CENTER);

        
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(new Color(103,192,144)); 
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "/img/loginicon.png");
        btnTroVe.setPreferredSize(new Dimension(130, 45));
        pnlFooter.add(btnTroVe, BorderLayout.WEST);

        btnTroVe.addActionListener(this);	
        
        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(pnlRight, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);
        add(pnlMain);
    }

//    private JMenuBar taoMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//        menuBar.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
//        menuBar.setBackground(new Color(220, 220, 220));
//
//        JMenu mnuHeThong = new JMenu("Hệ Thống");
//        JMenu mnuNghiepVuVe = new JMenu("Nghiệp Vụ Vé");
//        JMenu mnuQly = new JMenu("Quản Lý");
//        JMenu mnuTraCuu = new JMenu("Tra Cứu");
//        JMenu mnuThongKe = new JMenu("Thống Kê");
//        JMenu mnuTroGiup = new JMenu("Trợ Giúp");
//
//        mnuHeThong.setIcon(chinhKichThuoc("/img/heThong3.png", 30, 30));
//        mnuNghiepVuVe.setIcon(chinhKichThuoc("/img/ve2.png", 30, 30));
//        mnuQly.setIcon(chinhKichThuoc("/img/quanLy.png", 30, 30));
//        mnuTraCuu.setIcon(chinhKichThuoc("/img/traCuu.png", 30, 30));
//        mnuThongKe.setIcon(chinhKichThuoc("/img/thongKe.png", 30, 30));
//        mnuTroGiup.setIcon(chinhKichThuoc("/img/troGiup.png", 30, 30));
//
//        mnuHeThong.add(new JMenuItem("Đăng xuất"));
//        mnuHeThong.add(new JMenuItem("Thoát"));
//        mnuQly.add(new JMenuItem("Quản lý nhân viên"));
//        mnuQly.add(new JMenuItem("Quản lý hành khách"));
//        mnuQly.add(new JMenuItem("Quản lý chuyến tàu"));
//        mnuQly.add(new JMenuItem("Quản lý khuyến mãi"));
//
//        menuBar.add(mnuHeThong);
//        menuBar.add(mnuNghiepVuVe);
//        menuBar.add(mnuQly);
//        menuBar.add(mnuTraCuu);
//        menuBar.add(mnuThongKe);
//        menuBar.add(mnuTroGiup);
//        menuBar.add(Box.createHorizontalGlue());
//
//        JLabel lblXinChao = new JLabel("Xin Chào, [Tên Nhân Viên] ");
//        lblXinChao.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        JLabel lblNVBV = new JLabel(chinhKichThuoc("/img/nhanVienBanVe.png", 50, 50));
//        menuBar.add(lblXinChao);
//        menuBar.add(lblNVBV);
//
//        return menuBar;
//    }

    private JButton taoButton(String text, Color bg, String iconPath) {
        JButton btn = new JButton(text, chinhKichThuoc(iconPath, 24, 24));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
        URL iconUrl = QuanLyHanhKhach.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object src = e.getSource();
        if (src == btnTroVe) { 
            this.dispose();
            new MHC_NhanVienQuanLy(nhanVienHienTai).setVisible(true);
        }
	}

    public static void main(String[] args) {
    	LookAndFeelManager.setNimbusLookAndFeel();
        SwingUtilities.invokeLater(() -> new QuanLyHanhKhach().setVisible(true));
    }
    
    
}
