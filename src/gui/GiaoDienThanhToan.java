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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import control.QuanLyHoaDonControl;
import control.QuanLyVeControl;
import dao.HanhKhachDAO;
import dao.VeDAO;
import entity.ChuyenTau;
import entity.HanhKhach;
import entity.HoaDon;
import entity.NhanVien;
import entity.Ve;

public class GiaoDienThanhToan extends JFrame implements ActionListener {

   
    private final Color mauChinh = new Color(74, 140, 103);
    private final Color mauTongKet = new Color(200, 30, 30);
    private final Color MAU_NUT_QUAYLAI = new Color(0, 128, 255);
    private final Color MAU_NUT_THANHTOAN = new Color(103, 192, 144);
    private final Font phongChuGiaTri = new Font("Segoe UI", Font.PLAIN, 15);
    private final Font phongChuTongKet = new Font("Segoe UI", Font.BOLD, 22);

   
    private JTextField txtTenKhachHang, txtCmndPayer, txtSdtPayer;
    private JTextField txtMaKhuyenMai;
    private JTextField txtTongTienVeGoc;
    private JTextField txtThueVAT;
    private JTextField txtTongThanhToanCuoi;
    private JTable tblChiTiet;
    private DefaultTableModel moHinhBang;
    private JButton btnQuayLai, btnThanhToan, btnApDungMa;
    private JPanel pnlNhapLieu; 
    private QuanLyVeControl dieuKhienVe = new QuanLyVeControl();
    
    private List<Ve> danhSachVe;
    private HanhKhach nguoiThanhToan;
    private NhanVien nhanVienLap;
    private QuanLyHoaDonControl hdControl = new QuanLyHoaDonControl();
    private VeDAO vedao = new VeDAO(); 
    private BigDecimal tongTienTruocVAT = BigDecimal.ZERO;
    private ChuyenTau chuyenTauDuocChon;
    
    private GiaoDienNhapThongTinHK previousScreen;
    
    public GiaoDienThanhToan(List<Ve> danhSachVe, HanhKhach nguoiThanhToan, NhanVien nv,
    		GiaoDienNhapThongTinHK previous) throws Exception {
        if (danhSachVe == null || danhSachVe.isEmpty()) {
            throw new IllegalArgumentException("Danh sách vé không được rỗng.");
        }
       
        if (nguoiThanhToan == null || nv == null) {
            throw new IllegalArgumentException("Thông tin người thanh toán không được rỗng.");
        }
        this.previousScreen = previous;
        this.danhSachVe = danhSachVe;
        this.nhanVienLap = nv;
        this.nguoiThanhToan = nguoiThanhToan;

        // Không đặt vé ở đây, chỉ chuẩn bị dữ liệu hiển thị
        if (!danhSachVe.isEmpty()) {
            this.chuyenTauDuocChon = danhSachVe.get(0).getMaChuyenTau();
        }

        setTitle("Thanh Toán Giao Dịch Bán Vé");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        khoiTaoThanhPhan();
        thietLapSuKien();
        taiDuLieuGiaoDich();
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
        GridBagConstraints gbc = taoGBC(x, y, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST, 1.0, insets);
        gbc.gridwidth = width;
        return gbc;
    }

    private JButton taoNutApDung() {
        JButton nut = new JButton("Áp Dụng");
        nut.setBackground(new Color(93, 156, 236));
        nut.setForeground(Color.WHITE);
        nut.setFont(new Font("Segoe UI", Font.BOLD, 15));
        return nut;
    }

    
    private String layThongTinGa(ChuyenTau ct) {
        if (ct == null || ct.getMaLichTrinh() == null) return "N/A - N/A";
        String maLichTrinh = ct.getMaLichTrinh();
        return "[Lịch trình: " + maLichTrinh + "]";
    }

    private void khoiTaoThanhPhan() {
        JPanel khungChinh = new JPanel(new BorderLayout(10, 10));
        khungChinh.setBackground(Color.WHITE);
        khungChinh.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        JLabel lblTieuDeChinh = new JLabel("TIẾN HÀNH THANH TOÁN", SwingConstants.CENTER);
        lblTieuDeChinh.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTieuDeChinh.setForeground(mauChinh);
        pnlHeader.add(lblTieuDeChinh, BorderLayout.NORTH);
        khungChinh.add(pnlHeader, BorderLayout.NORTH);

        
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBackground(Color.WHITE);

       
        JPanel pnlThongTinThanhToan = new JPanel(new BorderLayout());
        pnlThongTinThanhToan.setPreferredSize(new Dimension(500, 0));
        pnlThongTinThanhToan.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(mauChinh, 2), "THÔNG TIN THANH TOÁN",
            TitledBorder.CENTER, TitledBorder.TOP, phongChuTongKet, mauChinh));

        pnlNhapLieu = new JPanel(new GridBagLayout());
        pnlNhapLieu.setBackground(Color.WHITE);
        Insets insets = new Insets(8, 10, 8, 10);
        int dong = 0;

       
        txtTenKhachHang = new JTextField(20); txtTenKhachHang.setEditable(false);
        txtCmndPayer = new JTextField(20); txtCmndPayer.setEditable(false);
        txtSdtPayer = new JTextField(20); txtSdtPayer.setEditable(false);
        txtMaKhuyenMai = new JTextField(15);
        btnApDungMa = taoNutApDung();
        txtTongTienVeGoc = new JTextField(20); txtTongTienVeGoc.setEditable(false);
        txtThueVAT = new JTextField(20); txtThueVAT.setEditable(false);
        txtTongThanhToanCuoi = new JTextField(20); txtTongThanhToanCuoi.setEditable(false);

        
        pnlNhapLieu.add(new JLabel("Người thanh toán:"), taogbcLabel(0, dong, insets));
        pnlNhapLieu.add(txtTenKhachHang, taogbcField(1, dong++, insets, 2));
        
        pnlNhapLieu.add(new JLabel("CMND/CCCD:"), taogbcLabel(0, dong, insets));
        pnlNhapLieu.add(txtCmndPayer, taogbcField(1, dong++, insets, 2));
        
        pnlNhapLieu.add(new JLabel("SĐT:"), taogbcLabel(0, dong, insets));
        pnlNhapLieu.add(txtSdtPayer, taogbcField(1, dong++, insets, 2));
        
        pnlNhapLieu.add(new JLabel("Mã Khuyến Mãi (KM):"), taogbcLabel(0, dong, insets));
        pnlNhapLieu.add(txtMaKhuyenMai, taogbcField(1, dong, insets, 1));
        pnlNhapLieu.add(btnApDungMa, taoGBC(2, dong++, GridBagConstraints.NONE, GridBagConstraints.EAST, 0.0, new Insets(8, 0, 8, 10)));
        
        pnlNhapLieu.add(new JLabel("Tổng tiền vé (Chưa Thuế):"), taogbcLabel(0, dong, insets));
        pnlNhapLieu.add(txtTongTienVeGoc, taogbcField(1, dong++, insets, 2));
       
        pnlNhapLieu.add(new JLabel("THUẾ VAT (10%):"), taogbcLabel(0, dong, insets));
        pnlNhapLieu.add(txtThueVAT, taogbcField(1, dong++, insets, 2));
        
        JLabel lblTongCong = new JLabel("TỔNG CỘNG PHẢI THU:");
        lblTongCong.setFont(phongChuTongKet);
        lblTongCong.setForeground(mauTongKet);
        pnlNhapLieu.add(lblTongCong, taogbcLabel(0, dong, insets));
        pnlNhapLieu.add(txtTongThanhToanCuoi, taogbcField(1, dong++, insets, 2));

        pnlThongTinThanhToan.add(pnlNhapLieu, BorderLayout.NORTH);
        pnlCenter.add(pnlThongTinThanhToan, BorderLayout.WEST);

        
        moHinhBang = new DefaultTableModel(
            new String[]{"STT", "Chuyến/Tàu", "Ga Đi - Đến", "Toa/Ghế", "Loại HK/Ưu đãi", "Giá TT (VNĐ)"}, 0);
        tblChiTiet = new JTable(moHinhBang);
        tblChiTiet.setRowHeight(35);
        tblChiTiet.setFont(phongChuGiaTri);

        JPanel pnlBangChiTiet = new JPanel(new BorderLayout());
        pnlBangChiTiet.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(mauChinh, 2), "CHI TIẾT VÉ",
            TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 18), mauChinh));

        pnlBangChiTiet.add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);
        pnlCenter.add(pnlBangChiTiet, BorderLayout.CENTER);

        khungChinh.add(pnlCenter, BorderLayout.CENTER);

        
        JPanel pnlThaoTac = taoPanelThaoTac();
        khungChinh.add(pnlThaoTac, BorderLayout.SOUTH);

        getContentPane().add(khungChinh);
    }

    private JPanel taoPanelThaoTac() {
        JPanel pnlThaoTac = new JPanel(new BorderLayout());
        pnlThaoTac.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); 
        
    	pnlThaoTac.setBackground(Color.WHITE);

        
        btnQuayLai = new JButton("Trở về");
        btnQuayLai.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnQuayLai.setForeground(Color.WHITE);
        btnQuayLai.setBackground(MAU_NUT_QUAYLAI);
        btnQuayLai.setPreferredSize(new Dimension(150, 50));

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        pnlLeft.setOpaque(false);
        pnlLeft.add(btnQuayLai);
        
        pnlThaoTac.add(pnlLeft, BorderLayout.WEST); 
        
       
        btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setBackground(MAU_NUT_THANHTOAN);
        btnThanhToan.setPreferredSize(new Dimension(180, 50));

        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); 
        pnlRight.setOpaque(false);
        pnlRight.add(btnThanhToan);
        
        pnlThaoTac.add(pnlRight, BorderLayout.EAST); 
        

        return pnlThaoTac;
    }

    private void thietLapSuKien() {
        btnQuayLai.addActionListener(this);
        btnThanhToan.addActionListener(this);
        btnApDungMa.addActionListener(this);
    }

    private void taiDuLieuGiaoDich() {
        tongTienTruocVAT = BigDecimal.ZERO;

       
        boolean isInfoMissing = nguoiThanhToan.getHoTen() == null || nguoiThanhToan.getHoTen().isEmpty();

        txtTenKhachHang.setText(isInfoMissing ? "k" : nguoiThanhToan.getHoTen());
        txtCmndPayer.setText(isInfoMissing || nguoiThanhToan.getCmndCccd() == null || nguoiThanhToan.getCmndCccd().trim().isEmpty() ? "Chưa cung cấp" : nguoiThanhToan.getCmndCccd());
        txtSdtPayer.setText(isInfoMissing || nguoiThanhToan.getSoDT() == null || nguoiThanhToan.getSoDT().trim().isEmpty() ? "Chưa cung cấp" : nguoiThanhToan.getSoDT());

        moHinhBang.setRowCount(0);
        int stt = 1;

        for (Ve ve : danhSachVe) {
            if (ve.getGiaThanhToan() == null) continue;

            tongTienTruocVAT = tongTienTruocVAT.add(ve.getGiaThanhToan());

            String maChuyen = ve.getMaChuyenTau() != null ? ve.getMaChuyenTau().getMaChuyenTau() : "N/A";
            String gaDiDen = layThongTinGa(ve.getMaChuyenTau());

            
            String toaGhe = (ve.getMaChoNgoi().getToaTau() != null ? ve.getMaChoNgoi().getToaTau().getMaToa() : "N/A")
                          + "/" + ve.getMaChoNgoi().getMaChoNgoi();

           
            String maUuDai = ve.getMaHanhkhach().getMaUuDai();
            String loaiHK = (maUuDai != null && !maUuDai.equals("0")) ? "Ưu đãi: " + maUuDai : "Thường";

            String giaTTFormat = String.format("%,.0f", ve.getGiaThanhToan().doubleValue());

            moHinhBang.addRow(new Object[]{
                stt++, maChuyen, gaDiDen, toaGhe, loaiHK, giaTTFormat
            });
        }

      
        BigDecimal VAT_RATE = new BigDecimal("0.1");
        BigDecimal thueVAT = tongTienTruocVAT.multiply(VAT_RATE).setScale(0, RoundingMode.HALF_UP);
        BigDecimal tongPhaiThu = tongTienTruocVAT.add(thueVAT);

        txtTongTienVeGoc.setText(String.format("%,.0f VNĐ", tongTienTruocVAT.doubleValue()));
        txtThueVAT.setText(String.format("%,.0f VNĐ", thueVAT.doubleValue()));
        txtTongThanhToanCuoi.setText(String.format("%,.0f VNĐ", tongPhaiThu.doubleValue()));
    }


    private void xuLyThanhToan() {
        btnThanhToan.setEnabled(false);

        try {
            // BƯỚC 1: ĐẢM BẢO HÀNH KHÁCH CÓ MÃ TRONG DB
            HanhKhachDAO hkDao = new HanhKhachDAO();
            String maHK = nguoiThanhToan.getMaKH();

            if (maHK == null || maHK.trim().isEmpty()) {
                // Chưa có trong DB → thêm mới
                if (!hkDao.themHanhKhach(nguoiThanhToan)) {
                    throw new Exception("Không thể thêm hành khách mới.");
                }
                // Lấy mã vừa thêm (giả sử DAO có hàm lấy theo CMND)
                HanhKhach hkVuaThem = hkDao.layHanhKhachTheoCMND(nguoiThanhToan.getCmndCccd());
                if (hkVuaThem == null || hkVuaThem.getMaKH() == null) {
                    throw new Exception("Không lấy được mã hành khách sau khi thêm.");
                }
                maHK = hkVuaThem.getMaKH();
                nguoiThanhToan.setMaKH(maHK); // CẬP NHẬT LẠI
            }

            // GÁN maHanhKhach CHO TẤT CẢ VÉ
            for (Ve ve : danhSachVe) {
                ve.getMaHanhkhach().setMaKH(maHK); // Quan trọng!
            }

            List<Ve> danhSachVeDaDat = new ArrayList<>();

            // BƯỚC 2: ĐẶT TỪNG VÉ
            for (Ve ve : danhSachVe) {
                String maVe = dieuKhienVe.datVe(ve, nguoiThanhToan, nhanVienLap);
                ve.setMaVe(maVe);
                danhSachVeDaDat.add(ve);
            }

            // BƯỚC 3: LẬP HÓA ĐƠN
            HoaDon hd = hdControl.lapHoaDon(nguoiThanhToan, nhanVienLap, danhSachVeDaDat);
            JOptionPane.showMessageDialog(this, "Thanh toán thành công! Mã HD: " + hd.getMaHoaDon());
            this.dispose();
            new GiaoDienLapHoaDon(danhSachVeDaDat, nguoiThanhToan, nhanVienLap, this).setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi đặt vé cho chỗ A-001:\n" + e.getMessage(),
                "Đặt vé thất bại", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            btnThanhToan.setEnabled(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();
        if (nguon == btnThanhToan) {
            if (btnThanhToan.isEnabled()) { 
                xuLyThanhToan();
            }
        } else if (nguon == btnQuayLai) {
            this.dispose(); 
            
            if (previousScreen != null) {
                previousScreen.setVisible(true); 
            }
        } else if (nguon == btnApDungMa) {
//            xuLyApDungKhuyenMai();
        }
    }
}