package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.ImageIcon;
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
    private boolean laDoiVe = false;
    public LapHoaDonGUI(List<Ve> danhSachVe, HanhKhach nguoiThanhToan, NhanVien nhanVienLap, ThanhToanGUI previous) {
        if (danhSachVe == null || danhSachVe.isEmpty()) {
            throw new IllegalArgumentException("Danh sách vé không được rỗng.");
        }
        this.danhSachVe = danhSachVe;
        this.nguoiThanhToan = nguoiThanhToan != null ? nguoiThanhToan : new HanhKhach();
        this.nhanVienLap = nhanVienLap != null ? nhanVienLap : new NhanVien("NV-000");
        this.previousScreen = previous;
        this.laDoiVe = danhSachVe.size() == 1 
                && danhSachVe.get(0).getTrangThai() != null 
                && danhSachVe.get(0).getTrangThai().contains("Đã đổi");
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

        JPanel pnlPreview = new JPanel(new BorderLayout(8, 8));
        TitledBorder tb = BorderFactory.createTitledBorder("Xem trước hóa đơn");
        tb.setTitleFont(new Font("Segoe UI", Font.BOLD, 20));
        tb.setTitleColor(MAU_TONGKET);
        pnlPreview.setBorder(tb);
        root.add(pnlPreview, BorderLayout.CENTER);

        txtPreview = new JTextArea(buildInvoiceString());
        txtPreview.setEditable(false);
        txtPreview.setFont(new Font("Courier New", Font.PLAIN, 13));
        txtPreview.setBackground(Color.WHITE);
        JScrollPane sc = new JScrollPane(txtPreview);
        sc.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pnlPreview.add(sc, BorderLayout.CENTER);

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnTroVe = new JButton("Trở về", new ImageIcon(getClass().getResource("/img/quay_lai.png")));
        btnTroVe.setBackground(new Color(41, 128, 185));
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setFont(FONT_BUTTON);

        btnInMoPhong = new JButton("Mô phỏng in", new ImageIcon(getClass().getResource("/img/in_hoa_don.png")));
        btnInMoPhong.setBackground(MAU_CHINH);
        btnInMoPhong.setForeground(Color.WHITE);
        btnInMoPhong.setFont(FONT_BUTTON);

        pnlActions.add(btnTroVe);
        pnlActions.add(btnInMoPhong);
        root.add(pnlActions, BorderLayout.SOUTH);
    }

    private void loadDataToPreview() {
        txtPreview.setText(buildInvoiceString());
    }

    private void setListeners() {
        btnTroVe.addActionListener(this);
        btnInMoPhong.addActionListener(this);
    }

    private String buildInvoiceString() {
        StringBuilder out = new StringBuilder();
        
        // === TIÊU ĐỀ HÓA ĐƠN ===
        String tieuDe = laDoiVe ? "HÓA ĐƠN PHÍ ĐỔI VÉ" : "HÓA ĐƠN BÁN VÉ TÀU";
        out.append(centerLine("CÔNG TY TNHH MTV ĐƯỜNG SẮT VIỆT NAM", 48)).append("\n");
        out.append(centerLine(tieuDe, 48)).append("\n\n");

        out.append(padRight("Số hóa đơn: HD-" + (laDoiVe ? "DOI" : "BAN") + "-XXXXXX", 48)).append("\n");
        out.append(padRight("Ngày lập: " + DTF.format(java.time.LocalDateTime.now()), 48)).append("\n");
        out.append(padRight("Nhân viên: " + nhanVienLap.getHoTen(), 48)).append("\n\n");

        out.append(padRight("Khách hàng: " + nguoiThanhToan.getHoTen(), 48)).append("\n");
        out.append(padRight("CMND/CCCD: " + nguoiThanhToan.getCmndCccd(), 48)).append("\n");
        out.append(padRight("SĐT: " + nguoiThanhToan.getSoDT(), 48)).append("\n\n");

        out.append("-----------------------------------------------\n");
        out.append(String.format("%-5s %-20s %-10s %-10s\n", "STT", "Thông tin vé", "SL", "Thành tiền"));
        out.append("-----------------------------------------------\n");

        BigDecimal tongVeGoc = BigDecimal.ZERO;
        int stt = 1;
        for (Ve ve : danhSachVe) {
            String info;
            if (laDoiVe) {
                info = truncate("Đổi: " + ve.getMaVe() + " → " + ve.getMaChoNgoi().getMaChoNgoi(), 20);
            } else {
                info = truncate("Vé " + ve.getMaVe() + " - Ghế " + ve.getMaChoNgoi().getMaChoNgoi(), 20);
            }
            out.append(String.format("%-5d %-20s %-10d %-10s\n",
                    stt++,
                    info,
                    1,
                    CURRENCY.format(ve.getGiaThanhToan())));
            tongVeGoc = tongVeGoc.add(ve.getGiaThanhToan());
        }

        BigDecimal vat = tongVeGoc.multiply(new BigDecimal("0.1")).setScale(0, RoundingMode.HALF_UP);
        BigDecimal tongCuoi = tongVeGoc.add(vat);

        out.append("-----------------------------------------------\n");
        
        // === TÊN DÒNG TỔNG KẾT ===
        String tenTong = laDoiVe ? "Phí đổi vé:" : "Tổng tiền vé gốc:";
        out.append(padRight(tenTong + " " + CURRENCY.format(tongVeGoc), 48)).append("\n");
        out.append(padRight("Thuế VAT (10%): " + CURRENCY.format(vat), 48)).append("\n");
        out.append(padRight("TỔNG CỘNG: " + CURRENCY.format(tongCuoi), 48)).append("\n\n");

        String loiCamOn = laDoiVe 
            ? "CẢM ƠN QUÝ KHÁCH ĐÃ SỬ DỤNG DỊCH VỤ ĐỔI VÉ" 
            : "CẢM ƠN QUÝ KHÁCH - CHÚC QUÝ KHÁCH HÀNH TRÌNH AN TOÀN";
        out.append(centerLine(loiCamOn, 48)).append("\n");
        
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
}