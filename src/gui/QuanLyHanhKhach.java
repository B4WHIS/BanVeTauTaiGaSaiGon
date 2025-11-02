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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import control.QuanLyHanhKhachControl;
import dao.UuDaiDAO;
import entity.HanhKhach;
import entity.NhanVien;
import entity.UuDai;

public class QuanLyHanhKhach extends JFrame implements ActionListener, MouseListener {

    private QuanLyHanhKhachControl controlHK;
    private JTable tblHanhKhach;
    private DefaultTableModel modelHK;
    private JTextField txtMaHK, txtHoTen, txtCMND, txtSoDT;
    private JDateChooser dateNgaySinh;
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTroVe;
    private NhanVien nhanVienHienTai;
    private String chucNangHienTai = "";
    private JButton btnTim;
    private JComboBox<String> cmbUuDai; 
    private Map<String, String> mapHienThiToMaUuDai = new HashMap<>(); 
    private Map<Integer, String> mapIdToTenLoai = new HashMap<>();
    private Map<String, UuDai> mapMaToUuDai = new HashMap<>(); 

    public QuanLyHanhKhach() {
        setTitle("Quản lý hành khách");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        controlHK = new QuanLyHanhKhachControl();
        // Load ưu đãi và loại ưu đãi
        UuDaiDAO udDao = new UuDaiDAO();
        try {
            mapIdToTenLoai = udDao.layTatCaLoaiUuDai2(); // Sửa tên hàm, xóa "2"
            List<UuDai> dsUuDai = udDao.layTatCaUuDai();

            cmbUuDai = new JComboBox<>();
            cmbUuDai.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            cmbUuDai.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
            ));

            
            for (UuDai ud : dsUuDai) {
                String tenLoai = mapIdToTenLoai.getOrDefault(ud.getIDloaiUuDai(), "Không xác định");
                String dk = ud.getDieuKienApDung() != null ? ud.getDieuKienApDung() : "";
                String hienThi = tenLoai + " - " + dk + " (Giảm " + ud.getMucGiamGia() + "%)";
                cmbUuDai.addItem(hienThi);
                mapHienThiToMaUuDai.put(hienThi, ud.getMaUuDai());
                mapMaToUuDai.put(ud.getMaUuDai(), ud);
            }
            cmbUuDai.setSelectedIndex(0); 
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách ưu đãi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

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
        pnlLeft.setPreferredSize(new Dimension(540, 0));
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

        Font lblFont = new Font("Segoe UI", Font.BOLD, 19); // Nếu không work, thay "Arial"
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 20);

        // Labels
        JLabel lblMaHK = new JLabel("Mã HK:");
        JLabel lblHoTen = new JLabel("Họ tên:");
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        JLabel lblCMND = new JLabel("CMND/CCCD:");
        JLabel lblSoDT = new JLabel("Số điện thoại:");
        JLabel lblUuDai = new JLabel("Ưu đãi:");
        JLabel[] labels = {lblMaHK, lblHoTen, lblNgaySinh, lblCMND, lblSoDT, lblUuDai};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        // Text fields
        txtMaHK = new JTextField();
        txtMaHK.setEnabled(false);
        txtHoTen = new JTextField();
        dateNgaySinh = new JDateChooser();
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
        dateNgaySinh.setMaximumSize(new Dimension(200, 30));
        
        // Add labels + fields with anchor fix
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã HK
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.EAST; // Fix align right for label
        pnlForm.add(lblMaHK, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST; // Align left for field
        pnlForm.add(txtMaHK, gbc);

        // Họ tên
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.EAST;
        pnlForm.add(lblHoTen, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        pnlForm.add(txtHoTen, gbc);

        // Ngày sinh
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.EAST;
        pnlForm.add(lblNgaySinh, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        pnlForm.add(dateNgaySinh, gbc);

        // CMND
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.EAST;
        pnlForm.add(lblCMND, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        pnlForm.add(txtCMND, gbc);

        // Số điện thoại
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.EAST;
        pnlForm.add(lblSoDT, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        pnlForm.add(txtSoDT, gbc);

        // Ưu đãi
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.EAST;
        pnlForm.add(lblUuDai, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        
        cmbUuDai.setPreferredSize(new Dimension(200, 40)); 
        cmbUuDai.setMaximumSize(new Dimension(200, 40));
        
        pnlForm.add(cmbUuDai, gbc);

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

        String[] colHeader = {"Mã HK", "Họ tên", "Ngày sinh", "CMND/CCCD", "Số điện thoại", "Ưu đãi"};
        modelHK = new DefaultTableModel(colHeader, 0);
        tblHanhKhach = new JTable(modelHK);
        tblHanhKhach.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblHanhKhach.setRowHeight(28);
        tblHanhKhach.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblHanhKhach.setSelectionBackground(new Color(58, 111, 67));

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

        taiDuLieuHanhKhachLenBang();

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnReset.addActionListener(this);
        btnTroVe.addActionListener(this);
        btnTim.addActionListener(this);
        btnExport.addActionListener(this);
        setEnableTextFields(false);

        tblHanhKhach.addMouseListener(this);

        add(pnlMain);

    }

    private void setEnableTextFields(boolean enabled) {
        txtHoTen.setEnabled(enabled);
        txtCMND.setEnabled(enabled);
        txtSoDT.setEnabled(enabled);
        dateNgaySinh.setEnabled(enabled);
        cmbUuDai.setEnabled(enabled);
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

        if (src == btnThem) {
            if (!"them".equals(chucNangHienTai)) {
                chucNangHienTai = "them";
                JOptionPane.showMessageDialog(this, "Bạn đã chọn chức năng THÊM hành khách. Hãy nhập thông tin rồi nhấn lại nút THÊM để xác nhận.");
                setEnableTextFields(true);
                xuLyLamMoi();
            } else {
                xuLyThemHanhKhach();
            }
        } else if (src == btnSua) {
            if (!"sua".equals(chucNangHienTai)) {
                chucNangHienTai = "sua";
                JOptionPane.showMessageDialog(this, "Bạn đã chọn chức năng SỬA hành khách.");
                setEnableTextFields(true);
            } else {
                xuLySuaHanhKhach();
            }

        } else if (src == btnXoa) {
            if(!"xoa".equals(chucNangHienTai)) {
                chucNangHienTai = "xoa";
                JOptionPane.showMessageDialog(this, "Bạn đã chọn chức năng XÓA hành khách.");
                setEnableTextFields(true);
            }else {
                xuLyXoaHanhKhach();
            }

        } else if (src == btnTim) {
            if (!"tim".equals(chucNangHienTai)) {
                chucNangHienTai = "tim";
                JOptionPane.showMessageDialog(this, "Bạn đã chọn chức năng TÌM hành khách. Hãy nhập thông tin cần tìm rồi nhấn lại nút TÌM để thực hiện.");
                setEnableTextFields(true);
            } else {
                xuLyTimHanhKhach();
            }
        }
        else if (src == btnReset) {
            xuLyLamMoi();
        } else if (src == btnTroVe) {
            xyLyTroVe();
        } else if (src == btnExport) {
            ExcelExporter.exportToExcel(tblHanhKhach, this);
        }
    }

    private void xuLyTimHanhKhach() {
        String ten = txtHoTen.getText().trim();
        String cmnd = txtCMND.getText().trim();
        String sdt = txtSoDT.getText().trim();
        LocalDate ngaySinh = null;
        if (dateNgaySinh.getDate() != null) {
            ngaySinh = dateNgaySinh.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        String hienThi = (String) cmbUuDai.getSelectedItem();
        String maUuDai = mapHienThiToMaUuDai.get(hienThi);

        if (ten.isEmpty() && cmnd.isEmpty() && sdt.isEmpty() && ngaySinh == null && "UD-01".equals(maUuDai)) {  // Giả sử UD-01 là default, không tìm nếu default
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ít nhất một thông tin để tìm.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            List<HanhKhach> dsTim = controlHK.timHanhKhach(ten, cmnd, sdt, ngaySinh, maUuDai);
            modelHK.setRowCount(0);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (HanhKhach hk : dsTim) {
                String maUuDaiDB = hk.getMaUuDai() != null ? hk.getMaUuDai().trim() : "UD-01";
                UuDai ud = mapMaToUuDai.get(maUuDaiDB);
                String hienThiUuDai = "Mặc định";
                if (ud != null) {
                    String tenLoai = mapIdToTenLoai.getOrDefault(ud.getIDloaiUuDai(), "Không xác định");
                    String dk = ud.getDieuKienApDung() != null ? ud.getDieuKienApDung() : "";
                    hienThiUuDai = tenLoai + " - " + dk + " (Giảm " + ud.getMucGiamGia() + "%)";
                }
                Object[] row = {
                    hk.getMaKH(),
                    hk.getHoTen(),
                    hk.getNgaySinh().format(dtf),
                    hk.getCmndCccd(),
                    hk.getSoDT(),
                    hienThiUuDai
                };
                modelHK.addRow(row);
            }

            if (dsTim.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hành khách phù hợp.", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + e.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xyLyTroVe() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát khỏi màn hình quản lý hành khách?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new MHC_NhanVienQuanLy(nhanVienHienTai).setVisible(true);
        }
    }

    
    private void xuLyLamMoi() {
        txtMaHK.setText("");
        txtHoTen.setText("");
        dateNgaySinh.setDate(null);
        txtCMND.setText("");
        txtSoDT.setText("");
        cmbUuDai.setSelectedIndex(0);
    }

    private void xuLyXoaHanhKhach() {
        int dongDuocChon = tblHanhKhach.getSelectedRow();
        if (dongDuocChon < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hành khách để ẩn/ngừng hoạt động.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maHK = modelHK.getValueAt(dongDuocChon, 0).toString();
        String tenHK = modelHK.getValueAt(dongDuocChon, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn XÓA hành khách " + tenHK + "\nHệ thống sẽ chỉ chuyển " + tenHK + " sang trạng thái NGỪNG HOẠT ĐỘNG (Ẩn khỏi danh sách chính)?",
                "Xác nhận ẩn", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Gọi hàm ẩn/cập nhật trạng thái mới
                boolean thanhCong = controlHK.anHanhKhach(maHK);

                if (thanhCong) {
                    JOptionPane.showMessageDialog(this, "Chuyển trạng thái thành công! Hành khách " + tenHK + " đã được XÓA.", "Thành công", JOptionPane.INFORMATION_MESSAGE);

                    // Reload bảng
                    taiDuLieuHanhKhachLenBang();
                    xuLyLamMoiFormInput();
                } else {
                    JOptionPane.showMessageDialog(this, "Chuyển trạng thái thất bại. Kiểm tra Log.", "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException e) {
                 JOptionPane.showMessageDialog(this, "Lỗi nghiệp vụ: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi hệ thống khi ẩn: " + e.getMessage(), "Lỗi nặng", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void xuLySuaHanhKhach() {
        int row = tblHanhKhach.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hành khách cần sửa!");
            return;
        }

        // Lấy thông tin mới từ fields
        String maHK = txtMaHK.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String cmnd = txtCMND.getText().trim();
        String sdt = txtSoDT.getText().trim();
        Date ngaySinhDate = dateNgaySinh.getDate();
        String hienThi = (String) cmbUuDai.getSelectedItem();
        String maUuDai = mapHienThiToMaUuDai.get(hienThi);

        if (hoTen.isEmpty() || cmnd.isEmpty() || sdt.isEmpty() || ngaySinhDate == null) {
            JOptionPane.showMessageDialog(this, "Phải nhập đủ thông tin!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate ngaySinh = ngaySinhDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (!ngaySinh.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn sửa thông tin hành khách này?", "Xác nhận sửa", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            HanhKhach hkSua = new HanhKhach(maHK, hoTen, ngaySinh, sdt, cmnd, maUuDai);
            boolean thanhCong = controlHK.capNhatHanhKhach(hkSua);
            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Sửa hành khách thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                taiDuLieuHanhKhachLenBang(); // Reload bảng
                xuLyLamMoi(); // Xóa form
            } else {
                JOptionPane.showMessageDialog(this, "Sửa thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuLyThemHanhKhach() {
        String hoTen = txtHoTen.getText().trim();
        String cmnd = txtCMND.getText().trim();
        String sdt = txtSoDT.getText().trim();
        Date ngaySinhDate = dateNgaySinh.getDate();
        String hienThi = (String) cmbUuDai.getSelectedItem();
        String maUuDai = mapHienThiToMaUuDai.get(hienThi);

        if (hoTen.isEmpty() || ngaySinhDate == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Họ tên, Ngày sinh và chọn Ưu đãi.",
            "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate ngaySinh = ngaySinhDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate ngayHienTai = LocalDate.now();
        int tuoi = Period.between(ngaySinh, ngayHienTai).getYears();  // Tính tuổi chính xác

        if (tuoi < 14 && (!cmnd.isEmpty() || !sdt.isEmpty())) {
            // Nếu dưới 14, vẫn cho nhập nhưng không bắt buộc, nên không check empty
        } else if (tuoi >= 14 && (cmnd.isEmpty() || sdt.isEmpty())) {
            JOptionPane.showMessageDialog(this, "Hành khách >=14 tuổi phải nhập CMND/CCCD và Số điện thoại.", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ngaySinh.isBefore(ngayHienTai)) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không được sau hoặc là ngày hiện tại.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            HanhKhach hkMoi = new HanhKhach(null, hoTen, ngaySinh, sdt, cmnd, maUuDai);

            boolean thanhCong = controlHK.themHanhKhach(hkMoi);

            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Thêm hành khách mới thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                taiDuLieuHanhKhachLenBang(); // Reload data
                xuLyLamMoiFormInput(); // Xóa form
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại. Có thể bị trùng CMND/SĐT.", "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + e.getMessage(), "Lỗi nghiệp vụ", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống khi thêm: " + e.getMessage(), "Lỗi nặng", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuLyLamMoiFormInput() {
        txtMaHK.setText("");
        txtHoTen.setText("");
        dateNgaySinh.setDate(null);
        txtCMND.setText("");
        txtSoDT.setText("");

        txtHoTen.requestFocus();

        btnThem.setEnabled(true);
        cmbUuDai.setSelectedIndex(0);
    }

    private void taiDuLieuHanhKhachLenBang() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        modelHK.setRowCount(0);

        try {
            List<HanhKhach> danhSachHK = controlHK.layDanhSachHanhKhachHoatDong();

            for (HanhKhach hk : danhSachHK) {
                String maHK = hk.getMaKH();
                String hoTen = hk.getHoTen();
                String ngaySinhStr = hk.getNgaySinh().format(dtf);
                String cmnd = hk.getCmndCccd();
                String sdt = hk.getSoDT();

                String maUuDaiDB = hk.getMaUuDai() != null ? hk.getMaUuDai().trim() : "UD-01";
                UuDai ud = mapMaToUuDai.get(maUuDaiDB);
                String hienThiUuDai = "Mặc định";
                if (ud != null) {
                    String tenLoai = mapIdToTenLoai.getOrDefault(ud.getIDloaiUuDai(), "Không xác định");
                    String dk = ud.getDieuKienApDung() != null ? ud.getDieuKienApDung() : "";
                    hienThiUuDai = tenLoai + " - " + dk + " (Giảm " + ud.getMucGiamGia() + "%)";
                }

                Object[] rowData = {maHK, hoTen, ngaySinhStr, cmnd, sdt, hienThiUuDai};
                modelHK.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi CSDL khi tải danh sách hành khách: " + e.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	LookAndFeelManager.setNimbusLookAndFeel();
        SwingUtilities.invokeLater(() -> new QuanLyHanhKhach().setVisible(true));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tblHanhKhach.getSelectedRow();
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
        txtMaHK.setText(modelHK.getValueAt(row, 0).toString());
        txtHoTen.setText(modelHK.getValueAt(row, 1).toString());

        String ngaySinhStr = modelHK.getValueAt(row, 2).toString();
        try {
            Date ngaySinh = new SimpleDateFormat("dd/MM/yyyy").parse(ngaySinhStr);
            dateNgaySinh.setDate(ngaySinh);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày sinh: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        txtCMND.setText(modelHK.getValueAt(row, 3).toString());
        txtSoDT.setText(modelHK.getValueAt(row, 4).toString());

        // Set combo dựa trên maUuDai từ DB
        String maHK = txtMaHK.getText().trim();
        try {
            HanhKhach hk = controlHK.layHanhKhachTheoMa(maHK);
            String maUuDai = hk.getMaUuDai() != null ? hk.getMaUuDai().trim() : "UD-01";
            UuDai ud = mapMaToUuDai.get(maUuDai);
            if (ud != null) {
                String tenLoai = mapIdToTenLoai.getOrDefault(ud.getIDloaiUuDai(), "Không xác định");
                String dk = ud.getDieuKienApDung() != null ? ud.getDieuKienApDung() : "";
                String hienThi = tenLoai + " - " + dk + " (Giảm " + ud.getMucGiamGia() + "%)";
                cmbUuDai.setSelectedItem(hienThi);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải ưu đãi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}