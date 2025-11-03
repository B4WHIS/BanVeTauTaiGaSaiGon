package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
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
import entity.ToaTau;
import entity.Ve;

public class GiaoDienLapHoaDon extends JFrame implements ActionListener {
    
    private QuanLyVeControl dataLookupControl = new QuanLyVeControl(); // để tra tên tàu, ga
    private List<Ve> danhSachVe;
    private HanhKhach nguoiThanhToan;
    private NhanVien nhanVienLap;
    private GiaoDienThanhToan previousScreen;
    
    private JButton btnTroVe, btnInHoaDon, btnInVe;
    private JTextArea txtPreview; // ô xem trước hóa đơn

    private final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final NumberFormat CURRENCY = NumberFormat.getInstance(new Locale("vi", "VN"));


 
    


    public GiaoDienLapHoaDon(List<Ve> danhSachVe, HanhKhach nguoiThanhToan, NhanVien nhanVienLap, GiaoDienThanhToan previous) {
        if (danhSachVe == null || danhSachVe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có vé để lập hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.danhSachVe = danhSachVe;
        this.nguoiThanhToan = nguoiThanhToan != null ? nguoiThanhToan : new HanhKhach();
        this.nhanVienLap = nhanVienLap != null ? nhanVienLap : new NhanVien("NV-000");
        this.previousScreen = previous;

        // thiết lập cửa sổ
        setTitle("HÓA ĐƠN THANH TOÁN");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        // tạo panel chính
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        getContentPane().add(root);

        // tiêu đề lớn ở trên
        JLabel lblTitle = new JLabel("HÓA ĐƠN THANH TOÁN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(new Color(74, 140, 103));
        root.add(lblTitle, BorderLayout.NORTH);

        // chia 2 cột: trái là chi tiết, phải là xem trước
        JPanel center = new JPanel(new GridLayout(1, 2, 12, 12));
        root.add(center, BorderLayout.CENTER);

        // === PHẦN TRÁI: CHI TIẾT HÓA ĐƠN ===
        JPanel left = new JPanel(new GridBagLayout());
        left.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(74, 140, 103), 2),
                "Chi tiết hóa đơn",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), new Color(74, 140, 103)
        ));
        center.add(left);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // thông tin người thanh toán
        JPanel pnlInfo = new JPanel(new GridLayout(6, 1, 6, 6));
        pnlInfo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        pnlInfo.add(taoLabel("Người thanh toán: " + safe(nguoiThanhToan.getHoTen())));
        pnlInfo.add(taoLabel("CCCD: " + safe(nguoiThanhToan.getCmndCccd())));
        pnlInfo.add(taoLabel("SĐT: " + safe(nguoiThanhToan.getSoDT())));
        
        ChuyenTau ct = danhSachVe.get(0).getMaChuyenTau();
        String chuyen = ct != null ? safe(ct.getMaChuyenTau()) : "N/A";
        String thoiGian = (ct != null && ct.getThoiGianKhoiHanh() != null) ? ct.getThoiGianKhoiHanh().format(DTF) : "N/A";
        pnlInfo.add(taoLabel("Chuyến/Tàu: " + chuyen));
        pnlInfo.add(taoLabel("Thời gian khởi hành: " + thoiGian));
        pnlInfo.add(taoLabel("Nhân viên lập: " + safe(nhanVienLap.getHoTen()) + " (" + safe(nhanVienLap.getMaNhanVien()) + ")"));
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        left.add(pnlInfo, gbc);

        // danh sách vé
        JPanel pnlVeList = new JPanel(new BorderLayout(6, 6));
        pnlVeList.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(74, 140, 103)),
                "Danh sách vé",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), new Color(74, 140, 103)
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
            String choDisplay = laySoGhe(maChoNgoiDB);
            String gia = ve.getGiaThanhToan() != null ? CURRENCY.format(ve.getGiaThanhToan().setScale(0, RoundingMode.HALF_UP)) : "0";
            sb.append(String.format("%-6s %-20s %-12s %12s\n", maVe, catTen(ten, 20), choDisplay, gia + " VNĐ"));
        }
        txtTickets.setText(sb.toString());
        txtTickets.setCaretPosition(0);
        pnlVeList.add(new JScrollPane(txtTickets), BorderLayout.CENTER);
        
        gbc.gridy = 1; gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        left.add(pnlVeList, gbc);

        // tổng kết tiền
        gbc.gridy = 2; gbc.weighty = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel pnlTotals = new JPanel(new GridLayout(4, 1, 6, 6));
        pnlTotals.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        BigDecimal tongTruocVAT = tinhTongTien();
        BigDecimal thueVAT = tongTruocVAT.multiply(new BigDecimal("0.1")).setScale(0, RoundingMode.HALF_UP);
        BigDecimal tongPhaiThu = tongTruocVAT.add(thueVAT);
        
        pnlTotals.add(taoLabelMau("Tổng tiền vé (chưa thuế): " + CURRENCY.format(tongTruocVAT) + " VNĐ"));
        pnlTotals.add(taoLabelMau("Thuế VAT (10%): " + CURRENCY.format(thueVAT) + " VNĐ"));
        pnlTotals.add(taoLabelMau("Tổng phải thu: " + CURRENCY.format(tongPhaiThu) + " VNĐ", new Color(200, 30, 30)));
        pnlTotals.add(taoLabel("Ghi chú: Hóa đơn mô phỏng - không có giá trị pháp lý."));
        
        left.add(pnlTotals, gbc);

        // === PHẦN PHẢI: XEM TRƯỚC HÓA ĐƠN ===
        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(74, 140, 103), 2),
                "Mô phỏng Văn bản (Chỉ hiển thị Hóa đơn)",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), new Color(74, 140, 103)
        ));
        center.add(right);

        txtPreview = new JTextArea();
        txtPreview.setEditable(false);
        txtPreview.setFont(new Font("Courier New", Font.PLAIN, 13));
        txtPreview.setBackground(Color.WHITE);
        txtPreview.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        right.add(new JScrollPane(txtPreview), BorderLayout.CENTER);

        // === NÚT CHỨC NĂNG DƯỚI CÙNG ===
        btnInVe = new JButton("In Vé");
        btnInVe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnInVe.setBackground(new Color(74, 140, 103));
        btnInVe.setForeground(Color.WHITE);
        btnInVe.setPreferredSize(new Dimension(150, 45));

        btnInHoaDon = new JButton("In hóa đơn");
        btnInHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnInHoaDon.setBackground(new Color(255, 184, 0));
        btnInHoaDon.setForeground(Color.BLACK);
        btnInHoaDon.setPreferredSize(new Dimension(150, 45));

        btnTroVe = new JButton("Đóng");
        btnTroVe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTroVe.setBackground(new Color(220, 20, 60));
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setPreferredSize(new Dimension(150, 45));

        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        JPanel pnlLeftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        pnlLeftButtons.setOpaque(false);
        pnlLeftButtons.add(btnTroVe);

        JPanel pnlRightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        pnlRightButtons.setOpaque(false);
        pnlRightButtons.add(btnInHoaDon);
        pnlRightButtons.add(btnInVe);

        pnlFooter.add(pnlLeftButtons, BorderLayout.WEST);
        pnlFooter.add(pnlRightButtons, BorderLayout.EAST);
        root.add(pnlFooter, BorderLayout.SOUTH);

        // gán sự kiện
        btnInHoaDon.addActionListener(this);
        btnInVe.addActionListener(this);
        btnTroVe.addActionListener(this);

        // load nội dung xem trước
        txtPreview.setText(taoNoiDungHoaDon());
        txtPreview.setCaretPosition(0);
    }

    // tạo label thông thường
    private JLabel taoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }

    // tạo label có màu
    private JLabel taoLabelMau(String text) {
        return taoLabelMau(text, null);
    }

    private JLabel taoLabelMau(String text, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (color != null) lbl.setForeground(color);
        return lbl;
    }

    // tránh null
    private String safe(String s) {
        return s == null ? "N/A" : s;
    }

    // lấy số ghế (bỏ mã toa)
    private String laySoGhe(String maChoNgoi) {
        if (maChoNgoi == null || maChoNgoi.isEmpty()) return ".......";
        int index = maChoNgoi.indexOf('-');
        if (index != -1 && index < maChoNgoi.length() - 1) {
            return maChoNgoi.substring(index + 1);
        }
        return maChoNgoi;
    }

    // cắt tên nếu quá dài
    private String catTen(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max - 3) + "...";
    }

    // tính tổng tiền
    private BigDecimal tinhTongTien() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Ve v : danhSachVe) {
            if (v.getGiaThanhToan() != null) {
                sum = sum.add(v.getGiaThanhToan());
            }
        }
        return sum.setScale(0, RoundingMode.HALF_UP);
    }

    // tạo nội dung hóa đơn để xem trước
    private String taoNoiDungHoaDon() {
        StringBuilder out = new StringBuilder();
        out.append(canGiua("HÓA ĐƠN THANH TOÁN", 48)).append("\n");
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
            tenTau = maTau;
        }
        out.append("Chuyến/Tàu: ").append(tenTau).append("\n");
        out.append("Khởi hành: ").append((ct != null && ct.getThoiGianKhoiHanh() != null) ?
                ct.getThoiGianKhoiHanh().format(DTF) : "N/A").append("\n\n");

        out.append(String.format("%-6s %-18s %-8s %12s\n", "Mã", "Hành khách", "Chỗ", "Giá (VNĐ)"));
        out.append("--------------------------------------------------------------\n");
        for (Ve v : danhSachVe) {
            String ma = safe(v.getMaVe());
            String ten = v.getMaHanhkhach() != null ? safe(v.getMaHanhkhach().getHoTen()) : "N/A";
            ChoNgoi cn = v.getMaChoNgoi();
            String maChoNgoiDB = cn != null ? safe(cn.getMaChoNgoi()) : "N/A";
            String soGhe = laySoGhe(maChoNgoiDB);
            String gia = v.getGiaThanhToan() != null ? CURRENCY.format(v.getGiaThanhToan()) : "0";
            out.append(String.format("%-6s %-18s %-8s %12s\n", ma, catTen(ten, 18), soGhe, gia));
        }
        out.append("\n");

        BigDecimal tong = tinhTongTien();
        BigDecimal vat = tong.multiply(new BigDecimal("0.1")).setScale(0, RoundingMode.HALF_UP);
        BigDecimal total = tong.add(vat);
        out.append(canPhai("Tổng tiền (chưa thuế):", 36)).append(String.format("%12s VNĐ\n", CURRENCY.format(tong)));
        out.append(canPhai("Thuế VAT (10%):", 36)).append(String.format("%12s VNĐ\n", CURRENCY.format(vat)));
        out.append(canPhai("Tổng phải thu:", 36)).append(String.format("%12s VNĐ\n", CURRENCY.format(total)));
        out.append("\n");
        out.append(canGiua("CẢM ƠN QUÝ KHÁCH - CHÚC QUÝ KHÁCH HÀNH TRÌNH AN TOÀN", 48)).append("\n");
        return out.toString();
    }

    // căn giữa dòng
    private String canGiua(String s, int width) {
        if (s == null) s = "";
        if (s.length() >= width) return s + "\n";
        int left = (width - s.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < left; i++) sb.append(" ");
        sb.append(s);
        return sb.toString();
    }

    // căn phải
    private String canPhai(String s, int width) {
        if (s == null) s = "";
        if (s.length() >= width) return s;
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < width) sb.append(' ');
        return sb.toString();
    }

    // xử lý nút
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnTroVe) {
            this.dispose();
            // quay lại màn hình chính
            SwingUtilities.invokeLater(() -> {
                try {
                    new MHC_NhanVienBanVe(nhanVienLap).setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi quay lại: " + ex.getMessage());
                }
            });
        } else if (src == btnInHoaDon) {
            inNoiDung(taoNoiDungHoaDon(), "Hóa đơn thanh toán");
        } else if (src == btnInVe) {
            if (danhSachVe.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có vé để in!");
                return;
            }
            StringBuilder all = new StringBuilder();
            int stt = 1;
            for (Ve ve : danhSachVe) {
                all.append("=============== VÉ THỨ ").append(stt++).append(" / ").append(danhSachVe.size()).append(" ===============\n\n");
                all.append(taoNoiDungVe(ve));
                all.append("\n\n--------------------------------------------------\n\n");
            }
            inNoiDung(all.toString(), "Tất cả vé");
        }
    }

    // tạo nội dung vé
    private String taoNoiDungVe(Ve ve) {
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
            tenGaDen = "[LỖI]";
            tenTau = maTau;
        }
        String ngayDi = (ct != null && ct.getThoiGianKhoiHanh() != null)
                ? ct.getThoiGianKhoiHanh().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "..................";
        String gioDi = (ct != null && ct.getThoiGianKhoiHanh() != null)
                ? ct.getThoiGianKhoiHanh().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                : ".........";
        String maToa = cn != null && cn.getToaTau() != null ? safe(cn.getToaTau().getMaToa()) : "......";
        String soGhe = laySoGhe(safe(cn != null ? cn.getMaChoNgoi() : ""));
        String loaiVe = (ve.getMaKhuyenMai() != null) ? "Vé KM" : "Vé Thường";
        String loaiCho = "[Ghế/Giường]";
        if (cn != null) {
            try {
                int idLoaiGhe = cn.getIDloaiGhe();
                loaiCho = dataLookupControl.getTenLoaiGhe(idLoaiGhe);
            } catch (SQLException e) {
                loaiCho = "[LỖI]";
            }
        }
        String hoTen = hk != null ? catTen(safe(hk.getHoTen()), 25) : ".........................";
        String giayTo = hk != null ? safe(hk.getCmndCccd()) : ".........................";
        String gia = ve.getGiaThanhToan() != null
                ? CURRENCY.format(ve.getGiaThanhToan().setScale(0, RoundingMode.HALF_UP))
                : "..................";

        StringBuilder out = new StringBuilder();
        final int WIDTH = 50;
        final int LABEL = 25;
        out.append(canGiua("GA SÀI GÒN", WIDTH)).append("\n");
        out.append(canGiua("THẺ LÊN TÀU HỎA", WIDTH)).append("\n");
        out.append(canGiua("==============================================", WIDTH)).append("\n");
        out.append(canPhai("Mã vé:", LABEL)).append(maVe).append("\n");
        out.append("--------------------------------------------------\n");
        out.append(String.format("%-25s %s\n", "Ga đi", "Ga đến"));
        out.append(String.format("%-25s %s\n", " " + catTen(tenGaDi, 18), " " + catTen(tenGaDen, 18))).append("\n");
        out.append(canPhai("Tàu/Train:", LABEL)).append(tenTau).append("\n");
        out.append(canPhai("Ngày đi/Date:", LABEL)).append(ngayDi).append("\n");
        out.append(canPhai("Giờ đi/Time:", LABEL)).append(gioDi).append("\n");
        out.append(String.format("%s%-18s %s%s", "Toa/Coach:", maToa, "Chỗ/Seat:", soGhe)).append("\n");
        out.append(canPhai("Loại vé/Ticket:", LABEL)).append(loaiVe).append("\n");
        out.append(canPhai("Loại chỗ/Class:", LABEL)).append(loaiCho).append("\n");
        out.append("--------------------------------------------------\n");
        out.append(canPhai("Họ tên/Name:", LABEL)).append(hoTen).append("\n");
        out.append(canPhai("Giấy tờ/Passport:", LABEL)).append(catTen(giayTo, 25)).append("\n");
        out.append(canPhai("Giá/Price:", LABEL)).append(String.format("%s VNĐ", gia)).append("\n");
        out.append(canGiua("==============================================", WIDTH)).append("\n");
        return out.toString();
    }

    // in nội dung
    private void inNoiDung(String content, String title) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new InVanBan(content));
        job.setJobName(title);
        if (job.printDialog()) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this, "Đã gửi lệnh in!");
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi in: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "In đã bị hủy.");
        }
    }

    // lớp in văn bản
    class InVanBan implements Printable {
        private final String content;
        private final Font font = new Font("Courier New", Font.PLAIN, 12);
        private final String[] lines;

        public InVanBan(String content) {
            this.content = content != null ? content : "";
            this.lines = this.content.split("\r?\n|\r");
        }

        @Override
        public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
            if (pageIndex > 0) return NO_SUCH_PAGE;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(font);
            g2d.translate(pf.getImageableX(), pf.getImageableY());
            int y = 50;
            for (String line : lines) {
                if (y > pf.getImageableHeight() - 50) break;
                g2d.drawString(line, 50, y);
                y += 15;
            }
            return PAGE_EXISTS;
        }
    }
    public static void main(String[] args) {
        // chạy giao diện trên luồng EDT
        SwingUtilities.invokeLater(() -> {
            try {
                // tạo dữ liệu giả
                List<Ve> danhSachVe = taoDanhSachVeGia();
                HanhKhach nguoiThanhToan = taoNguoiThanhToan();
                NhanVien nhanVienLap = taoNhanVien();
                GiaoDienThanhToan previous = null; // không cần màn hình trước

                // mở màn hình hóa đơn
                GiaoDienLapHoaDon hoaDon = new GiaoDienLapHoaDon(
                        danhSachVe, nguoiThanhToan, nhanVienLap, previous
                );
                hoaDon.setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    private static List<Ve> taoDanhSachVeGia() {
        List<Ve> ds = new ArrayList<>();

        // chuyến tàu giả
        ChuyenTau chuyenTau = new ChuyenTau();
        chuyenTau.setMaChuyenTau("CT001");
        chuyenTau.setMaTau("SE1");
        chuyenTau.setMaLichTrinh("LT001");
        chuyenTau.setThoiGianKhoiHanh(LocalDateTime.of(2025, 11, 5, 6, 30));

        // ghế 1
        ChoNgoi ghe1 = new ChoNgoi();
        ghe1.setMaChoNgoi("TOA01-A01");
        ToaTau toa1 = new ToaTau();
        toa1.setMaToa("TOA01");
        ghe1.setToaTau(toa1);
        ghe1.setIDloaiGhe(1); // ghế ngồi

        // hành khách 1
        HanhKhach hk1 = new HanhKhach();
        hk1.setHoTen("Nguyễn Văn A");
        hk1.setCmndCccd("123456789");
        hk1.setSoDT("0901234567");

        // vé 1
        Ve ve1 = new Ve();
        ve1.setMaVe("VE0001");
        ve1.setMaChuyenTau(chuyenTau);
        ve1.setMaChoNgoi(ghe1);
        ve1.setMaHanhkhach(hk1);
        ve1.setGiaThanhToan(new BigDecimal("850000"));

        // ghế 2
        ChoNgoi ghe2 = new ChoNgoi();
        ghe2.setMaChoNgoi("TOA01-A02");
        ToaTau toa2 = new ToaTau();
        toa2.setMaToa("TOA01");
        ghe2.setToaTau(toa2);
        ghe2.setIDloaiGhe(2); // giường nằm

        HanhKhach hk2 = new HanhKhach();
        hk2.setHoTen("Trần Thị B");
        hk2.setCmndCccd("987654321");
        hk2.setSoDT("0919876543");

        Ve ve2 = new Ve();
        ve2.setMaVe("VE0002");
        ve2.setMaChuyenTau(chuyenTau);
        ve2.setMaChoNgoi(ghe2);
        ve2.setMaHanhkhach(hk2);
        ve2.setGiaThanhToan(new BigDecimal("1200000"));

        // thêm vào danh sách
        ds.add(ve1);
        ds.add(ve2);

        return ds;
    }

    // tạo người thanh toán
    private static HanhKhach taoNguoiThanhToan() {
        HanhKhach hk = new HanhKhach();
        hk.setHoTen("Lê Văn C");
        hk.setCmndCccd("111222333");
        hk.setSoDT("0987654321");
        return hk;
    }

    // tạo nhân viên lập
    private static NhanVien taoNhanVien() {
        NhanVien nv = new NhanVien("NV001");
        nv.setHoTen("Phạm Thị Nhân Viên");
        return nv;
    }
}