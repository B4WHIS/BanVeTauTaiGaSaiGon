package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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
import java.awt.event.MouseListener;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import control.QuanLyGaControl;
import dao.GaDAO;
import entity.Ga;
import entity.NhanVien;

public class QuanLyGa extends JFrame implements ActionListener, MouseListener {
    private QuanLyGaControl controlGa;
    private JTable tblGa;
    private DefaultTableModel modelGa;
    private JTextField txtMaGa, txtTenGa, txtDiaChi;
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTroVe;
    private NhanVien nhanVienHienTai;
    private String chucNangHienTai = "";
	private JButton btnTim;

    public QuanLyGa() {
        setTitle("Quản lý ga");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        controlGa = new QuanLyGaControl();
        GaDAO gaDao = new GaDAO();

        // Main panel
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // Title
        JLabel lblTitle = new JLabel("QUẢN LÝ GA", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 44));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103,192,144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // Left panel - form + buttons
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(440, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));
        Font fontTieuDe = new Font("Segoe UI", Font.BOLD, 24);
        TitledBorder titleBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Nhập Thông Tin Tra Cứu",
                TitledBorder.CENTER, TitledBorder.TOP,
                fontTieuDe, Color.BLACK);
        titleBorder.setTitleFont(fontTieuDe);
        pnlLeft.setBorder(titleBorder);

        // Form panel
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setBackground(Color.WHITE);
        Font lblFont = new Font("Segoe UI", Font.BOLD, 19);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 20);

        // Labels
        JLabel lblMaGa = new JLabel("Mã ga:");
        JLabel lblTenGa = new JLabel("Tên ga:");
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        JLabel[] labels = {lblMaGa, lblTenGa, lblDiaChi};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.LEFT);
        }

        // Text fields
        txtMaGa = new JTextField();
        txtMaGa.setEnabled(false);
        txtTenGa = new JTextField();
        txtDiaChi = new JTextField();
        txtDiaChi.setFont(new Font("Segoe UI", Font.BOLD, 15));
        JTextField[] textFields = {txtMaGa, txtTenGa, txtDiaChi};
        for (JTextField txt : textFields) {
            txt.setFont(txtFont);
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }

        // Add labels + fields
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã ga
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.EAST;
        pnlForm.add(lblMaGa, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        pnlForm.add(txtMaGa, gbc);

        // Tên ga
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.EAST;
        pnlForm.add(lblTenGa, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        pnlForm.add(txtTenGa, gbc);

        // Địa chỉ
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.EAST;
        pnlForm.add(lblDiaChi, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        pnlForm.add(txtDiaChi, gbc);

        // Buttons
        btnThem = taoButton("Thêm", new Color(46, 204, 113), "/img/plus2.png");
        btnSua = taoButton("Sửa", new Color(187, 102, 83), "/img/repair.png");
        btnXoa = taoButton("Xóa", new Color(231, 76, 60), "/img/trash-bin.png");
        btnReset = taoButton("Làm mới", new Color(52, 152, 219), "/img/undo2.png");
        btnExport = taoButton("Xuất Excel", new Color(241, 196, 15), "/img/export2.png");
        btnTim = taoButton("Tìm", new Color(155, 89, 182), "/img/magnifying-glass.png");
        
        JPanel pnlButtons = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnReset);
        pnlButtons.add(btnExport);
        pnlButtons.add(btnTim);
        

        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        // Right panel - table
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(Color.WHITE);
        String[] colHeader = {"Mã ga", "Tên ga", "Địa chỉ"};
        modelGa = new DefaultTableModel(colHeader, 0);
        tblGa = new JTable(modelGa);
        tblGa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblGa.setRowHeight(28);
        tblGa.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblGa.setSelectionBackground(new Color(58, 111, 67));
        
        tblGa.getColumnModel().getColumn(0).setMaxWidth(140);
        tblGa.getColumnModel().getColumn(1).setMaxWidth(440);
        tblGa.getColumnModel().getColumn(1).setMinWidth(200);
//        tblGa.getColumnModel().getColumn(2).setPreferredWidth(350);
        
        
        JScrollPane scroll = new JScrollPane(tblGa);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scroll.setBackground(Color.white);

        // Panel có border cho bảng
        JPanel pnlTableBorder = new JPanel(new BorderLayout());
        TitledBorder titleBorderTrungTam = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Danh sách ga",
                TitledBorder.CENTER, TitledBorder.TOP,
                fontTieuDe, Color.BLACK);
        titleBorderTrungTam.setTitleFont(fontTieuDe);
        pnlTableBorder.setBorder(titleBorderTrungTam);
        pnlTableBorder.add(scroll, BorderLayout.CENTER);
        pnlRight.add(pnlTableBorder, BorderLayout.CENTER);
        pnlTableBorder.setBackground(Color.white);

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

        // Load dữ liệu
        taiDuLieuGaLenBang();

        // Add listeners
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnReset.addActionListener(this);
        btnExport.addActionListener(this);
        btnTroVe.addActionListener(this);
        btnTim.addActionListener(this);
        
        
        tblGa.addMouseListener(this);	
        
        setEnableTextFields(false);
        add(pnlMain);
    }

    private void setEnableTextFields(boolean enabled) {
        txtTenGa.setEnabled(enabled);
        txtDiaChi.setEnabled(enabled);
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
        URL iconUrl = QuanLyGa.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnThem) {
            if (!"them".equals(chucNangHienTai)) {
                chucNangHienTai = "them";
                JOptionPane.showMessageDialog(this, "Bạn đã chọn chức năng THÊM ga. Hãy nhập thông tin rồi nhấn lại nút THÊM để xác nhận.");
                setEnableTextFields(true);
                xuLyLamMoi();
            } else {
                xuLyThemGa();
            }
        } else if (src == btnSua) {
            if (!"sua".equals(chucNangHienTai)) {
                chucNangHienTai = "sua";
                JOptionPane.showMessageDialog(this, "Bạn đã chọn chức năng SỬA ga.");
                setEnableTextFields(true);
            } else {
                xuLySuaGa();
            }
        } else if (src == btnXoa) {
            if (!"xoa".equals(chucNangHienTai)) {
                chucNangHienTai = "xoa";
                JOptionPane.showMessageDialog(this, "Bạn đã chọn chức năng XÓA ga.");
            } else {
                xuLyXoaGa();
            }
        }else if (src == btnTim) {
            if (!"tim".equals(chucNangHienTai)) {
                chucNangHienTai = "tim";
                JOptionPane.showMessageDialog(this, "Nhập thông tin cần tìm rồi nhấn TÌM.");
                setEnableTextFields(true);
            } else {
                xuLyTimGa();
            }
        } else if (src == btnReset) {
            xuLyLamMoi();
        } else if (src == btnTroVe) {
            xyLyTroVe();
        } else if (src == btnExport) {
            ExcelExporter.exportToExcel(tblGa, this);
        }
    }

    private void xuLyTimGa() {
		// TODO Auto-generated method stub
    	String ten = txtTenGa.getText().trim();
        String dc = txtDiaChi.getText().trim();
        if (ten.isEmpty() && dc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ít nhất một thông tin để tìm.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        controlGa.timKiemGa(ten, dc, modelGa);
	}

	private void xuLyThemGa() {
        String tenGa = txtTenGa.getText().trim();
        String diaChi = txtDiaChi.getText().trim();

        if (tenGa.isEmpty() || diaChi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Tên ga và Địa chỉ!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Ga gaMoi = new Ga(null, tenGa, diaChi);
            boolean thanhCong = controlGa.themGa(gaMoi);
            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Thêm ga mới thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                taiDuLieuGaLenBang();
                xuLyLamMoiFormInput();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại. Có thể bị trùng tên ga.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuLySuaGa() {
        int row = tblGa.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ga cần sửa!");
            return;
        }

        String maGa = txtMaGa.getText().trim();
        String tenGa = txtTenGa.getText().trim();
        String diaChi = txtDiaChi.getText().trim();

        if (tenGa.isEmpty() || diaChi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn sửa thông tin ga này?", "Xác nhận sửa", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Ga gaSua = new Ga(maGa, tenGa, diaChi);
            boolean thanhCong = controlGa.capNhatGa(gaSua);
            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Sửa ga thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                taiDuLieuGaLenBang();
                xuLyLamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Sửa thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuLyXoaGa() {
        int row = tblGa.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một ga để xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maGa = modelGa.getValueAt(row, 0).toString();
        String tenGa = modelGa.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn XÓA ga " + tenGa + "?\nHành động này không thể hoàn tác!",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean thanhCong = controlGa.xoaGa(maGa);
                if (thanhCong) {
                    JOptionPane.showMessageDialog(this, "Xóa ga thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    taiDuLieuGaLenBang();
                    xuLyLamMoiFormInput();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại. Ga có thể đang được sử dụng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi hệ thống khi xóa: " + e.getMessage(), "Lỗi nặng", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xyLyTroVe() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát khỏi màn hình quản lý ga?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            // Quay về màn hình chính (giả sử có nhân viên)
            new MHC_NhanVienQuanLy(nhanVienHienTai).setVisible(true);
        }
    }

    private void xuLyLamMoi() {
        txtMaGa.setText("");
        txtTenGa.setText("");
        txtDiaChi.setText("");
        tblGa.clearSelection();
        chucNangHienTai = "";
    }

    private void xuLyLamMoiFormInput() {
        xuLyLamMoi();
        txtTenGa.requestFocus();
    }

    private void taiDuLieuGaLenBang() {
        modelGa.setRowCount(0);
        try {
            List<Ga> danhSachGa = controlGa.layDanhSachGa();
            for (Ga ga : danhSachGa) {
                Object[] rowData = {
                    ga.getMaGa(),
                    ga.getTenGa(),
                    ga.getDiaChi()
                };
                modelGa.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi CSDL khi tải danh sách ga: " + e.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LookAndFeelManager.setNimbusLookAndFeel();
        SwingUtilities.invokeLater(() -> new QuanLyGa().setVisible(true));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tblGa.getSelectedRow();
        if (row >= 0) {
            hienThiDuLieuLenForm(row);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
  
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    	
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    	
    }
    @Override
    public void mouseExited(MouseEvent e) {
    	
    }

    private void hienThiDuLieuLenForm(int row) {
        txtMaGa.setText(modelGa.getValueAt(row, 0).toString());
        txtTenGa.setText(modelGa.getValueAt(row, 1).toString());
        txtDiaChi.setText(modelGa.getValueAt(row, 2).toString());
    }
}