package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ManHinhChinh extends JFrame implements ActionListener{
	private JMenuBar menuBar;


	public ManHinhChinh() throws IOException{
		setTitle("Nhân viên bán vé");
		setSize(1500,1000);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

//		backgroundPanel BackgPn = new backgroundPanel("/img/train.jpg");
//		BackgPn.setLayout(null);
//		this.setContentPane(BackgPn);
		setJMenuBar(taoMenuBar());
		
	}
	
	public JMenuBar taoMenuBar() {
		menuBar = new JMenuBar();
		menuBar.setBorder(BorderFactory.createEmptyBorder(8,0,8,0));
		
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

	
	public static void main(String[] args) throws IOException {
		ManHinhChinh mhc = new ManHinhChinh();
		mhc.setVisible(true);
		
	}
}

