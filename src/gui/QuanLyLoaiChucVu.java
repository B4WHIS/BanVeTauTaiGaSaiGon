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

import control.QuanLyLoaiChucVuControl;

public class QuanLyLoaiChucVu extends JFrame implements ActionListener, MouseListener {
    private JTable tblLoaiCV;
    private DefaultTableModel modelCV;
    private JTextField txtMaLoaiCV, txtTenLoaiCV;
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTim, btnTroVe;
    private QuanLyLoaiChucVuControl control = new QuanLyLoaiChucVuControl();
    private String chucNangHienTai = "";

    public QuanLyLoaiChucVu() {
        setTitle("Quản lý danh mục chức vụ");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // Title
        JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC CHỨC VỤ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 44));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103,192,144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // Left panel - form + buttons
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(450, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        Font fontTieuDe = new Font("Segoe UI", Font.BOLD, 24);

        TitledBorder titleBorder = BorderFactory.createTitledBorder("Nhập Thông Tin Tra Cứu");
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
        JLabel lblMa = new JLabel("Mã Loại:");
        JLabel lblTen = new JLabel("Tên Loại:");
        JLabel[] labels = {lblMa, lblTen};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        // Text fields
        txtMaLoaiCV = new JTextField();
        txtMaLoaiCV.setEditable(false); 
        txtMaLoaiCV.setBackground(new Color(230, 230, 230));
        txtTenLoaiCV = new JTextField();
        JTextField[] textFields = {txtMaLoaiCV, txtTenLoaiCV};
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

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            pnlForm.add(labels[i], gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            pnlForm.add(textFields[i], gbc);
        }

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

        // Right panel - table + border
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(Color.WHITE);

        String[] colHeader = {"Mã Loại Chức Vụ", "Tên Loại Chức Vụ"};
        modelCV = new DefaultTableModel(colHeader, 0);
        tblLoaiCV = new JTable(modelCV);
        tblLoaiCV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblLoaiCV.setRowHeight(28);
        tblLoaiCV.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblLoaiCV.setSelectionBackground(new Color(58, 111, 67));
        tblLoaiCV.setSelectionForeground(Color.WHITE);
        
        
        
        tblLoaiCV.getColumnModel().getColumn(0).setMaxWidth(140);

        tblLoaiCV.getColumnModel().getColumn(1).setPreferredWidth(350);
        tblLoaiCV.getColumnModel().getColumn(1).setMinWidth(200);
        
        control.loadData(modelCV);

        
        

        JScrollPane scroll = new JScrollPane(tblLoaiCV);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scroll.setBackground(Color.WHITE);

        // Tạo panel có border chứa table
        JPanel pnlTableBorder = new JPanel(new BorderLayout());

        TitledBorder titleBorderTrungTam = BorderFactory.createTitledBorder("Danh Sách Loại Chức Vụ");
        titleBorderTrungTam.setTitleFont(fontTieuDe);

        pnlTableBorder.setBorder(titleBorderTrungTam);
        pnlTableBorder.add(scroll, BorderLayout.CENTER);
        pnlRight.add(pnlTableBorder, BorderLayout.CENTER);

        pnlTableBorder.setBackground(Color.WHITE);

        // Footer
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "");
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnTroVe.setPreferredSize(new Dimension(160, 60));
        pnlFooter.add(btnTroVe, BorderLayout.WEST);
        btnTroVe.addActionListener(e -> dispose());

        // Add
        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(pnlRight, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnReset.addActionListener(this);
        btnExport.addActionListener(this);
        btnTim.addActionListener(this);
        setEnableTextFields(false);

        tblLoaiCV.addMouseListener(this);
        add(pnlMain);
    }

    private void setEnableTextFields(boolean enabled) {
        txtTenLoaiCV.setEnabled(enabled);
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
        URL iconUrl = QuanLyLoaiChucVu.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void hienThiMaTiepTheo() {
        int maTiepTheo = control.layMaLoaiCVTiepTheo();
        txtMaLoaiCV.setText(String.valueOf(maTiepTheo));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnThem) {
            if (!"them".equals(chucNangHienTai)) {
                chucNangHienTai = "them";
                JOptionPane.showMessageDialog(this, "Bạn đã chọn chức năng THÊM loại chức vụ. Hãy nhập thông tin rồi nhấn lại nút THÊM để xác nhận.");
                setEnableTextFields(true);
                xuLyLamMoi();
                txtTenLoaiCV.requestFocus();
                hienThiMaTiepTheo();
            } else {
                xuLyThemLoaiChucVu();
            }
        } else if (src == btnSua) {
            if (!"sua".equals(chucNangHienTai)) {
                chucNangHienTai = "sua";
                JOptionPane.showMessageDialog(this, "Bạn đã chọn chức năng SỬA loại chức vụ. Hãy chọn bản ghi, chỉnh sửa thông tin rồi nhấn lại nút SỬA để xác nhận.");
                setEnableTextFields(true);
            } else {
                xuLySuaLoaiChucVu();
            }
        } else if (src == btnXoa) {
            if (!"xoa".equals(chucNangHienTai)) {
                chucNangHienTai = "xoa";
                JOptionPane.showMessageDialog(this, "Bạn đã chọn chức năng XÓA loại chức vụ. Hãy chọn bản ghi rồi nhấn lại nút XÓA để xác nhận.");
                setEnableTextFields(true);
            } else {
                xuLyXoaLoaiChucVu();
            }
        } else if (src == btnTim) {
            if (!"tim".equals(chucNangHienTai)) {
                chucNangHienTai = "tim";
                JOptionPane.showMessageDialog(this, "Bạn đã chọn chức năng TÌM loại chức vụ. Hãy nhập thông tin cần tìm rồi nhấn lại nút TÌM để thực hiện.");
                setEnableTextFields(true);
            } else {
                xuLyTimLoaiChucVu();
            }
        } else if (src == btnReset) {
            xuLyLamMoi();
        } else if (src == btnExport) {
           ExcelExporter.exportToExcel(tblLoaiCV, this);
        } else if (src == btnTroVe) {
            // Xử lý trở về tương tự, có thể dispose() hoặc mở màn hình khác
            dispose();
        }
    }

    private void xuLyThemLoaiChucVu() {
        String ten = txtTenLoaiCV.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên loại chức vụ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (control.themLoaiChucVu(ten)) {
            JOptionPane.showMessageDialog(this, "Thêm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            resetForm();
        }
    }

    private void xuLySuaLoaiChucVu() {
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

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn sửa loại chức vụ này?", 
            "Xác nhận sửa", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
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
    }

    private void xuLyXoaLoaiChucVu() {
        int r = tblLoaiCV.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bản ghi để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, 
            "Không thể XÓA được loại chức vụ.", 
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);

    }

    private void xuLyTimLoaiChucVu() {
        String ten = txtTenLoaiCV.getText().trim();
        if (ten.isEmpty()) {
            control.loadData(modelCV);
            return;
        }

        List<Object[]> dsTim = control.timLoaiChucVu(ten);
        modelCV.setRowCount(0);

        if (dsTim != null && !dsTim.isEmpty()) {
            for (Object[] row : dsTim) {
                modelCV.addRow(row);
            }
        } else {
            modelCV.addRow(new Object[]{"", "Không tìm thấy loại chức vụ nào phù hợp."});
        }
    }

    

    private void xuLyLamMoi() {
        txtMaLoaiCV.setText("");
        txtTenLoaiCV.setText("");
        tblLoaiCV.clearSelection();
        control.loadData(modelCV);
        chucNangHienTai = "";
    }

    private void resetForm() {
        xuLyLamMoi();
    }

    public static void main(String[] args) {
    	LookAndFeelManager.setNimbusLookAndFeel();
        SwingUtilities.invokeLater(() -> new QuanLyLoaiChucVu().setVisible(true));
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
        int r = tblLoaiCV.getSelectedRow();
        if (r >= 0) {
            int modelRow = tblLoaiCV.convertRowIndexToModel(r);
            Object maObj = modelCV.getValueAt(modelRow, 0);
            Object tenObj = modelCV.getValueAt(modelRow, 1);
            txtMaLoaiCV.setText(String.valueOf(maObj));
            txtTenLoaiCV.setText(String.valueOf(tenObj));
        }

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}