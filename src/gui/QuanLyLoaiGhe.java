package gui;

import control.QuanLyLoaiGheControl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class QuanLyLoaiGhe extends JFrame implements ActionListener {
    private JTable tblLoaiGhe;
    private DefaultTableModel modelGhe;
    private JTextField txtMaLoaiGhe, txtTenLoaiGhe;
    private JButton btnThem, btnSua, btnXoaMem, btnKhoiPhuc, btnReset, btnExport, btnTim, btnTroVe;
    private QuanLyLoaiGheControl control = new QuanLyLoaiGheControl();
    private String chucNangHienTai = "";

    public QuanLyLoaiGhe() {
        setTitle("Quản lý danh mục loại ghế");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // Title
        JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC LOẠI GHẾ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 44));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103, 192, 144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // Left panel
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(450, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        TitledBorder titleBorder = BorderFactory.createTitledBorder("Nhập Thông Tin Tra Cứu");
        titleBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 24));
        pnlLeft.setBorder(titleBorder);

        // Form
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setBackground(Color.WHITE);

        Font lblFont = new Font("Segoe UI", Font.BOLD, 19);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 20);

        JLabel lblMa = new JLabel("Mã Loại:");
        JLabel lblTen = new JLabel("Tên Loại:");
        JLabel[] labels = {lblMa, lblTen};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        txtMaLoaiGhe = new JTextField();
        txtMaLoaiGhe.setEditable(false);
        txtMaLoaiGhe.setBackground(new Color(230, 230, 230));
        txtTenLoaiGhe = new JTextField();

        JTextField[] textFields = {txtMaLoaiGhe, txtTenLoaiGhe};
        for (JTextField txt : textFields) {
            txt.setFont(txtFont);
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.3;
            pnlForm.add(labels[i], gbc);
            gbc.gridx = 1; gbc.weightx = 0.7;
            pnlForm.add(textFields[i], gbc);
        }

        // Buttons
        btnThem = taoButton("Thêm", new Color(46, 204, 113), "/img/plus2.png");
        btnSua = taoButton("Sửa", new Color(187, 102, 83), "/img/repair.png");
        btnXoaMem = taoButton("Xóa", new Color(231, 76, 60), "/img/trash-bin.png");
        btnKhoiPhuc = taoButton("Khôi phục", new Color(52, 152, 219), "/img/restore.png");
        btnReset = taoButton("Làm mới", new Color(241, 196, 15), "/img/undo2.png");
        btnExport = taoButton("Xuất Excel", new Color(155, 89, 182), "/img/export2.png");
        btnTim = taoButton("Tìm", new Color(241, 196, 15), "/img/magnifying-glass.png");

        JPanel pnlButtons = new JPanel(new GridLayout(4, 2, 10, 10));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoaMem);
        pnlButtons.add(btnKhoiPhuc);
        pnlButtons.add(btnReset);
        pnlButtons.add(btnExport);
        pnlButtons.add(btnTim);

        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        // Right panel - table
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(Color.WHITE);

        String[] colHeader = {"Mã Loại Ghế", "Tên Loại", "Trạng Thái"};
        modelGhe = new DefaultTableModel(colHeader, 0);
        tblLoaiGhe = new JTable(modelGhe);
        tblLoaiGhe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblLoaiGhe.setRowHeight(28);
        tblLoaiGhe.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblLoaiGhe.setSelectionBackground(new Color(58, 111, 67));
        tblLoaiGhe.setSelectionForeground(Color.WHITE);

        tblLoaiGhe.getColumnModel().getColumn(0).setMaxWidth(120);
        tblLoaiGhe.getColumnModel().getColumn(1).setPreferredWidth(300);
        tblLoaiGhe.getColumnModel().getColumn(2).setMaxWidth(150);

        control.loadData(modelGhe);

        JScrollPane scroll = new JScrollPane(tblLoaiGhe);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JPanel pnlTableBorder = new JPanel(new BorderLayout());
        TitledBorder titleTable = BorderFactory.createTitledBorder("Danh Sách Loại Ghế");
        titleTable.setTitleFont(new Font("Segoe UI", Font.BOLD, 24));
        pnlTableBorder.setBorder(titleTable);
        pnlTableBorder.add(scroll, BorderLayout.CENTER);
        pnlRight.add(pnlTableBorder, BorderLayout.CENTER);

        // Footer
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "");
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnTroVe.setPreferredSize(new Dimension(160, 60));
        pnlFooter.add(btnTroVe, BorderLayout.WEST);

        // Add all
        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(pnlRight, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);
        add(pnlMain);

        // Events
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoaMem.addActionListener(this);
        btnKhoiPhuc.addActionListener(this);
        btnReset.addActionListener(this);
        btnExport.addActionListener(this);
        btnTim.addActionListener(this);
        btnTroVe.addActionListener(e -> dispose());

        tblLoaiGhe.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tblLoaiGhe.getSelectedRow();
                if (r >= 0) {
                    int modelRow = tblLoaiGhe.convertRowIndexToModel(r);
                    txtMaLoaiGhe.setText(String.valueOf(modelGhe.getValueAt(modelRow, 0)));
                    txtTenLoaiGhe.setText(String.valueOf(modelGhe.getValueAt(modelRow, 1)));
                }
            }
        });

        setEnableTextFields(false);
    }

    private void setEnableTextFields(boolean enabled) {
        txtTenLoaiGhe.setEnabled(enabled);
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
        URL iconUrl = QuanLyLoaiGhe.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void hienThiMaTiepTheo() {
        txtMaLoaiGhe.setText(String.valueOf(control.layMaLoaiGheTiepTheo()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnThem) {
            if (!"them".equals(chucNangHienTai)) {
                chucNangHienTai = "them";
                JOptionPane.showMessageDialog(this, "Nhập tên loại ghế mới, rồi nhấn THÊM để xác nhận.");
               
                xuLyLamMoi();
                setEnableTextFields(true);
                txtTenLoaiGhe.requestFocus();
                hienThiMaTiepTheo();
            } else {
                xuLyThem();
            }
        } else if (src == btnSua) {
            if (!"sua".equals(chucNangHienTai)) {
                chucNangHienTai = "sua";
                JOptionPane.showMessageDialog(this, "Chọn bản ghi, sửa tên, rồi nhấn SỬA để xác nhận.");
                setEnableTextFields(true);
            } else {
                xuLySua();
            }
        } else if (src == btnXoaMem) {
            if (!"xoa".equals(chucNangHienTai)) {
                chucNangHienTai = "xoa";
                JOptionPane.showMessageDialog(this, "Chọn loại ghế đang HOẠT ĐỘNG, rồi nhấn XÓA để ngừng hoạt động.");
            } else {
                xuLyXoaMem();
            }
        } else if (src == btnKhoiPhuc) {
            if (!"khoiphuc".equals(chucNangHienTai)) {
                chucNangHienTai = "khoiphuc";
                JOptionPane.showMessageDialog(this, "Chọn loại ghế NGỪNG HOẠT ĐỘNG, rồi nhấn KHÔI PHỤC.");
            } else {
                xuLyKhoiPhuc();
            }
        } else if (src == btnTim) {
            if (!"tim".equals(chucNangHienTai)) {
                chucNangHienTai = "tim";
                JOptionPane.showMessageDialog(this, "Nhập tên loại ghế cần tìm, rồi nhấn TÌM.");
                setEnableTextFields(true);
            } else {
                xuLyTim();
            }
        } else if (src == btnReset) {
            xuLyLamMoi();
        } else if (src == btnExport) {
            ExcelExporter.exportToExcel(tblLoaiGhe, this);
        }
    }

    private void xuLyThem() {
        String ten = txtTenLoaiGhe.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại ghế!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (control.themLoaiGhe(ten)) {
            JOptionPane.showMessageDialog(this, "Thêm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            xuLyLamMoi();
        }
    }

    private void xuLySua() {
        int r = tblLoaiGhe.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bản ghi!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String ten = txtTenLoaiGhe.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại ghế!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(txtMaLoaiGhe.getText());
        if (control.suaLoaiGhe(id, ten)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            xuLyLamMoi();
        }
    }

    private void xuLyXoaMem() {
        int r = tblLoaiGhe.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại ghế HOẠT ĐỘNG để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tblLoaiGhe.convertRowIndexToModel(r);
        String trangThai = (String) modelGhe.getValueAt(modelRow, 2);
        if (!"Hoạt động".equals(trangThai)) {
            JOptionPane.showMessageDialog(this, "Chỉ có thể xóa loại ghế đang HOẠT ĐỘNG!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maStr = txtMaLoaiGhe.getText().trim();
        if (maStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã loại ghế không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Ngừng hoạt động loại ghế này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int id = Integer.parseInt(maStr);
            if (control.xoaMemLoaiGhe(id)) {
                JOptionPane.showMessageDialog(this, "Đã ngừng hoạt động loại ghế!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                resetFormHoanToan();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã loại ghế không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void xuLyKhoiPhuc() {
        int r = tblLoaiGhe.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại ghế NGỪNG HOẠT ĐỘNG để khôi phục!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tblLoaiGhe.convertRowIndexToModel(r);
        String trangThai = (String) modelGhe.getValueAt(modelRow, 2);
        if (!"Ngừng hoạt động".equals(trangThai)) {
            JOptionPane.showMessageDialog(this, "Chỉ có thể khôi phục loại ghế NGỪNG HOẠT ĐỘNG!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maStr = txtMaLoaiGhe.getText().trim();
        if (maStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã loại ghế không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Khôi phục loại ghế này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int id = Integer.parseInt(maStr);
            if (control.khoiPhucLoaiGhe(id)) {
                JOptionPane.showMessageDialog(this, "Đã khôi phục loại ghế!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                resetFormHoanToan();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã loại ghế không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuLyTim() {
        String ten = txtTenLoaiGhe.getText().trim();
        modelGhe.setRowCount(0);
        if (ten.isEmpty()) {
            control.loadData(modelGhe);
            return;
        }
        var ds = control.timLoaiGhe(ten);
        if (ds.isEmpty()) {
            modelGhe.addRow(new Object[]{"", "Không tìm thấy loại ghế nào.", ""});
        } else {
            for (Object[] row : ds) {
                modelGhe.addRow(row);
            }
        }
    }

    private void xuLyLamMoi() {
        txtMaLoaiGhe.setText("");
        txtTenLoaiGhe.setText("");
        tblLoaiGhe.clearSelection();
        control.loadData(modelGhe);
        
        setEnableTextFields(false);
    }
    private void resetFormHoanToan() {
        xuLyLamMoi();
        chucNangHienTai = "";
        setEnableTextFields(false);
    }

    public static void main(String[] args) {
        LookAndFeelManager.setNimbusLookAndFeel();
        SwingUtilities.invokeLater(() -> new QuanLyLoaiGhe().setVisible(true));
    }
}