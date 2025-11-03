package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;
import control.QuanLyHoaDonControl;
import entity.HoaDon;

public class QuanLyHoaDon extends JFrame implements ActionListener {

    private JTable tblHoaDon;
    private DefaultTableModel modelHD;
    private JTextField txtMaHD, txtTongTien, txtTenHanhKhach, txtNhanVienLap;
    private JDateChooser dateNgayLap;
    private JButton btnTimKiem, btnReset, btnExport, btnTroVe;

    private QuanLyHoaDonControl hdControl = new QuanLyHoaDonControl();

    public QuanLyHoaDon() {
        setTitle("Quản lý hóa đơn");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        JLabel lblTitle = new JLabel("QUẢN LÝ HÓA ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103, 192, 144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // panel nhập thong tin tìm kiếm
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(450, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        JLabel lblLeftTitle = new JLabel("THÔNG TIN HÓA ĐƠN", SwingConstants.CENTER);
        lblLeftTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLeftTitle.setForeground(new Color(103, 192, 144));
        lblLeftTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setBackground(Color.WHITE);

        Font lblFont = new Font("Segoe UI", Font.BOLD, 19);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 20);

        JLabel lblMaHD = new JLabel("Mã Hóa Đơn: ");
        JLabel lblNgayLap = new JLabel("Ngày Lập: ");
        JLabel lblTongTien = new JLabel("Tổng Tiền: ");
        JLabel lblTenHanhKhach = new JLabel("Tên Hành Khách: ");
        JLabel lblNhanVienLap = new JLabel("Nhân Viên Lập: ");

        JLabel[] labels = {lblMaHD, lblNgayLap, lblTongTien, lblTenHanhKhach, lblNhanVienLap};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        txtMaHD = new JTextField();
        dateNgayLap = new JDateChooser();
        dateNgayLap.setDateFormatString("dd/MM/yyyy");
        dateNgayLap.setFont(txtFont);
        dateNgayLap.setPreferredSize(new Dimension(200, 40));
        txtTongTien = new JTextField();
        txtTenHanhKhach = new JTextField();
        txtNhanVienLap = new JTextField();

        JTextField[] textFields = {txtMaHD, txtTongTien, txtTenHanhKhach, txtNhanVienLap};
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

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        pnlForm.add(lblMaHD, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(txtMaHD, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        pnlForm.add(lblNgayLap, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(dateNgayLap, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        pnlForm.add(lblTongTien, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(txtTongTien, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        pnlForm.add(lblTenHanhKhach, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(txtTenHanhKhach, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        pnlForm.add(lblNhanVienLap, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        pnlForm.add(txtNhanVienLap, gbc);

        // BUTTON
        btnTimKiem = taoButton("Tìm Kiếm", new Color(46, 204, 113), "/img/search.png");
        btnReset = taoButton("Làm mới", new Color(52, 152, 219), "/img/reset.png");
        btnExport = taoButton("Xuất Excel", new Color(155, 89, 182), "/img/export.png");
        btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "/img/loginicon.png");

        JPanel pnlButtons = new JPanel(new GridLayout(1, 3, 10, 10));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.add(btnTimKiem);
        pnlButtons.add(btnReset);
        pnlButtons.add(btnExport);

        pnlLeft.add(lblLeftTitle, BorderLayout.NORTH);
        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        // bảng thông tin
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(new Color(245, 247, 250));

        String[] colHeader = {"Mã HĐ", "Ngày Lập", "Tổng Tiền", "Tên Hành Khách", "Nhân Viên Lập"};
        modelHD = new DefaultTableModel(colHeader, 0);
        tblHoaDon = new JTable(modelHD);
        tblHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblHoaDon.setRowHeight(28);
        tblHoaDon.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblHoaDon.setSelectionBackground(new Color(173, 216, 230));

        JScrollPane scroll = new JScrollPane(tblHoaDon);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnlRight.add(scroll, BorderLayout.CENTER);

        // FOOTER
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(new Color(103, 192, 144));
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnTroVe.setPreferredSize(new Dimension(130, 45));
        pnlFooter.add(btnTroVe, BorderLayout.WEST);

        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(pnlRight, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);

        add(pnlMain);

        btnTimKiem.addActionListener(this);
        btnReset.addActionListener(this);
        btnExport.addActionListener(this);
        btnTroVe.addActionListener(this);

        // LOAD TABLE BAN ĐẦU
        loadTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnTimKiem) {
            // Tìm kiếm hóa đơn
            try {
                String maHD = txtMaHD.getText().trim();
                String tenHK = txtTenHanhKhach.getText().trim();
                String tenNV = txtNhanVienLap.getText().trim();
                String tongTienStr = txtTongTien.getText().trim();
                boolean isNgayLapEmpty = (dateNgayLap.getDate() == null);

                if (maHD.isEmpty() && tenHK.isEmpty() && tenNV.isEmpty() && tongTienStr.isEmpty() && isNgayLapEmpty) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập tối thiểu 1 thông tin để tìm kiếm!", 
                                                  "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                BigDecimal tongTien = null;
                LocalDateTime ngayLap = null;

                if (!tongTienStr.isEmpty()) tongTien = new BigDecimal(tongTienStr);
                if (!isNgayLapEmpty) {
                    ngayLap = dateNgayLap.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
                }

                List<HoaDon> list = hdControl.timKiemHoaDon(maHD, tenHK, tenNV, ngayLap, tongTien);
                modelHD.setRowCount(0);
                for (HoaDon hd : list) {
                    modelHD.addRow(new Object[] {
                            hd.getMaHoaDon(),
                            hd.getNgayLap().toLocalDate(),
                            hd.getTongTien(),
                            hd.getMaHanhKhach().getHoTen(),
                            hd.getMaNhanVien().getHoTen()
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + ex.getMessage());
            }
        } else if (src == btnReset) {
            // Reset form
            txtMaHD.setText("");
            txtTenHanhKhach.setText("");
            txtNhanVienLap.setText("");
            txtTongTien.setText("");
            dateNgayLap.setDate(null);
            loadTable();
        } else if (src == btnExport) {
            // Xuất Excel
            ExcelExporter.exportToExcel(tblHoaDon, this);
        } else if (src == btnTroVe) {
            // Trở về
            try {
                new MHC_NhanVienBanVe().setVisible(true);
                dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //load bảng
    private void loadTable() {
        try {
            modelHD.setRowCount(0);
            List<HoaDon> list = hdControl.timKiemHoaDon("", "", "", null, null);
            for (HoaDon hd : list) {
            	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            	modelHD.addRow(new Object[] {
            	    hd.getMaHoaDon(),
            	    hd.getNgayLap().toLocalDate().format(dtf),
            	    hd.getTongTien(),
            	    hd.getMaHanhKhach().getHoTen(),
            	    hd.getMaNhanVien().getHoTen()
            	});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi load dữ liệu: " + e.getMessage());
        }
    }

    // button
    public static JButton taoButton(String text, Color bg, String iconPath) {
        ImageIcon icon = chinhKichThuoc(iconPath, 24, 24);
        JButton btn = new JButton(text, icon != null ? icon : null);
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
        URL iconUrl = QuanLyHoaDon.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
       
        return new ImageIcon(img);
    }

    public static void main(String[] args) {
        LookAndFeelManager.setNimbusLookAndFeel();
        new QuanLyHoaDon().setVisible(true);
    }
}
