package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import control.QuanLyVeControl;
import dao.HanhKhachDAO;
import dao.UuDaiDAO;
import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.HanhKhach;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.ToaTau;
import entity.Ve;
public class ThongTinKhachHangGUI extends JFrame implements ActionListener {
    // Constants
    private final Color MAU_CHU_DAO = new Color(74, 140, 103);
    private final Color MAU_NEN_TIEU_DE = new Color(225, 242, 232);
    private static final Font FONT_NHAN = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_GIA_TRI = new Font("Segoe UI", Font.PLAIN, 14);
    private static final DateTimeFormatter DINH_DANG_NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private BigDecimal tongTienCanThanhToan = BigDecimal.ZERO;
    // UI Components
    private JTextField txtHoTen, txtSoDienThoai, txtCmndCccd;
    private JDateChooser chonNgaySinh;
    private JComboBox<String> hopChonUuDai;
    private JTable bangDanhSach;
    private DefaultTableModel moHinhBang;
    private JButton nutThemHanhKhach, nutQuayLai, nutTiepTucThanhToan, nutChonNguoiThanhToan, nutLamMoi;
    private JLabel lblTongTien;
    // Data/Control
    private ChuyenTau chuyenTauDuocChon;
    private List<ChoNgoi> danhSachChoNgoi;
    private NhanVien nhanVienLap;
    private List<HanhKhach> danhSachHanhKhachDaNhap;
    private HanhKhach nguoiThanhToan;
    private final UuDaiDAO daoUuDai;
    private final HanhKhachDAO daoHanhKhach;
    private final QuanLyVeControl dieuKhienVe;
    public ThongTinKhachHangGUI(ChuyenTau ct, List<ChoNgoi> gheDaChon, NhanVien nv) {
        this.chuyenTauDuocChon = ct;
        this.danhSachChoNgoi = gheDaChon;
        this.nhanVienLap = nv != null ? nv : new NhanVien("NV-001");
        this.daoUuDai = new UuDaiDAO();
        this.daoHanhKhach = new HanhKhachDAO();
        this.dieuKhienVe = new QuanLyVeControl();
        this.danhSachHanhKhachDaNhap = new ArrayList<>(gheDaChon.size());
        for (int i = 0; i < gheDaChon.size(); i++) {
            this.danhSachHanhKhachDaNhap.add(null);
        }
        khoiTaoThanhPhan();
        thietLapSuKien();
        taiDanhSachUuDai();
        hienThiDanhSachChoNgoiBanDau(gheDaChon);
        capNhatTongTien();
        setTitle("Nhập Thông Tin Hành Khách");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    // Layout Helper Methods
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
    private GridBagConstraints taogbcField(int x, int y, Insets insets) {
        return taoGBC(x, y, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST, 1.0, insets);
    }
    private JButton taoNutChucNang(String text, Color background, String iconPath, Dimension size) {
        JButton nut = new JButton(text);
        nut.setBackground(background);
        nut.setForeground(Color.WHITE);
        nut.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nut.setPreferredSize(size);
        nut.setFocusPainted(false);
        nut.setHorizontalTextPosition(SwingConstants.RIGHT);
        return nut;
    }
    private void khoiTaoThanhPhan() {
        // Initialize UI components
        txtHoTen = new JTextField(20);
        txtSoDienThoai = new JTextField(15);
        txtCmndCccd = new JTextField(15);
        chonNgaySinh = new JDateChooser();
        hopChonUuDai = new JComboBox<>();
        moHinhBang = new DefaultTableModel(
                new String[]{"STT", "Chuyến", "Toa/Ghế", "Họ tên HK", "Ngày sinh", "Ưu đãi", "Giá TT (VNĐ)", "Tình trạng", "Người TT"},
                0);
        bangDanhSach = new JTable(moHinhBang);
        bangDanhSach.setRowHeight(30);
        nutThemHanhKhach = taoNutChucNang("Thêm Hành Khách", MAU_CHU_DAO.darker(), null, new Dimension(190, 40));
        nutQuayLai = taoNutChucNang("Quay Lại", new Color(220, 20, 60), null, new Dimension(150, 40));
        nutChonNguoiThanhToan = taoNutChucNang("Chọn Người TT", new Color(93, 156, 236), null, new Dimension(220, 40));
        nutTiepTucThanhToan = taoNutChucNang("Tiếp Tục Thanh Toán", new Color(103, 192, 144), null, new Dimension(250, 40));
        nutLamMoi = taoNutChucNang("Làm Mới", new Color(255, 184, 0), null, new Dimension(190, 40));
        lblTongTien = new JLabel("TỔNG CỘNG: 0.00 VNĐ", SwingConstants.RIGHT);
        lblTongTien.setForeground(Color.WHITE);
        // NORTH
        JPanel pnlTitleWrapper = new JPanel(new BorderLayout());
        pnlTitleWrapper.setBackground(MAU_NEN_TIEU_DE);
        JLabel lblTieuDeChinh = new JLabel("NHẬP THÔNG TIN HÀNH KHÁCH", SwingConstants.CENTER);
        lblTieuDeChinh.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTieuDeChinh.setForeground(MAU_CHU_DAO);
        pnlTitleWrapper.add(lblTieuDeChinh, BorderLayout.CENTER);
        this.getContentPane().add(pnlTitleWrapper, BorderLayout.NORTH);
        // WEST (Input Panel)
        JPanel pnlThongTinHanhKhach = new JPanel(new BorderLayout());
        pnlThongTinHanhKhach.setPreferredSize(new Dimension(450, 0));
        pnlThongTinHanhKhach.setBackground(Color.WHITE);
        pnlThongTinHanhKhach.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(MAU_CHU_DAO, 2),
                "NHẬP THÔNG TIN KHÁCH HÀNG (Từng chỗ ngồi)",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 18),
                MAU_CHU_DAO));
        JPanel pnlGrid = new JPanel(new GridBagLayout());
        pnlGrid.setBackground(Color.WHITE);
        pnlGrid.setBorder(new EmptyBorder(10, 10, 10, 10));
        Insets insets = new Insets(8, 5, 8, 5);
        int dong = 0;
        pnlGrid.add(new JLabel("Họ tên:"), taogbcLabel(0, dong, insets));
        pnlGrid.add(txtHoTen, taogbcField(1, dong++, insets));
        pnlGrid.add(new JLabel("Ngày sinh:"), taogbcLabel(0, dong, insets));
        pnlGrid.add(chonNgaySinh, taogbcField(1, dong++, insets));
        pnlGrid.add(new JLabel("SĐT:"), taogbcLabel(0, dong, insets));
        pnlGrid.add(txtSoDienThoai, taogbcField(1, dong++, insets));
        pnlGrid.add(new JLabel("CMND/CCCD:"), taogbcLabel(0, dong, insets));
        pnlGrid.add(txtCmndCccd, taogbcField(1, dong++, insets));
        pnlGrid.add(new JLabel("Chọn Ưu đãi:"), taogbcLabel(0, dong, insets));
        pnlGrid.add(hopChonUuDai, taogbcField(1, dong++, insets));
        JPanel pnlNutBam = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlNutBam.setOpaque(false);
// pnlNutBam.add(nutLamMoi);
        pnlNutBam.add(nutThemHanhKhach);
        GridBagConstraints gbcNut = taoGBC(0, dong++, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, 1.0, new Insets(20, 10, 10, 10));
        gbcNut.gridwidth = 2;
        pnlGrid.add(pnlNutBam, gbcNut);
        pnlThongTinHanhKhach.add(pnlGrid, BorderLayout.NORTH);
        // CENTER (Table Panel)
        JPanel pnlDanhSachWrapper = new JPanel(new BorderLayout());
        pnlDanhSachWrapper.setBackground(Color.WHITE);
        pnlDanhSachWrapper.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(MAU_CHU_DAO, 2),
                "DANH SÁCH CHỖ NGỒI ĐÃ CHỌN",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 18),
                MAU_CHU_DAO));
        pnlDanhSachWrapper.add(new JScrollPane(bangDanhSach), BorderLayout.CENTER);
        JPanel pnlBody = new JPanel(new BorderLayout(20, 10));
        pnlBody.setBackground(Color.WHITE);
        pnlBody.setBorder(new EmptyBorder(10, 20, 10, 20));
        pnlBody.add(pnlThongTinHanhKhach, BorderLayout.WEST);
        pnlBody.add(pnlDanhSachWrapper, BorderLayout.CENTER);
        this.getContentPane().add(pnlBody, BorderLayout.CENTER);
        // SOUTH (Footer)
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(MAU_CHU_DAO.darker());
        pnlFooter.setBorder(new EmptyBorder(5, 20, 5, 20));
        JPanel pnlFooterRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlFooterRight.setOpaque(false);
        pnlFooterRight.add(nutQuayLai);
        pnlFooterRight.add(nutChonNguoiThanhToan);
        pnlFooterRight.add(nutTiepTucThanhToan);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pnlFooter.add(lblTongTien, BorderLayout.WEST);
        pnlFooter.add(pnlFooterRight, BorderLayout.EAST);
        this.getContentPane().add(pnlFooter, BorderLayout.SOUTH);
        nutTiepTucThanhToan.setEnabled(false);
        nutChonNguoiThanhToan.setEnabled(false);
    }
    private void thietLapSuKien() {
        nutThemHanhKhach.addActionListener(this);
        nutQuayLai.addActionListener(this);
        nutTiepTucThanhToan.addActionListener(this);
        nutChonNguoiThanhToan.addActionListener(this);
        nutLamMoi.addActionListener(this);
    }
    private void hienThiDanhSachChoNgoiBanDau(List<ChoNgoi> gheDaChon) {
        moHinhBang.setRowCount(0);
        for (ChoNgoi cho : gheDaChon) {
            String toaGhe = (cho.getToaTau() != null ? cho.getToaTau().getMaToa() : "N/A") + "/" + cho.getMaChoNgoi();
            moHinhBang.addRow(new Object[]{
                    moHinhBang.getRowCount() + 1,
                    chuyenTauDuocChon.getMaChuyenTau(),
                    toaGhe,
                    "[Chưa có]",
                    "",
                    "[Chưa chọn]",
                    "0.00",
                    "CHỜ NHẬP",
                    "Chưa chọn"
            });
        }
        if (!gheDaChon.isEmpty()) {
            bangDanhSach.setRowSelectionInterval(0, 0);
        }
    }
    private void capNhatTongTien() {
        tongTienCanThanhToan = BigDecimal.ZERO;
        for (int i = 0; i < danhSachChoNgoi.size(); i++) {
            String giaStr = moHinhBang.getValueAt(i, 6).toString();
            try {
                giaStr = giaStr.replaceAll("\\.", "").replace(",", ".").replace(" VNĐ", "");
                BigDecimal giaTT = new BigDecimal(giaStr);
                tongTienCanThanhToan = tongTienCanThanhToan.add(giaTT);
            } catch (Exception e) {
                // Skip parsing errors
            }
        }
        lblTongTien.setText("TỔNG CỘNG: " + String.format("%,.2f VNĐ", tongTienCanThanhToan));
    }
    private void xuLyLamMoiForm() {
        txtHoTen.setText("");
        chonNgaySinh.setDate(null);
        txtSoDienThoai.setText("");
        txtCmndCccd.setText("");
        hopChonUuDai.setSelectedIndex(0);
        txtHoTen.requestFocus();
    }
    private BigDecimal layGiaVeCoBan(ChoNgoi choNgoi) {
        BigDecimal giaChuyen = chuyenTauDuocChon.getGiaChuyen();
        ToaTau toa = choNgoi.getToaTau();
        if (toa != null && toa.getHeSoGia() != null) {
            return giaChuyen.multiply(toa.getHeSoGia()).setScale(2, RoundingMode.HALF_UP);
        }
        return giaChuyen.setScale(2, RoundingMode.HALF_UP);
    }
    private void taiDanhSachUuDai() {
        hopChonUuDai.removeAllItems();
hopChonUuDai.addItem("--- Chọn Ưu đãi ---");
List<entity.UuDai> listUd = daoUuDai.layTatCaUuDai();
for (entity.UuDai ud : listUd) {
    String ma = ud.getMaUuDai();
    String tenLoaiTam = "Giảm " + ud.getMucGiamGia().setScale(0, RoundingMode.HALF_UP) + "%";
    hopChonUuDai.addItem(tenLoaiTam + " (ID: " + ma + ")");
}
    }
    private String layMaUuDaiTuTenFull(String tenLoaiFull) {
        if (tenLoaiFull == null || tenLoaiFull.contains("---")) {
            return null;
        }
        try {
            String[] parts = tenLoaiFull.split("\\(ID: ");
            if (parts.length > 1) {
                return parts[1].replace(")", "").trim();
            }
        } catch (Exception e) {
            // Handle parsing error
        }
        return null;
    }
    private boolean kiemTraTatCaDaNhap() {
        for (int i = 0; i < moHinhBang.getRowCount(); i++) {
            if (moHinhBang.getValueAt(i, 7).equals("CHỜ NHẬP")) {
                return false;
            }
        }
        return true;
    }
    private void xuLyThemHanhKhach() {
        int dongDuocChon = bangDanhSach.getSelectedRow();
        if (dongDuocChon < 0 || !moHinhBang.getValueAt(dongDuocChon, 7).equals("CHỜ NHẬP")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chỗ ngồi CHỜ NHẬP để nhập thông tin.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String hoTen = txtHoTen.getText().trim();
        java.util.Date ngaySinhDate = chonNgaySinh.getDate();
        String sdt = txtSoDienThoai.getText().trim();
        String cmnd = txtCmndCccd.getText().trim();
        String tenLoaiUuDaiFull = (String) hopChonUuDai.getSelectedItem();
        if (hoTen.isEmpty() || ngaySinhDate == null || tenLoaiUuDaiFull == null || tenLoaiUuDaiFull.contains("---")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Họ tên, Ngày sinh và chọn Ưu đãi.", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maUuDai = layMaUuDaiTuTenFull(tenLoaiUuDaiFull);
        LocalDate ngaySinh = ngaySinhDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        nutThemHanhKhach.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        System.out.println("==> daoHanhKhach: " + daoHanhKhach + ", daoUuDai: " + daoUuDai);

        new SwingWorker<Void, Void>() {
            String validationError = null;
            HanhKhach hkKetQua = null;
            BigDecimal giaThanhToan = null;
            ChoNgoi choNgoiDangChon = danhSachChoNgoi.get(dongDuocChon);
            @Override
            protected Void doInBackground() throws Exception {
                String cccdChecked = cmnd.isEmpty() ? "" : cmnd;
                String sdtChecked = sdt.isEmpty() ? "" : sdt;
                boolean laTreEm = "UD-01".equals(maUuDai);
                if (!cmnd.isEmpty() && !cmnd.matches("^(\\d{9}|\\d{12})$")) {
                    validationError = "CMND/CCCD phải là 9 hoặc 12 chữ số.";
                    return null;
                }
                if (!sdt.isEmpty() && !sdt.matches("^0\\d{9}$")) {
                    validationError = "SĐT phải là 10 chữ số và bắt đầu bằng 0.";
                    return null;
                }
                if (laTreEm) {
                    if (sdt.isEmpty()) {
                        validationError = "Vui lòng nhập Số điện thoại người giám hộ.";
                        return null;
                    }
                    cccdChecked = "";
                } else {
                    if (cmnd.isEmpty() && sdt.isEmpty()) {
                        validationError = "Vui lòng nhập ít nhất CMND/CCCD hoặc SĐT cho người lớn.";
                        return null;
                    }
                }
                try {
                    if (maUuDai != null && !maUuDai.equals("0") && !daoUuDai.kiemTraMaUuDaiTonTai(maUuDai)) {
                        validationError = "Mã ưu đãi không tồn tại.";
                        return null;
                    }
                    HanhKhach hkMoi = new HanhKhach(null, hoTen, cccdChecked, sdtChecked, ngaySinh, maUuDai);
                    HanhKhach hanhkhachHienCo = null;
                    if (!hkMoi.getCmndCccd().isEmpty()) {
                        hanhkhachHienCo = daoHanhKhach.layHanhKhachTheoCMND(hkMoi.getCmndCccd());
                    }
                    if (hanhkhachHienCo == null && !hkMoi.getSoDT().isEmpty()) {
                        hanhkhachHienCo = daoHanhKhach.layHanhKhachTheoSDT(hkMoi.getSoDT());
                    }
                    if (hanhkhachHienCo == null) {
                        daoHanhKhach.themHanhKhach(hkMoi);
                        hkKetQua = hkMoi;
                    } else {
                        // Update if needed, or handle existing
                        validationError = "Hành khách đã tồn tại với thông tin tương tự.";
                        return null;
                    }
                    if (hkKetQua != null) {
                        BigDecimal giaVeGoc = layGiaVeCoBan(choNgoiDangChon);
                        giaThanhToan = dieuKhienVe.tinhGiaVeCuoiCung(giaVeGoc, maUuDai, null);
                    }
                } catch (Exception e) {
                    validationError = "Lỗi CSDL/Nghiệp vụ: " + e.getMessage();
                }
                return null;
            }
            @Override
            protected void done() {
                nutThemHanhKhach.setEnabled(true);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                try {
                    get();
                    if (validationError != null) {
                        JOptionPane.showMessageDialog(ThongTinKhachHangGUI.this, validationError, "Lỗi Xử Lý", JOptionPane.ERROR_MESSAGE);
                    } else if (hkKetQua != null && giaThanhToan != null) {
                        String giaTTFormat = String.format("%,.2f VNĐ", giaThanhToan.setScale(2, RoundingMode.HALF_UP));
                        danhSachHanhKhachDaNhap.set(dongDuocChon, hkKetQua);
                        
                        moHinhBang.setValueAt(hoTen, dongDuocChon, 3);
                        moHinhBang.setValueAt(ngaySinh.format(DINH_DANG_NGAY), dongDuocChon, 4);
                        moHinhBang.setValueAt(tenLoaiUuDaiFull, dongDuocChon, 5);
                        moHinhBang.setValueAt(giaTTFormat, dongDuocChon, 6);
                        moHinhBang.setValueAt("ĐÃ NHẬP", dongDuocChon, 7);
                        moHinhBang.setValueAt("Khách", dongDuocChon, 8);
                        capNhatTongTien();
                        nutChonNguoiThanhToan.setEnabled(true);
                        xuLyLamMoiForm();
                        if (dongDuocChon < bangDanhSach.getRowCount() - 1) {
                            bangDanhSach.setRowSelectionInterval(dongDuocChon + 1, dongDuocChon + 1);
                        }
                        if (kiemTraTatCaDaNhap()) {
                            JOptionPane.showMessageDialog(ThongTinKhachHangGUI.this, "Đã nhập thông tin cho tất cả chỗ ngồi.", "Hoàn tất nhập liệu", JOptionPane.INFORMATION_MESSAGE);
                            nutTiepTucThanhToan.setEnabled(nguoiThanhToan != null);
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(ThongTinKhachHangGUI.this, "Lỗi khi thêm hành khách: " + e.getMessage(), "Lỗi Hệ thống", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
    private void xuLyChuyenSangThanhToan() {
        if (!kiemTraTatCaDaNhap() || nguoiThanhToan == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin hành khách và chọn người thanh toán.", "Lỗi nghiệp vụ", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Ve> danhSachVeThuc = new ArrayList<>();
        try {
            for (int i = 0; i < danhSachChoNgoi.size(); i++) {
                ChoNgoi choNgoi = danhSachChoNgoi.get(i);
                HanhKhach hanhKhach = danhSachHanhKhachDaNhap.get(i);
                String giaTTStr = moHinhBang.getValueAt(i, 6).toString();
                giaTTStr = giaTTStr.replaceAll("\\.", "").replace(",", ".").replace(" VNĐ", "");
                BigDecimal giaThanhToan = new BigDecimal(giaTTStr);
                if (giaThanhToan.compareTo(BigDecimal.ZERO) <= 0) {
                    giaThanhToan = new BigDecimal("0.01").setScale(2, RoundingMode.HALF_UP);
                }
                KhuyenMai khuyenMai = null; // Set to null to defer khuyến mãi to ThanhToanGUI
                Ve ve = new Ve();
                // Không set maVe ở đây, để datVe xử lý
                ve.setNgayDat(LocalDateTime.now());
                ve.setTrangThai("Đã đặt");
                ve.setGiaVeGoc(layGiaVeCoBan(choNgoi));
                ve.setGiaThanhToan(giaThanhToan);
                ve.setMaChoNgoi(choNgoi);
                ve.setMaChuyenTau(chuyenTauDuocChon);
                ve.setMaHanhkhach(hanhKhach);
                ve.setMaKhuyenMai(khuyenMai);
                ve.setMaNhanVien(nhanVienLap);

                // Gọi datVe để đặt vé thực tế qua QuanLyVeControl
                String maVe = dieuKhienVe.datVe(ve, hanhKhach, nhanVienLap);
                ve.setMaVe(maVe); // Set maVe sau khi nhận từ datVe

                danhSachVeThuc.add(ve);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tạo đối tượng vé hoặc đặt vé: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(() -> {
            new ThanhToanGUI(danhSachVeThuc, nguoiThanhToan, nhanVienLap).setVisible(true);
            this.dispose();
        });
    }
    private void xuLyChonNguoiThanhToan() {
        int dongDuocChon = bangDanhSach.getSelectedRow();
        if (dongDuocChon < 0 || danhSachHanhKhachDaNhap.get(dongDuocChon) == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hành khách đã nhập thông tin để chỉ định là người thanh toán.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        nguoiThanhToan = danhSachHanhKhachDaNhap.get(dongDuocChon);
        String hoTenNguoiTT = nguoiThanhToan.getHoTen();
        for (int i = 0; i < moHinhBang.getRowCount(); i++) {
            if (moHinhBang.getValueAt(i, 7).equals("ĐÃ NHẬP")) {
                moHinhBang.setValueAt("Khách", i, 8);
            }
        }
        moHinhBang.setValueAt("Người TT", dongDuocChon, 8);
        JOptionPane.showMessageDialog(this, "Đã chỉ định " + hoTenNguoiTT + " là người thanh toán.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        if (kiemTraTatCaDaNhap() && nguoiThanhToan != null) {
            nutTiepTucThanhToan.setEnabled(true);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();
        if (nguon == nutThemHanhKhach) {
            xuLyThemHanhKhach();
        } else if (nguon == nutChonNguoiThanhToan) {
            xuLyChonNguoiThanhToan();
        } else if (nguon == nutTiepTucThanhToan) {
            xuLyChuyenSangThanhToan();
        } else if (nguon == nutQuayLai) {
            SwingUtilities.invokeLater(() -> {
                try {
                    new ChonChoNgoiGUI(chuyenTauDuocChon, nhanVienLap, null).setVisible(true);
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi quay lại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else if (nguon == nutLamMoi) {
            xuLyLamMoiForm();
        }
    }
}