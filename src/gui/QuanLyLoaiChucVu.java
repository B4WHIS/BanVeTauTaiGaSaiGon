package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import control.QuanLyLoaiChucVuControl;

public class QuanLyLoaiChucVu extends JFrame implements ActionListener {
    private JTable tblLoaiCV;
    private DefaultTableModel modelCV;
    private JTextField txtMaLoaiCV, txtTenLoaiCV;
    private JButton btnThem, btnSua, btnXoa, btnReset, btnTroVe;
    private QuanLyLoaiChucVuControl control = new QuanLyLoaiChucVuControl();

    public QuanLyLoaiChucVu() {
        setTitle("Quản lý danh mục chức vụ");
        setSize(1500, 1000);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== GẮN THANH MENU BAR TRÊN CÙNG =====
        setJMenuBar(taoMenuBar());

        // ===== MAIN PANEL =====
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC CHỨC VỤ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103,192,144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // ===== LEFT PANEL (FORM) =====
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(450, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        JLabel lblLeftTitle = new JLabel("THÔNG TIN LOẠI CHỨC VỤ", SwingConstants.CENTER);
        lblLeftTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLeftTitle.setForeground(new Color(103,192,144));
        lblLeftTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setBackground(Color.WHITE);

        Font lblFont = new Font("Segoe UI", Font.BOLD, 19);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 20);

        JLabel lblMa = new JLabel("Mã Loại CV:");
        JLabel lblTen = new JLabel("Tên Loại CV:");
        JLabel[] labels = {lblMa, lblTen};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        txtMaLoaiCV = new JTextField();
        txtMaLoaiCV.setFont(txtFont);
        txtMaLoaiCV.setEditable(false);  // ✅ KHÔNG CHO NHẬP TAY
        txtMaLoaiCV.setBackground(new Color(230, 230, 230));
        
        txtTenLoaiCV = new JTextField();
        
        JTextField[] textFields = {txtMaLoaiCV, txtTenLoaiCV};
        for (JTextField txt : textFields) {
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            pnlForm.add(labels[i], gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            pnlForm.add(textFields[i], gbc);
        }

        // ===== BUTTONS =====
        btnThem = taoButton("Thêm", new Color(46, 204, 113), "/img/plus.png");
        btnSua = taoButton("Sửa", new Color(241, 196, 15), "/img/maintenance.png");
        btnXoa = taoButton("Xóa", new Color(231, 76, 60), "/img/bin.png");
        btnReset = taoButton("Làm mới", new Color(52, 152, 219), "/img/reset.png");

        JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnReset);

        // add listeners
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnReset.addActionListener(this);

        pnlLeft.add(lblLeftTitle, BorderLayout.NORTH);
        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        // ===== RIGHT PANEL (TABLE) =====
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(new Color(245, 247, 250));

        JLabel lblRightTitle = new JLabel("DANH SÁCH LOẠI CHỨC VỤ", SwingConstants.CENTER);
        lblRightTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblRightTitle.setForeground(new Color(103,192,144));
        lblRightTitle.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        pnlRight.add(lblRightTitle, BorderLayout.NORTH);

        String[] colHeader = {"Mã Loại CV", "Tên Loại CV"};
        modelCV = new DefaultTableModel(colHeader, 0);
        tblLoaiCV = new JTable(modelCV);

        //LOAD DATA VÀ HIỂN THỊ MÃ TIẾP THEO
        control.loadData(modelCV);
        hienThiMaTiepTheo();  

        tblLoaiCV.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tblLoaiCV.getSelectedRow();
                if (r >= 0) {
                    int modelRow = tblLoaiCV.convertRowIndexToModel(r);
                    Object maObj = modelCV.getValueAt(modelRow, 0);
                    Object tenObj = modelCV.getValueAt(modelRow, 1);
                    txtMaLoaiCV.setText(String.valueOf(maObj));
                    txtTenLoaiCV.setText(String.valueOf(tenObj));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tblLoaiCV);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnlRight.add(scroll, BorderLayout.CENTER);

        // ===== FOOTER =====
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(new Color(103,192,144));
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "/img/loginicon.png");
        btnTroVe.setPreferredSize(new Dimension(130, 45));
        btnTroVe.addActionListener(e -> dispose());
        pnlFooter.add(btnTroVe, BorderLayout.WEST);

        // ===== GẮN TẤT CẢ VÀO MAIN =====
        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(pnlRight, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);
        add(pnlMain);
    }

    // METHOD TỰ ĐỘNG HIỂN THỊ MÃ TIẾP THEO
    private void hienThiMaTiepTheo() {
        int maTiepTheo = control.layMaLoaiCVTiepTheo();
        txtMaLoaiCV.setText(String.valueOf(maTiepTheo));
    }

    private JMenuBar taoMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        menuBar.setBackground(new Color(220, 220, 220));

        JMenu mnuHeThong = new JMenu("Hệ Thống");
        JMenu mnuNghiepVuVe = new JMenu("Nghiệp Vụ Vé");
        JMenu mnuQly = new JMenu("Quản Lý");
        JMenu mnuTraCuu = new JMenu("Tra Cứu");
        JMenu mnuThongKe = new JMenu("Thống Kê");
        JMenu mnuTroGiup = new JMenu("Trợ Giúp");

        mnuHeThong.setIcon(chinhKichThuoc("/img/heThong3.png", 30, 30));
        mnuNghiepVuVe.setIcon(chinhKichThuoc("/img/ve2.png", 30, 30));
        mnuQly.setIcon(chinhKichThuoc("/img/quanLy.png", 30, 30));
        mnuTraCuu.setIcon(chinhKichThuoc("/img/traCuu.png", 30, 30));
        mnuThongKe.setIcon(chinhKichThuoc("/img/thongKe.png", 30, 30));
        mnuTroGiup.setIcon(chinhKichThuoc("/img/troGiup.png", 30, 30));

        mnuHeThong.add(new JMenuItem("Đăng xuất"));
        mnuHeThong.add(new JMenuItem("Thoát"));
        mnuQly.add(new JMenuItem("Quản lý nhân viên"));
        mnuQly.add(new JMenuItem("Quản lý hành khách"));
        mnuQly.add(new JMenuItem("Quản lý chuyến tàu"));
        mnuQly.add(new JMenuItem("Quản lý khuyến mãi"));

        menuBar.add(mnuHeThong);
        menuBar.add(mnuNghiepVuVe);
        menuBar.add(mnuQly);
        menuBar.add(mnuTraCuu);
        menuBar.add(mnuThongKe);
        menuBar.add(mnuTroGiup);
        menuBar.add(Box.createHorizontalGlue());

        JLabel lblXinChao = new JLabel("Xin Chào, [Tên Nhân Viên] ");
        lblXinChao.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel lblNVBV = new JLabel(chinhKichThuoc("/img/nhanVienBanVe.png", 50, 50));
        menuBar.add(lblXinChao);
        menuBar.add(lblNVBV);

        return menuBar;
    }

    private JButton taoButton(String text, Color bg, String iconPath) {
        JButton btn = new JButton(text, chinhKichThuoc(iconPath, 24, 24));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
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
        URL iconUrl = QuanLyLoaiChucVu.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnThem) {
            //  CHỈ KIỂM TRA TÊN (MÃ TỰ ĐỘNG)
            String ten = txtTenLoaiCV.getText().trim();
            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên loại chức vụ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            
            if (control.themLoaiChucVu(ten)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                resetForm();
            }
   

        } else if (src == btnSua) {
            int r = tblLoaiCV.getSelectedRow();
            if (r < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bản ghi để sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String ten = txtTenLoaiCV.getText().trim();
            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên loại chức vụ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int ma = Integer.parseInt(txtMaLoaiCV.getText().trim());
                if (control.suaLoaiChucVu(ma, ten)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    resetForm();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            }

        } else if (src == btnXoa) {
            int r = tblLoaiCV.getSelectedRow();
            if (r < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bản ghi để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa bản ghi này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    int ma = Integer.parseInt(txtMaLoaiCV.getText().trim());
                    if (control.xoaLoaiChucVu(ma)) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        resetForm();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi dữ liệu!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else if (src == btnReset) {
            resetForm();
        }
    }
    
   
    private void resetForm() {
        txtMaLoaiCV.setText("");
        txtTenLoaiCV.setText("");
        tblLoaiCV.clearSelection();
        control.loadData(modelCV);
        hienThiMaTiepTheo();  
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuanLyLoaiChucVu().setVisible(true));
    }
}