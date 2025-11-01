package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import control.QuanLyGaController;
import dao.GaDAO;
import entity.Ga;

public class QuanLyGa extends JFrame{
    // Components
    private JTable tblGa;
    private DefaultTableModel modelGa;
    private JTextField txtMaGa, txtTenGa, txtDiaChi;
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTroVe;
    private JButton btnLuu;
    // Controller và DAO
    private QuanLyGaController controller;
    private GaDAO dao;

    public QuanLyGa() {
        super();
        dao = new GaDAO();
        controller = new QuanLyGaController(this);
        setTitle("Quản lý ga");
        setSize(1200, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setJMenuBar(taoMenuBar());

        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("QUẢN LÝ GA", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103, 192, 144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // ===== LEFT PANEL =====
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(450, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        JLabel lblLeftTitle = new JLabel("THÔNG TIN GA", SwingConstants.CENTER);
        lblLeftTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLeftTitle.setForeground(new Color(103, 192, 144));
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

        JLabel lblMaGa = new JLabel("Mã ga:");
        JLabel lblTenGa = new JLabel("Tên ga:");
        JLabel lblDiaChi = new JLabel("Địa chỉ:");

        JLabel[] labels = {lblMaGa, lblTenGa, lblDiaChi};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        txtMaGa = new JTextField();
        txtMaGa.setEditable(false);
        txtTenGa = new JTextField();
        txtDiaChi = new JTextField();

        Component[] inputFields = {txtMaGa, txtTenGa, txtDiaChi};
        for (Component comp : inputFields) {
            if (comp instanceof JTextField) {
                JTextField txt = (JTextField) comp;
                txt.setFont(txtFont);
                txt.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(150, 150, 150)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
                txt.setPreferredSize(new Dimension(200, 30));
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
        btnLuu.setEnabled(false);

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

        String[] colHeader = {"Mã ga", "Tên ga", "Địa chỉ"};
        modelGa = new DefaultTableModel(colHeader, 0);
        tblGa = new JTable(modelGa);
        tblGa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblGa.setRowHeight(28);
        tblGa.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblGa.setSelectionBackground(new Color(173, 216, 230));

        JScrollPane scroll = new JScrollPane(tblGa);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnlRight.add(scroll, BorderLayout.CENTER);

        // ===== FOOTER =====
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(new Color(103, 192, 144));
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

        // Load data
        loadDataFromDB();
    }
    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
	    URL iconUrl = GiaoDienChinh.class.getResource(duongDan);
	    if (iconUrl == null) {
	        System.err.println("Không tìm thấy icon tại đường dẫn: " + duongDan); 
	        return null; 
	    }
	    ImageIcon iicGoc = new ImageIcon(iconUrl);
	    Image anhGoc = iicGoc.getImage();
	    Image anhDaDoi = anhGoc.getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
	    ImageIcon iicMoi = new ImageIcon(anhDaDoi);
	    return iicMoi;
	}
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
    
    // Load data từ DB
    private void loadDataFromDB() {
        modelGa.setRowCount(0);
        try {
            List<Ga> list = dao.getAllGa();
            for (Ga ga : list) {
                modelGa.addRow(new Object[]{
                    ga.getMaGa(),
                    ga.getTenGa(),
                    ga.getDiaChi()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi load dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Attach listeners
    private void attachListeners() {
        btnThem.addActionListener(controller);
        btnSua.addActionListener(controller);
        btnXoa.addActionListener(controller);
        btnReset.addActionListener(controller);
        btnExport.addActionListener(controller);
        btnTroVe.addActionListener(controller);
        btnLuu.addActionListener(controller);

        tblGa.getSelectionModel().addListSelectionListener(e -> {
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
    public JTable getTblGa() { return tblGa; }
    public DefaultTableModel getModelGa() { return modelGa; }
    public JTextField getTxtMaGa() { return txtMaGa; }
    public JTextField getTxtTenGa() { return txtTenGa; }
    public JTextField getTxtDiaChi() { return txtDiaChi; }

    // Load form khi chọn dòng
    public void loadFormData(Ga ga) {
        if (ga != null) {
            txtMaGa.setText(ga.getMaGa());
            txtTenGa.setText(ga.getTenGa());
            txtDiaChi.setText(ga.getDiaChi());
        }
    }

    // Reset form
    public void resetForm() {
        txtMaGa.setText("");
        txtTenGa.setText("");
        txtDiaChi.setText("");
        btnLuu.setEnabled(false);
        tblGa.clearSelection();
        enableFormFields(false);
    }

    // Enable/disable fields
    public void enableFormFields(boolean enable) {
        txtTenGa.setEnabled(enable);
        txtDiaChi.setEnabled(enable);
    }

    // Lấy mã ga được chọn
    public String getSelectedMaGa() {
        int row = tblGa.getSelectedRow();
        if (row >= 0) {
            return (String) modelGa.getValueAt(row, 0);
        }
        return null;
    }

    // Refresh
    public void refreshData() {
        loadDataFromDB();
        resetForm();
    }

    // Show message
    public void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    // Main
    public static void main(String[] args) {
        LookAndFeelManager.setNimbusLookAndFeel();
        new QuanLyGa().setVisible(true);
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//        // Không cần
//    }
}
