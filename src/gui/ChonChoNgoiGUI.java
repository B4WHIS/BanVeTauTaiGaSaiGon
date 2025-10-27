package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

// Entities và DAO
import dao.ChoNgoiDAO;
import dao.ToaTauDAO;
import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.NhanVien;
import entity.ToaTau; 

public class ChonChoNgoiGUI extends JFrame implements ActionListener {
    private ChuyenTau chuyenTauDuocChon;
    private NhanVien nhanVienHienTai;
    
    private ToaTauDAO toaTauDAO = new ToaTauDAO(); 
    private ChoNgoiDAO choNgoiDAO = new ChoNgoiDAO(); 
    private Map<String, ToaTau> mapMaToa = new HashMap<>(); 

    private JPanel pnlChinh;
    private JPanel pnlBenTrai;
    private JPanel pnlHienThiCho;
    private JPanel pnlChuaNutToa;
    private JButton nutDatVe;
    private JButton nutTroVe;
    private JLabel lblTieuDe;
    private Color mauChinh = new Color(74, 140, 103); 
    private Color mauNenChan = new Color(240, 240, 240);
    private List<ChoNgoi> danhSachGheDuocChon = new ArrayList<>(); 
    private TraCuuChuyenTauGUI previousScreen; 
    private int soChoTrongBanDau = 0; 
    private final int SO_HANG = 4; 
    private final int SO_COT = 10; 
    
    // Giả định màn hình trước đã được định nghĩa
    class TraCuuChuyenTauGUI extends JFrame {} 

    // Helper: Chỉnh kích thước Icon (nếu cần)
    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
        URL iconUrl = ChonChoNgoiGUI.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public ChonChoNgoiGUI(ChuyenTau chuyentau, NhanVien nv, TraCuuChuyenTauGUI previous) throws SQLException { 
        this.chuyenTauDuocChon = chuyentau;
        this.nhanVienHienTai = nv;
        this.previousScreen = previous;
        
        khoiTaoThanhPhan(); 
        
        if (this.chuyenTauDuocChon.getMaTau() != null) {
            taiDanhSachToa(this.chuyenTauDuocChon.getMaTau()); // Lấy dữ liệu thực từ DAO
        }
        
        thietLapSuKien(); 
        setTitle("CHỌN CHỖ NGỒI");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
    }
    
    private void khoiTaoThanhPhan() {
        // --- Title Panel ---
        JPanel pnlTieuDe = new JPanel(new BorderLayout()); 
        lblTieuDe = new JLabel("CHỌN CHỖ NGỒI - " + chuyenTauDuocChon.getMaChuyenTau(), SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTieuDe.setForeground(mauChinh);
        pnlTieuDe.add(lblTieuDe, BorderLayout.CENTER); 

        // --- Coach selection panel (Nơi đặt các nút Toa) ---
        pnlChuaNutToa = new JPanel();
        pnlChuaNutToa.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 8));
        pnlChuaNutToa.setBackground(new Color(103, 192, 144));
        
        JPanel pnlBacTren = new JPanel(new BorderLayout());
        pnlBacTren.add(pnlTieuDe, BorderLayout.NORTH);
        pnlBacTren.add(pnlChuaNutToa, BorderLayout.CENTER);

        // --- Left Panel (Information) ---
        pnlBenTrai = new JPanel(new BorderLayout()); 
        pnlBenTrai.setPreferredSize(new Dimension(300, 0));
        pnlBenTrai.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(mauChinh, 1), "THÔNG TIN CHỌN", 
            TitledBorder.CENTER, TitledBorder.TOP));
        
        JEditorPane areaThongTin = new JEditorPane(); 
        areaThongTin.setContentType("text/html");
        areaThongTin.setEditable(false);
        areaThongTin.setBackground(Color.WHITE);
        areaThongTin.setText("<html><body style='font-family: Segoe UI; font-size: 12pt; color: #222222;'>Chuyến: " 
                            + chuyenTauDuocChon.getMaChuyenTau()
                            + "<br><br>Trắng: Trống<br>Đỏ: Đã đặt<br>Xanh lá: Đã chọn</body></html>");
        pnlBenTrai.add(new JScrollPane(areaThongTin), BorderLayout.CENTER); 

        // --- Seat Display Panel (Center) ---
        pnlHienThiCho = new JPanel(new BorderLayout()); 
        pnlHienThiCho.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel lblKhoiTao = new JLabel("Chọn toa để hiển thị chỗ ngồi.", SwingConstants.CENTER); 
        pnlHienThiCho.add(lblKhoiTao, BorderLayout.CENTER); 
        
        JPanel pnlTrungTam = new JPanel(new BorderLayout(10, 10));
        pnlTrungTam.add(pnlBenTrai, BorderLayout.WEST);
        pnlTrungTam.add(pnlHienThiCho, BorderLayout.CENTER);

        // --- Footer Buttons ---
        JPanel pnlChan = new JPanel(new BorderLayout()); 
        pnlChan.setBackground(mauNenChan);
        pnlChan.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        nutDatVe = new JButton("Tiếp Tục Thanh Toán"); 
        nutDatVe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nutDatVe.setBackground(new Color(103, 192, 144));
        nutDatVe.setForeground(Color.WHITE);
        nutDatVe.setPreferredSize(new Dimension(200, 36));
        nutDatVe.setEnabled(false); 

        nutTroVe = new JButton("Trở Về"); 
        nutTroVe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nutTroVe.setBackground(new Color(220, 20, 60)); 
        nutTroVe.setForeground(Color.WHITE);
        nutTroVe.setPreferredSize(new Dimension(150, 36));
        
        pnlChan.add(nutTroVe, BorderLayout.WEST); 
        pnlChan.add(nutDatVe, BorderLayout.EAST); 

        pnlChinh = new JPanel(new BorderLayout()); 
        pnlChinh.add(pnlBacTren, BorderLayout.NORTH); 
        pnlChinh.add(pnlTrungTam, BorderLayout.CENTER); 
        pnlChinh.add(pnlChan, BorderLayout.SOUTH); 
        add(pnlChinh);
    }
    
    private void thietLapSuKien() { 
        nutDatVe.addActionListener(this);
        nutTroVe.addActionListener(this);
        // Gán sự kiện cho các nút toa (được thực hiện sau khi taiDanhSachToa hoàn tất)
    }

    // TẢI DỮ LIỆU THỰC TỪ DAO
    private void taiDanhSachToa(String maTau) throws SQLException {
        // Lấy dữ liệu thực từ DAO
        List<ToaTau> danhSachToa = toaTauDAO.getToaTauByMaTau(maTau);  // Uncomment dòng này

        pnlChuaNutToa.removeAll();
        mapMaToa.clear();  // Có thể xóa dòng này nếu không cần map nữa, hoặc giữ để populate sau

        if (danhSachToa.isEmpty()) {
            pnlChuaNutToa.add(new JLabel("Không tìm thấy toa tàu."));
            return;
        }

        // Populate map và tạo nút (giữ nguyên phần còn lại)
        for (ToaTau toa : danhSachToa) {
            mapMaToa.put(toa.getMaToa(), toa);
            JButton btnToa = new JButton(toa.getMaToa());
            btnToa.setBackground(Color.WHITE);
            btnToa.setForeground(mauChinh);
            btnToa.addActionListener(e -> xuLyChonToa(toa.getMaToa()));
            pnlChuaNutToa.add(btnToa);
        }
        pnlChuaNutToa.revalidate();
        pnlChuaNutToa.repaint();
    }

    private String layLoaiCho(String maToa, BigDecimal heSoGia) { 
        // Logic xác định loại chỗ dựa trên hệ số giá [5]
        if (heSoGia == null) return "Ghế Thường";
        if (heSoGia.doubleValue() >= 2.0) return "Giường Nằm"; // [6] TOA-03, TOA-04 có HeSoGia=2.0
        if (heSoGia.doubleValue() >= 1.1) return "Ghế Mềm"; // [6] TOA-02 có HeSoGia=1.1
        return "Ghế Thường";
    }

    private void xuLyChonToa(String maToa) { 
        ToaTau toa = mapMaToa.get(maToa);
        if (toa == null) return;
        try {
            // 1. GỌI DAO ĐỂ TẢI DANH SÁCH CHỖ NGỒI VÀ TRẠNG THÁI
            List<ChoNgoi> danhSachCho = choNgoiDAO.getSeatsByMaToa(toa.getMaToa()); // Dữ liệu thực [9]
            String loai = layLoaiCho(toa.getMaToa(), toa.getHeSoGia());
            
            // 2. TẠO PANEL HIỂN THỊ
            JPanel pnlHienThi = loai.contains("Giường") ? taoPanelGiuong(toa, danhSachCho) : taoPanelGhe(toa, danhSachCho);
            
            pnlHienThiCho.removeAll();
            pnlHienThiCho.add(pnlHienThi, BorderLayout.CENTER);
            pnlHienThiCho.revalidate();
            pnlHienThiCho.repaint(); 

            // 3. Cập nhật số chỗ trống ban đầu
            soChoTrongBanDau = 0;
            if (danhSachCho != null) {
                soChoTrongBanDau = (int) danhSachCho.stream().filter(c -> {
                    String trangThai = (c.getTrangThai() != null) ? c.getTrangThai().trim() : "";
                    return !"Đã đặt".equalsIgnoreCase(trangThai);
                }).count();
            }
            capNhatThongTin(toa.getMaToa(), toa.getSoLuongCho(), loai, soChoTrongBanDau); 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải chỗ ngồi: " + ex.getMessage(), "Lỗi CSDL", JOptionPane.ERROR_MESSAGE); 
        }
    }

    private JPanel taoPanelGhe(ToaTau toa, List<ChoNgoi> danhSachCho) { 
        JPanel panelToa = new JPanel(new BorderLayout());
        String loai = layLoaiCho(toa.getMaToa(), toa.getHeSoGia());
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            new LineBorder(mauChinh, 2), toa.getMaToa() + " - " + loai,
            TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 18), mauChinh); 
        panelToa.setBorder(titledBorder);
        
        JPanel pnlGhe = new JPanel(new GridLayout(SO_HANG, SO_COT, 10, 10));
        pnlGhe.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Mapping chỗ ngồi vào lưới 2D
        ChoNgoi[][] choTheoViTri = new ChoNgoi[SO_HANG][SO_COT];
        if (danhSachCho != null) {
             int idx = 0;
             for (int c = 0; c < SO_COT; c++) {
                 for (int r = 0; r < SO_HANG; r++) {
                    if (idx < danhSachCho.size()) {
                        choTheoViTri[r][c] = danhSachCho.get(idx++);
                    }
                 }
             }
        }
        
        // --- LÕI HIỂN THỊ TRẠNG THÁI ---
        for (int r = 0; r < SO_HANG; r++) { 
            for (int c = 0; c < SO_COT; c++) {
                JButton nut = new JButton();
                nut.setPreferredSize(new Dimension(50, 40));
                nut.setFont(new Font("Segoe UI", Font.BOLD, 12));
                
                ChoNgoi cho = choTheoViTri[r][c];

                if (cho != null) { 
                    String maCN = cho.getMaChoNgoi();
                    nut.setText(maCN.substring(maCN.lastIndexOf('-') + 1));
                    
                    String trangThai = (cho.getTrangThai() != null) ? cho.getTrangThai().trim() : ""; 
                    
                    if ("Đã đặt".equalsIgnoreCase(trangThai)) { 
                        nut.setBackground(Color.RED);
                        nut.setForeground(Color.WHITE);  
                        nut.setEnabled(false); // <--- KHÓA VÀ TÔ MÀU ĐỎ
                    } else {
                        Color mauMacDinh = Color.WHITE;
                        Color mauDaChon = Color.GREEN;
                        Color initial = danhSachGheDuocChon.contains(cho) ? mauDaChon : mauMacDinh; 
                        nut.setBackground(initial);
                        nut.addActionListener(new TrinhLangNgheChonGhe(cho, nut, mauMacDinh, mauDaChon)); 
                    }
                } else {
                    nut.setText("X");
                    nut.setBackground(Color.LIGHT_GRAY); 
                    nut.setEnabled(false); 
                }
                pnlGhe.add(nut);
            }
        }
        panelToa.add(new JScrollPane(pnlGhe), BorderLayout.CENTER);
        return panelToa; 
    }
    
    private JPanel taoPanelGiuong(ToaTau toa, List<ChoNgoi> danhSachCho) {
        JPanel panelToa = new JPanel(new BorderLayout());
        TitledBorder titledBorder = new TitledBorder(new LineBorder(mauChinh, 2), toa.getMaToa() + " - GIƯỜNG NẰM", TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 18), mauChinh);
        panelToa.setBorder(titledBorder);
        
        int giuongMoiTangMoiKhoang = 2;
        int soKhoang = toa.getSoLuongCho() / 4; 

        JPanel pnlKhoang = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        int idx = 0;
        
        for (int k = 0; k < soKhoang; k++) { // Duyệt qua các khoang
            JPanel pnlMotKhoang = new JPanel(new BorderLayout()); 
            pnlMotKhoang.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1), "Khoang " + (k + 1)));
            
            JPanel pnlGiuongGrid = new JPanel(new GridLayout(2, 2, 6, 6)); 
            
            // Tầng: 1 (trên) -> 0 (dưới)
            for (int tang = 1; tang >= 0; tang--) { 
                for (int vt = 0; vt < giuongMoiTangMoiKhoang; vt++) { 
                    ChoNgoi cho = (idx < danhSachCho.size()) ? danhSachCho.get(idx++) : null;
                    
                    JButton nut = new JButton();
                    nut.setPreferredSize(new Dimension(60, 44));
                    nut.setFont(new Font("Segoe UI", Font.BOLD, 10));

                    if (cho != null) { 
                        nut.setText(cho.getMaChoNgoi().substring(cho.getMaChoNgoi().lastIndexOf('-') + 1)); 
                        String trangThai = (cho.getTrangThai() != null) ? cho.getTrangThai().trim() : ""; 

                        if ("Đã đặt".equalsIgnoreCase(trangThai)) { 
                            nut.setBackground(Color.RED);
                            nut.setForeground(Color.WHITE);
                            nut.setEnabled(false); 
                        } else {
                            Color mauMacDinh = Color.WHITE;
                            Color mauDaChon = Color.GREEN;
                            Color initial = danhSachGheDuocChon.contains(cho) ? mauDaChon : mauMacDinh;
                            nut.setBackground(initial);
                            nut.addActionListener(new TrinhLangNgheChonGhe(cho, nut, mauMacDinh, mauDaChon)); 
                        }
                    } else {
                        nut.setBackground(Color.LIGHT_GRAY);
                        nut.setEnabled(false);
                    }
                    pnlGiuongGrid.add(nut);
                }
            }
            pnlMotKhoang.add(pnlGiuongGrid, BorderLayout.CENTER); 
            pnlKhoang.add(pnlMotKhoang);
        }
        
        JScrollPane scroll = new JScrollPane(pnlKhoang);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        panelToa.add(scroll, BorderLayout.CENTER);
        return panelToa;
    }


    private void capNhatThongTin(String maToa, int soLuong, String loai, int soTrong) {
        int soChonTrongToa = (int) danhSachGheDuocChon.stream().filter(c ->
        c.getToaTau().getMaToa().equals(maToa)).count();
        
        Component[] comp = pnlBenTrai.getComponents();
        for (Component c : comp) {
            if (c instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) c;
                if (scroll.getViewport().getView() instanceof JEditorPane) {
                    JEditorPane ed = (JEditorPane) scroll.getViewport().getView();
                    
                    String maChuyen = chuyenTauDuocChon.getMaChuyenTau();
                    String mauXanhLaDam = "#4A8C67"; 
                    String mauDo = "#FF0000";

                    StringBuilder sb = new StringBuilder("<html><body style='font-family: Segoe UI; font-size: 12pt; color: #222222;'>");
                    sb.append("Chuyến: <font color='").append(mauXanhLaDam).append("'>").append(maChuyen).append("</font><br>");
                    sb.append("Toa: <font color='").append(mauXanhLaDam).append("'>").append(maToa).append("</font><br>");
                    sb.append("Loại: <font color='").append(mauXanhLaDam).append("'>").append(loai).append(" (").append(soLuong).append(" chỗ)</font><br>");
                    sb.append("Số chỗ trống: <font color='").append(mauDo).append("'>").append(soTrong).append("</font><br>");
                    
                    sb.append("<br>Số ghế đang chọn: <font color='green'>").append(danhSachGheDuocChon.size()).append("</font><br>");
                    sb.append("Chỗ: ");
                    if (danhSachGheDuocChon.isEmpty()) {
                        sb.append("Không");
                    } else {
                        sb.append(danhSachGheDuocChon.stream()
                                .map(ChoNgoi::getMaChoNgoi)
                                .collect(Collectors.joining(", ")));
                    }
                    sb.append("<br><br>Màu hiển thị:<br>");
                    sb.append("Đỏ: Đã đặt<br>Trắng: Trống<br>Xanh lá: Đã chọn");
                    
                    ed.setText(sb.toString());
                }
            }
        }
        pnlBenTrai.revalidate();
        pnlBenTrai.repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e) { 
        Object nguon = e.getSource();
        if (nguon == nutDatVe) {
            if (danhSachGheDuocChon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn chỗ ngồi.", "Cảnh báo", JOptionPane.WARNING_MESSAGE); 
                return;
            }
            
            // Chuyển sang màn hình nhập thông tin khách hàng (FormDatVe/ThongTinKhachHangGUI) [7]
            SwingUtilities.invokeLater(() -> {
                // Giả định chuyển sang màn hình tiếp theo
                new ThongTinKhachHangGUI(chuyenTauDuocChon, danhSachGheDuocChon, nhanVienHienTai).setVisible(true);
                this.dispose();
            });
        } else if (nguon == nutTroVe) {
            SwingUtilities.invokeLater(() -> {
                if (previousScreen != null) {
                    previousScreen.setVisible(true); 
                }
                this.dispose();
            });
        }
    }
    
    /**
     * Inner class xử lý sự kiện click trên từng nút ghế/giường.
     */
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
            if (nut.getBackground().equals(mauMacDinh)) {
                nut.setBackground(mauDaChon);
                danhSachGheDuocChon.add(cho);
            } else {
                nut.setBackground(mauMacDinh);
                danhSachGheDuocChon.remove(cho);
            }
            nutDatVe.setEnabled(!danhSachGheDuocChon.isEmpty());

            ToaTau toa = cho.getToaTau();
            if (toa != null) {
                capNhatThongTin(toa.getMaToa(), toa.getSoLuongCho(), layLoaiCho(toa.getMaToa(), toa.getHeSoGia()), soChoTrongBanDau);
            }
        }
    }
}
