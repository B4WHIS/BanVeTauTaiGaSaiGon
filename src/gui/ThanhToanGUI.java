package gui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import entity.ChiTietHoaDon;
import entity.HanhKhach;
import entity.HoaDon;
import entity.NhanVien;
import entity.Ve;

public class ThanhToanGUI extends JFrame implements ActionListener {
	 // THÊM CÁC TRƯỜNG MỚI CHO CONTEXT
    private List<Ve> danhSachVe;
    private HanhKhach khachHang;
    private NhanVien nhanVien;
    private double tongTienThanhToan;
    
    // DAO instances (giả định)
    private HoaDonDAO hdDao = new HoaDonDAO();
    private ChiTietHoaDonDAO cthdDao = new ChiTietHoaDonDAO();
	
    private JTextField txtMaGD;
    private JTextField txtTenKH;
    private JTextField txtTongTien;
    private JTextField txtGiamGia;
    private JTextField txtThanhToan;

    private JTable table;

    private JPanel pnlPT;
    private JPanel footer;
	private JButton btnthanhtoan;

    public ThanhToanGUI(List<Ve> danhSachVeDaDat, HanhKhach finalHkDaLuu, NhanVien nvLap, double tongTien) {
    	 this.danhSachVe = danhSachVeDaDat;
         this.khachHang = finalHkDaLuu;
         this.nhanVien = nvLap;
         this.tongTienThanhToan = tongTien; // Cần khởi tạo các components GUI
        
        btnthanhtoan.addActionListener(this); 
        setTitle("Thanh Toán");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1000);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        txtMaGD = new JTextField(20);
        txtTenKH = new JTextField(20);
        txtTongTien = new JTextField(20);
        txtGiamGia = new JTextField(20);
        txtThanhToan = new JTextField(20);

        // header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(103, 192, 144));
        header.setPreferredSize(new Dimension(0, 100));

        JLabel lblTieuDe = new JLabel("THANH TOÁN", SwingConstants.CENTER);
        lblTieuDe.setForeground(Color.WHITE);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 36));
        header.add(lblTieuDe, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // icon header
        ImageIcon iconhoadon = new ImageIcon("src/img/pay.png");
        Image imagehoadon = iconhoadon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        lblTieuDe.setIcon(new ImageIcon(imagehoadon));
        lblTieuDe.setHorizontalTextPosition(SwingConstants.RIGHT);
        lblTieuDe.setIconTextGap(10);

        // center
        JPanel pnlCenter = new JPanel(new BorderLayout());
        add(pnlCenter, BorderLayout.CENTER);

        // Cột trái
        JPanel pnlTrai = new JPanel(new BorderLayout(10, 10));
        pnlTrai.setPreferredSize(new Dimension(450, 0));
        pnlTrai.setBackground(Color.WHITE);
        pnlTrai.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        pnlCenter.add(pnlTrai, BorderLayout.WEST);

        JLabel lblFormTitle = new JLabel("Thông tin thanh toán", SwingConstants.CENTER);
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblFormTitle.setForeground(new Color(103, 192, 144));
        pnlTrai.add(lblFormTitle, BorderLayout.NORTH);

        JPanel pnlNhap = new JPanel(new GridBagLayout());
        pnlNhap.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã GD
        gbc.gridx = 0; gbc.gridy = 0;
        pnlNhap.add(new JLabel("Mã giao dịch:"), gbc);
        gbc.gridx = 1;

        // Tên KH
        gbc.gridx = 0; gbc.gridy = 1;
        pnlNhap.add(new JLabel("Tên Khách Hàng:"), gbc);
        gbc.gridx = 1;

        // Giảm giá
        gbc.gridx = 0; gbc.gridy = 2;
        pnlNhap.add(new JLabel("Giảm giá:"), gbc);
        gbc.gridx = 1;

        gbc.gridx = 0; gbc.gridy = 3;
        pnlNhap.add(new JLabel("Thuế:"), gbc);
        gbc.gridx = 1;

        // Thành tiền
        gbc.gridx = 0; gbc.gridy = 4;
        pnlNhap.add(new JLabel("Thành tiền:"), gbc);
        gbc.gridx = 1;

        // radio
        gbc.gridx = 0; gbc.gridy = 5;
        pnlNhap.add(new JLabel("Phương thức Thanh Toán:"), gbc);
        gbc.gridx = 1;
        pnlPT = new JPanel(new GridLayout(1, 2, 5, 5));
        pnlPT.setBackground(Color.WHITE);

        // Tạo radio button
        JRadioButton radTienMat = new JRadioButton("Tiền mặt");
        radTienMat.setBackground(Color.WHITE);
        radTienMat.setOpaque(true);
        JRadioButton radchuyenkhoan = new JRadioButton("Chuyển Khoản");
        radchuyenkhoan.setBackground(Color.WHITE);
        radchuyenkhoan.setOpaque(true);

        JPanel groupPT = new JPanel();
        groupPT.setBackground(Color.WHITE);
        groupPT.add(radTienMat);
        groupPT.add(radchuyenkhoan);

        pnlPT.add(radTienMat);
        pnlPT.add(radchuyenkhoan);

        pnlNhap.add(pnlPT, gbc);
        pnlTrai.add(pnlNhap, BorderLayout.CENTER);

        // cột phải
        JPanel pnlPhai = new JPanel(new BorderLayout());
        pnlPhai.setBackground(Color.WHITE);
        pnlPhai.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        pnlCenter.add(pnlPhai, BorderLayout.CENTER);

        JLabel lblTableTitle = new JLabel("Chi tiết vé", SwingConstants.CENTER);
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTableTitle.setForeground(new Color(103, 192, 144));
        pnlPhai.add(lblTableTitle, BorderLayout.NORTH);

        String[] columns = {"Mã Vé", "Ga đi", "Ga đến", "Ngày giờ", "Toa - Ghế", "Số Lượng", "Giá"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(table);
        pnlPhai.add(scrollPane, BorderLayout.CENTER);

        // Tổng cộng phía dưới bảng
        JPanel pnlTong = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlTong.setBackground(Color.WHITE);
        JLabel lblTong = new JLabel("TỔNG CỘNG: ");
        lblTong.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTong.setForeground(new Color(200, 30, 30));
        JLabel lblSoTien = new JLabel("0 VNĐ");
        lblSoTien.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSoTien.setForeground(new Color(200, 30, 30));
        pnlTong.add(lblTong);
        pnlTong.add(lblSoTien);
        pnlPhai.add(pnlTong, BorderLayout.SOUTH);

        // footer
        footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(103, 192, 144));
        footer.setPreferredSize(new Dimension(0, 60));
        footer.setBorder(new EmptyBorder(10, 20, 10, 20));
        //btn qauy kaij
        JButton btnquaylai = new JButton("Quay Lại");
        btnquaylai.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnquaylai.setForeground(Color.WHITE);
        btnquaylai.setBackground(new Color(99, 184, 255));
        btnquaylai.setBorder(new EmptyBorder(10, 0, 10, 0));
        btnquaylai.setPreferredSize(new Dimension(130, 80));

        ImageIcon iconquaylai = new ImageIcon("src/img/undo.png");
        Image imagequaylai = iconquaylai.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        btnquaylai.setIcon(new ImageIcon(imagequaylai));
        btnquaylai.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnquaylai.setIconTextGap(10);
        footer.add(btnquaylai, BorderLayout.WEST);
        
        //btnthanh toán
        btnthanhtoan = new JButton("Thanh Toán");
        btnthanhtoan.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnthanhtoan.setForeground(Color.WHITE);
        btnthanhtoan.setBackground(new Color(0, 128, 0));
        btnthanhtoan.setBorder(new EmptyBorder(10, 0, 10, 0));
        btnthanhtoan.setPreferredSize(new Dimension(130, 80));

        ImageIcon iconthanhtoan = new ImageIcon("src/img/mark.png");
        Image imagethanhtoan = iconthanhtoan.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        btnthanhtoan.setIcon(new ImageIcon(imagethanhtoan));
        btnthanhtoan.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnthanhtoan.setIconTextGap(10);

        footer.add(btnthanhtoan, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
    	LookAndFeelManager.setNimbusLookAndFeel();
        new ThanhToanGUI().setVisible(true);
    }
}

