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
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import control.ChonChoNgoiControl;
import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.NhanVien;
import entity.ToaTau;

public class GiaoDienChonCho extends JFrame implements ActionListener {
    private ChuyenTau chuyenTauDuocChon;
    private NhanVien nhanVienHienTai;
    private ChonChoNgoiControl control;
    private Map<String, ToaTau> mapMaToa = new HashMap<>();
    private Map<String, String> mapMaToaToDisplay = new HashMap<>();
    private JPanel pnlChinh;
    private JPanel pnlHienThiCho;
    private JPanel pnlChuaNutToa;
    private JButton nutDatVe;
    private JButton nutTroVe;
    private JLabel lblTieuDe;
 
    private Color mauChinh = new Color(74, 140, 103);
    private Color mauNenChan = new Color(240, 240, 240);
 
    private Color MAU_NUT_HANHDONG = new Color(74, 140, 103);
    private Color MAU_NUT_QUAY_LAI = new Color(0, 128, 255);
 
    private final Color MAU_TOA_KHA_DUNG_NHE = new Color(57, 129, 188);
    private final Color MAU_TOA_DANG_CHON = new Color(169, 182, 69);
    private final Color MAU_TOA_HET_VE = new Color(207, 92, 54);
 
    private final Color MAU_GHE_DA_DAT = new Color(207, 92, 54);
    private final Color MAU_GHE_TRONG = Color.WHITE;
    private final Color MAU_GHE_DANG_CHON = new Color(169, 182, 69);
 
    private Color MAU_NEN_TOA = new Color(103, 192, 144);
 
    private List<ChoNgoi> danhSachGheDuocChon = new ArrayList<>();
    private GiaoDienTraCuuChuyentau previousScreen;
    private int soChoTrongBanDau = 0;
    private final int SO_HANG = 4;
    private final int SO_COT = 10;
    private String currentMaToa = null;
    private JLabel lblThongTinChuyen;
    private JLabel lblGioDi;
    private JTextArea txtDanhSachChoNgoi;
    private final DateTimeFormatter DTF_FULL = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
 
    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
        URL iconUrl = GiaoDienChonCho.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
 
    public GiaoDienChonCho(ChuyenTau chuyentau, NhanVien nv, GiaoDienTraCuuChuyentau previous) throws SQLException {
        this.chuyenTauDuocChon = chuyentau;
        this.nhanVienHienTai = nv;
        this.previousScreen = previous;
        this.control = new ChonChoNgoiControl();
        khoiTaoThanhPhan();
        if (this.chuyenTauDuocChon.getMaTau() != null) {
            taiDanhSachToa(this.chuyenTauDuocChon.getMaTau());
        }
 
        thietLapSuKien();
        setTitle("CHỌN CHỖ NGỒI");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        capNhatThongTinChuyenTauTinh();
    }
 
    private void khoiTaoThanhPhan() {
        pnlChuaNutToa = new JPanel();
        pnlChuaNutToa.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 8));
        pnlChuaNutToa.setBackground(MAU_NEN_TOA);
 
        TitledBorder border = BorderFactory.createTitledBorder(
            new LineBorder(Color.BLACK, 2),
            "Chọn Toa",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 24),
            Color.BLACK
        );
        pnlChuaNutToa.setBorder(border);
 
        JPanel pnlBacTren = new JPanel(new BorderLayout());
        pnlBacTren.add(pnlChuaNutToa, BorderLayout.CENTER);
 
        pnlChinh = new JPanel(new BorderLayout());
        pnlChinh.add(pnlBacTren, BorderLayout.NORTH);
 
        JPanel pnlTrungTam = new JPanel(new BorderLayout(10, 10));
        pnlHienThiCho = new JPanel(new BorderLayout());
        pnlHienThiCho.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel lblKhoiTao = new JLabel("Chọn toa để hiển thị chỗ ngồi.", SwingConstants.CENTER);
        pnlHienThiCho.add(lblKhoiTao, BorderLayout.CENTER);
        pnlTrungTam.add(pnlHienThiCho, BorderLayout.CENTER);
        pnlTrungTam.add(taoPanelThongTinChiTiet(), BorderLayout.WEST);
 
        pnlChinh.add(pnlTrungTam, BorderLayout.CENTER);
 
        JPanel pnlChan = new JPanel(new BorderLayout());
        pnlChan.setBackground(mauNenChan);
        pnlChan.setBorder(new EmptyBorder(10, 10, 10, 10));
 
        nutDatVe = new JButton("Tiếp Tục");
        nutDatVe.setBackground(MAU_NUT_HANHDONG);
        nutDatVe.setForeground(Color.WHITE);
        nutDatVe.setFont(new Font("Segoe UI", Font.BOLD, 17));
        nutDatVe.setPreferredSize(new Dimension(250, 40));
        nutDatVe.setEnabled(false);
        ImageIcon iconDatVe = chinhKichThuoc("/images/datve_icon.png", 30, 30);
        if (iconDatVe != null) {
            nutDatVe.setIcon(iconDatVe);
        }
 
        nutTroVe = new JButton("Trở Về");
        nutTroVe.setFont(new Font("Segoe UI", Font.BOLD, 17));
        nutTroVe.setBackground(MAU_NUT_QUAY_LAI);
        nutTroVe.setForeground(Color.WHITE);
        nutTroVe.setPreferredSize(new Dimension(150, 40));
 
        pnlChan.add(nutTroVe, BorderLayout.WEST);
        pnlChan.add(nutDatVe, BorderLayout.EAST);
        pnlChinh.add(pnlChan, BorderLayout.SOUTH);
 
        add(pnlChinh);
    }
 
    private void thietLapSuKien() {
        nutDatVe.addActionListener(this);
        nutTroVe.addActionListener(this);
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();
        if (nguon == nutDatVe) {
            if (danhSachGheDuocChon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn chỗ ngồi.", "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            danhSachGheDuocChon = danhSachGheDuocChon.stream()
                    .filter(cho -> cho != null && cho.getMaChoNgoi() != null)
                    .collect(Collectors.toList());
            if (danhSachGheDuocChon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Danh sách ghế được chọn không hợp lệ.",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            SwingUtilities.invokeLater(() -> {
                new GiaoDienNhapThongTinHK(chuyenTauDuocChon, danhSachGheDuocChon,
                        nhanVienHienTai).setVisible(true);
                this.dispose();
            });
        } else if (nguon == nutTroVe) {
            SwingUtilities.invokeLater(() -> {
                new GiaoDienTraCuuChuyentau(nhanVienHienTai).setVisible(true);
                this.dispose();
            });
        }
    }
 
    private JPanel taoPanelThongTinChiTiet() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(350, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(mauChinh, 2), "THÔNG TIN CHUYẾN",
            TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 16), mauChinh));
 
        JPanel pnlNoiDung = new JPanel(new GridBagLayout());
        pnlNoiDung.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
 
        lblThongTinChuyen = new JLabel("Tàu: N/A - Chuyến: N/A");
        lblGioDi = new JLabel("Khởi hành: N/A");
 
        int dong = 0;
        pnlNoiDung.add(lblThongTinChuyen, taogbcLabel(0, dong++, gbc.insets));
        pnlNoiDung.add(lblGioDi, taogbcLabel(0, dong++, gbc.insets));
 
        txtDanhSachChoNgoi = new JTextArea(12, 20);
        txtDanhSachChoNgoi.setEditable(false);
        txtDanhSachChoNgoi.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtDanhSachChoNgoi.setText("Danh sách chỗ đang chọn sẽ hiển thị tại đây...");
        txtDanhSachChoNgoi.setLineWrap(true);
        txtDanhSachChoNgoi.setWrapStyleWord(true);
        JScrollPane scrollChoNgoi = new JScrollPane(txtDanhSachChoNgoi);
        scrollChoNgoi.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(Color.GRAY, 1), "Chỗ đã chọn (Toa/Ghế)"));
 
        panel.add(pnlNoiDung, BorderLayout.NORTH);
        panel.add(scrollChoNgoi, BorderLayout.CENTER);
 
        return panel;
    }
 
    private void capNhatThongTinChuyenTauTinh() {
        if (chuyenTauDuocChon != null) {
            String maCT = chuyenTauDuocChon.getMaChuyenTau();
            String maTau = chuyenTauDuocChon.getMaTau();
            String maLT = chuyenTauDuocChon.getMaLichTrinh();
 
            String tenTauDisplay = "N/A";
            String hanhTrinhDisplay = "Chuyến: " + maCT;
 
            try {
                String tenTau = control.layTenTau(maTau);
                if (tenTau != null) {
                    tenTauDisplay = tenTau;
                } else {
                    tenTauDisplay = "Mã Tàu " + maTau;
                }
 
                String tenGaDi = control.layTenGaDi(maLT);
                String tenGaDen = control.layTenGaDen(maLT);
 
                if (tenGaDi != null && tenGaDen != null) {
                    hanhTrinhDisplay = tenGaDi + " -> " + tenGaDen;
                } else {
                    hanhTrinhDisplay = "Chuyến: " + maCT;
                }
 
            } catch (Exception e) {
                System.err.println("Lỗi khi tải thông tin chi tiết chuyến tàu: " + e.getMessage());
            }
 
            String gioDi = (chuyenTauDuocChon.getThoiGianKhoiHanh() != null) ?
                chuyenTauDuocChon.getThoiGianKhoiHanh().format(DTF_FULL)
                : "N/A";
 
            lblThongTinChuyen.setText("Tàu: " + tenTauDisplay + " | Chuyến: " + hanhTrinhDisplay);
            lblGioDi.setText("Khởi hành: " + gioDi);
        }
    }
 
    private void taiDanhSachToa(String maTau) throws SQLException {
        List<ToaTau> danhSachToa = control.layDanhSachToa(maTau);
        pnlChuaNutToa.removeAll();
        mapMaToa.clear();
        mapMaToaToDisplay.clear();
        if (danhSachToa.isEmpty()) {
            pnlChuaNutToa.add(new JLabel("Không tìm thấy toa tàu."));
            return;
        }
        int displayIndex = danhSachToa.size();
        for (int i = danhSachToa.size() - 1; i >= 0; i--) {
            ToaTau toa = danhSachToa.get(i);
            String displayLabel = String.format("%02d", displayIndex--);
            mapMaToa.put(toa.getMaToa(), toa);
            mapMaToaToDisplay.put(toa.getMaToa(), displayLabel);
            JButton btnToa = new JButton(displayLabel);
 
            btnToa.setBackground(MAU_TOA_KHA_DUNG_NHE);
            btnToa.setForeground(Color.WHITE); 
            btnToa.setFont(new Font("Segoe UI", Font.BOLD, 18)); 
            btnToa.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            btnToa.setPreferredSize(new Dimension(80, 50));
            btnToa.setActionCommand(toa.getMaToa());
            btnToa.addActionListener(e -> xuLyChonToa(e.getActionCommand()));
            pnlChuaNutToa.add(btnToa);
        }
        pnlChuaNutToa.revalidate();
        pnlChuaNutToa.repaint();
    }
 
    private void xuLyChonToa(String maToa) {
        currentMaToa = maToa;
        capNhatMauButtonToa();
        ToaTau toa = mapMaToa.get(maToa);
        if (toa == null) return;
        try {
            List<ChoNgoi> danhSachCho = control.layDanhSachChoNgoi(toa.getMaToa());
            danhSachCho = danhSachCho.stream()
                    .filter(cho -> cho != null && cho.getMaChoNgoi() != null)
                    .collect(Collectors.toList());
            if (danhSachCho.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy chỗ ngồi cho toa " + maToa,
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String loai = control.layLoaiCho(toa.getHeSoGia());
            int soTrong = control.demSoChoTrong(danhSachCho);
            JPanel pnlHienThi = loai.contains("Giường") ? taoPanelGiuong(toa, danhSachCho) :
                    taoPanelGhe(toa, danhSachCho, loai);
            pnlHienThiCho.removeAll();
            pnlHienThiCho.add(pnlHienThi, BorderLayout.CENTER);
            pnlHienThiCho.revalidate();
            pnlHienThiCho.repaint();
            soChoTrongBanDau = soTrong;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải chỗ ngồi: " + ex.getMessage(),
                    "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private void capNhatDanhSachChoNgoiDaChon() {
        StringBuilder sb = new StringBuilder();
        sb.append("---------------------------------\n");
        sb.append(String.format("%-8s %-10s %s\n", "STT", "TOA", "CHỖ"));
        sb.append("---------------------------------\n");
        int i = 1;
        List<ChoNgoi> sortedList = danhSachGheDuocChon.stream()
            .sorted((c1, c2) -> c1.getMaChoNgoi().compareTo(c2.getMaChoNgoi()))
            .collect(Collectors.toList());
        for (ChoNgoi cho : sortedList) {
            if (cho != null && cho.getToaTau() != null) {
                String maToa = cho.getToaTau().getMaToa();
                String maCho = cho.getMaChoNgoi();
                sb.append(String.format("%-8d %-10s %s\n", i++, maToa, maCho));
            }
        }
        sb.append("---------------------------------\n");
        sb.append("Tổng cộng: ").append(danhSachGheDuocChon.size()).append(" chỗ.");
        txtDanhSachChoNgoi.setText(sb.toString());
    }
 
    private void capNhatMauButtonToa() {
        for (Component comp : pnlChuaNutToa.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getActionCommand().equals(currentMaToa)) {
                    btn.setBackground(MAU_TOA_DANG_CHON);
                    btn.setForeground(Color.BLACK); // CHỮ ĐEN KHI CHỌN
                } else {
                    btn.setBackground(MAU_TOA_KHA_DUNG_NHE);
                    btn.setForeground(Color.WHITE); // CHỮ TRẮNG KHI KHÔNG CHỌN
                }
            }
        }
    }
 
    private JPanel taoPanelGhe(ToaTau toa, List<ChoNgoi> danhSachCho, String loaiCho) {
        JPanel panelToa = new JPanel(new BorderLayout());
        String displayMaToa = mapMaToaToDisplay.get(toa.getMaToa());
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            new LineBorder(mauChinh, 2),
            displayMaToa + " - " + loaiCho,
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 18),
            mauChinh
        );
        panelToa.setBorder(titledBorder);
        final int SO_HANG_KHU = 2;
        final int SO_COT_KHU = 5;
        JPanel pnlKhuVucTong = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlKhuVucTong.setBorder(new EmptyBorder(10, 10, 10, 10));
        String[][] maGheKhuVuc = {
            {"01", "05", "09", "13", "17", "02", "06", "10", "14", "18"},
            {"21", "25", "29", "33", "37", "22", "26", "30", "34", "38"},
            {"03", "07", "11", "15", "19", "04", "08", "12", "16", "20"},
            {"23", "27", "31", "35", "39", "24", "28", "32", "36", "40"}
        };
        List<ChoNgoi> danhSachChoSorted = danhSachCho.stream()
            .filter(cho -> cho != null && cho.getMaChoNgoi() != null)
            .sorted((c1, c2) -> c1.getMaChoNgoi().compareTo(c2.getMaChoNgoi()))
            .collect(Collectors.toList());
        if (danhSachChoSorted.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy ghế nào trong toa " + toa.getMaToa(),
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return panelToa;
        }
        Map<String, ChoNgoi> mapSoUIToChoNgoi = new HashMap<>();
        int maxSeats = Math.min(danhSachChoSorted.size(), 40);
        for (int i = 0; i < maxSeats; i++) {
            int khuIndex = i / (SO_HANG_KHU * SO_COT_KHU);
            int indexInKhu = i % (SO_HANG_KHU * SO_COT_KHU);
            String soUI = maGheKhuVuc[khuIndex][indexInKhu];
            mapSoUIToChoNgoi.put(soUI, danhSachChoSorted.get(i));
        }
        for (int k = 0; k < 4; k++) {
            JPanel pnlKhuVuc = new JPanel(new GridLayout(SO_HANG_KHU, SO_COT_KHU, 5, 5));
            TitledBorder khuVucBorder = BorderFactory.createTitledBorder(
                new LineBorder(Color.GRAY, 1),
                "Khu vực " + (k + 1),
                TitledBorder.LEADING,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.ITALIC, 14),
                Color.DARK_GRAY
            );
            pnlKhuVuc.setBorder(khuVucBorder);
            for (int i = 0; i < SO_HANG_KHU * SO_COT_KHU; i++) {
                String maGheSoUI = maGheKhuVuc[k][i].trim();
                ChoNgoi cho = mapSoUIToChoNgoi.get(maGheSoUI);
                if (cho != null) {
                    String trangThai = (cho.getTrangThai() != null) ? cho.getTrangThai().trim() : "Trống";
                    if ("Đã đặt".equalsIgnoreCase(trangThai)) {
                        JLabel lbl = new JLabel(cho.getMaChoNgoi(), SwingConstants.CENTER);
                        lbl.setPreferredSize(new Dimension(50, 40));
                        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
                        lbl.setBackground(MAU_GHE_DA_DAT);
                        lbl.setForeground(Color.WHITE);
                        lbl.setOpaque(true);  
                        lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));  
                        pnlKhuVuc.add(lbl);
                    } else {
                    	//button ghe trong
                    	JButton nut = new JButton(cho.getMaChoNgoi());
                        nut.setPreferredSize(new Dimension(50, 40));
                        nut.setFont(new Font("Segoe UI", Font.BOLD, 16));
                        Color mauMacDinh = MAU_GHE_TRONG;
                        Color mauDaChon = MAU_GHE_DANG_CHON;
                        nut.setEnabled(true);
                        boolean isSelected = danhSachGheDuocChon.stream().anyMatch(c ->
                            c.getMaChoNgoi().equals(cho.getMaChoNgoi()) &&
                            c.getToaTau().getMaToa().equals(cho.getToaTau().getMaToa()));
                        Color initial = isSelected ? mauDaChon : mauMacDinh;
                        nut.setBackground(initial);
                        nut.addActionListener(new TrinhLangNgheChonGhe(cho, nut, mauMacDinh, mauDaChon));
                        pnlKhuVuc.add(nut);
                    }
                } else {
                    JLabel lbl = new JLabel(maGheSoUI, SwingConstants.CENTER);
                    lbl.setPreferredSize(new Dimension(50, 40));
                    lbl.setBackground(Color.LIGHT_GRAY);
                    lbl.setOpaque(true);
                    pnlKhuVuc.add(lbl);
                }
            }
            pnlKhuVucTong.add(pnlKhuVuc);
        }
        panelToa.add(new JScrollPane(pnlKhuVucTong), BorderLayout.CENTER);
        return panelToa;
    }
 
    private JPanel taoPanelGiuong(ToaTau toa, List<ChoNgoi> danhSachCho) {
        JPanel panelToa = new JPanel(new BorderLayout());
        String displayMaToa = mapMaToaToDisplay.get(toa.getMaToa());
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            new LineBorder(mauChinh, 2),
            displayMaToa + " - GIƯỜNG NẰM",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 18),
            mauChinh
        );
        panelToa.setBorder(titledBorder);
        int giuongMoiTangMoiKhoang = 2;
        int soKhoang = danhSachCho.size() / 4;
 
        JPanel pnlTong = new JPanel(new BorderLayout());
        JPanel pnlTangLabels = new JPanel(new GridLayout(2, 1));
        JLabel lblT2 = new JLabel("Tầng 2", SwingConstants.CENTER);
        lblT2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JLabel lblT1 = new JLabel("Tầng 1", SwingConstants.CENTER);
        lblT1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlTangLabels.add(lblT2);
        pnlTangLabels.add(lblT1);
        pnlTong.add(pnlTangLabels, BorderLayout.WEST);
        JPanel pnlKhoang = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        int idx = 0;
        for (int k = 0; k < soKhoang; k++) {
            JPanel pnlMotKhoang = new JPanel(new BorderLayout());
            pnlMotKhoang.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1), "Khoang " + (k + 1)));
            JPanel pnlGiuongGrid = new JPanel(new GridLayout(2, 2, 6, 6));
            for (int tang = 1; tang >= 0; tang--) {
                for (int vt = 0; vt < giuongMoiTangMoiKhoang; vt++) {
                    ChoNgoi cho = (idx < danhSachCho.size()) ? danhSachCho.get(idx) : null;
                    if (cho != null) {
                        String trangThai = (cho.getTrangThai() != null) ? cho.getTrangThai().trim() : "";
                        
                        if ("Đã đặt".equalsIgnoreCase(trangThai)) {
                        	JLabel lbl = new JLabel(cho.getMaChoNgoi(), SwingConstants.CENTER);
                            lbl.setBackground(MAU_GHE_DA_DAT);
                            lbl.setForeground(Color.WHITE);
                            lbl.setOpaque(true);
                            lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); 
                            lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
                            pnlGiuongGrid.add(lbl);
                        } else {
                           
                            JButton nut = new JButton(cho.getMaChoNgoi());
                            Color mauMacDinh = MAU_GHE_TRONG;
                            Color mauDaChon = MAU_GHE_DANG_CHON;
                            boolean isSelected = danhSachGheDuocChon.stream()
                                .anyMatch(c -> c.getMaChoNgoi().equals(cho.getMaChoNgoi())
                                    && c.getToaTau().getMaToa().equals(cho.getToaTau().getMaToa()));
                            Color initial = isSelected ? mauDaChon : mauMacDinh;
                            nut.setBackground(initial);
                            nut.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
                            nut.addActionListener(new TrinhLangNgheChonGhe(cho, nut, mauMacDinh, mauDaChon));
                            pnlGiuongGrid.add(nut);
                        }
                    } else {
                        JLabel lbl = new JLabel(String.format("%02d", idx + 1), SwingConstants.CENTER);
                        lbl.setBackground(Color.LIGHT_GRAY);
                        lbl.setOpaque(true);
                        pnlGiuongGrid.add(lbl);
                    }
                    idx++;
                }
            }
            pnlMotKhoang.add(pnlGiuongGrid, BorderLayout.CENTER);
            gbc.gridx = k;
            gbc.weightx = 1.0;
 
            pnlKhoang.add(pnlMotKhoang, gbc);
        }
        JScrollPane scroll = new JScrollPane(pnlKhoang);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        pnlTong.add(scroll, BorderLayout.CENTER);
        panelToa.add(pnlTong, BorderLayout.CENTER);
        return panelToa;
    }
 
    private GridBagConstraints taoGBC(int x, int y, int fill, int anchor, double weightx, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = fill;
        gbc.anchor = anchor;
        gbc.weightx = weightx;
        gbc.insets = insets;
        return gbc;
    }
 
    private GridBagConstraints taogbcLabel(int x, int y, Insets insets) {
        return taoGBC(x, y, GridBagConstraints.NONE, GridBagConstraints.WEST, 0.0, insets);
    }
 
    private GridBagConstraints taogbcField(int x, int y, Insets insets, int width) {
        GridBagConstraints gbc = taoGBC(x, y, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST,
            1.0, insets);
        gbc.gridwidth = width;
        return gbc;
    }
 
    class TrinhLangNgheChonGhe implements ActionListener {
        private ChoNgoi cho;
        private JButton nut;
        private Color mauMacDinh;
        private Color mauDaChon;
        public TrinhLangNgheChonGhe(ChoNgoi cho, JButton nut, Color mauMacDinh, Color mauDaChon) {
            this.cho = cho;
            this.nut = nut;
            this.mauMacDinh = mauMacDinh;
            this.mauDaChon = mauDaChon;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cho == null) {
                System.err.println("Lỗi logic: ChoNgoi là NULL trong action listener.");
                return;
            }
            if (nut.getBackground().equals(mauMacDinh)) {
                String seatMaToa = cho.getToaTau().getMaToa();
                String seatMaCho = cho.getMaChoNgoi();
                danhSachGheDuocChon.removeIf(c -> c.getToaTau().getMaToa().equals(seatMaToa) && c.getMaChoNgoi().equals(seatMaCho));
                danhSachGheDuocChon.add(cho);
                nut.setBackground(mauDaChon);
            } else {
                String seatMaToa = cho.getToaTau().getMaToa();
                String seatMaCho = cho.getMaChoNgoi();
                danhSachGheDuocChon.removeIf(c -> c.getToaTau().getMaToa().equals(seatMaToa) && c.getMaChoNgoi().equals(seatMaCho));
                nut.setBackground(mauMacDinh);
            }
            capNhatDanhSachChoNgoiDaChon();
            nutDatVe.setEnabled(!danhSachGheDuocChon.isEmpty());
        }
    }
}