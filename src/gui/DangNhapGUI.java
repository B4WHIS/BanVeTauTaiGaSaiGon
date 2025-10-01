package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DangNhapGUI extends JFrame implements ActionListener {
	
	private JPanel pnlChinh;
	private JPanel pnlTrai;
	private JLabel lblImg;
	private JPanel pnlPhai;
	private JLabel lblDn;
	private JLabel lblTenDN;
	private JTextField txtTenDN;
	private JLabel lblMatKhau;
	private JPasswordField txtMatKhau;
	private JButton btnDn;
	private JLabel lblGsg;

	DangNhapGUI() throws IOException{
		setTitle("Đăng nhập");
		setSize(1000,650);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		pnlChinh = new JPanel(new GridLayout(1,2));
		pnlTrai = new JPanel();
		
		pnlTrai.setBackground(new Color(236, 238, 223));
		
		ImageIcon TauIC = ManHinhChinh.chinhKichThuoc("/img/trainDN.png",400,400);
		lblImg = new JLabel(TauIC);
		
		
		lblGsg = new JLabel("GA SÀI GÒN");
		lblGsg.setFont(new Font("Segoe UI", Font.BOLD, 70));
		lblGsg.setForeground(new Color(207, 171, 141));
		
		pnlTrai.add(lblImg);
		pnlTrai.add(lblGsg);
		
		//PNL PHẢI
		pnlPhai = new JPanel(new GridBagLayout());
		pnlPhai.setBackground(new Color(236, 238, 223));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		 
//		 rgb(187, 220, 229)
//		ImageIcon NVIC = ManHinhChinh.chinhKichThuoc("/img/nhanVienBanVe.png",50,50);
//		lblNV = new JLabel();
		
		 
		 lblDn = new JLabel("ĐĂNG NHẬP");
		 lblDn.setFont(new Font("Segoe UI", Font.BOLD, 36));
		 lblDn.setForeground(new Color(207, 171, 141));
		 
		 gbc.gridx = 0;
		 gbc.gridy = 0;
		 gbc.gridwidth = 5;
		 gbc.anchor = GridBagConstraints.CENTER;
		 
//		 pnlPhai.add(lblNV, gbc);
		 pnlPhai.add(lblDn,gbc);
		 
		 //Đăng Nhập
		 lblTenDN = new JLabel("Tên đăng nhập");
		 lblTenDN.setFont(new Font("Segoe UI", Font.BOLD, 16));
		 lblTenDN.setForeground(new Color(207, 171, 141));

		 gbc.gridx = 0;
	     gbc.gridy = 1;
	     gbc.gridwidth = 1;
	     gbc.anchor = GridBagConstraints.LINE_END;
	     pnlPhai.add(lblTenDN, gbc);
	     
	     
	     txtTenDN = new JTextField(20);
	     txtTenDN.setFont(new Font("Segoe UI", Font.PLAIN, 16));
	     gbc.gridx = 1;
	     gbc.gridy = 1;
	     gbc.anchor = GridBagConstraints.LINE_START;
	     pnlPhai.add(txtTenDN, gbc);
	     
	     //Mật khẩu
	     lblMatKhau = new JLabel("Mật khẩu");
	     lblMatKhau.setFont(new Font("Segoe UI", Font.BOLD, 16));
	     lblMatKhau.setForeground(new Color(207, 171, 141));
	     gbc.gridx = 0;
	     gbc.gridy = 2;
	     gbc.anchor = GridBagConstraints.LINE_END;
	     pnlPhai.add(lblMatKhau, gbc);
		
	     txtMatKhau = new JPasswordField(20);
	     txtMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 16));
	     gbc.gridx = 1;
	     gbc.gridy = 2;
	     gbc.anchor = GridBagConstraints.LINE_START;
	     pnlPhai.add(txtMatKhau, gbc);
	     
	     btnDn = new JButton("Đăng nhập");
	     btnDn.setFont(new Font("Segoe UI", Font.BOLD, 18));
	     btnDn.setBackground(new Color(0, 102, 204));
	     btnDn.setForeground(Color.WHITE);
	     btnDn.setPreferredSize(new Dimension(150, 40));
	     gbc.gridx = 0;
	     gbc.gridy = 3;
	     gbc.gridwidth = 2;
	     gbc.anchor = GridBagConstraints.CENTER;
	     pnlPhai.add(btnDn, gbc);

	     pnlChinh.add(pnlTrai);
	     pnlChinh.add(pnlPhai);
	     add(pnlChinh);
	}
	
	public static void main(String[] args) throws IOException {
		DangNhapGUI dn = new DangNhapGUI();
		dn.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
