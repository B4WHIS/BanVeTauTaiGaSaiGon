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
import javax.swing.JComboBox;
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

//check
public class QuanLyNhanVien extends JFrame implements ActionListener {
    private JTable tblNhanVien;
    private DefaultTableModel modelNV;
    private JTextField txtHoTen, txtNgaySinh, txtCMND, txtDienThoai; 
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTroVe;
    private JComboBox<String> cbChucVu;
    private NhanVien nhanVienHienTai; 
    public QuanLyNhanVien() {
        setTitle("Quản lý nhân viên");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

//        setJMenuBar(taoMenuBar());

        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // TITLE
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103, 192, 144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

       
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(450, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        JLabel lblLeftTitle = new JLabel("THÔNG TIN NHÂN VIÊN", SwingConstants.CENTER);
        lblLeftTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLeftTitle.setForeground(new Color(103, 192, 144));
        lblLeftTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

       
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setBackground(Color.WHITE);

        Font lblFont = new Font("Segoe UI", Font.BOLD, 19);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 20);

        JLabel lblHoTen = new JLabel("Họ tên:");
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        JLabel lblCMND = new JLabel("CMND/CCCD:");
        JLabel lblDienThoai = new JLabel("Số điện thoại:"); // THÊM
        JLabel lblChucVu = new JLabel("Chức vụ:");

        JLabel[] labels = {lblHoTen, lblNgaySinh, lblCMND, lblDienThoai, lblChucVu};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        txtHoTen = new JTextField();
        txtNgaySinh = new JTextField("dd/MM/yyyy");
        txtCMND = new JTextField();
        txtDienThoai = new JTextField(); // THÊM

        cbChucVu = new JComboBox<>(new String[]{"Nhân viên bán vé", "Quản lý"});
        cbChucVu.setFont(txtFont);
        cbChucVu.setBackground(Color.WHITE);
        cbChucVu.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));

        JTextField[] textFields = {txtHoTen, txtNgaySinh, txtCMND, txtDienThoai}; // THÊM
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

        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        pnlForm.add(lblHoTen, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(txtHoTen, gbc);

       
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        pnlForm.add(lblNgaySinh, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(txtNgaySinh, gbc);

       
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        pnlForm.add(lblCMND, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(txtCMND, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        pnlForm.add(lblDienThoai, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(txtDienThoai, gbc);

        
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        pnlForm.add(lblChucVu, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(cbChucVu, gbc);

        btnThem = taoButton("Thêm", new Color(46, 204, 113), "/img/plus.png");
        btnSua = taoButton("Sửa", new Color(241, 196, 15), "/img/maintenance.png");
        btnXoa = taoButton("Xóa", new Color(231, 76, 60), "/img/bin.png");
        btnReset = taoButton("Làm mới", new Color(52, 152, 219), "/img/reset.png");
        btnExport = taoButton("Xuất Excel", new Color(155, 89, 182), "/img/export.png");

        JPanel pnlButtons = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.add(btnThem); pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa); pnlButtons.add(btnReset);
        pnlButtons.add(btnExport);

        pnlLeft.add(lblLeftTitle, BorderLayout.NORTH);
        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(new Color(245, 247, 250));

       
        String[] colHeader = {"Mã NV", "Họ tên", "Ngày sinh", "CMND/CCCD", "Số ĐT", "Chức vụ"};
        modelNV = new DefaultTableModel(colHeader, 0);
        tblNhanVien = new JTable(modelNV);
        tblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblNhanVien.setRowHeight(28);
        tblNhanVien.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblNhanVien.setSelectionBackground(new Color(173, 216, 230));

        JScrollPane scroll = new JScrollPane(tblNhanVien);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnlRight.add(scroll, BorderLayout.CENTER);

        
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(new Color(103, 192, 144));
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "/img/loginicon.png");
        btnTroVe.setPreferredSize(new Dimension(130, 45));
        
        pnlFooter.add(btnTroVe, BorderLayout.WEST);

        
        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(pnlRight, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);
        add(pnlMain);

        btnTroVe.addActionListener(this);
        new control.QuanLyNhanVienControl(this);
    }

    
    public JTable getTblNhanVien() { return tblNhanVien; }
    public DefaultTableModel getModelNV() { return modelNV; }
    public JTextField getTxtHoTen() { return txtHoTen; }
    public JTextField getTxtNgaySinh() { return txtNgaySinh; }
    public JTextField getTxtCMND() { return txtCMND; }
    public JTextField getTxtDienThoai() { return txtDienThoai; } // THÊM
    public JComboBox<String> getCbChucVu() { return cbChucVu; }
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnReset() { return btnReset; }
    public JButton getBtnExport() { return btnExport; }
    public JButton getBtnTroVe() { return btnTroVe; }

    public static JButton taoButton(String text, Color bg, String iconPath) {
        ImageIcon icon = chinhKichThuoc(iconPath, 24, 24);
        JButton btn = new JButton(text, icon != null ? icon : null);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
        URL iconUrl = QuanLyNhanVien.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
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
//        mnuQly.add(new JMenuItem("Quản lý khuyến mại"));
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

    public static void main(String[] args) {
        LookAndFeelManager.setNimbusLookAndFeel();
        SwingUtilities.invokeLater(() -> new QuanLyNhanVien().setVisible(true));
    }


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object src = e.getSource();
        if (src == btnTroVe) { 
            this.dispose();
            new NhanVienQuanLyGUI(nhanVienHienTai).setVisible(true);
        }
	}
}