package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class NhanVienBanVeGUI  extends GiaoDienChinh implements ActionListener{
	private JButton btnDatVe;
	private JButton btnHuyVe;
	private JButton btnTimChuyenTau;
	private JButton btnDangXuat;
	private JButton btnDoiVe;

	public NhanVienBanVeGUI()throws IOException {
		super();
		pnlChucNang = taoPanelMenuChinh();
		pnlChinh.add(pnlChucNang, BorderLayout.CENTER);



	}

	public JPanel taoPanelMenuChinh() {
		pnlChucNang = new JPanel();
		pnlChucNang.setLayout(new GridLayout(0,2,0,10));
		pnlChucNang.setBackground(new Color(221, 218, 208));
		
		Color mauDatVe = new Color(74, 140, 103); 
		Color mauHuyVe = new Color(229, 115, 115); 
		Color mauDoiVe = new Color(93, 156, 236); 
		Color mauTimChuyen = new Color(155, 93, 224); 
		Color mauDangXuat = Color.orange; 
		
		btnDatVe = new JButton("ĐẶT VÉ");
		btnDatVe.setBackground(Color.white);
		btnDatVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
		btnDatVe.setForeground(new Color(74, 140, 103));
		ImageIcon icDatVe = chinhKichThuoc("/img/tickets_icon.png", 60, 60);
		btnDatVe.setIcon(icDatVe);
		
		btnHuyVe = new JButton("HỦY VÉ");
		btnHuyVe.setBackground(Color.white);
		btnHuyVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
		btnHuyVe.setForeground(new Color(229, 115, 115));
		ImageIcon icHuyve = chinhKichThuoc("/img/cancel2.png", 55, 55);
		btnHuyVe.setIcon(icHuyve);
		

		btnDoiVe = new JButton("ĐỔI VÉ");
		btnDoiVe.setBackground(Color.white);
		btnDoiVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
		btnDoiVe.setForeground(new Color(93, 156, 236));
		ImageIcon icDoiVe = chinhKichThuoc("/img/exchange.png", 55, 55);
		btnDoiVe.setIcon(icDoiVe);
		
		
		btnTimChuyenTau = new JButton("TÌM CHUYẾN");
		btnTimChuyenTau.setBackground(Color.white);
		btnTimChuyenTau.setFont(new Font("Segoe UI", Font.BOLD, 35));
		btnTimChuyenTau.setForeground(new Color(155, 93, 224));
		ImageIcon icTimChuyen = chinhKichThuoc("/img/search.png", 65, 65);
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
		
		addHoverEffect(btnDatVe, mauDatVe);
		addHoverEffect(btnHuyVe, mauHuyVe);
		addHoverEffect(btnDoiVe, mauDoiVe);
		addHoverEffect(btnTimChuyenTau, mauTimChuyen);
		addHoverEffect(btnDangXuat, mauDangXuat);
		
		return pnlChucNang;
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}

