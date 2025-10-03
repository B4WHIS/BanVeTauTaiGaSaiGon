package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.Timer;

public class ManHinhChinh extends JFrame implements ActionListener{
	private JMenuBar menuBar;
	private JToolBar toolBar;
	private JButton btnDatVe;
	private JButton btnHuyVe;
	private JButton btnTimChuyenTau;
	private JButton btnDangXuat;
	private JButton btnDoiVe;
	private JPanel pnlTBar;
	private JPanel pnlChinh;
	private JPanel pnlChucNang;
	private JLabel lblNguoiDung;
	private JPanel pnlNorth;
	private JLabel lblTenND;
	private JPanel pnlThongTin;
	private JPanel pnlDate;
	private JLabel lblNgay;
	private JLabel lblToday;
	private JLabel lblGio;
	private JLabel lblNgayHienTai;
	private JLabel lblGioThuc;


	public ManHinhChinh() throws IOException{
		setTitle("Nhân viên bán vé");
		setSize(1500,1000);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setJMenuBar(taoMenuBar());
		
		pnlChinh = new JPanel(new BorderLayout());
		pnlChucNang = new JPanel();
		pnlChucNang.setLayout(new GridLayout(0,2,0,10));
		pnlChucNang.setBackground(new Color(221, 218, 208));
		
		btnDatVe = new JButton("ĐẶT VÉ");
		btnDatVe.setBackground(Color.white);
		btnDatVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
		btnDatVe.setForeground(new Color(74, 140, 103));
		ImageIcon icDatVe = ManHinhChinh.chinhKichThuoc("/img/tickets_icon.png", 60, 60);
		btnDatVe.setIcon(icDatVe);
		
		btnHuyVe = new JButton("HỦY VÉ");
		btnHuyVe.setBackground(Color.white);
		btnHuyVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
		btnHuyVe.setForeground(new Color(229, 115, 115));
		ImageIcon icHuyve = ManHinhChinh.chinhKichThuoc("/img/cancel2.png", 55, 55);
		btnHuyVe.setIcon(icHuyve);
		

		btnDoiVe = new JButton("ĐỔI VÉ");
		btnDoiVe.setBackground(Color.white);
		btnDoiVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
		btnDoiVe.setForeground(new Color(93, 156, 236));
		ImageIcon icDoiVe = ManHinhChinh.chinhKichThuoc("/img/exchange.png", 55, 55);
		btnDoiVe.setIcon(icDoiVe);
		
		
		btnTimChuyenTau = new JButton("TÌM CHUYẾN");
		btnTimChuyenTau.setBackground(Color.white);
		btnTimChuyenTau.setFont(new Font("Segoe UI", Font.BOLD, 35));
		btnTimChuyenTau.setForeground(new Color(155, 93, 224));
		ImageIcon icTimChuyen = ManHinhChinh.chinhKichThuoc("/img/search.png", 65, 65);
		btnTimChuyenTau.setIcon(icTimChuyen);
		
		btnDangXuat = new JButton("ĐĂNG XUẤT");
		btnDangXuat.setBackground(Color.white);
		btnDangXuat.setFont(new Font("Segoe UI", Font.BOLD, 30));
		btnDangXuat.setForeground(Color.orange);
		ImageIcon icDangXuat = ManHinhChinh.chinhKichThuoc("/img/export.png", 55, 55);
		btnDangXuat.setIcon(icDangXuat);
		
		pnlChucNang.add(btnDatVe);
		pnlChucNang.add(btnHuyVe);
		pnlChucNang.add(btnDoiVe);
		pnlChucNang.add(btnTimChuyenTau);
		pnlChucNang.add(btnDangXuat);

		
		pnlNorth = new JPanel(new GridLayout(2,1,10,10));
		pnlNorth.setBackground(new Color(225, 242, 232));
		pnlNorth.setPreferredSize(new Dimension(0, 100));
		pnlNorth.setBorder(BorderFactory.createEtchedBorder());
		
		pnlThongTin = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlThongTin.setBackground(new Color(225, 242, 232));
		lblNguoiDung = new JLabel("Người dùng: ");
		lblNguoiDung.setFont(new Font("Segoe UI", Font.BOLD, 25));
		lblTenND = new JLabel("Nhân viên bán vé - NGUYỄN VĂN A");
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
		
		pnlChucNang.setPreferredSize(new Dimension(350, 70));
		
		pnlChinh.add(pnlNorth,BorderLayout.NORTH);
		pnlChinh.add(pnlChucNang, BorderLayout.CENTER);
		
		add(pnlChinh);
		
	}
	
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
		JMenuItem Item_QLHK = new JMenuItem("Quản lý hành khác");
		JMenuItem Item_QLCT = new JMenuItem("Quản lý chuyến tàu");
		JMenuItem Item_QLKM = new JMenuItem("Quản lý khuyến mãi");
		mnuQly.add(Item_QLNV);
		mnuQly.add(Item_QLHK);
		mnuQly.add(Item_QLCT);
		mnuQly.add(Item_QLKM);
		
		
		JMenuItem ItemTimVe = new JMenuItem("Tìm vé");
		JMenuItem ItemChuyenTau = new JMenuItem("Chuyến tàu");
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
		JLabel lblXinChao = new JLabel("Xin Chào, Nguyễn Văn A ");
		
		lblXinChao.setForeground(Color.BLACK);
		lblXinChao.setFont(new Font("Segoe UI", Font.BOLD, 18));
		
		menuBar.add(lblXinChao);
		menuBar.add(lbl_NVBV);
		
		return menuBar;
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
		URL iconUrl = ManHinhChinh.class.getResource(duongDan);
		
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
	
	public static void main(String[] args) throws IOException {
		ManHinhChinh mhc = new ManHinhChinh();
		mhc.setVisible(true);
	}
}

