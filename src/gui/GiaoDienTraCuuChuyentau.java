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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import control.TraCuuChuyenTauControl;
import dao.ToaTauDAO;
import dao.VeDAO;
import entity.ChuyenTau;
import entity.NhanVien;

public class GiaoDienTraCuuChuyentau extends JFrame implements ActionListener {

    private TraCuuChuyenTauControl control;
    private JComboBox<String> cbGaDi, cbGaDen;
    private JDateChooser dateChooser;
    private JPanel pnlTrungTam, pnlNutChucNang;
    private JButton btnTim, btnLamMoi, btnTroVe;
    private NhanVien nhanVienHienTai;
    private VeDAO veDAO = new VeDAO();
    private ToaTauDAO toaTaudao = new ToaTauDAO();

    public GiaoDienTraCuuChuyentau(NhanVien nhanVien) {
        this.nhanVienHienTai = nhanVien != null ? nhanVien : new NhanVien("NV-001");
        this.control = new TraCuuChuyenTauControl(this);

        setTitle("Tra cứu chuyến tàu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // Title
        
        JLabel lblTitle = new JLabel("TRA CỨU CHUYẾN TÀU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 44));
        lblTitle.setForeground(new Color(74, 140, 103));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(225, 242, 232));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // Left panel - form nhập liệu
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(540, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        Font fontTieuDe = new Font("Segoe UI", Font.BOLD, 24);
        TitledBorder titleBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK,2),
                "Thông tin tra cứu",
                TitledBorder.CENTER, TitledBorder.TOP,
                fontTieuDe, Color.BLACK);
        pnlLeft.setBorder(titleBorder);

        // Form nhập liệu
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setBackground(Color.WHITE);

        Font lblFont = new Font("Segoe UI", Font.BOLD, 19);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 20);

        JLabel lblGaDi = new JLabel("Ga đi:");
        JLabel lblGaDen = new JLabel("Ga đến:");
        JLabel lblNgayDi = new JLabel("Ngày đi:");

        JLabel[] labels = {lblGaDi, lblGaDen, lblNgayDi};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        cbGaDi = new JComboBox<>();
        cbGaDen = new JComboBox<>();
        dateChooser = new JDateChooser();
        dateChooser.setFont(txtFont);
        dateChooser.setPreferredSize(new Dimension(200, 40));
        dateChooser.setMinSelectableDate(new Date());
        cbGaDen.setPreferredSize(new Dimension(200,40));
        cbGaDi.setPreferredSize(new Dimension(200,40));
        JComboBox<?>[] combos = {cbGaDi, cbGaDen};
        for (JComboBox<?> cb : combos) {
            cb.setFont(txtFont);
            cb.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ga đi
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.EAST;
        pnlForm.add(lblGaDi, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        pnlForm.add(cbGaDi, gbc);

        // Ga đến
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.EAST;
        pnlForm.add(lblGaDen, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        pnlForm.add(cbGaDen, gbc);

        // Ngày đi
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.EAST;
        pnlForm.add(lblNgayDi, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        pnlForm.add(dateChooser, gbc);

        // Buttons tìm và làm mới
        btnTim = taoButton("Tìm", new Color(46, 204, 113), "/img/magnifying-glass.png");
        btnLamMoi = taoButton("Làm mới", new Color(52, 152, 219), "/img/undo2.png");

        JPanel pnlButtons = new JPanel(new GridLayout(1, 2, 10, 10));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.add(btnLamMoi);
        pnlButtons.add(btnTim);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        pnlForm.add(pnlButtons, gbc);

        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlMain.add(pnlLeft, BorderLayout.WEST);

        // Right panel - kết quả
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(Color.WHITE);
        
        pnlTrungTam = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        TitledBorder titleBorderKQ = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Kết quả tra cứu",
                TitledBorder.CENTER, TitledBorder.TOP,
                fontTieuDe, Color.BLACK);

        JScrollPane scroll = new JScrollPane(pnlTrungTam);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scroll.setBackground(Color.WHITE);

        JPanel pnlTableBorder = new JPanel(new BorderLayout());
        pnlTrungTam.setBorder(titleBorderKQ);
        pnlTableBorder.add(scroll, BorderLayout.CENTER);
        pnlRight.add(pnlTableBorder, BorderLayout.CENTER);

        pnlMain.add(pnlRight, BorderLayout.CENTER);

        // Footer
        pnlNutChucNang = new JPanel(new BorderLayout());
        pnlNutChucNang.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "");
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnTroVe.setPreferredSize(new Dimension(160, 60));
        pnlNutChucNang.add(btnTroVe, BorderLayout.WEST);

        pnlMain.add(pnlNutChucNang, BorderLayout.SOUTH);

        // Thêm vào frame
        add(pnlMain);

        // Gán sự kiện
        btnTim.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnTroVe.addActionListener(this);

        // Tải dữ liệu ga
        loadGaDataToComboBox();
    }

    // Hàm tạo button giống QuanLyHanhKhach
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

    // Hàm resize icon
    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
        URL iconUrl = GiaoDienTraCuuChuyentau.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        java.awt.Image img = icon.getImage().getScaledInstance(rong, cao, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // Tải ga vào combo box
    private void loadGaDataToComboBox() {
        List<String> tenGaList = control.loadGaData();
        cbGaDi.removeAllItems();
        cbGaDen.removeAllItems();
        for (String ten : tenGaList) {
            cbGaDi.addItem(ten);
            cbGaDen.addItem(ten);
        }
        if (!tenGaList.isEmpty()) {
            cbGaDi.setSelectedIndex(0);
            if (tenGaList.size() > 1) cbGaDen.setSelectedIndex(1);
        }
    }

    // Tạo panel cho mỗi chuyến tàu
    private JPanel taoVePanel(ChuyenTau ct) throws SQLException {
        JPanel pnlVe = new JPanel(new BorderLayout());
        pnlVe.setPreferredSize(new Dimension(320, 220));
        pnlVe.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel pnlNoiDung = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);

        Map<String, Object> thongKe = control.getThongTinChuyenTau(ct);
        String tenTau = (String) thongKe.get("tenTau");
        int slChoDaDat = (int) thongKe.get("slChoDaDat");
        int slChoTrong = (int) thongKe.get("slChoTrong");

        JLabel lblTenChuyen = new JLabel(tenTau == null ? ct.getMaChuyenTau() : tenTau);
        lblTenChuyen.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTenChuyen.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlNoiDung.add(lblTenChuyen, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;

        JLabel lblTGDi = new JLabel("Giờ đi:");
        lblTGDi.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        pnlNoiDung.add(lblTGDi, gbc);
        JLabel lblGioDi = new JLabel(ct.getThoiGianKhoiHanh().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM HH:mm")));
        lblGioDi.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblGioDi.setForeground(new Color(229, 115, 115));
        gbc.gridx = 1; gbc.gridy = 1;
        pnlNoiDung.add(lblGioDi, gbc);

        JLabel lblTGDen = new JLabel("Giờ đến:");
        lblTGDen.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 2;
        pnlNoiDung.add(lblTGDen, gbc);
        JLabel lblGioDen = new JLabel(ct.getThoiGianDen().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM HH:mm")));
        lblGioDen.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblGioDen.setForeground(new Color(229, 115, 115));
        gbc.gridx = 1; gbc.gridy = 2;
        pnlNoiDung.add(lblGioDen, gbc);

        JLabel lblDaDat = new JLabel("Số chỗ đã đặt:");
        lblDaDat.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 3;
        pnlNoiDung.add(lblDaDat, gbc);
        JLabel lblSoDaDat = new JLabel(String.valueOf(slChoDaDat));
        lblSoDaDat.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSoDaDat.setForeground(new Color(229, 115, 115));
        gbc.gridx = 1; gbc.gridy = 3;
        pnlNoiDung.add(lblSoDaDat, gbc);

        JLabel lblTrong = new JLabel("Số chỗ trống:");
        lblTrong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 4;
        pnlNoiDung.add(lblTrong, gbc);
        JLabel lblSoTrong = new JLabel(String.valueOf(slChoTrong));
        lblSoTrong.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSoTrong.setForeground(new Color(74, 140, 103));
        gbc.gridx = 1; gbc.gridy = 4;
        pnlNoiDung.add(lblSoTrong, gbc);

        JButton btnDatVe = new JButton("Đặt vé");
        btnDatVe.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDatVe.setBackground(new Color(74, 140, 103));
        btnDatVe.setForeground(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 5; gbc.anchor = GridBagConstraints.EAST;
        pnlNoiDung.add(btnDatVe, gbc);

        pnlVe.add(pnlNoiDung, BorderLayout.CENTER);

        btnDatVe.addActionListener(e -> {
            if (ct.getMaChuyenTau() == null || ct.getMaChuyenTau().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin chuyến tàu.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                try {
                    new GiaoDienChonCho(ct, nhanVienHienTai).setVisible(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi khi mở màn hình chọn chỗ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
        });

        return pnlVe;
    }

    // Cập nhật kết quả
    private void capNhatKetQua(List<ChuyenTau> danhSach) throws SQLException {
        pnlTrungTam.removeAll();
        if (danhSach == null || danhSach.isEmpty()) {
            JPanel thongBao = new JPanel(new BorderLayout());
            thongBao.setPreferredSize(new Dimension(600, 120));
            JLabel lbl = new JLabel("Không tìm thấy chuyến tàu nào phù hợp.", SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
            thongBao.add(lbl, BorderLayout.CENTER);
            pnlTrungTam.add(thongBao);
        } else {
            for (ChuyenTau ct : danhSach) {
                pnlTrungTam.add(taoVePanel(ct));
            }
        }
        pnlTrungTam.revalidate();
        pnlTrungTam.repaint();
    }

    // Xử lý tìm kiếm
    private void xuLyTimChuyenTau() {
        String tenGaDi = (String) cbGaDi.getSelectedItem();
        String tenGaDen = (String) cbGaDen.getSelectedItem();
        Date date = dateChooser.getDate();

        if (tenGaDi == null || tenGaDen == null || date == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ga đi, ga đến và ngày đi.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<ChuyenTau> ketQua = control.timChuyenTau(tenGaDi, tenGaDen, date);
            capNhatKetQua(ketQua);
            if (ketQua == null || ketQua.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến tàu nào phù hợp.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnTim) {
            xuLyTimChuyenTau();
        } else if (src == btnLamMoi) {
            cbGaDi.setSelectedIndex(cbGaDi.getItemCount() > 0 ? 0 : -1);
            cbGaDen.setSelectedIndex(cbGaDen.getItemCount() > 1 ? 1 : (cbGaDen.getItemCount() > 0 ? 0 : -1));
            dateChooser.setDate(null);
            pnlTrungTam.removeAll();
            pnlTrungTam.revalidate();
            pnlTrungTam.repaint();
        } else if (src == btnTroVe) {
            this.dispose();
            try {
                new MHC_NhanVienBanVe(nhanVienHienTai).setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        LookAndFeelManager.setNimbusLookAndFeel();
        NhanVien nv = new NhanVien("NV-001");
        SwingUtilities.invokeLater(() -> new GiaoDienTraCuuChuyentau(nv).setVisible(true));
    }
}