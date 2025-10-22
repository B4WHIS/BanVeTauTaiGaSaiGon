package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import control.QuanLyChuyenTauController;
import dao.ChuyenTauDAO;
import dao.LichTrinhDAO;
import dao.TauDAO;
import entity.ChuyenTau;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DanhSachChuyenTau extends GiaoDienChinh {
    // Components
    private JTable tblChuyenTau;
    private DefaultTableModel modelCT;
    private JTextField txtMaChuyenTau, txtThoiGianKhoiHanh, txtThoiGianDen, txtGiaChuyen;
    private JComboBox<String> cbTenTau, cbTenLichTrinh, cbTrangThai;  // Đổi tên để rõ ràng: hiển thị tên
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTroVe;
    private JButton btnLuu;  // Nút Lưu (cho add/update)

    // Date formatter for display
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Controller
    private QuanLyChuyenTauController controller;
    private ChuyenTauDAO dao;  // DAO để load data
    private LichTrinhDAO lichTrinhDAO;
    private TauDAO tauDAO;

    public DanhSachChuyenTau() {
        super();  // Gọi constructor cha
        dao = new ChuyenTauDAO();
        lichTrinhDAO= new LichTrinhDAO();
        tauDAO = new TauDAO();
        controller = new QuanLyChuyenTauController(this);
        setTitle("Quản lý chuyến tàu");
        setSize(1500, 1000);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setJMenuBar(taoMenuBar());

        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("QUẢN LÝ CHUYẾN TÀU", SwingConstants.CENTER);
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

        JLabel lblLeftTitle = new JLabel("THÔNG TIN CHUYẾN TÀU", SwingConstants.CENTER);
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

        JLabel lblMaChuyenTau = new JLabel("Mã chuyến:");
        JLabel lblTenTau = new JLabel("Tên tàu:");
        JLabel lblTenLichTrinh = new JLabel("Lịch trình:");
        JLabel lblThoiGianKhoiHanh = new JLabel("Thời gian KH:");
        JLabel lblThoiGianDen = new JLabel("Thời gian Đến:");
        JLabel lblGiaChuyen = new JLabel("Giá chuyến:");
        JLabel lblTrangThai = new JLabel("Trạng thái:");

        JLabel[] labels = {lblMaChuyenTau, lblTenTau, lblTenLichTrinh, lblThoiGianKhoiHanh, lblThoiGianDen, lblGiaChuyen, lblTrangThai};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        txtMaChuyenTau = new JTextField();  // Read-only, computed by DB
        txtMaChuyenTau.setEditable(false);
        cbTenTau = new JComboBox<>();  // Hiển thị tên tàu
        cbTenLichTrinh = new JComboBox<>();  // Hiển thị tên lịch trình
        txtThoiGianKhoiHanh = new JTextField("yyyy-MM-dd HH:mm");
        txtThoiGianDen = new JTextField("yyyy-MM-dd HH:mm");
        txtGiaChuyen = new JTextField();
        cbTrangThai = new JComboBox<>(new String[]{
                "Chưa khởi hành", "Đang khởi hành", "Đã hoàn thành", "Đã hủy"
        });
        cbTrangThai.setSelectedIndex(0);  // Mặc định "Chưa khởi hành"
        
        Component[] inputFields = {txtMaChuyenTau, cbTenTau, cbTenLichTrinh, txtThoiGianKhoiHanh, txtThoiGianDen, txtGiaChuyen, cbTrangThai};
        for (Component comp : inputFields) {
            if (comp instanceof JTextField) {
                JTextField txt = (JTextField) comp;
                txt.setFont(txtFont);
                txt.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(150, 150, 150)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
                if (txt == txtThoiGianKhoiHanh || txt == txtThoiGianDen) {
                    txt.setEditable(true);  // Cho phép nhập thời gian
                }
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

        // ===== FORM BUTTONS =====
       
        btnLuu = new JButton("Lưu");
        btnLuu.setBackground(new Color(46, 204, 113));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLuu.setPreferredSize(new Dimension(100, 35));
        btnLuu.setEnabled(false);  // Ban đầu disable
   

        // ===== MAIN BUTTONS =====
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
        pnlButtons.add(btnLuu);
        pnlButtons.add(btnExport);
        pnlButtons.add(new JLabel(" "));  // Placeholder để cân bằng

        pnlLeft.add(lblLeftTitle, BorderLayout.NORTH);
        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        // ===== RIGHT PANEL =====
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(new Color(245, 247, 250));

        // Cập nhật header: Hiển thị tên tàu và tên lịch trình
        String[] colHeader = {"Mã chuyến", "Thời gian KH", "Thời gian Đến", "Tên tàu", "Tên lịch trình", "Giá chuyến", "Trạng thái"};
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

    // Load combo boxes từ DB - Hiển thị tên
    private void loadCombosFromDB() {
        // Load cbTenTau với tên tàu
        cbTenTau.removeAllItems();
        List<String> tenTauList = dao.getAllTenTau();
        for (String tenTau : tenTauList) {
            cbTenTau.addItem(tenTau);
        }
        if (!tenTauList.isEmpty()) cbTenTau.setSelectedIndex(0);

        // Load cbTenLichTrinh với tên lịch trình
        cbTenLichTrinh.removeAllItems();
        List<String> tenLichTrinhList = dao.getAllTenLichTrinh();
        for (String tenLichTrinh : tenLichTrinhList) {
            cbTenLichTrinh.addItem(tenLichTrinh);
        }
        if (!tenLichTrinhList.isEmpty()) cbTenLichTrinh.setSelectedIndex(0);
    }

    // Load data từ DB - Hiển thị tên tàu và tên lịch trình trong table
    private void loadDataFromDB() {
        modelCT.setRowCount(0);
        try {
            List<ChuyenTau> list = dao.getAllChuyenTau();
            for (ChuyenTau ct : list) {
                // Lấy tên từ mã
                String tenTau = tauDAO.getTenTauByMaTau(ct.getMaTau());
                String tenLichTrinh = lichTrinhDAO.getTenLichTrinhByMaLichTrinh(ct.getMaLichTrinh());
                modelCT.addRow(new Object[]{
                    ct.getMaChuyenTau(),
                    ct.getThoiGianKhoiHanh().format(dateTimeFormatter),
                    ct.getThoiGianDen().format(dateTimeFormatter),
                    tenTau != null ? tenTau : ct.getMaTau(),  // Fallback to mã nếu không lấy được tên
                    tenLichTrinh != null ? tenLichTrinh : ct.getMaLichTrinh(),
                    ct.getGiaChuyen(),
                    ct.getTrangThai()
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
        tblChuyenTau.getSelectionModel().addListSelectionListener(e -> {
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
    public JTable getTblChuyenTau() { return tblChuyenTau; }
    public DefaultTableModel getModelCT() { return modelCT; }
    public JTextField getTxtMaChuyenTau() { return txtMaChuyenTau; }
    public JComboBox<String> getCbTenTau() { return cbTenTau; }
    public JComboBox<String> getCbTenLichTrinh() { return cbTenLichTrinh; }
    public JTextField getTxtThoiGianKhoiHanh() { return txtThoiGianKhoiHanh; }
    public JTextField getTxtThoiGianDen() { return txtThoiGianDen; }
    public JTextField getTxtGiaChuyen() { return txtGiaChuyen; }
    public JComboBox<String> getCbTrangThai() { return cbTrangThai; }

    // SỬA: Method để chỉ load data vào form (không enable btnLuu hay fields) - Gọi từ controller cho select row (view-only)
    public void loadFormData(ChuyenTau ct) {
        if (ct != null) {
            txtMaChuyenTau.setText(ct.getMaChuyenTau());
            // Set tên tàu và lịch trình vào combo
            String tenTau = tauDAO.getTenTauByMaTau(ct.getMaTau());
            if (tenTau != null) cbTenTau.setSelectedItem(tenTau);
            String tenLichTrinh = lichTrinhDAO.getTenLichTrinhByMaLichTrinh(ct.getMaLichTrinh());
            if (tenLichTrinh != null) cbTenLichTrinh.setSelectedItem(tenLichTrinh);
            txtThoiGianKhoiHanh.setText(ct.getThoiGianKhoiHanh().format(dateTimeFormatter));
            txtThoiGianDen.setText(ct.getThoiGianDen().format(dateTimeFormatter));
            txtGiaChuyen.setText(ct.getGiaChuyen().toString());
            cbTrangThai.setSelectedItem(ct.getTrangThai());
            // KHÔNG enable btnLuu hay fields ở đây
        }
    }

    // SỬA: Method để cập nhật form từ dữ liệu selected row (gọi từ controller cho edit) - Chỉ load data, controller sẽ enable
    public void updateForm(ChuyenTau ct) {
        loadFormData(ct);  // Gọi method mới để load data
        // KHÔNG enable tự động ở đây nữa
    }

    // Method để reset form (cho add mode)
    public void resetForm() {
        txtMaChuyenTau.setText("");
        cbTenTau.setSelectedIndex(0);
        cbTenLichTrinh.setSelectedIndex(0);
        txtThoiGianKhoiHanh.setText("yyyy-MM-dd HH:mm");  // SỬA: Set placeholder rõ ràng
        txtThoiGianDen.setText("yyyy-MM-dd HH:mm");
        txtGiaChuyen.setText("");
        cbTrangThai.setSelectedIndex(0);
        btnLuu.setEnabled(false);
        tblChuyenTau.clearSelection();
        // Disable form fields ban đầu
        enableFormFields(false);
    }

    // SỬA: Làm public để controller gọi được
    public void enableFormFields(boolean enable) {
        cbTenTau.setEnabled(enable);
        cbTenLichTrinh.setEnabled(enable);
        txtThoiGianKhoiHanh.setEnabled(enable);
        txtThoiGianDen.setEnabled(enable);
        txtGiaChuyen.setEnabled(enable);
        cbTrangThai.setEnabled(enable);
    }

    // Method để refresh data
    public void refreshData() {
        loadDataFromDB();
        resetForm();
    }

    // Method để lấy selected maChuyenTau từ table
    public String getSelectedMaChuyenTau() {
        int selectedRow = tblChuyenTau.getSelectedRow();
        if (selectedRow >= 0) {
            return (String) modelCT.getValueAt(selectedRow, 0);  // Cột 0: Mã chuyến
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
            new DanhSachChuyenTau().setVisible(true);
      
    }
}