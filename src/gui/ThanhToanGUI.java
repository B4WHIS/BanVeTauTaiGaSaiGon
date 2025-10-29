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
import java.util.ArrayList;
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
import control.QuanLyVeControl;
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
    private final Color MAU_NUT_QUAYLAI = new Color(0, 128, 255);
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
    private QuanLyVeControl dieuKhienVe = new QuanLyVeControl();
    // Data Holders
    private List<Ve> danhSachVe;
    private HanhKhach nguoiThanhToan;
    private NhanVien nhanVienLap;
    private QuanLyHoaDonControl hdControl = new QuanLyHoaDonControl();
    private VeDAO vedao = new VeDAO(); // DAO để lưu vé
    private BigDecimal tongTienTruocVAT = BigDecimal.ZERO;
    private ChuyenTau chuyenTauDuocChon;
    
    private ThongTinKhachHangGUI previousScreen;
    public ThanhToanGUI(List<Ve> danhSachVe, HanhKhach nguoiThanhToan, NhanVien nv,
    		ThongTinKhachHangGUI previous) {
        if (danhSachVe == null || danhSachVe.isEmpty()) {
            throw new IllegalArgumentException("Danh sách vé không được rỗng.");
        }
       
        if (nguoiThanhToan == null || nv == null) {
            throw new IllegalArgumentException("Thông tin người thanh toán không được rỗng.");
        }
        this.previousScreen = previous;
        this.danhSachVe = danhSachVe;
        this.nhanVienLap = nv;
        this.nguoiThanhToan = nguoiThanhToan;

        for (Ve ve : danhSachVe) {
            ve.setMaHanhkhach(this.nguoiThanhToan);
        }

        if (!danhSachVe.isEmpty()) {
            this.chuyenTauDuocChon = danhSachVe.get(0).getMaChuyenTau();
        }

        setTitle("Thanh Toán Giao Dịch Bán Vé");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        khoiTaoThanhPhan();
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

        pnlBangChiTiet.add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);
        pnlCenter.add(pnlBangChiTiet, BorderLayout.CENTER);

        khungChinh.add(pnlCenter, BorderLayout.CENTER);

        // Footer (Thao tác)
        JPanel pnlThaoTac = taoPanelThaoTac();
        khungChinh.add(pnlThaoTac, BorderLayout.SOUTH);

        getContentPane().add(khungChinh);
    }

    private JPanel taoPanelThaoTac() {
        JPanel pnlThaoTac = new JPanel(new BorderLayout());
        pnlThaoTac.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); 
        
    	pnlThaoTac.setBackground(Color.WHITE);

        // Nút Quay lại
        btnQuayLai = new JButton("Trở về");
        btnQuayLai.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnQuayLai.setForeground(Color.WHITE);
        btnQuayLai.setBackground(MAU_NUT_QUAYLAI);
        btnQuayLai.setForeground(Color.WHITE);
        btnQuayLai.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        pnlLeft.setOpaque(false);
        pnlLeft.add(btnQuayLai);
        
        pnlThaoTac.add(pnlLeft, BorderLayout.WEST); 
        
        // Nút Thanh Toán
        btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setBackground(MAU_NUT_THANHTOAN);
        btnThanhToan.setPreferredSize(new Dimension(180, 50));

        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); 
        pnlRight.setOpaque(false);
        pnlRight.add(btnThanhToan);
        
        pnlThaoTac.add(pnlRight, BorderLayout.EAST); 
        

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
        List<Ve> danhSachVeDaDat = new ArrayList<>();
        try {
            // --- BƯỚC 1: Thực hiện giao dịch đặt vé (ghi vào DB) cho từng vé ---
            for (Ve veChuaMa : danhSachVe) {
                // Chúng ta phải sử dụng hàm datVe CÓ QUẢN LÝ TRANSACTION.
                // datVe sẽ trả về mã vé và cập nhật trạng thái chỗ ngồi.
                
                // Chú ý: Vì datVe trong QuanLyVeControl BAO GỒM logic cập nhật HK
                // (HK đã có mã KH) [11], chúng ta vẫn cần truyền HK vào, nhưng nó sẽ chỉ 
                // CẬP NHẬT HK thay vì tạo mới.
                
                String maVe = dieuKhienVe.datVe(veChuaMa, veChuaMa.getMaHanhkhach(), nhanVienLap); 
                if (maVe == null || maVe.isEmpty()) {
                     throw new Exception("Lỗi CSDL: Không thể đặt vé cho chỗ ngồi: " + veChuaMa.getMaChoNgoi().getMaChoNgoi());
                }
                veChuaMa.setMaVe(maVe);
                danhSachVeDaDat.add(veChuaMa); // Danh sách vé đã có mã
            }
            
            // --- BƯỚC 2: Lập Hóa đơn sau khi tất cả vé đã được đặt thành công ---
            hoaDonDaLap = hdControl.lapHoaDon(nguoiThanhToan, nhanVienLap, danhSachVeDaDat); // [8]

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Thanh toán thất bại",
            JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            
            // !!! QUAN TRỌNG: Nếu BƯỚC 2 (lập hóa đơn) thất bại,
            // các vé vừa được tạo (VE000023, VE000024) và Lịch sử vé đã bị COMMIT 
            // trong BƯỚC 1 vẫn tồn tại (gây ra lỗi mâu thuẫn).
            // Cần thêm logic ROLLBACK/HỦY VÉ nếu Lập Hóa đơn thất bại.
            // Đây là vấn đề phức tạp nếu không dùng JTA hoặc Transaction xuyên suốt.
            
            // Nếu không dùng Transaction xuyên suốt, ta cần gọi hàm hủy vé cho tất cả 
            // danhSachVeDaDat nếu lapHoaDon() thất bại.
            btnThanhToan.setEnabled(true);
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
            if (btnThanhToan.isEnabled()) { 
                xuLyThanhToan();
            }
        } else if (nguon == btnQuayLai) {
            this.dispose(); 
            
            if (previousScreen != null) {
                previousScreen.setVisible(true); 
            }
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