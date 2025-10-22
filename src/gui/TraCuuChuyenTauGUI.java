package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import dao.ChuyenTauDAO;
import dao.GaDAO;
import entity.ChuyenTau;
import entity.Ga;
import entity.NhanVien;

public class TraCuuChuyenTauGUI extends JFrame implements ActionListener {
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
	private JDateChooser dateChooser;
	private ChuyenTauDAO chuyenTauDAO = new ChuyenTauDAO();
    private GaDAO gaDAO = new GaDAO();
	private ChonChoNgoiGUI ChonChoNgoiGUI; 
    
	public TraCuuChuyenTauGUI() {
		setTitle("Tra cứu chuyến tàu");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
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
		
		dateChooser = new JDateChooser(); 
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
		
		
		btnTim.addActionListener(this);
		
		
		 loadGaDataToComboBox(); 
		 loadAllTripsOnStartup();
		 
		add(pnlChinh);
		
	}
	private void loadGaDataToComboBox() {
	    // Khởi tạo DAO để truy cập CSDL
	    GaDAO gaDAO = new GaDAO();
	    List<Ga> danhSachGa = gaDAO.getAllGa(); // Lấy tất cả Ga [2]

	    // 1. Khởi tạo danh sách tạm thời chứa Tên Ga
	    List<String> tenGaList = new ArrayList<>();
	    for (Ga ga : danhSachGa) {
	        // Lấy tên Ga để hiển thị trong combo box
	        tenGaList.add(ga.getTenGa()); // [3]
	    }

	    // 2. Xóa dữ liệu cũ (nếu có) và thêm dữ liệu mới vào cả hai ComboBox
	    cbGaDi.removeAllItems(); // [3]
	    cbGaDen.removeAllItems(); // [3]

	    for (String tenGa : tenGaList) {
	        cbGaDi.addItem(tenGa); // Việc quản lý danh sách trong combo box được thực hiện dễ dàng [4, 5]
	        cbGaDen.addItem(tenGa); // [4]
	    }

	    // 3. Thiết lập giá trị mặc định (Tùy chọn)
	    if (!tenGaList.isEmpty()) {
	        cbGaDi.setSelectedIndex(0); // [4]
	        if (tenGaList.size() > 1) {
	            // Đảm bảo Ga đến khác Ga đi ban đầu
	            cbGaDen.setSelectedIndex(1); // [4]
	        } else {
	            cbGaDen.setSelectedIndex(0);
	        }
	    }
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
		
		
		btnDatVe.addActionListener(this);
		return pnlVe;
		
	}
	
	
	public static void main(String[] args) {
		TraCuuChuyenTauGUI tcct = new TraCuuChuyenTauGUI();
		tcct.setVisible(true);
		
	}
	
	private void loadAllTripsOnStartup() {
	    // 1. Khởi tạo DAO
	    ChuyenTauDAO ctDAO = new ChuyenTauDAO(); 

	    // 2. Gọi phương thức lấy tất cả chuyến tàu từ CSDL
	    List<ChuyenTau> danhSachChuyenTau = ctDAO.getAllChuyenTau(); 
	    // 3. Hiển thị kết quả lên giao diện bằng hàm đã có
	    updateResultsPanel(danhSachChuyenTau); 
	}
	
	private JButton findDatVeButton(Container parent) {
	    for (Component comp : parent.getComponents()) {
	        if (comp instanceof JButton && ((JButton) comp).getText().equals("Đặt vé")) {
	            return (JButton) comp;
	        }
	        // Nếu là Container (JPanel lồng nhau), tiếp tục tìm kiếm sâu hơn (recursive search)
	        if (comp instanceof Container) {
	            JButton found = findDatVeButton((Container) comp);
	            if (found != null) {
	                return found;
	            }
	        }
	    }
	    return null;
	}
	
	private void updateResultsPanel(List<ChuyenTau> danhSach) {
	    pnlTrungTam.removeAll(); 
	    
	    if (danhSach.isEmpty()) {
	        // ... (Logic hiển thị không có kết quả) ...
	        JLabel lblNoResult = new JLabel("Không tìm thấy chuyến tàu nào phù hợp.",
	                SwingConstants.CENTER);
	        // ... (Cấu hình lblNoResult và thêm vào pnlTrungTam)
	        pnlTrungTam.setLayout(new GridLayout(1, 1));
	    } else {
	        pnlTrungTam.setLayout(new GridLayout(0, 3, 15, 15)); 

	        // GIẢ ĐỊNH: Lấy thông tin Nhân viên đang đăng nhập (thay thế bằng logic thực tế của bạn)
	        NhanVien nvHienTai = new NhanVien("NV001"); 
	        
	        for (ChuyenTau ct : danhSach) {
	            // ***** Đóng gói đối tượng ChuyenTau (Closure) *****
	            final ChuyenTau selectedCT = ct; 

	            JPanel vePanel = taoVe(); 
	            updateVePanelData(vePanel, selectedCT); // Cập nhật hiển thị mã chuyến tàu [1, 2]

	            // 1. Tìm nút Đặt vé trong cấu trúc phức tạp của vePanel
	            JButton btnDatVeHienTai = findDatVeButton(vePanel); // Sử dụng hàm tiện ích
	            
	            if (btnDatVeHienTai != null) {
	                // 2. Gán ActionListener mới cho NÚT NÀY
	                btnDatVeHienTai.addActionListener(e -> {
	                    // *** ĐỐI TƯỢNG selectedCT ĐÃ ĐƯỢC CHỤP LẠI (CAPTURED) VÀ SẴN SÀNG SỬ DỤNG ***
	                    
	                    if (selectedCT.getMaChuyenTau() == null || selectedCT.getMaChuyenTau().isEmpty()) {
	                         // Lỗi này xảy ra nếu đối tượng được truyền vào rỗng, nguyên nhân ban đầu của bạn.
	                         JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin chuyến tàu (Mã chuyến rỗng).");
	                         return;
	                    }
	                    
	                    // Ẩn màn hình hiện tại
	                    this.setVisible(false);

	                    // 3. Khởi tạo màn hình Chọn Chỗ Ngồi và TRUYỀN đối tượng Chuyến tàu
	                    // Cần đảm bảo lớp ChonChoNgoiGUI có constructor này.
	                    new ChonChoNgoiGUI(selectedCT, nvHienTai).setVisible(true); 
	                });
	            }

	            pnlTrungTam.add(vePanel);
	        }
	    }

	    pnlTrungTam.revalidate(); 
	    pnlTrungTam.repaint();
	}
	   
	
	private void updateVePanelData(JPanel vePanel, ChuyenTau ct) {
	    // ... logic tìm kiếm các component (JLabel/JTextField) trong cấu trúc panel lồng nhau
	    
	    // Ví dụ về cách tìm và cập nhật MaChuyenTau:
	    // (Giả định rằng lblTenChuyen được đặt text ban đầu là "SE[MaChuyen]")
	    Component[] components = vePanel.getComponents();
	    for (Component comp : components) {
	        if (comp instanceof JPanel) {
	            // Lặp qua các panel lồng nhau (innerPanel) [20]
	            JPanel innerPanel = (JPanel) comp;
	            for (Component deepestComp : innerPanel.getComponents()) {
	                if (deepestComp instanceof JLabel) {
	                    JLabel label = (JLabel) deepestComp;

	                    // Nếu tìm thấy JLabel chứa mã chuyến tàu
	                    if (label.getText().startsWith("SE[")) { 
	                        label.setText(ct.getMaChuyenTau()); // Cập nhật bằng mã chuyến tàu thực tế
	                    }
	                    // Thêm logic cập nhật các trường khác (TG ĐI, TG ĐẾN, SL CHỖ...)
	                }
	            }
	        }
	    }
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
//	    Object src = e.getSource();

	    // Xử lý nút TÌM KIẾM (btnTim)
//	    if (src == btnTim) {
//	        // ... logic tìm kiếm và gọi updateResultsPanel(ketQuaTimKiem) ...
//	    }
//
//	    // Xử lý nút ĐẶT VÉ (btnDatVe) trên mỗi kết quả
//	    if (src instanceof JButton && ((JButton) src).getText().equals("Đặt vé")) {
//	        // 1. LẤY CONTEXT: Xác định Chuyến tàu (selectedCT) từ nút được nhấn.
//	        // ********* THAY THẾ LOGIC LẤY CHUYẾN TÀU THỰC TẾ TẠI ĐÂY *********
//	        // Đây là phần phức tạp nhất trong Swing GUI động, cần tìm ra
//	        // đối tượng ChuyenTau liên quan đến JPanel/JButton cha.
//	        ChuyenTau selectedCT = new ChuyenTau(); // Dùng placeholder
//	        NhanVien currentNV = new NhanVien(); // Giả định đã có thông tin nhân viên đăng nhập
//
//	        if (selectedCT.getMaChuyenTau() != null) {
//	            // 2. CHUYỂN MÀN HÌNH: Khởi tạo màn hình chọn chỗ ngồi và truyền dữ liệu
//	            new ChonChoNgoiGUI(selectedCT, currentNV).setVisible(true);
//	            this.dispose(); // Đóng màn hình tra cứu
//	        } else {
//	            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin chuyến tàu.");
//	        }
//	    }
	}

	
	
	}
	 
