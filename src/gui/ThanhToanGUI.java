package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

// Giả định các lớp Control và Entity đã được import đúng
import control.QuanLyHoaDonControl;
import dao.VeDAO;
import entity.ChuyenTau;
import entity.HanhKhach;
import entity.HoaDon;
import entity.NhanVien;
import entity.Ve;

public class ThanhToanGUI extends JFrame implements ActionListener {

    // Constants (Dựa trên [2])
    private final Color mauChinh = new Color(74, 140, 103);
    private final Color mauTongKet = new Color(200, 30, 30);
    private final Color MAU_NUT_QUAYLAI = new Color(220, 20, 60);
    private final Color MAU_NUT_THANHTOAN = new Color(103, 192, 144);
    private final Font phongChuGiaTri = new Font("Segoe UI", Font.PLAIN, 15);
    private final Font phongChuTongKet = new Font("Segoe UI", Font.BOLD, 22);

    // Components (Sử dụng tiền tố chuẩn [1, 3])
    private JTextField txtTenKhachHang, txtCmndPayer, txtSdtPayer;
    private JTextField txtMaKhuyenMai;
    private JTextField txtTongTienVeGoc;
    private JTextField txtThueVAT;
    private JTextField txtTongThanhToanCuoi;
    private JTable tblChiTiet;
    private DefaultTableModel moHinhBang;
    private JButton btnQuayLai, btnThanhToan, btnApDungMa;
    private JPanel pnlNhapLieu; // Panel chứa form nhập liệu

    // Data Holders
    private List<Ve> danhSachVe;
    private HanhKhach nguoiThanhToan;
    private NhanVien nhanVienLap;
    private QuanLyHoaDonControl hdControl = new QuanLyHoaDonControl();
    private VeDAO vedao = new VeDAO(); // DAO để lưu vé
    private BigDecimal tongTienTruocVAT = BigDecimal.ZERO;
    private ChuyenTau chuyenTauDuocChon;
    private boolean laDoiVe = false;
    
    public ThanhToanGUI(List<Ve> danhSachVe, HanhKhach nguoiThanhToan, NhanVien nv) {
        if (danhSachVe == null || danhSachVe.isEmpty()) {
            throw new IllegalArgumentException("Danh sách vé không được rỗng.");
        }
        this.danhSachVe = danhSachVe;
        this.nguoiThanhToan = nguoiThanhToan != null ? nguoiThanhToan : new HanhKhach();
        this.nhanVienLap = nv != null ? nv : new NhanVien("NV-001");

        
        this.laDoiVe = danhSachVe.size() == 1 
                       && danhSachVe.get(0).getTrangThai() != null 
                       && danhSachVe.get(0).getTrangThai().contains("Đã đổi");

        khoiTaoGiaoDien();
        preloadThongTinKhachHang();
        hienThiChiTietVe();
        tinhToanTongTien();         
        thietLapSuKien();
    }

    private void khoiTaoGiaoDien() {
        setTitle("Thanh toán");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JPanel pnlChinh = new JPanel(new BorderLayout(12, 12));
        pnlChinh.setBorder(new EmptyBorder(12, 12, 12, 12));
        getContentPane().add(pnlChinh);

        JLabel lblTieuDe = new JLabel("THANH TOÁN", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTieuDe.setForeground(mauChinh);
        pnlChinh.add(lblTieuDe, BorderLayout.NORTH);

        pnlNhapLieu = new JPanel(new GridBagLayout());
        pnlNhapLieu.setPreferredSize(new Dimension(420, 0));
        TitledBorder tb = BorderFactory.createTitledBorder("THÔNG TIN THANH TOÁN");
        tb.setTitleFont(new Font("Segoe UI", Font.BOLD, 20));
        tb.setTitleColor(mauChinh);
        pnlNhapLieu.setBorder(tb);
        pnlChinh.add(pnlNhapLieu, BorderLayout.WEST);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel lblTenKH = new JLabel("Họ tên:");
        lblTenKH.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = row;
        pnlNhapLieu.add(lblTenKH, gbc);

        txtTenKhachHang = new JTextField();
        txtTenKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.weightx = 1;
        pnlNhapLieu.add(txtTenKhachHang, gbc);

        row++;
        JLabel lblCmnd = new JLabel("CMND/CCCD:");
        lblCmnd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = row;
        pnlNhapLieu.add(lblCmnd, gbc);

        txtCmndPayer = new JTextField();
        txtCmndPayer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.weightx = 1;
        pnlNhapLieu.add(txtCmndPayer, gbc);

        row++;
        JLabel lblSdt = new JLabel("SĐT:");
        lblSdt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = row;
        pnlNhapLieu.add(lblSdt, gbc);

        txtSdtPayer = new JTextField();
        txtSdtPayer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.weightx = 1;
        pnlNhapLieu.add(txtSdtPayer, gbc);

        row++;
        JLabel lblMaKM = new JLabel("Mã khuyến mãi:");
        lblMaKM.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = row;
        pnlNhapLieu.add(lblMaKM, gbc);

        JPanel pnlMaKM = new JPanel(new BorderLayout(5, 0));
        txtMaKhuyenMai = new JTextField();
        txtMaKhuyenMai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlMaKM.add(txtMaKhuyenMai, BorderLayout.CENTER);

        btnApDungMa = new JButton("Áp dụng");
        btnApDungMa.setBackground(mauChinh);
        btnApDungMa.setForeground(Color.WHITE);
        btnApDungMa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlMaKM.add(btnApDungMa, BorderLayout.EAST);

        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1;
        pnlNhapLieu.add(pnlMaKM, gbc);

        JPanel pnlTongKet = new JPanel(new GridBagLayout());
        TitledBorder tbTongKet = BorderFactory.createTitledBorder("TỔNG KẾT");
        tbTongKet.setTitleFont(new Font("Segoe UI", Font.BOLD, 20));
        tbTongKet.setTitleColor(mauTongKet);
        pnlTongKet.setBorder(tbTongKet);
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2; gbc.weightx = 1;
        pnlNhapLieu.add(pnlTongKet, gbc);

        GridBagConstraints gbcTongKet = new GridBagConstraints();
        gbcTongKet.insets = new Insets(4, 4, 4, 4);
        gbcTongKet.anchor = GridBagConstraints.WEST;
        gbcTongKet.fill = GridBagConstraints.HORIZONTAL;

        int rowTongKet = 0;
        JLabel lblTongVeGoc = new JLabel("Tổng tiền vé gốc:");
        lblTongVeGoc.setFont(phongChuGiaTri);
        gbcTongKet.gridx = 0; gbcTongKet.gridy = rowTongKet;
        pnlTongKet.add(lblTongVeGoc, gbcTongKet);

        txtTongTienVeGoc = new JTextField();
        txtTongTienVeGoc.setEditable(false);
        txtTongTienVeGoc.setFont(phongChuGiaTri);
        gbcTongKet.gridx = 1; gbcTongKet.weightx = 1;
        pnlTongKet.add(txtTongTienVeGoc, gbcTongKet);

        rowTongKet++;
        JLabel lblThueVAT = new JLabel("Thuế VAT (10%):");
        lblThueVAT.setFont(phongChuGiaTri);
        gbcTongKet.gridx = 0; gbcTongKet.gridy = rowTongKet;
        pnlTongKet.add(lblThueVAT, gbcTongKet);

        txtThueVAT = new JTextField();
        txtThueVAT.setEditable(false);
        txtThueVAT.setFont(phongChuGiaTri);
        gbcTongKet.gridx = 1; gbcTongKet.weightx = 1;
        pnlTongKet.add(txtThueVAT, gbcTongKet);

        rowTongKet++;
        JLabel lblTongCuoi = new JLabel("TỔNG THANH TOÁN:");
        lblTongCuoi.setFont(phongChuTongKet);
        gbcTongKet.gridx = 0; gbcTongKet.gridy = rowTongKet;
        pnlTongKet.add(lblTongCuoi, gbcTongKet);

        txtTongThanhToanCuoi = new JTextField();
        txtTongThanhToanCuoi.setEditable(false);
        txtTongThanhToanCuoi.setFont(phongChuTongKet);
        gbcTongKet.gridx = 1; gbcTongKet.weightx = 1;
        pnlTongKet.add(txtTongThanhToanCuoi, gbcTongKet);

        JPanel pnlChiTiet = new JPanel(new BorderLayout());
        pnlChiTiet.setBorder(BorderFactory.createTitledBorder("CHI TIẾT VÉ"));
        pnlChinh.add(pnlChiTiet, BorderLayout.CENTER);

        String[] cot = {"STT", "Mã vé", "Thông tin vé", "Thành tiền"};
        moHinhBang = new DefaultTableModel(cot, 0);
        tblChiTiet = new JTable(moHinhBang);
        JScrollPane sc = new JScrollPane(tblChiTiet);
        pnlChiTiet.add(sc, BorderLayout.CENTER);

        JPanel pnlNutChucNang = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnQuayLai = new JButton("Quay lại", new ImageIcon(getClass().getResource("/img/reload.png")));
        btnQuayLai.setBackground(MAU_NUT_QUAYLAI);
        btnQuayLai.setForeground(Color.WHITE);
        btnQuayLai.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnThanhToan = new JButton("Thanh toán", new ImageIcon(getClass().getResource("/img/plus.png")));
        btnThanhToan.setBackground(MAU_NUT_THANHTOAN);
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 14));

        pnlNutChucNang.add(btnQuayLai);
        pnlNutChucNang.add(btnThanhToan);
        pnlChinh.add(pnlNutChucNang, BorderLayout.SOUTH);
    }
 // Preload thông tin khách hàng nếu là đổi vé
    private void preloadThongTinKhachHang() {
        if (laDoiVe && nguoiThanhToan != null) {
            txtTenKhachHang.setText(nguoiThanhToan.getHoTen());
            txtCmndPayer.setText(nguoiThanhToan.getCmndCccd());
            txtSdtPayer.setText(nguoiThanhToan.getSoDT());

            // Disable edit nếu là đổi vé
            txtTenKhachHang.setEditable(false);
            txtCmndPayer.setEditable(false);
            txtSdtPayer.setEditable(false);
        }
    }
    private void hienThiChiTietVe() {
        moHinhBang.setRowCount(0);
        int stt = 1;
        for (Ve ve : danhSachVe) {
            String thongTin = laDoiVe 
                ? "Đổi vé: " + ve.getMaVe() + " → Ghế mới: " + ve.getMaChoNgoi().getMaChoNgoi()
                : "Chuyến: " + ve.getMaChuyenTau().getMaChuyenTau() + 
                  "\nGhế: " + ve.getMaChoNgoi().getMaChoNgoi();

            Object[] dong = {
                    stt++,
                    ve.getMaVe(),
                    thongTin,
                    formatCurrency(ve.getGiaThanhToan())
            };
            moHinhBang.addRow(dong);
        }
    }

    private void tinhToanTongTien() {
        final BigDecimal VAT_RATE = new BigDecimal("0.1");
        tongTienTruocVAT = danhSachVe.stream()
                .map(Ve::getGiaThanhToan)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal thueVAT = tongTienTruocVAT.multiply(VAT_RATE).setScale(0, RoundingMode.HALF_UP);
        BigDecimal tongPhaiThu = tongTienTruocVAT.add(thueVAT);

        if (laDoiVe) {
            txtTongTienVeGoc.setText("Phí đổi vé: " + formatCurrency(tongTienTruocVAT));
            txtThueVAT.setText("VAT (10%): " + formatCurrency(thueVAT));
            txtTongThanhToanCuoi.setText("TỔNG CỘNG: " + formatCurrency(tongPhaiThu));
        } else {
            txtTongTienVeGoc.setText(formatCurrency(tongTienTruocVAT));
            txtThueVAT.setText(formatCurrency(thueVAT));
            txtTongThanhToanCuoi.setText(formatCurrency(tongPhaiThu));
        }
    }
    private String formatCurrency(BigDecimal amount) {
        return String.format("%,.0f VNĐ", amount.doubleValue());
    }

    private void xuLyThanhToan() {
        System.out.println("Thực hiện thanh toán - Mã NV: " + (nhanVienLap != null ? nhanVienLap.getMaNhanVien() : "null"));

        // DEBUG
        System.out.println("=== DEBUG THANH TOÁN ===");
        System.out.println("danhSachVe = " + danhSachVe);
        if (danhSachVe != null) {
            System.out.println("Số vé: " + danhSachVe.size());
            danhSachVe.forEach(v -> System.out.println("Vé: " + v.getMaVe()));
        }
        System.out.println("========================\n");

        // BẮT LỖI SỚM
        if (danhSachVe == null || danhSachVe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Danh sách vé trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        HoaDon hoaDonDaLap = null;
        try {
            // TRUYỀN ĐÚNG danhSachVe (không phải null)
            hoaDonDaLap = hdControl.lapHoaDon(nguoiThanhToan, nhanVienLap, danhSachVe);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Thanh toán thất bại", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        if (hoaDonDaLap != null) {
            JOptionPane.showMessageDialog(this, "Thanh toán thành công! Mã HD: " + hoaDonDaLap.getMaHoaDon());
            this.dispose();
            // Mở hóa đơn in
            SwingUtilities.invokeLater(() -> {
                new LapHoaDonGUI(danhSachVe, nguoiThanhToan, nhanVienLap, this).setVisible(true);
            });
        }
    }

    private void xuLyApDungKhuyenMai() {
        // Logic áp dụng khuyến mãi (tạm: hiển thị thông báo)
        JOptionPane.showMessageDialog(this, "Chức năng áp dụng mã Khuyến Mãi chưa được tích hợp logic tính toán.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
        java.net.URL iconUrl = ChonChoNgoiGUI.class.getResource(duongDan);
        if (iconUrl == null) {
            System.err.println("Không tìm thấy ảnh: " + duongDan);
            return null; // Trả về null → không set icon
        }
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();
        if (nguon == btnThanhToan) {
            xuLyThanhToan();
        } else if (nguon == btnQuayLai) {
            // Quay lại màn hình trước đó
            this.dispose();
        } else if (nguon == btnApDungMa) {
            xuLyApDungKhuyenMai();
        }
    }

    private void thietLapSuKien() {
        btnThanhToan.addActionListener(this);
        btnQuayLai.addActionListener(this);
        btnApDungMa.addActionListener(this);
    }
}