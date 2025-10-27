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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.HanhKhach;
import entity.NhanVien;
import entity.Ve;

public class LapHoaDonGUI extends JFrame implements ActionListener {

    private final Color MAU_CHINH = new Color(74, 140, 103);
    private final Color MAU_TONGKET = new Color(200, 30, 30);
    private final Font FONT_TIEU_DE = new Font("Segoe UI", Font.BOLD, 36);
    private final Font FONT_NHI = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);

    private List<Ve> danhSachVe;
    private HanhKhach nguoiThanhToan;
    private NhanVien nhanVienLap;
    private ThanhToanGUI previousScreen;

    private JButton btnInMoPhong, btnTroVe;
    private JTextArea txtPreview;

    private final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final NumberFormat CURRENCY = NumberFormat.getInstance(new Locale("vi", "VN"));

    public LapHoaDonGUI(List<Ve> danhSachVe, HanhKhach nguoiThanhToan, NhanVien nhanVienLap, ThanhToanGUI previous) {
        if (danhSachVe == null || danhSachVe.isEmpty()) {
            throw new IllegalArgumentException("Danh sách vé không được rỗng.");
        }
        this.danhSachVe = danhSachVe;
        this.nguoiThanhToan = nguoiThanhToan != null ? nguoiThanhToan : new HanhKhach();
        this.nhanVienLap = nhanVienLap != null ? nhanVienLap : new NhanVien("NV-000");
        this.previousScreen = previous;

        initFrame();
        initComponents();
        loadDataToPreview();
        setListeners();
    }

    private void initFrame() {
        setTitle("HÓA ĐƠN THANH TOÁN");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        getContentPane().add(root);

        JLabel lblTitle = new JLabel("HÓA ĐƠN THANH TOÁN", SwingConstants.CENTER);
        lblTitle.setFont(FONT_TIEU_DE);
        lblTitle.setForeground(MAU_CHINH);
        root.add(lblTitle, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 12, 12));
        root.add(center, BorderLayout.CENTER);

        JPanel left = new JPanel(new GridBagLayout());
        left.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MAU_CHINH, 2),
                "Chi tiết hóa đơn",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_CHINH
        ));
        center.add(left);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;

        JPanel pnlInfo = new JPanel(new GridLayout(6, 1, 6, 6));
        pnlInfo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        pnlInfo.add(createInfoLabel("Người thanh toán: " + safe(nguoiThanhToan.getHoTen())));
        pnlInfo.add(createInfoLabel("CCCD: " + safe(nguoiThanhToan.getCmndCccd())));
        pnlInfo.add(createInfoLabel("SĐT: " + safe(nguoiThanhToan.getSoDT())));

        ChuyenTau ct = danhSachVe.get(0).getMaChuyenTau();
        String chuyen = ct != null ? safe(ct.getMaChuyenTau()) : "N/A";
        String thoiGian = (ct != null && ct.getThoiGianKhoiHanh() != null) ? ct.getThoiGianKhoiHanh().format(DTF) : "N/A";
        pnlInfo.add(createInfoLabel("Chuyến/Tàu: " + chuyen));
        pnlInfo.add(createInfoLabel("Thời gian khởi hành: " + thoiGian));
        pnlInfo.add(createInfoLabel("Nhân viên lập: " + safe(nhanVienLap.getHoTen()) + " (" + safe(nhanVienLap.getMaNhanVien()) + ")"));

        left.add(pnlInfo, gbc);

        JPanel pnlVeList = new JPanel(new BorderLayout(6, 6));
        pnlVeList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MAU_CHINH),
                "Danh sách vé",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), MAU_CHINH
        ));

        JTextArea txtTickets = new JTextArea();
        txtTickets.setEditable(false);
        txtTickets.setFont(new Font("Consolas", Font.PLAIN, 13));
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-6s %-20s %-12s %-12s\n", "Mã vé", "Hành khách", "Chỗ ngồi", "Giá (VNĐ)"));
        sb.append("--------------------------------------------------------------\n");
        for (Ve ve : danhSachVe) {
            String maVe = safe(ve.getMaVe());
            String ten = ve.getMaHanhkhach() != null ? safe(ve.getMaHanhkhach().getHoTen()) : "N/A";
            ChoNgoi cn = ve.getMaChoNgoi();
            String cho = cn != null ? (safe(cn.getMaChoNgoi())) : "N/A";
            String gia = ve.getGiaThanhToan() != null ? String.format("%s", CURRENCY.format(ve.getGiaThanhToan().setScale(0, RoundingMode.HALF_UP))) : "0";
            sb.append(String.format("%-6s %-20s %-12s %12s\n", maVe, ten, cho, gia + " VNĐ"));
        }
        txtTickets.setText(sb.toString());
        txtTickets.setCaretPosition(0);
        pnlVeList.add(new JScrollPane(txtTickets), BorderLayout.CENTER);

        gbc.gridy = 1; gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        left.add(pnlVeList, gbc);

        gbc.gridy = 2; gbc.weighty = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel pnlTotals = new JPanel(new GridLayout(4, 1, 6, 6));
        pnlTotals.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        BigDecimal tongTruocVAT = computeTongTienTruocVAT();
        BigDecimal thueVAT = tongTruocVAT.multiply(new BigDecimal("0.1")).setScale(0, RoundingMode.HALF_UP);
        BigDecimal tongPhaiThu = tongTruocVAT.add(thueVAT);

        pnlTotals.add(createTotalsLabel("Tổng tiền vé (chưa thuế): " + CURRENCY.format(tongTruocVAT) + " VNĐ"));
        pnlTotals.add(createTotalsLabel("Thuế VAT (10%): " + CURRENCY.format(thueVAT) + " VNĐ"));
        pnlTotals.add(createTotalsLabel("Tổng phải thu: " + CURRENCY.format(tongPhaiThu) + " VNĐ", MAU_TONGKET));
        pnlTotals.add(createTotalsLabel("Ghi chú: Hóa đơn mô phỏng - không có giá trị pháp lý."));

        left.add(pnlTotals, gbc);

        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MAU_CHINH, 2),
                "Mô phỏng in hóa đơn",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_CHINH
        ));
        center.add(right);

        txtPreview = new JTextArea();
        txtPreview.setEditable(false);
        txtPreview.setFont(new Font("Courier New", Font.PLAIN, 13));
        txtPreview.setBackground(Color.WHITE);
        txtPreview.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane previewScroll = new JScrollPane(txtPreview);
        right.add(previewScroll, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        btnTroVe = new JButton("Đóng");
        btnInMoPhong = new JButton("In mô phỏng hóa đơn");
        btnTroVe.setFont(FONT_BUTTON);
        btnInMoPhong.setFont(FONT_BUTTON);
        btnInMoPhong.setBackground(MAU_CHINH);
        btnInMoPhong.setForeground(Color.WHITE);
        btnTroVe.setBackground(new Color(220, 20, 60));
        btnTroVe.setForeground(Color.WHITE);

        pnlButtons.add(btnTroVe);
        pnlButtons.add(btnInMoPhong);
        root.add(pnlButtons, BorderLayout.SOUTH);
    }

    private JLabel createInfoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_NHI);
        return lbl;
    }

    private JLabel createTotalsLabel(String text) {
        return createTotalsLabel(text, null);
    }

    private JLabel createTotalsLabel(String text, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_NHI);
        if (color != null) lbl.setForeground(color);
        return lbl;
    }

    private String safe(String s) {
        return s == null ? "N/A" : s;
    }

    private BigDecimal computeTongTienTruocVAT() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Ve v : danhSachVe) {
            if (v.getGiaThanhToan() != null) sum = sum.add(v.getGiaThanhToan());
        }
        return sum.setScale(0, RoundingMode.HALF_UP);
    }

    private void setListeners() {
        btnInMoPhong.addActionListener(this);
        btnTroVe.addActionListener(this);
    }

    private void loadDataToPreview() {
        txtPreview.setText(buildInvoiceString());
        txtPreview.setCaretPosition(0);
    }

    private String buildInvoiceString() {
        StringBuilder out = new StringBuilder();
        out.append(centerLine("HÓA ĐƠN THANH TOÁN", 48)).append("\n\n");
        out.append("Ngày lập: ").append(java.time.LocalDateTime.now().format(DTF)).append("\n");
        out.append("Người thanh toán: ").append(safe(nguoiThanhToan.getHoTen())).append("\n");
        out.append("CCCD: ").append(safe(nguoiThanhToan.getCmndCccd())).append("\n");
        out.append("SĐT: ").append(safe(nguoiThanhToan.getSoDT())).append("\n\n");

        ChuyenTau ct = danhSachVe.get(0).getMaChuyenTau();
        out.append("Chuyến/Tàu: ").append(ct != null ? safe(ct.getMaChuyenTau()) : "N/A").append("\n");
        out.append("Khởi hành: ").append((ct != null && ct.getThoiGianKhoiHanh()!=null) ? ct.getThoiGianKhoiHanh().format(DTF) : "N/A").append("\n\n");

        out.append(String.format("%-6s %-18s %-8s %12s\n", "Mã", "Hành khách", "Chỗ", "Giá (VNĐ)"));
        out.append("--------------------------------------------------------------\n");
        for (Ve v : danhSachVe) {
            String ma = safe(v.getMaVe());
            String ten = v.getMaHanhkhach() != null ? safe(v.getMaHanhkhach().getHoTen()) : "N/A";
            ChoNgoi cn = v.getMaChoNgoi();
            String cho = cn != null ? safe(cn.getMaChoNgoi()) : "N/A";
            String gia = v.getGiaThanhToan() != null ? CURRENCY.format(v.getGiaThanhToan()) : "0";
            out.append(String.format("%-6s %-18s %-8s %12s\n", ma, truncate(ten, 18), cho, gia));
        }

        out.append("\n");
        BigDecimal tong = computeTongTienTruocVAT();
        BigDecimal vat = tong.multiply(new BigDecimal("0.1")).setScale(0, RoundingMode.HALF_UP);
        BigDecimal total = tong.add(vat);
        out.append(padRight("Tổng tiền (chưa thuế):", 36)).append(String.format("%12s VNĐ\n", CURRENCY.format(tong)));
        out.append(padRight("Thuế VAT (10%):", 36)).append(String.format("%12s VNĐ\n", CURRENCY.format(vat)));
        out.append(padRight("Tổng phải thu:", 36)).append(String.format("%12s VNĐ\n", CURRENCY.format(total)));
        out.append("\n");
        out.append(centerLine("CẢM ƠN QUÝ KHÁCH - CHÚC QUÝ KHÁCH HÀNH TRÌNH AN TOÀN", 48)).append("\n");
        return out.toString();
    }

    private String centerLine(String s, int width) {
        if (s == null) s = "";
        if (s.length() >= width) return s + "\n";
        int left = (width - s.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < left; i++) sb.append(" ");
        sb.append(s);
        return sb.toString();
    }

    private String padRight(String s, int width) {
        if (s == null) s = "";
        if (s.length() >= width) return s;
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < width) sb.append(' ');
        return sb.toString();
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max - 3) + "...";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnTroVe) {
            if (previousScreen != null) {
                previousScreen.setVisible(true);
            }
            this.dispose();
        } else if (src == btnInMoPhong) {
            showPrintDialog(buildInvoiceString());
        }
    }

    private void showPrintDialog(String invoiceText) {
        JDialog dlg = new JDialog(this, "Mô phỏng in hóa đơn", true);
        dlg.setSize(new Dimension(600, 700));
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(8, 8));

        JTextArea paper = new JTextArea(invoiceText);
        paper.setEditable(false);
        paper.setFont(new Font("Courier New", Font.PLAIN, 13));
        paper.setBackground(Color.WHITE);
        paper.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JScrollPane sc = new JScrollPane(paper);
        sc.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        dlg.add(sc, BorderLayout.CENTER);

        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnClose = new JButton("Đóng");
        JButton btnFakePrint = new JButton("Mô phỏng gửi lệnh in");
        btnClose.setFont(FONT_BUTTON);
        btnFakePrint.setFont(FONT_BUTTON);
        btnFakePrint.setBackground(MAU_CHINH);
        btnFakePrint.setForeground(Color.WHITE);
        btnClose.setBackground(new Color(220, 20, 60));
        btnClose.setForeground(Color.WHITE);

        pnl.add(btnClose);
        pnl.add(btnFakePrint);
        dlg.add(pnl, BorderLayout.SOUTH);

        btnClose.addActionListener(a -> dlg.dispose());
        btnFakePrint.addActionListener(a -> {
            JOptionPane.showMessageDialog(dlg, "Lệnh in đã được gửi (mô phỏng).", "In mô phỏng", JOptionPane.INFORMATION_MESSAGE);
        });

        dlg.setVisible(true);
    }
}
