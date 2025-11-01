package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import control.QuanLyLichTrinhController;
import dao.GaDAO;
import dao.LichTrinhDAO;
import entity.Ga;
import entity.LichTrinh;

public class QuanLyLichTrinh extends GiaoDienChinh {
    // Components
    private JTable tblLichTrinh;
    private DefaultTableModel modelLT;
    private JTextField txtMaLichTrinh, txtTenLichTrinh, txtKhoangCach;
    private JComboBox<String> cbGaDi, cbGaDen;
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTroVe;
    private JButton btnLuu;

    // Controller & DAO
    private QuanLyLichTrinhController controller;
    private LichTrinhDAO lichTrinhDAO;
    private GaDAO gaDAO;

    public QuanLyLichTrinh() {
        super();
        lichTrinhDAO = new LichTrinhDAO();
        gaDAO = new GaDAO();
        controller = new QuanLyLichTrinhController(this);

        setTitle("Quản lý lịch trình");
        setSize(1300, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setJMenuBar(taoMenuBar());

        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("QUẢN LÝ LỊCH TRÌNH", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103, 192, 144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // ===== LEFT PANEL =====
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(500, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        JLabel lblLeftTitle = new JLabel("THÔNG TIN LỊCH TRÌNH", SwingConstants.CENTER);
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

        JLabel lblMaLT = new JLabel("Mã lịch trình:");
        JLabel lblTenLT = new JLabel("Tên lịch trình:");
        JLabel lblGaDi = new JLabel("Ga đi:");
        JLabel lblGaDen = new JLabel("Ga đến:");
        JLabel lblKhoangCach = new JLabel("Khoảng cách (km):");

        JLabel[] labels = {lblMaLT, lblTenLT, lblGaDi, lblGaDen, lblKhoangCach};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        txtMaLichTrinh = new JTextField();
        txtMaLichTrinh.setEditable(false);
        txtTenLichTrinh = new JTextField();
        txtKhoangCach = new JTextField();
        cbGaDi = new JComboBox<>();
        cbGaDen = new JComboBox<>();

        Component[] inputs = {txtMaLichTrinh, txtTenLichTrinh, cbGaDi, cbGaDen, txtKhoangCach};
        for (Component comp : inputs) {
            if (comp instanceof JTextField) {
                JTextField txt = (JTextField) comp;
                txt.setFont(txtFont);
                txt.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(150, 150, 150)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
                txt.setPreferredSize(new Dimension(220, 30));
            } else if (comp instanceof JComboBox) {
                JComboBox<?> cb = (JComboBox<?>) comp;
                cb.setFont(txtFont);
                cb.setPreferredSize(new Dimension(220, 30));
            }
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.35;
            pnlForm.add(labels[i], gbc);
            gbc.gridx = 1;
            gbc.weightx = 0.65;
            pnlForm.add(inputs[i], gbc);
        }

        // ===== BUTTONS =====
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

        String[] colHeader = {"Mã LT", "Tên lịch trình", "Ga đi", "Ga đến", "Khoảng cách (km)"};
        modelLT = new DefaultTableModel(colHeader, 0);
        tblLichTrinh = new JTable(modelLT);
        tblLichTrinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblLichTrinh.setRowHeight(28);
        tblLichTrinh.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblLichTrinh.setSelectionBackground(new Color(173, 216, 230));

        JScrollPane scroll = new JScrollPane(tblLichTrinh);
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

        // Load data
        loadCombosFromDB();
        loadDataFromDB();

        // Attach listeners
        attachListeners();
    }

    // Load combo ga
    private void loadCombosFromDB() {
        cbGaDi.removeAllItems();
        cbGaDen.removeAllItems();
        List<Ga> listGa = gaDAO.getAllGa();
        for (Ga ga : listGa) {
            String item = ga.getTenGa() + " (" + ga.getMaGa() + ")";
            cbGaDi.addItem(item);
            cbGaDen.addItem(item);
        }
    }

    // Load table
    private void loadDataFromDB() {
        modelLT.setRowCount(0);
        try {
            List<LichTrinh> list = lichTrinhDAO.getAllLichTrinh();
            for (LichTrinh lt : list) {
                modelLT.addRow(new Object[]{
                    lt.getMaLichTrinh(),
                    lt.getTenLichTrinh(),
                    lt.getMaGaDi().getTenGa(),
                    lt.getMaGaDen().getTenGa(),
                    lt.getKhoangCach()
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

        tblLichTrinh.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                controller.handleTableSelection();
            }
        });
    }

    // ===== GETTERS =====
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnReset() { return btnReset; }
    public JButton getBtnExport() { return btnExport; }
    public JButton getBtnTroVe() { return btnTroVe; }
    public JButton getBtnLuu() { return btnLuu; }
    public JTable getTblLichTrinh() { return tblLichTrinh; }
    public DefaultTableModel getModelLT() { return modelLT; }
    public JTextField getTxtMaLichTrinh() { return txtMaLichTrinh; }
    public JTextField getTxtTenLichTrinh() { return txtTenLichTrinh; }
    public JComboBox<String> getCbGaDi() { return cbGaDi; }
    public JComboBox<String> getCbGaDen() { return cbGaDen; }
    public JTextField getTxtKhoangCach() { return txtKhoangCach; }

    // Load form
    public void loadFormData(LichTrinh lt) {
        if (lt != null) {
            txtMaLichTrinh.setText(lt.getMaLichTrinh());
            txtTenLichTrinh.setText(lt.getTenLichTrinh());
            txtKhoangCach.setText(String.valueOf(lt.getKhoangCach()));
            setComboSelection(cbGaDi, lt.getMaGaDi().getTenGa());
            setComboSelection(cbGaDen, lt.getMaGaDen().getTenGa());
        }
    }

    private void setComboSelection(JComboBox<String> combo, String tenGa) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            String item = combo.getItemAt(i);
            if (item.startsWith(tenGa)) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    // Reset form
    public void resetForm() {
        txtMaLichTrinh.setText("");
        txtTenLichTrinh.setText("");
        txtKhoangCach.setText("");
        cbGaDi.setSelectedIndex(0);
        cbGaDen.setSelectedIndex(0);
        btnLuu.setEnabled(false);
        tblLichTrinh.clearSelection();
        enableFormFields(false);
    }

    // Enable fields
    public void enableFormFields(boolean enable) {
        txtTenLichTrinh.setEnabled(enable);
        cbGaDi.setEnabled(enable);
        cbGaDen.setEnabled(enable);
        txtKhoangCach.setEnabled(enable);
    }

    // Get selected maLichTrinh
    public String getSelectedMaLichTrinh() {
        int row = tblLichTrinh.getSelectedRow();
        if (row >= 0) {
            return (String) modelLT.getValueAt(row, 0);
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
        new QuanLyLichTrinh().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}