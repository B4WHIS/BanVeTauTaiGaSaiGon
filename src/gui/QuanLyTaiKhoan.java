package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import control.QuanLyTaiKhoanController;
import dao.TaiKhoanDAO;
import dao.NhanVienDAO;
import entity.TaiKhoan;
import entity.NhanVien;

public class QuanLyTaiKhoan extends GiaoDienChinh {
    // Components
    private JTable tblTaiKhoan;
    private DefaultTableModel modelTK;
    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cbNhanVien;  // Hiển thị hoTen từ DB
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTroVe;
    private JButton btnLuu;  // Nút Lưu (cho add/update)

    // Controller và DAO
    private QuanLyTaiKhoanController controller;
    private TaiKhoanDAO dao;
    private NhanVienDAO nhanVienDAO;

    public QuanLyTaiKhoan() {
        super();  // Gọi constructor cha
        dao = new TaiKhoanDAO();
        nhanVienDAO = new NhanVienDAO();
        controller = new QuanLyTaiKhoanController(this);
        setTitle("Quản lý tài khoản");
        setSize(1200, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setJMenuBar(taoMenuBar());

        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("QUẢN LÝ TÀI KHOẢN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103,192,144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // ===== LEFT PANEL =====
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(450, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        JLabel lblLeftTitle = new JLabel("THÔNG TIN TÀI KHOẢN", SwingConstants.CENTER);
        lblLeftTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLeftTitle.setForeground(new Color(103,192,144));
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

        JLabel lblTenDangNhap = new JLabel("Tên đăng nhập:");
        JLabel lblMatKhau = new JLabel("Mật khẩu:");
        JLabel lblNhanVien = new JLabel("Nhân viên:");

        JLabel[] labels = {lblTenDangNhap, lblMatKhau, lblNhanVien};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        txtTenDangNhap = new JTextField();  // Read-only, tự động từ soDienThoai NV
        txtTenDangNhap.setEditable(false);
        txtMatKhau = new JPasswordField();
        cbNhanVien = new JComboBox<>();  // Hiển thị hoTen

        Component[] inputFields = {txtTenDangNhap, txtMatKhau, cbNhanVien};
        for (Component comp : inputFields) {
            if (comp instanceof JTextField || comp instanceof JPasswordField) {
                ((JTextField) comp).setFont(txtFont);
                ((JTextField) comp).setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(150, 150, 150)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
                ((JTextField) comp).setPreferredSize(new Dimension(200, 30));
            } else if (comp instanceof JComboBox) {
                JComboBox<?> cb = (JComboBox<?>) comp;
                cb.setFont(txtFont);
                cb.setPreferredSize(new Dimension(200, 30));
            }
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            pnlForm.add(labels[i], gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            pnlForm.add(inputFields[i], gbc);
        }

        // Listener cho cbNhanVien: Tự động set txtTenDangNhap từ soDienThoai của NV
        cbNhanVien.addActionListener(e -> {
            String hoTen = (String) cbNhanVien.getSelectedItem();
            if (hoTen != null && !hoTen.isEmpty()) {
                NhanVien nv = nhanVienDAO.getNhanVienByTen(hoTen);
                if (nv != null) {
                    txtTenDangNhap.setText(nv.getSoDienThoai());  // Dùng getSoDienThoai từ entity (validated)
                } else {
                    txtTenDangNhap.setText("");
                }
            } else {
                txtTenDangNhap.setText("");
            }
        });

        // ===== MAIN BUTTONS =====
        btnThem = taoButton("Thêm", new Color(46, 204, 113), "/img/plus.png");
        btnSua = taoButton("Sửa", new Color(241, 196, 15), "/img/maintenance.png");
        btnXoa = taoButton("Xóa", new Color(231, 76, 60), "/img/bin.png");
        btnReset = taoButton("Làm mới", new Color(52, 152, 219), "/img/reset.png");
        btnExport = taoButton("Xuất Excel", new Color(155, 89, 182), "/img/export.png");
        btnLuu = new JButton("Lưu");
        btnLuu.setBackground(new Color(46, 204, 113));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLuu.setPreferredSize(new Dimension(100, 35));
        btnLuu.setEnabled(false);  // Ban đầu disable

        JPanel pnlButtons = new JPanel(new GridLayout(2, 3, 10, 10));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnReset);
        pnlButtons.add(btnLuu);
        pnlButtons.add(btnExport);

        pnlLeft.add(lblLeftTitle, BorderLayout.NORTH);
        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        // ===== RIGHT PANEL =====
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(new Color(245, 247, 250));

        // Header: Tên đăng nhập, Mật khẩu (ẩn), Tên nhân viên
        String[] colHeader = {"Tên đăng nhập", "Mật khẩu", "Tên nhân viên"};
        modelTK = new DefaultTableModel(colHeader, 0);
        tblTaiKhoan = new JTable(modelTK);
        tblTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblTaiKhoan.setRowHeight(28);
        tblTaiKhoan.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblTaiKhoan.setSelectionBackground(new Color(173, 216, 230));

        JScrollPane scroll = new JScrollPane(tblTaiKhoan);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnlRight.add(scroll, BorderLayout.CENTER);

        // ===== FOOTER =====
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(new Color(103,192,144)); 
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "/img/loginicon.png");
        btnTroVe.setPreferredSize(new Dimension(130, 45));
        pnlFooter.add(btnTroVe, BorderLayout.WEST);

        // ===== ADD ALL =====
        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(pnlRight, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);
        add(pnlMain);

        // Attach listeners
        attachListeners();
        
        // Load combos và data từ DB
        loadCombosFromDB();
        loadDataFromDB();
    }

    // Load combo boxes từ DB - Hiển thị hoTen
    private void loadCombosFromDB() {
        cbNhanVien.removeAllItems();
        List<String> tenNhanVienList = nhanVienDAO.getAllTenNhanVien();
        for (String tenNV : tenNhanVienList) {
            cbNhanVien.addItem(tenNV);
        }
        if (!tenNhanVienList.isEmpty()) cbNhanVien.setSelectedIndex(0);
    }

    // Load data từ DB - Hiển thị hoTen, mật khẩu ẩn
    private void loadDataFromDB() {
        modelTK.setRowCount(0);
        try {
            List<TaiKhoan> list = dao.getAllTaiKhoan();
            for (TaiKhoan tk : list) {
                String tenNV = nhanVienDAO.getTenNhanVienByMa(tk.getNhanVien().getMaNhanVien());
                String matKhauAn = "*".repeat(tk.getMatKhau().length());  // Ẩn mật khẩu
                modelTK.addRow(new Object[]{
                    tk.getTenDangNhap(),
                    matKhauAn,
                    tenNV != null ? tenNV : "Không xác định"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi load dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Attach listeners to controller
    private void attachListeners() {
        btnThem.addActionListener(controller);
        btnSua.addActionListener(controller);
        btnXoa.addActionListener(controller);
        btnReset.addActionListener(controller);
        btnExport.addActionListener(controller);
        btnTroVe.addActionListener(controller);
        btnLuu.addActionListener(controller);  // Listener cho nút Lưu

        // Thêm listener cho table selection
        tblTaiKhoan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                controller.handleTableSelection();
            }
        });
    }

    // ===== GETTERS CHO CONTROLLER =====
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnReset() { return btnReset; }
    public JButton getBtnExport() { return btnExport; }
    public JButton getBtnTroVe() { return btnTroVe; }
    public JButton getBtnLuu() { return btnLuu; }
    public JTable getTblTaiKhoan() { return tblTaiKhoan; }
    public DefaultTableModel getModelTK() { return modelTK; }
    public JTextField getTxtTenDangNhap() { return txtTenDangNhap; }
    public JPasswordField getTxtMatKhau() { return txtMatKhau; }
    public JComboBox<String> getCbNhanVien() { return cbNhanVien; }

    // Method để load data vào form
    public void loadFormData(TaiKhoan tk) {
        if (tk != null) {
            txtTenDangNhap.setText(tk.getTenDangNhap());
            txtMatKhau.setText(tk.getMatKhau().toString());  // Load rõ cho edit
            String tenNV = nhanVienDAO.getTenNhanVienByMa(tk.getNhanVien().getMaNhanVien());
            if (tenNV != null) cbNhanVien.setSelectedItem(tenNV);
        }
    }

    // Method để reset form
    public void resetForm() {
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        cbNhanVien.setSelectedIndex(0);
        btnLuu.setEnabled(false);
        tblTaiKhoan.clearSelection();
        enableFormFields(false);
    }

    // Method để enable/disable form fields
    public void enableFormFields(boolean enable) {
        cbNhanVien.setEnabled(enable);
        txtMatKhau.setEnabled(enable);
    }

    // Method để refresh data
    public void refreshData() {
        loadDataFromDB();
        resetForm();
    }

    // Method để lấy selected tenDangNhap từ table
    public String getSelectedTenDangNhap() {
        int selectedRow = tblTaiKhoan.getSelectedRow();
        if (selectedRow >= 0) {
            return (String) modelTK.getValueAt(selectedRow, 0);  // Cột 0: Tên đăng nhập
        }
        return null;
    }

    // Method để show message
    public void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    // Main method để chạy ứng dụng
    public static void main(String[] args) {
        LookAndFeelManager.setNimbusLookAndFeel();
        new QuanLyTaiKhoan().setVisible(true);
    }
}