package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import entity.Ve;

public class ThanhToanGUI extends JFrame implements ActionListener {

    private final Color mauChinh = new Color(74, 140, 103);
    private final Color mauPhu = new Color(229, 115, 115);
    private final Color mauXanhDuong = new Color(93, 156, 236);
    private final Color mauNenHeader = new Color(225, 242, 232);
    private final Color mauTongKet = new Color(200, 30, 30);

    private final Font phongChuNhan = new Font("Segoe UI", Font.BOLD, 15);
    private final Font phongChuGiaTri = new Font("Segoe UI", Font.PLAIN, 15);
    private final Font phongChuTongKet = new Font("Segoe UI", Font.BOLD, 20);

    private JPanel khungChinh; 
    private JLabel lblTieuDeChinh;

    private JPanel pnlThongTinThanhToan;
    private JTextField txtMaGiaoDich;
    private JTextField txtTenKhachHang;

    private JTextField txtTongTienVeGoc;
    private JTextField txtThueVAT;
    private JTextField txtTongThanhToanCuoi;

    private JTextField txtMaGiamGia;
    private JButton nutApDungMa;
    private JTextField txtGiaTriGiamKM;

    private JRadioButton radTienMat;
    private JRadioButton radChuyenKhoan;
    private ButtonGroup nhomPhuongThuc;

    // Bảng chi tiết vé
    private JTable bangChiTiet;
    private DefaultTableModel moHinhBang;

    // Footer
    private JPanel pnlThaoTac;
    private JButton nutQuayLai;
    private JButton nutThanhToan;
    private JButton nutLapHoaDon;

    private List<Ve> danhSachVe; 
    private BigDecimal tongTienCanThanhToan;
    private BigDecimal tongGiamGiaHienTai = BigDecimal.ZERO; 

    public ThanhToanGUI() {
        setTitle("Thanh Toán Giao Dịch Bán Vé");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        //tieu de
        lblTieuDeChinh = new JLabel("TIẾN HÀNH THANH TOÁN", SwingConstants.CENTER);
        lblTieuDeChinh.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTieuDeChinh.setForeground(mauChinh);

        JPanel pnlTieuDe = new JPanel(new BorderLayout());
        pnlTieuDe.setBorder(new EmptyBorder(15, 20, 15, 20));
        pnlTieuDe.setBackground(mauNenHeader);
        pnlTieuDe.add(lblTieuDeChinh, BorderLayout.CENTER);
        this.getContentPane().add(pnlTieuDe, BorderLayout.NORTH);

        // pnl trai
        pnlThongTinThanhToan = new JPanel(new BorderLayout());
        JPanel pnlNhapLieu = new JPanel(new GridBagLayout());
        pnlNhapLieu.setBackground(Color.WHITE);
        pnlNhapLieu.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int dong = 0;

        // Mã giao dịch
        JLabel lblMaGD = new JLabel("Mã Giao Dịch:");
        lblMaGD.setFont(phongChuNhan);
        txtMaGiaoDich = new JTextField("GDXXXXXX", 20);
        txtMaGiaoDich.setEditable(false);
        txtMaGiaoDich.setBackground(Color.WHITE);
        txtMaGiaoDich.setHorizontalAlignment(SwingConstants.RIGHT);
        txtMaGiaoDich.setFont(phongChuGiaTri);

        gbc.gridx = 0; gbc.gridy = dong; pnlNhapLieu.add(lblMaGD, gbc);
        gbc.gridx = 1; gbc.gridy = dong++; pnlNhapLieu.add(txtMaGiaoDich, gbc);

        // Tên khách hàng
        JLabel lblTenKH = new JLabel("Khách Hàng:");
        lblTenKH.setFont(phongChuNhan);
        txtTenKhachHang = new JTextField("[Tên Khách Hàng]", 20);
        txtTenKhachHang.setEditable(false);
        txtTenKhachHang.setBackground(Color.WHITE);
        txtTenKhachHang.setHorizontalAlignment(SwingConstants.RIGHT);
        txtTenKhachHang.setFont(phongChuGiaTri);

        gbc.gridx = 0; gbc.gridy = dong; pnlNhapLieu.add(lblTenKH, gbc);
        gbc.gridx = 1; gbc.gridy = dong++; pnlNhapLieu.add(txtTenKhachHang, gbc);

        // Phân cách
        JPanel phanCach1 = new JPanel();
        phanCach1.setBackground(Color.LIGHT_GRAY);
        phanCach1.setPreferredSize(new Dimension(0, 1));
        gbc.gridx = 0; gbc.gridy = dong++; gbc.gridwidth = 2;
        pnlNhapLieu.add(phanCach1, gbc);
        gbc.gridwidth = 1;

        // Mã khuyến mãi
        JLabel lblMaKM = new JLabel("Mã Khuyến Mãi:");
        lblMaKM.setFont(phongChuNhan);
        txtMaGiamGia = new JTextField(15);
        txtMaGiamGia.setFont(phongChuGiaTri);
        nutApDungMa = GiaoDienChinh.taoButton("Áp Dụng", mauXanhDuong, "/img/tick.png");
        nutApDungMa.setPreferredSize(new Dimension(100, 30));
        JPanel pnlMaKM = new JPanel(new BorderLayout(5, 0));
        pnlMaKM.setOpaque(false);
        pnlMaKM.add(txtMaGiamGia, BorderLayout.CENTER);
        pnlMaKM.add(nutApDungMa, BorderLayout.EAST);

        gbc.gridx = 0; gbc.gridy = dong; pnlNhapLieu.add(lblMaKM, gbc);
        gbc.gridx = 1; gbc.gridy = dong++; pnlNhapLieu.add(pnlMaKM, gbc);

        // Giá trị giảm KM
        JLabel lblGiaTriKM = new JLabel("Giá Trị Giảm KM:");
        lblGiaTriKM.setFont(phongChuNhan);
        txtGiaTriGiamKM = new JTextField("0.00 VNĐ", 20);
        txtGiaTriGiamKM.setEditable(false);
        txtGiaTriGiamKM.setBackground(Color.WHITE);
        txtGiaTriGiamKM.setHorizontalAlignment(SwingConstants.RIGHT);
        txtGiaTriGiamKM.setFont(phongChuGiaTri);
        txtGiaTriGiamKM.setForeground(mauPhu);

        gbc.gridx = 0; gbc.gridy = dong; pnlNhapLieu.add(lblGiaTriKM, gbc);
        gbc.gridx = 1; gbc.gridy = dong++; pnlNhapLieu.add(txtGiaTriGiamKM, gbc);

        // Phân cách
        JPanel phanCach2 = new JPanel();
        phanCach2.setBackground(Color.LIGHT_GRAY);
        phanCach2.setPreferredSize(new Dimension(0, 1));
        gbc.gridx = 0; gbc.gridy = dong++; gbc.gridwidth = 2;
        pnlNhapLieu.add(phanCach2, gbc);
        gbc.gridwidth = 1;

        // Tổng tiền vé gốc
        JLabel lblTongGoc = new JLabel("Tổng Tiền Vé Gốc:");
        lblTongGoc.setFont(phongChuNhan);
        txtTongTienVeGoc = new JTextField("0.00 VNĐ", 20);
        txtTongTienVeGoc.setEditable(false);
        txtTongTienVeGoc.setBackground(Color.WHITE);
        txtTongTienVeGoc.setHorizontalAlignment(SwingConstants.RIGHT);
        txtTongTienVeGoc.setFont(phongChuGiaTri);

        gbc.gridx = 0; gbc.gridy = dong; pnlNhapLieu.add(lblTongGoc, gbc);
        gbc.gridx = 1; gbc.gridy = dong++; pnlNhapLieu.add(txtTongTienVeGoc, gbc);

        // Thuế VAT
        JLabel lblVAT = new JLabel("Thuế VAT (10%):");
        lblVAT.setFont(phongChuNhan);
        txtThueVAT = new JTextField("0.00 VNĐ", 20);
        txtThueVAT.setEditable(false);
        txtThueVAT.setBackground(Color.WHITE);
        txtThueVAT.setHorizontalAlignment(SwingConstants.RIGHT);
        txtThueVAT.setFont(phongChuGiaTri);

        gbc.gridx = 0; gbc.gridy = dong; pnlNhapLieu.add(lblVAT, gbc);
        gbc.gridx = 1; gbc.gridy = dong++; pnlNhapLieu.add(txtThueVAT, gbc);

        // Phân cách
        JPanel phanCach3 = new JPanel();
        phanCach3.setBackground(mauChinh);
        phanCach3.setPreferredSize(new Dimension(0, 2));
        gbc.gridx = 0; gbc.gridy = dong++; gbc.gridwidth = 2;
        pnlNhapLieu.add(phanCach3, gbc);
        gbc.gridwidth = 1;

        // Tổng cộng
        JLabel lblTongCong = new JLabel("TỔNG CỘNG:");
        lblTongCong.setFont(phongChuTongKet);
        lblTongCong.setForeground(mauTongKet);
        txtTongThanhToanCuoi = new JTextField("0.00 VNĐ", 20);
        txtTongThanhToanCuoi.setEditable(false);
        txtTongThanhToanCuoi.setBackground(Color.WHITE);
        txtTongThanhToanCuoi.setHorizontalAlignment(SwingConstants.RIGHT);
        txtTongThanhToanCuoi.setFont(phongChuTongKet);
        txtTongThanhToanCuoi.setForeground(mauTongKet);

        gbc.gridx = 0; gbc.gridy = dong; pnlNhapLieu.add(lblTongCong, gbc);
        gbc.gridx = 1; gbc.gridy = dong++; pnlNhapLieu.add(txtTongThanhToanCuoi, gbc);

        // Phương thức thanh toán
        JLabel lblPTTT = new JLabel("P.Thức TT:");
        lblPTTT.setFont(phongChuNhan);
        radTienMat = new JRadioButton("Tiền Mặt");
        radChuyenKhoan = new JRadioButton("Chuyển Khoản");
        radTienMat.setBackground(Color.WHITE);
        radChuyenKhoan.setBackground(Color.WHITE);
        radTienMat.setSelected(true);

        nhomPhuongThuc = new ButtonGroup();
        nhomPhuongThuc.add(radTienMat);
        nhomPhuongThuc.add(radChuyenKhoan);

        JPanel pnlPhuongThuc = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlPhuongThuc.setOpaque(false);
        pnlPhuongThuc.add(radTienMat);
        pnlPhuongThuc.add(radChuyenKhoan);

        gbc.gridx = 0; gbc.gridy = dong; pnlNhapLieu.add(lblPTTT, gbc);
        gbc.gridx = 1; gbc.gridy = dong++; pnlNhapLieu.add(pnlPhuongThuc, gbc);

        pnlThongTinThanhToan.add(pnlNhapLieu, BorderLayout.NORTH);
        pnlThongTinThanhToan.setBorder(BorderFactory.createTitledBorder(new LineBorder(mauChinh, 2),
                "THÔNG TIN GIAO DỊCH", TitledBorder.CENTER, TitledBorder.TOP, phongChuTongKet, mauChinh));

        // bảng chir tiết
        moHinhBang = new DefaultTableModel(
                new String[]{"STT", "Mã Vé", "Chuyến/Tàu", "Ga Đi - Đến", "Loại HK/Ưu đãi", "Giá TT (VNĐ)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        bangChiTiet = new JTable(moHinhBang);
        bangChiTiet.setRowHeight(30);
        bangChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bangChiTiet.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        JScrollPane thanhCuonBang = new JScrollPane(bangChiTiet);
        JPanel pnlBangChiTiet = new JPanel(new BorderLayout());
        pnlBangChiTiet.setBorder(BorderFactory.createTitledBorder(new LineBorder(mauChinh, 2),
                "CHI TIẾT VÉ & ƯU ĐÃI KHÁCH HÀNG", TitledBorder.CENTER, TitledBorder.TOP, phongChuTongKet, mauChinh));
        pnlBangChiTiet.add(thanhCuonBang, BorderLayout.CENTER);

        // footer
        nutQuayLai = GiaoDienChinh.taoButton("Quay Lại", mauPhu.darker(), "/img/undo.png");
        nutThanhToan = GiaoDienChinh.taoButton("Thanh Toán", mauChinh.darker(), "/img/mark.png");
        nutLapHoaDon = GiaoDienChinh.taoButton("Lập Hóa Đơn", mauXanhDuong, "/img/receipt.png");

        JPanel pnlNutPhai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlNutPhai.setOpaque(false);
        pnlNutPhai.add(nutLapHoaDon);
        pnlNutPhai.add(nutThanhToan);

        pnlThaoTac = new JPanel(new BorderLayout());
        pnlThaoTac.setBackground(mauChinh);
        pnlThaoTac.setBorder(new EmptyBorder(10, 20, 10, 20));
        pnlThaoTac.add(nutQuayLai, BorderLayout.WEST);
        pnlThaoTac.add(pnlNutPhai, BorderLayout.EAST);

        // add gd chinh
        JPanel pnlNoiDung = new JPanel(new BorderLayout(20, 10));
        pnlNoiDung.setBackground(Color.WHITE);
        pnlNoiDung.add(pnlThongTinThanhToan, BorderLayout.WEST);
        pnlNoiDung.add(pnlBangChiTiet, BorderLayout.CENTER);
        this.getContentPane().add(pnlNoiDung, BorderLayout.CENTER);
        this.getContentPane().add(pnlThaoTac, BorderLayout.SOUTH);

        //skien
        nutQuayLai.addActionListener(this);
        nutThanhToan.addActionListener(this);
        nutLapHoaDon.addActionListener(this);
        nutApDungMa.addActionListener(this);

        
    }

   

    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();
        if (nguon == nutQuayLai) this.dispose();
        else if (nguon == nutThanhToan) {
        	
        }
        else if (nguon == nutLapHoaDon) {
        	
        }
        else if (nguon == nutApDungMa) {
        	
        }
    }

    public static void main(String[] args) {
        LookAndFeelManager.setNimbusLookAndFeel();
        SwingUtilities.invokeLater(() -> new ThanhToanGUI().setVisible(true));
    }
    
    
}
