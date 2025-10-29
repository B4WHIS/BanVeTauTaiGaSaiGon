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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
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
        taiDuLieuGiaoDich();
    }

    // Helper methods cho GridBagLayout (Dựa trên [4])
    private GridBagConstraints taoGBC(int x, int y, int fill, int anchor, double weightx, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = fill;
        gbc.anchor = anchor;
        gbc.weightx = weightx;
        gbc.insets = insets;
        return gbc;
    }

    private GridBagConstraints taogbcLabel(int x, int y, Insets insets) {
        return taoGBC(x, y, GridBagConstraints.NONE, GridBagConstraints.WEST, 0.0, insets);
    }

    private GridBagConstraints taogbcField(int x, int y, Insets insets, int width) {
        GridBagConstraints gbc = taoGBC(x, y, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST, 1.0, insets);
        gbc.gridwidth = width;
        return gbc;
    }

    private JButton taoNutApDung() {
        JButton nut = new JButton("Áp Dụng");
        nut.setBackground(new Color(93, 156, 236));
        nut.setForeground(Color.WHITE);
        nut.setFont(new Font("Segoe UI", Font.BOLD, 15));
        return nut;
    }

    // R4: Helper method để lấy thông tin Ga Đi/Đến (Dựa trên [5])
    private String layThongTinGa(ChuyenTau ct) {
        if (ct == null || ct.getMaLichTrinh() == null) return "N/A - N/A";
        String maLichTrinh = ct.getMaLichTrinh();
        return "[Lịch trình: " + maLichTrinh + "]";
    }

    private void khoiTaoThanhPhan() {
        JPanel khungChinh = new JPanel(new BorderLayout(10, 10));
        khungChinh.setBackground(Color.WHITE);
        khungChinh.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        JLabel lblTieuDeChinh = new JLabel("TIẾN HÀNH THANH TOÁN", SwingConstants.CENTER);
        lblTieuDeChinh.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTieuDeChinh.setForeground(mauChinh);
        pnlHeader.add(lblTieuDeChinh, BorderLayout.NORTH);
        khungChinh.add(pnlHeader, BorderLayout.NORTH);

        // Center Content
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBackground(Color.WHITE);

        // Left Panel: Thông tin thanh toán
        JPanel pnlThongTinThanhToan = new JPanel(new BorderLayout());
        pnlThongTinThanhToan.setPreferredSize(new Dimension(500, 0));
        pnlThongTinThanhToan.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(mauChinh, 2), "THÔNG TIN THANH TOÁN",
            TitledBorder.CENTER, TitledBorder.TOP, phongChuTongKet, mauChinh));

        pnlNhapLieu = new JPanel(new GridBagLayout());
        pnlNhapLieu.setBackground(Color.WHITE);
        Insets insets = new Insets(8, 10, 8, 10);
        int dong = 0;

        // Khởi tạo Fields
        txtTenKhachHang = new JTextField(20); txtTenKhachHang.setEditable(false);
        txtCmndPayer = new JTextField(20); txtCmndPayer.setEditable(false);
        txtSdtPayer = new JTextField(20); txtSdtPayer.setEditable(false);
        txtMaKhuyenMai = new JTextField(15);
        btnApDungMa = taoNutApDung();
        txtTongTienVeGoc = new JTextField(20); txtTongTienVeGoc.setEditable(false);
        txtThueVAT = new JTextField(20); txtThueVAT.setEditable(false);
        txtTongThanhToanCuoi = new JTextField(20); txtTongThanhToanCuoi.setEditable(false);

        // Hàng 0: Khách Hàng (Tên)
        pnlNhapLieu.add(new JLabel("Người thanh toán:"), taogbcLabel(0, dong, insets));
        pnlNhapLieu.add(txtTenKhachHang, taogbcField(1, dong++, insets, 2));
        // Hàng 1: CMND/CCCD
        pnlNhapLieu.add(new JLabel("CMND/CCCD:"), taogbcLabel(0, dong, insets));
        pnlNhapLieu.add(txtCmndPayer, taogbcField(1, dong++, insets, 2));
        // Hàng 2: SĐT
        pnlNhapLieu.add(new JLabel("SĐT:"), taogbcLabel(0, dong, insets));
        pnlNhapLieu.add(txtSdtPayer, taogbcField(1, dong++, insets, 2));
        // Hàng 3: Mã Khuyến Mãi
        pnlNhapLieu.add(new JLabel("Mã Khuyến Mãi (KM):"), taogbcLabel(0, dong, insets));
        pnlNhapLieu.add(txtMaKhuyenMai, taogbcField(1, dong, insets, 1));
        pnlNhapLieu.add(btnApDungMa, taoGBC(2, dong++, GridBagConstraints.NONE, GridBagConstraints.EAST, 0.0, new Insets(8, 0, 8, 10)));
        // Hàng 4: Tổng Tiền Gốc (Chưa Thuế)
        pnlNhapLieu.add(new JLabel("Tổng tiền vé (Chưa Thuế):"), taogbcLabel(0, dong, insets));
        pnlNhapLieu.add(txtTongTienVeGoc, taogbcField(1, dong++, insets, 2));
        // Hàng 5: Thuế VAT
        pnlNhapLieu.add(new JLabel("THUẾ VAT (10%):"), taogbcLabel(0, dong, insets));
        pnlNhapLieu.add(txtThueVAT, taogbcField(1, dong++, insets, 2));
        // Hàng 6: TỔNG CỘNG PHẢI THU (Màu đỏ)
        JLabel lblTongCong = new JLabel("TỔNG CỘNG PHẢI THU:");
        lblTongCong.setFont(phongChuTongKet);
        lblTongCong.setForeground(mauTongKet);
        pnlNhapLieu.add(lblTongCong, taogbcLabel(0, dong, insets));
        pnlNhapLieu.add(txtTongThanhToanCuoi, taogbcField(1, dong++, insets, 2));

        pnlThongTinThanhToan.add(pnlNhapLieu, BorderLayout.NORTH);
        pnlCenter.add(pnlThongTinThanhToan, BorderLayout.WEST);

        // Right Panel: Bảng chi tiết vé
        moHinhBang = new DefaultTableModel(
            new String[]{"STT", "Chuyến/Tàu", "Ga Đi - Đến", "Toa/Ghế", "Loại HK/Ưu đãi", "Giá TT (VNĐ)"}, 0);
        tblChiTiet = new JTable(moHinhBang);
        tblChiTiet.setRowHeight(35);
        tblChiTiet.setFont(phongChuGiaTri);

        JPanel pnlBangChiTiet = new JPanel(new BorderLayout());
        pnlBangChiTiet.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(mauChinh, 2), "CHI TIẾT VÉ",
            TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 18), mauChinh));

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
        btnQuayLai.setPreferredSize(new Dimension(150, 50));

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
        

        return pnlThaoTac;
    }

    private void thietLapSuKien() {
        btnQuayLai.addActionListener(this);
        btnThanhToan.addActionListener(this);
        btnApDungMa.addActionListener(this);
    }

    private void taiDuLieuGiaoDich() {
        tongTienTruocVAT = BigDecimal.ZERO;

        // --- HIỂN THỊ DỮ LIỆU THẬT TỪ ENTITY ---
        boolean isInfoMissing = nguoiThanhToan.getHoTen() == null || nguoiThanhToan.getHoTen().isEmpty();

        txtTenKhachHang.setText(isInfoMissing ? "Khách Vãng Lai (Mới)" : nguoiThanhToan.getHoTen());
        txtCmndPayer.setText(isInfoMissing || nguoiThanhToan.getCmndCccd() == null || nguoiThanhToan.getCmndCccd().trim().isEmpty() ? "Chưa cung cấp" : nguoiThanhToan.getCmndCccd());
        txtSdtPayer.setText(isInfoMissing || nguoiThanhToan.getSoDT() == null || nguoiThanhToan.getSoDT().trim().isEmpty() ? "Chưa cung cấp" : nguoiThanhToan.getSoDT());

        moHinhBang.setRowCount(0);
        int stt = 1;

        for (Ve ve : danhSachVe) {
            if (ve.getGiaThanhToan() == null) continue;

            tongTienTruocVAT = tongTienTruocVAT.add(ve.getGiaThanhToan());

            String maChuyen = ve.getMaChuyenTau() != null ? ve.getMaChuyenTau().getMaChuyenTau() : "N/A";
            String gaDiDen = layThongTinGa(ve.getMaChuyenTau());

            // Xử lý thông tin chỗ ngồi
            String toaGhe = (ve.getMaChoNgoi().getToaTau() != null ? ve.getMaChoNgoi().getToaTau().getMaToa() : "N/A")
                          + "/" + ve.getMaChoNgoi().getMaChoNgoi();

            // Lấy thông tin ưu đãi/loại khách hàng
            String maUuDai = ve.getMaHanhkhach().getMaUuDai();
            String loaiHK = (maUuDai != null && !maUuDai.equals("0")) ? "Ưu đãi: " + maUuDai : "Thường";

            String giaTTFormat = String.format("%,.0f", ve.getGiaThanhToan().doubleValue());

            moHinhBang.addRow(new Object[]{
                stt++, maChuyen, gaDiDen, toaGhe, loaiHK, giaTTFormat
            });
        }

        // Tính VAT và Tổng cuối cùng (10% VAT)
        BigDecimal VAT_RATE = new BigDecimal("0.1");
        BigDecimal thueVAT = tongTienTruocVAT.multiply(VAT_RATE).setScale(0, RoundingMode.HALF_UP);
        BigDecimal tongPhaiThu = tongTienTruocVAT.add(thueVAT);

        txtTongTienVeGoc.setText(String.format("%,.0f VNĐ", tongTienTruocVAT.doubleValue()));
        txtThueVAT.setText(String.format("%,.0f VNĐ", thueVAT.doubleValue()));
        txtTongThanhToanCuoi.setText(String.format("%,.0f VNĐ", tongPhaiThu.doubleValue()));
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
                if (nhanVienLap != null) {
                    System.out.println("Mã NV lập: " + nhanVienLap.getMaNhanVien());
                } else {
                    System.out.println("LỖI: Đối tượng nhanVienLap là NULL.");
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
}