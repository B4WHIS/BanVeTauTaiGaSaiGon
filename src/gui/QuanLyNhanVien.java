package gui;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import entity.NhanVien;

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

        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103, 192, 144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // nhâp thông tin
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
        JLabel lblDienThoai = new JLabel("Số điện thoại:");
        JLabel lblChucVu = new JLabel("Chức vụ:");
        JLabel[] labels = {lblHoTen, lblNgaySinh, lblCMND, lblDienThoai, lblChucVu};

        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        txtHoTen = new JTextField();
        txtNgaySinh = new JTextField("dd/MM/yyyy");
        txtCMND = new JTextField();
        txtDienThoai = new JTextField();
        cbChucVu = new JComboBox<>(new String[]{"Nhân viên bán vé", "Quản lý"});
        cbChucVu.setFont(txtFont);
        cbChucVu.setBackground(Color.WHITE);

        JTextField[] txts = {txtHoTen, txtNgaySinh, txtCMND, txtDienThoai};
        for (JTextField txt : txts) {
            txt.setFont(txtFont);
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; pnlForm.add(lblHoTen, gbc);
        gbc.gridx = 1; pnlForm.add(txtHoTen, gbc);
        gbc.gridx = 0; gbc.gridy = 1; pnlForm.add(lblNgaySinh, gbc);
        gbc.gridx = 1; pnlForm.add(txtNgaySinh, gbc);
        gbc.gridx = 0; gbc.gridy = 2; pnlForm.add(lblCMND, gbc);
        gbc.gridx = 1; pnlForm.add(txtCMND, gbc);
        gbc.gridx = 0; gbc.gridy = 3; pnlForm.add(lblDienThoai, gbc);
        gbc.gridx = 1; pnlForm.add(txtDienThoai, gbc);
        gbc.gridx = 0; gbc.gridy = 4; pnlForm.add(lblChucVu, gbc);
        gbc.gridx = 1; pnlForm.add(cbChucVu, gbc);

        // buttton
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

        pnlLeft.add(lblLeftTitle, BorderLayout.NORTH);
        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        // table
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

        // footer
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

        // action
        btnTroVe.addActionListener(this);
        btnExport.addActionListener(this); // ✅ Sự kiện xuất Excel
        new control.QuanLyNhanVienControl(this);
    }

    // hàm tạo buuton
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

//gắn sự kiện
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        // Nút trở về
        if (src == btnTroVe) {
            this.dispose();
            new MHC_NhanVienQuanLy(nhanVienHienTai).setVisible(true);
        }

        //xuất ex
        else if (src == btnExport) {
            if (tblNhanVien.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ExcelExporter.exportToExcel(tblNhanVien, this);
        }
    }

    public static void main(String[] args) {
        LookAndFeelManager.setNimbusLookAndFeel();
        SwingUtilities.invokeLater(() -> new QuanLyNhanVien().setVisible(true));
    }

    public JTable getTblNhanVien() { return tblNhanVien; }
    public DefaultTableModel getModelNV() { return modelNV; }
    public JTextField getTxtHoTen() { return txtHoTen; }
    public JTextField getTxtNgaySinh() { return txtNgaySinh; }
    public JTextField getTxtCMND() { return txtCMND; }
    public JTextField getTxtDienThoai() { return txtDienThoai; }
    public JComboBox<String> getCbChucVu() { return cbChucVu; }
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnReset() { return btnReset; }
    public JButton getBtnExport() { return btnExport; }
    public JButton getBtnTroVe() { return btnTroVe; }
}
