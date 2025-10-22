package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

public class TraCuuChuyenTauGUI extends JFrame {
	private JPanel pnlChinh;
	private JPanel pnlTraCuu;
	private JLabel lblGaden;
	private JLabel lblGadi;
	private JLabel lblNgayDi;
	private JComboBox cbGaDi;
	private JComboBox cbGaDen;
	private JTextField txtNgayDi;
	private JPanel pnlThongTinTim;
	private JLabel lblTextChinh;
	private JPanel pnlTitle;
	private JCalendar chonNgay;
	private JPanel pnlNutBam;
	private JButton btnXoaTrang;
	private JButton btnTim;
	private JPanel pnlTrungTam;
	private JPanel pnlVe;
	private JScrollPane ScPTrungTam;
	private JPanel pnlNutChucNang;
	private JButton btnTroVe;
	private JLabel lblTenChuyen;
	private JPanel pnlNoiDungChuyen;
	private JLabel lblTGDi;
	private Component lblTGDen;
	private JLabel lblGioDen;
	private JLabel lblSLChotrong;
	private JLabel lblSLChoDat;
	private JButton btnDatVe;

	public TraCuuChuyenTauGUI() {
		setTitle("Tra cứu chuyến tàu");
		setSize(1500,1000);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		pnlChinh = new JPanel(new BorderLayout());
		
		//Title
		pnlTitle = new JPanel();
		pnlTitle.setBorder(BorderFactory.createEtchedBorder());
		
		lblTextChinh = new JLabel("TRA CỨU CHUYẾN TÀU");
		lblTextChinh.setFont(new Font("Segoe UI", Font.BOLD, 55));
		lblTextChinh.setForeground(new Color(74, 140, 103));
		lblTextChinh.setHorizontalAlignment(SwingConstants.CENTER);
		pnlTitle.add(lblTextChinh,BorderLayout.NORTH);
		
		//Thông tin tra cứu	
		pnlThongTinTim = new JPanel(new GridBagLayout());
		pnlTraCuu = new JPanel(new BorderLayout());;
		Font title = new Font("Segoe UI", Font.BOLD, 25);
		TitledBorder titleBorder = BorderFactory.createTitledBorder("THÔNG TIN TRA CỨU");
		titleBorder.setTitleFont(title);
		titleBorder.setTitleColor(new Color(93, 156, 236));
		pnlTraCuu.setBorder(titleBorder);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		
		//GA ĐI
		lblGadi = new JLabel("Ga đi: ");
		lblGadi.setFont(new Font("Segoe UI", Font.BOLD, 20));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.0;
		gbc.anchor = GridBagConstraints.WEST;
		pnlThongTinTim.add(lblGadi,gbc);
		
		cbGaDi = new JComboBox();
		cbGaDi.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		pnlThongTinTim.add(cbGaDi, gbc);
		
		//GA ĐẾN
		lblGaden = new JLabel("Ga đến: ");
		lblGaden.setFont(new Font("Segoe UI", Font.BOLD, 20));
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.0;
		gbc.anchor = GridBagConstraints.WEST;
		pnlThongTinTim.add(lblGaden,gbc);
		
		cbGaDen = new JComboBox();
		cbGaDen.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		pnlThongTinTim.add(cbGaDen,gbc);
		
		//NGÀY ĐI
		lblNgayDi = new JLabel("Ngày đi: ");
		lblNgayDi.setFont(new Font("Segoe UI", Font.BOLD, 20));
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.0;
		gbc.anchor = GridBagConstraints.WEST;
		pnlThongTinTim.add(lblNgayDi,gbc);
		
		JDateChooser dateChooser = new JDateChooser();		
		dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		dateChooser.setPreferredSize(new Dimension(0, 35)); 
		pnlThongTinTim.add(dateChooser,gbc);
		
		//NÚT BẤM
		pnlNutBam = new JPanel(new GridLayout(1, 2, 10, 0));
		btnTim = new JButton("Tìm");
		btnXoaTrang = new JButton("Làm mới");
		btnTim.setFont(new Font("Segoe UI", Font.BOLD, 17));
		btnXoaTrang.setFont(new Font("Segoe UI", Font.BOLD, 17));
		btnXoaTrang.setBackground(new Color(229, 115, 115));
		btnXoaTrang.setForeground(Color.white);
		btnTim.setBackground(new Color(93, 156, 236));
		btnTim.setForeground(Color.white);
		ImageIcon timIC = GiaoDienChinh.chinhKichThuoc("/img/traCuu.png", 25, 25);
		btnTim.setIcon(timIC);
		ImageIcon lamMoiIC = GiaoDienChinh.chinhKichThuoc("/img/undo.png", 25, 25);
		btnXoaTrang.setIcon(lamMoiIC);
		
		pnlNutBam.add(btnXoaTrang);
		pnlNutBam.add(btnTim);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		
		pnlThongTinTim.add(pnlNutBam, gbc);
		
		pnlTraCuu.setPreferredSize(new Dimension(400, 0));
		pnlTraCuu.add(pnlThongTinTim, BorderLayout.NORTH);
		
		pnlTrungTam = new JPanel(new GridLayout(0, 3, 15, 15));
		
		TitledBorder titleBorderTrungTam = BorderFactory.createTitledBorder("KẾT QUẢ TRA CỨU");
		titleBorderTrungTam.setTitleFont(title);
		titleBorderTrungTam.setTitleColor(new Color(229, 115, 115));
		pnlTrungTam.setBorder(titleBorderTrungTam);
		
		ScPTrungTam = new JScrollPane(pnlTrungTam);

		
		//vé mô phỏng
		JPanel ve1 = taoVe();
		pnlTrungTam.add(ve1);
		JPanel ve2 = taoVe();
		pnlTrungTam.add(ve2);
		JPanel ve3 = taoVe();
		pnlTrungTam.add(ve3);
		JPanel ve4 = taoVe();
		pnlTrungTam.add(ve4);
		JPanel ve5 = taoVe();
		pnlTrungTam.add(ve5);
		JPanel ve6 = taoVe();
		pnlTrungTam.add(ve6);
		JPanel ve7 = taoVe();
		pnlTrungTam.add(ve7);
		JPanel ve8 = taoVe();
		pnlTrungTam.add(ve8);
		JPanel ve9 = taoVe();
		pnlTrungTam.add(ve9);
		
		//Nút chức năng phía dưới
		pnlNutChucNang = new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnTroVe = new JButton("Trở về");
		btnTroVe.setFont(new Font("Segoe UI", Font.BOLD, 17));
		btnTroVe.setBackground(new Color(229, 115, 115));
		btnTroVe.setForeground(Color.white);
		pnlNutChucNang.add(btnTroVe);
		
		
		pnlChinh.add(pnlNutChucNang, BorderLayout.SOUTH);
		pnlChinh.add(ScPTrungTam, BorderLayout.CENTER);
		pnlChinh.add(pnlTitle, BorderLayout.NORTH);
		pnlChinh.add(pnlTraCuu, BorderLayout.WEST);
		
		add(pnlChinh);
		
	}
	public JPanel taoVe() {
		pnlVe = new JPanel(new BorderLayout());
		pnlVe.setPreferredSize(new Dimension(200, 200));
//		pnlVe.setBackground(new Color(138, 187, 108));
		pnlVe.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		pnlNoiDungChuyen = new JPanel(new GridBagLayout());
	    
//		pnlNoiDungChuyen.setBackground(new Color(138, 187, 108));
		GridBagConstraints gbc = new GridBagConstraints();
		
		lblTenChuyen = new JLabel("SE[MaChuyen]");
		lblTenChuyen.setFont(new Font("Segoe UI",Font.BOLD, 40));
		lblTenChuyen.setHorizontalAlignment(SwingConstants.CENTER);
//		gbc.insets = new Insets(4,8,4,8);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.weightx= 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		pnlNoiDungChuyen.add(lblTenChuyen, gbc);

		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		
		lblTGDi = new JLabel("TG ĐI: ");
		lblTGDi.setFont(new Font("Segoe UI",Font.BOLD, 20));
		gbc.gridx = 0;
		gbc.gridy = 1;
//		gbc.weightx = 0.0;
		gbc.anchor = GridBagConstraints.WEST;
		pnlNoiDungChuyen.add(lblTGDi, gbc);

		JLabel lblGioDi = new JLabel("dd/MM HH:mm"); 
		lblGioDi.setFont(new Font("Segoe UI",Font.BOLD, 20));
		lblGioDi.setForeground(new Color(229, 115, 115)); 

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		pnlNoiDungChuyen.add(lblGioDi, gbc);
		
		lblTGDen = new JLabel("TG ĐẾN: ");
		lblTGDen.setFont(new Font("Segoe UI",Font.BOLD, 20));
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.0;
		gbc.anchor = GridBagConstraints.WEST;
		pnlNoiDungChuyen.add(lblTGDen, gbc);
		
		
		lblGioDen = new JLabel("dd/MM HH:mm"); 
		lblGioDen.setFont(new Font("Segoe UI",Font.BOLD, 20));
		lblGioDen.setForeground(new Color(229, 115, 115));
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		pnlNoiDungChuyen.add(lblGioDen, gbc);
		//SỐ lượng DAT
		lblSLChoDat = new JLabel("SL CHO DAT: ");
		lblSLChoDat.setFont(new Font("Segoe UI",Font.BOLD, 20));
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.0;
		gbc.anchor = GridBagConstraints.WEST;
		pnlNoiDungChuyen.add(lblSLChoDat, gbc);

		JLabel lblChoDat = new JLabel("[XXX]"); 
		lblChoDat.setFont(new Font("Segoe UI",Font.BOLD, 20));
		lblChoDat.setForeground(new Color(229, 115, 115)); 

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		pnlNoiDungChuyen.add(lblChoDat, gbc);
		
		//SL CHO TRONG
		lblSLChotrong = new JLabel("SL CHO TRONG: ");
		lblSLChotrong.setFont(new Font("Segoe UI",Font.BOLD, 20));
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0.0;
		gbc.anchor = GridBagConstraints.WEST;
		pnlNoiDungChuyen.add(lblSLChotrong, gbc);

		JLabel lblChotrong = new JLabel("[XXX]"); 
		lblChotrong.setFont(new Font("Segoe UI",Font.BOLD, 20));
		lblChotrong.setForeground(new Color(229, 115, 115)); 

		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		pnlNoiDungChuyen.add(lblChotrong, gbc);
		pnlVe.add(pnlNoiDungChuyen, BorderLayout.NORTH);
		
		btnDatVe = new JButton("Đặt vé"); 
		btnDatVe.setFont(new Font("Segoe UI",Font.BOLD, 20));
		btnDatVe.setBackground(new Color(74, 140, 103)); 
		btnDatVe.setForeground(Color.white);

		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.EAST;
		pnlNoiDungChuyen.add(btnDatVe, gbc);
		pnlVe.add(pnlNoiDungChuyen, BorderLayout.NORTH);
		
		return pnlVe;
		
	}
	public static void main(String[] args) {
		LookAndFeelManager.setNimbusLookAndFeel();
		TraCuuChuyenTauGUI tcct = new TraCuuChuyenTauGUI();
		tcct.setVisible(true);
		
	}
	
}
