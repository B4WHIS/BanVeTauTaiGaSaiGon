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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class LapHoaDonGUI extends JFrame implements ActionListener {
    
    private final Color MAU_CHINH = new Color(74, 140, 103); //xanhladam
    private final Color MAU_PHU = new Color(229, 115, 115); // do 
    private final Color MAU_CHU_DE = new Color(93, 156, 236); // xanhduong
    private final Color MAU_LUU = new Color(41, 128, 185); //xanh bien
    private final Color MAU_IN = new Color(241, 196, 15); //vang
    private final Color MAU_NHAN_BANG = MAU_CHINH.darker().darker();
    private final DateTimeFormatter DINH_DANG_NGAY_GIO =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final Font PHONG_CHU_NHAN = new Font("Segoe UI", Font.BOLD, 15);
    private final Font PHONG_CHU_GIA_TRI = new Font("Segoe UI", Font.PLAIN, 15);
    private final Font PHONG_CHU_TONG_KET = new Font("Segoe UI", Font.BOLD, 20);
    private JLabel lblTieuDeChinh;
    private JPanel pnlThongTinKetHop;
    
    private JLabel lblMaHoaDon;
    private JLabel lblNgayLap;
    private JLabel lblNhanVienLap;
    private JLabel lblTenKhachHang;
    private JLabel lblSoDienThoai;
    private JLabel lblMaKhuyenMai;
    private JLabel lblTongTienVe;
    private JLabel lblThueVAT;
    private JLabel lblTongThanhToan;
    
    private JTable bangChiTiet;
    private DefaultTableModel moHinhBang;
    private JPanel pnlKhungBangChiTiet;
    
    private JButton btnTroVe;
    private JButton btnLuuGiaoDich;
    private JButton btnInHoaDon;
    private JPanel pnlChucNang;

    public LapHoaDonGUI() {
        setTitle("Lập Hóa Đơn Bán Vé Tàu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        //tieu de chinh
        lblTieuDeChinh = new JLabel("HÓA ĐƠN DỊCH VỤ VẬN TẢI ĐƯỜNG SẮT", SwingConstants.CENTER);
        lblTieuDeChinh.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTieuDeChinh.setForeground(MAU_CHINH);
        
        JPanel pnlKhungTieuDe = new JPanel(new BorderLayout());
        pnlKhungTieuDe.setBorder(new EmptyBorder(15, 20, 15, 20));
        pnlKhungTieuDe.setBackground(new Color(225, 242, 232)); // Màu nền Header
        pnlKhungTieuDe.add(lblTieuDeChinh, BorderLayout.CENTER);
        this.getContentPane().add(pnlKhungTieuDe, BorderLayout.NORTH);

        //pnl thong tin
        pnlThongTinKetHop = new JPanel(new BorderLayout());
        pnlThongTinKetHop.setBackground(Color.WHITE);
        pnlThongTinKetHop.setPreferredSize(new Dimension(450, 0));
        
        // TitledBorder cho khung thông tin
        pnlThongTinKetHop.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(MAU_CHINH, 2),
            "THÔNG TIN CHUNG & TÀI CHÍNH",
            TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 18), MAU_CHINH)
        );
        
        //noi dung chinh
        JPanel pnlLuoi = new JPanel(new GridBagLayout());
        pnlLuoi.setBackground(Color.WHITE);
        pnlLuoi.setBorder(new EmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 10);
        int dong = 0;
        
        // font
        Font fontNhan = PHONG_CHU_NHAN;
        Font fontGiaTri = PHONG_CHU_GIA_TRI;
        Font fontTongKet = PHONG_CHU_TONG_KET;
        
        //data khach hang
        // Hàng 0: Mã HĐ
        gbc.gridx = 0;
        gbc.gridy = dong;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0; gbc.gridwidth = 1;
        
        JLabel lblTieuDeMaHD = new JLabel("Mã HĐ:"); 
        lblTieuDeMaHD.setFont(fontNhan);
        pnlLuoi.add(lblTieuDeMaHD, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = dong++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        lblMaHoaDon = new JLabel("[HDXXXXXX]");
        lblMaHoaDon.setFont(fontGiaTri);
        lblMaHoaDon.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlLuoi.add(lblMaHoaDon, gbc);
        
        // Hàng 1: Ngày Lập
        gbc.gridx = 0;
        gbc.gridy = dong; 
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.weightx = 0.0;
        
        JLabel lblTieuDeNgayLap = new JLabel("Ngày Lập:"); 
        lblTieuDeNgayLap.setFont(fontNhan);
        pnlLuoi.add(lblTieuDeNgayLap, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = dong++; 
        gbc.anchor = GridBagConstraints.EAST; 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.weightx = 1.0;
        
        lblNgayLap = new JLabel("[dd/MM/yyyy HH:mm]"); 
        lblNgayLap.setFont(fontGiaTri); 
        lblNgayLap.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlLuoi.add(lblNgayLap, gbc);
        
        // Hàng 2: Nhân viên Lập
        gbc.gridx = 0; 
        gbc.gridy = dong; 
        gbc.anchor = GridBagConstraints.WEST; 
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        
        JLabel lblTieuDeNVLap = new JLabel("Nhân viên lập:"); 
        lblTieuDeNVLap.setFont(fontNhan);
        pnlLuoi.add(lblTieuDeNVLap, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = dong++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        lblNhanVienLap = new JLabel("[Họ Tên NV]"); 
        lblNhanVienLap.setFont(fontGiaTri); lblNhanVienLap.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlLuoi.add(lblNhanVienLap, gbc);
        
        // Thêm dải phân cách
//        gbc.gridx = 0; 
//        gbc.gridy = dong++;
//        gbc.gridwidth = 2;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        JPanel phanCach = new JPanel();
//        phanCach.setBackground(Color.LIGHT_GRAY);
//        phanCach.setPreferredSize(new Dimension(0, 1));
//        phanCach.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
//        pnlLuoi.add(phanCach, gbc);
        
        // Hàng 4: Họ tên KH
        gbc.gridx = 0;
        gbc.gridy = dong; 
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.weightx = 0.0; gbc.gridwidth = 1;
        
        JLabel lblTieuDeTenKH = new JLabel("Họ tên KH:");
        lblTieuDeTenKH.setFont(fontNhan);
        pnlLuoi.add(lblTieuDeTenKH, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = dong++; 
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        lblTenKhachHang = new JLabel("[Tên HK]");
        lblTenKhachHang.setFont(fontGiaTri);
        lblTenKhachHang.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlLuoi.add(lblTenKhachHang, gbc);
        
        // Hàng 5: SĐT
        gbc.gridx = 0; 
        gbc.gridy = dong; 
        gbc.anchor = GridBagConstraints.WEST; 
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JLabel lblTieuDeSDT = new JLabel("Số ĐT:"); 
        lblTieuDeSDT.setFont(fontNhan);
        pnlLuoi.add(lblTieuDeSDT, gbc);
        
        gbc.gridx = 1; 
        gbc.gridy = dong++;
        gbc.anchor = GridBagConstraints.EAST; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        lblSoDienThoai = new JLabel("[SĐT]");
        lblSoDienThoai.setFont(fontGiaTri); 
        lblSoDienThoai.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlLuoi.add(lblSoDienThoai, gbc);
        
        // Hàng 6: Mã Khuyến Mãi
        gbc.gridx = 0; 
        gbc.gridy = dong;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        
        JLabel lblTieuDeMaKM = new JLabel("Mã Khuyến Mãi:"); 
        lblTieuDeMaKM.setFont(fontNhan);
        pnlLuoi.add(lblTieuDeMaKM, gbc);
        
        gbc.gridx = 1; 
        gbc.gridy = dong++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.weightx = 1.0;
        
        lblMaKhuyenMai = new JLabel("[Mã/Không]");
        lblMaKhuyenMai.setFont(fontGiaTri);
        lblMaKhuyenMai.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlLuoi.add(lblMaKhuyenMai, gbc);
        

        // tong ket
        
        // Hàng 7: Tổng tiền vé sau Ưu đãi cá nhân, trước KM và VAT
        gbc.gridx = 0; 
        gbc.gridy = dong;
        gbc.anchor = GridBagConstraints.WEST; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.weightx = 0.0; 
        gbc.gridwidth = 1;
        JLabel lblTieuDeTongVe = new JLabel("TỔNG TIỀN VÉ (Chưa Thuế):");
        lblTieuDeTongVe.setFont(fontNhan);
        pnlLuoi.add(lblTieuDeTongVe, gbc);
        
        gbc.gridx = 1; 
        gbc.gridy = dong++; 
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        lblTongTienVe = new JLabel("0.00 VNĐ");
        lblTongTienVe.setFont(fontGiaTri); 
        lblTongTienVe.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlLuoi.add(lblTongTienVe, gbc);
        
        // Hàng 8: Thuế VAT
        gbc.gridx = 0;
        gbc.gridy = dong; 
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JLabel lblTieuDeVAT = new JLabel("THUẾ VAT (10%):");
        lblTieuDeVAT.setFont(fontNhan);
        pnlLuoi.add(lblTieuDeVAT, gbc);
        
        gbc.gridx = 1; 
        gbc.gridy = dong++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        lblThueVAT = new JLabel("0.00 VNĐ");
        lblThueVAT.setFont(fontGiaTri);
        lblThueVAT.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlLuoi.add(lblThueVAT, gbc);
        
        // Hàng 9: Tổng cộng phải thu (Màu đỏ)
        gbc.gridx = 0;
        gbc.gridy = dong;
        gbc.anchor = GridBagConstraints.WEST; 
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        
        JLabel lblTieuDeTongCong = new JLabel("TỔNG CỘNG PHẢI THU:");
        lblTieuDeTongCong.setFont(fontTongKet);
        lblTieuDeTongCong.setForeground(MAU_PHU);
        
        pnlLuoi.add(lblTieuDeTongCong, gbc);
        
        gbc.gridx = 1; 
        gbc.gridy = dong++;
        gbc.anchor = GridBagConstraints.EAST; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        lblTongThanhToan = new JLabel("0.00 VNĐ");
        lblTongThanhToan.setFont(fontTongKet);
        lblTongThanhToan.setForeground(MAU_PHU);
        lblTongThanhToan.setHorizontalAlignment(SwingConstants.RIGHT);
        
        pnlLuoi.add(lblTongThanhToan, gbc);
        
        pnlThongTinKetHop.add(pnlLuoi, BorderLayout.NORTH);

        // bang chi tiet center
        pnlKhungBangChiTiet = new JPanel(new BorderLayout());
        pnlKhungBangChiTiet.setBackground(Color.WHITE);
        
        //titledBorder cho bảng
        pnlKhungBangChiTiet.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(MAU_CHINH, 2),
            "CHI TIẾT CÁC VÉ ĐÃ THANH TOÁN (Line Items)",
            TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 18), MAU_CHINH)
        );
        
        // Khởi tạo bảng
        String[] tieuDeCot = {"STT", "Mã Vé", "Chuyến Tàu", "Ga Đi - Đến", "Toa/Ghế", "Giá (VNĐ)"};
        moHinhBang = new DefaultTableModel(tieuDeCot, 0) {
            @Override
            public boolean isCellEditable(int dong, int cot) {
                return false;
            }
        };
        
        bangChiTiet = new JTable(moHinhBang);
        bangChiTiet.setRowHeight(30);
        bangChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bangChiTiet.setShowGrid(true);
        bangChiTiet.setGridColor(new Color(240, 240, 240));
        // Đảm bảo màu sắc đồng bộ
        bangChiTiet.setSelectionBackground(MAU_NHAN_BANG);
        bangChiTiet.setSelectionForeground(Color.WHITE);

        JTableHeader tieuDeBang = bangChiTiet.getTableHeader();
        tieuDeBang.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tieuDeBang.setBackground(new Color(245, 245, 245));
        
        JScrollPane thanhCuonBang = new JScrollPane(bangChiTiet);
        thanhCuonBang.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        pnlKhungBangChiTiet.add(thanhCuonBang, BorderLayout.CENTER);

        //pnl nut
        pnlChucNang = new JPanel(new BorderLayout());
        pnlChucNang.setBackground(MAU_CHINH);
        pnlChucNang.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // btn tro ve
        btnTroVe = new JButton("Trở Về", GiaoDienChinh.chinhKichThuoc("/img/undo.png", 25, 25));
        btnTroVe.setBackground(MAU_PHU.darker());
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnTroVe.setFocusPainted(false);
        btnTroVe.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnTroVe.setPreferredSize(new Dimension(150, 45));
        btnTroVe.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnTroVe.setIconTextGap(10);
        
        // luu va in
        JPanel pnlLuuVaIn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlLuuVaIn.setOpaque(false);

        //luu giao dich
        btnLuuGiaoDich = new JButton("Lưu Giao Dịch", GiaoDienChinh.chinhKichThuoc("/img/mark.png", 25, 25));
        btnLuuGiaoDich.setBackground(MAU_LUU);
        btnLuuGiaoDich.setForeground(Color.WHITE);
        btnLuuGiaoDich.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLuuGiaoDich.setFocusPainted(false);
        btnLuuGiaoDich.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnLuuGiaoDich.setPreferredSize(new Dimension(180, 45));
        btnLuuGiaoDich.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnLuuGiaoDich.setIconTextGap(10);

        // in
        btnInHoaDon = new JButton("In Hóa Đơn", GiaoDienChinh.chinhKichThuoc("/img/print.png", 25, 25));
        btnInHoaDon.setBackground(MAU_IN);
        btnInHoaDon.setForeground(Color.WHITE);
        btnInHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnInHoaDon.setFocusPainted(false);
        btnInHoaDon.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnInHoaDon.setPreferredSize(new Dimension(180, 45));
        btnInHoaDon.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnInHoaDon.setIconTextGap(10);
        
        pnlLuuVaIn.add(btnLuuGiaoDich);
        pnlLuuVaIn.add(btnInHoaDon);

        pnlChucNang.add(btnTroVe, BorderLayout.WEST);
        pnlChucNang.add(pnlLuuVaIn, BorderLayout.EAST);

        // layout bc chinh
        JPanel pnlChinh = new JPanel(new BorderLayout(20, 10));
        pnlChinh.setBackground(Color.WHITE);
        pnlChinh.setBorder(new EmptyBorder(10, 20, 10, 20));

        //thông tin bên trái
        pnlChinh.add(pnlThongTinKetHop, BorderLayout.WEST);
        
        //Bảng chi tiết centre
        JPanel pnlNoiDungPhai = new JPanel(new BorderLayout(0, 0));
        pnlNoiDungPhai.setBackground(Color.WHITE);
        pnlNoiDungPhai.add(pnlKhungBangChiTiet, BorderLayout.CENTER);
        pnlChinh.add(pnlNoiDungPhai, BorderLayout.CENTER);
        
        
        this.getContentPane().add(pnlChinh, BorderLayout.CENTER);
        this.getContentPane().add(pnlChucNang, BorderLayout.SOUTH);

        //sukien
        btnTroVe.addActionListener(this);
        btnLuuGiaoDich.addActionListener(this);
        btnInHoaDon.addActionListener(this);
        
        // GIỮ LIỆU GIẢ: Tải dữ liệu mẫu để hiển thị
        String maHD = "HD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "001";
        String tenNV = "Trần Thị B (NVBánVé)";
        String tenHK = "Nguyễn Văn Khách";
        String sdt = "0901234567";
        String maKhuyenMai = "KMTEST001 (10%)";
        
        // GIỮ LIỆU GIẢ: Tính toán mẫu (Giảm 10% do KM, cộng VAT 10%) [6]
        BigDecimal tongTienGoc = new BigDecimal("1500000.00");
        BigDecimal giaSauUuDai = tongTienGoc.multiply(new BigDecimal("0.90"));
        BigDecimal thueVAT = giaSauUuDai.multiply(new BigDecimal("0.1"));
        BigDecimal tongThanhToan = giaSauUuDai.add(thueVAT);

        // Cập nhật thông tin chung
        lblMaHoaDon.setText(maHD);
        lblNgayLap.setText(LocalDateTime.now().format(DINH_DANG_NGAY_GIO));
        lblNhanVienLap.setText(tenNV);
        lblTenKhachHang.setText(tenHK);
        lblSoDienThoai.setText(sdt);
        lblMaKhuyenMai.setText(maKhuyenMai);

        // GIỮ LIỆU GIẢ: Cập nhật bảng chi tiết
        moHinhBang.setRowCount(0);
        moHinhBang.addRow(new Object[]{
            1, "VE001", "SE1 (SG-HN)", "Ga Sài Gòn - Ga Đà Nẵng", "TOA05/A-01", "800,000"
        });
        moHinhBang.addRow(new Object[]{
            2, "VE002", "SE1 (SG-HN)", "Ga Sài Gòn - Ga Đà Nẵng", "TOA05/A-02", "700,000"
        });

        // Cập nhật tổng kết
        lblTongTienVe.setText(String.format("%,.2f VNĐ", giaSauUuDai));
        lblThueVAT.setText(String.format("%,.2f VNĐ", thueVAT));
        lblTongThanhToan.setText(String.format("%,.2f VNĐ", tongThanhToan));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();
        
        if (nguon == btnTroVe) {
            this.dispose();
        }
        else if (nguon == btnLuuGiaoDich) {
            JOptionPane.showMessageDialog(this,
                "Đã lưu giao dịch hóa đơn vào hệ thống thành công (Blue button).",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
        else if (nguon == btnInHoaDon) {
            JOptionPane.showMessageDialog(this,
                "Đã gửi lệnh in Hóa đơn (Yellow button). Chức năng in ấn đang được phát triển...",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LapHoaDonGUI().setVisible(true);
        });
    }
}	