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
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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

    // Các thành phần giao diện
    private JTextField txtHoTen, txtSoDienThoai, txtCmndCccd;
    private JDateChooser chonNgaySinh;
    private JComboBox<String> hopChonUuDai;
    private JTable tblCho;
    private DefaultTableModel modelDSCho;

    private JButton btnThem, btnTroVe, btnTiepTuc, btnChonNTT, btnLamMoi, btnSua;
    private JLabel lblTongTien;

    // Dữ liệu
    private ChuyenTau chuyenTauDuocChon;
    private List<ChoNgoi> danhSachChoNgoi;
    private NhanVien nhanVienLap;
    private List<HanhKhach> danhSachHanhKhachDaNhap;
    private HanhKhach nguoiThanhToan;
    private final UuDaiDAO daoUuDai;
    private final HanhKhachDAO daoHanhKhach;
    private final QuanLyVeControl dieuKhienVe;

    private boolean dangSua = false;
    private int dongDangSua = -1;
    private BigDecimal tongTienCanThanhToan = BigDecimal.ZERO;

    public GiaoDienNhapThongTinHK(ChuyenTau ct, List<ChoNgoi> gheDaChon, NhanVien nv) {
        this.chuyenTauDuocChon = ct;
        this.danhSachChoNgoi = gheDaChon != null ? gheDaChon : new ArrayList<>();
        this.nhanVienLap = nv != null ? nv : new NhanVien("NV-001");
        this.daoUuDai = new UuDaiDAO();
        this.daoHanhKhach = new HanhKhachDAO();
        this.dieuKhienVe = new QuanLyVeControl();

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

        setTitle("Nhập Thông Tin Hành Khách");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Khởi tạo giao diện
        txtHoTen = new JTextField(20);
        txtSoDienThoai = new JTextField(15);
        txtCmndCccd = new JTextField(15);
        chonNgaySinh = new JDateChooser();
        hopChonUuDai = new JComboBox<>();
        modelDSCho = new DefaultTableModel(
                new String[]{"STT", "Chuyến", "Toa/Ghế", "Họ tên HK", "Ngày sinh", "Ưu đãi", "Giá TT (VNĐ)", "Tình trạng", "Người TT"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblCho = new JTable(modelDSCho);
        tblCho.setRowHeight(28);
        tblCho.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblCho.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblCho.setSelectionBackground(new Color(58, 111, 67));

        tblCho.getColumnModel().getColumn(0).setMaxWidth(40);
        tblCho.getColumnModel().getColumn(1).setMaxWidth(80);
        tblCho.getColumnModel().getColumn(3).setMinWidth(180);

        xuLyChonDongTrongBang();

        btnThem = taoButton("Thêm", new Color(46, 204, 113), "/img/plus2.png");
        btnChonNTT = taoButton("Chọn Người TT", new Color(241, 196, 15), "/img/choose.png");
        btnLamMoi = taoButton("Làm Mới", new Color(255, 184, 0), "/img/undo2.png");
        btnSua = taoButton("Sửa TT Hành Khách", new Color(187, 102, 83), "/img/repair.png");

        lblTongTien = new JLabel("TỔNG CỘNG: 0.00 VNĐ", SwingConstants.RIGHT);
        lblTongTien.setForeground(Color.BLACK);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 24));

        // Tiêu đề
        JPanel pnlTitleWrapper = new JPanel(new BorderLayout());
        pnlTitleWrapper.setBackground(new Color(225, 242, 232));
        JLabel lblTieuDeChinh = new JLabel("NHẬP THÔNG TIN HÀNH KHÁCH", SwingConstants.CENTER);
        lblTieuDeChinh.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        lblTieuDeChinh.setFont(new Font("Segoe UI", Font.BOLD, 44));
        lblTieuDeChinh.setForeground(new Color(74, 140, 103));
        pnlTitleWrapper.add(lblTieuDeChinh, BorderLayout.CENTER);
        add(pnlTitleWrapper, BorderLayout.NORTH);

        // Form nhập
        JPanel pnlThongTinHanhKhach = new JPanel(new BorderLayout(10, 10));
        pnlThongTinHanhKhach.setPreferredSize(new Dimension(500, 0));
        pnlThongTinHanhKhach.setBackground(new Color(245, 247, 250));

        TitledBorder titleBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "NHẬP THÔNG TIN HÀNH KHÁCH",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 24), Color.BLACK);
        pnlThongTinHanhKhach.setBorder(titleBorder);

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setBackground(Color.WHITE);

        Font lblFont = new Font("Segoe UI", Font.BOLD, 19);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 20);

        JLabel lblHoTen = new JLabel("Họ tên:");
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        JLabel lblSoDT = new JLabel("SĐT:");
        JLabel lblCmndCccd = new JLabel("CMND/CCCD:");
        JLabel lblChonUuDai = new JLabel("Chọn Ưu đãi:");

        JLabel[] labels = {lblHoTen, lblNgaySinh, lblSoDT, lblCmndCccd, lblChonUuDai};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.LEFT);
        }

        chonNgaySinh.setPreferredSize(new Dimension(240, 35));
        hopChonUuDai.setPreferredSize(new Dimension(240, 35));

        JComponent[] fields = {txtHoTen, chonNgaySinh, txtSoDienThoai, txtCmndCccd, hopChonUuDai};
        for (JComponent comp : fields) {
            if (comp instanceof JTextField) {
                JTextField txt = (JTextField) comp;
                txt.setFont(txtFont);
                txt.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(150, 150, 150)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
            } else if (comp instanceof JComboBox) {
                ((JComboBox<?>) comp).setFont(txtFont);
            } else if (comp instanceof JDateChooser) {
                JDateChooser dc = (JDateChooser) comp;
                dc.setFont(txtFont);
                dc.getComponent(0).setFont(txtFont);
            }
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4; gbc.anchor = GridBagConstraints.EAST;
        pnlForm.add(lblHoTen, gbc);
        gbc.gridx = 1; gbc.weightx = 0.6; gbc.anchor = GridBagConstraints.WEST;
        pnlForm.add(txtHoTen, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlForm.add(lblNgaySinh, gbc);
        gbc.gridx = 1;
        pnlForm.add(chonNgaySinh, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnlForm.add(lblSoDT, gbc);
        gbc.gridx = 1;
        pnlForm.add(txtSoDienThoai, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        pnlForm.add(lblCmndCccd, gbc);
        gbc.gridx = 1;
        pnlForm.add(txtCmndCccd, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        pnlForm.add(lblChonUuDai, gbc);
        gbc.gridx = 1;
        pnlForm.add(hopChonUuDai, gbc);

        JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnChonNTT);
        pnlButtons.add(btnLamMoi);

        pnlThongTinHanhKhach.add(pnlForm, BorderLayout.CENTER);
        pnlThongTinHanhKhach.add(pnlButtons, BorderLayout.SOUTH);

        // Bảng danh sách ghế
        JPanel pnlDanhSachWrapper = new JPanel(new BorderLayout());
        pnlDanhSachWrapper.setBackground(Color.WHITE);
        pnlDanhSachWrapper.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.BLACK, 2),
                "DANH SÁCH CHỖ NGỒI ĐÃ CHỌN",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 24), Color.BLACK));
        pnlDanhSachWrapper.add(new JScrollPane(tblCho), BorderLayout.CENTER);

        JPanel pnlBody = new JPanel(new BorderLayout(20, 10));
        pnlBody.setBackground(Color.WHITE);
        pnlBody.setBorder(new EmptyBorder(10, 20, 10, 20));
        pnlBody.add(pnlThongTinHanhKhach, BorderLayout.WEST);
        pnlBody.add(pnlDanhSachWrapper, BorderLayout.CENTER);
        add(pnlBody, BorderLayout.CENTER);

        // Footer
        btnTroVe = taoButton("Trở Về", new Color(41, 128, 185), "/img/back.png");
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnTroVe.setPreferredSize(new Dimension(160, 60));

        btnTiepTuc = taoButton("Tiếp Tục", new Color(103, 192, 144), "/img/payment.png");
        btnTiepTuc.setForeground(Color.WHITE);
        btnTiepTuc.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnTiepTuc.setPreferredSize(new Dimension(160, 60));

        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel pnlFooterWest = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlFooterWest.setOpaque(false);
        pnlFooterWest.add(btnTroVe);
        pnlFooterWest.add(lblTongTien);

        JPanel pnlFooterEast = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlFooterEast.setOpaque(false);
        pnlFooterEast.add(btnTiepTuc);

        btnTiepTuc.setEnabled(false);
        btnChonNTT.setEnabled(false);
        btnSua.setEnabled(false);

        pnlFooter.add(pnlFooterWest, BorderLayout.WEST);
        pnlFooter.add(pnlFooterEast, BorderLayout.EAST);
        add(pnlFooter, BorderLayout.SOUTH);

        // Gán sự kiện
        btnThem.addActionListener(this);
        btnTroVe.addActionListener(this);
        btnTiepTuc.addActionListener(this);
        btnChonNTT.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnSua.addActionListener(this);

        // Tải dữ liệu
        taiDanhSachUuDai();
        hienThiDanhSachChoNgoiBanDau(danhSachChoNgoi);
        capNhatTongTien();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnThem) {
            xuLyThemHanhKhach();
        } else if (src == btnSua) {
            if (!dangSua) {
                batDauCheDoSua();
            } else {
                xuLySuaHanhKhach();
            }
        } else if (src == btnChonNTT) {
            xuLyChonNguoiThanhToan();
        } else if (src == btnTiepTuc) {
            xuLyChuyenSangThanhToan();
        } else if (src == btnTroVe) {
            quayLaiManHinhChonCho();
        } else if (src == btnLamMoi) {
            xuLyLamMoiForm();
        }
    }

    private void xuLyThemHanhKhach() {
        int dong = tblCho.getSelectedRow();
        if (dong < 0 || !"CHỜ NHẬP".equals(modelDSCho.getValueAt(dong, 7))) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chỗ ngồi CHỜ NHẬP.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnThem.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<Void, Void>() {
            String error = null;
            HanhKhach hkKetQua = null;
            BigDecimal giaTT = null;
            String tenUD = null;

            @Override
            protected Void doInBackground() throws Exception {
                try {
                    String hoTen = txtHoTen.getText().trim();
                    java.util.Date ngaySinhDate = chonNgaySinh.getDate();
                    String sdt = txtSoDienThoai.getText().trim();
                    String cmnd = txtCmndCccd.getText().trim();
                    tenUD = (String) hopChonUuDai.getSelectedItem();

                    if (hoTen.isEmpty() || ngaySinhDate == null || tenUD == null || tenUD.contains("--- Chọn")) {
                        error = "Vui lòng nhập đầy đủ Họ tên, Ngày sinh và chọn Ưu đãi hợp lệ.";
                        return null;
                    }

                    String maUuDai = layMaUuDaiTuTenFull(tenUD);
                    if (maUuDai == null || maUuDai.isEmpty()) maUuDai = "UD-01";

                    LocalDate ngaySinh = ngaySinhDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate now = LocalDate.now();
                    if (!ngaySinh.isBefore(now)) {
                        error = "Ngày sinh không hợp lệ.";
                        return null;
                    }

                    int tuoi = java.time.Period.between(ngaySinh, now).getYears();
                    boolean laTreEm = "UD-02".equals(maUuDai) || "UD-03".equals(maUuDai);

                    if (tuoi < 14) {
                        if (!laTreEm) { error = "Trẻ dưới 14 tuổi phải chọn ưu đãi trẻ em."; return null; }
                    } else {
                        if (laTreEm) { error = "Ưu đãi trẻ em chỉ dành cho dưới 14 tuổi."; return null; }
                        if (cmnd.isEmpty() || sdt.isEmpty()) {
                            error = "Người lớn phải nhập CMND và SĐT.";
                            return null;
                        }
                    }

                    if (!cmnd.isEmpty() && !cmnd.matches("^(\\d{9}|\\d{12})$")) {
                        error = "CMND/CCCD phải là 9 hoặc 12 số.";
                        return null;
                    }
                    if (!sdt.isEmpty() && !sdt.matches("^0\\d{9}$")) {
                        error = "SĐT phải bắt đầu bằng 0, 10 số.";
                        return null;
                    }

                    HanhKhach hkMoi = new HanhKhach(null, hoTen, cmnd, sdt, ngaySinh, maUuDai);

                    HanhKhach hkTonTai = null;
                    if (!cmnd.isEmpty()) hkTonTai = daoHanhKhach.layHanhKhachTheoCMND(cmnd);
                    if (hkTonTai == null && !sdt.isEmpty() && !laTreEm)
                        hkTonTai = daoHanhKhach.layHanhKhachTheoSDT(sdt);

                    if (hkTonTai == null) {
                        daoHanhKhach.themHanhKhach(hkMoi);
                        hkKetQua = hkMoi;
                    } else {
                        hkTonTai.setHoTen(hoTen); hkTonTai.setNgaySinh(ngaySinh);
                        hkTonTai.setMaUuDai(maUuDai); if (!sdt.isEmpty()) hkTonTai.setSoDT(sdt);
                        daoHanhKhach.capNhatHanhKhach(hkTonTai);
                        hkKetQua = hkTonTai;
                    }

                    ChoNgoi cho = danhSachChoNgoi.get(dong);
                    BigDecimal giaGoc = layGiaVeCoBan(cho);
                    giaTT = dieuKhienVe.tinhGiaVeCuoiCung(giaGoc, maUuDai, null);

                } catch (Exception ex) {
                    error = ex.getMessage();
                }
                return null;
            }

            @Override
            protected void done() {
                btnThem.setEnabled(true);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if (error != null) {
                    JOptionPane.showMessageDialog(GiaoDienNhapThongTinHK.this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                capNhatBangSauThem(dong, hkKetQua, tenUD, giaTT);
            }
        }.execute();
    }

    private void xuLySuaHanhKhach() {
        int dong = tblCho.getSelectedRow();
        if (dong < 0 || !dangSua) return;

        btnSua.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<Void, Void>() {
            String error = null;
            BigDecimal giaTT = null;
            String tenUD = null;

            @Override
            protected Void doInBackground() throws Exception {
                try {
                    String hoTen = txtHoTen.getText().trim();
                    java.util.Date ngaySinhDate = chonNgaySinh.getDate();
                    String sdt = txtSoDienThoai.getText().trim();
                    String cmnd = txtCmndCccd.getText().trim();
                    tenUD = (String) hopChonUuDai.getSelectedItem();

                    if (hoTen.isEmpty() || ngaySinhDate == null || tenUD == null || tenUD.contains("--- Chọn")) {
                        error = "Vui lòng nhập đầy đủ thông tin để sửa.";
                        return null;
                    }

                    String maUuDai = layMaUuDaiTuTenFull(tenUD);
                    if (maUuDai == null || maUuDai.isEmpty()) maUuDai = "UD-01";

                    LocalDate ngaySinh = ngaySinhDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    HanhKhach hk = danhSachHanhKhachDaNhap.get(dongDangSua);
                    hk.setHoTen(hoTen); hk.setNgaySinh(ngaySinh);
                    hk.setSoDT(sdt); hk.setCmndCccd(cmnd);
                    hk.setMaUuDai(maUuDai);

                    daoHanhKhach.capNhatHanhKhach(hk);

                    ChoNgoi cho = danhSachChoNgoi.get(dong);
                    BigDecimal giaGoc = layGiaVeCoBan(cho);
                    giaTT = dieuKhienVe.tinhGiaVeCuoiCung(giaGoc, maUuDai, null);

                } catch (Exception ex) {
                    error = ex.getMessage();
                }
                return null;
            }

            @Override
            protected void done() {
                btnSua.setEnabled(true);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if (error != null) {
                    JOptionPane.showMessageDialog(GiaoDienNhapThongTinHK.this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                capNhatBangSauSua(dongDangSua, tenUD, giaTT);
                ketThucCheDoSua();
            }
        }.execute();
    }

    private void batDauCheDoSua() {
        int row = tblCho.getSelectedRow();
        if (row < 0 || !"ĐÃ NHẬP".equals(modelDSCho.getValueAt(row, 7))) return;

        dangSua = true;
        dongDangSua = row;
        btnSua.setText("Cập Nhật Sửa");
        btnThem.setEnabled(false);
        btnChonNTT.setEnabled(false);
        txtHoTen.requestFocus();
        txtHoTen.selectAll();
    }

    private void ketThucCheDoSua() {
        dangSua = false;
        dongDangSua = -1;
        btnSua.setText("Sửa TT Hành Khách");
        btnThem.setEnabled(true);
        btnChonNTT.setEnabled(true);
        xuLyLamMoiForm();
    }

    private void capNhatBangSauThem(int row, HanhKhach hk, String tenUD, BigDecimal gia) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String giaFmt = String.format("%,.2f VNĐ", gia);
        modelDSCho.setValueAt(hk.getHoTen(), row, 3);
        modelDSCho.setValueAt(hk.getNgaySinh().format(fmt), row, 4);
        modelDSCho.setValueAt(tenUD, row, 5);
        modelDSCho.setValueAt(giaFmt, row, 6);
        modelDSCho.setValueAt("ĐÃ NHẬP", row, 7);
        modelDSCho.setValueAt(nguoiThanhToan != null && nguoiThanhToan.equals(hk) ? "Người TT" : "Khách", row, 8);
        danhSachHanhKhachDaNhap.set(row, hk);
        capNhatTongTien();
        xuLyLamMoiForm();
        if (row < tblCho.getRowCount() - 1) tblCho.setRowSelectionInterval(row + 1, row + 1);
        if (kiemTraTatCaDaNhap()) btnTiepTuc.setEnabled(nguoiThanhToan != null);
        btnChonNTT.setEnabled(true);
    }

    private void capNhatBangSauSua(int row, String tenUD, BigDecimal gia) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String giaFmt = String.format("%,.2f VNĐ", gia);
        HanhKhach hk = danhSachHanhKhachDaNhap.get(row);
        modelDSCho.setValueAt(hk.getHoTen(), row, 3);
        modelDSCho.setValueAt(hk.getNgaySinh().format(fmt), row, 4);
        modelDSCho.setValueAt(tenUD, row, 5);
        modelDSCho.setValueAt(giaFmt, row, 6);
        modelDSCho.setValueAt(nguoiThanhToan != null && nguoiThanhToan.equals(hk) ? "Người TT" : "Khách", row, 8);
        capNhatTongTien();
        xuLyLamMoiForm();
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
        List<UuDai> listUd = daoUuDai.layTatCaUuDai();
        for (UuDai ud : listUd) {
            String ma = ud.getMaUuDai();
            BigDecimal mucGiam = ud.getMucGiamGia();
            String tenLoai = "";
            try {
                tenLoai = new UuDaiDAO().getTenLoaiByID(ud.getIDloaiUuDai());
            } catch (SQLException e) { tenLoai = "LỖI TẢI LOẠI"; }

            String mucGiamStr;
            if (mucGiam.compareTo(BigDecimal.ZERO) == 0) mucGiamStr = "Nguyên giá";
            else if (mucGiam.compareTo(new BigDecimal("100")) == 0) mucGiamStr = "Miễn phí";
            else mucGiamStr = "Giảm " + mucGiam.setScale(0, RoundingMode.HALF_UP) + "%";

            String tenHienThi = mucGiamStr + " - " + tenLoai + " (ID: " + ma + ")";
            hopChonUuDai.addItem(tenHienThi);
        }
        hopChonUuDai.insertItemAt("--- Chọn Ưu đãi ---", 0);
        for (int i = 0; i < hopChonUuDai.getItemCount(); i++) {
            if (hopChonUuDai.getItemAt(i).contains("Nguyên giá") && hopChonUuDai.getItemAt(i).contains("UD-01")) {
                hopChonUuDai.setSelectedIndex(i);
                break;
            }
        }
        if (hopChonUuDai.getSelectedIndex() == -1) hopChonUuDai.setSelectedIndex(0);
    }

    private String layMaUuDaiTuTenFull(String tenFull) {
        if (tenFull == null || tenFull.contains("--- Chọn") || tenFull.trim().isEmpty()) {
            return "UD-01";
        }
        try {
            String[] parts = tenFull.split("\\(ID: ");
            if (parts.length < 2) return "UD-01";
            String idPart = parts[1].replace(")", "").trim();
            return idPart.isEmpty() ? "UD-01" : idPart;
        } catch (Exception e) {
            return "UD-01";
        }
    }

    private boolean kiemTraTatCaDaNhap() {
        for (int i = 0; i < modelDSCho.getRowCount(); i++) {
            if ("CHỜ NHẬP".equals(modelDSCho.getValueAt(i, 7))) return false;
        }
        return true;
    }

    private void hienThiDanhSachChoNgoiBanDau(List<ChoNgoi> gheDaChon) {
        modelDSCho.setRowCount(0);
        for (int i = 0; i < gheDaChon.size(); i++) {
            ChoNgoi cho = gheDaChon.get(i);
            if (cho == null) continue;
            String maToa = (cho.getToaTau() != null) ? cho.getToaTau().getMaToa() : "N/A";
            String toaGhe = maToa + "/" + cho.getMaChoNgoi();
            modelDSCho.addRow(new Object[]{
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
        if (modelDSCho.getRowCount() > 0) {
            tblCho.setRowSelectionInterval(0, 0);
        }
    }

    private BigDecimal parseGiaFromTableCell(String giaStr) {
        String cleaned = giaStr.replace("VNĐ", "").replace(" ", "").trim();
        cleaned = cleaned.replace(".", "").replace(",", ".");
        cleaned = cleaned.replaceAll("[^0-9.]", "");
        if (!cleaned.matches("\\d*\\.?\\d+")) return BigDecimal.ZERO;
        try {
            return new BigDecimal(cleaned);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private void capNhatTongTien() {
        tongTienCanThanhToan = BigDecimal.ZERO;
        for (int i = 0; i < modelDSCho.getRowCount(); i++) {
            String giaStr = modelDSCho.getValueAt(i, 6).toString();
            BigDecimal gia = parseGiaFromTableCell(giaStr);
            if (gia.compareTo(BigDecimal.ZERO) > 0) {
                tongTienCanThanhToan = tongTienCanThanhToan.add(gia);
            }
        }
        String formatted = String.format("%,.2f", tongTienCanThanhToan);
        lblTongTien.setText("TỔNG CỘNG: " + formatted + " VNĐ");
    }

    private void xuLyChonNguoiThanhToan() {
        int dong = tblCho.getSelectedRow();
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
        for (int i = 0; i < modelDSCho.getRowCount(); i++) {
            modelDSCho.setValueAt("Khách", i, 8);
        }
        modelDSCho.setValueAt("Người TT", dong, 8);

        JOptionPane.showMessageDialog(this, "Đã chỉ định " + hk.getHoTen() + " là người thanh toán.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        if (kiemTraTatCaDaNhap()) {
            btnTiepTuc.setEnabled(true);
        }
    }

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
                BigDecimal giaTT = parseGiaFromTableCell(modelDSCho.getValueAt(i, 6).toString());
                if (giaTT.compareTo(BigDecimal.ZERO) <= 0) {
                    BigDecimal giaGoc = layGiaVeCoBan(cho);
                    giaTT = dieuKhienVe.tinhGiaVeCuoiCung(giaGoc, hk.getMaUuDai(), null);
                }

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

    private void quayLaiManHinhChonCho() {
        SwingUtilities.invokeLater(() -> {
            try {
                new GiaoDienChonCho(chuyenTauDuocChon, nhanVienLap).setVisible(true);
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi quay lại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void xuLyChonDongTrongBang() {
        tblCho.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = tblCho.getSelectedRow();
            if (row == -1) return;

            String tinhTrang = modelDSCho.getValueAt(row, 7).toString();

            if ("ĐÃ NHẬP".equals(tinhTrang)) {
                btnSua.setEnabled(true);
                btnChonNTT.setEnabled(true);

                HanhKhach hk = danhSachHanhKhachDaNhap.get(row);
                if (hk == null) return;

                txtHoTen.setText(hk.getHoTen());
                chonNgaySinh.setDate(java.util.Date.from(hk.getNgaySinh().atStartOfDay(ZoneId.systemDefault()).toInstant()));
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

                if (!dangSua) {
                    batDauCheDoSua();
                }
            } else {
                btnSua.setEnabled(false);
                boolean coDaNhap = false;
                for (int i = 0; i < modelDSCho.getRowCount(); i++) {
                    if ("ĐÃ NHẬP".equals(modelDSCho.getValueAt(i, 7))) {
                        coDaNhap = true;
                        break;
                    }
                }
                btnChonNTT.setEnabled(coDaNhap);

                if (dangSua) {
                    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có muốn hủy sửa hành khách này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        ketThucCheDoSua();
                    }
                }
            }
        });
    }

    private JButton taoButton(String text, Color bg, String iconPath) {
        JButton btn = new JButton(text, chinhKichThuoc(iconPath, 24, 24));
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
        URL iconUrl = GiaoDienNhapThongTinHK.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}