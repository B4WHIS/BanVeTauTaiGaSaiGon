package gui;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import control.QuanLyKhuyenMaiControl;
import entity.KhuyenMai;
import com.toedter.calendar.JDateChooser;

public class QuanLykhuyenMai extends JFrame {

    public JTable tblKhuyenMai;
    private DefaultTableModel modelKM;
    public JTextField txtMaKM, txtTenKM, txtMucGiamGia, txtDieuKien;
    public JDateChooser dateBD, dateKT;
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTroVe, btnTim;
    private QuanLyKhuyenMaiControl controller;

    private boolean dangNhap = false;
    private boolean dangSua = false;

    public QuanLykhuyenMai() {
        controller = new QuanLyKhuyenMaiControl(this);
        setTitle("Quản lý khuyến mãi");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("QUẢN LÝ KHUYẾN MÃI", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 44));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103, 192, 144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // ===== LEFT PANEL =====
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(540, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        TitledBorder titleBorder = BorderFactory.createTitledBorder("Nhập Thông Tin Tra Cứu");
        titleBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 24));
        pnlLeft.setBorder(titleBorder);

        // ===== FORM =====
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setBackground(Color.WHITE);

        Font lblFont = new Font("Segoe UI", Font.BOLD, 19);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 20);

        JLabel lblMaKM = new JLabel("Mã KM:");
        JLabel lblTenKM = new JLabel("Tên khuyến mãi:");
        JLabel lblNgayBD = new JLabel("Ngày bắt đầu:");
        JLabel lblNgayKT = new JLabel("Ngày kết thúc:");
        JLabel lblMucGiam = new JLabel("Mức giảm (%):");
        JLabel lblDieuKien = new JLabel("Điều kiện:");

        JLabel[] labels = {lblMaKM, lblTenKM, lblNgayBD, lblNgayKT, lblMucGiam, lblDieuKien};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        // === Ô MÃ: BẬT + STYLE ===
        txtMaKM = new JTextField();
        txtMaKM.setEnabled(false); // MẶC ĐỊNH KHÓA
        txtMaKM.setFont(txtFont);
        txtMaKM.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtMaKM.setPreferredSize(new Dimension(220, 32));
        txtMaKM.setToolTipText("Nhập mã: KM001, SALE2025, NOEL24...");

        txtTenKM = new JTextField();
        dateBD = new JDateChooser();
        dateKT = new JDateChooser();
        txtMucGiamGia = new JTextField();
        txtDieuKien = new JTextField();

        JTextField[] textFields = {txtTenKM, txtMucGiamGia, txtDieuKien};
        for (JTextField txt : textFields) {
            txt.setFont(txtFont);
            txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            txt.setPreferredSize(new Dimension(220, 32));
        }
        dateBD.setFont(txtFont); dateKT.setFont(txtFont);
        dateBD.setPreferredSize(new Dimension(220, 32)); dateKT.setPreferredSize(new Dimension(220, 32));
        dateBD.setDateFormatString("dd/MM/yyyy"); dateKT.setDateFormatString("dd/MM/yyyy");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        for (JLabel lbl : labels) {
            gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3; gbc.anchor = GridBagConstraints.EAST;
            pnlForm.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 0.7; gbc.anchor = GridBagConstraints.WEST;
            if (row == 0) pnlForm.add(txtMaKM, gbc);
            else if (row == 1) pnlForm.add(txtTenKM, gbc);
            else if (row == 2) pnlForm.add(dateBD, gbc);
            else if (row == 3) pnlForm.add(dateKT, gbc);
            else if (row == 4) pnlForm.add(txtMucGiamGia, gbc);
            else if (row == 5) pnlForm.add(txtDieuKien, gbc);
            row++;
        }

        // ===== NÚT TÌM KIẾM =====
        btnTim = taoButton("Tìm kiếm", new Color(155, 89, 182), "/img/magnifying-glass.png");
        JPanel pnlTim = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTim.setBackground(Color.WHITE);
        pnlTim.add(btnTim);
        pnlForm.add(pnlTim, new GridBagConstraints(0, row++, 2, 1, 1, 0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(15, 0, 0, 0), 0, 0));

        // ===== BUTTONS =====
        btnThem = taoButton("Thêm", new Color(46, 204, 113), "/img/plus2.png");
        btnSua = taoButton("Sửa", new Color(187, 102, 83), "/img/repair.png");
        btnXoa = taoButton("Xóa", new Color(231, 76, 60), "/img/trash-bin.png");
        btnReset = taoButton("Làm mới", new Color(52, 152, 219), "/img/undo2.png");
        btnExport = taoButton("Xuất Excel", new Color(241, 196, 15), "/img/export2.png");

        JPanel pnlButtons = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.add(btnThem); pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa); pnlButtons.add(btnReset);
        pnlButtons.add(btnExport); pnlButtons.add(btnTim);

        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        // ===== RIGHT PANEL =====
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(Color.WHITE);

        String[] colHeader = {"Mã KM", "Tên KM", "Giảm (%)", "Ngày BD", "Ngày KT", "Điều kiện"};
        modelKM = new DefaultTableModel(colHeader, 0);
        tblKhuyenMai = new JTable(modelKM);
        tblKhuyenMai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblKhuyenMai.setRowHeight(28);
        tblKhuyenMai.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblKhuyenMai.setSelectionBackground(new Color(58, 111, 67));
        tblKhuyenMai.setSelectionForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tblKhuyenMai);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        TitledBorder tblBorder = BorderFactory.createTitledBorder("Danh Sách Khuyến Mãi");
        tblBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 24));
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBorder(tblBorder);
        pnlTable.add(scroll, BorderLayout.CENTER);
        pnlRight.add(pnlTable, BorderLayout.CENTER);

        // ===== FOOTER =====
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "");
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnTroVe.setPreferredSize(new Dimension(160, 60));
        pnlFooter.add(btnTroVe, BorderLayout.WEST);

        // ===== ADD ALL =====
        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(pnlRight, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);
        add(pnlMain);

        // Load data
        controller.loadDataToTable();

        // Attach listeners
        attachListeners();

        enableFormFields(false);
        txtMaKM.setEnabled(false); // ĐẢM BẢO KHÓA BAN ĐẦU
    }

    private void attachListeners() {
        // === THÊM ===
        btnThem.addActionListener(e -> {
            if (!dangNhap && !dangSua) {
                controller.them();
                dangNhap = true;
                txtMaKM.setEnabled(true); // BẬT Ô MÃ
                txtMaKM.requestFocus();   // FOCUS VÀO MÃ
                btnThem.setText("Xác nhận thêm");
                btnThem.setIcon(chinhKichThuoc("/img/check.png", 24, 24));
            } else if (dangNhap) {
                try {
                    controller.xuLyThem();
                    dangNhap = false;
                    txtMaKM.setEnabled(false); // KHÓA SAU THÊM
                    btnThem.setText("Thêm");
                    btnThem.setIcon(chinhKichThuoc("/img/plus2.png", 24, 24));
                } catch (Exception ex) {
                    showMessage("Lỗi thêm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // === SỬA ===
        btnSua.addActionListener(e -> {
            if (!dangSua && !dangNhap) {
                controller.sua();
                dangSua = true;
                txtMaKM.setEnabled(false); // KHÓA MÃ KHI SỬA
                btnSua.setText("Xác nhận sửa");
                btnSua.setIcon(chinhKichThuoc("/img/check.png", 24, 24));
            } else if (dangSua) {
                try {
                    controller.xuLySua();
                    dangSua = false;
                    txtMaKM.setEnabled(false);
                    btnSua.setText("Sửa");
                    btnSua.setIcon(chinhKichThuoc("/img/repair.png", 24, 24));
                } catch (Exception ex) {
                    showMessage("Lỗi sửa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // === XÓA ===
        btnXoa.addActionListener(e -> {
            try {
                controller.xoa();
            } catch (Exception ex) {
                showMessage("Lỗi xóa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // === LÀM MỚI ===
        btnReset.addActionListener(e -> {
            dangNhap = false;
            dangSua = false;
            btnThem.setText("Thêm");
            btnThem.setIcon(chinhKichThuoc("/img/plus2.png", 24, 24));
            btnSua.setText("Sửa");
            btnSua.setIcon(chinhKichThuoc("/img/repair.png", 24, 24));
            controller.loadDataToTable();
            resetForm();
            txtMaKM.setEnabled(false); // KHÓA SAU RESET
        });

        // === TÌM KIẾM ===
        btnTim.addActionListener(e -> timKiemTuForm());

        // === XUẤT EXCEL ===
        btnExport.addActionListener(e -> showMessage("Chức năng Xuất Excel đang phát triển!", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        // === TRỞ VỀ ===
        btnTroVe.addActionListener(e -> new MHC_NhanVienQuanLy().setVisible(true));

        // === CLICK TABLE ===
        tblKhuyenMai.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    controller.handleTableSelection();
                    txtMaKM.setEnabled(false); // KHÓA KHI CLICK
                } catch (Exception ex) {
                    ex.printStackTrace();
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
        URL iconUrl = QuanLykhuyenMai.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // === TÌM KIẾM TỪ CÁC INPUT ===
    private void timKiemTuForm() {
        String ma = txtMaKM.getText().trim();
        String ten = txtTenKM.getText().trim();
        String mucGiamStr = txtMucGiamGia.getText().trim();
        String dk = txtDieuKien.getText().trim();

        LocalDate bd = dateBD.getDate() != null ?
            dateBD.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null;
        LocalDate kt = dateKT.getDate() != null ?
            dateKT.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null;

        modelKM.setRowCount(0);
        ArrayList<KhuyenMai> ds = controller.dsKhuyenMai(); // Lấy tất cả
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        int count = 0;
        for (KhuyenMai km : ds) {
            boolean match = true;

            if (!ma.isEmpty() && !km.getMaKhuyenMai().toLowerCase().contains(ma.toLowerCase())) match = false;
            if (!ten.isEmpty() && !km.getTenKhuyenMai().toLowerCase().contains(ten.toLowerCase())) match = false;
            if (!dk.isEmpty() && !km.getDieuKien().toLowerCase().contains(dk.toLowerCase())) match = false;
            if (!mucGiamStr.isEmpty()) {
                try {
                    BigDecimal muc = new BigDecimal(mucGiamStr);
                    if (km.getMucGiamGia().compareTo(muc) != 0) match = false;
                } catch (Exception e) { match = false; }
            }
            if (bd != null && km.getNgayBatDau().isBefore(bd)) match = false;
            if (kt != null && km.getNgayKetThuc().isAfter(kt)) match = false;

            if (match) {
                modelKM.addRow(new Object[]{
                    km.getMaKhuyenMai(),
                    km.getTenKhuyenMai(),
                    km.getMucGiamGia() + "%",
                    km.getNgayBatDau().format(df),
                    km.getNgayKetThuc().format(df),
                    km.getDieuKien()
                });
                count++;
            }
        }

        showMessage("Tìm thấy " + count + " kết quả.", "Kết quả tìm kiếm", JOptionPane.INFORMATION_MESSAGE);
    }

    public void loadFormData(KhuyenMai km) {
        txtMaKM.setText(km.getMaKhuyenMai());
        txtMaKM.setEnabled(false); // KHÓA MÃ
        txtTenKM.setText(km.getTenKhuyenMai());
        txtMucGiamGia.setText(km.getMucGiamGia().toString());
        txtDieuKien.setText(km.getDieuKien());
        dateBD.setDate(java.sql.Date.valueOf(km.getNgayBatDau()));
        dateKT.setDate(java.sql.Date.valueOf(km.getNgayKetThuc()));
    }

    public String getSelectedMaKM() {
        int row = tblKhuyenMai.getSelectedRow();
        return row >= 0 ? (String) modelKM.getValueAt(row, 0) : null;
    }

    public DefaultTableModel getModelKM() { return modelKM; }

    public void resetForm() {
        txtMaKM.setText("");
        txtTenKM.setText("");
        txtMucGiamGia.setText("");
        txtDieuKien.setText("");
        dateBD.setDate(null);
        dateKT.setDate(null);
        tblKhuyenMai.clearSelection();
        txtMaKM.setEnabled(false); // KHÓA MÃ
    }

    public void enableFormFields(boolean b) {
        txtTenKM.setEnabled(b);
        txtMucGiamGia.setEnabled(b);
        txtDieuKien.setEnabled(b);
        dateBD.setEnabled(b);
        dateKT.setEnabled(b);
    }

    public void refreshData() {
        controller.loadDataToTable();
        resetForm();
        enableFormFields(false);
        txtMaKM.setEnabled(false);
    }

    public void showMessage(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }

    public static void main(String[] args) {
        try {
           LookAndFeelManager.setNimbusLookAndFeel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new QuanLykhuyenMai().setVisible(true);
    }
}