package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TraCuuVeTauGUI extends GiaoDienChinh implements ActionListener {

    // Thành phần chính
    private JPanel pnlChinh, pnlTitle, pnlTraCuu, pnlKetQua;
    private JLabel lblTieuDe;
    private JTextField txtHoTen, txtCMND, txtSDT;
    private JButton btnTim, btnLamMoi, btnTroVe, btnInVe;
    private JTable tblKetQua;
    private DefaultTableModel modelKetQua = new DefaultTableModel();
    private JEditorPane editorPane;
    private JScrollPane scpKetQua;

    // Màu sắc & Font
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

        // Dữ liệu mẫu để test
        themDuLieuMau();
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
    }

    // === BẢNG KẾT QUẢ - 1 BẢNG DUY NHẤT, TIÊU ĐỀ NHÓM TRẢI DÀI ===
    private void setupTableKetQua() {
        editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        editorPane.setBackground(Color.WHITE);

        // CSS để giống JTable
        String css = """
            <style>
                body { font-family: 'Segoe UI', sans-serif; margin: 0; padding: 0; }
                table { width: 100%; border-collapse: collapse; font-size: 13px; }
                th { background: #f5f5f5; padding: 12px 8px; text-align: left; font-weight: bold; border-bottom: 1px solid #ddd; }
                td { padding: 12px 8px; border-bottom: 1px solid #eee; vertical-align: top; }
                .group-row td { 
                    background: #ADD8E6 !important; 
                    font-weight: bold; 
                    font-size: 14px; 
                    padding: 10px 15px; 
                    border-bottom: 1px solid #B0B0B0;
                }
                .even-row td { background: #f8fafc; }
                .checkbox { text-align: center; }
                input[type="checkbox"] { transform: scale(1.2); }
            </style>
            """;

        editorPane.setText(css + "<body><table>" +
            "<tr><th>#</th><th>Họ tên</th><th>Thông tin vé</th><th>Thành tiền (VNĐ)</th>" +
            "<th>Loại trả vé</th><th>Lệ phí trả vé</th><th>Tiền trả lại</th>" +
            "<th>Thông tin trả vé</th><th class='checkbox'>Chọn</th></tr>" +
            "</table></body>");

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(225, 242, 232)));
        pnlKetQua.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(new EmptyBorder(8, 8, 8, 8));
        JPanel pnlButtonsRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtonsRight.setOpaque(false);

      
      
        pnlKetQua.add(pnlFooter, BorderLayout.SOUTH);
    }
    // === RENDERER HTML: TIÊU ĐỀ NHÓM TRẢI DÀI TOÀN BẢNG ===
 // === RENDERER HTML: VẼ TIÊU ĐỀ NHÓM TOÀN HÀNG ===
    private class HTMLFullRowRenderer extends DefaultTableCellRenderer {
        private final Color GROUP_BG = new Color(173, 216, 230);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            // XỬ LÝ CHECKBOX
            if (column == 8 && value instanceof Boolean) {
                JCheckBox cb = new JCheckBox();
                cb.setSelected((Boolean) value);
                cb.setHorizontalAlignment(SwingConstants.CENTER);
                cb.setBackground(row % 2 == 0 ? new Color(248, 250, 252) : Color.WHITE);
                return cb;
            }

            String text = value != null ? value.toString() : "";
            boolean isGroupRow = text.matches(".*(Hà Nội|Sài Gòn).*\\d{2}/\\d{2}/\\d{4}.*");

            if (isGroupRow && column == 0) {
                // DÙNG HTML ĐỂ VẼ TOÀN HÀNG
                String html = "<html><div style='"
                        + "background-color: #ADD8E6; "
                        + "color: black; "
                        + "font-weight: bold; "
                        + "font-size: 14px; "
                        + "padding: 10px 15px; "
                        + "border-bottom: 1px solid #B0B0B0; "
                        + "width: 100%; "
                        + "box-sizing: border-box;'>"
                        + text + "</div></html>";
                setText(html);
                setHorizontalAlignment(SwingConstants.LEFT);

                // QUAN TRỌNG: Set chiều cao và chiều rộng để vẽ toàn hàng
                setPreferredSize(new Dimension(table.getWidth(), 48));
                return this;
            }

            // ẨN CÁC Ô KHÁC TRONG HÀNG NHÓM
            if (isGroupRow && column != 0) {
                setText("");
                setBackground(GROUP_BG);
                return this;
            }

            // DÒNG BÌNH THƯỜNG
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBackground(row % 2 == 0 ? new Color(248, 250, 252) : Color.WHITE);
            setForeground(Color.BLACK);
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setHorizontalAlignment(column == 8 ? SwingConstants.CENTER : SwingConstants.LEFT);
            setBorder(new EmptyBorder(8, 8, 8, 8));
            return this;
        }
    }

    // === DỮ LIỆU MẪU ===
    private void themDuLieuMau() {
        StringBuilder html = new StringBuilder();
        html.append("<table>");

        // Header
        html.append("<tr><th>#</th><th>Họ tên</th><th>Thông tin vé</th><th>Thành tiền (VNĐ)</th>")
            .append("<th>Loại vé</th>")
            .append("<th>Trạng thái vé</th><th class='checkbox'>Chọn</th></tr>");

        int rowIndex = 0;

        // Nhóm 1
        html.append("<tr class='group-row'><td colspan='9'>Hà Nội - Sài Gòn 04/01/2021</td></tr>");
        rowIndex++;

        html.append(formatRow(++rowIndex, "1", "Nguyễn Văn A<br>Số ghế: 32A23434",
            "SE7 04/01 21 06:00<br>Toa: 1 chỗ số: 24<br>Ngồi mềm điều hòa",
            "1,014,000", "Bình thường", "", false));

        html.append(formatRow(++rowIndex, "4", "Nguyễn Văn A<br>Số ghế: 32A23434",
            "SE7 04/01 21 06:00<br>Toa: 1 chỗ số: 24<br>Ngồi mềm điều hòa",
            "1,014,000", "Bình thường", "Vé đã trả lại.", false));

        // Khoảng trống
        html.append("<tr><td colspan='9' style='padding: 5px;'></td></tr>");

        // Nhóm 2
        html.append("<tr class='group-row'><td colspan='9'>Sài Gòn - Hà Nội 09/01/2021</td></tr>");
        rowIndex++;

        html.append(formatRow(++rowIndex, "1", "Nguyễn Văn A<br>Số ghế: 32A23434",
            "SE8 09/01 21 06:00<br>Toa: 1 chỗ số: 44<br>Ngồi mềm điều hòa",
            "823,000", "Bình thường", "Vé đã trả lại.", false));

        html.append(formatRow(++rowIndex, "4", "Nguyễn Văn A<br>Số ghế: 32A23434",
            "SE8 09/01 21 06:00<br>Toa: 1 chỗ số: 44<br>Ngồi mềm điều hòa",
            "823,000", "Bình thường",
            "[Khuyến mãi cuối tuần] trả từ [1 ngày] đến [3000 ngày] áp dụng 15%", false));

        html.append("</table>");

        // Cập nhật nội dung
        String fullHtml = "<html><head><style>" +
            "body {font-family: 'Segoe UI', sans-serif; margin: 0; padding: 0;}" +
            "table {width: 100%; border-collapse: collapse; font-size: 13px;}" +
            "th {background: #f5f5f5; padding: 12px 8px; text-align: left; font-weight: bold; border-bottom: 1px solid #ddd;}" +
            "td {padding: 12px 8px; border-bottom: 1px solid #eee; vertical-align: top;}" +
            ".group-row td {background: #ADD8E6 !important; font-weight: bold; font-size: 14px; padding: 10px 15px; border-bottom: 1px solid #B0B0B0;}" +
            ".even-row td {background: #f8fafc;}" +
            ".checkbox {text-align: center;}" +
            "input[type='checkbox'] {transform: scale(1.2);}" +
            "</style></head><body>" + html.toString() + "</body></html>";

        editorPane.setText(fullHtml);
    }

    // Hàm hỗ trợ tạo dòng
    private String formatRow(int index, String no, String hoTen, String thongTinVe,
                             String thanhTien, String loaiTraVe,
                             String ghiChu, boolean checked) {
        String rowClass = (index % 2 == 0) ? "even-row" : "";
        String check = checked ? "checked" : "";
        return String.format(
            "<tr class='%s'>" +
            "<td>%s</td><td>%s</td><td>%s</td><td>%s</td>" +
            "<td>%s</td><td>%s</td>" +
            "<td class='checkbox'><input type='checkbox' %s></td>" +
            "</tr>", rowClass, no, hoTen, thongTinVe, thanhTien, loaiTraVe, ghiChu, check
        );
    }
    // === XỬ LÝ SỰ KIỆN ===
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTim) {
            themDuLieuMau();
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
            JOptionPane.showMessageDialog(this, "Chức năng in vé đang phát triển...");
        }
        else {
            int count = 0;
            for (int i = 0; i < modelKetQua.getRowCount(); i++) {
                Boolean checked = (Boolean) modelKetQua.getValueAt(i, 8);
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
            LookAndFeelManager.setNimbusLookAndFeel();
        } catch (Exception ex) {}
        SwingUtilities.invokeLater(() -> new TraCuuVeTauGUI().setVisible(true));
    }
}