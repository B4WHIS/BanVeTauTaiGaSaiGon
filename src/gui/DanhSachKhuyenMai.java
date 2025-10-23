package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import control.QuanLyKhuyenMaiControl;
import dao.KhuyenMaiDAO;
import entity.KhuyenMai;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DanhSachKhuyenMai extends JFrame implements ActionListener {
    private JTable tblKhuyenMai;
    private DefaultTableModel modelKM;
    private JTextField txtMaKM, txtTenKM, txtNgayBatDau, txtNgayKetThuc, txtMucGiamGia, txtDieuKien;
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTroVe;

    public DanhSachKhuyenMai() {
        setTitle("Quản lý khuyến mãi");
        setSize(1400, 950);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        setJMenuBar(taoMenuBar());

        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        JLabel lblTitle = new JLabel("QUẢN LÝ KHUYẾN MÃI", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103,192,144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);


        JPanel pnlLeft = new JPanel(new BorderLayout(8, 8));
        pnlLeft.setPreferredSize(new Dimension(430, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        JLabel lblLeftTitle = new JLabel("THÔNG TIN KHUYẾN MÃI", SwingConstants.CENTER);
        lblLeftTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLeftTitle.setForeground(new Color(74, 140, 103));
        lblLeftTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        pnlForm.setBackground(Color.WHITE);

        Font lblFont = new Font("Segoe UI", Font.BOLD, 15);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 16);

        JLabel lblMaKM = new JLabel("Mã Khuyến Mãi:");
        JLabel lblTenKM = new JLabel("Tên Khuyến Mãi:");
        JLabel lblNgayBatDau = new JLabel("Ngày Bắt Đầu:");
        JLabel lblNgayKetThuc = new JLabel("Ngày Kết Thúc:");
        JLabel lblMucGiamGia = new JLabel("Mức Giảm (%):");
        JLabel lblDieuKien = new JLabel("Điều Kiện:");

        JLabel[] labels = {lblMaKM, lblTenKM, lblNgayBatDau, lblNgayKetThuc, lblMucGiamGia, lblDieuKien};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        txtMaKM = new JTextField();
        txtTenKM = new JTextField();
        txtNgayBatDau = new JTextField();
        txtNgayKetThuc = new JTextField();
        txtMucGiamGia = new JTextField();
        txtDieuKien = new JTextField();

        JTextField[] textFields = {txtMaKM, txtTenKM, txtNgayBatDau, txtNgayKetThuc, txtMucGiamGia, txtDieuKien};
        for (JTextField txt : textFields) {
            txt.setFont(txtFont);
            txt.setPreferredSize(new Dimension(200, 30));
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150)),
                    BorderFactory.createEmptyBorder(4, 6, 4, 6)
            ));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
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


        btnThem = taoButton("Thêm", new Color(46, 204, 113), "/img/plus.png");
        btnSua = taoButton("Sửa", new Color(241, 196, 15), "/img/maintenance.png");
        btnXoa = taoButton("Xóa", new Color(231, 76, 60), "/img/bin.png");
        btnReset = taoButton("Làm mới", new Color(52, 152, 219), "/img/reset.png");
        btnExport = taoButton("Xuất Excel", new Color(155, 89, 182), "/img/export.png");

        JPanel pnlButtons = new JPanel(new GridLayout(3, 2, 8, 8));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnReset);
        pnlButtons.add(btnExport);

        pnlLeft.add(lblLeftTitle, BorderLayout.NORTH);
        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(new Color(245, 247, 250));

        String[] colHeader = {"Mã KM", "Tên khuyến mãi", "Ngày bắt đầu", "Ngày kết thúc", "Mức giảm (%)", "Điều kiện"};
        modelKM = new DefaultTableModel(colHeader, 0);
        tblKhuyenMai = new JTable(modelKM);
        tblKhuyenMai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblKhuyenMai.setRowHeight(26);
        tblKhuyenMai.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblKhuyenMai.setSelectionBackground(new Color(173, 216, 230));

        JScrollPane scroll = new JScrollPane(tblKhuyenMai);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnlRight.add(scroll, BorderLayout.CENTER);


        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(new Color(103,192,144)); 
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "/img/loginicon.png");
        btnTroVe.setPreferredSize(new Dimension(120, 40));
        pnlFooter.add(btnTroVe, BorderLayout.WEST);

       
        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(pnlRight, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);
        add(pnlMain);
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnReset.addActionListener(this);
    }

    private JMenuBar taoMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        menuBar.setBackground(new Color(230, 230, 230));

        JMenu mnuHeThong = new JMenu("Hệ Thống");
        JMenu mnuNghiepVuVe = new JMenu("Nghiệp Vụ Vé");
        JMenu mnuQly = new JMenu("Quản Lý");
        JMenu mnuTraCuu = new JMenu("Tra Cứu");
        JMenu mnuThongKe = new JMenu("Thống Kê");
        JMenu mnuTroGiup = new JMenu("Trợ Giúp");

        mnuHeThong.setIcon(chinhKichThuoc("/img/heThong3.png", 24, 24));
        mnuNghiepVuVe.setIcon(chinhKichThuoc("/img/ve2.png", 24, 24));
        mnuQly.setIcon(chinhKichThuoc("/img/quanLy.png", 24, 24));
        mnuTraCuu.setIcon(chinhKichThuoc("/img/traCuu.png", 24, 24));
        mnuThongKe.setIcon(chinhKichThuoc("/img/thongKe.png", 24, 24));
        mnuTroGiup.setIcon(chinhKichThuoc("/img/troGiup.png", 24, 24));

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

        JLabel lblXinChao = new JLabel("Xin Chào, [Tên Nhân Viên]");
        lblXinChao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel lblNVBV = new JLabel(chinhKichThuoc("/img/nhanVienBanVe.png", 40, 40));
        menuBar.add(lblXinChao);
        menuBar.add(lblNVBV);

        return menuBar;
    }

    private JButton taoButton(String text, Color bg, String iconPath) {
        JButton btn = new JButton(text, chinhKichThuoc(iconPath, 20, 20));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
        URL iconUrl = DanhSachKhuyenMai.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    
    
    
    
    //sự kiện 
    public void actionPerformed(ActionEvent e) {
    	QuanLyKhuyenMaiControl qlKM = new QuanLyKhuyenMaiControl();
        Object src = e.getSource();
        try {
        	if(src == btnThem){
        	    try {
        	        KhuyenMai km = kiemtradulieunhap(); 
        	        if(qlKM.themKhuyenMai(km)){
        	            JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công!");
        	            TaiDuLieuKhuyenMai();
        	            resetForm();
        	        } else {
        	            JOptionPane.showMessageDialog(this, "Trùng mã khuyến mãi, vui lòng nhập mã khuyến mãi khác! ");
        	        }
        	    } catch(Exception ex){
        	        JOptionPane.showMessageDialog(this, ex.getMessage());
        	    }
        	}

            else if (src == btnSua) {
                int row = tblKhuyenMai.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa!");
                    return;
                }
                KhuyenMai km = kiemtradulieunhap();
                if (qlKM.capNhatKhuyenMai(km)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi thành công!");
                    TaiDuLieuKhuyenMai();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            } 
            else if (src == btnXoa) {
                int row = tblKhuyenMai.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
                    return;
                }
                String maKM = tblKhuyenMai.getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khuyến mãi này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (qlKM.xoaKhuyenMai(maKM)) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công!");
                        TaiDuLieuKhuyenMai();
                        resetForm();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                    }
                }
            } 
            else if (src == btnReset) {
                resetForm();
            } 
            else if (src == btnExport) {
            } 
            else if (src == btnTroVe) {
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
    
    
    
    //kiểm tra dữ liệu vào có đầy đủ không và tạo dữ liệu vào form
    private KhuyenMai kiemtradulieunhap() throws Exception {
        String maKM = txtMaKM.getText().trim();
        String tenKM = txtTenKM.getText().trim();
        String ngayBDStr = txtNgayBatDau.getText().trim();
        String ngayKTStr = txtNgayKetThuc.getText().trim();
        String mucGiamStr = txtMucGiamGia.getText().trim();
        String dieuKien = txtDieuKien.getText().trim();

        // Kiểm tra các trường nhập 
        if(maKM.isEmpty()|| tenKM.isEmpty()|| ngayBDStr.isEmpty()||ngayKTStr.isEmpty()|| mucGiamStr.isEmpty()|| dieuKien.isEmpty()) {
        	throw new Exception("Vui lòng nhập đầy đủ thông tin của khuyến mãi !");
        }
        else if(maKM.isEmpty()) {
            txtMaKM.requestFocus();
            throw new Exception("Vui lòng nhập Mã khuyến mãi!");
        }
        else if(tenKM.isEmpty()) {
            txtTenKM.requestFocus();
            throw new Exception("Vui lòng nhập Tên khuyến mãi!");
        }
        else if(ngayBDStr.isEmpty()) {
            txtNgayBatDau.requestFocus();
            throw new Exception("Vui lòng nhập Ngày bắt đầu!");
        }
        else if(ngayKTStr.isEmpty()) {
            txtNgayKetThuc.requestFocus();
            throw new Exception("Vui lòng nhập Ngày kết thúc!");
        }
        else if(mucGiamStr.isEmpty()) {
            txtMucGiamGia.requestFocus();
            throw new Exception("Vui lòng nhập Mức giảm!");
        }
        else  if(dieuKien.isEmpty()) {
            txtDieuKien.requestFocus();
            throw new Exception("Vui lòng nhập Điều kiện!");
        }

        // Kiểm tra định dạng ngày
        LocalDate ngayBD, ngayKT;
        try {
            ngayBD = LocalDate.parse(ngayBDStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            throw new Exception("Ngày bắt đầu không hợp lệ! (VD: Định dạng dd/MM/yyyy)");
        }
        try {
            ngayKT = LocalDate.parse(ngayKTStr,DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            throw new Exception("Ngày kết thúc không hợp lệ! VD: Định dạng dd/MM/yyyy");
        }

        // Kiểm tra ngày kết thúc >= ngày bắt đầu
        if(ngayKT.isBefore(ngayBD)) {
            throw new Exception("Ngày kết thúc phải sau hoặc bằng Ngày bắt đầu!");
        }

        // Kiểm tra mức giảm hợp lệ
        BigDecimal mucGiam;
        try {
            mucGiam = new BigDecimal(mucGiamStr);
            if(mucGiam.compareTo(BigDecimal.ZERO) < 0 || mucGiam.compareTo(new BigDecimal("100")) > 0) {
                throw new Exception("Mức giảm phải là số từ 0 đến 100!");
            }
        } catch(Exception e) {
            throw new Exception("Mức giảm phải là số hợp lệ từ 0 đến 100!");
        }

        return new KhuyenMai(maKM, tenKM, mucGiam, ngayBD, ngayKT, dieuKien);
    }
    
    

    // hàm reset
    private void resetForm() {
        txtMaKM.setText("");
        txtTenKM.setText("");
        txtNgayBatDau.setText("");
        txtNgayKetThuc.setText("");
        txtMucGiamGia.setText("");
        txtDieuKien.setText("");
    }
    
    
    
    //hàm load dữ liệu lên bảng
    private void TaiDuLieuKhuyenMai() {
        try {
            KhuyenMaiDAO dao = new KhuyenMaiDAO();
            ArrayList<KhuyenMai> ds = dao.LayTatCaKhuyenMai();
            //xóa  dữ liệu trên bảng nếu mà khoog có cái này thì ki mà xóa 
            // hay là cập nhật thì nó sẽ thêm không thôi chứ nó không xóa 
            modelKM.setRowCount(0);
            // Đổ dữ liệu mới vào   
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
         for (KhuyenMai km : ds) {
             modelKM.addRow(new Object[]{
                 km.getMaKhuyenMai(),
                 km.getTenKhuyenMai(),
                 km.getNgayBatDau().format(formatter),   
                 km.getNgayKetThuc().format(formatter),  
                 km.getMucGiamGia(),
                 km.getDieuKien()
             });
         }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: ");
        }
        
        
        
        
     // sự kiện khi chọn một đồi tượng trong bảng thì nó sẽ chuyển hết dữ liệu lên các text tương ứng
        tblKhuyenMai.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tblKhuyenMai.getSelectedRow() != -1) {
                int row = tblKhuyenMai.getSelectedRow();
                txtMaKM.setText(tblKhuyenMai.getValueAt(row, 0).toString());
                txtTenKM.setText(tblKhuyenMai.getValueAt(row, 1).toString());
                txtNgayBatDau.setText(tblKhuyenMai.getValueAt(row, 2).toString());
                txtNgayKetThuc.setText(tblKhuyenMai.getValueAt(row, 3).toString());
                txtMucGiamGia.setText(tblKhuyenMai.getValueAt(row, 4).toString());
                txtDieuKien.setText(tblKhuyenMai.getValueAt(row, 5).toString());
            }
        });

    }
    public static void main(String[] args) {
    	DanhSachKhuyenMai ds = new DanhSachKhuyenMai();
    	ds.setVisible(true);
    	ds.TaiDuLieuKhuyenMai();
       
    }
}
