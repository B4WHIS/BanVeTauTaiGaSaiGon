package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.util.Vector;

public class TraCuuVeTauGUI extends GiaoDienChinh implements ActionListener {

    // Thành phần chính
    private JPanel pnlChinh, pnlTitle, pnlTraCuu, pnlKetQua;
    private JLabel lblTieuDe;
    private JTextField txtHoTen, txtCMND, txtSDT;
    private JButton btnTim, btnLamMoi, btnTroVe, btnInVe, btnDoiVe;
    private JTable tblKetQua;
    private DefaultTableModel modelKetQua;

    // Màu sắc & Font
    private final Color COLOR_PRIMARY = new Color(74, 140, 103);
    private final Color COLOR_ACCENT = new Color(93, 156, 236);
    private final Color COLOR_ALERT = new Color(229, 115, 115);
    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 42);
    private final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 13);

    public TraCuuVeTauGUI() {
        setTitle("Tra cứu vé tàu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        // === PNL CHÍNH ===
        pnlChinh = new JPanel(new BorderLayout(12, 12));
        pnlChinh.setBorder(new EmptyBorder(12, 12, 12, 12));
        getContentPane().add(pnlChinh);

        // === TIÊU ĐỀ ===
        pnlTitle = new JPanel(new BorderLayout());
        pnlTitle.setBorder(BorderFactory.createEtchedBorder());
        lblTieuDe = new JLabel("TRA CỨU VÉ TÀU", SwingConstants.CENTER);
        lblTieuDe.setFont(FONT_TITLE);
        lblTieuDe.setForeground(COLOR_PRIMARY);
        pnlTitle.add(lblTieuDe, BorderLayout.CENTER);
        pnlChinh.add(pnlTitle, BorderLayout.NORTH);

        // === PANEL TRÁI - FORM TRA CỨU ===
        pnlTraCuu = new JPanel(new GridBagLayout());
        TitledBorder tb = BorderFactory.createTitledBorder("THÔNG TIN TRA CỨU");
        tb.setTitleFont(FONT_SECTION);
        tb.setTitleColor(COLOR_ACCENT);
        pnlTraCuu.setBorder(tb);
        pnlTraCuu.setPreferredSize(new Dimension(420, 0));
        setupFormTraCuu();
        pnlChinh.add(pnlTraCuu, BorderLayout.WEST);

        // === PANEL PHẢI - KẾT QUẢ ===
        pnlKetQua = new JPanel(new BorderLayout());
        TitledBorder tbKetQua = BorderFactory.createTitledBorder("KẾT QUẢ XÁC THỰC");
        tbKetQua.setTitleFont(FONT_SECTION);
        tbKetQua.setTitleColor(COLOR_ALERT);
        pnlKetQua.setBorder(tbKetQua);
        setupTableKetQua();
        pnlChinh.add(pnlKetQua, BorderLayout.CENTER);

        // === NÚT TRỞ VỀ DƯỚI CÙNG ===
        JPanel pnlChucNang = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnTroVe = new JButton("Trở về", GiaoDienChinh.chinhKichThuoc("/img/loginicon.png", 20, 20));
        btnTroVe.setBackground(COLOR_ALERT);
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setFont(FONT_LABEL);
        btnTroVe.addActionListener(this);
        pnlChucNang.add(btnTroVe);
        pnlChinh.add(pnlChucNang, BorderLayout.SOUTH);

        // Gắn sự kiện
        btnTim.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnInVe.addActionListener(this);
        btnDoiVe.addActionListener(this);
    }

    private void setupFormTraCuu() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        int row = 0;

        // Họ tên
        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setFont(FONT_LABEL);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlTraCuu.add(lblHoTen, gbc);
        txtHoTen = new JTextField();
        txtHoTen.setFont(FONT_INPUT);
        txtHoTen.setPreferredSize(new Dimension(220, 32));
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(txtHoTen, gbc);

        // CMND
        JLabel lblCMND = new JLabel("CMND/Hộ chiếu:");
        lblCMND.setFont(FONT_LABEL);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlTraCuu.add(lblCMND, gbc);
        txtCMND = new JTextField();
        txtCMND.setFont(FONT_INPUT);
        txtCMND.setPreferredSize(new Dimension(220, 32));
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(txtCMND, gbc);

        // SĐT
        JLabel lblSDT = new JLabel("SĐT:");
        lblSDT.setFont(FONT_LABEL);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlTraCuu.add(lblSDT, gbc);
        txtSDT = new JTextField();
        txtSDT.setFont(FONT_INPUT);
        txtSDT.setPreferredSize(new Dimension(220, 32));
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(txtSDT, gbc);

        // Nút chức năng
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

        // Nút in vé
        btnInVe = new JButton("In vé", GiaoDienChinh.chinhKichThuoc("/img/ve.png", 20, 20));
        btnInVe.setBackground(new Color(255, 237, 0));
        btnInVe.setFont(FONT_LABEL);
        btnInVe.setForeground(Color.BLACK);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        pnlTraCuu.add(btnInVe, gbc);

        // Nút đổi vé
        btnDoiVe = new JButton("Đổi vé", GiaoDienChinh.chinhKichThuoc("/img/swap.png", 20, 20));
        btnDoiVe.setBackground(new Color(255, 193, 7));
        btnDoiVe.setForeground(Color.BLACK);
        btnDoiVe.setFont(FONT_LABEL);
        gbc.gridx = 1; gbc.gridy = row++;
        gbc.anchor = GridBagConstraints.EAST;
        pnlTraCuu.add(btnDoiVe, gbc);
    }

    private void setupTableKetQua() {
        // Tạo model với 7 cột
        String[] cols = {"#", "Họ tên", "Thông tin vé", "Thành tiền (VNĐ)", "Loại vé", "Trạng thái vé", "Chọn"};
        modelKetQua = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 6 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Chỉ checkbox được chọn
            }
        };

        tblKetQua = new JTable(modelKetQua);
        tblKetQua.setRowHeight(50);
        tblKetQua.setFont(FONT_TABLE);
        tblKetQua.getTableHeader().setFont(FONT_LABEL);
        tblKetQua.getTableHeader().setBackground(new Color(245, 245, 245));
        tblKetQua.getTableHeader().setReorderingAllowed(false);

        // Renderer cho toàn bảng
        tblKetQua.setDefaultRenderer(Object.class, new GroupRowRenderer());
        tblKetQua.setDefaultRenderer(Boolean.class, new CheckBoxRenderer());

        JScrollPane scrollPane = new JScrollPane(tblKetQua);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(225, 242, 232)));
        pnlKetQua.add(scrollPane, BorderLayout.CENTER);
    }

    // Renderer cho hàng nhóm và dòng thường
    private class GroupRowRenderer extends DefaultTableCellRenderer {
        private final Color GROUP_BG = new Color(173, 216, 230);
        private final Color EVEN_BG = new Color(248, 250, 252);
        private final Color ODD_BG = Color.WHITE;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JLabel label = (JLabel) c;
            label.setBorder(new EmptyBorder(8, 8, 8, 8));

            String text = value != null ? value.toString() : "";
            boolean isGroupRow = text.contains("Hà Nội") || text.contains("Sài Gòn");

            if (isGroupRow) {
                label.setText("<html><b>" + text + "</b></html>");
                label.setBackground(GROUP_BG);
                label.setForeground(Color.BLACK);
                label.setFont(FONT_TABLE.deriveFont(Font.BOLD, 14));
                label.setHorizontalAlignment(SwingConstants.LEFT);

                // Ẩn các cột khác trong hàng nhóm
                if (column > 0) {
                    label.setText("");
                }
            } else {
                label.setBackground(row % 2 == 0 ? EVEN_BG : ODD_BG);
                label.setForeground(Color.BLACK);
                label.setFont(FONT_TABLE);
                label.setHorizontalAlignment(column == 6 ? SwingConstants.CENTER : SwingConstants.LEFT);
            }

            return label;
        }
    }

    // Renderer cho checkbox
    private class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        public CheckBoxRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setSelected(value != null && (Boolean) value);
            setBackground(row % 2 == 0 ? new Color(248, 250, 252) : Color.WHITE);
            return this;
        }
    }

    
    // === XỬ LÝ SỰ KIỆN ===
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTim) {
            JOptionPane.showMessageDialog(this, "Tìm thấy vé phù hợp!", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (e.getSource() == btnLamMoi) {
            txtHoTen.setText("");
            txtCMND.setText("");
            txtSDT.setText("");
            modelKetQua.setRowCount(0);
        }
        else if (e.getSource() == btnTroVe) {
            this.dispose();
        }
        else if (e.getSource() == btnInVe) {
            JOptionPane.showMessageDialog(this, "Chức năng in vé đang phát triển...", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (e.getSource() == btnDoiVe) {
            JOptionPane.showMessageDialog(this, "Chức năng đổi vé đang được phát triển...", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            int count = 0;
            for (int i = 0; i < modelKetQua.getRowCount(); i++) {
                Boolean checked = (Boolean) modelKetQua.getValueAt(i, 6);
                if (checked != null && checked) count++;
            }
            if (count == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một vé để trả!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Đã gửi yêu cầu trả " + count + " vé thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // === MAIN ===
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception ex) {}
        SwingUtilities.invokeLater(() -> new TraCuuVeTauGUI().setVisible(true));
    }
}