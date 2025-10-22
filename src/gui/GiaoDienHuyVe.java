package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import control.QuanLyVeControl;
import entity.LichSuVe;
import entity.NhanVien;

public class GiaoDienHuyVe extends GiaoDienChinh {
	private JPanel jpmenu;
	private JPanel jpContent;
	private JPanel jpTrai;
	private JPanel jpPhai;
	private JButton btn;
	private CardLayout cardLayout;
	private JPanel jpB1;
	private JLabel lbltileB1;
	private JPanel jpInfo;
	private JLabel lblStd;
	private JTextField txtSDT;
	private JLabel lblCCCD;
	private JTextField txtCCCD;
	private JLabel lblHT;
	private JTextField txtHT;
	private JLabel lblLuuY;
	private JPanel jpTongPhai;
	
	private JLabel[] lblCacBuoc;
	private JPanel jpTienTrinh;
	private int buocHT = 0;
	private JButton btnQuayLai;
	private JButton btnTiepTheo;
	private JPanel jpDieuHuong;
	private DefaultTableModel tblModel;
	private JTable tableB2;
	private JScrollPane srcollPane;
	private JLabel lblB2;
	private JPanel jpInfoB2;
	private Component txtHTht;
	private Component txtSDTht;
	private JTextField txtCCCDht;
	
	public GiaoDienHuyVe() {
		setTitle("Hệ thống bán vé");
		setSize(1200,800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Layout của frame tổng.
		setLayout(new BorderLayout());
		 
		// Frame Menu
		jpmenu = new JPanel(new FlowLayout());
		
		jpmenu.setPreferredSize(new Dimension(1000, 50));
		// Frame content
		jpContent = new JPanel(new BorderLayout());
	
		// các frame con của Frame content
		//Frame trái
	
		
	
		jpTongPhai = new JPanel(new BorderLayout());
	
		cardLayout = new CardLayout();
		jpPhai = new JPanel(cardLayout);
		jpPhai.setBorder(new EmptyBorder(20,20,20,20));
	
		//tạo Frame cho từng bước
		JPanel step1Panel = createStep1Panel();
		jpPhai.add(step1Panel,"Step1");
	    JPanel step2Panel = createStep2Panel();
	    jpPhai.add(step2Panel,"Step2");
	    JPanel step3Panel = createStep3Panel();
	    jpPhai.add(step3Panel,"Step3");
	    JPanel step4Panel = createStep4Panel();
	    jpPhai.add(step4Panel,"Step4");
		
		
		
		jpTienTrinh = taoThanhQuyTrinh();
		jpDieuHuong = taoDieuHuong();
		jpTongPhai.add(jpTienTrinh,BorderLayout.NORTH);
		jpTongPhai.add(jpPhai,BorderLayout.CENTER);
		jpTongPhai.add(jpDieuHuong,BorderLayout.SOUTH);
		
		
		jpContent.add(jpTongPhai,BorderLayout.CENTER);
		
		
		//add component vào Frame tổng
		add(jpmenu, BorderLayout.NORTH);
		add(jpContent, BorderLayout.CENTER);
		
		cardLayout.show(jpPhai, "Step1");
		capNhatTienTrinh();
		setVisible(true);
		
	}
	private JPanel createStep1Panel() {
		jpB1 = new JPanel(new BorderLayout());
		jpB1.setBorder(new EmptyBorder(30,30,30,100));
		lbltileB1 = new JLabel("Để hiển thị các vé cần hủy hãy nhập các thông tin dưới đây:");
		lbltileB1.setFont(new Font("Arial", Font.BOLD,25));
		lbltileB1.setHorizontalAlignment(SwingConstants.CENTER);
		lbltileB1.setForeground(new Color(74, 140, 103));
	
		
		lblLuuY = new JLabel("Lưu ý : các thông tin này phải khớp với thông tin trên vé");
		lblLuuY.setFont(new Font("Arial", Font.ITALIC, 14));
		lblLuuY.setForeground(new Color(229, 115, 11));
		lblLuuY.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		jpInfo = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;
		lblHT = new JLabel("Họ và tên:");
		txtHT = new JTextField();
		txtHT.setPreferredSize(new Dimension(300,25));
		lblStd = new JLabel("Số điện thoại:");
		txtSDT = new JTextField();
		txtSDT.setPreferredSize(new Dimension(300,25));
		lblCCCD = new JLabel("CCCD:");
		txtCCCD = new JTextField();
		txtCCCD.setPreferredSize(new Dimension(300,25));
		
		gbc.gridx = 0; gbc.gridy=0;
		jpInfo.add(lblHT,gbc);
		gbc.gridx = 1;
		jpInfo.add(txtHT,gbc);
		
		gbc.gridx = 0; gbc.gridy=1;
		jpInfo.add(lblStd,gbc);
		gbc.gridx = 1;
		jpInfo.add(txtSDT,gbc);
		
		gbc.gridx = 0; gbc.gridy=2;
		jpInfo.add(lblCCCD,gbc);
		gbc.gridx = 1;
		jpInfo.add(txtCCCD,gbc);
		
		
		jpB1.add(lbltileB1,BorderLayout.NORTH);
		jpB1.add(lblLuuY,BorderLayout.SOUTH);
		jpB1.add(jpInfo,BorderLayout.CENTER);
		
		return jpB1;
		
		
	}
	private JPanel createStep2Panel() {
		JPanel jpB2 = new JPanel(new BorderLayout());
		jpB2.setBorder(new EmptyBorder(20,20,20,20));
		String [] colums = {"#","Họ tên","Thông tin vé","Thành tiền (VND)","Lệ phí trả vé","Tiền trả lại","Thông tin vé trả","Chọn trả vé"};
		Object[][] data = {};
		DefaultTableModel model = new DefaultTableModel(data, colums) {
	            @Override
	            public Class<?> getColumnClass(int columnIndex) {
	                if (columnIndex == 7) return Boolean.class; // cột checkbox
	                return String.class;
	            }

	            @Override
	            public boolean isCellEditable(int row, int column) {
	                return column == 7; // chỉ cho phép tick vào checkbox
	            }
	        };

		
		tableB2 = new JTable(model);
		srcollPane = new JScrollPane(tableB2);
		
		lblB2 = new JLabel("Thông tin của vé: ");
		lblB2.setFont(new Font("Arial", Font.BOLD, 16));
		lblB2.setForeground(new Color(229, 115, 11));
		
		
		JPanel panelSouth = new JPanel(new BorderLayout());
		JLabel lblInFoDV = new JLabel("Thông tin người đặt vé:");
		lblInFoDV.setFont(new Font("Arial", Font.BOLD, 16));
		lblInFoDV.setForeground(new Color(229,115,11));
		jpInfoB2 = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;
		lblHT = new JLabel("Họ và tên:");
		txtHTht = new JTextField();
		txtHTht.setPreferredSize(new Dimension(300,25));
		txtHTht.setEnabled(false);
		lblStd = new JLabel("Số điện thoại:");
		txtSDTht = new JTextField();
		txtSDTht.setPreferredSize(new Dimension(300,25));
		txtSDTht.setEnabled(false);
		lblCCCD = new JLabel("CCCD:");
		txtCCCDht = new JTextField();
		txtCCCDht.setPreferredSize(new Dimension(300,25));
		txtCCCDht.setEnabled(false);
		gbc.gridx = 0; gbc.gridy=0;
		jpInfoB2.add(lblHT,gbc);
		gbc.gridx = 1;
		jpInfoB2.add(txtHTht,gbc);
		
		gbc.gridx = 0; gbc.gridy=1;
		jpInfoB2.add(lblStd,gbc);
		gbc.gridx = 1;
		jpInfoB2.add(txtSDTht,gbc);
		
		gbc.gridx = 0; gbc.gridy=2;
		jpInfoB2.add(lblCCCD,gbc);
		gbc.gridx = 1;
		jpInfoB2.add(txtCCCDht,gbc);
		panelSouth.add(lblInFoDV,BorderLayout.NORTH);
		panelSouth.add(jpInfoB2,BorderLayout.CENTER);
		
		
		jpB2.add(lblB2,BorderLayout.NORTH);
		jpB2.add(srcollPane,BorderLayout.CENTER);
		jpB2.add(panelSouth,BorderLayout.SOUTH);
		return jpB2;
		
		
	}
	private JPanel createStep3Panel() {
		 JPanel panel = new JPanel(new BorderLayout());
	        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

	        JLabel title = new JLabel("Thông tin vé chọn hủy:");
	        title.setFont(new Font("Arial", Font.BOLD, 18));
	        title.setForeground(new Color(229,115,11));

	       
	        String[] columnNames = {"Mục", "Nội dung"};
	        Object[][] data = {
	                {"- Họ tên: ", " "},
	                {"- Số giấy tờ", " "},
	                {"- Tàu", " "},
	                {"- Số toa", " "},
	                {"- Số chỗ ngồi", " "},
	                {"- Tiền vé", " "},
	                {"- Lệ phí trả vé", " "},
	                {"- Tiền trả", " "},
	        };

	        JTable table = new JTable(data, columnNames) {
	            public boolean isCellEditable(int row, int column) {
	                return false;
	            }
	        };
	        table.setRowHeight(30);
	        table.setFont(new Font("Arial", Font.PLAIN, 14));
	        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
	        table.setFillsViewportHeight(true);

	        JScrollPane scrollPane = new JScrollPane(table);
	        scrollPane.setPreferredSize(new Dimension(500, 150));
	        scrollPane.setBorder(BorderFactory.createTitledBorder("Thông tin xác nhận:"));
	        
	        JLabel lblLuuYxn = new JLabel("Lưu ý: Xác nhận đúng thông tin và nhấn nút tiếp theo để hoàn tất thủ tục hủy vé");
	        lblLuuYxn.setFont(new Font("Arial", Font.ITALIC, 14));
	        lblLuuYxn.setForeground(new Color(229,115,11));

	        panel.add(title, BorderLayout.NORTH);
	        panel.add(scrollPane, BorderLayout.CENTER);
	        panel.add(lblLuuYxn,BorderLayout.SOUTH);

	        return panel;
	}
	private JPanel createStep4Panel() {
		JPanel jpB4 = new JPanel(new BorderLayout());
		JLabel lblTB = new JLabel("Đã hủy vé thành công!",SwingConstants.CENTER);
		lblTB.setFont(new Font("Arial", Font.BOLD, 20));
		lblTB.setForeground(new Color(74, 140, 103));
		jpB4.add(lblTB,BorderLayout.CENTER);
		return jpB4;
		
	}
	private JPanel taoThanhQuyTrinh() {
	    JPanel jpQuyTrinh = new JPanel();
	    jpQuyTrinh.setLayout(new BoxLayout(jpQuyTrinh, BoxLayout.X_AXIS));
	    jpQuyTrinh.setBorder(new EmptyBorder(10, 0, 10, 0));
	    jpQuyTrinh.setBackground(new Color(74, 140, 103));
	    
	    lblCacBuoc = new JLabel[4];
	    String[] cacBuoc = {"Nhập thông tin", "Chọn vé", "Xác nhận", "Hoàn tất"};
	    
	  
	    jpQuyTrinh.add(Box.createHorizontalGlue());
	    
	    for (int i = 0; i < 4; i++) {
	       
	        JPanel stepSubPanel = new JPanel();
	        stepSubPanel.setLayout(new BoxLayout(stepSubPanel, BoxLayout.Y_AXIS));
	        stepSubPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        stepSubPanel.setPreferredSize(new Dimension(150, 70));  
	        stepSubPanel.setMinimumSize(new Dimension(150, 70));
	        stepSubPanel.setMaximumSize(new Dimension(150, 70));
	        stepSubPanel.setBackground(new Color(74, 140, 103));  

	        
	        ImageIcon stepIcon = chinhKichThuoc("/img/step-icon-" + (i+1) + ".png", 30, 30);
	        JLabel iconLabel = new JLabel(stepIcon);
	        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        stepSubPanel.add(iconLabel);

	        // Label chính (bước hiện tại)
	        lblCacBuoc[i] = new JLabel(cacBuoc[i]);
	        lblCacBuoc[i].setForeground(Color.WHITE);
	        lblCacBuoc[i].setFont(new Font("Arial", Font.BOLD, 16));
	        lblCacBuoc[i].setHorizontalAlignment(SwingConstants.CENTER);
	        lblCacBuoc[i].setBorder(BorderFactory.createLineBorder(new Color(225, 242, 232), 1, true));
	        lblCacBuoc[i].setAlignmentX(Component.CENTER_ALIGNMENT);
	        stepSubPanel.add(lblCacBuoc[i]);

	        // Label dưới (thêm mô tả, ví dụ số bước hoặc tùy chỉnh)
	        JLabel subLabel = new JLabel("Bước " + (i + 1));  // Hoặc text khác như "Hoàn thành" cho bước đã làm
	        subLabel.setForeground(Color.WHITE);
	        subLabel.setFont(new Font("Arial", Font.PLAIN, 10));
	        subLabel.setHorizontalAlignment(SwingConstants.CENTER);
	        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        stepSubPanel.add(subLabel);

	        jpQuyTrinh.add(stepSubPanel);
	        
	        if (i < 3) {
	            JLabel muiTen = new JLabel("→");
	            muiTen.setForeground(Color.WHITE);
	            muiTen.setFont(new Font("Arial", Font.BOLD, 14));
	            muiTen.setPreferredSize(new Dimension(30, 60));  // Height tăng để fit sub-panel
	            muiTen.setAlignmentY(Component.CENTER_ALIGNMENT);
	            jpQuyTrinh.add(muiTen);
	        }
	    }
	    
	    // Thêm glue để căn giữa
	    jpQuyTrinh.add(Box.createHorizontalGlue());
	    
	    // Cập nhật bước đầu tiên
	    lblCacBuoc[0].setOpaque(true);
	    lblCacBuoc[0].setBackground(Color.WHITE);
	    lblCacBuoc[0].setForeground(new Color(52, 152, 219));
	    
	    return jpQuyTrinh;
	}
	 
	private JPanel taoDieuHuong () {
		JPanel jpDieuHuong = new JPanel(new BorderLayout());
		jpDieuHuong.setBorder(new EmptyBorder(10,0,10,0));
		jpDieuHuong.setPreferredSize(new Dimension(150,80));
		
		btnQuayLai = new JButton("Quay lại");
		btnTiepTheo = new JButton("Tiếp theo");
		
		btnQuayLai.setEnabled(false);
		btnQuayLai.addActionListener(e -> {
			if(buocHT > 0) {
				buocHT--;
				cardLayout.previous(jpPhai);
				capNhatTienTrinh();
				btnTiepTheo.setText(buocHT == 3 ? "Hoàn tất":"Tiếp theo");
				btnQuayLai.setEnabled(buocHT > 0);
				btnTiepTheo.setEnabled(buocHT < 3);
			}
		});
		btnTiepTheo.addActionListener(e ->{
			if(buocHT < 3) {
				if(buocHT == 0) {
					buocHT++;
					cardLayout.next(jpPhai);
					capNhatTienTrinh();
					btnTiepTheo.setText(buocHT == 3 ? "Hoàn tất":"Tiếp theo");
					btnQuayLai.setEnabled(true);
					btnTiepTheo.setEnabled(buocHT < 3);	
				}else {
					buocHT++;
					cardLayout.next(jpPhai);
					capNhatTienTrinh();
					btnTiepTheo.setText(buocHT == 3 ? "Hoàn tất":"Tiếp theo");
					btnQuayLai.setEnabled(true);
					btnTiepTheo.setEnabled(buocHT < 3);
					
			}
		}
		});
		btnQuayLai.setFont(new Font("Arial", Font.PLAIN, 16));
		btnTiepTheo.setFont(new Font("Arial", Font.PLAIN, 16));
		btnTiepTheo.setBackground(new Color(93, 156, 236));
		btnTiepTheo.setForeground(Color.WHITE);
		btnQuayLai.setBorder(new EmptyBorder(20,40,20,40));
		btnTiepTheo.setBorder(new EmptyBorder(20,40,20,40));
		jpDieuHuong.add(btnQuayLai,BorderLayout.WEST);
		jpDieuHuong.add(btnTiepTheo,BorderLayout.EAST);
		jpDieuHuong.setBackground(new Color(103, 192, 144));
		return jpDieuHuong;
	
	}
	private void capNhatTienTrinh() {
		for(int i = 0 ; i < 4; i++) {
			if(i < buocHT ) {
				lblCacBuoc[i].setOpaque(true);
			lblCacBuoc[i].setBackground(new Color(103, 192, 144));
				lblCacBuoc[i].setForeground(Color.WHITE);
			}else if(i == buocHT) {
				lblCacBuoc[i].setOpaque(true);
				lblCacBuoc[i].setBackground(Color.white);
				lblCacBuoc[i].setForeground(new Color(103, 192, 144));
			}else {
				lblCacBuoc[i].setOpaque(true);
				lblCacBuoc[i].setForeground(new Color(151,151,151));
				lblCacBuoc[i].setBackground(Color.WHITE);
			}
		}
	}
	
	public static void main(String[] args) {
		LookAndFeelManager.setNimbusLookAndFeel();
		new GiaoDienHuyVe();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		if(obj == btnTiepTheo) {
			if(buocHT < 3) {
				if(buocHT == 0) {
					String maVeCanHuy = layMaVeTuForm();
					NhanVien nvThucHien = layThongTinNhanVien();
					QuanLyVeControl veCT = new QuanLyVeControl();
					try {
						LichSuVe kqHuy = veCT.xuLyHuyVe(maVeCanHuy, "Kiểm tra điều kiện hủy", nvThucHien);
						BigDecimal phiHuy = kqHuy.getPhiXuLy();
						
		                JOptionPane.showMessageDialog(this, 
		                		"Vé hợp lệ! Phí hủy: " + phiHuy +
		                		". Chuyển sang Bước 2.",
                                "Thành công", JOptionPane.INFORMATION_MESSAGE);
		                
		                buocHT++;
		                cardLayout.next(jpPhai);
		                capNhatTienTrinh();
		                btnTiepTheo.setText(buocHT == 3 ? "Hoàn tất" : "Tiếp theo");
		                btnQuayLai.setEnabled(true);
					}catch (Exception ex) {
						// TODO: handle exception
						JOptionPane.showMessageDialog(this, ex.getMessage(),
								"Lỗi nghiệp vụ", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}else {
			buocHT++;
			cardLayout.next(jpPhai);
			capNhatTienTrinh();
			btnTiepTheo.setText(buocHT == 3 ? "Hoàn tất" : "Tiếp theo");
			btnQuayLai.setEnabled(true);
			btnTiepTheo.setEnabled(buocHT < 3);
		}
	}
	private NhanVien layThongTinNhanVien() {
		// TODO Auto-generated method stub
		return null;
	}
	private String layMaVeTuForm() {
		// TODO Auto-generated method stub
		return null;
	}
}
