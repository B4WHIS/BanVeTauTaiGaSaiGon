package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import control.QuanLyVeControl;
import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.HanhKhach;
import entity.NhanVien;
import entity.Ve;

public class LapHoaDonGUI extends JFrame implements ActionListener {
    private final Color MAU_CHINH = new Color(74, 140, 103);
    private final Color MAU_TONGKET = new Color(200, 30, 30);
    private final Color MAU_IN_HOADON = new Color(255, 184, 0); 
    private final Font FONT_TIEU_DE = new Font("Segoe UI", Font.BOLD, 36);
    private final Font FONT_NHI = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    private QuanLyVeControl dataLookupControl = new QuanLyVeControl();
    private List<Ve> danhSachVe;
    private HanhKhach nguoiThanhToan;
    private NhanVien nhanVienLap;
    private ThanhToanGUI previousScreen;
    private JButton btnTroVe;
    private JTextArea txtPreview;
    private final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final NumberFormat CURRENCY = NumberFormat.getInstance(new Locale("vi", "VN"));
    private JButton btnInHoaDon;
    private JButton btnInVe;
 
    
    
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
            String maChoNgoiDB = cn != null ? safe(cn.getMaChoNgoi()) : "N/A";
            String choDisplay = extractDisplaySeat(maChoNgoiDB);
            String gia = ve.getGiaThanhToan() != null ? String.format("%s",
                CURRENCY.format(ve.getGiaThanhToan().setScale(0, RoundingMode.HALF_UP))) : "0";
            sb.append(String.format("%-6s %-20s %-12s %12s\n", maVe, ten, choDisplay, gia + " VNĐ"));
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
                "Mô phỏng Văn bản (Chỉ hiển thị Hóa đơn)",
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
        
        
   
        btnInVe = new JButton("In Vé");
        btnInVe.setFont(FONT_BUTTON);
        btnInVe.setBackground(MAU_CHINH); 
        btnInVe.setForeground(Color.WHITE);
        btnInVe.setPreferredSize(new Dimension(150, 45)); 
        
        btnInHoaDon = new JButton("In hóa đơn");
        btnInHoaDon.setFont(FONT_BUTTON);
        btnInHoaDon.setBackground(MAU_IN_HOADON);
        btnInHoaDon.setForeground(Color.BLACK);
        btnInHoaDon.setPreferredSize(new Dimension(150, 45)); 
      
        btnTroVe = new JButton("Đóng");
        btnTroVe.setFont(FONT_BUTTON);
        btnTroVe.setBackground(new Color(220, 20, 60));
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setPreferredSize(new Dimension(150, 45)); 
        
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        
        
        JPanel pnlLeftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10)); // FlowLayout.LEFT sát lề
        pnlLeftButtons.setOpaque(false);
        pnlLeftButtons.add(btnTroVe); 
        
        JPanel pnlRightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        pnlRightButtons.setOpaque(false);
        pnlRightButtons.add(btnInHoaDon);
        pnlRightButtons.add(btnInVe);
        
        pnlFooter.add(pnlLeftButtons, BorderLayout.WEST);
        pnlFooter.add(pnlRightButtons, BorderLayout.EAST);
        
        root.add(pnlFooter, BorderLayout.SOUTH);
    }

    private String extractDisplaySeat(String maChoNgoi) {
        if (maChoNgoi == null || maChoNgoi.isEmpty()) return ".......";
        int index = maChoNgoi.indexOf('-');
        if (index != -1 && index < maChoNgoi.length() - 1) {
            return maChoNgoi.substring(index + 1);
        }
        return maChoNgoi;
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

        pnlActions.add(btnTroVe);
        pnlActions.add(btnInMoPhong);
        root.add(pnlActions, BorderLayout.SOUTH);
    }

    private void loadDataToPreview() {
        txtPreview.setText(buildInvoiceString());
    }

    private void setListeners() {
        btnInHoaDon.addActionListener(this);
        btnInVe.addActionListener(this);
        btnTroVe.addActionListener(this);
        btnInMoPhong.addActionListener(this);
    }

    private String buildInvoiceString() {
        StringBuilder out = new StringBuilder();
        out.append(centerLine("HÓA ĐƠN THANH TOÁN", 48)).append(System.lineSeparator());
        out.append("Ngày lập: ").append(java.time.LocalDateTime.now().format(DTF)).append("\n");
        out.append("Người thanh toán: ").append(safe(nguoiThanhToan.getHoTen())).append("\n");
        out.append("CCCD: ").append(safe(nguoiThanhToan.getCmndCccd())).append("\n");
        out.append("SĐT: ").append(safe(nguoiThanhToan.getSoDT())).append("\n\n");
        ChuyenTau ct = danhSachVe.get(0).getMaChuyenTau();
        String maTau = ct != null ? safe(ct.getMaTau()) : "N/A";
        String tenTau = maTau;
        try {
            if (!maTau.equals("N/A")) {
                tenTau = dataLookupControl.getTenTau(maTau);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tra cứu tên tàu: " + e.getMessage());
        }
        out.append("Chuyến/Tàu: ").append(tenTau).append("\n");
        out.append("Khởi hành: ").append((ct != null && ct.getThoiGianKhoiHanh()!=null) ?
            ct.getThoiGianKhoiHanh().format(DTF) : "N/A").append("\n\n");
        out.append(String.format("%-6s %-18s %-8s %12s\n", "Mã", "Hành khách", "Chỗ", "Giá (VNĐ)"));
        out.append("--------------------------------------------------------------\n");
        for (Ve v : danhSachVe) {
            String ma = safe(v.getMaVe());
            String ten = v.getMaHanhkhach() != null ? safe(v.getMaHanhkhach().getHoTen()) : "N/A";
            ChoNgoi cn = v.getMaChoNgoi();
            String maChoNgoiDB = cn != null ? safe(cn.getMaChoNgoi()) : "N/A";
            String soGheDisplay = extractDisplaySeat(maChoNgoiDB);
            String gia = v.getGiaThanhToan() != null ? CURRENCY.format(v.getGiaThanhToan()) : "0";
            out.append(String.format("%-6s %-18s %-8s %12s\n", ma, truncate(ten, 18), soGheDisplay, gia));
        }
        out.append(System.lineSeparator());
        BigDecimal tong = computeTongTienTruocVAT();
        BigDecimal vat = tong.multiply(new BigDecimal("0.1")).setScale(0, RoundingMode.HALF_UP);
        BigDecimal total = tong.add(vat);
        out.append(padRight("Tổng tiền (chưa thuế):", 36)).append(String.format("%12s VNĐ\n", CURRENCY.format(tong)));
        out.append(padRight("Thuế VAT (10%):", 36)).append(String.format("%12s VNĐ\n", CURRENCY.format(vat)));
        out.append(padRight("Tổng phải thu:", 36)).append(String.format("%12s VNĐ\n", CURRENCY.format(total)));
        out.append(System.lineSeparator());
        out.append(centerLine("CẢM ƠN QUÝ KHÁCH - CHÚC QUÝ KHÁCH HÀNH TRÌNH AN TOÀN", 48)).append("\n");
        return out.toString();
    }

    private String buildTicketString(Ve ve) {
        if (ve == null) return "KHÔNG CÓ DỮ LIỆU VÉ";
        ChuyenTau ct = ve.getMaChuyenTau();
        HanhKhach hk = ve.getMaHanhkhach();
        ChoNgoi cn = ve.getMaChoNgoi();
        String maVe = safe(ve.getMaVe());
        String maTau = ct != null ? safe(ct.getMaTau()) : "N/A";
        String maLichTrinh = ct != null ? safe(ct.getMaLichTrinh()) : null;
        String tenGaDi = "[GA ĐI]";
        String tenGaDen = "[GA ĐẾN]";
        String tenTau = maTau;
        try {
            if (maLichTrinh != null) {
                tenGaDen = dataLookupControl.getTenGaDen(maLichTrinh);
                tenGaDi = dataLookupControl.getTenGaDi(maLichTrinh);
            }
            if (!maTau.equals("N/A")) {
                tenTau = dataLookupControl.getTenTau(maTau);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tra cứu CSDL khi in vé: " + e.getMessage());
            tenGaDen = "[LỖI TẢI GA]";
            tenTau = maTau;
        }
        String ngayDi = (ct != null && ct.getThoiGianKhoiHanh() != null)
                        ? ct.getThoiGianKhoiHanh().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "..................";
        String gioDi = (ct != null && ct.getThoiGianKhoiHanh() != null)
                       ? ct.getThoiGianKhoiHanh().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                       : ".........";
        String maToa = cn != null && cn.getToaTau() != null ? safe(cn.getToaTau().getMaToa()) : "......";
        String maChoNgoiDB = cn != null ? safe(cn.getMaChoNgoi()) : ".......";
        String soGheDisplay = extractDisplaySeat(maChoNgoiDB);
        String loaiVe = (ve.getMaKhuyenMai() != null) ? "Vé KM" : "Vé Thường";
        String loaiCho = "[Ghế/Giường]";
        if (cn != null && dataLookupControl != null) {
            try {
                int idLoaiGhe = cn.getIDloaiGhe();
                loaiCho = dataLookupControl.getTenLoaiGhe(idLoaiGhe);
            } catch (SQLException e) {
                System.err.println("Lỗi tra cứu Tên Loại Ghế: " + e.getMessage());
                loaiCho = "[LỖI TẢI LOẠI GHẾ]";
            }
        }
        String hoTen = hk != null ? truncate(safe(hk.getHoTen()), 25) : ".........................";
        String giayTo = hk != null ? safe(hk.getCmndCccd()) : ".........................";
        String gia = ve.getGiaThanhToan() != null
                     ? CURRENCY.format(ve.getGiaThanhToan().setScale(0, RoundingMode.HALF_UP))
                     : "..................";
        StringBuilder out = new StringBuilder();
        final int FIXED_WIDTH = 50;
        final int LABEL_WIDTH = 25;
        out.append(centerLine("GA SÀI GÒN", FIXED_WIDTH)).append("\n");
        out.append(centerLine("THẺ LÊN TÀU HỎA", FIXED_WIDTH)).append("\n");
        out.append(centerLine("==============================================", FIXED_WIDTH)).append("\n");
        out.append(padRight("Mã vé:", LABEL_WIDTH)).append(maVe).append("\n");
        out.append("--------------------------------------------------\n");
        String headerGa = String.format("%-25s %s\n", "Ga đi", "Ga đến");
        out.append(headerGa);
        String lineGa = String.format("%-25s %s\n",
            " " + truncate(tenGaDi, 18),
            " " + truncate(tenGaDen, 18));
        out.append(lineGa).append("\n");
        out.append(padRight("Tàu/Train:", LABEL_WIDTH)).append(tenTau).append("\n");
        out.append(padRight("Ngày đi/Date:", LABEL_WIDTH)).append(ngayDi).append("\n");
        out.append(padRight("Giờ đi/Time:", LABEL_WIDTH)).append(gioDi).append("\n");
        String toaCho = String.format("%s%-18s %s%s",
            "Toa/Coach:", maToa,
            "Chỗ/Seat:", soGheDisplay);
        out.append(toaCho).append("\n");
        out.append(padRight("Loại vé/Ticket:", LABEL_WIDTH)).append(loaiVe).append("\n");
        out.append(padRight("Loại chỗ/Class:", LABEL_WIDTH)).append(loaiCho).append("\n");
        out.append("--------------------------------------------------\n");
        out.append(padRight("Họ tên/Name:", LABEL_WIDTH)).append(hoTen).append("\n");
        out.append(padRight("Giấy tờ/Passport:", LABEL_WIDTH)).append(truncate(giayTo, 25)).append("\n");
        out.append(padRight("Giá/Price:", LABEL_WIDTH)).append(String.format("%s VNĐ", gia)).append("\n");
        out.append(padRight("Ghi chú:", LABEL_WIDTH)).append("").append("\n");
        out.append(centerLine("==============================================", FIXED_WIDTH)).append("\n");
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
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                try {
                    new NhanVienBanVeGUI(nhanVienLap).setVisible(true); 
                } catch (IOException e1) {
                    System.err.println("Lỗi khi quay lại màn hình chính: " + e1.getMessage());
                }
            });
        } else if (src == btnInHoaDon) {
            String invoiceContent = buildInvoiceString();
            printContent(invoiceContent, "Hóa đơn thanh toán");
        } else if (src == btnInVe) {
            if (danhSachVe == null || danhSachVe.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có vé nào để in.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            StringBuilder allTicketsContent = new StringBuilder();
            int ticketCount = 1;
            for (Ve ve : danhSachVe) {
                allTicketsContent.append("=============== VÉ THỨ ").append(ticketCount++).append(" / ").append(danhSachVe.size()).append(" ===============\n\n");
                allTicketsContent.append(buildTicketString(ve));
                allTicketsContent.append("\n\n--------------------------------------------------\n\n");
            }
            printContent(allTicketsContent.toString(), "Tất cả vé");
        }
    }

    private void printContent(String content, String title) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new TextPrintable(content));
        job.setJobName(title);
        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this, "Đã gửi lệnh in đến máy in.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi in: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "In đã bị hủy.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    class TextPrintable implements Printable {
        private final String content;
        private final Font font;
        private final int linesPerPage;
        private final String[] lines;

        public TextPrintable(String content) {
            this.content = content != null ? content : "";
            this.font = new Font("Courier New", Font.PLAIN, 12);
            this.lines = this.content.split("\r?\n|\r");
            FontMetrics fm = new JPanel().getFontMetrics(font);
            int lineHeight = fm.getHeight();
            double pageHeight = 11.0 * 72; // A4 ~ 11 inch
            double margin = 0.75 * 72; // lề 0.75 inch
            this.linesPerPage = (int) ((pageHeight - 2 * margin) / lineHeight) - 1;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex >= (lines.length + linesPerPage - 1) / linesPerPage) {
                return NO_SUCH_PAGE;
            }
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setFont(font);
            FontMetrics metrics = g2d.getFontMetrics();
            int lineHeight = metrics.getHeight();
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            if (pageFormat.getOrientation() == PageFormat.LANDSCAPE) {
                g2d.rotate(Math.PI / 2);
                g2d.translate(0, -pageFormat.getImageableWidth());
            }
            int startLine = pageIndex * linesPerPage;
            int endLine = Math.min(startLine + linesPerPage, lines.length);
            int y = lineHeight;
            for (int i = startLine; i < endLine; i++) {
                if (y > pageFormat.getImageableHeight() - lineHeight) {
                    break;
                }
                g2d.drawString(lines[i], 10, y);
                y += lineHeight;
            }
            return PAGE_EXISTS;
        }
    }
}