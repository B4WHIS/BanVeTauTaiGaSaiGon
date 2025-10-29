package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import dao.ChiTietPhieuDatChoDAO;
import dao.ChuyenTauDAO;
import dao.GaDAO;
import dao.TauDAO;
import dao.ToaTauDAO;
import entity.ChuyenTau;
import entity.Ga;
import entity.NhanVien;
import entity.ToaTau;


public class TraCuuChuyenTauGUI extends JFrame implements ActionListener {
    private JPanel pnlChinh;
    private JPanel pnlTraCuu;
    private JLabel lblGaDen;
    private JLabel lblGaDi;
    private JLabel lblNgayDi;
    private JComboBox<String> cbGaDi;
    private JComboBox<String> cbGaDen;
    private JDateChooser dateChooser;
    private JPanel pnlThongTinTim;
    private JLabel lblTieuDe;
    private JPanel pnlTitle;

    private JPanel pnlTrungTam; // nơi chứa các panel vé
    private JScrollPane scpTrungTam;

    private JPanel pnlNutChucNang;
    private JButton btnTroVe;
    private JPanel pnlNutBam;
    private JButton btnLamMoi;
    private JButton btnTim;

    private ChuyenTauDAO chuyenTauDAO = new ChuyenTauDAO();
    private GaDAO gaDAO = new GaDAO();
    private ToaTauDAO toaTauDAO = new ToaTauDAO();
    private ChiTietPhieuDatChoDAO ctpdcDAO = new ChiTietPhieuDatChoDAO();
    private TauDAO tauDAO = new TauDAO();

    private final DateTimeFormatter dinhDangNgayGio = DateTimeFormatter.ofPattern("dd/MM HH:mm");

    public TraCuuChuyenTauGUI() {
        setTitle("Tra cứu chuyến tàu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Khởi tạo layout chính
        pnlChinh = new JPanel(new BorderLayout());

        // Tiêu đề
        pnlTitle = new JPanel(new BorderLayout());
        pnlTitle.setBorder(BorderFactory.createEtchedBorder());
        lblTieuDe = new JLabel("TRA CỨU CHUYẾN TÀU");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 55));
        lblTieuDe.setForeground(new Color(74, 140, 103));
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        pnlTitle.add(lblTieuDe, BorderLayout.CENTER);

        // Panel thông tin tra cứu
        pnlThongTinTim = new JPanel(new GridBagLayout());
        pnlTraCuu = new JPanel(new BorderLayout());
        Font fontTieuDe = new Font("Segoe UI", Font.BOLD, 25);
        TitledBorder titleBorder = BorderFactory.createTitledBorder("THÔNG TIN TRA CỨU");
        titleBorder.setTitleFont(fontTieuDe);
        titleBorder.setTitleColor(new Color(93, 156, 236));
        pnlTraCuu.setBorder(titleBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Ga đi
        lblGaDi = new JLabel("Ga đi:");
        lblGaDi.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        pnlThongTinTim.add(lblGaDi, gbc);

        cbGaDi = new JComboBox<>();
        cbGaDi.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlThongTinTim.add(cbGaDi, gbc);

        // Ga đến
        lblGaDen = new JLabel("Ga đến:");
        lblGaDen.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        pnlThongTinTim.add(lblGaDen, gbc);

        cbGaDen = new JComboBox<>();
        cbGaDen.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlThongTinTim.add(cbGaDen, gbc);

        // Ngày đi
        lblNgayDi = new JLabel("Ngày đi:");
        lblNgayDi.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        pnlThongTinTim.add(lblNgayDi, gbc);

        dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        dateChooser.setPreferredSize(new Dimension(0, 35));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlThongTinTim.add(dateChooser, gbc);

        // Nút bấm (Tìm, Làm mới)
        pnlNutBam = new JPanel(new GridLayout(1, 2, 10, 0));
        btnTim = new JButton("Tìm");
        btnLamMoi = new JButton("Làm mới");
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnLamMoi.setBackground(new Color(229, 115, 115));
        btnLamMoi.setForeground(Color.white);
        btnTim.setBackground(new Color(93, 156, 236));
        btnTim.setForeground(Color.white);

        // icon 
        ImageIcon timIC = GiaoDienChinh.chinhKichThuoc("/img/traCuu.png", 25, 25);
        btnTim.setIcon(timIC);
        ImageIcon lamMoiIC = GiaoDienChinh.chinhKichThuoc("/img/undo.png", 25, 25);
        btnLamMoi.setIcon(lamMoiIC);
        

        pnlNutBam.add(btnLamMoi);
        pnlNutBam.add(btnTim);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        pnlThongTinTim.add(pnlNutBam, gbc);

        pnlTraCuu.setPreferredSize(new Dimension(420, 0));
        pnlTraCuu.add(pnlThongTinTim, BorderLayout.NORTH);

        // Panel trung tâm
        pnlTrungTam = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        TitledBorder titleBorderTrungTam = BorderFactory.createTitledBorder("KẾT QUẢ TRA CỨU");
        titleBorderTrungTam.setTitleFont(fontTieuDe);
        titleBorderTrungTam.setTitleColor(new Color(229, 115, 115));
        pnlTrungTam.setBorder(titleBorderTrungTam);

        scpTrungTam = new JScrollPane(pnlTrungTam);

        // Nút chức năng bên dưới
        pnlNutChucNang = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnTroVe = new JButton("Trở về");
        btnTroVe.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnTroVe.setBackground(new Color(229, 115, 115));
        btnTroVe.setForeground(Color.white);
        pnlNutChucNang.add(btnTroVe);

        pnlChinh.add(pnlNutChucNang, BorderLayout.SOUTH);
        pnlChinh.add(scpTrungTam, BorderLayout.CENTER);
        pnlChinh.add(pnlTitle, BorderLayout.NORTH);
        pnlChinh.add(pnlTraCuu, BorderLayout.WEST);

        //listener
        btnTim.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnTroVe.addActionListener(this);

        // load dữ liệu ga
        loadGaDataToComboBox();

        add(pnlChinh);
    }
    
    
    private JPanel taoVePanel(ChuyenTau ct) {
        JPanel pnlVe = new JPanel(new BorderLayout());
        pnlVe.setPreferredSize(new Dimension(320, 220)); 
        pnlVe.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel pnlNoiDung = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);

        // Tên tàu 
        JLabel lblTenChuyen = new JLabel("TÀU [MÃ]");
        lblTenChuyen.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTenChuyen.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlNoiDung.add(lblTenChuyen, gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        // Giờ đi
        JLabel lblTGDi = new JLabel("Giờ đi:");
        lblTGDi.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        pnlNoiDung.add(lblTGDi, gbc);

        JLabel lblGioDi = new JLabel("-");
        lblGioDi.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblGioDi.setForeground(new Color(229, 115, 115));
        gbc.gridx = 1;
        gbc.gridy = 1;
        pnlNoiDung.add(lblGioDi, gbc);

        // Giờ đến
        JLabel lblTGDen = new JLabel("Giờ đến:");
        lblTGDen.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        pnlNoiDung.add(lblTGDen, gbc);

        JLabel lblGioDen = new JLabel("-");
        lblGioDen.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblGioDen.setForeground(new Color(229, 115, 115));
        gbc.gridx = 1;
        gbc.gridy = 2;
        pnlNoiDung.add(lblGioDen, gbc);

        // Số chỗ đã đặt
        JLabel lblDaDat = new JLabel("Số chỗ đã đặt:");
        lblDaDat.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        pnlNoiDung.add(lblDaDat, gbc);

        JLabel lblSoDaDat = new JLabel("-");
        lblSoDaDat.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSoDaDat.setForeground(new Color(229, 115, 115));
        gbc.gridx = 1;
        gbc.gridy = 3;
        pnlNoiDung.add(lblSoDaDat, gbc);

        // Số chỗ trống
        JLabel lblTrong = new JLabel("Số chỗ trống:");
        lblTrong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        pnlNoiDung.add(lblTrong, gbc);

        JLabel lblSoTrong = new JLabel("-");
        lblSoTrong.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSoTrong.setForeground(new Color(229, 115, 115));
        gbc.gridx = 1;
        gbc.gridy = 4;
        pnlNoiDung.add(lblSoTrong, gbc);

        // Nút đặt vé
        JButton btnDatVe = new JButton("Đặt vé");
        btnDatVe.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDatVe.setBackground(new Color(74, 140, 103));
        btnDatVe.setForeground(Color.white);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        pnlNoiDung.add(btnDatVe, gbc);

        
        pnlVe.add(pnlNoiDung, BorderLayout.CENTER);

        if (ct != null) {
            String tenTau = tauDAO.getTenTauByMaTau(ct.getMaTau());
            lblTenChuyen.setText(tenTau == null ? ct.getMaChuyenTau() : tenTau);

            LocalDateTime tgDi = ct.getThoiGianKhoiHanh();
            LocalDateTime tgDen = ct.getThoiGianDen();
            if (tgDi != null) lblGioDi.setText(tgDi.format(dinhDangNgayGio));
            if (tgDen != null) lblGioDen.setText(tgDen.format(dinhDangNgayGio));

            Map<String, Integer> dem = laySoLuongGheCuaChuyen(ct);
            lblSoDaDat.setText(String.valueOf(dem.get("DAT")));
            lblSoTrong.setText(String.valueOf(dem.get("TRONG")));
        }

        btnDatVe.addActionListener(e -> {
            if (ct == null || ct.getMaChuyenTau() == null || ct.getMaChuyenTau().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin chuyến tàu.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            this.setVisible(false);
            NhanVien nvHienTai = new NhanVien("NV-001"); 
            new ChonChoNgoiGUI(ct, nvHienTai).setVisible(true);
        });

        return pnlVe;
    }

    private void capNhatKetQua(List<ChuyenTau> danhSach) {
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

        if (tenGaDi == null || tenGaDen == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ga đi và ga đến.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (date == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày đi!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maGaDi = gaDAO.getMaGaByTenGa(tenGaDi);
        String maGaDen = gaDAO.getMaGaByTenGa(tenGaDen);

        if (maGaDi == null || maGaDen == null) {
            JOptionPane.showMessageDialog(this, "Dữ liệu ga không hợp lệ.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate ngayKhoiHanh = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        List<ChuyenTau> ketQua = chuyenTauDAO.getChuyenTauTheoNgayVaGa(ngayKhoiHanh, maGaDi, maGaDen);

        capNhatKetQua(ketQua);

        if (ketQua == null || ketQua.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến tàu nào phù hợp.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadGaDataToComboBox() {
        List<Ga> danhSachGa = gaDAO.getAllGa();
        cbGaDi.removeAllItems();
        cbGaDen.removeAllItems();
        List<String> tenGaList = new ArrayList<>();
        for (Ga ga : danhSachGa) {
            tenGaList.add(ga.getTenGa());
        }
        for (String ten : tenGaList) {
            cbGaDi.addItem(ten);
            cbGaDen.addItem(ten);
        }
        if (!tenGaList.isEmpty()) {
            cbGaDi.setSelectedIndex(0);
            if (tenGaList.size() > 1) cbGaDen.setSelectedIndex(1);
        }
    }

    //đếm sl
    private Map<String, Integer> laySoLuongGheCuaChuyen(ChuyenTau ct) {
        Map<String, Integer> dem = new HashMap<>();
        if (ct == null) {
            dem.put("DAT", 0);
            dem.put("TRONG", 0);
            return dem;
        }

        String maCT = ct.getMaChuyenTau();
        int slChoDat = ctpdcDAO.TimKiemctPDCTheoMaChuyenTau(maCT).size();

        int tongSoCho = 0;
        String maTau = ct.getMaTau();
        List<ToaTau> danhSachToa = toaTauDAO.getToaTauByMaTau(maTau);
        for (ToaTau toa : danhSachToa) {
            tongSoCho += toa.getSoLuongCho();
        }

        int slChoTrong = Math.max(0, tongSoCho - slChoDat);

        dem.put("DAT", slChoDat);
        dem.put("TRONG", slChoTrong);
        return dem;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnTim) {
            xuLyTimChuyenTau();
            return;
        }
        if (src == btnLamMoi) {
            cbGaDi.setSelectedIndex(cbGaDi.getItemCount() > 0 ? 0 : -1);
            cbGaDen.setSelectedIndex(cbGaDen.getItemCount() > 1 ? 1 : 0);
            dateChooser.setDate(null);
            pnlTrungTam.removeAll();
            pnlTrungTam.revalidate();
            pnlTrungTam.repaint();
            return;
        }
        if (src == btnTroVe) {
          
        }
    }

    public static void main(String[] args) {
        LookAndFeelManager.setNimbusLookAndFeel();
        TraCuuChuyenTauGUI tcct = new TraCuuChuyenTauGUI();
        tcct.setVisible(true);
    }
}
