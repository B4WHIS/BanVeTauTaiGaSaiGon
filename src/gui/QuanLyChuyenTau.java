package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;  // THÊM: Import JDateChooser từ thư viện toedter (cần add JAR: jcalendar-1.4.jar)

import control.QuanLyChuyenTauController;
import dao.ChuyenTauDAO;
import dao.LichTrinhDAO;
import dao.TauDAO;
import entity.ChuyenTau;
import entity.NhanVien;

public class QuanLyChuyenTau extends GiaoDienChinh implements ActionListener{
    
    private JTable tblChuyenTau;
    private DefaultTableModel modelCT;
    private JTextField txtMaChuyenTau, txtGiaChuyen;
    private JComboBox<String> cbTenTau, cbTenLichTrinh, cbTrangThai; 
    private JButton btnThem, btnSua, btnXoa, btnReset, btnExport, btnTroVe;
    private JButton btnLuu; 
    private JDateChooser dcThoiGianKhoiHanh, dcThoiGianDen;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
    // Controller
    private QuanLyChuyenTauController controller;
    private ChuyenTauDAO dao; 
    private LichTrinhDAO lichTrinhDAO;
    private TauDAO tauDAO;
    private NhanVien nhanVienHienTai;
    private Timer autoUpdateTimer;

    public QuanLyChuyenTau() {
        super();  
        dao = new ChuyenTauDAO();
        lichTrinhDAO= new LichTrinhDAO();
        tauDAO = new TauDAO();
        controller = new QuanLyChuyenTauController(this);
        setTitle("Quản lý chuyến tàu");
        setSize(1500, 1000);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

//        setJMenuBar(taoMenuBar());

        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));

        // TITLE
        JLabel lblTitle = new JLabel("QUẢN LÝ CHUYẾN TÀU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(103,192,144));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // LEFT PANEL 
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(450, 0));
        pnlLeft.setBackground(new Color(245, 247, 250));

        JLabel lblLeftTitle = new JLabel("THÔNG TIN CHUYẾN TÀU", SwingConstants.CENTER);
        lblLeftTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLeftTitle.setForeground(new Color(103,192,144));
        lblLeftTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        //  FORM 
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setBackground(Color.WHITE);

        Font lblFont = new Font("Segoe UI", Font.BOLD, 19);
        Font txtFont = new Font("Segoe UI", Font.PLAIN, 20);

        JLabel lblMaChuyenTau = new JLabel("Mã chuyến:");
        JLabel lblTenTau = new JLabel("Tên tàu:");
        JLabel lblTenLichTrinh = new JLabel("Lịch trình:");
        JLabel lblThoiGianKhoiHanh = new JLabel("Thời gian KH:");
        JLabel lblThoiGianDen = new JLabel("Thời gian Đến:");
        JLabel lblGiaChuyen = new JLabel("Giá chuyến:");
        JLabel lblTrangThai = new JLabel("Trạng thái:");

        JLabel[] labels = {lblMaChuyenTau, lblTenTau, lblTenLichTrinh, lblThoiGianKhoiHanh, lblThoiGianDen, lblGiaChuyen, lblTrangThai};
        for (JLabel lbl : labels) {
            lbl.setFont(lblFont);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        txtMaChuyenTau = new JTextField(); 
        txtMaChuyenTau.setEditable(false);
        cbTenTau = new JComboBox<>();  
        cbTenLichTrinh = new JComboBox<>();  
        
        
        dcThoiGianKhoiHanh = new JDateChooser();
        dcThoiGianKhoiHanh.setDateFormatString("yyyy-MM-dd HH:mm");
        dcThoiGianKhoiHanh.setFont(txtFont);
        dcThoiGianKhoiHanh.setPreferredSize(new Dimension(200, 30));
        
        dcThoiGianDen = new JDateChooser();
        dcThoiGianDen.setDateFormatString("yyyy-MM-dd HH:mm");
        dcThoiGianDen.setFont(txtFont);
        dcThoiGianDen.setPreferredSize(new Dimension(200, 30));
        
        txtGiaChuyen = new JTextField();
        cbTrangThai = new JComboBox<>(new String[]{
                "Chưa khởi hành", "Đang khởi hành", "Đã hoàn thành", "Đã hủy"
        });
        cbTrangThai.setSelectedIndex(0); 
       
        Component[] inputFields = {txtMaChuyenTau, cbTenTau, cbTenLichTrinh, dcThoiGianKhoiHanh, dcThoiGianDen, txtGiaChuyen, cbTrangThai};
        for (Component comp : inputFields) {
            if (comp instanceof JTextField) {
                JTextField txt = (JTextField) comp;
                txt.setFont(txtFont);
                txt.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(150, 150, 150)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
            } else if (comp instanceof JComboBox) {
                JComboBox<?> cb = (JComboBox<?>) comp;
                cb.setFont(txtFont);
                cb.setPreferredSize(new Dimension(200, 30));
            } else if (comp instanceof JDateChooser) {
               
            }
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            pnlForm.add(labels[i], gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            pnlForm.add(inputFields[i], gbc);
        }

        // ===== FORM BUTTONS =====
       
        btnLuu = new JButton("Lưu");
        btnLuu.setBackground(new Color(46, 204, 113));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLuu.setPreferredSize(new Dimension(100, 35));
        btnLuu.setEnabled(false);  
   

        //  MAIN BUTTONS 
        btnThem = taoButton("Thêm", new Color(46, 204, 113), "/img/plus.png");
        btnSua = taoButton("Sửa", new Color(241, 196, 15), "/img/maintenance.png");
        btnXoa = taoButton("Xóa", new Color(231, 76, 60), "/img/bin.png");
        btnReset = taoButton("Làm mới", new Color(52, 152, 219), "/img/reset.png");
        btnExport = taoButton("Xuất Excel", new Color(155, 89, 182), "/img/export.png");

        JPanel pnlButtons = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlButtons.setBackground(new Color(245, 247, 250));
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnReset);
        pnlButtons.add(btnLuu);
        pnlButtons.add(btnExport);
        pnlButtons.add(new JLabel(" "));  

        pnlLeft.add(lblLeftTitle, BorderLayout.NORTH);
        pnlLeft.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlButtons, BorderLayout.SOUTH);

        //RIGHT PANEL
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setBackground(new Color(245, 247, 250));

        // 
        String[] colHeader = {"Mã chuyến", "Thời gian KH", "Thời gian Đến", "Tên tàu", "Tên lịch trình", "Giá chuyến", "Trạng thái"};
        modelCT = new DefaultTableModel(colHeader, 0);
        tblChuyenTau = new JTable(modelCT);
        tblChuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblChuyenTau.setRowHeight(28);
        tblChuyenTau.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblChuyenTau.setSelectionBackground(new Color(173, 216, 230));

        JScrollPane scroll = new JScrollPane(tblChuyenTau);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnlRight.add(scroll, BorderLayout.CENTER);

        // FOOTER 
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(new Color(103,192,144)); 
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "/img/loginicon.png");
        btnTroVe.setPreferredSize(new Dimension(130, 45));
        pnlFooter.add(btnTroVe, BorderLayout.WEST);
        btnTroVe.addActionListener(this);
        // ADD ALL 
        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(pnlRight, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);
        add(pnlMain);

       
        attachListeners();
        
      
        loadCombosFromDB();
        loadDataFromDB();
        
       
        autoUpdateTimer = new Timer(60000, e -> {
            dao.updateTrangThaiTuThoiGian();  
            loadDataFromDB();  
            System.out.println("Auto-update trạng thái lúc: " + LocalDateTime.now());
        });
        autoUpdateTimer.start();  
    }
    public JButton taoButton2(String text, Color bg, String iconPath) {
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
   
    private void loadCombosFromDB() {
        
        cbTenTau.removeAllItems();
        List<String> tenTauList = dao.getAllTenTau();
        for (String tenTau : tenTauList) {
            cbTenTau.addItem(tenTau);
        }
        if (!tenTauList.isEmpty()) cbTenTau.setSelectedIndex(0);

       
        cbTenLichTrinh.removeAllItems();
        List<String> tenLichTrinhList = dao.getAllTenLichTrinh();
        for (String tenLichTrinh : tenLichTrinhList) {
            cbTenLichTrinh.addItem(tenLichTrinh);
        }
        if (!tenLichTrinhList.isEmpty()) cbTenLichTrinh.setSelectedIndex(0);
    }

    
    private void loadDataFromDB() {
        modelCT.setRowCount(0);
        try {
            List<ChuyenTau> list = dao.getAllChuyenTau();
            for (ChuyenTau ct : list) {
                
                String tenTau = tauDAO.getTenTauByMaTau(ct.getMaTau());
                String tenLichTrinh = lichTrinhDAO.getTenLichTrinhByMaLichTrinh(ct.getMaLichTrinh());
                modelCT.addRow(new Object[]{
                    ct.getMaChuyenTau(),
                    ct.getThoiGianKhoiHanh().format(dateTimeFormatter),
                    ct.getThoiGianDen().format(dateTimeFormatter),
                    tenTau != null ? tenTau : ct.getMaTau(), 
                    tenLichTrinh != null ? tenLichTrinh : ct.getMaLichTrinh(),
                    ct.getGiaChuyen(),
                    ct.getTrangThai()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi load dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

  
    private void attachListeners() {
        btnThem.addActionListener(controller);
        btnSua.addActionListener(controller);
        btnXoa.addActionListener(controller);
        btnReset.addActionListener(controller);
        btnExport.addActionListener(controller);
        btnTroVe.addActionListener(controller);
        btnLuu.addActionListener(controller); 

       
        tblChuyenTau.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                controller.handleTableSelection();
            }
        });
    }

   
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnReset() { return btnReset; }
    public JButton getBtnExport() { return btnExport; }
    public JButton getBtnTroVe() { return btnTroVe; }
    public JButton getBtnLuu() { return btnLuu; }
    public JTable getTblChuyenTau() { return tblChuyenTau; }
    public DefaultTableModel getModelCT() { return modelCT; }
    public JTextField getTxtMaChuyenTau() { return txtMaChuyenTau; }
    public JComboBox<String> getCbTenTau() { return cbTenTau; }
    public JComboBox<String> getCbTenLichTrinh() { return cbTenLichTrinh; }
    
   
    public String getTxtThoiGianKhoiHanh() {
        Date date = dcThoiGianKhoiHanh.getDate();
        if (date == null) return "yyyy-MM-dd HH:mm";
        return dateFormat.format(date);
    }
    
    public String getTxtThoiGianDen() {
        Date date = dcThoiGianDen.getDate();
        if (date == null) return "yyyy-MM-dd HH:mm";
        return dateFormat.format(date);
    }
    
    public JTextField getTxtGiaChuyen() { return txtGiaChuyen; }
    public JComboBox<String> getCbTrangThai() { return cbTrangThai; }

    
    public void loadFormData(ChuyenTau ct) {
        if (ct != null) {
            txtMaChuyenTau.setText(ct.getMaChuyenTau());
            
            String tenTau = tauDAO.getTenTauByMaTau(ct.getMaTau());
            if (tenTau != null) cbTenTau.setSelectedItem(tenTau);
            String tenLichTrinh = lichTrinhDAO.getTenLichTrinhByMaLichTrinh(ct.getMaLichTrinh());
            if (tenLichTrinh != null) cbTenLichTrinh.setSelectedItem(tenLichTrinh);
            
           
            try {
                Date khDate = Date.from(ct.getThoiGianKhoiHanh().atZone(java.time.ZoneId.systemDefault()).toInstant());
                dcThoiGianKhoiHanh.setDate(khDate);
                Date denDate = Date.from(ct.getThoiGianDen().atZone(java.time.ZoneId.systemDefault()).toInstant());
                dcThoiGianDen.setDate(denDate);
            } catch (Exception ex) {
                System.out.println("Lỗi set date cho JDateChooser: " + ex.getMessage());
            }
            
            txtGiaChuyen.setText(ct.getGiaChuyen().toString());
            cbTrangThai.setSelectedItem(ct.getTrangThai());
           
        }
    }

    
    public void updateForm(ChuyenTau ct) {
        loadFormData(ct); 
        
    }

    
    public void resetForm() {
        txtMaChuyenTau.setText("");
        cbTenTau.setSelectedIndex(0);
        cbTenLichTrinh.setSelectedIndex(0);
        dcThoiGianKhoiHanh.setDate(null);  
        dcThoiGianDen.setDate(null);
        txtGiaChuyen.setText("");
        cbTrangThai.setSelectedIndex(0);
        btnLuu.setEnabled(false);
        tblChuyenTau.clearSelection();
        
        enableFormFields(false);
    }

    
    public void enableFormFields(boolean enable) {
        cbTenTau.setEnabled(enable);
        cbTenLichTrinh.setEnabled(enable);
        dcThoiGianKhoiHanh.setEnabled(enable);  
        dcThoiGianDen.setEnabled(enable);
        txtGiaChuyen.setEnabled(enable);
        cbTrangThai.setEnabled(enable);
    }

    
    public void refreshData() {
        dao.updateTrangThaiTuThoiGian();  
        loadDataFromDB();  
        resetForm();
    }

   
    public String getSelectedMaChuyenTau() {
        int selectedRow = tblChuyenTau.getSelectedRow();
        if (selectedRow >= 0) {
            return (String) modelCT.getValueAt(selectedRow, 0);  
        }
        return null;
    }

    
    public void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }

   
    @Override
    public void dispose() {
        if (autoUpdateTimer != null) {
            autoUpdateTimer.stop();
        }
        super.dispose();
    }

    
  

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object src = e.getSource();
        if (src == btnTroVe) { 
            this.dispose();
            new MHC_NhanVienQuanLy(nhanVienHienTai).setVisible(true);
        }
	}
}