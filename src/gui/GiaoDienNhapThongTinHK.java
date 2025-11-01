package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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
import entity.NhanVien;
import entity.ToaTau;
import entity.UuDai;
import entity.Ve;

public class GiaoDienNhapThongTinHK extends JFrame implements ActionListener {

 
    private final Color MAU_CHU_DAO = new Color(74, 140, 103);
    private final Color MAU_NEN_TIEU_DE = new Color(225, 242, 232);
    private static final Font FONT_NHAN = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_GIA_TRI = new Font("Segoe UI", Font.PLAIN, 14);
    private static final DateTimeFormatter DINH_DANG_NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private BigDecimal tongTienCanThanhToan = BigDecimal.ZERO;
    private Color MAU_NUT_QUAY_LAI = new Color(0, 128, 255);
    
    
    private JTextField txtHoTen, txtSoDienThoai, txtCmndCccd;
    private JDateChooser chonNgaySinh;
    private JComboBox<String> hopChonUuDai;
    private JTable bangDanhSach;
    private DefaultTableModel moHinhBang;

    private JButton nutThemHanhKhach, nutQuayLai, nutTiepTucThanhToan,
            nutChonNguoiThanhToan, nutLamMoi, nutSuaHanhKhach;

    private JLabel lblTongTien;

   
    private ChuyenTau chuyenTauDuocChon;
    private List<ChoNgoi> danhSachChoNgoi;
    private NhanVien nhanVienLap;
    private List<HanhKhach> danhSachHanhKhachDaNhap;
    private HanhKhach nguoiThanhToan;
    private final UuDaiDAO daoUuDai;
    private final HanhKhachDAO daoHanhKhach;
    private final QuanLyVeControl dieuKhienVe;
    private final UuDaiDAO daoLoaiUuDai;

    
    private boolean dangSua = false;
    private int dongDangSua = -1;

   
    public GiaoDienNhapThongTinHK(ChuyenTau ct, List<ChoNgoi> gheDaChon, NhanVien nv) {
        this.chuyenTauDuocChon = ct;
        this.danhSachChoNgoi = gheDaChon != null ? gheDaChon : new ArrayList<>();
        this.nhanVienLap = nv != null ? nv : new NhanVien("NV-001");
        this.daoUuDai = new UuDaiDAO();
        this.daoHanhKhach = new HanhKhachDAO();
        this.dieuKhienVe = new QuanLyVeControl();
        this.daoLoaiUuDai = new UuDaiDAO();

        this.danhSachHanhKhachDaNhap = new ArrayList<>();
        for (ChoNgoi cho : danhSachChoNgoi) {
            if (cho != null && cho.getMaChoNgoi() != null) {
                this.danhSachHanhKhachDaNhap.add(null);
            }
        }

        if (danhSachHanhKhachDaNhap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Danh sách chỗ ngồi không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            SwingUtilities.invokeLater(() -> {
                try {
                    new GiaoDienChonCho(chuyenTauDuocChon, nhanVienLap).setVisible(true);
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi quay lại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
            return;
        }

        khoiTaoThanhPhan();
        thietLapSuKien();
        taiDanhSachUuDai();
        hienThiDanhSachChoNgoiBanDau(danhSachChoNgoi);
        capNhatTongTien();

        setTitle("Nhập Thông Tin Hành Khách");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    
    private GridBagConstraints taoGBC(int x, int y, int fill, int anchor, double weightx, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x; gbc.gridy = y; gbc.fill = fill; gbc.anchor = anchor;
        gbc.weightx = weightx; gbc.insets = insets;
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
        // ----- UI components -----
        txtHoTen = new JTextField(20);
        txtSoDienThoai = new JTextField(15);
        txtCmndCccd = new JTextField(15);
        chonNgaySinh = new JDateChooser();
        hopChonUuDai = new JComboBox<>();
        moHinhBang = new DefaultTableModel(
                new String[]{"STT", "Chuyến", "Toa/Ghế", "Họ tên HK", "Ngày sinh", "Ưu đãi",
                        "Giá TT (VNĐ)", "Tình trạng", "Người TT"}, 0);
        
        bangDanhSach = new JTable(moHinhBang);
        bangDanhSach.setRowHeight(30);

     
        bangDanhSach.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            int row = bangDanhSach.getSelectedRow();
            if (row == -1) return;

           
            String tinhTrang = moHinhBang.getValueAt(row, 7).toString();
            if ("ĐÃ NHẬP".equals(tinhTrang)) {
                
                nutSuaHanhKhach.setEnabled(true);
                nutChonNguoiThanhToan.setEnabled(true);

               
                HanhKhach hk = danhSachHanhKhachDaNhap.get(row);
                if (hk == null) return;

                
                txtHoTen.setText(hk.getHoTen());
                chonNgaySinh.setDate(java.util.Date.from(
                        hk.getNgaySinh().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                txtSoDienThoai.setText(hk.getSoDT() != null ? hk.getSoDT() : "");
                txtCmndCccd.setText(hk.getCmndCccd() != null ? hk.getCmndCccd() : "");

                
                String maUuDai = hk.getMaUuDai();
                for (int i = 0; i < hopChonUuDai.getItemCount(); i++) {
                    String item = hopChonUuDai.getItemAt(i);
                    if (item != null && item.contains("(ID: " + maUuDai + ")")) {
                        hopChonUuDai.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
               
                nutSuaHanhKhach.setEnabled(false);

               
                boolean coDaNhap = false;
                for (int i = 0; i < moHinhBang.getRowCount(); i++) {
                    if ("ĐÃ NHẬP".equals(moHinhBang.getValueAt(i, 7))) {
                        coDaNhap = true;
                        break;
                    }
                }
                nutChonNguoiThanhToan.setEnabled(coDaNhap);

                
                if (dangSua) {
                    int confirm = JOptionPane.showConfirmDialog(
                            GiaoDienNhapThongTinHK.this,
                            "Bạn có muốn hủy sửa hành khách này?",
                            "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        huyCheDoSua();
                    }
                }
            }
        });
        
        nutThemHanhKhach = taoNutChucNang("Thêm Hành Khách", MAU_CHU_DAO.darker(), null, new Dimension(190, 40));
        nutQuayLai = taoNutChucNang("Quay Lại", MAU_NUT_QUAY_LAI, null, new Dimension(150, 40));
        nutChonNguoiThanhToan = taoNutChucNang("Chọn Người TT", new Color(93, 156, 236), null, new Dimension(220, 40));
        nutTiepTucThanhToan = taoNutChucNang("Tiếp Tục Thanh Toán", new Color(103, 192, 144), null, new Dimension(250, 40));
        nutLamMoi = taoNutChucNang("Làm Mới", new Color(255, 184, 0), null, new Dimension(190, 40));
        nutSuaHanhKhach = taoNutChucNang("Sửa TT Hành Khách", new Color(255, 152, 0), null, new Dimension(220, 40));

        lblTongTien = new JLabel("TỔNG CỘNG: 0.00 VNĐ", SwingConstants.RIGHT);
        lblTongTien.setForeground(Color.WHITE);

        // ----- NORTH -----
        JPanel pnlTitleWrapper = new JPanel(new BorderLayout());
        pnlTitleWrapper.setBackground(MAU_NEN_TIEU_DE);
        JLabel lblTieuDeChinh = new JLabel("NHẬP THÔNG TIN HÀNH KHÁCH", SwingConstants.CENTER);
        lblTieuDeChinh.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTieuDeChinh.setForeground(MAU_CHU_DAO);
        pnlTitleWrapper.add(lblTieuDeChinh, BorderLayout.CENTER);
        getContentPane().add(pnlTitleWrapper, BorderLayout.NORTH);

        // ----- WEST (Input Panel) -----
        JPanel pnlThongTinHanhKhach = new JPanel(new BorderLayout());
        pnlThongTinHanhKhach.setPreferredSize(new Dimension(450, 0));
        pnlThongTinHanhKhach.setBackground(Color.WHITE);
        pnlThongTinHanhKhach.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(MAU_CHU_DAO, 2),
                "NHẬP THÔNG TIN KHÁCH HÀNG",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 18), MAU_CHU_DAO));

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

        // Buttons: Thêm | Sửa | Chọn TT
        JPanel pnlNutBam = new JPanel(new GridLayout(2, 2, 15, 10));
        pnlNutBam.setOpaque(false);
        pnlNutBam.add(nutThemHanhKhach);
        pnlNutBam.add(nutSuaHanhKhach);
        pnlNutBam.add(nutChonNguoiThanhToan);

        GridBagConstraints gbcNut = taoGBC(0, dong++, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, 1.0, new Insets(20, 10, 10, 10));
        gbcNut.gridwidth = 2;
        pnlGrid.add(pnlNutBam, gbcNut);
        pnlThongTinHanhKhach.add(pnlGrid, BorderLayout.NORTH);

        // ----- CENTER (Table) -----
        JPanel pnlDanhSachWrapper = new JPanel(new BorderLayout());
        pnlDanhSachWrapper.setBackground(Color.WHITE);
        pnlDanhSachWrapper.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(MAU_CHU_DAO, 2),
                "DANH SÁCH CHỖ NGỒI ĐÃ CHỌN",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 18), MAU_CHU_DAO));
        pnlDanhSachWrapper.add(new JScrollPane(bangDanhSach), BorderLayout.CENTER);

        JPanel pnlBody = new JPanel(new BorderLayout(20, 10));
        pnlBody.setBackground(Color.WHITE);
        pnlBody.setBorder(new EmptyBorder(10, 20, 10, 20));
        pnlBody.add(pnlThongTinHanhKhach, BorderLayout.WEST);
        pnlBody.add(pnlDanhSachWrapper, BorderLayout.CENTER);
        getContentPane().add(pnlBody, BorderLayout.CENTER);

        // ----- SOUTH (Footer) -----
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(MAU_CHU_DAO.darker());
        pnlFooter.setBorder(new EmptyBorder(5, 20, 5, 20));

        JPanel pnlFooterWest = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlFooterWest.setOpaque(false);
        pnlFooterWest.add(nutQuayLai);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pnlFooterWest.add(lblTongTien);

        JPanel pnlFooterEast = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlFooterEast.setOpaque(false);
        pnlFooterEast.add(nutTiepTucThanhToan);

        nutTiepTucThanhToan.setEnabled(false);
        nutChonNguoiThanhToan.setEnabled(false);
        nutSuaHanhKhach.setEnabled(false);

        pnlFooter.add(pnlFooterWest, BorderLayout.WEST);
        pnlFooter.add(pnlFooterEast, BorderLayout.EAST);
        getContentPane().add(pnlFooter, BorderLayout.SOUTH);

    }        

    private void huyCheDoSua() {
        dangSua = false;
        dongDangSua = -1;
        nutSuaHanhKhach.setText("Sửa TT Hành Khách");
        nutThemHanhKhach.setEnabled(true);
        xuLyLamMoiForm();
    }

    /* ====================== EVENT SETUP ====================== */
    private void thietLapSuKien() {
        nutThemHanhKhach.addActionListener(this);
        nutQuayLai.addActionListener(this);
        nutTiepTucThanhToan.addActionListener(this);
        nutChonNguoiThanhToan.addActionListener(this);
        nutLamMoi.addActionListener(this);
        nutSuaHanhKhach.addActionListener(this);
    }

    /* ====================== TABLE INITIAL DATA ====================== */
    private void hienThiDanhSachChoNgoiBanDau(List<ChoNgoi> gheDaChon) {
        moHinhBang.setRowCount(0);
        for (int i = 0; i < gheDaChon.size(); i++) {
            ChoNgoi cho = gheDaChon.get(i);
            if (cho == null) continue;
            String maToa = (cho.getToaTau() != null) ? cho.getToaTau().getMaToa() : "N/A";
            String toaGhe = maToa + "/" + cho.getMaChoNgoi();
            moHinhBang.addRow(new Object[]{
                    i + 1,
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
        if (moHinhBang.getRowCount() > 0) {
            bangDanhSach.setRowSelectionInterval(0, 0);
        }
    }

    /* ====================== TỔNG TIỀN ====================== */
    private void capNhatTongTien() {
        tongTienCanThanhToan = BigDecimal.ZERO;
        for (int i = 0; i < moHinhBang.getRowCount(); i++) {
            String giaStr = moHinhBang.getValueAt(i, 6).toString()
                    .replaceAll("\\.", "").replace(",", ".").replace(" VNĐ", "");
            try {
                BigDecimal gia = new BigDecimal(giaStr);
                tongTienCanThanhToan = tongTienCanThanhToan.add(gia);
            } catch (Exception ignored) {}
        }
        lblTongTien.setText("TỔNG CỘNG: " + String.format("%,.2f VNĐ", tongTienCanThanhToan));
    }

    /* ====================== CLEAR FORM ====================== */
    private void xuLyLamMoiForm() {
        txtHoTen.setText("");
        chonNgaySinh.setDate(null);
        txtSoDienThoai.setText("");
        txtCmndCccd.setText("");
        hopChonUuDai.setSelectedIndex(0);
        txtHoTen.requestFocus();
    }

    /* ====================== GIÁ VÉ CƠ BẢN ====================== */
    private BigDecimal layGiaVeCoBan(ChoNgoi choNgoi) {
        BigDecimal giaChuyen = chuyenTauDuocChon.getGiaChuyen();
        ToaTau toa = choNgoi.getToaTau();
        if (toa != null && toa.getHeSoGia() != null) {
            return giaChuyen.multiply(toa.getHeSoGia()).setScale(2, RoundingMode.HALF_UP);
        }
        return giaChuyen.setScale(2, RoundingMode.HALF_UP);
    }

    /* ====================== LOAD ƯU ĐÃI ====================== */
    private void taiDanhSachUuDai() {
        hopChonUuDai.removeAllItems();
        hopChonUuDai.addItem("--- Chọn Ưu đãi ---");
        List<UuDai> listUd = daoUuDai.layTatCaUuDai();
        for (UuDai ud : listUd) {
            String ma = ud.getMaUuDai();
            BigDecimal mucGiam = ud.getMucGiamGia();
            String tenLoai = "";
            try {
                tenLoai = daoLoaiUuDai.getTenLoaiByID(ud.getIDloaiUuDai());
            } catch (SQLException e) { tenLoai = "LỖI TẢI LOẠI"; }

            String mucGiamStr;
            if (mucGiam.compareTo(BigDecimal.ZERO) == 0) mucGiamStr = "Nguyên giá";
            else if (mucGiam.compareTo(new BigDecimal("100")) == 0) mucGiamStr = "Miễn phí";
            else mucGiamStr = "Giảm " + mucGiam.setScale(0, RoundingMode.HALF_UP) + "%";

            String tenHienThi = mucGiamStr + " - " + tenLoai + " (ID: " + ma + ")";
            hopChonUuDai.addItem(tenHienThi);
        }
    }

    private String layMaUuDaiTuTenFull(String tenFull) {
        if (tenFull == null || tenFull.contains("---")) return null;
        try {
            return tenFull.split("\\(ID: ")[1].replace(")", "").trim();
        } catch (Exception e) { return null; }
    }

    private boolean kiemTraTatCaDaNhap() {
        for (int i = 0; i < moHinhBang.getRowCount(); i++) {
            if ("CHỜ NHẬP".equals(moHinhBang.getValueAt(i, 7))) return false;
        }
        return true;
    }

    /* ====================== THÊM / SỬA HÀNH KHÁCH ====================== */
    private void xuLyThemHanhKhach() {
        int dongDuocChon = bangDanhSach.getSelectedRow();
        if (dongDuocChon < 0 || !"CHỜ NHẬP".equals(moHinhBang.getValueAt(dongDuocChon, 7))) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chỗ ngồi CHỜ NHẬP để nhập thông tin.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (dangSua) huyCheDoSua();

        batDauXuLyThemHoacSua(dongDuocChon);
    }

    /** Hàm chung thực hiện Thêm hoặc Sửa */
    private void batDauXuLyThemHoacSua(int dongDuocChon) {
        nutThemHanhKhach.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<Void, Void>() {
            String validationError = null;
            HanhKhach hkKetQua = null;
            BigDecimal giaThanhToan = null;
            ChoNgoi choNgoiDangChon = danhSachChoNgoi.get(dongDuocChon);
            String tenLoaiUuDaiFull = (String) hopChonUuDai.getSelectedItem();

            @Override
            protected Void doInBackground() throws Exception {
                String hoTen = txtHoTen.getText().trim();
                java.util.Date ngaySinhDate = chonNgaySinh.getDate();
                String sdt = txtSoDienThoai.getText().trim();
                String cmnd = txtCmndCccd.getText().trim();

                if (hoTen.isEmpty() || ngaySinhDate == null || tenLoaiUuDaiFull == null || tenLoaiUuDaiFull.contains("---")) {
                    validationError = "Vui lòng nhập đầy đủ Họ tên, Ngày sinh và chọn Ưu đãi.";
                    return null;
                }

                String maUuDai = layMaUuDaiTuTenFull(tenLoaiUuDaiFull);
                LocalDate ngaySinh = ngaySinhDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                boolean laTreEm = "UD-02".equals(maUuDai) || "UD-03".equals(maUuDai);
                String cccdChecked = cmnd.isEmpty() ? "" : cmnd;
                String sdtChecked = sdt.isEmpty() ? "" : sdt;

                if (laTreEm) {
                    long age = java.time.temporal.ChronoUnit.YEARS.between(ngaySinh, LocalDate.now());
                    if ("UD-02".equals(maUuDai) && age >= 6) {
                        validationError = "Ưu đãi UD-02 chỉ áp dụng cho trẻ em dưới 6 tuổi.";
                        return null;
                    } else if ("UD-03".equals(maUuDai) && (age < 6 || age > 10)) {
                        validationError = "Ưu đãi UD-03 chỉ áp dụng cho trẻ em từ 6-10 tuổi.";
                        return null;
                    }
                    cccdChecked = "";
                }

                if (!cmnd.isEmpty() && !laTreEm && !cccdChecked.matches("^(\\d{9}|\\d{12})$")) {
                    validationError = "CMND/CCCD phải là 9 hoặc 12 chữ số.";
                    return null;
                }
                if (!sdt.isEmpty() && !sdt.matches("^0\\d{9}$")) {
                    validationError = "SĐT phải là 10 chữ số bắt đầu bằng 0.";
                    return null;
                }

                HanhKhach hkMoi = new HanhKhach(null, hoTen, cccdChecked, sdtChecked, ngaySinh, maUuDai);

                if (dangSua) {
                    HanhKhach hkCu = danhSachHanhKhachDaNhap.get(dongDangSua);
                    hkCu.setHoTen(hoTen);
                    hkCu.setNgaySinh(ngaySinh);
                    hkCu.setSoDT(sdtChecked);
                    hkCu.setMaUuDai(maUuDai);
                    daoHanhKhach.capNhatHanhKhach(hkCu);
                    hkKetQua = hkCu;
                } else {
                    HanhKhach hkTonTai = null;
                    if (!hkMoi.getCmndCccd().isEmpty()) {
                        hkTonTai = daoHanhKhach.layHanhKhachTheoCMND(hkMoi.getCmndCccd());
                    }
                    if (hkTonTai == null && !laTreEm && !hkMoi.getSoDT().isEmpty()) {
                        hkTonTai = daoHanhKhach.layHanhKhachTheoSDT(hkMoi.getSoDT());
                    }

                    if (hkTonTai == null) {
                        daoHanhKhach.themHanhKhach(hkMoi);
                        hkKetQua = hkMoi;
                    } else {
                        if (!sdtChecked.isEmpty() && !sdtChecked.equals(hkTonTai.getSoDT())) {
                            hkTonTai.setSoDT(sdtChecked);
                        }
                        hkTonTai.setHoTen(hoTen);
                        hkTonTai.setNgaySinh(ngaySinh);
                        hkTonTai.setMaUuDai(maUuDai);
                        daoHanhKhach.capNhatHanhKhach(hkTonTai);
                        hkKetQua = hkTonTai;
                    }
                }

                BigDecimal giaVeGoc = layGiaVeCoBan(choNgoiDangChon);
                giaThanhToan = dieuKhienVe.tinhGiaVeCuoiCung(giaVeGoc, hkKetQua.getMaUuDai(), null);
                return null;
            }

            @Override
            protected void done() {
                nutThemHanhKhach.setEnabled(true);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                try {
                    get();
                    if (validationError != null) {
                        JOptionPane.showMessageDialog(GiaoDienNhapThongTinHK.this, validationError, "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (hkKetQua != null && giaThanhToan != null) {
                        String giaFmt = String.format("%,.2f VNĐ", giaThanhToan.setScale(2, RoundingMode.HALF_UP));
                        int row = dangSua ? dongDangSua : dongDuocChon;

                        danhSachHanhKhachDaNhap.set(row, hkKetQua);
                        moHinhBang.setValueAt(hkKetQua.getHoTen(), row, 3);
                        moHinhBang.setValueAt(hkKetQua.getNgaySinh().format(DINH_DANG_NGAY), row, 4);
                        moHinhBang.setValueAt(tenLoaiUuDaiFull, row, 5);
                        moHinhBang.setValueAt(giaFmt, row, 6);
                        moHinhBang.setValueAt("ĐÃ NHẬP", row, 7);
                        if (nguoiThanhToan != null && nguoiThanhToan.equals(hkKetQua)) {
                            moHinhBang.setValueAt("Người TT", row, 8);
                        } else {
                            moHinhBang.setValueAt("Khách", row, 8);
                        }

                        capNhatTongTien();
                        bangDanhSach.clearSelection(); 
                        xuLyLamMoiForm();

                        if (dangSua) {
                            dangSua = false;
                            dongDangSua = -1;
                            nutSuaHanhKhach.setText("Sửa TT Hành Khách");
                            nutChonNguoiThanhToan.setEnabled(true);
                        }

                        if (!dangSua && row < bangDanhSach.getRowCount() - 1) {
                            bangDanhSach.setRowSelectionInterval(row + 1, row + 1);
                        }

                        if (kiemTraTatCaDaNhap()) {
                            nutTiepTucThanhToan.setEnabled(nguoiThanhToan != null);
                        }

                        // Bật lại nút Chọn Người TT nếu có ĐÃ NHẬP
                        boolean coDaNhap = false;
                        for (int i = 0; i < moHinhBang.getRowCount(); i++) {
                            if ("ĐÃ NHẬP".equals(moHinhBang.getValueAt(i, 7))) {
                                coDaNhap = true;
                                break;
                            }
                        }
                        nutChonNguoiThanhToan.setEnabled(coDaNhap);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(GiaoDienNhapThongTinHK.this,
                            "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    /* ====================== CHỌN NGƯỜI THANH TOÁN ====================== */
    private void xuLyChonNguoiThanhToan() {
        int dong = bangDanhSach.getSelectedRow();
        if (dong < 0 || danhSachHanhKhachDaNhap.get(dong) == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hành khách đã nhập để chỉ định là người thanh toán.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        HanhKhach hk = danhSachHanhKhachDaNhap.get(dong);
        if ("UD-02".equals(hk.getMaUuDai()) || "UD-03".equals(hk.getMaUuDai())) {
            JOptionPane.showMessageDialog(this, "Trẻ em (UD-02/UD-03) không thể là người thanh toán.",
                    "Lỗi nghiệp vụ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        nguoiThanhToan = hk;
        for (int i = 0; i < moHinhBang.getRowCount(); i++) {
            moHinhBang.setValueAt("Khách", i, 8);
        }
        moHinhBang.setValueAt("Người TT", dong, 8);

        JOptionPane.showMessageDialog(this, "Đã chỉ định " + hk.getHoTen() + " là người thanh toán.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        if (kiemTraTatCaDaNhap()) {
            nutTiepTucThanhToan.setEnabled(true);
        }
    }

    /* ====================== CHUYỂN SANG THANH TOÁN ====================== */
    private void xuLyChuyenSangThanhToan() {
        if (!kiemTraTatCaDaNhap() || nguoiThanhToan == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ và chọn người thanh toán.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Ve> dsVe = new ArrayList<>();
        try {
            for (int i = 0; i < danhSachChoNgoi.size(); i++) {
                ChoNgoi cho = danhSachChoNgoi.get(i);
                HanhKhach hk = danhSachHanhKhachDaNhap.get(i);
                String giaStr = moHinhBang.getValueAt(i, 6).toString()
                        .replaceAll("\\.", "").replace(",", ".").replace(" VNĐ", "");
                BigDecimal giaTT = new BigDecimal(giaStr);
                if (giaTT.compareTo(BigDecimal.ZERO) <= 0) giaTT = new BigDecimal("0.01");

                Ve ve = new Ve();
                ve.setNgayDat(LocalDateTime.now());
                ve.setTrangThai("Đã đặt");
                ve.setGiaVeGoc(layGiaVeCoBan(cho));
                ve.setGiaThanhToan(giaTT);
                ve.setMaChoNgoi(cho);
                ve.setMaChuyenTau(chuyenTauDuocChon);
                ve.setMaHanhkhach(hk);
                ve.setMaKhuyenMai(null);
                ve.setMaNhanVien(nhanVienLap);
                dsVe.add(ve);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tạo vé: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            new GiaoDienThanhToan(dsVe, nguoiThanhToan, nhanVienLap, this).setVisible(true);
            this.setVisible(false);
        });
    }

    /* ====================== ACTION LISTENER ====================== */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == nutThemHanhKhach) {
            xuLyThemHanhKhach();
        } else if (src == nutSuaHanhKhach) {
            if (!dangSua) {
                int row = bangDanhSach.getSelectedRow();
                if (row >= 0 && "ĐÃ NHẬP".equals(moHinhBang.getValueAt(row, 7))) {
                    dangSua = true;
                    dongDangSua = row;
                    nutSuaHanhKhach.setText("Cập Nhật Sửa");
                    nutThemHanhKhach.setEnabled(false);
                    nutChonNguoiThanhToan.setEnabled(false);
                    txtHoTen.requestFocus();
                    txtHoTen.selectAll();
                }
            } else {
                int row = bangDanhSach.getSelectedRow();
                if (row >= 0) {
                    batDauXuLyThemHoacSua(row);
                }
            }
        } else if (src == nutChonNguoiThanhToan) {
            xuLyChonNguoiThanhToan();
        } else if (src == nutTiepTucThanhToan) {
            xuLyChuyenSangThanhToan();
        } else if (src == nutQuayLai) {
            SwingUtilities.invokeLater(() -> {
                try {
                    new GiaoDienChonCho(chuyenTauDuocChon, nhanVienLap).setVisible(true);
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi quay lại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else if (src == nutLamMoi) {
            xuLyLamMoiForm();
        }
    }
    
    
}