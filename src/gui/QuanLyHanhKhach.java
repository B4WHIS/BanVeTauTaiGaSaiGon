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
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser; // thêm JDateChooser

import entity.NhanVien;

public class QuanLyHanhKhach extends JFrame implements ActionListener {

    private JTable tblHanhKhach;
    private DefaultTableModel modelHK;
    private JTextField txtMaHK, txtHoTen, txtCMND, txtSoDT;
    private JDateChooser dateNgaySinh; // thay JTextField bằng JDateChooser
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTroVe;
    private NhanVien nhanVienHienTai;

    public QuanLyHanhKhach() {
        setTitle("Quản lý hành khách");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // Title
        JLabel lblTitle = new JLabel("QUẢN LÝ HÀNH KHÁCH", SwingConstants.CENTER);
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
        JLabel lblMaHK = new JLabel("Mã HK:");
        JLabel lblHoTen = new JLabel("Họ tên:");
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        JLabel lblCMND = new JLabel("CMND/CCCD:");
        JLabel lblSoDT = new JLabel("Số điện thoại:");
        JLabel[] labels = {lblMaHK, lblHoTen, lblNgaySinh, lblCMND, lblSoDT};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        // Text fields
        txtMaHK = new JTextField();
        txtHoTen = new JTextField();
        dateNgaySinh = new JDateChooser(); // tạo JDateChooser
        txtCMND = new JTextField();
        txtSoDT = new JTextField();
        JTextField[] textFields = {txtMaHK, txtHoTen, txtCMND, txtSoDT};
        for (JTextField txt : textFields) {
            txt.setFont(txtFont);
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }
        dateNgaySinh.setFont(txtFont);
        dateNgaySinh.setPreferredSize(new Dimension(200, 30));

        // Add labels + fields
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã HK
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        pnlForm.add(lblMaHK, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(txtMaHK, gbc);

        // Họ tên
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        pnlForm.add(lblHoTen, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(txtHoTen, gbc);

        // Ngày sinh
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        pnlForm.add(lblNgaySinh, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(dateNgaySinh, gbc);

        // CMND
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        pnlForm.add(lblCMND, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(txtCMND, gbc);

        // Số điện thoại
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        pnlForm.add(lblSoDT, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(txtSoDT, gbc);

        // Buttons
        btnThem = taoButton("Thêm", new Color(46, 204, 113), "/img/plus2.png");
        btnSua = taoButton("Sửa", new Color (187, 102, 83), "/img/repair.png");
        btnXoa = taoButton("Xóa", new Color(231, 76, 60), "/img/trash-bin.png");
        btnReset = taoButton("Làm mới", new Color(52, 152, 219), "/img/undo2.png");
        btnExport = taoButton("Xuất Excel", new Color(241, 196, 15), "/img/export2.png");

        JPanel pnlButtons = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnReset);
        pnlButtons.add(btnExport);

//        pnlLeft.add(lblLeftTitle, BorderLayout.NORTH);
        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        // Right panel - table + border
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(Color.WHITE);

        String[] colHeader = {"Mã HK", "Họ tên", "Ngày sinh", "CMND/CCCD", "Số điện thoại"};
        modelHK = new DefaultTableModel(colHeader, 0);
        tblHanhKhach = new JTable(modelHK);
        tblHanhKhach.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblHanhKhach.setRowHeight(28);
        tblHanhKhach.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblHanhKhach.setSelectionBackground(new Color(173, 216, 230));

        
        JScrollPane scroll = new JScrollPane(tblHanhKhach);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scroll.setBackground(Color.white);
        // Tạo panel có border chứa table
        JPanel pnlTableBorder = new JPanel(new BorderLayout());
 
        TitledBorder titleBorderTrungTam = BorderFactory.createTitledBorder("Danh Sách Hành Khách");
        titleBorderTrungTam.setTitleFont(fontTieuDe);
        
        
        pnlTableBorder.setBorder(titleBorderTrungTam);
        
        pnlTableBorder.add(scroll, BorderLayout.CENTER);
        pnlRight.add(pnlTableBorder, BorderLayout.CENTER);
        
        pnlTableBorder.setBackground(Color.white);
        
        // Footer 
        JPanel pnlFooter = new JPanel(new BorderLayout());
//        pnlFooter.setBackground(new Color(103,192,144));
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "");
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnTroVe.setPreferredSize(new Dimension(160, 60));
        pnlFooter.add(btnTroVe, BorderLayout.WEST);
        btnTroVe.addActionListener(this);

        // Add
        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(pnlRight, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);

        add(pnlMain);
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
        URL iconUrl = QuanLyHanhKhach.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnTroVe) {
            this.dispose();
            new MHC_NhanVienQuanLy(nhanVienHienTai).setVisible(true);
        }
    }

    public static void main(String[] args) {
        LookAndFeelManager.setNimbusLookAndFeel();
        SwingUtilities.invokeLater(() -> new QuanLyHanhKhach().setVisible(true));
    }
}
