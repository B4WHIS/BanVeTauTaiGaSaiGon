package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class DanhSachChuyenTau extends JFrame implements ActionListener {
    private JTable tblChuyenTau;
    private DefaultTableModel modelCT;
    private JTextField txtMaChuyen, txtTenTau, txtGaDi, txtGaDen, txtGioKhoiHanh, txtGiaVe;
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTroVe;

    public DanhSachChuyenTau() {
        setTitle("Quản lý chuyến tàu");
        setSize(1500, 1000);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== MENU BAR =====
        setJMenuBar(taoMenuBar());

        // ===== MAIN PANEL =====
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));
        pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("QUẢN LÝ CHUYẾN TÀU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(41, 128, 185));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // ===== LEFT PANEL =====
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(450, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        JLabel lblLeftTitle = new JLabel("THÔNG TIN CHUYẾN TÀU", SwingConstants.CENTER);
        lblLeftTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLeftTitle.setForeground(new Color(41, 128, 185));
        lblLeftTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // ===== FORM =====
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setBackground(Color.WHITE);

        Font lblFont = new Font("Segoe UI", Font.BOLD, 19);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 20);

        JLabel lblMaChuyen = new JLabel("Mã chuyến:");
        JLabel lblTenTau = new JLabel("Tên tàu:");
        JLabel lblGaDi = new JLabel("Ga đi:");
        JLabel lblGaDen = new JLabel("Ga đến:");
        JLabel lblGioKhoiHanh = new JLabel("Giờ khởi hành:");
        JLabel lblGiaVe = new JLabel("Giá vé:");

        JLabel[] labels = {lblMaChuyen, lblTenTau, lblGaDi, lblGaDen, lblGioKhoiHanh, lblGiaVe};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        txtMaChuyen = new JTextField();
        txtTenTau = new JTextField();
        txtGaDi = new JTextField();
        txtGaDen = new JTextField();
        txtGioKhoiHanh = new JTextField("hh:mm dd/MM/yyyy");
        txtGiaVe = new JTextField();

        JTextField[] textFields = {txtMaChuyen, txtTenTau, txtGaDi, txtGaDen, txtGioKhoiHanh, txtGiaVe};
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

        // ===== BUTTONS =====
        btnThem = taoButton("Thêm", new Color(46, 204, 113), "/img/add.png");
        btnSua = taoButton("Sửa", new Color(241, 196, 15), "/img/edit.png");
        btnXoa = taoButton("Xóa", new Color(231, 76, 60), "/img/delete.png");
        btnReset = taoButton("Làm mới", new Color(52, 152, 219), "/img/reset.png");
        btnExport = taoButton("Xuất Excel", new Color(155, 89, 182), "/img/export.png");

        JPanel pnlButtons = new JPanel(new GridLayout(5, 1, 10, 10));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnReset);
        pnlButtons.add(btnExport);

        pnlLeft.add(lblLeftTitle, BorderLayout.NORTH);
        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        // ===== RIGHT PANEL =====
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(new Color(245, 247, 250));

        String[] colHeader = {"Mã chuyến", "Tên tàu", "Ga đi", "Ga đến", "Giờ khởi hành", "Giá vé"};
        modelCT = new DefaultTableModel(colHeader, 0);
        tblChuyenTau = new JTable(modelCT);
        tblChuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblChuyenTau.setRowHeight(28);
        tblChuyenTau.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblChuyenTau.setSelectionBackground(new Color(173, 216, 230));

        JScrollPane scroll = new JScrollPane(tblChuyenTau);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnlRight.add(scroll, BorderLayout.CENTER);

        // ===== FOOTER =====
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(new Color(149, 214, 179));
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        btnTroVe = taoButton("Trở về", new Color(52, 152, 219), "/img/loginicon.png");
        btnTroVe.setPreferredSize(new Dimension(100, 45));
        pnlFooter.add(btnTroVe, BorderLayout.WEST);

        // ===== GẮN TẤT CẢ =====
        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(pnlRight, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);
        add(pnlMain);
    }

    private JMenuBar taoMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        menuBar.setBackground(new Color(220, 220, 220));

        JMenu mnuHeThong = new JMenu("Hệ Thống");
        JMenu mnuNghiepVu = new JMenu("Nghiệp Vụ Vé");
        JMenu mnuQuanLy = new JMenu("Quản Lý");
        JMenu mnuTraCuu = new JMenu("Tra Cứu");
        JMenu mnuThongKe = new JMenu("Thống Kê");
        JMenu mnuTroGiup = new JMenu("Trợ Giúp");

        mnuHeThong.setIcon(chinhKichThuoc("/img/heThong3.png", 30, 30));
        mnuNghiepVu.setIcon(chinhKichThuoc("/img/ve2.png", 30, 30));
        mnuQuanLy.setIcon(chinhKichThuoc("/img/quanLy.png", 30, 30));
        mnuTraCuu.setIcon(chinhKichThuoc("/img/traCuu.png", 30, 30));
        mnuThongKe.setIcon(chinhKichThuoc("/img/thongKe.png", 30, 30));
        mnuTroGiup.setIcon(chinhKichThuoc("/img/troGiup.png", 30, 30));

        mnuHeThong.add(new JMenuItem("Đăng xuất"));
        mnuHeThong.add(new JMenuItem("Thoát"));
        mnuQuanLy.add(new JMenuItem("Quản lý nhân viên"));
        mnuQuanLy.add(new JMenuItem("Quản lý hành khách"));
        mnuQuanLy.add(new JMenuItem("Quản lý chuyến tàu"));
        mnuQuanLy.add(new JMenuItem("Quản lý khuyến mãi"));

        menuBar.add(mnuHeThong);
        menuBar.add(mnuNghiepVu);
        menuBar.add(mnuQuanLy);
        menuBar.add(mnuTraCuu);
        menuBar.add(mnuThongKe);
        menuBar.add(mnuTroGiup);
        menuBar.add(Box.createHorizontalGlue());

        JLabel lblXinChao = new JLabel("Xin Chào, [Tên Nhân Viên] ");
        lblXinChao.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel lblAvatar = new JLabel(chinhKichThuoc("/img/nhanVienBanVe.png", 50, 50));
        menuBar.add(lblXinChao);
        menuBar.add(lblAvatar);

        return menuBar;
    }

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
        URL iconUrl = DanhSachChuyenTau.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    @Override
    public void actionPerformed(ActionEvent e) { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DanhSachChuyenTau().setVisible(true));
    }
}
