package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.toedter.calendar.JDateChooser;

import control.ThongKeDoanhThuControl;


public class GiaoDienThongKe extends JFrame implements ActionListener {

	private JButton nutTroVe;
	private JButton nutTimKiem;
	private JComboBox<String> cboLoaiThoiGian;
	private JComboBox<Integer> cboNam;
	private JDateChooser ngayBatDau;
	private JDateChooser ngayKetThuc;
	private JLabel lblTongDoanhThu;
	private JLabel lblVeDaBan;
	private JLabel lblVeDaHuy;
	private JLabel lblVeDaHoan;
	private JLabel lblNam;
	private JLabel lblNgayBD;
	private JLabel lblNgayKT;
	private JTable bangHoaDon;
	private JTable bangNhanVien;
	private JTable bangKhachHang;
	private JPanel bieuDoThoiGian;
	private JPanel bieuDoNhanVien;
	private JPanel bieuDoKhachHang;
	private ThongKeDoanhThuControl dieuKhienThongKe;

    private final DecimalFormat dinhDangTien = new DecimalFormat("#,### VNĐ");

    public GiaoDienThongKe() {
        dieuKhienThongKe = new ThongKeDoanhThuControl();

        setTitle("Thống kê doanh thu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JPanel giaoDienChinh = new JPanel(new BorderLayout(10, 10));
        giaoDienChinh.setBackground(new Color(245, 247, 250));
        giaoDienChinh.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       
        JPanel khungTieuDe = new JPanel(new BorderLayout());
        khungTieuDe.setBackground(new Color(74, 140, 103));
        JLabel tieuDe = new JLabel("THỐNG KÊ - BÁO CÁO DOANH THU", SwingConstants.CENTER);
        tieuDe.setFont(new Font("Segoe UI", Font.BOLD, 28));
        tieuDe.setForeground(Color.WHITE);
        khungTieuDe.add(tieuDe, BorderLayout.CENTER);
        khungTieuDe.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        giaoDienChinh.add(khungTieuDe, BorderLayout.NORTH);

        
        JTabbedPane tabChinh = taoTabChinh();
        giaoDienChinh.add(tabChinh, BorderLayout.CENTER);

        
        JPanel chanTrang = new JPanel(new BorderLayout());
        chanTrang.setBackground(new Color(74,140,103));
        chanTrang.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JPanel thongKeTongQuan = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 5));
        thongKeTongQuan.setOpaque(false);

        lblTongDoanhThu = new JLabel("Doanh thu: ---");
        lblVeDaBan = new JLabel("Vé đã bán: ---");
        lblVeDaHuy = new JLabel("Vé đã hủy: ---");
        lblVeDaHoan = new JLabel("Vé đã hoàn: ---");

        Font font = new Font("Segoe UI", Font.BOLD, 15);
        lblTongDoanhThu.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblVeDaBan.setFont(font);
        lblVeDaHuy.setFont(font);
        lblVeDaHoan.setFont(font);

        Color mauTrang = Color.WHITE;
        lblTongDoanhThu.setForeground(mauTrang);
        lblVeDaBan.setForeground(mauTrang);
        lblVeDaHuy.setForeground(mauTrang);
        lblVeDaHoan.setForeground(mauTrang);

        thongKeTongQuan.add(lblTongDoanhThu);
        thongKeTongQuan.add(lblVeDaBan);
        thongKeTongQuan.add(lblVeDaHuy);
        thongKeTongQuan.add(lblVeDaHoan);

        chanTrang.add(thongKeTongQuan, BorderLayout.CENTER);

        JPanel khungNut = new JPanel(new FlowLayout(FlowLayout.LEFT));
        khungNut.setOpaque(false);
        nutTroVe = taoNut("Trở về",new Color(93, 156, 236));
        nutTroVe.setForeground(Color.WHITE);
        nutTroVe.setPreferredSize(new Dimension(120, 40));
        nutTroVe.addActionListener(this);
        khungNut.add(nutTroVe);
        chanTrang.add(khungNut, BorderLayout.WEST);

        giaoDienChinh.add(chanTrang, BorderLayout.SOUTH);

        add(giaoDienChinh);
        capNhatThongKeTongQuan();
        setVisible(true);
    }

    
    private JTabbedPane taoTabChinh() {
        JTabbedPane tab = new JTabbedPane();
        tab.addTab("Thời gian", taoTabTheoThoiGian());
        tab.addTab("Nhân viên", taoTabTheoNhanVien());
        tab.addTab("Khách hàng", taoTabTheoKhachHang());
        tab.setFont(new Font("Segoe UI", Font.BOLD, 18));

        tab.addChangeListener(e -> {
            String tenTab = tab.getTitleAt(tab.getSelectedIndex());
            if (tenTab.equals("Nhân viên")) {
            	capNhatTheoNhanVien();
            }
            else if (tenTab.equals("Khách hàng")) {
            	capNhatTheoKhachHang();
            }
        });
        return tab;
    }

   
    private JPanel taoTabTheoThoiGian() {
        JPanel tong = new JPanel(new BorderLayout());
        tong.setBackground(Color.WHITE);

        JPanel boLoc = new JPanel(new GridBagLayout());
        boLoc.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        boLoc.add(new JLabel("Loại thời gian:"), gbc);

        gbc.gridx = 1;
        cboLoaiThoiGian = new JComboBox<>(new String[]{"Theo ngày", "Theo tháng", "Theo năm"});
        cboLoaiThoiGian.setPreferredSize(new Dimension(150, 30));
        cboLoaiThoiGian.addActionListener(this);
        boLoc.add(cboLoaiThoiGian, gbc);

        gbc.gridx = 2;
        lblNam = new JLabel("Năm:");
        boLoc.add(lblNam, gbc);
        gbc.gridx = 3;
        cboNam = new JComboBox<>();
        int namHienTai = LocalDate.now().getYear();
        for (int i = namHienTai; i >= namHienTai - 20; i--) cboNam.addItem(i);
        cboNam.setPreferredSize(new Dimension(120, 30));
        boLoc.add(cboNam, gbc);

        gbc.gridx = 4;
        lblNgayBD = new JLabel("Ngày bắt đầu:");
        boLoc.add(lblNgayBD, gbc);
        gbc.gridx = 5;
        ngayBatDau = new JDateChooser();
        ngayBatDau.setDateFormatString("dd/MM/yyyy");
        ngayBatDau.setPreferredSize(new Dimension(150, 30));
        boLoc.add(ngayBatDau, gbc);

        gbc.gridx = 6;
        lblNgayKT = new JLabel("Ngày kết thúc:");
        boLoc.add(lblNgayKT, gbc);
        gbc.gridx = 7;
        ngayKetThuc = new JDateChooser();
        ngayKetThuc.setDateFormatString("dd/MM/yyyy");
        ngayKetThuc.setPreferredSize(new Dimension(150, 30));
        boLoc.add(ngayKetThuc, gbc);

        gbc.gridx = 8;
        nutTimKiem = taoNut("Tìm kiếm", new Color(93, 156, 236));
        nutTimKiem.setForeground(Color.WHITE);
        nutTimKiem.addActionListener(this);
        boLoc.add(nutTimKiem, gbc);

        tong.add(boLoc, BorderLayout.NORTH);

       
        JTabbedPane tabNoi = new JTabbedPane();
        tabNoi.setFont(new Font("Segoe UI", Font.BOLD, 14));

        bieuDoThoiGian = new JPanel(new BorderLayout());
        bieuDoThoiGian.add(new JLabel("Chưa có dữ liệu để hiển thị", SwingConstants.CENTER), BorderLayout.CENTER);
        tabNoi.addTab("Tổng quan", bieuDoThoiGian);

        bangHoaDon = new JTable();
        tabNoi.addTab("Chi tiết", new JScrollPane(bangHoaDon));

        tong.add(tabNoi, BorderLayout.CENTER);
        capNhatHienThiBoLoc();
        return tong;
    }

   
    private JPanel taoTabTheoNhanVien() {
        JPanel p = new JPanel(new BorderLayout());
        JTabbedPane tabNoi = new JTabbedPane();
        tabNoi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bieuDoNhanVien = new JPanel(new BorderLayout());
        tabNoi.addTab("Tổng quan", bieuDoNhanVien);

        bangNhanVien = new JTable();
        tabNoi.addTab("Chi tiết", new JScrollPane(bangNhanVien));

        p.add(tabNoi, BorderLayout.CENTER);
        return p;
    }

    
    private JPanel taoTabTheoKhachHang() {
        JPanel p = new JPanel(new BorderLayout());
        JTabbedPane tabNoi = new JTabbedPane();
        tabNoi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bieuDoKhachHang = new JPanel(new BorderLayout());
        tabNoi.addTab("Tổng quan", bieuDoKhachHang);

        bangKhachHang = new JTable();
        tabNoi.addTab("Chi tiết", new JScrollPane(bangKhachHang));

        p.add(tabNoi, BorderLayout.CENTER);
        return p;
    }

   
    private void capNhatTheoNhanVien() {
        List<Object[]> ds = dieuKhienThongKe.thongKeChiTietNhanVien();
      
        LinkedHashMap<String, Double> top5 = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(5, ds.size()); i++) {
            Object[] dong = ds.get(i);
            top5.put(dong[1].toString(), (Double) dong[3]);
        }
        hienThiBieuDo(top5, bieuDoNhanVien, "5 nhân viên có doanh thu cao nhất");

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Mã NV", "Tên nhân viên", "Ngày sinh", "Doanh thu"}, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Object[] row : ds) {
            model.addRow(new Object[]{
                    row[0],
                    row[1],
                    row[2] != null ? sdf.format((Date) row[2]) : "",
                    dinhDangTien.format((Double) row[3])
            });
        }
        bangNhanVien.setModel(model);
    }
   
    private void capNhatTheoKhachHang() {
        List<Object[]> ds = dieuKhienThongKe.thongKeChiTietKhachHang();

        LinkedHashMap<String, Double> top5 = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(5, ds.size()); i++) {
            Object[] dong = ds.get(i);
            top5.put(dong[1].toString(), (Double) dong[2]);
        }
        hienThiBieuDo(top5, bieuDoKhachHang, "5 khách hàng có chi tiêu cao nhất");

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Mã KH", "Tên khách hàng", "Chi Tiêu"}, 0);
        for (Object[] row : ds) {
            model.addRow(new Object[]{
                    row[0],
                    row[1],
                    dinhDangTien.format((Double) row[2])
            });
        }
        bangKhachHang.setModel(model);
    }

   
    private void hienThiBieuDo(Map<String, Double> duLieu, JPanel bieuDo, String tieuDe) {
        bieuDo.removeAll();
        
        DefaultCategoryDataset tapDuLieu = new DefaultCategoryDataset();
        
        if (duLieu != null && !duLieu.isEmpty()) {
            
            DateTimeFormatter dinhDangGoc = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dinhDangMoi = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            duLieu.forEach((key, value) -> {
                try {
                    LocalDate ngay = LocalDate.parse(key, dinhDangGoc);
                    String keyDaDinhDang = ngay.format(dinhDangMoi);
                    tapDuLieu.addValue(value, "Doanh thu", keyDaDinhDang);
                } catch (Exception e) {
                    tapDuLieu.addValue(value, "Doanh thu", key);
                }
            });

           
            JFreeChart chart = ChartFactory.createBarChart(
                    tieuDe, "", "Doanh thu (VNĐ)", tapDuLieu,
                    PlotOrientation.VERTICAL, false , true, false);

            
            CategoryPlot layptubieudo = chart.getCategoryPlot();
            layptubieudo.setBackgroundPaint(Color.WHITE);
            layptubieudo.setRangeGridlinePaint(new Color(74, 140, 103));

            
            NumberAxis trucDoc = (NumberAxis) layptubieudo.getRangeAxis();
            trucDoc.setNumberFormatOverride(new DecimalFormat("#,###"));

            
            BarRenderer veCot = (BarRenderer) layptubieudo.getRenderer();
            veCot.setSeriesPaint(0, new Color(93, 156, 23));
            veCot.setShadowVisible(false);
            veCot.setMaximumBarWidth(0.1);
            veCot.setMinimumBarLength(0.05);

           
            chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 20));

            
            ChartPanel khungBieuDo = new ChartPanel(chart);
            bieuDo.add(khungBieuDo, BorderLayout.CENTER);
        } else {
            bieuDo.add(new JLabel("Không có dữ liệu để hiển thị!", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        bieuDo.revalidate();
        bieuDo.repaint();
    }


    private JButton taoNut(String ten, Color mauNen) {
        JButton nut = new JButton(ten);
        nut.setBackground(mauNen);
        nut.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nut.setForeground(Color.WHITE);
        nut.setFocusPainted(false);
        nut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return nut;
    }
    private void capNhatThongKeTongQuan() {
        double tong = 0;
        for (Double v : dieuKhienThongKe.thongKeDoanhThuTheoNam().values()) tong += v;
        lblTongDoanhThu.setText("Doanh thu: " + dinhDangTien.format(tong));
        lblVeDaBan.setText("Vé đã bán: " + dieuKhienThongKe.tongVeDaBan());
        lblVeDaHuy.setText("Vé đã hủy: " + dieuKhienThongKe.tongVeDaHuy());
        lblVeDaHoan.setText("Vé đã hoàn: " + dieuKhienThongKe.tongVeDaHoan());
    }
    
    private void capNhatHienThiBoLoc() {
        String loai = (String) cboLoaiThoiGian.getSelectedItem();
        cboNam.setEnabled(loai.equals("Theo tháng"));
        ngayBatDau.setEnabled(loai.equals("Theo ngày"));
        ngayKetThuc.setEnabled(loai.equals("Theo ngày"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();

        if (nguon == cboLoaiThoiGian) {
            capNhatHienThiBoLoc();
        } else if (nguon == nutTroVe) {
     	
            dispose();
        } else if (nguon == nutTimKiem) {
            String loai = (String) cboLoaiThoiGian.getSelectedItem();
            Map<String, Double> duLieu;
            Date bd = ngayBatDau.getDate(), kt = ngayKetThuc.getDate();
            Integer nam = (Integer) cboNam.getSelectedItem();

            switch (loai) {
                case "Theo ngày":
                    if (bd == null || kt == null) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bắt đầu và kết thúc!");
                        return;
                    }
                    if (bd.after(kt)) {
                        JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được lớn hơn ngày kết thúc!");
                        return;
                    }
                    duLieu = dieuKhienThongKe.thongKeDoanhThuTheoNgay(bd, kt);
                    break;
                case "Theo tháng":
                    duLieu = dieuKhienThongKe.thongKeDoanhThuTheoThang(nam);
                    break;
                default:
                    duLieu = dieuKhienThongKe.thongKeDoanhThuTheoNam();
                    break;
            }

            hienThiBieuDo(duLieu, bieuDoThoiGian, "Doanh thu" + loai.toLowerCase());

            
            List<Object[]> danhSachHD = new ArrayList<>();
            if (loai.equals("Theo ngày")) {
            	danhSachHD = dieuKhienThongKe.getHoaDonTheoKhoangNgay(bd, kt);
            }
            else if (loai.equals("Theo tháng")) {
            	danhSachHD = dieuKhienThongKe.getHoaDonTheoNam(nam);
            }
            else danhSachHD = dieuKhienThongKe.getTatCaHoaDon();

            DefaultTableModel modelHD = new DefaultTableModel(
                    new String[]{"Mã hóa đơn", "Ngày lập", "Nhân viên", "Khách hàng", "Tổng tiền"}, 0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (Object[] row : danhSachHD) {
                modelHD.addRow(new Object[]{
                        row[0],
                        sdf.format((Date) row[1]),
                        row[2],
                        row[3],
                        dinhDangTien.format((Double) row[4])
                });
            }
            bangHoaDon.setModel(modelHD);
        }
    }

    public static void main(String[] args) {
    	LookAndFeelManager.setNimbusLookAndFeel();
        new GiaoDienThongKe();
    }
}
