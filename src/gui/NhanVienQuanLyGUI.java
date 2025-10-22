package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class NhanVienQuanLyGUI extends GiaoDienChinh{
	public NhanVienQuanLyGUI() {
		super();
		pnlChucNang = taoPanelMenuChinh();
		pnlChinh.add(pnlChucNang);
		
	}
	public JPanel taoPanelMenuChinh() {
		
		pnlChucNang = new JPanel();
		pnlChucNang.setLayout(new GridLayout(0,2,0,10));
		pnlChucNang.setBackground(new Color(221, 218, 208));
		
		btnDatVe = new JButton("QUẢN LÝ NHÂN VIÊN");
		btnDatVe.setBackground(Color.white);
		btnDatVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
		btnDatVe.setForeground(new Color(93, 156, 236));
		ImageIcon icDatVe = chinhKichThuoc("/img/group.png", 60, 60);
		btnDatVe.setIcon(icDatVe);
		
		btnHuyVe = new JButton("QUẢN LÝ HÀNH KHÁCH");
		btnHuyVe.setBackground(Color.white);
		btnHuyVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
		btnHuyVe.setForeground(new Color(74, 140, 103));
		ImageIcon icHuyve = chinhKichThuoc("/img/passenger.png", 55, 55);
		btnHuyVe.setIcon(icHuyve);
		

		btnDoiVe = new JButton("QUẢN LÝ CHUYẾN TÀU");
		btnDoiVe.setBackground(Color.white);
		btnDoiVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
		btnDoiVe.setForeground(new Color (247, 82, 112));
		ImageIcon icDoiVe = chinhKichThuoc("/img/QLtrain.png", 55, 55);
		btnDoiVe.setIcon(icDoiVe);
		
		
		btnTimChuyenTau = new JButton("QUẢN LÝ KHUYẾN MÃI");
		btnTimChuyenTau.setBackground(Color.white);
		btnTimChuyenTau.setFont(new Font("Segoe UI", Font.BOLD, 35));
		btnTimChuyenTau.setForeground(new Color (228, 90, 146));
		ImageIcon icTimChuyen = chinhKichThuoc("/img/coupon.png", 65, 65);
		btnTimChuyenTau.setIcon(icTimChuyen);
		
		btnDangXuat = new JButton("ĐĂNG XUẤT");
		btnDangXuat.setBackground(Color.white);
		btnDangXuat.setFont(new Font("Segoe UI", Font.BOLD, 30));
		btnDangXuat.setForeground(Color.orange);
		ImageIcon icDangXuat = chinhKichThuoc("/img/export.png", 55, 55);
		btnDangXuat.setIcon(icDangXuat);
		
		pnlChucNang.add(btnDatVe);
		pnlChucNang.add(btnHuyVe);
		pnlChucNang.add(btnDoiVe);
		pnlChucNang.add(btnTimChuyenTau);
		pnlChucNang.add(btnDangXuat);
		pnlChucNang.setPreferredSize(new Dimension(350, 70));
		return pnlChucNang;
	}
	public static void main(String[] args) {
		NhanVienQuanLyGUI mh_nvql = new NhanVienQuanLyGUI();
		mh_nvql.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
