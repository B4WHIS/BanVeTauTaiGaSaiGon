package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class ThongKeBaoCaoDoanhThu extends JFrame implements ActionListener {
    private JPanel chartPanel;
    private JButton btnCot, btnDuong, btnTron, btnTroVe,btnExport;
    private int bieuDoHienTai = 0; // 0=cột, 1=đường, 2=tròn
    protected JMenuBar menuBar;

    public ThongKeBaoCaoDoanhThu() {
        setTitle("Thống kê doanh thu");
        setSize(1500, 1000);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== GẮN THANH MENU BAR TRÊN CÙNG =====
        setJMenuBar(taoMenuBar());

        // ===== MAIN PANEL =====
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(new Color(245, 247, 250));
        pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("THỐNG KÊ - BÁO CÁO DOANH THU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(41, 128, 185));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // ===== PANEL TRÁI: CHỌN LOẠI BIỂU ĐỒ =====
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        pnlLeft.setPreferredSize(new Dimension(400, 0));
        pnlLeft.setBackground(Color.WHITE);
        pnlLeft.setBorder(BorderFactory.createTitledBorder("Lựa chọn thống kê"));

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 18);
        JLabel lblLoai = new JLabel("Chọn loại biểu đồ:");
        lblLoai.setFont(fontLabel);

        btnCot = taoButtonChonBieuDo("Biểu đồ cột", new Color(46, 134, 222));
        btnDuong = taoButtonChonBieuDo("Biểu đồ đường", new Color(231, 76, 60));
        btnTron = taoButtonChonBieuDo("Biểu đồ tròn", new Color(46, 204, 113));
        btnExport = taoButton("Xuất Excel", new Color(155, 89, 182), "/img/export.png");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlLeft.add(lblLoai, gbc);
        gbc.gridy++;
        pnlLeft.add(btnCot, gbc);
        gbc.gridy++;
        pnlLeft.add(btnDuong, gbc);
        gbc.gridy++;
        pnlLeft.add(btnTron, gbc);
        gbc.gridy++;
        pnlLeft.add(btnExport, gbc);

        // ===== PANEL PHẢI: BIỂU ĐỒ =====
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawSampleChart(g, bieuDoHienTai);
            }
        };
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        // ===== FOOTER =====
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        pnlFooter.setBackground(new Color(149, 214, 179));

        btnTroVe = taoButton("Trở về", new Color(52, 152, 219), "/img/loginicon.png");
        pnlFooter.add(btnTroVe);

        // ===== GẮN TẤT CẢ VÀO MAIN =====
        pnlMain.add(pnlLeft, BorderLayout.WEST);
        pnlMain.add(chartPanel, BorderLayout.CENTER);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);

        add(pnlMain);
        setVisible(true);
    }

    // ================== TẠO BUTTON CHỌN BIỂU ĐỒ ==================
    private JButton taoButtonChonBieuDo(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(250, 45));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });
        return btn;
    }

    // ================== BIỂU ĐỒ MINH HỌA ==================
    private void drawSampleChart(Graphics g, int type) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = chartPanel.getWidth();
        int h = chartPanel.getHeight();
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        g2.setColor(Color.GRAY);
        g2.drawString("(Biểu đồ minh họa - chưa có dữ liệu thực)", 30, 40);

        switch (type) {
            case 0 -> drawBarChart(g2, w, h);
            case 1 -> drawLineChart(g2, w, h);
            case 2 -> drawPieChart(g2, w, h);
        }
    }

    private void drawBarChart(Graphics2D g2, int w, int h) {
        int[] values = {100, 200, 150, 300, 250};
        String[] months = {"1", "2", "3", "4", "5"};
        int barWidth = (w - 200) / values.length;
        int max = 300;

        g2.setColor(Color.BLACK);
        g2.drawLine(100, h - 100, w - 100, h - 100);
        g2.drawLine(100, 80, 100, h - 100);

        for (int i = 0; i < values.length; i++) {
            int barHeight = (int) ((values[i] / (double) max) * (h - 180));
            int x = 130 + i * barWidth;
            int y = h - 100 - barHeight;

            g2.setColor(new Color(46, 134, 222));
            g2.fillRect(x, y, barWidth - 20, barHeight);
            g2.setColor(Color.BLACK);
            g2.drawString(months[i], x + barWidth / 3, h - 70);
        }
    }

    private void drawLineChart(Graphics2D g2, int w, int h) {
        int[] values = {80, 150, 220, 180, 260};
        String[] months = {"1", "2", "3", "4", "5"};
        int stepX = (w - 200) / (values.length - 1);
        int max = 300;

        g2.setColor(Color.BLACK);
        g2.drawLine(100, h - 100, w - 100, h - 100);
        g2.drawLine(100, 80, 100, h - 100);

        g2.setColor(new Color(231, 76, 60));
        int prevX = 100, prevY = h - 100 - (int) ((values[0] / (double) max) * (h - 180));
        for (int i = 1; i < values.length; i++) {
            int x = 100 + i * stepX;
            int y = h - 100 - (int) ((values[i] / (double) max) * (h - 180));
            g2.drawLine(prevX, prevY, x, y);
            g2.fillOval(x - 5, y - 5, 10, 10);
            prevX = x;
            prevY = y;
        }

        g2.setColor(Color.BLACK);
        for (int i = 0; i < months.length; i++) {
            g2.drawString(months[i], 100 + i * stepX - 5, h - 70);
        }
    }

    private void drawPieChart(Graphics2D g2, int w, int h) {
        int[] values = {30, 25, 20, 15, 10};
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA};
        int total = 0;
        for (int v : values) total += v;

        int startAngle = 0;
        int diameter = Math.min(w, h) / 2;
        int cx = w / 2 - diameter / 2;
        int cy = h / 2 - diameter / 2;

        for (int i = 0; i < values.length; i++) {
            int arc = (int) (360.0 * values[i] / total);
            g2.setColor(colors[i]);
            g2.fillArc(cx, cy, diameter, diameter, startAngle, arc);
            startAngle += arc;
        }

        g2.setColor(Color.BLACK);
        g2.drawOval(cx, cy, diameter, diameter);
    }

    // ================== TẠO MENU BAR ==================
    public JMenuBar taoMenuBar() {
		menuBar = new JMenuBar();
		menuBar.setBorder(BorderFactory.createEmptyBorder(8,0,8,0));
		menuBar.setBackground(new Color(220, 220, 220));
		JMenu mnuHeThong = new JMenu("Hệ Thống");
		JMenu mnuNghiepVuVe = new JMenu("Nghiệp Vụ Vé");
		JMenu mnuQly = new JMenu("Quản Lý");
		JMenu mnuTraCuu = new JMenu("Tra Cứu");
		JMenu mnuThongKe = new JMenu("Thống Kê");
		JMenu mnuTroGiup = new JMenu("Trợ Giúp");
	
		ImageIcon heThongIC = chinhKichThuoc("/img/heThong3.png", 30, 30);
		mnuHeThong.setIcon(heThongIC);
		ImageIcon NV_VeIC = chinhKichThuoc("/img/ve2.png", 30, 30);
		mnuNghiepVuVe.setIcon(NV_VeIC);
		ImageIcon QlyIC = chinhKichThuoc("/img/quanLy.png", 30, 30);
		mnuQly.setIcon(QlyIC);
		ImageIcon traCuuIC = chinhKichThuoc("/img/traCuu.png", 30, 30);
		mnuTraCuu.setIcon(traCuuIC);
		ImageIcon thongKeIC = chinhKichThuoc("/img/thongKe.png", 30, 30);
		mnuThongKe.setIcon(thongKeIC);
		ImageIcon troGiupIC = chinhKichThuoc("/img/troGiup.png", 30, 30);
		mnuTroGiup.setIcon(troGiupIC);
		
		
		JMenuItem ItemDX = new JMenuItem("Đăng xuất");
		JMenuItem ItemThoat = new JMenuItem("Thoát");
		mnuHeThong.add(ItemDX);
		mnuHeThong.add(ItemThoat);
		
		JMenuItem ItemDatVe = new JMenuItem("Đặt vé");
		JMenuItem ItemHuyVe= new JMenuItem("Hủy vé");
		JMenuItem ItemDoiVe= new JMenuItem("Đổi vé");
		JMenuItem ItemLapHoaDon = new JMenuItem("Lập hóa đơn");
		mnuNghiepVuVe.add(ItemDatVe);
		mnuNghiepVuVe.add(ItemHuyVe);
		mnuNghiepVuVe.add(ItemDoiVe);
		mnuNghiepVuVe.add(ItemLapHoaDon);
		
		JMenuItem Item_QLNV = new JMenuItem("Quản lý nhân viên");
		JMenuItem Item_QLHK = new JMenuItem("Quản lý hành khách");
		JMenuItem Item_QLCT = new JMenuItem("Quản lý chuyến tàu");
		JMenuItem Item_QLKM = new JMenuItem("Quản lý khuyến mãi");
		mnuQly.add(Item_QLNV);
		mnuQly.add(Item_QLHK);
		mnuQly.add(Item_QLCT);
		mnuQly.add(Item_QLKM);
		
		
		JMenuItem ItemTimVe = new JMenuItem("Tìm vé");
		JMenuItem ItemChuyenTau = new JMenuItem("Tìm Chuyến tàu");
		JMenuItem ItemTimKhachHang = new JMenuItem("Tìm khách hàng");
		mnuTraCuu.add(ItemTimVe);
		mnuTraCuu.add(ItemChuyenTau);
		mnuTraCuu.add(ItemTimKhachHang);
		
		JMenuItem ItemTkDoanhThu = new JMenuItem("Thống kê doanh thu");
		JMenuItem ItemTkSoLuongHanhKhach = new JMenuItem("Thống kê hành khách");
		JMenuItem ItemTkVeDoiHuy = new JMenuItem("Thống kê vé đổi/hủy");
		mnuThongKe.add(ItemTkDoanhThu);
		mnuThongKe.add(ItemTkSoLuongHanhKhach);
		mnuThongKe.add(ItemTkVeDoiHuy);
		
		JMenuItem ItemHdsd = new JMenuItem("Hướng dẫn sử dụng");
		JMenuItem ItemThongtinApp = new JMenuItem("Thông tin ứng dụng");
		mnuTroGiup.add(ItemHdsd);
		mnuTroGiup.add(ItemThongtinApp);
	
		menuBar.add(mnuHeThong);
		menuBar.add(mnuNghiepVuVe);
		menuBar.add(mnuQly);
		menuBar.add(mnuTraCuu);
		menuBar.add(mnuThongKe);
		menuBar.add(mnuTroGiup);

		Font menuFont = new Font("Segoe UI", Font.BOLD, 18);
		Font menuItemFont = new Font("Segoe UI", Font.BOLD, 16);
		
		for(int i = 0; i < menuBar.getMenuCount(); i++) {
			JMenu menu = menuBar.getMenu(i);
			menu.setFont(menuFont);
			for(int j = 0;j < menu.getItemCount();j++) {
				JMenuItem menuItem = menu.getItem(j);
				if(menuItem != null) {
					menuItem.setFont(menuItemFont);
				}
			}
		}
		
		menuBar.add(Box.createHorizontalGlue());
		
		ImageIcon NVBV_IC = chinhKichThuoc("/img/nhanVienBanVe.png", 50, 50);
		JLabel lbl_NVBV = new JLabel(NVBV_IC);
		JLabel lblXinChao = new JLabel("Xin Chào, [Tên Nhân Viên] ");
		
		lblXinChao.setForeground(Color.BLACK);
		lblXinChao.setFont(new Font("Segoe UI", Font.BOLD, 18));
		
		menuBar.add(lblXinChao);
		menuBar.add(lbl_NVBV);
		
		return menuBar;
		
	}

    // ================== HÀM PHỤ ==================
    private JButton taoButton(String text, Color bg, String iconPath) {
        JButton btn = new JButton(text, chinhKichThuoc(iconPath, 24, 24));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(250, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD,16 ));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
        URL iconUrl = ThongKeBaoCaoDoanhThu.class.getResource(duongDan);
        if (iconUrl == null) return null;
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnCot) bieuDoHienTai = 0;
        else if (src == btnDuong) bieuDoHienTai = 1;
        else if (src == btnTron) bieuDoHienTai = 2;

        chartPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ThongKeBaoCaoDoanhThu::new);
    }
}
