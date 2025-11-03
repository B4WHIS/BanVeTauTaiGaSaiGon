package gui;
//check
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import entity.NhanVien;

public abstract class GiaoDienChinh extends JFrame implements ActionListener{
    
  
    protected NhanVien nhanVien;
    
    protected JLabel lblGioThuc;
    protected JMenuBar menuBar;
    protected JButton btnDatVe;
    protected JButton btnHuyVe;
    protected JButton btnTimChuyenTau;
    protected JButton btnDangXuat;
    protected JButton btnDoiVe;
    protected JPanel pnlChinh;
    protected JPanel pnlChucNang;
    protected JLabel lblNguoiDung;
    protected JPanel pnlNorth;
    protected JLabel lblTenND;
    protected JPanel pnlThongTin;
    protected JPanel pnlDate;
    protected JLabel lblNgay;
    protected JLabel lblGio;
    protected JLabel lblNgayHienTai;
    

    public GiaoDienChinh(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
        initUI();
    }
    
   
    public GiaoDienChinh() {
        this(null);
    }
    
    private void initUI() {
        setTitle("Ứng dụng bán vé tàu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setJMenuBar(taoMenuBar());
        pnlChinh = new JPanel(new BorderLayout());
        pnlThongTin = taoPanelThongTin();
        pnlChinh.add(pnlNorth, BorderLayout.NORTH);
        add(pnlChinh);
        
      
        if (nhanVien != null) {
            capNhatThongTinNhanVien();
        }
    }
    
    
    protected void capNhatThongTinNhanVien() {
        
        String chucVu = getTenChucVu(nhanVien.getIDloaiChucVu());
        lblTenND.setText(chucVu + " - " + nhanVien.getHoTen());
        
        
        for (int i = 0; i < menuBar.getComponentCount(); i++) {
            if (menuBar.getComponent(i) instanceof JLabel) {
                JLabel lbl = (JLabel) menuBar.getComponent(i);
                if (lbl.getText().contains("Xin Chào")) {
                    lbl.setText("Xin Chào, " + nhanVien.getHoTen());
                    break;
                }
            }
        }
    }
    
    
    private String getTenChucVu(int id) {
        switch (id) {
            case 1: return "Nhân viên bán vé";
            case 2: return "Nhân viên quản lý";
            default: return "Không xác định";
        }
    }
    
    protected JPanel taoPanelThongTin() {
        pnlNorth = new JPanel(new GridLayout(2,1,10,10));
        pnlNorth.setBackground(new Color(225, 242, 232));
        pnlNorth.setPreferredSize(new Dimension(0, 100));
        pnlNorth.setBorder(BorderFactory.createEtchedBorder());
        
        pnlThongTin = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlThongTin.setBackground(new Color(225, 242, 232));
        lblNguoiDung = new JLabel("Người dùng: ");
        lblNguoiDung.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblTenND = new JLabel("[CHỨC VỤ] - [TÊN NHÂN VIÊN]");
        lblTenND.setFont(new Font("Segoe UI", Font.PLAIN, 25));
        pnlThongTin.add(lblNguoiDung);
        pnlThongTin.add(lblTenND);
        
        pnlDate = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlDate.setBackground(new Color(225, 242, 232));
        
        String ngay = ngayHienTai();
        lblNgay = new JLabel("Ngày: ");
        lblNgayHienTai = new JLabel(ngay);
        lblNgay.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblNgayHienTai.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblNgayHienTai.setForeground(new Color(229, 115, 115));
        
        lblGio = new JLabel(" Giờ hệ thống: ");
        lblGio.setFont(new Font("Segoe UI", Font.BOLD, 25));
        
        lblGioThuc = new JLabel();
        lblGioThuc.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblGioThuc.setForeground(new Color(229, 115, 115));
        
        pnlDate.add(lblNgay);
        pnlDate.add(lblNgayHienTai);
        pnlDate.add(lblGio);
        pnlDate.add(lblGioThuc);
        thoiGianThuc();
        
        pnlNorth.add(pnlThongTin);
        pnlNorth.add(pnlDate);
        return pnlNorth;
    }
    
    public JMenuBar taoMenuBar() {
		menuBar = new JMenuBar();
		menuBar.setBorder(BorderFactory.createEmptyBorder(8,0,8,0));
		menuBar.setBackground(new Color(220, 220, 220));
		JMenu mnuHeThong = new JMenu("Hệ Thống");
		JMenu mnuNghiepVuVe = new JMenu("Xử Lý");
		JMenu mnuQly = new JMenu("Danh Mục");
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

		// === CÀI ĐẶT SỰ KIỆN CHO TỪNG JMenuITEM ===
		ItemDX.addActionListener(e -> {
			try {
				dangXuat();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		ItemThoat.addActionListener(e -> thoatUngDung());

		ItemDatVe.addActionListener(e -> moGiaoDienDatVe());
		ItemHuyVe.addActionListener(e -> moGiaoDienHuyVe());
		ItemDoiVe.addActionListener(e -> moGiaoDienDoiVe());
		ItemLapHoaDon.addActionListener(e -> moGiaoDienLapHoaDon());

		Item_QLNV.addActionListener(e -> moQuanLyNhanVien());
		Item_QLHK.addActionListener(e -> moQuanLyHanhKhach());
		Item_QLCT.addActionListener(e -> moQuanLyChuyenTau());
		Item_QLKM.addActionListener(e -> moQuanLyKhuyenMai());

		ItemTimVe.addActionListener(e -> moGiaoDienTraCuuVeTau());
		ItemChuyenTau.addActionListener(e -> moGiaoDienTimChuyenTau());
		ItemTimKhachHang.addActionListener(e -> moGiaoDienTimKhachHang());

		ItemTkDoanhThu.addActionListener(e -> moThongKeDoanhThu());
		ItemTkSoLuongHanhKhach.addActionListener(e -> moThongKeHanhKhach());
		ItemTkVeDoiHuy.addActionListener(e -> moThongKeVeDoiHuy());

		ItemHdsd.addActionListener(e -> moHuongDanSuDung());
		ItemThongtinApp.addActionListener(e -> moThongTinUngDung());
		
		
		
		
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
 // === CÁC HÀM MỞ GIAO DIỆN ===
    private void moGiaoDienDatVe() {
        new GiaoDienTraCuuChuyentau(nhanVien).setVisible(true);
    }

    private void moGiaoDienHuyVe() {
        new GiaoDienTraCuuVeTau().setVisible(true); // Sẽ cần truyền danh sách vé sau
    }

    private void moGiaoDienDoiVe() {
        new GiaoDienTraCuuVeTau().setVisible(true);
    }

    private void moGiaoDienLapHoaDon() {
      //  new GiaoDienLapHoaDon().setVisible(true);
    }

    private void moQuanLyNhanVien() {
        new QuanLyNhanVien().setVisible(true);
    }

    private void moQuanLyHanhKhach() {
        new QuanLyHanhKhach().setVisible(true);
    }

    private void moQuanLyChuyenTau() {
        new QuanLyChuyenTau().setVisible(true);
    }

    private void moQuanLyKhuyenMai() {
        new QuanLykhuyenMai().setVisible(true);
    }

    private void moGiaoDienTraCuuVeTau() {
        new GiaoDienTraCuuVeTau().setVisible(true);
    }

    private void moGiaoDienTimChuyenTau() {
        new GiaoDienTraCuuChuyentau(nhanVien).setVisible(true);
    }

    private void moGiaoDienTimKhachHang() {
        new QuanLyHanhKhach().setVisible(true);
    }

    private void moThongKeDoanhThu() {
        new GiaoDienThongKe().setVisible(true);
    }

    private void moThongKeHanhKhach() {
        new GiaoDienThongKe().setVisible(true);
    }

    private void moThongKeVeDoiHuy() {
        new GiaoDienThongKe().setVisible(true);
    }

    private void moHuongDanSuDung() {
        JOptionPane.showMessageDialog(this, 
            "Hướng dẫn sử dụng:\n- Sử dụng menu để điều hướng.\n- Nhấn F11 để toàn màn hình.", 
            "Hướng dẫn", JOptionPane.INFORMATION_MESSAGE);
    }

    private void moThongTinUngDung() {
        JOptionPane.showMessageDialog(this, 
            "Ứng dụng Quản lý Bán Vé Tàu\nPhiên bản: 1.0\nNhóm phát triển: xAI Team", 
            "Thông tin ứng dụng", JOptionPane.INFORMATION_MESSAGE);
    }

    // === HỆ THỐNG ===
    private void dangXuat() throws IOException {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn đăng xuất?", "Đăng xuất", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new GiaoDienDangNhap().setVisible(true); // Giả sử có form đăng nhập
        }
    }

    private void thoatUngDung() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Thoát ứng dụng?", "Thoát", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
	    URL iconUrl = GiaoDienChinh.class.getResource(duongDan);
	    if (iconUrl == null) {
	        System.err.println("Không tìm thấy icon tại đường dẫn: " + duongDan); 
	        return null; 
	    }
	    ImageIcon iicGoc = new ImageIcon(iconUrl);
	    Image anhGoc = iicGoc.getImage();
	    Image anhDaDoi = anhGoc.getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
	    ImageIcon iicMoi = new ImageIcon(anhDaDoi);
	    return iicMoi;
	}
    
    public String ngayHienTai() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter dinhDangNgay = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String today = now.format(dinhDangNgay);	
		return today;
	}
	public JButton taoButton2(String text, Color bg, String iconPath) {
        ImageIcon icon = chinhKichThuoc(iconPath, 24, 24);
        JButton btn = new JButton(text, icon != null ? icon : null);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

	public JButton taoButton(String text, Color bg, String iconPath) {
	        ImageIcon icon = chinhKichThuoc(iconPath, 24, 24);
	        JButton btn = new JButton(text, icon != null ? icon : null);
	        btn.setBackground(bg);
	        btn.setForeground(Color.WHITE);
	        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
	        btn.setFocusPainted(false);
	        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
	        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	        btn.addMouseListener(new MouseAdapter() {
	            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
	            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
	        });
	        return btn;
	    }

	
	public void thoiGianThuc() {
		Timer dongHo = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				LocalTime gioHienTai = LocalTime.now();
				
				DateTimeFormatter dinhDangGio = DateTimeFormatter.ofPattern("HH:mm:ss");
				String chuoiGio = gioHienTai.format(dinhDangGio);
				
				lblGioThuc.setText(chuoiGio);
			}
		});
		dongHo.start();	
	}
	protected void addHoverEffect(JButton btn, Color mainColor) {
	    btn.setFocusPainted(false); 
	    
	    btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)); 
	    btn.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            btn.setBackground(new Color(240, 240, 240)); 
	           
	            btn.setBorder(BorderFactory.createLineBorder(mainColor, 3));
	        }
	        @Override
	        public void mouseExited(MouseEvent e) {
	            btn.setBackground(Color.white);
	            btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
	        }
	    });
	}
	}