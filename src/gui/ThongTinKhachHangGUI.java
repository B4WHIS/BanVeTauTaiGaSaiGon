package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import control.QuanLyVeControl;
import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.NhanVien;
import entity.Ve;

public class ThongTinKhachHangGUI extends JFrame implements ActionListener {

    // ======= CÁC THUỘC TÍNH QUAN TRỌNG =======
    private ChuyenTau chuyenTauDuocChon;
    private List<ChoNgoi> danhSachChoNgoi;
    private NhanVien nvLap;
    private List<Ve> danhSachVeDaDat = new ArrayList<>();
    private QuanLyVeControl veControl = new QuanLyVeControl();

    // ======= THÀNH PHẦN GIAO DIỆN =======
    private JTextField txtHoTen, txtNgaySinh, txtSDT, txtCMND;
    private JComboBox<String> cboLoaiHK;
    private JButton btnLamMoi, btnXacNhan, btnQuayLai;
    private JTable table;
    private DefaultTableModel model;

    // ======= CONSTRUCTOR CHÍNH =======
    public ThongTinKhachHangGUI(ChuyenTau ct, List<ChoNgoi> seats, NhanVien nv) {
        this.chuyenTauDuocChon = ct;
        this.danhSachChoNgoi = seats;
        this.nvLap = nv;

        setTitle("Thông Tin Khách Hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ========== HEADER ==========
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(100, 90));
        header.setBackground(new Color(103, 192, 144));
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTieuDe = new JLabel("THÔNG TIN KHÁCH HÀNG", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTieuDe.setForeground(Color.WHITE);
        ImageIcon icon = new ImageIcon("src/img/boss.png");
        Image img = icon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
        lblTieuDe.setIcon(new ImageIcon(img));
        lblTieuDe.setHorizontalTextPosition(SwingConstants.RIGHT);
        lblTieuDe.setIconTextGap(10);
        header.add(lblTieuDe, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // ========== CENTER ==========
        JPanel pnlCenter = new JPanel(new BorderLayout(20, 10));
        pnlCenter.setBackground(new Color(245, 247, 250));
        pnlCenter.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(pnlCenter, BorderLayout.CENTER);

        // === FORM BÊN TRÁI ===
        JPanel pnlTrai = new JPanel(new BorderLayout(10, 10));
        pnlTrai.setPreferredSize(new Dimension(420, 0));
        pnlTrai.setBackground(Color.WHITE);
        pnlTrai.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 220, 210), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblFormTitle = new JLabel("Nhập thông tin khách hàng", SwingConstants.CENTER);
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblFormTitle.setForeground(new Color(103, 192, 144));
        pnlTrai.add(lblFormTitle, BorderLayout.NORTH);

        JPanel pnlNhap = new JPanel(new GridBagLayout());
        pnlNhap.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font lblFont = new Font("Segoe UI", Font.BOLD, 14);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 14);

        String[] lblTexts = {"Họ tên:", "Ngày sinh:", "Số điện thoại:", "CMND/CCCD:", "Loại KH:"};
        JLabel[] labels = new JLabel[lblTexts.length];
        JTextField[] textFields = new JTextField[4];

        for (int i = 0; i < lblTexts.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            labels[i] = new JLabel(lblTexts[i]);
            labels[i].setFont(lblFont);
            pnlNhap.add(labels[i], gbc);

            gbc.gridx = 1;
            if (i < 4) {
                textFields[i] = new JTextField(15);
                textFields[i].setFont(txtFont);
                textFields[i].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 180)),
                        new EmptyBorder(5, 8, 5, 8)
                ));
                pnlNhap.add(textFields[i], gbc);
            } else {
                cboLoaiHK = new JComboBox<>(new String[]{"Người lớn", "Trẻ em", "Sinh viên", "Người cao tuổi"});
                cboLoaiHK.setFont(txtFont);
                pnlNhap.add(cboLoaiHK, gbc);
            }
        }

        txtHoTen = textFields[0];
        txtNgaySinh = textFields[1];
        txtSDT = textFields[2];
        txtCMND = textFields[3];

        // === BUTTONS ===
        gbc.gridx = 0;
        gbc.gridy = lblTexts.length;
        gbc.gridwidth = 2;
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        pnlButton.setBackground(Color.WHITE);

        btnLamMoi = taoButton("Làm mới", new Color(93, 156, 236), "src/img/undo.png");
        btnXacNhan = taoButton("Xác nhận", new Color(103, 192, 144), "src/img/mark.png");

        pnlButton.add(btnLamMoi);
        pnlButton.add(btnXacNhan);
        pnlNhap.add(pnlButton, gbc);

        pnlTrai.add(pnlNhap, BorderLayout.CENTER);
        pnlCenter.add(pnlTrai, BorderLayout.WEST);

        // === BẢNG BÊN PHẢI ===
        JPanel pnlPhai = new JPanel(new BorderLayout(10, 10));
        pnlPhai.setBackground(Color.WHITE);
        pnlPhai.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 220, 210), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTableTitle = new JLabel("Danh sách khách hàng", SwingConstants.CENTER);
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTableTitle.setForeground(new Color(103, 192, 144));
        pnlPhai.add(lblTableTitle, BorderLayout.NORTH);

        String[] columns = {"STT", "Họ tên", "Ngày sinh", "SĐT", "CMND/CCCD", "Loại KH"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        pnlPhai.add(scrollPane, BorderLayout.CENTER);

        pnlCenter.add(pnlPhai, BorderLayout.CENTER);

        // ========== FOOTER ==========
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(103, 192, 144));
        footer.setBorder(new EmptyBorder(10, 20, 10, 20));
        footer.setPreferredSize(new Dimension(0, 60));

        btnQuayLai = taoButton("Quay lại", new Color(41, 128, 185), "src/img/undo.png");
        btnQuayLai.setPreferredSize(new Dimension(120, 40));
        footer.add(btnQuayLai, BorderLayout.WEST);

        add(footer, BorderLayout.SOUTH);

        // ===== SỰ KIỆN =====
        btnLamMoi.addActionListener(this);
        btnXacNhan.addActionListener(this);
        btnQuayLai.addActionListener(this);
    }

    // === Constructor mặc định để chạy thử GUI ===
    public ThongTinKhachHangGUI() {
        this(null, new ArrayList<>(), null);
    }

    // === Hàm tạo nút có style ===
    private JButton taoButton(String text, Color bg, String iconPath) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 15, 10, 15));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btn.setIcon(new ImageIcon(img));
        btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btn.setIconTextGap(8);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    // === Xử lý sự kiện ===
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnLamMoi) {
            txtHoTen.setText("");
            txtNgaySinh.setText("");
            txtSDT.setText("");
            txtCMND.setText("");
            cboLoaiHK.setSelectedIndex(0);
            txtHoTen.requestFocus();
        }
        else if (src == btnXacNhan) {
            String hoTen = txtHoTen.getText().trim();
            String ngaySinh = txtNgaySinh.getText().trim();
            String sdt = txtSDT.getText().trim();
            String cmnd = txtCMND.getText().trim();
            String loai = (String) cboLoaiHK.getSelectedItem();

            if (hoTen.isEmpty() || sdt.isEmpty() || cmnd.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Thêm vào bảng
            model.addRow(new Object[]{model.getRowCount() + 1, hoTen, ngaySinh, sdt, cmnd, loai});

            // Giả lập tạo vé (sau này có thể kết nối QuanLyVeControl)
            Ve ve = new Ve(); 
            danhSachVeDaDat.add(ve);

            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            btnLamMoi.doClick();
        }
        else if (src == btnQuayLai) {
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ThongTinKhachHangGUI().setVisible(true));
    }
}
