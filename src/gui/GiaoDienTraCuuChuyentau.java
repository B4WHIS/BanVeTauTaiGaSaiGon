package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import control.TraCuuChuyenTauControl;
import dao.ToaTauDAO;
import dao.VeDAO;
import entity.ChuyenTau;
import entity.NhanVien;
import entity.ToaTau;

public class GiaoDienTraCuuChuyentau extends JFrame implements ActionListener {
    
    private TraCuuChuyenTauControl control;
    
    private JPanel pnlChinh;
    private JPanel pnlTraCuu;
    private JLabel lblGaDen;
    private JLabel lblGaDi;
    private JLabel lblNgayDi;
    JComboBox<String> cbGaDi;
    JComboBox<String> cbGaDen;
    JDateChooser dateChooser;
    private JPanel pnlThongTinTim;
    private JLabel lblTieuDe;
    private JPanel pnlTitle;
    private JPanel pnlTrungTam;
    private JScrollPane scpTrungTam;
    private JPanel pnlNutChucNang;
    private JButton btnTroVe;
    private JPanel pnlNutBam;
    private JButton btnLamMoi;
    private JButton btnTim;
    
    private NhanVien nhanVienHienTai; 
    private VeDAO veDAO = new VeDAO();
    private ToaTauDAO toaTaudao = new ToaTauDAO();
    
    private final Color MAU_CHU_DAO = new Color(74, 140, 103);
    private final Color MAU_TIEU_DE_PHU = new Color(229, 115, 115);
    private final Color MAU_NUT_QUAY_LAI = new Color(41, 128, 185);
    private final Color MAU_NUT_LAM_MOI = new Color(150, 150, 150);
    
    private final DateTimeFormatter dinhDangNgayGio = DateTimeFormatter.ofPattern("dd/MM HH:mm");

    public GiaoDienTraCuuChuyentau(NhanVien nhanVien) {
        this.nhanVienHienTai = nhanVien != null ? nhanVien : new NhanVien("NV-001");
        
        this.control = new TraCuuChuyenTauControl(this);
        
        initializeUI();
        loadGaDataToComboBox();
    }
    
    private void initializeUI() {
        setTitle("Tra cứu chuyến tàu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pnlChinh = new JPanel(new BorderLayout());

        pnlTitle = new JPanel(new BorderLayout());
        pnlTitle.setBorder(BorderFactory.createEtchedBorder());
        lblTieuDe = new JLabel("TRA CỨU CHUYẾN TÀU");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 55));
        lblTieuDe.setForeground(MAU_CHU_DAO);
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        pnlTitle.add(lblTieuDe, BorderLayout.CENTER);

        pnlThongTinTim = new JPanel(new GridBagLayout());
        pnlTraCuu = new JPanel(new BorderLayout());
        Font fontTieuDe = new Font("Segoe UI", Font.BOLD, 25);
        TitledBorder titleBorder = BorderFactory.createTitledBorder("THÔNG TIN TRA CỨU");
        titleBorder.setTitleFont(fontTieuDe);
        titleBorder.setTitleColor(new Color(93, 156, 236));
        pnlTraCuu.setBorder(titleBorder);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        lblGaDi = new JLabel("Ga đi:");
        lblGaDi.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        pnlThongTinTim.add(lblGaDi, gbc);
        cbGaDi = new JComboBox<>();
        cbGaDi.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlThongTinTim.add(cbGaDi, gbc);
        
        // Ga đến [11]
        lblGaDen = new JLabel("Ga đến:");
        lblGaDen.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        pnlThongTinTim.add(lblGaDen, gbc);
        cbGaDen = new JComboBox<>();
        cbGaDen.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlThongTinTim.add(cbGaDen, gbc);
        
        // Ngày đi [12]
        lblNgayDi = new JLabel("Ngày đi:");
        lblNgayDi.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        pnlThongTinTim.add(lblNgayDi, gbc);
        dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        dateChooser.setPreferredSize(new Dimension(0, 35));
        dateChooser.setMinSelectableDate(new Date()); 
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlThongTinTim.add(dateChooser, gbc);

        // Buttons [13]
        pnlNutBam = new JPanel(new GridLayout(1, 2, 10, 0));
        btnTim = new JButton("Tìm");
        btnLamMoi = new JButton("Làm mới");
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnLamMoi.setBackground(MAU_NUT_LAM_MOI);
        btnLamMoi.setForeground(Color.white);
        btnTim.setBackground(new Color(93, 156, 236));
        btnTim.setForeground(Color.white);
        
        ImageIcon timIC = GiaoDienChinh.chinhKichThuoc("/img/traCuu.png", 25, 25);
        btnTim.setIcon(timIC);
        ImageIcon lamMoiIC = GiaoDienChinh.chinhKichThuoc("/img/undo.png", 25, 25);
        btnLamMoi.setIcon(lamMoiIC);
        
        pnlNutBam.add(btnLamMoi);
        pnlNutBam.add(btnTim);
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        pnlThongTinTim.add(pnlNutBam, gbc);
        
        pnlTraCuu.setPreferredSize(new Dimension(420, 0));
        pnlTraCuu.add(pnlThongTinTim, BorderLayout.NORTH);

        // --- Results Panel (pnlTrungTam) --- [14]
        pnlTrungTam = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        TitledBorder titleBorderTrungTam = BorderFactory.createTitledBorder("KẾT QUẢ TRA CỨU");
        titleBorderTrungTam.setTitleFont(fontTieuDe);
        titleBorderTrungTam.setTitleColor(MAU_TIEU_DE_PHU);
        pnlTrungTam.setBorder(titleBorderTrungTam);
        scpTrungTam = new JScrollPane(pnlTrungTam);

        // --- Footer buttons (pnlNutChucNang) --- [15]
        pnlNutChucNang = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnTroVe = new JButton("Trở về");
        btnTroVe.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnTroVe.setBackground(MAU_NUT_QUAY_LAI);
        btnTroVe.setForeground(Color.white);
        btnTroVe.setPreferredSize(new Dimension(150, 40));
        pnlNutChucNang.add(btnTroVe);
        
        // --- Add components to pnlChinh --- [16]
        pnlChinh.add(pnlNutChucNang, BorderLayout.SOUTH);
        pnlChinh.add(scpTrungTam, BorderLayout.CENTER);
        pnlChinh.add(pnlTitle, BorderLayout.NORTH);
        pnlChinh.add(pnlTraCuu, BorderLayout.WEST);
        
        btnTim.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnTroVe.addActionListener(this);
        
        add(pnlChinh);
    }

    // Phương thức tải dữ liệu Ga vào ComboBox [17]
    private void loadGaDataToComboBox() {
        // GỌI QUA CONTROL
        List<String> tenGaList = control.loadGaData();

        cbGaDi.removeAllItems();
        cbGaDen.removeAllItems();

        for (String ten : tenGaList) {
            cbGaDi.addItem(ten);
            cbGaDen.addItem(ten);
        }
        
        if (!tenGaList.isEmpty()) { 
            cbGaDi.setSelectedIndex(0);
            if (tenGaList.size() > 1) cbGaDen.setSelectedIndex(1);
        }
    }
    
    // Phương thức tạo Panel kết quả cho mỗi chuyến tàu
    private JPanel taoVePanel(ChuyenTau ct) throws SQLException {
        JPanel pnlVe = new JPanel(new BorderLayout());
        pnlVe.setPreferredSize(new Dimension(320, 220)); 
        pnlVe.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        JPanel pnlNoiDung = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        
        // Lấy thông tin chi tiết qua Control (đã bao gồm tính toán số chỗ)
        Map<String, Object> thongKe = control.getThongTinChuyenTau(ct);
        String tenTau = (String) thongKe.get("tenTau");
        int slChoDaDat = (int) thongKe.get("slChoDaDat");
        int slChoTrong = (int) thongKe.get("slChoTrong");

        // Tên chuyến tàu [19]
        JLabel lblTenChuyen = new JLabel(tenTau == null ? ct.getMaChuyenTau() : tenTau);
        lblTenChuyen.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTenChuyen.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlNoiDung.add(lblTenChuyen, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;

        // Giờ đi [20]
        JLabel lblTGDi = new JLabel("Giờ đi:");
        lblTGDi.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        pnlNoiDung.add(lblTGDi, gbc);
        JLabel lblGioDi = new JLabel(ct.getThoiGianKhoiHanh().format(dinhDangNgayGio));
        lblGioDi.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblGioDi.setForeground(MAU_TIEU_DE_PHU);
        gbc.gridx = 1; gbc.gridy = 1;
        pnlNoiDung.add(lblGioDi, gbc);

        // Giờ đến [21]
        JLabel lblTGDen = new JLabel("Giờ đến:");
        lblTGDen.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 2;
        pnlNoiDung.add(lblTGDen, gbc);
        JLabel lblGioDen = new JLabel(ct.getThoiGianDen().format(dinhDangNgayGio));
        lblGioDen.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblGioDen.setForeground(MAU_TIEU_DE_PHU);
        gbc.gridx = 1; gbc.gridy = 2;
        pnlNoiDung.add(lblGioDen, gbc);

        // Số chỗ đã đặt (đã chiếm dụng) [22]
        JLabel lblDaDat = new JLabel("Số chỗ đã đặt:");
        lblDaDat.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 3;
        pnlNoiDung.add(lblDaDat, gbc);
        JLabel lblSoDaDat = new JLabel(String.valueOf(slChoDaDat));
        lblSoDaDat.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSoDaDat.setForeground(MAU_TIEU_DE_PHU);
        gbc.gridx = 1; gbc.gridy = 3;
        pnlNoiDung.add(lblSoDaDat, gbc);

        // Số chỗ trống [23]
        JLabel lblTrong = new JLabel("Số chỗ trống:");
        lblTrong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 4;
        pnlNoiDung.add(lblTrong, gbc);
        JLabel lblSoTrong = new JLabel(String.valueOf(slChoTrong));
        lblSoTrong.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSoTrong.setForeground(MAU_CHU_DAO);
        gbc.gridx = 1; gbc.gridy = 4;
        pnlNoiDung.add(lblSoTrong, gbc);

        // Nút Đặt vé (thực hiện BÁN VÉ trực tiếp) [23]
        JButton btnDatVe = new JButton("Đặt vé");
        btnDatVe.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDatVe.setBackground(MAU_CHU_DAO);
        btnDatVe.setForeground(Color.white);
        gbc.gridx = 1; gbc.gridy = 5; gbc.anchor = GridBagConstraints.EAST;
        pnlNoiDung.add(btnDatVe, gbc);
        
        pnlVe.add(pnlNoiDung, BorderLayout.CENTER);
        
        // Listener cho nút Đặt vé
        btnDatVe.addActionListener(e -> {
            if (ct.getMaChuyenTau() == null || ct.getMaChuyenTau().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin chuyến tàu.", "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Chuyển sang màn hình chọn chỗ ngồi (GiaoDienChonCho)
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                try {
                    new GiaoDienChonCho(ct, nhanVienHienTai).setVisible(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi khi mở màn hình chọn chỗ: " + ex.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
        });
        return pnlVe;
    }
    
    private void capNhatKetQua(List<ChuyenTau> danhSach) throws SQLException {
        pnlTrungTam.removeAll();
        if (danhSach == null || danhSach.isEmpty()) {
            JPanel thongBao = new JPanel(new BorderLayout());
            thongBao.setPreferredSize(new Dimension(600, 120));
            JLabel lbl = new JLabel("Không tìm thấy chuyến tàu nào phù hợp.", SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
            thongBao.add(lbl, BorderLayout.CENTER);
            pnlTrungTam.add(thongBao);
        } else {
            for (ChuyenTau ct : danhSach) {
                JPanel ve = taoVePanel(ct); 
                pnlTrungTam.add(ve);
            }
        }
        pnlTrungTam.revalidate();
        pnlTrungTam.repaint();
    }

    private void xuLyTimChuyenTau() {
        String tenGaDi = (String) cbGaDi.getSelectedItem();
        String tenGaDen = (String) cbGaDen.getSelectedItem();
        Date date = dateChooser.getDate();
        
        if (tenGaDi == null || tenGaDen == null || date == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ga đi, ga đến và ngày đi.", "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<ChuyenTau> ketQua = control.timChuyenTau(tenGaDi, tenGaDen, date);
            
            capNhatKetQua(ketQua);
            
            if (ketQua == null || ketQua.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến tàu nào phù hợp.", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
             JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi Dữ liệu",
                    JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn cơ sở dữ liệu.", "Lỗi CSDL",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private Map<String, Integer> laySoLuongGheCuaChuyen(ChuyenTau ct) throws SQLException {
        // ... Khai báo VeDAO và ToaTauDAO
        String maCT = ct.getMaChuyenTau();

        List<String> danhSachChoDaDat = veDAO.getDanhSachChoDaDat(maCT); // Sử dụng VeDAO [2]
        int slChoDaDat = danhSachChoDaDat.size(); 

        // 2. Tính Tổng số chỗ
        int tongSoCho = 0;
        String maTau = ct.getMaTau();
        List<ToaTau> danhSachToa = toaTaudao.getAllToaTauByMaTau(maTau); // Sử dụng ToaTauDAO [5, 6]
        for (ToaTau toa : danhSachToa) {
            tongSoCho += toa.getSoLuongCho();
        }

        // 3. Tính số chỗ trống
        int slChoTrong = Math.max(0, tongSoCho - slChoDaDat);
        
        Map<String, Integer> dem = new HashMap<>();
        dem.put("DAT", slChoDaDat);
        dem.put("TRONG", slChoTrong);
        return dem;
    }
    
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnTim) {
            xuLyTimChuyenTau();
        } else if (src == btnLamMoi) { 
            cbGaDi.setSelectedIndex(cbGaDi.getItemCount() > 0 ? 0 : -1);
            // Đảm bảo không lỗi nếu chỉ có 1 ga
            cbGaDen.setSelectedIndex(cbGaDen.getItemCount() > 1 ? 1 : (cbGaDen.getItemCount() > 0 ? 0 : -1)); 
            dateChooser.setDate(null);
            pnlTrungTam.removeAll();
            pnlTrungTam.revalidate();
            pnlTrungTam.repaint();
        } else if (src == btnTroVe) { 
            this.dispose();
            try {
				new MHC_NhanVienBanVe(nhanVienHienTai).setVisible(true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }
    
    public Component getCbGaDi() { return cbGaDi; }
    public Component getCbGaDen() { return cbGaDen; }
    public Date getDateChooserValue() { return dateChooser.getDate(); }

    public static void main(String[] args) {
        LookAndFeelManager.setNimbusLookAndFeel();
        NhanVien nv = new NhanVien("NV-001");
        SwingUtilities.invokeLater(() -> new GiaoDienTraCuuChuyentau(nv).setVisible(true));
    }
}