package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
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
	private JPanel pnlForm;
	private JPanel pnlNutBam;
	private JButton btnThoat;
	private JPanel pnlGop;
	private Object iconThoat;

	DangNhapGUI() throws IOException{
		setTitle("Đăng nhập");
		setSize(1000,650);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		//Bo Cuc
		pnlChinh = new JPanel(new GridLayout(1,2));
		pnlTrai = new JPanel();
		pnlPhai = new JPanel();
		
		pnlTrai.setBackground(new Color(250, 247, 243));
		pnlPhai.setBackground(new Color(250, 247, 243));
		
		//Trái
		pnlTrai.setLayout(new GridBagLayout());
		pnlGop = new JPanel();
		pnlGop.setLayout(new BoxLayout(pnlGop, BoxLayout.Y_AXIS));
		pnlGop.setBackground(new Color(250, 247, 243));
		
		//thành phần
		lblGsg = new JLabel("GA SÀI GÒN");
		lblGsg.setFont(new Font("Segoe UI", Font.BOLD, 60));
		lblGsg.setForeground(new Color(74, 140, 103));
		lblGsg.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		ImageIcon TauIC = ManHinhChinh.chinhKichThuoc("/img/trainDN.png",497,300);
		lblImg = new JLabel(TauIC);
		lblImg.setAlignmentX(Component.CENTER_ALIGNMENT);

		//thêm vào trái
		pnlGop.add(lblGsg);
		pnlGop.add(lblImg);
		pnlTrai.add(pnlGop);
		
		//Phải
		pnlPhai.setLayout(new BorderLayout(10,10));

		//form nhập
		pnlForm = new JPanel(new GridBagLayout());
		pnlForm.setBackground(new Color(250, 247, 243));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		
		//Đăng Nhập
		lblDn = new JLabel("ĐĂNG NHẬP");
		lblDn.setFont(new Font("Segoe UI", Font.BOLD, 40));
		lblDn.setForeground(new Color(66, 66, 66));
		gbc.gridx= 0;
		gbc.gridy= 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		
		pnlForm.add(lblDn,gbc);
	
		lblTenDN = new JLabel("Tên đăng nhập");
		lblTenDN.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTenDN.setForeground(new Color(158, 158, 158));
		gbc.gridx= 0;
		gbc.gridy= 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		pnlForm.add(lblTenDN,gbc);
		
	    txtTenDN = new JTextField(20);
	    txtTenDN.setFont(new Font("Segoe UI", Font.BOLD, 16));
		gbc.gridx= 1;
		gbc.gridy= 1;

		gbc.anchor = GridBagConstraints.LINE_START;
		pnlForm.add(txtTenDN,gbc);
		
	    //Mật khẩu
	    lblMatKhau = new JLabel("Mật khẩu");
	    lblMatKhau.setFont(new Font("Segoe UI", Font.BOLD, 20));
	    lblMatKhau.setForeground(new Color(158, 158, 158));
	    gbc.anchor = GridBagConstraints.LINE_END;
	    
		gbc.gridx= 0;
		gbc.gridy= 2;
		pnlForm.add(lblMatKhau,gbc);
		
	    txtMatKhau = new JPasswordField(20);
	    txtMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 16));
	    gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridx= 1;
		gbc.gridy= 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		pnlForm.add(txtMatKhau,gbc);

	    
	    //nut bam	
		pnlNutBam = new JPanel(new GridLayout(1, 2, 10, 0));

	    btnThoat = new JButton();
	    btnThoat.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    btnThoat.setBackground(new Color(229, 115, 115));
	    btnThoat.setForeground(Color.WHITE);
	    btnThoat.setText("Thoát");
	    ImageIcon iconThoat = ManHinhChinh.chinhKichThuoc("/img/thoaticon.png", 25, 25);
	    btnThoat.setIcon(iconThoat);

	    pnlNutBam.add(btnThoat);
	    
	    btnDn = new JButton("Đăng nhập");
	    btnDn.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    btnDn.setBackground(new Color(93, 156, 236));
	    btnDn.setForeground(Color.WHITE);
	    ImageIcon iconDN = ManHinhChinh.chinhKichThuoc("/img/loginicon.png", 25, 25);
	    btnDn.setIcon(iconDN);


	    pnlNutBam.add(btnDn);
		gbc.gridx= 1;
		gbc.gridy= 3;
		gbc.anchor = GridBagConstraints.EAST;
		
		pnlForm.add(pnlNutBam,gbc);
	    //them
	    pnlPhai.add(pnlForm);

	    
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
