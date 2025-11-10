package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JPanel pnlChinh;
    private JPanel pnlHienThiCho;
    private JPanel pnlChuaNutToa;
    private JButton btnTiepTuc;
    private JButton nutTroVe;
    private String currentMaToa = null;
    private JLabel lblThongTinChuyen;
    private JLabel lblGioDi;
    private JTextArea txtDanhSachChoNgoi;
    private final DateTimeFormatter DTF_FULL = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private JButton btnTroVe;
	private JFrame parentFrame;
    private List<ChoNgoi> danhSachGheDuocChon = new ArrayList<>();
    private Map<String, ToaTau> mapMaToa = new HashMap<>();

    public GiaoDienChonCho(ChuyenTau chuyentau, NhanVien nv) throws SQLException {

        this.chuyenTauDuocChon = chuyentau;
        this.nhanVienHienTai = nv;
        this.control = new ChonChoNgoiControl();
        
        capNhatThongTinChuyenTau();
        setTitle("CHỌN CHỖ");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        pnlChuaNutToa = new JPanel();
        pnlChuaNutToa.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 8));
        pnlChuaNutToa.setBackground(new Color(103, 192, 144));
        TitledBorder border = BorderFactory.createTitledBorder(
            new LineBorder(Color.BLACK, 2),
            "Chọn Toa",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 24),
            Color.BLACK
        );
        pnlChuaNutToa.setBorder(border);
        
        JPanel pnlTren = new JPanel(new BorderLayout());
        pnlTren.add(pnlChuaNutToa, BorderLayout.CENTER);
        pnlChinh = new JPanel(new BorderLayout());
        pnlChinh.add(pnlTren, BorderLayout.NORTH);
        
        JPanel pnlTrungTam = new JPanel(new BorderLayout(10, 10));
        pnlHienThiCho = new JPanel(new BorderLayout());
        pnlHienThiCho.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel lblKhoiTao = new JLabel("Chọn toa để hiển thị chỗ ngồi.", SwingConstants.CENTER);
        pnlHienThiCho.add(lblKhoiTao, BorderLayout.CENTER);
        
        pnlTrungTam.add(pnlHienThiCho, BorderLayout.CENTER);
        pnlTrungTam.add(taoPanelThongTinChiTiet(), BorderLayout.WEST);
        pnlTrungTam.add(taoPanelHuongDanMau(), BorderLayout.SOUTH);
        pnlChinh.add(pnlTrungTam, BorderLayout.CENTER);
        
        
        JPanel pnlChan = new JPanel(new BorderLayout());
        pnlChan.setBackground(new Color(240, 240, 240));
        pnlChan.setBorder(new EmptyBorder(10, 10, 10, 10));
        
     // Nút Trở Về - sát TRÁI
        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlLeft.setOpaque(false);
        
        // Nút Đặt Vé - sát PHẢI
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlRight.setOpaque(false);
        
        btnTiepTuc = taoButton("Tiếp tục", new Color(74, 140, 103), "");
        btnTiepTuc.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnTiepTuc.setPreferredSize(new Dimension(160, 60));
        btnTiepTuc.setEnabled(false);
        
       
        btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "");
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnTroVe.setPreferredSize(new Dimension(160, 60));
        
        pnlLeft.add(btnTroVe);
        pnlRight.add(btnTiepTuc);
        
        pnlChan.add(pnlLeft, BorderLayout.WEST);
        pnlChan.add(pnlRight, BorderLayout.EAST);
        
        pnlChinh.add(pnlChan, BorderLayout.SOUTH);
        
        
        add(pnlChinh);
        btnTiepTuc.addActionListener(this);
        btnTroVe.addActionListener(this);
        
        if (this.chuyenTauDuocChon.getMaTau() != null) {
            taiDanhSachToa(this.chuyenTauDuocChon.getMaTau());
        }
        capNhatThongTinChuyenTau();

    }
  
    
	@Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();
        if (nguon == btnTiepTuc) {
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
                new GiaoDienTraCuuChuyentau(null, nhanVienHienTai).setVisible(true);
                this.dispose();
            });
        }
    }
    private JPanel taoPanelThongTinChiTiet() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(350, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(new Color(74, 140, 103), 2), "THÔNG TIN CHUYẾN",
            TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 16), new Color(74, 140, 103)));
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

    private JPanel taoPanelHuongDanMau() {
    	
        // Tạo panel chứa hướng dẫn màu cho toa và ghế
        JPanel pnlTongHuongDan = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        pnlTongHuongDan.setBackground(new Color(240, 240, 240));

        // Phần hướng dẫn cho Toa
        JPanel pnlToa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        TitledBorder borderHD = BorderFactory.createTitledBorder(
                new LineBorder(Color.BLACK, 2),
                "Hướng dẫn màu toa",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                Color.BLACK
            );
        pnlToa.setBorder(borderHD);
        pnlToa.add(taoItemMau(new Color(57, 129, 188), Color.BLACK, "Toa còn chỗ"));
        pnlToa.add(taoItemMau(new Color(169, 182, 69), Color.BLACK, "Toa đang chọn"));
        pnlToa.add(taoItemMau(new Color(207, 92, 54), Color.BLACK, "Toa đầy (hết chỗ)"));

        // Phần hướng dẫn cho Ghế
        
        JPanel pnlGhe = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        TitledBorder borderHD2 = BorderFactory.createTitledBorder(
                new LineBorder(Color.BLACK, 2),
                "Hướng dẫn màu ghế",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                Color.BLACK
            );
        pnlGhe.setBorder(borderHD2);
        pnlGhe.add(taoItemMau(Color.WHITE, Color.BLACK, "Ghế trống"));
        pnlGhe.add(taoItemMau(new Color(169, 182, 69), Color.BLACK, "Ghế đang chọn"));
        pnlGhe.add(taoItemMau(new Color(207, 92, 54), Color.BLACK, "Ghế đã đặt"));

        pnlTongHuongDan.add(pnlToa);
        pnlTongHuongDan.add(pnlGhe);

        return pnlTongHuongDan;
    }

    private JPanel taoItemMau(Color mauNen, Color mauChu, String text) {
        // Tạo item màu với ô vuông và text
        JPanel pnlItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        JLabel lblMau = new JLabel();
        lblMau.setPreferredSize(new Dimension(20, 20));
        lblMau.setBackground(mauNen);
        lblMau.setOpaque(true);
        lblMau.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JLabel lblText = new JLabel(text);
        lblText.setForeground(mauChu);
        lblText.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        pnlItem.add(lblMau);
        pnlItem.add(lblText);

        return pnlItem;
    }

    private void capNhatThongTinChuyenTau() {
        // Cập nhật thông tin chuyến tàu và hiển thị
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
        // Tải danh sách toa và tạo nút cho từng toa
        List<ToaTau> danhSachToa = control.layDanhSachToa(maTau);
        pnlChuaNutToa.removeAll();
        mapMaToa.clear();
        if (danhSachToa.isEmpty()) {
            pnlChuaNutToa.add(new JLabel("Không tìm thấy toa tàu."));
            return;
        }
        int displayIndex = danhSachToa.size();
        for (int i = danhSachToa.size() - 1; i >= 0; i--) {
            ToaTau toa = danhSachToa.get(i);
            String displayLabel = String.format("%02d", displayIndex--);
            mapMaToa.put(toa.getMaToa(), toa);
            JButton btnToa = new JButton(displayLabel);
            btnToa.setFont(new Font("Segoe UI", Font.BOLD, 18));
            btnToa.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            btnToa.setPreferredSize(new Dimension(80, 50));
            btnToa.setActionCommand(toa.getMaToa());
            btnToa.addActionListener(e -> xuLyChonToa(e.getActionCommand()));

            // Check toa đầy để set màu và disable
            try {
                List<ChoNgoi> danhSachCho = control.layDanhSachChoNgoi(toa.getMaToa());
                int soTrong = control.demSoChoTrong(danhSachCho);
                if (soTrong == 0) {
                    btnToa.setBackground(new Color(207, 92, 54)); // Màu đầy
                    btnToa.setForeground(Color.WHITE);
                    btnToa.setEnabled(false); // Không click được
                } else {
                    btnToa.setBackground(new Color(57, 129, 188)); // Màu bình thường
                    btnToa.setForeground(Color.WHITE);
                }
            } catch (Exception ex) {
                System.err.println("Lỗi check chỗ trống toa: " + ex.getMessage());
                btnToa.setBackground(new Color(57, 129, 188)); // Default nếu lỗi
                btnToa.setForeground(Color.WHITE);
            }

            pnlChuaNutToa.add(btnToa);
        }
        pnlChuaNutToa.revalidate();
        pnlChuaNutToa.repaint();
    }
    
    private void xuLyChonToa(String maToa) {
        // Xử lý khi chọn toa, cập nhật màu và hiển thị chỗ ngồi
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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải chỗ ngồi: " + ex.getMessage(),
                    "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void capNhatDanhSachChoNgoiDaChon() {
        // Cập nhật text area danh sách chỗ đã chọn
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
        // Cập nhật màu nút toa khi chọn
        for (Component comp : pnlChuaNutToa.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (!btn.isEnabled()) {
                    continue; // Không thay đổi màu toa đầy khi chọn toa khác
                }
                if (btn.getActionCommand().equals(currentMaToa)) {
                    btn.setBackground(new Color(169, 182, 69));
                    btn.setForeground(Color.BLACK);
                } else {
                    btn.setBackground(new Color(57, 129, 188));
                    btn.setForeground(Color.WHITE);
                }
            }
        }
    }
    
    private JPanel taoPanelGhe(ToaTau toa, List<ChoNgoi> danhSachCho, String loaiCho) {
        JPanel panelToa = new JPanel(new BorderLayout());
        
        String displayMaToa = String.format("%02d",
            Integer.parseInt(toa.getMaToa().substring(toa.getMaToa().length() - 2)));
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            new LineBorder(new Color(74, 140, 103), 2),
            displayMaToa + " - " + loaiCho,
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 18),
            new Color(74, 140, 103)
        );
        panelToa.setBorder(titledBorder);
        
        JPanel pnlKhuVucTong = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlKhuVucTong.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        List<ChoNgoi> danhSachChoSorted = danhSachCho.stream()
            .filter(cho -> cho != null && cho.getMaChoNgoi() != null)
            .sorted((c1, c2) -> c1.getMaChoNgoi().compareTo(c2.getMaChoNgoi()))
            .collect(Collectors.toList());
            
        if (danhSachChoSorted.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy ghế nào trong toa " + toa.getMaToa(),
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return panelToa;
        }
        
        final String maChuyenTauHienTai = chuyenTauDuocChon.getMaChuyenTau();
        
        int soGheToiDa = Math.min(danhSachChoSorted.size(), 40); 
        int soGheMoiKhu = soGheToiDa / 4;
        int index = 0; 

        for (int k = 0; k < 4; k++) { // 4 khu vực [6]
            JPanel pnlKhuVuc = new JPanel(new GridLayout(2, 5, 5, 5));
            pnlKhuVuc.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.GRAY, 1),
                "Khu vực " + (k + 1),
                TitledBorder.LEADING,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.ITALIC, 14),
                Color.DARK_GRAY
            ));
            
            for (int i = 0; i < soGheMoiKhu && index < soGheToiDa; i++, index++) {
                ChoNgoi cho = danhSachChoSorted.get(index);
                if (cho != null) {
                    
                    boolean isBookedDynamically = false;
                    try {
                        isBookedDynamically = control.isChoNgoiBooked(cho.getMaChoNgoi(), maChuyenTauHienTai);
                    } catch (SQLException ex) {
                        System.err.println("Lỗi CSDL khi kiểm tra chỗ ngồi " + cho.getMaChoNgoi() + ": " + ex.getMessage());
                        isBookedDynamically = false;
                    }

                    if (isBookedDynamically) { 
                        JLabel lbl = new JLabel(cho.getMaChoNgoi(), SwingConstants.CENTER);
                        lbl.setPreferredSize(new Dimension(50, 40));
                        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
                        lbl.setBackground(new Color(207, 92, 54)); // Màu đỏ
                        lbl.setForeground(Color.WHITE);
                        lbl.setOpaque(true);
                        lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                        pnlKhuVuc.add(lbl);
                    } else {
                        JButton nut = new JButton(cho.getMaChoNgoi());
                        nut.setPreferredSize(new Dimension(50, 40)); 
                        nut.setFont(new Font("Segoe UI", Font.BOLD, 16)); 

                        boolean isSelected = danhSachGheDuocChon.stream().anyMatch(c ->
                            c.getMaChoNgoi().equals(cho.getMaChoNgoi()) &&
                            c.getToaTau().getMaToa().equals(cho.getToaTau().getMaToa())); 
                            
                        nut.setBackground(isSelected ? new Color(169, 182, 69) : Color.WHITE); 
                        nut.addActionListener(new ClickChonGhe(cho, nut, Color.WHITE, new Color(169, 182, 69))); 
                        pnlKhuVuc.add(nut);
                    }
                }
            }
            pnlKhuVucTong.add(pnlKhuVuc); 
        }
        panelToa.add(new JScrollPane(pnlKhuVucTong), BorderLayout.CENTER); 
        return panelToa;
    }

    private JPanel taoPanelGiuong(ToaTau toa, List<ChoNgoi> danhSachCho) {
        
    	JPanel panelToa = new JPanel(new BorderLayout());

        String displayMaToa = String.format("%02d",
            Integer.parseInt(toa.getMaToa().substring(toa.getMaToa().length() - 2)));
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            new LineBorder(new Color(74, 140, 103), 2),
            displayMaToa + " - GIƯỜNG NẰM",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 18),
            new Color(74, 140, 103)
        );
        panelToa.setBorder(titledBorder);
        
        int giuongMoiTangMoiKhoang = 2; 
        int tongGiuongMoiKhoang = giuongMoiTangMoiKhoang * 2; 
        int soKhoang = (int) Math.ceil((double) danhSachCho.size() / tongGiuongMoiKhoang); 
        
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

        String maChuyenTauHienTai = chuyenTauDuocChon.getMaChuyenTau();

        for (int k = 0; k < soKhoang; k++) {
            JPanel pnlMotKhoang = new JPanel(new BorderLayout());
            pnlMotKhoang.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1), "Khoang " + (k + 1)));
            JPanel pnlGiuongGrid = new JPanel(new GridLayout(2, 2, 6, 6)); 
            
            for (int tang = 1; tang >= 0; tang--) {
                for (int vt = 0; vt < giuongMoiTangMoiKhoang; vt++) {
                    ChoNgoi cho = (idx < danhSachCho.size()) ? danhSachCho.get(idx) : null; 
                    if (cho != null) {
                        
                        boolean isBookedDynamically = false;
                        try {
                            isBookedDynamically = control.isChoNgoiBooked(cho.getMaChoNgoi(), maChuyenTauHienTai);
                        } catch (SQLException ex) {
                            System.err.println("Lỗi CSDL khi kiểm tra giường " + cho.getMaChoNgoi() + ": " + ex.getMessage());
                            isBookedDynamically = false;
                        }
                        
                        if (isBookedDynamically) { 
                            JLabel lbl = new JLabel(cho.getMaChoNgoi(), SwingConstants.CENTER);
                            lbl.setBackground(new Color(207, 92, 54));
                            lbl.setForeground(Color.WHITE);
                            lbl.setOpaque(true);
                            lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
                            lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                            pnlGiuongGrid.add(lbl);
                        } else {
                            JButton nut = new JButton(cho.getMaChoNgoi());
                            nut.setFont(new Font("Segoe UI", Font.BOLD, 16));

                            Color mauMacDinh = Color.WHITE;
                            Color mauDaChon = new Color(169, 182, 69); 
                            
                            boolean isSelected = danhSachGheDuocChon.stream().anyMatch(c ->
                                c.getMaChoNgoi().equals(cho.getMaChoNgoi()) &&
                                c.getToaTau().getMaToa().equals(cho.getToaTau().getMaToa())); 
                                
                            nut.setBackground(isSelected ? mauDaChon : mauMacDinh);
                            nut.addActionListener(new ClickChonGhe(cho, nut, mauMacDinh, mauDaChon)); 
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
    
    class ClickChonGhe implements ActionListener {
        private ChoNgoi cho;
        private JButton nut;
        private Color mauMacDinh;
        private Color mauDaChon;
        public ClickChonGhe(ChoNgoi cho, JButton nut, Color mauMacDinh, Color mauDaChon) {
            this.cho = cho;
            this.nut = nut;
            this.mauMacDinh = mauMacDinh;
            this.mauDaChon = mauDaChon;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            // Xử lý click chọn ghế
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
            btnTiepTuc.setEnabled(!danhSachGheDuocChon.isEmpty());
        }
    }
    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
        URL iconUrl = GiaoDienChonCho.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
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

//    public static void main(String[] args) throws SQLException {
//    	ChuyenTau ct2 = new ChuyenTau();
//     	NhanVien nv = new NhanVien();
//     	new GiaoDienChonCho(ct2, nv).setVisible(true);
//    }
}