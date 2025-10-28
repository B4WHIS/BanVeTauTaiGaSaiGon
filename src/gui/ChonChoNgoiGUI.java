// File: src/gui/ChonChoNgoiGUI.java
package gui;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;

import dao.*;
import entity.*;

public class ChonChoNgoiGUI extends JFrame implements ActionListener {
    private ChuyenTau chuyenTauDuocChon;
    private NhanVien nhanVienHienTai;
    private Ve veCu;
    private TraCuuChuyenTauGUI previousScreen;

    private ToaTauDAO toaTauDAO = new ToaTauDAO();
    private ChoNgoiDAO choNgoiDAO = new ChoNgoiDAO();
    private VeDAO veDAO = new VeDAO();
    private DoiVeDAO doiVeDAO = new DoiVeDAO();
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
    private int soChoTrongBanDau = 0;
    private final int SO_HANG = 4;
    private final int SO_COT = 10;

    
    // Constructor 4 tham số – KHÔNG throws
    public ChonChoNgoiGUI(ChuyenTau chuyentau, NhanVien nv, TraCuuChuyenTauGUI previous, Ve veCu) {
        this.chuyenTauDuocChon = chuyentau;
        this.nhanVienHienTai = nv != null ? nv : new NhanVien("NV-001");
        this.previousScreen = previous;
        this.veCu = veCu;

        khoiTaoGiaoDien();

        try {
            layDuLieuChoToaTau();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu toa tàu: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        thietLapSuKien();
        nutDatVe.setEnabled(false);

        if (veCu != null) {
            setTitle("Đổi chỗ ngồi cho vé " + veCu.getMaVe());
        }
    }

    private void khoiTaoGiaoDien() {
        setTitle("Chọn chỗ ngồi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        pnlChinh = new JPanel(new BorderLayout(12, 12));
        pnlChinh.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(pnlChinh);

        lblTieuDe = new JLabel("CHỌN CHỖ NGỒI", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTieuDe.setForeground(mauChinh);
        pnlChinh.add(lblTieuDe, BorderLayout.NORTH);

        pnlBenTrai = new JPanel(new BorderLayout(10, 10));
        pnlBenTrai.setPreferredSize(new Dimension(200, 0));
        pnlBenTrai.setBorder(BorderFactory.createTitledBorder("Chọn toa tàu"));
        pnlChinh.add(pnlBenTrai, BorderLayout.WEST);

        pnlChuaNutToa = new JPanel(new GridLayout(0, 1, 10, 10));
        pnlBenTrai.add(new JScrollPane(pnlChuaNutToa), BorderLayout.CENTER);

        pnlHienThiCho = new JPanel(new BorderLayout());
        pnlHienThiCho.setBorder(BorderFactory.createTitledBorder("Sơ đồ chỗ ngồi"));
        pnlChinh.add(pnlHienThiCho, BorderLayout.CENTER);

        JPanel pnlNutChucNang = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        nutTroVe = new JButton("Trở về", chinhKichThuoc("/img/quay_lai.png", 20, 20));
        nutTroVe.setBackground(new Color(41, 128, 185));
        nutTroVe.setForeground(Color.WHITE);
        nutTroVe.setFont(new Font("Segoe UI", Font.BOLD, 14));

        nutDatVe = new JButton(veCu != null ? "Đổi vé" : "Đặt vé", chinhKichThuoc("/img/ve.png", 20, 20));
        nutDatVe.setBackground(mauChinh);
        nutDatVe.setForeground(Color.WHITE);
        nutDatVe.setFont(new Font("Segoe UI", Font.BOLD, 14));

        pnlNutChucNang.add(nutTroVe);
        pnlNutChucNang.add(nutDatVe);
        pnlChinh.add(pnlNutChucNang, BorderLayout.SOUTH);
    }
    private void layDuLieuChoToaTau() throws SQLException {
        String maTau = chuyenTauDuocChon.getMaTau();
        List<ToaTau> danhSachToa = toaTauDAO.getAllToaTauByMaTau(maTau);
        soChoTrongBanDau = danhSachToa.stream().mapToInt(ToaTau::getSoLuongCho).sum();

        pnlChuaNutToa.removeAll();
        for (ToaTau toa : danhSachToa) {
            String maToa = toa.getMaToa();
            mapMaToa.put(maToa, toa);

            JButton nutToa = new JButton("Toa " + toa.getSoThuTu());
            nutToa.setBackground(mauChinh);
            nutToa.setForeground(Color.WHITE);
            nutToa.setFont(new Font("Segoe UI", Font.BOLD, 14));
            nutToa.addActionListener(e -> {
                try {
                    hienThiChoNgoi(maToa);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi tải chỗ ngồi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
            pnlChuaNutToa.add(nutToa);
        }
        pnlChuaNutToa.revalidate();
        pnlChuaNutToa.repaint();

        if (!danhSachToa.isEmpty()) {
            hienThiChoNgoi(danhSachToa.get(0).getMaToa());
        }
    }
    private void hienThiChoNgoi(String maToa) throws SQLException {
        pnlHienThiCho.removeAll();

        ToaTau toa = mapMaToa.get(maToa);
        if (toa == null) return;

        int tongCho = toa.getSoLuongCho();
        int loaiCho = layLoaiCho(maToa, toa.getHeSoGia());
        capNhatThongTin(maToa, tongCho, loaiCho, soChoTrongBanDau);

        List<ChoNgoi> danhSachCho = choNgoiDAO.getChoNgoiByToa(maToa);
        JPanel pnlSoDo = new JPanel(new GridLayout(SO_HANG, SO_COT, 5, 5));
        pnlSoDo.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlSoDo.setBackground(mauNenChan);

        for (int i = 0; i < SO_HANG * SO_COT; i++) {
            if (i < danhSachCho.size()) {
                ChoNgoi cho = danhSachCho.get(i);
                JButton nutCho = new JButton(cho.getMaChoNgoi().substring(cho.getMaChoNgoi().length() - 2));
                nutCho.setFont(new Font("Segoe UI", Font.BOLD, 12));

                String trangThai = cho.getTrangThai();
                Color mauNen = trangThai.equals("Trống") ? Color.GREEN : Color.RED;
                nutCho.setBackground(mauNen);
                nutCho.setForeground(Color.WHITE);

                if (trangThai.equals("Trống")) {
                    nutCho.addActionListener(new TrinhLangNgheChonGhe(cho, nutCho, mauNen, Color.BLUE));
                } else {
                    nutCho.setEnabled(false);
                }
                pnlSoDo.add(nutCho);
            } else {
                pnlSoDo.add(new JLabel());
            }
        }

        pnlHienThiCho.add(new JScrollPane(pnlSoDo), BorderLayout.CENTER);
        pnlHienThiCho.revalidate();
        pnlHienThiCho.repaint();
    }

    private int layLoaiCho(String maToa, BigDecimal heSoGia) {
        if (heSoGia == null) return 1;
        if (heSoGia.compareTo(new BigDecimal("1.0")) == 0) return 1; // Ghế cứng
        if (heSoGia.compareTo(new BigDecimal("1.5")) == 0) return 2; // Ghế mềm
        if (heSoGia.compareTo(new BigDecimal("2.0")) == 0) return 3; // Giường nằm
        return 1;
    }

    private void capNhatThongTin(String maToa, int tongCho, int loaiCho, int tongChoTrong) {
        JEditorPane ep = new JEditorPane();
        ep.setContentType("text/html");
        ep.setText("<html><b>Toa:</b> " + maToa + "<br>" +
                "<b>Loại chỗ:</b> " + loaiCho + "<br>" +
                "<b>Tổng chỗ:</b> " + tongCho + "<br>" +
                "<b>Chỗ trống toàn tàu:</b> " + tongChoTrong + "</html>");
        ep.setEditable(false);
        ep.setBorder(new LineBorder(Color.BLACK));
        pnlHienThiCho.add(ep, BorderLayout.NORTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();
        if (nguon == nutDatVe) {
            if (danhSachGheDuocChon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một chỗ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (veCu != null) {
                if (danhSachGheDuocChon.size() != 1) {
                    JOptionPane.showMessageDialog(this, "Chỉ chọn một chỗ để đổi vé!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                ChoNgoi gheMoi = danhSachGheDuocChon.get(0);
                try {
                    HanhKhach hanhKhach = veCu.getMaHanhkhach(); // Lấy từ vé cũ (giả sử getter đúng)
                    boolean thanhCong = doiVeDAO.doiVe(veCu, chuyenTauDuocChon, gheMoi, nhanVienHienTai, hanhKhach);
                    if (thanhCong) {
                        BigDecimal phiPhat = tinhPhiDoiTuDAO(veCu);
                        BigDecimal giaGheMoi = layGiaVeCoBan(gheMoi);
                        BigDecimal giaVeCu = veCu.getGiaVeGoc();
                        BigDecimal chenhLech = giaGheMoi.subtract(giaVeCu).max(BigDecimal.ZERO);
                        BigDecimal phiDoiTong = phiPhat.add(chenhLech);

                        // Cập nhật vé cũ
                        veCu.setMaChoNgoi(gheMoi);
                        veCu.setMaChuyenTau(chuyenTauDuocChon);
                        veCu.setGiaThanhToan(veCu.getGiaThanhToan().add(chenhLech));
                        veCu.setTrangThai("Đã đổi");

                        // Tạo vé hiển thị
                        Ve veHienThi = new Ve();
                        veHienThi.setMaVe(veCu.getMaVe());
                        veHienThi.setGiaThanhToan(phiDoiTong);
                        veHienThi.setMaHanhkhach(veCu.getMaHanhkhach());
                        veHienThi.setMaChoNgoi(gheMoi);
                        veHienThi.setMaChuyenTau(chuyenTauDuocChon);
                        veHienThi.setTrangThai("Đã đổi");

                        List<Ve> dsVe = new ArrayList<>();
                        dsVe.add(veHienThi);

                        // MỞ THANH TOÁN, KHÔNG HIỆN POPUP
                        new ThanhToanGUI(dsVe, veCu.getMaHanhkhach(), nhanVienHienTai).setVisible(true);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Đổi vé thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi đổi vé: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Giữ nguyên code cho đặt vé mới (không đổi vé)
                SwingUtilities.invokeLater(() -> {
                    new ThongTinKhachHangGUI(chuyenTauDuocChon, danhSachGheDuocChon, nhanVienHienTai).setVisible(true);
                    this.dispose();
                });
            }
        } else if (nguon == nutTroVe) {
            SwingUtilities.invokeLater(() -> {
                if (previousScreen != null) {
                    previousScreen.setVisible(true);
                }
                this.dispose();
            });
        }
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
            if (veCu != null && danhSachGheDuocChon.size() >= 1 && nut.getBackground().equals(mauMacDinh)) {
                return;
            }
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

    private void thietLapSuKien() {
        nutDatVe.addActionListener(this);
        nutTroVe.addActionListener(this);
    }
    private BigDecimal tinhPhiDoiTuDAO(Ve veCu) {
       
        return veCu.getGiaVeGoc().multiply(new BigDecimal("0.1")).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal layGiaVeCoBan(ChoNgoi gheMoi) {
        if (gheMoi == null || chuyenTauDuocChon == null) {
            return BigDecimal.ZERO;
        }

        // 1. Lấy giá chuyến tàu
        BigDecimal giaChuyen = chuyenTauDuocChon.getGiaChuyen();
        if (giaChuyen == null || giaChuyen.compareTo(BigDecimal.ZERO) <= 0) {
            giaChuyen = new BigDecimal("500000"); // Giá mặc định nếu DB lỗi
        }

        // 2. Lấy hệ số toa (từ ToaTau)
        ToaTau toa = gheMoi.getToaTau();
        BigDecimal heSoToa = BigDecimal.ONE;
        if (toa != null && toa.getHeSoGia() != null) {
            heSoToa = toa.getHeSoGia();
        }

        // 3. Lấy hệ số loại ghế theo IDloaiGhe (HARDCODE - vì DB không có)
        BigDecimal heSoLoaiGhe = switch (gheMoi.getIDloaiGhe()) {
            case 1 -> new BigDecimal("1.0");   // Ghế cứng
            case 2 -> new BigDecimal("1.5");   // Ghế mềm
            case 3 -> new BigDecimal("2.0");   // Giường nằm cứng
            case 4 -> new BigDecimal("3.0");   // Giường nằm mềm
            // Thêm các loại khác nếu cần
            default -> BigDecimal.ONE;
        };

        // 4. Tính và làm tròn
        return giaChuyen
                .multiply(heSoToa)
                .multiply(heSoLoaiGhe)
                .setScale(0, BigDecimal.ROUND_HALF_UP);
    }
    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
        URL iconUrl = ChonChoNgoiGUI.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}