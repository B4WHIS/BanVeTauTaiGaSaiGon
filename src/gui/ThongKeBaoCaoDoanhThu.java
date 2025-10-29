package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import com.toedter.calendar.JDateChooser;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.*;
import org.jfree.data.category.DefaultCategoryDataset;
import gui.GiaoDienChinh;

import control.ThongKeDoanhThuControl;

public class ThongKeBaoCaoDoanhThu extends JFrame implements ActionListener {

    private JButton nutTroVe, nutTimKiem;
    private JComboBox<String> cboLoaiThoiGian;
    private JComboBox<Integer> cboNam;
    private JDateChooser ngayBatDau, ngayKetThuc;

    private JLabel lblTongDoanhThu, lblVeDaBan, lblVeDaHuy, lblVeDaHoan;
    private ThongKeDoanhThuControl dieuKhienThongKe;
    private JPanel bieuDoThoiGian, bieuDoNhanVien, bieuDoKhachHang;
    private JTable bangHoaDon, bangNhanVien, bangKhachHang;

    private JLabel lblNam, lblNgayBD, lblNgayKT;
    private final DecimalFormat dinhDangTien = new DecimalFormat("#,### VNĐ");

    public ThongKeBaoCaoDoanhThu() {
        dieuKhienThongKe = new ThongKeDoanhThuControl();

        setTitle("Thống kê doanh thu");
        setSize(1500, 950);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel giaoDienChinh = new JPanel(new BorderLayout(10, 10));
        giaoDienChinh.setBackground(new Color(245, 247, 250));
        giaoDienChinh.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Tiêu đề =====
        JPanel khungTieuDe = new JPanel(new BorderLayout());
        khungTieuDe.setBackground(new Color(93, 156, 236));
        JLabel tieuDe = new JLabel("THỐNG KÊ - BÁO CÁO DOANH THU", SwingConstants.CENTER);
        tieuDe.setFont(new Font("Segoe UI", Font.BOLD, 28));
        tieuDe.setForeground(Color.WHITE);
        khungTieuDe.add(tieuDe, BorderLayout.CENTER);
        khungTieuDe.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        giaoDienChinh.add(khungTieuDe, BorderLayout.NORTH);

        // ===== Tabs chính =====
        JTabbedPane tabChinh = taoTabChinh();
        giaoDienChinh.add(tabChinh, BorderLayout.CENTER);

        // ===== Footer =====
        JPanel chanTrang = new JPanel(new BorderLayout());
        chanTrang.setBackground(new Color(93, 156, 236));
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
        nutTroVe = taoNut("Trở về", Color.WHITE);
        nutTroVe.setForeground(new Color(93, 156, 236));
        nutTroVe.setPreferredSize(new Dimension(120, 40));
        nutTroVe.addActionListener(this);
        khungNut.add(nutTroVe);
        chanTrang.add(khungNut, BorderLayout.WEST);

        giaoDienChinh.add(chanTrang, BorderLayout.SOUTH);

        add(giaoDienChinh);
        capNhatThongKeTongQuan();
        setVisible(true);
    }

    // tab chính 
    private JTabbedPane taoTabChinh() {
        JTabbedPane tab = new JTabbedPane();
        tab.addTab("Thời gian", taoTabTheoThoiGian());
        tab.addTab("Nhân viên", taoTabTheoNhanVien());
        tab.addTab("Khách hàng", taoTabTheoKhachHang());
        tab.setFont(new Font("Segoe UI", Font.BOLD, 18));

        tab.addChangeListener(e -> {
            String tenTab = tab.getTitleAt(tab.getSelectedIndex());
            if (tenTab.equals("Nhân viên")) capNhatTheoNhanVien();
            else if (tenTab.equals("Khách hàng")) capNhatTheoKhachHang();
        });
        return tab;
    }

    // tab thời gian
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

        //tab chi tiết và tổng quan
        JTabbedPane tabNoi = new JTabbedPane();
        tabNoi.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        bieuDoThoiGian = new JPanel(new BorderLayout());
        bieuDoThoiGian.add(new JLabel("Chưa có dữ liệu để hiển thị", SwingConstants.CENTER), BorderLayout.CENTER);
        tabNoi.addTab("Tổng quan", bieuDoThoiGian);

        bangHoaDon = new JTable();
        tabNoi.addTab("Chi tiết", new JScrollPane(bangHoaDon));

        tong.add(tabNoi, BorderLayout.CENTER);
        capNhatHienThiBoLoc();
        return tong;
    }

    // tab nhân viên
    private JPanel taoTabTheoNhanVien() {
        JPanel p = new JPanel(new BorderLayout());
        JTabbedPane tabNoi = new JTabbedPane();

        bieuDoNhanVien = new JPanel(new BorderLayout());
        tabNoi.addTab("Tổng quan", bieuDoNhanVien);

        bangNhanVien = new JTable();
        tabNoi.addTab("Chi tiết", new JScrollPane(bangNhanVien));

        p.add(tabNoi, BorderLayout.CENTER);
        return p;
    }

    // tabkhachhang
    private JPanel taoTabTheoKhachHang() {
        JPanel p = new JPanel(new BorderLayout());
        JTabbedPane tabNoi = new JTabbedPane();

        bieuDoKhachHang = new JPanel(new BorderLayout());
        tabNoi.addTab("Tổng quan", bieuDoKhachHang);

        bangKhachHang = new JTable();
        tabNoi.addTab("Chi tiết", new JScrollPane(bangKhachHang));

        p.add(tabNoi, BorderLayout.CENTER);
        return p;
    }

    // hàm cập nhật dữ liệu cho tab nhân viên
    private void capNhatTheoNhanVien() {
        List<Object[]> ds = dieuKhienThongKe.thongKeChiTietNhanVien();

        LinkedHashMap<String, Double> top5 = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(5, ds.size()); i++) {
            Object[] dong = ds.get(i);
            top5.put(dong[1].toString(), (Double) dong[3]);
        }
        hienThiBieuDo(top5, bieuDoNhanVien, "Top 5 nhân viên có doanh thu cao nhất");

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
    /// cập nhật dữ liệu cho khách hàng
    private void capNhatTheoKhachHang() {
        List<Object[]> ds = dieuKhienThongKe.thongKeChiTietKhachHang();

        LinkedHashMap<String, Double> top5 = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(5, ds.size()); i++) {
            Object[] dong = ds.get(i);
            top5.put(dong[1].toString(), (Double) dong[2]);
        }
        hienThiBieuDo(top5, bieuDoKhachHang, "Top 5 khách hàng có doanh thu cao nhất");

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Mã KH", "Tên khách hàng", "Doanh thu"}, 0);
        for (Object[] row : ds) {
            model.addRow(new Object[]{
                    row[0],
                    row[1],
                    dinhDangTien.format((Double) row[2])
            });
        }
        bangKhachHang.setModel(model);
    }

    // hàm biểu đồ
    private void hienThiBieuDo(Map<String, Double> duLieu, JPanel bieuDo, String tieuDe) {
        bieuDo.removeAll();
        DefaultCategoryDataset tapDuLieu = new DefaultCategoryDataset();

        if (duLieu != null && !duLieu.isEmpty()) {
            duLieu.forEach((key, value) -> tapDuLieu.addValue(value, "Doanh thu", key));

            JFreeChart chart = ChartFactory.createBarChart(
                    tieuDe, "", "Doanh thu (VNĐ)", tapDuLieu,
                    PlotOrientation.VERTICAL, false, true, false);

            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setRangeGridlinePaint(new Color(200, 200, 200));

            NumberAxis trucDoc = (NumberAxis) plot.getRangeAxis();
            trucDoc.setNumberFormatOverride(new DecimalFormat("#,###"));

            BarRenderer veCot = (BarRenderer) plot.getRenderer();
            veCot.setSeriesPaint(0, new Color(93, 156, 236));
            veCot.setShadowVisible(false);

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
        nut.setFocusPainted(false);
        nut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return nut;
    }
    // cập nhật thống kê ở dưới footer
    private void capNhatThongKeTongQuan() {
        double tong = 0;
        for (Double v : dieuKhienThongKe.thongKeDoanhThuTheoNam().values()) tong += v;
        lblTongDoanhThu.setText("Doanh thu: " + dinhDangTien.format(tong));
        lblVeDaBan.setText("Vé đã bán: " + dieuKhienThongKe.tongVeDaBan());
        lblVeDaHuy.setText("Vé đã hủy: " + dieuKhienThongKe.tongVeDaHuy());
        lblVeDaHoan.setText("Vé đã hoàn: " + dieuKhienThongKe.tongVeDaHoan());
    }
    // cập nhật hiển thị cho chọn  và tắt bộ lọc
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
//        	GiaoDienChinh gdchinh = new GiaoDienChinh();
//        	gdchinh.setVisible(true);
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

            hienThiBieuDo(duLieu, bieuDoThoiGian, "Doanh thu " + loai.toLowerCase());

            // ===== Hiển thị bảng chi tiết =====
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
        new ThongKeBaoCaoDoanhThu();
    }
}
