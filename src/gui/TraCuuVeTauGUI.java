// File: src/gui/TraCuuVeTauGUI.java
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;  // ĐÃ THÊM DÒNG NÀY

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import control.TraCuuVeTauController;

public class TraCuuVeTauGUI extends GiaoDienChinh {
    private JPanel pnlChinh, pnlTitle, pnlTraCuu, pnlKetQua;
    private JLabel lblTieuDe;
    private JTextField txtHoTen, txtCMND, txtSDT;
    private JButton btnTim, btnLamMoi, btnTroVe, btnInVe, btnDoiVe, btnHuyVe;
    private JTable table;
    private DefaultTableModel tableModel;

    private final Color COLOR_PRIMARY = new Color(74, 140, 103);
    private final Color COLOR_ACCENT = new Color(93, 156, 236);
    private final Color COLOR_ALERT = new Color(229, 115, 115);
    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 42);
    private final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);

    public TraCuuVeTauGUI() {
        setTitle("Tra cứu vé tàu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        initComponents();
        setupLayout();
        setupActions();
        setupEmptyTable();
    }

    private void initComponents() {
        pnlChinh = new JPanel(new BorderLayout(12, 12));
        pnlChinh.setBorder(new EmptyBorder(12, 12, 12, 12));
        getContentPane().add(pnlChinh);

        // Tiêu đề
        pnlTitle = new JPanel(new BorderLayout());
        pnlTitle.setBorder(BorderFactory.createEtchedBorder());
        lblTieuDe = new JLabel("TRA CỨU VÉ TÀU", SwingConstants.CENTER);
        lblTieuDe.setFont(FONT_TITLE);
        lblTieuDe.setForeground(COLOR_PRIMARY);
        pnlTitle.add(lblTieuDe, BorderLayout.CENTER);

        // Form
        pnlTraCuu = new JPanel(new GridBagLayout());
        TitledBorder tb = BorderFactory.createTitledBorder("THÔNG TIN TRA CỨU");
        tb.setTitleFont(FONT_SECTION);
        tb.setTitleColor(COLOR_ACCENT);
        pnlTraCuu.setBorder(tb);
        pnlTraCuu.setPreferredSize(new Dimension(420, 0));

        // Kết quả
        pnlKetQua = new JPanel(new BorderLayout());
        TitledBorder tbKetQua = BorderFactory.createTitledBorder("KẾT QUẢ TRA CỨU");
        tbKetQua.setTitleFont(FONT_SECTION);
        tbKetQua.setTitleColor(COLOR_ALERT);
        pnlKetQua.setBorder(tbKetQua);

        // Bảng JTable
        tableModel = new DefaultTableModel(
        	    new Object[]{"#", "Họ tên", "Thông tin vé", "Thành tiền (VNĐ)", "Loại vé", "Trạng thái", "Chọn"}, 0
        	) {
        	    @Override
        	    public Class<?> getColumnClass(int columnIndex) {
        	        return columnIndex == 6 ? Boolean.class : String.class;
        	    }

        	    @Override
        	    public boolean isCellEditable(int row, int column) {
        	        if (column != 6) return false;
        	        Object value = getValueAt(row, 6);
        	        // Chỉ cho phép chỉnh sửa nếu là Boolean (true/false) → tức là vé cho phép hủy
        	        return value instanceof Boolean;
        	    }
        	};
        table = new JTable(tableModel);
        table.setRowHeight(60);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(6).setMaxWidth(60);
        table.getColumnModel().getColumn(6).setCellRenderer((table1, value, isSelected, hasFocus, row, column) -> {
            JCheckBox cb = new JCheckBox();
            cb.setHorizontalAlignment(SwingConstants.CENTER);

            if (value instanceof Boolean) {
                cb.setSelected((Boolean) value);
                cb.setEnabled(true); // Cho phép click
            } else {
                cb.setSelected(false);
                cb.setEnabled(false); // Không cho click
                cb.setToolTipText("Vé không đủ điều kiện hủy");
            }

            if (isSelected) {
                cb.setBackground(table1.getSelectionBackground());
                cb.setForeground(table1.getSelectionForeground());
            } else {
                cb.setBackground(table1.getBackground());
                cb.setForeground(table1.getForeground());
            }

            return cb;
        });
        table.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(225, 242, 232)));
        pnlKetQua.add(scrollPane, BorderLayout.CENTER);
    }

    private void setupLayout() {
        pnlChinh.add(pnlTitle, BorderLayout.NORTH);
        pnlChinh.add(pnlTraCuu, BorderLayout.WEST);
        pnlChinh.add(pnlKetQua, BorderLayout.CENTER);

        JPanel pnlChucNang = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnTroVe = new JButton("Trở về", GiaoDienChinh.chinhKichThuoc("/img/loginicon.png", 20, 20));
        btnTroVe.setBackground(COLOR_ALERT);
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setFont(FONT_LABEL);
        pnlChucNang.add(btnTroVe);
        pnlChinh.add(pnlChucNang, BorderLayout.SOUTH);

        setupFormTraCuu();
    }

    private void setupFormTraCuu() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        int row = 0;

        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setFont(FONT_LABEL);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlTraCuu.add(lblHoTen, gbc);
        txtHoTen = new JTextField();
        txtHoTen.setFont(FONT_INPUT);
        txtHoTen.setPreferredSize(new Dimension(220, 32));
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(txtHoTen, gbc);

        JLabel lblCMND = new JLabel("CMND/Hộ chiếu:");
        lblCMND.setFont(FONT_LABEL);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlTraCuu.add(lblCMND, gbc);
        txtCMND = new JTextField();
        txtCMND.setFont(FONT_INPUT);
        txtCMND.setPreferredSize(new Dimension(220, 32));
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(txtCMND, gbc);

        JLabel lblSDT = new JLabel("SĐT:");
        lblSDT.setFont(FONT_LABEL);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlTraCuu.add(lblSDT, gbc);
        txtSDT = new JTextField();
        txtSDT.setFont(FONT_INPUT);
        txtSDT.setPreferredSize(new Dimension(220, 32));
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(txtSDT, gbc);

        JPanel pnlNut = new JPanel(new GridLayout(1, 2, 10, 0));
        btnLamMoi = new JButton("Làm mới", GiaoDienChinh.chinhKichThuoc("/img/undo.png", 20, 20));
        btnLamMoi.setBackground(COLOR_ALERT);
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(FONT_LABEL);
        btnTim = new JButton("Tìm", GiaoDienChinh.chinhKichThuoc("/img/traCuu.png", 20, 20));
        btnTim.setBackground(COLOR_ACCENT);
        btnTim.setForeground(Color.WHITE);
        btnTim.setFont(FONT_LABEL);
        pnlNut.add(btnLamMoi);
        pnlNut.add(btnTim);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(pnlNut, gbc);

        JPanel pnlNutThem = new JPanel(new GridLayout(1, 2, 10, 0));
        btnDoiVe = new JButton("Đổi vé", GiaoDienChinh.chinhKichThuoc("/img/change.png", 20, 20));
        btnDoiVe.setBackground(new Color(255, 193, 7));
        btnDoiVe.setForeground(Color.WHITE);
        btnDoiVe.setFont(FONT_LABEL);
        btnHuyVe = new JButton("Hủy vé", GiaoDienChinh.chinhKichThuoc("/img/cancel.png", 20, 20));
        btnHuyVe.setBackground(COLOR_ALERT);
        btnHuyVe.setForeground(Color.WHITE);
        btnHuyVe.setFont(FONT_LABEL);
        pnlNutThem.add(btnDoiVe);
        pnlNutThem.add(btnHuyVe);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(pnlNutThem, gbc);

        btnInVe = new JButton("In vé", GiaoDienChinh.chinhKichThuoc("/img/ve.png", 20, 20));
        btnInVe.setBackground(new Color(255, 237, 0));
        btnInVe.setFont(FONT_LABEL);
        btnInVe.setForeground(Color.BLACK);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        pnlTraCuu.add(btnInVe, gbc);
    }

    private void setupActions() {
        TraCuuVeTauController controller = new TraCuuVeTauController(this);

        btnTim.addActionListener(controller);
        btnTim.setActionCommand("Tìm");

        btnLamMoi.addActionListener(controller);
        btnLamMoi.setActionCommand("Làm mới");

        btnTroVe.addActionListener(controller);
        btnTroVe.setActionCommand("Trở về");

        btnInVe.addActionListener(controller);
        btnInVe.setActionCommand("In vé");

        btnDoiVe.addActionListener(controller);
        btnDoiVe.setActionCommand("Đổi vé");

        btnHuyVe.addActionListener(controller);
        btnHuyVe.setActionCommand("Hủy vé");
    }

    public void setupEmptyTable() {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{
            "", "Nhập thông tin và nhấn Tìm để tra cứu vé", "", "", "", "", null
        });
    }

    // === GETTER CHO CONTROLLER ===
    public JTextField getTxtHoTen() { return txtHoTen; }
    public JTextField getTxtCMND() { return txtCMND; }
    public JTextField getTxtSDT() { return txtSDT; }
    public DefaultTableModel getTableModel() { return tableModel; }

    public static void main(String[] args) {
        try {
            LookAndFeelManager.setNimbusLookAndFeel();
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new TraCuuVeTauGUI().setVisible(true));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Không cần dùng vì đã dùng ActionListener riêng
    }
}