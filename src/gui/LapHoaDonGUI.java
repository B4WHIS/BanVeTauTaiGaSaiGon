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

/**
 * Giao diện Lập Hóa Đơn (LapHoaDonGUI)
 * Đồng bộ UI với TraCuuChuyenTauGUI (màu xanh lá: 74, 140, 103).
 * Bố cục: [Thông tin HĐ, KH & Tài chính (Tây)] | [Chi tiết Vé (Trung tâm)].
 * Loại bỏ metadata khỏi Title lớn và hợp nhất tất cả thông tin vào cột trái.
 */
public class LapHoaDonGUI extends JFrame implements ActionListener {

    // ===== KHAI BÁO CÁC THÀNH PHẦN GIAO DIỆN (Đảm bảo tên tiếng Việt) =====
    
    // Màu sắc đồng bộ
    private final Color MAU_CHINH = new Color(74, 140, 103); // Xanh đậm chủ đạo
    private final Color MAU_PHU = new Color(229, 115, 115); // Đỏ/Cam (Accent/Cảnh báo)
    private final Color MAU_CHU_DE = new Color(93, 156, 236); // Xanh dương
    private final Color MAU_LUU = new Color(41, 128, 185); // Xanh biển (Blue) cho nút Lưu
    private final Color MAU_IN = new Color(241, 196, 15); // Vàng cho nút In
    private final Color MAU_TRONGLAP = MAU_CHU_DE.darker().darker(); // Màu xanh đậm cho select table

    // Tiêu đề chính
    private JLabel lblTieuDeChinh;

    // Panel nội dung bên trái đã hợp nhất (R1, R2, R3)
    private JPanel pnlThongTinVaTongTien; 
    
    // Các Label hiển thị giá trị (Value Labels)
    private JLabel lblMaHoaDon;
    private JLabel lblNgayLap;
    private JLabel lblNhanVienLap;
    private JLabel lblTenKhachHang;
    private JLabel lblSoDienThoai;
    private JLabel lblMaUuDai; 
    private JLabel lblTongTienVe;
    private JLabel lblThueVAT;
    private JLabel lblTongThanhToan; // Highlight màu đỏ

    // Bảng chi tiết Vé
    private JTable tblChiTiet;
    private DefaultTableModel moHinhBang;
    private JPanel pnlBangChiTietWrapper; 
    
    // Nút chức năng (Footer)
    private JButton btnTroVe;
    private JButton btnLuuGiaoDich; 
    private JButton btnInHoaDon;   
    private JPanel pnlChucNangDuoi;

    // Định dạng ngày giờ
    private final DateTimeFormatter DINH_DANG_NGAY_GIO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public LapHoaDonGUI() {
        // Cấu hình JFrame cơ bản
        setTitle("Lập Hóa Đơn Bán Vé Tàu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        
        // Khởi tạo và thiết lập bố cục
        khoiTaoThanhPhan();
        thietLapBoCuc();

        // Gán sự kiện
        ganSuKien();
        
        // Giả lập load data
        taiDuLieuMau();
    }
    
    /**
     * Khởi tạo các thành phần giao diện (UI Components)
     */
    private void khoiTaoThanhPhan() {
        // --- 1. Tiêu đề chính (NORTH) ---
        lblTieuDeChinh = new JLabel("HÓA ĐƠN DỊCH VỤ VẬN TẢI ĐƯỜNG SẮT", SwingConstants.CENTER);
        lblTieuDeChinh.setFont(new Font("Segoe UI", Font.BOLD, 40)); 
        lblTieuDeChinh.setForeground(MAU_CHINH); 
        
        JPanel pnlTitleWrapper = new JPanel(new BorderLayout());
        pnlTitleWrapper.setBorder(new EmptyBorder(15, 20, 15, 20));
        pnlTitleWrapper.setBackground(new Color(225, 242, 232)); 
        pnlTitleWrapper.add(lblTieuDeChinh, BorderLayout.CENTER);
        this.getContentPane().add(pnlTitleWrapper, BorderLayout.NORTH);

        // --- 2. Panel Thông tin & Tổng tiền Hợp nhất (PHẦN TRÁI) --- (R1, R2, R3)
        pnlThongTinVaTongTien = taoPanelKetHop();

        // --- 3. Bảng chi tiết vé (PHẦN GIỮA/PHẢI) ---
        pnlBangChiTietWrapper = taoPanelBangChiTiet(); 

        // --- 4. Panel Nút chức năng (SOUTH - Footer) ---
        pnlChucNangDuoi = taoPanelChucNang();
    }
    
    /**
     * Helper: Tạo dải phân cách ngang mỏng.
     */
    private JPanel taoPhanCach() {
        JPanel separator = new JPanel();
        separator.setBackground(Color.LIGHT_GRAY); 
        separator.setPreferredSize(new Dimension(0, 1));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1)); 
        return separator;
    }

    /**
     * Helper: Tạo panel chứa thông tin Khách hàng VÀ Tổng tiền (Hợp nhất theo yêu cầu R2, R3)
     */
    private JPanel taoPanelKetHop() {
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setBackground(Color.WHITE);
        pnlWrapper.setPreferredSize(new Dimension(450, 0)); 
        
        // Thiết lập TitledBorder DUY NHẤT
        pnlWrapper.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(MAU_CHINH, 2), 
            "THÔNG TIN CHUNG & TÀI CHÍNH", 
            TitledBorder.CENTER, 
            TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 18), 
            MAU_CHINH)
        );
        
        // --- Khối nội dung chính (Grid) ---
        JPanel pnlGrid = new JPanel(new GridBagLayout());
        pnlGrid.setBackground(Color.WHITE);
        pnlGrid.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontValue = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontValueHighlight = new Font("Segoe UI", Font.BOLD, 16);
        Font fontFinal = new Font("Segoe UI", Font.BOLD, 18);
        
        int row = 0;

        // --- KHỐI METADATA & KHÁCH HÀNG ---
        
        // Hàng 0: Mã HĐ
        pnlGrid.add(taoLabel("Mã HĐ:"), taoGBC(gbc, 0, row, fontLabel, GridBagConstraints.WEST));
        lblMaHoaDon = taoValueLabel("[HDXXXXXX]", fontValue);
        pnlGrid.add(lblMaHoaDon, taoGBC(gbc, 1, row++, fontValue, GridBagConstraints.EAST));

        // Hàng 1: Ngày Lập
        pnlGrid.add(taoLabel("Ngày Lập:"), taoGBC(gbc, 0, row, fontLabel, GridBagConstraints.WEST));
        lblNgayLap = taoValueLabel("[dd/MM/yyyy HH:mm]", fontValue);
        pnlGrid.add(lblNgayLap, taoGBC(gbc, 1, row++, fontValue, GridBagConstraints.EAST));

        // Hàng 2: Nhân viên Lập
        pnlGrid.add(taoLabel("Nhân viên lập:"), taoGBC(gbc, 0, row, fontLabel, GridBagConstraints.WEST));
        lblNhanVienLap = taoValueLabel("[Họ Tên NV]", fontValue);
        pnlGrid.add(lblNhanVienLap, taoGBC(gbc, 1, row++, fontValue, GridBagConstraints.EAST));
        
        pnlGrid.add(taoPhanCach(), taoGBC(gbc, 0, row++, 2, 0)); // Phân cách
        
        // Hàng 4: Họ tên KH
        pnlGrid.add(taoLabel("Họ tên KH:"), taoGBC(gbc, 0, row, fontLabel, GridBagConstraints.WEST));
        lblTenKhachHang = taoValueLabel("[Tên HK]", fontValue);
        pnlGrid.add(lblTenKhachHang, taoGBC(gbc, 1, row++, fontValue, GridBagConstraints.EAST));

        // Hàng 5: SĐT
        pnlGrid.add(taoLabel("Số ĐT:"), taoGBC(gbc, 0, row, fontLabel, GridBagConstraints.WEST));
        lblSoDienThoai = taoValueLabel("[SĐT]", fontValue);
        pnlGrid.add(lblSoDienThoai, taoGBC(gbc, 1, row++, fontValue, GridBagConstraints.EAST));
        
        // Hàng 6: Mã Ưu đãi (Khuyến mãi) - R1
        pnlGrid.add(taoLabel("Mã Khuyến Mãi:"), taoGBC(gbc, 0, row, fontLabel, GridBagConstraints.WEST));
        lblMaUuDai = taoValueLabel("[Mã/Không]", fontValue);
        pnlGrid.add(lblMaUuDai, taoGBC(gbc, 1, row++, fontValue, GridBagConstraints.EAST));

        pnlGrid.add(taoPhanCach(), taoGBC(gbc, 0, row++, 2, 0)); // Phân cách
        
        // --- KHỐI TỔNG KẾT TÀI CHÍNH --- (R3)
        
        // Hàng 7: Tổng tiền vé
        pnlGrid.add(taoLabel("TỔNG TIỀN VÉ (Chưa Thuế):"), taoGBC(gbc, 0, row, fontValueHighlight, GridBagConstraints.WEST));
        lblTongTienVe = taoValueLabel("0.00 VNĐ", fontValueHighlight);
        pnlGrid.add(lblTongTienVe, taoGBC(gbc, 1, row++, fontValueHighlight, GridBagConstraints.EAST));
        
        // Hàng 8: Thuế VAT
        pnlGrid.add(taoLabel("THUẾ VAT (10%):"), taoGBC(gbc, 0, row, fontValueHighlight, GridBagConstraints.WEST));
        lblThueVAT = taoValueLabel("0.00 VNĐ", fontValueHighlight);
        pnlGrid.add(lblThueVAT, taoGBC(gbc, 1, row++, fontValueHighlight, GridBagConstraints.EAST));

        // Hàng 9: Tổng cộng phải thu (Red highlight)
        JLabel tdTongCong = taoLabel("TỔNG CỘNG PHẢI THU:");
        tdTongCong.setForeground(MAU_PHU); // Màu đỏ (R3)
        pnlGrid.add(tdTongCong, taoGBC(gbc, 0, row, fontFinal, GridBagConstraints.WEST));
        
        lblTongThanhToan = taoValueLabel("0.00 VNĐ", fontFinal);
        lblTongThanhToan.setForeground(MAU_PHU); // Màu đỏ (R3)
        pnlGrid.add(lblTongThanhToan, taoGBC(gbc, 1, row++, fontFinal, GridBagConstraints.EAST));


        pnlWrapper.add(pnlGrid, BorderLayout.NORTH); // Giữ pnlGrid ở phía trên
        
        return pnlWrapper;
    }
    
    /**
     * Helper: Tạo JLabel thông thường
     */
    private JLabel taoLabel(String text) {
        JLabel lbl = new JLabel(text);
        return lbl;
    }

    /**
     * Helper: Tạo JLabel hiển thị giá trị, áp dụng font và căn phải (EAST)
     */
    private JLabel taoValueLabel(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        return lbl;
    }
    
    /**
     * Helper: Tạo GridBagConstraints
     */
    private GridBagConstraints taoGBC(GridBagConstraints gbc, int x, int y, Font font, int anchor) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = anchor;
        // Đặt trọng số để cột giá trị (x=1) chiếm hết phần còn lại
        gbc.weightx = (x == 1) ? 1.0 : 0.0; 
        gbc.gridwidth = 1;
        gbc.fill = (x == 1) ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
        return gbc;
    }

    /**
     * Helper: Tạo GridBagConstraints cho phần tử chiếm 2 cột
     */
    private GridBagConstraints taoGBC(GridBagConstraints gbc, int x, int y, int width, int weightx) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.weightx = weightx;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        return gbc;
    }

    /**
     * Helper: Tạo panel chứa Bảng chi tiết Vé với TitledBorder (Phần Trung tâm)
     */
    private JPanel taoPanelBangChiTiet() {
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setBackground(Color.WHITE);
        pnlWrapper.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(MAU_CHINH, 2), 
            "CHI TIẾT CÁC VÉ ĐÃ THANH TOÁN (Line Items)", 
            TitledBorder.CENTER, 
            TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 18), 
            MAU_CHINH)
        );

        String[] tieuDeCot = {"STT", "Mã Vé", "Chuyến Tàu", "Ga Đi - Đến", "Toa/Ghế", "Giá (VNĐ)"};
        moHinhBang = new DefaultTableModel(tieuDeCot, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tblChiTiet = new JTable(moHinhBang);
        tblChiTiet.setRowHeight(30);
        tblChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblChiTiet.setShowGrid(true);
        tblChiTiet.setGridColor(new Color(240, 240, 240)); 
        
        tblChiTiet.setSelectionBackground(MAU_TRONGLAP); 
        tblChiTiet.setSelectionForeground(Color.WHITE);
        
        JTableHeader tieuDeBang = tblChiTiet.getTableHeader();
        tieuDeBang.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tieuDeBang.setBackground(new Color(245, 245, 245));
        
        JScrollPane thanhCuonBang = new JScrollPane(tblChiTiet);
        thanhCuonBang.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        pnlWrapper.add(thanhCuonBang, BorderLayout.CENTER);
        return pnlWrapper;
    }

    /**
     * Helper: Tạo Panel chức năng (Footer) và thiết lập vị trí/màu (Lưu Xanh biển, In Vàng)
     */
    private JPanel taoPanelChucNang() {
        JPanel pnlFooter = new JPanel(new BorderLayout()); 
        pnlFooter.setBackground(MAU_CHINH); 
        pnlFooter.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // Nút Trở Về (WEST)
        btnTroVe = GiaoDienChinh.taoButton("Trở Về", MAU_PHU.darker(), "/img/undo.png"); 
        btnTroVe.setPreferredSize(new Dimension(150, 45));

        // Nút Lưu & In (EAST)
        JPanel pnlLuuVaIn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0)); 
        pnlLuuVaIn.setOpaque(false);
        
        // Nút Lưu Giao Dịch (Xanh biển)
        btnLuuGiaoDich = GiaoDienChinh.taoButton("Lưu Giao Dịch", MAU_LUU, "/img/mark.png"); 
        btnLuuGiaoDich.setPreferredSize(new Dimension(180, 45));
        
        // Nút In Hóa Đơn (Vàng)
        btnInHoaDon = GiaoDienChinh.taoButton("In Hóa Đơn", MAU_IN, "/img/print.png"); 
        btnInHoaDon.setPreferredSize(new Dimension(180, 45));
        
        pnlLuuVaIn.add(btnLuuGiaoDich);
        pnlLuuVaIn.add(btnInHoaDon);

        pnlFooter.add(btnTroVe, BorderLayout.WEST);
        pnlFooter.add(pnlLuuVaIn, BorderLayout.EAST);
        
        return pnlFooter;
    }


    /**
     * Xây dựng bố cục chính (Layout)
     */
    private void thietLapBoCuc() {
        // Panel nội dung bên phải (Bảng chi tiết)
        JPanel pnlRightContent = new JPanel(new BorderLayout());
        pnlRightContent.setBackground(Color.WHITE);
        pnlRightContent.setBorder(new EmptyBorder(0, 10, 0, 10)); 
        
        pnlRightContent.add(pnlBangChiTietWrapper, BorderLayout.CENTER); 

        // Panel Body chính (Chứa Left Info và Right Content)
        JPanel pnlBody = new JPanel(new BorderLayout(20, 10)); 
        pnlBody.setBackground(Color.WHITE);
        pnlBody.setBorder(new EmptyBorder(10, 20, 10, 20));

        // 1. Khối thông tin bên trái đã hợp nhất (WEST)
        pnlBody.add(pnlThongTinVaTongTien, BorderLayout.WEST);
        
        // 2. Bảng chi tiết (CENTER)
        pnlBody.add(pnlRightContent, BorderLayout.CENTER);

        // Gắn Body và Footer vào Frame
        this.getContentPane().add(pnlBody, BorderLayout.CENTER);        
        this.getContentPane().add(pnlChucNangDuoi, BorderLayout.SOUTH); 
    }

    /**
     * Gán sự kiện cho các nút bấm
     */
    private void ganSuKien() {
        btnTroVe.addActionListener(this);
        btnLuuGiaoDich.addActionListener(this);
        btnInHoaDon.addActionListener(this);
    }

    /**
     * Phương thức giả lập tải dữ liệu cho hóa đơn
     */
    private void taiDuLieuMau() {
        // Dữ liệu mẫu
        String maHD = "HD20251025001";
        String tenNV = "Trần Thị B (NVBánVé)";
        String tenHK = "Nguyễn Văn Khách";
        String sdt = "0901234567";
        String maKhuyenMai = "KMTEST001 (10%)"; // Dữ liệu Mã Khuyến Mãi (R1)
        
        // Tính toán mẫu
        BigDecimal tongTienGoc = new BigDecimal("1500000.00");
        // Giả sử mã khuyến mãi giảm 10% trên tổng tiền gốc
        BigDecimal giaSauUuDai = tongTienGoc.multiply(new BigDecimal("0.90"));
        BigDecimal thueVAT = giaSauUuDai.multiply(new BigDecimal("0.1"));
        BigDecimal tongThanhToan = giaSauUuDai.add(thueVAT);
        
        // Cập nhật thông tin chung
        lblMaHoaDon.setText(maHD);
        lblNgayLap.setText(LocalDateTime.now().format(DINH_DANG_NGAY_GIO));
        lblNhanVienLap.setText(tenNV);
        lblTenKhachHang.setText(tenHK);
        lblSoDienThoai.setText(sdt);
        lblMaUuDai.setText(maKhuyenMai); // Mã Khuyến Mãi (R1)

        // Cập nhật bảng chi tiết
        moHinhBang.setRowCount(0);
        moHinhBang.addRow(new Object[]{
            1, "VE001", "SE1 (SG-HN)", "Ga Sài Gòn - Ga Đà Nẵng", "TOA05/A-01", "800,000"
        });
        moHinhBang.addRow(new Object[]{
            2, "VE002", "SE1 (SG-HN)", "Ga Sài Gòn - Ga Đà Nẵng", "TOA05/A-02", "700,000"
        });
        
        // Cập nhật tổng kết (R3)
        lblTongTienVe.setText(String.format("%,.2f VNĐ", giaSauUuDai));
        lblThueVAT.setText(String.format("%,.2f VNĐ", thueVAT));
        lblTongThanhToan.setText(String.format("%,.2f VNĐ", tongThanhToan));
    }

    /**
     * Xử lý khi nút được nhấn
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        if (src == btnTroVe) {
            this.dispose();
        } 
        else if (src == btnLuuGiaoDich) {
            xuLyLuuHoaDon();
        } 
        else if (src == btnInHoaDon) {
            xuLyInHoaDon();
        }
    }

    /**
     * Xử lý chức năng Lưu Hóa đơn/Giao dịch.
     */
    private void xuLyLuuHoaDon() {
        // Logic nghiệp vụ lưu hóa đơn vào DB [1]
        JOptionPane.showMessageDialog(this, 
            "Đã lưu giao dịch hóa đơn vào hệ thống thành công (Blue button).", 
            "Thông báo", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Xử lý chức năng In Hóa đơn.
     * Chức năng in ấn thường sử dụng `Printable interface` trong Java [2, 3].
     */
    private void xuLyInHoaDon() {
        JOptionPane.showMessageDialog(this, 
            "Đã gửi lệnh in Hóa đơn (Yellow button).", 
            "Thông báo", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    // Phương thức main để test giao diện
    public static void main(String[] args) {
        // Thiết lập Look and Feel
        LookAndFeelManager.setNimbusLookAndFeel(); 
        SwingUtilities.invokeLater(() -> {
            new LapHoaDonGUI().setVisible(true);
        });
    }
}