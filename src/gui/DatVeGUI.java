package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;

public class DatVeGUI extends JFrame implements ActionListener{

	private JPanel pnlTraCuu;
	private JPanel pnlChinh;
	private JLabel lblGaDi;
	private JLabel lblGaDen;
	private JLabel lblNgayDi;
	private JComboBox cbGaDi;
	private JPanel pnlNut;
	private JPanel pnlThongTinHK;
	private JComboBox cbGaDen;
	private JButton btnTimCT;
	private JLabel lblT;
	private JLabel lblHoTen;
	private JTextField txtHoTen;
	private JLabel lbl_CCCD;
	private JTextField txt_CCCD;
	private JLabel lbl_SDT;
	private JTextField txt_SDT;
	private JLabel lblNgaySinh;
	private JPanel pnlKetQuaTC;
	private JTable tableKetqua;
	private JLabel lblThoiGian;
	private JLabel clockLabel;
	private JPanel pnlTrai;
	
	DatVeGUI() throws IOException{
		ManHinhChinh mhc = new ManHinhChinh();
		JMenuBar jmb = mhc.taoMenuBar();
		
		setSize(1500,1000);
		setTitle("ĐẶT VÉ");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		lblThoiGian = new JLabel();
		
		pnlChinh = new JPanel(new BorderLayout());
		
		pnlTraCuu = new JPanel();
		pnlTraCuu.setLayout(new FlowLayout());
		
		pnlKetQuaTC = new JPanel();
		pnlKetQuaTC.setBorder(BorderFactory.createTitledBorder("Kết quả tra cứu"));
		
		tableKetqua = new JTable();
		pnlNut = new JPanel();
		
		GridBagConstraints gbc = new GridBagConstraints();
	
		lblGaDi = new JLabel("Ga đi: ");
		lblGaDen = new JLabel("Ga đến: ");
		lblNgayDi = new JLabel("Ngày đi: ");
		cbGaDi = new JComboBox<>(new String[] {"Sài Gòn"});
		cbGaDen = new JComboBox<>(new String[] {"Hà Nội"});
		btnTimCT = new JButton("Tìm kiếm");
		btnTimCT.setBackground(new Color(28, 110, 164));
		btnTimCT.setForeground(new Color(245, 239, 230));

		lblGaDi.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblGaDen.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblNgayDi.setFont(new Font("Segoe UI", Font.BOLD, 18));
		cbGaDen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbGaDi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnTimCT.setFont(new Font("Segoe UI", Font.BOLD, 16));
		
		
		
		pnlTraCuu.add(lblGaDi);
		pnlTraCuu.add(cbGaDi);
		pnlTraCuu.add(lblGaDen);
		pnlTraCuu.add(cbGaDen);
		pnlTraCuu.add(lblNgayDi);
		pnlTraCuu.add(btnTimCT);
		
		pnlChinh.add(jmb,BorderLayout.NORTH);
		pnlChinh.add(pnlTraCuu,BorderLayout.CENTER);
		
		add(pnlChinh);
		
		
        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        clockLabel.setForeground(Color.WHITE); 
        clockLabel.setBackground(new Color(51, 51, 51)); 
        clockLabel.setOpaque(true);
        
        Border lineBorder = BorderFactory.createLineBorder(new Color(85, 85, 85), 2);
        Border paddingBorder = BorderFactory.createEmptyBorder(10, 15, 10, 15);
        clockLabel.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));

       
        Timer timer = new Timer(1000, e -> {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String timeString = LocalTime.now().format(timeFormatter);
            clockLabel.setText(timeString);
        });
        timer.start();

       
        pnlTrai = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTrai.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        pnlTrai.add(clockLabel);
        add(pnlTrai, BorderLayout.WEST);

	}
	
	public static void main(String[] args) throws IOException {
		DatVeGUI dvg = new DatVeGUI();
		dvg.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
