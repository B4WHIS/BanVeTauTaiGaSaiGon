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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import control.QuanLyVeControl;
import dao.UuDaiDAO;
import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.NhanVien;
import entity.Ve;

public class ThongTinKhachHangGUI extends JFrame implements ActionListener {
    private final Color MAU_CHU_DAO = new Color(74, 140, 103); // Xanh lá đậm
    private final Color MAU_TIEU_DE = new Color(93, 156, 236); // Xanh dương
    private final Color MAU_QUAY_LAI = new Color(229, 115, 115); // Đỏ/Cam (Cảnh báo)
    private final Color MAU_NEN_TIEU_DE = new Color(225, 242, 232); // Nền header
    private static final DateTimeFormatter DINH_DANG_NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private ChuyenTau chuyenTauDuocChon; 
    private List<ChoNgoi> danhSachChoNgoi; 
    private NhanVien nhanVienLap; 
    private List<Ve> danhSachVeDaDat; 
    private UuDaiDAO daoUuDai;
    private QuanLyVeControl dieuKhienVe;
    private JLabel lblTieuDeChinh;
    private JPanel pnlThongTinHanhKhach;
    private JTextField txtHoTen;
    private JDateChooser chonNgaySinh;
    private JTextField txtSoDienThoai;
    private JTextField txtCmndCccd;
    private JComboBox<String> hopChonUuDai;
    private JButton nutLamMoi;
    private JButton nutThemHanhKhach;
    private JTable bangDanhSach;
    private DefaultTableModel moHinhBang;
    private JButton nutQuayLai;
    private JButton nutTiepTucThanhToan;

    public ThongTinKhachHangGUI(ChuyenTau ct, List<ChoNgoi> gheDaChon, NhanVien nv) {
        this.chuyenTauDuocChon = ct;
        this.danhSachChoNgoi = gheDaChon;
        this.nhanVienLap = nv;
        this.daoUuDai = new UuDaiDAO();
        this.dieuKhienVe = new QuanLyVeControl();
        this.danhSachVeDaDat = new ArrayList<>(); // Khởi tạo danh sách vé giả lập

        // Header 
        lblTieuDeChinh = new JLabel("NHẬP THÔNG TIN HÀNH KHÁCH", SwingConstants.CENTER);
        lblTieuDeChinh.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTieuDeChinh.setForeground(MAU_CHU_DAO);
        JPanel pnlTitleWrapper = new JPanel(new BorderLayout());
        pnlTitleWrapper.setBorder(new EmptyBorder(15, 20, 15, 20));
        pnlTitleWrapper.setBackground(MAU_NEN_TIEU_DE);
        pnlTitleWrapper.add(lblTieuDeChinh, BorderLayout.CENTER);
        this.getContentPane().add(pnlTitleWrapper, BorderLayout.NORTH);

        // Form Nhập liệu
        pnlThongTinHanhKhach = new JPanel(new BorderLayout());
        pnlThongTinHanhKhach.setPreferredSize(new Dimension(450, 0));
        pnlThongTinHanhKhach.setBackground(Color.WHITE);
        pnlThongTinHanhKhach.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(MAU_TIEU_DE, 2),
            "THÔNG TIN HÀNH KHÁCH CHI TIẾT",
            TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 18),
            MAU_TIEU_DE)
        );

        JPanel pnlGrid = new JPanel(new GridBagLayout());
        pnlGrid.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        int dong = 0;

        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHoTen.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = dong;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        pnlGrid.add(lblHoTen, gbc);

        txtHoTen = new JTextField(15);
        txtHoTen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtHoTen.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = dong++;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        pnlGrid.add(txtHoTen, gbc);

        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNgaySinh.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = dong;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        pnlGrid.add(lblNgaySinh, gbc);

        chonNgaySinh = new JDateChooser();
        chonNgaySinh.setDateFormatString("dd/MM/yyyy");
        chonNgaySinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chonNgaySinh.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = dong++;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        pnlGrid.add(chonNgaySinh, gbc);

        JLabel lblSoDienThoai = new JLabel("Số điện thoại (SĐT):");
        lblSoDienThoai.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSoDienThoai.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = dong;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        pnlGrid.add(lblSoDienThoai, gbc);

        txtSoDienThoai = new JTextField(15);
        txtSoDienThoai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSoDienThoai.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txtSoDienThoai.setToolTipText("Không bắt buộc cho trẻ em.");
        gbc.gridx = 1;
        gbc.gridy = dong++;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        pnlGrid.add(txtSoDienThoai, gbc);

        JLabel lblCmndCccd = new JLabel("CMND/CCCD:");
        lblCmndCccd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCmndCccd.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = dong;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        pnlGrid.add(lblCmndCccd, gbc);

        txtCmndCccd = new JTextField(15);
        txtCmndCccd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCmndCccd.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txtCmndCccd.setToolTipText("Không bắt buộc cho trẻ em.");
        gbc.gridx = 1;
        gbc.gridy = dong++;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        pnlGrid.add(txtCmndCccd, gbc);

        JLabel lblUuDai = new JLabel("Chọn Ưu đãi:");
        lblUuDai.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUuDai.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = dong;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        pnlGrid.add(lblUuDai, gbc);

        hopChonUuDai = new JComboBox<>();
        hopChonUuDai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        hopChonUuDai.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = dong++;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        pnlGrid.add(hopChonUuDai, gbc);

        JPanel pnlNutBam = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlNutBam.setBackground(Color.WHITE);
        nutLamMoi = new JButton("Làm Mới", GiaoDienChinh.chinhKichThuoc("/img/reload.png", 20, 20));
        nutLamMoi.setBackground(MAU_TIEU_DE);
        nutLamMoi.setForeground(Color.WHITE);
        nutLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nutLamMoi.setFocusPainted(false);
        nutLamMoi.setBorder(new EmptyBorder(10, 20, 10, 20));
        nutLamMoi.setHorizontalTextPosition(SwingConstants.RIGHT);
        nutLamMoi.setIconTextGap(10);
        pnlNutBam.add(nutLamMoi);

        nutThemHanhKhach = new JButton("Thêm Khách Hàng", GiaoDienChinh.chinhKichThuoc("/img/plus.png", 20, 20));
        nutThemHanhKhach.setBackground(MAU_CHU_DAO);
        nutThemHanhKhach.setForeground(Color.WHITE);
        nutThemHanhKhach.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nutThemHanhKhach.setFocusPainted(false);
        nutThemHanhKhach.setBorder(new EmptyBorder(10, 20, 10, 20));
        nutThemHanhKhach.setHorizontalTextPosition(SwingConstants.RIGHT);
        nutThemHanhKhach.setIconTextGap(10);
        pnlNutBam.add(nutThemHanhKhach);

        gbc.gridx = 0;
        gbc.gridy = dong++;
        gbc.gridwidth = 2;
        pnlGrid.add(pnlNutBam, gbc);

        pnlThongTinHanhKhach.add(pnlGrid, BorderLayout.NORTH);

        // Bảng Danh sách (Phải)
        moHinhBang = new DefaultTableModel(
            new String[]{"STT", "Chuyến", "Toa/Ghế", "Họ tên HK", "Ngày sinh", "Ưu đãi", "Tình trạng"}, 0);
        bangDanhSach = new JTable(moHinhBang);
        JTableHeader tieuDeBang = bangDanhSach.getTableHeader();
        tieuDeBang.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tieuDeBang.setBackground(new Color(245, 245, 245));
        bangDanhSach.setRowHeight(30);
        bangDanhSach.setSelectionBackground(MAU_CHU_DAO.darker());

        JPanel pnlDanhSachWrapper = new JPanel(new BorderLayout());
        pnlDanhSachWrapper.setBackground(Color.WHITE);
        pnlDanhSachWrapper.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(MAU_CHU_DAO, 2),
            "DANH SÁCH CHỖ NGỒI & THÔNG TIN KHÁCH HÀNG (" + danhSachChoNgoi.size() + " vé)",
            TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 18),
            MAU_CHU_DAO)
        );
        pnlDanhSachWrapper.add(new JScrollPane(bangDanhSach), BorderLayout.CENTER);

        // Panel trung tâm
        JPanel pnlBody = new JPanel(new BorderLayout(20, 10));
        pnlBody.setBackground(Color.WHITE);
        pnlBody.setBorder(new EmptyBorder(10, 20, 10, 20));
        pnlBody.add(pnlThongTinHanhKhach, BorderLayout.WEST);
        pnlBody.add(pnlDanhSachWrapper, BorderLayout.CENTER);
        this.getContentPane().add(pnlBody, BorderLayout.CENTER);

        // Footer
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(MAU_CHU_DAO);
        pnlFooter.setBorder(new EmptyBorder(10, 20, 10, 20));
        nutQuayLai = new JButton("Quay Lại", GiaoDienChinh.chinhKichThuoc("/img/undo.png", 20, 20));
        nutQuayLai.setBackground(MAU_QUAY_LAI.darker());
        nutQuayLai.setForeground(Color.WHITE);
        nutQuayLai.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nutQuayLai.setFocusPainted(false);
        nutQuayLai.setBorder(new EmptyBorder(10, 20, 10, 20));
        nutQuayLai.setPreferredSize(new Dimension(150, 45));
        nutQuayLai.setHorizontalTextPosition(SwingConstants.RIGHT);
        nutQuayLai.setIconTextGap(10);
        pnlFooter.add(nutQuayLai, BorderLayout.WEST);

        nutTiepTucThanhToan = new JButton("Tiếp Tục Thanh Toán", GiaoDienChinh.chinhKichThuoc("/img/mark.png", 20, 20));
        nutTiepTucThanhToan.setBackground(MAU_CHU_DAO);
        nutTiepTucThanhToan.setForeground(Color.WHITE);
        nutTiepTucThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nutTiepTucThanhToan.setFocusPainted(false);
        nutTiepTucThanhToan.setBorder(new EmptyBorder(10, 20, 10, 20));
        nutTiepTucThanhToan.setPreferredSize(new Dimension(220, 45));
        nutTiepTucThanhToan.setHorizontalTextPosition(SwingConstants.RIGHT);
        nutTiepTucThanhToan.setIconTextGap(10);
        nutTiepTucThanhToan.setEnabled(false);
        pnlFooter.add(nutTiepTucThanhToan, BorderLayout.EAST);
        this.getContentPane().add(pnlFooter, BorderLayout.SOUTH);

        // Gán sự kiện
        nutLamMoi.addActionListener(this);
        nutThemHanhKhach.addActionListener(this);
        nutQuayLai.addActionListener(this);
        nutTiepTucThanhToan.addActionListener(this);

        // Load dữ liệu ban đầu
        taiDanhSachUuDai();
        hienThiDanhSachChoNgoiBanDau(gheDaChon);

        setTitle("Nhập Thông Tin Hành Khách");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();

        if (nguon == nutQuayLai) {
            this.dispose();
        }

        else if (nguon == nutThemHanhKhach) {
            xuLyThemHanhKhach();
        }

        else if (nguon == nutTiepTucThanhToan) {
//            xuLyChuyenSangThanhToan();
        }

        else if (nguon == nutLamMoi) {
            xuLyLamMoiForm();
        }
    }

   
    private void xuLyThemHanhKhach() {
        int dongDuocChon = bangDanhSach.getSelectedRow();

        // 1. Kiểm tra lựa chọn dòng
        if (dongDuocChon < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một chỗ ngồi (hàng) trong danh sách để nhập thông tin.",
                "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Thu thập dữ liệu từ form
        String hoTen = txtHoTen.getText().trim();
        Date ngaySinhDate = chonNgaySinh.getDate();
        String sdt = txtSoDienThoai.getText().trim();
        String cmnd = txtCmndCccd.getText().trim();
        String tenLoaiUuDai = (String) hopChonUuDai.getSelectedItem();

        // 3. Kiểm tra tính hợp lệ cơ bản
        if (hoTen.isEmpty() || ngaySinhDate == null || tenLoaiUuDai.contains("---")) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ Họ tên, Ngày sinh và chọn Loại Ưu đãi.",
                "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Chuyển đổi ngày sinh sang LocalDate
        LocalDate ngaySinh = ngaySinhDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String ngaySinhStr = ngaySinh.format(DINH_DANG_NGAY);

        // Quy tắc: Ngày sinh phải trước ngày hiện tại
        if (!ngaySinh.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this,
                "Ngày sinh không được sau hoặc là ngày hiện tại.",
                "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy mã ưu đãi
        String maUuDai = null;
        try {
            int batDau = tenLoaiUuDai.indexOf("ID: ") + 4;
            int ketThuc = tenLoaiUuDai.indexOf(")");
            maUuDai = tenLoaiUuDai.substring(batDau, ketThuc);
        } catch (Exception e) {
            // Lỗi tách chuỗi nếu format sai
        }

        // Quy tắc nghiệp vụ (Kiểm tra Trẻ em/Không phải trẻ em)
        boolean laTreEm = (maUuDai != null && maUuDai.equals("1")); // Giả định ID 1 là Trẻ em

        // Nếu KHÔNG phải trẻ em, phải có CMND/CCCD HOẶC SĐT
        if (!laTreEm) {
            if (cmnd.isEmpty() && sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Hành khách (không phải trẻ em) phải cung cấp CMND/CCCD hoặc SĐT.",
                    "Lỗi nghiệp vụ", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Kiểm tra định dạng CMND/CCCD
            if (!cmnd.isEmpty() && !cmnd.matches("^\\d{9}$|^\\d{12}$")) {
                JOptionPane.showMessageDialog(this,
                    "CMND/CCCD phải là 9 hoặc 12 chữ số.",
                    "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra định dạng SĐT
            if (!sdt.isEmpty() && !sdt.matches("^0\\d{9}$")) {
                JOptionPane.showMessageDialog(this,
                    "SĐT phải là 10 chữ số và bắt đầu bằng 0.",
                    "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 4. Cập nhật dữ liệu vào bảng (UI)
        moHinhBang.setValueAt(hoTen, dongDuocChon, 3);
        moHinhBang.setValueAt(ngaySinhStr, dongDuocChon, 4);
        moHinhBang.setValueAt(tenLoaiUuDai, dongDuocChon, 5);
        moHinhBang.setValueAt("ĐÃ NHẬP", dongDuocChon, 6);

        // 5. Kiểm tra trạng thái hoàn tất
        if (kiemTraTatCaDaNhap()) {
            JOptionPane.showMessageDialog(this,
                "Đã nhập thông tin cho tất cả chỗ ngồi. Có thể nhấn 'Tiếp Tục Thanh Toán'.",
                "Hoàn tất nhập liệu", JOptionPane.INFORMATION_MESSAGE);
            nutTiepTucThanhToan.setEnabled(true);
        } else {
            // Tự động chọn dòng tiếp theo
            if (dongDuocChon < bangDanhSach.getRowCount() - 1) {
                bangDanhSach.setRowSelectionInterval(dongDuocChon + 1, dongDuocChon + 1);
            }
        }

        // 6. Làm mới form để nhập khách tiếp theo
        xuLyLamMoiForm();
    }

//chuyen man 
//    private void xuLyChuyenSangThanhToan() {
//        if (!kiemTraTatCaDaNhap()) {
//            JOptionPane.showMessageDialog(this,
//                "Vui lòng nhập đầy đủ thông tin hành khách cho tất cả chỗ ngồi trước khi thanh toán.",
//                "Lỗi nghiệp vụ", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        // BƯỚC 1: Xử lý lưu HÀNH KHÁCH vào DB và tạo đối tượng Vé
//        double tongTienGiaLap = 0.0;
//        try {
//            for (int i = 0; i < moHinhBang.getRowCount(); i++) {
//                tongTienGiaLap += 500000.00;
//            }
//
//            // BƯỚC 2: Chuyển màn hình và truyền dữ liệu cần thiết cho ThanhToanGUI
//            SwingUtilities.invokeLater(() -> {
//                List<Ve> dsVeTest = new ArrayList<>();
//                dsVeTest.add(new Ve("VE001", null, "Khả dụng", new java.math.BigDecimal("500000.00"), new java.math.BigDecimal("500000.00"), null, chuyenTauDuocChon, null, null, nhanVienLap));
//                new ThanhToanGUI(dsVeTest, new entity.HanhKhach("HK001"), nhanVienLap, tongTienGiaLap).setVisible(true);
//            });
//
//            this.dispose();
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this,
//                "Lỗi trong quá trình chuyển sang thanh toán: " + ex.getMessage(),
//                "Lỗi Hệ thống", JOptionPane.ERROR_MESSAGE);
//            ex.printStackTrace();
//        }
//    }

//    làm mới
    private void xuLyLamMoiForm() {
        txtHoTen.setText("");
        chonNgaySinh.setDate(null);
        txtSoDienThoai.setText("");
        txtCmndCccd.setText("");
        hopChonUuDai.setSelectedIndex(0);
        txtHoTen.requestFocus();
    }

    //kiểm tra
    private boolean kiemTraTatCaDaNhap() {
        for (int i = 0; i < moHinhBang.getRowCount(); i++) {
            if (moHinhBang.getValueAt(i, 6).equals("CHỜ NHẬP")) {
                return false;
            }
        }
        return true;
    }

   
    private void taiDanhSachUuDai() {
        try {
            hopChonUuDai.removeAllItems();
            hopChonUuDai.addItem("--- Chọn Ưu đãi ---");
            daoUuDai.layTatCaLoaiUuDai().forEach((id, tenLoai) -> {
                hopChonUuDai.addItem(tenLoai + " (ID: " + id + ")");
            });
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi tải danh sách Ưu đãi: " + e.getMessage(), "Lỗi CSDL",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

   
    private void hienThiDanhSachChoNgoiBanDau(List<ChoNgoi> gheDaChon) {
        moHinhBang.setRowCount(0);
        int stt = 1;
        String maChuyenTau = (chuyenTauDuocChon != null) ? chuyenTauDuocChon.getMaChuyenTau() : "N/A";
        for (ChoNgoi cho : gheDaChon) {
            String toaGhe = (cho.getToaTau() != null ? cho.getToaTau().getMaToa() : "N/A") + "/" + cho.getMaChoNgoi();
            moHinhBang.addRow(new Object[]{
                stt++,
                maChuyenTau,
                toaGhe,
                "[Chưa có]",
                "",
                "[Chưa chọn]",
                "CHỜ NHẬP"
            });
        }
        if (!gheDaChon.isEmpty()) {
            bangDanhSach.setRowSelectionInterval(0, 0);
        }
    }

    
    public static void main(String[] args) {
        ChuyenTau ctMau = new ChuyenTau("C-001");
        List<ChoNgoi> seatsMau = new ArrayList<>();
        seatsMau.add(new ChoNgoi("A-01"));
        seatsMau.add(new ChoNgoi("A-02"));
        seatsMau.add(new ChoNgoi("B-01"));
        try {
            seatsMau.get(0).setToaTau(new entity.ToaTau("TOA-01"));
            seatsMau.get(1).setToaTau(new entity.ToaTau("TOA-01"));
            seatsMau.get(2).setToaTau(new entity.ToaTau("TOA-02"));
        } catch (Exception e) { e.printStackTrace(); }
        NhanVien nvMau = new NhanVien("NV001");
        LookAndFeelManager.setNimbusLookAndFeel();
        SwingUtilities.invokeLater(() -> {
            new ThongTinKhachHangGUI(ctMau, seatsMau, nvMau).setVisible(true);
        });
    }
}