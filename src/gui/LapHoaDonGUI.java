package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.ChiTietHoaDonDAO;
import dao.ChuyenTauDAO;
import dao.HoaDonDAO;

public class LapHoaDonGUI extends JFrame implements ActionListener{
	// DAO instances (giả định)
    private HoaDonDAO hdDao = new HoaDonDAO();
    private ChiTietHoaDonDAO cthdDao = new ChiTietHoaDonDAO();
    private ChuyenTauDAO ctDao = new ChuyenTauDAO(); 

	private JPanel pnldau;
	private JPanel pnlHanhKhach;
	private JPanel pnlChuyenTau;
	private JPanel pnlChiTiet;
	private JPanel panelTong;
	private JPanel panelButtons;
	private JLabel lblTitle;
	private JLabel mahd;
	private JLabel nhanvien;
	private JLabel ngaylap;
	private JLabel hanhk;
	private JLabel cmnd;
	private JLabel sdt;
	private JLabel email;
	private JLabel matau;
	private JLabel gadi;
	private JLabel gaden;
	private JLabel ngaydi;
	private JLabel giodi;
	private JLabel loaighe;
	private JLabel tongtien;
	private JLabel vat;
	private JLabel tongcong;
	private JTable table;
	private DefaultTableModel model;
	private JButton btnThoat;
	private JButton btnLuu;
	
    public LapHoaDonGUI(String maHoaDonFinal) {
    	taiVaHienThiHoaDon(maHoaDonFinal);
    	 btnThoat.addActionListener(e -> this.dispose());
         btnLuu.addActionListener(e -> inHoaDon());
        setTitle("HÓA ĐƠN BÁN VÉ TÀU HỎA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1000);
        setResizable(false);
        setLocationRelativeTo(null);

        // đầu
        pnldau = new JPanel(new BorderLayout());
        pnldau.setBorder(new EmptyBorder(10, 15, 10, 15));
        pnldau.setBackground(new Color(3, 192, 144));

        lblTitle = new JLabel("HÓA ĐƠN VÉ TÀU HỎA", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        ImageIcon iconhoadon = new ImageIcon("src/img/receipt.png");
        Image imagehoadon = iconhoadon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        lblTitle.setIcon(new ImageIcon(imagehoadon));
        lblTitle.setHorizontalTextPosition(SwingConstants.RIGHT);
        lblTitle.setIconTextGap(10);

        JPanel panelRightHeader = new JPanel(new GridLayout(3, 2, 5, 5));
        panelRightHeader.setOpaque(false);

        mahd = new JLabel("Mã hóa đơn:");
        nhanvien = new JLabel("Nhân viên lập:");
        ngaylap = new JLabel("Ngày lập:");

        mahd.setForeground(Color.WHITE);
        nhanvien.setForeground(Color.WHITE);
        ngaylap.setForeground(Color.WHITE);

        panelRightHeader.add(mahd);
        panelRightHeader.add(nhanvien);
        panelRightHeader.add(ngaylap);

        pnldau.add(lblTitle, BorderLayout.CENTER);
        pnldau.add(panelRightHeader, BorderLayout.EAST);

        add(pnldau, BorderLayout.NORTH);

        // panel nội dung chính
        JPanel pnlnd = new JPanel();
        pnlnd.setLayout(new BoxLayout(pnlnd, BoxLayout.Y_AXIS));
        pnlnd.setBorder(new EmptyBorder(10, 15, 10, 15));
        pnlnd.setBackground(Color.WHITE);

        // thông tin khách hàng
        pnlHanhKhach = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlHanhKhach.setBorder(BorderFactory.createLineBorder(Color.WHITE, 20));
        pnlHanhKhach.setBackground(Color.WHITE);

        hanhk = new JLabel("Họ tên hành khách:");
        hanhk.setForeground(Color.BLACK);
        hanhk.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pnlHanhKhach.add(hanhk);

        cmnd = new JLabel("CMND/CCCD:");
        cmnd.setForeground(Color.BLACK);
        cmnd.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pnlHanhKhach.add(cmnd);

        sdt = new JLabel("Số điện thoại:");
        sdt.setForeground(Color.BLACK);
        sdt.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pnlHanhKhach.add(sdt);

        email = new JLabel("Email:");
        email.setForeground(Color.BLACK);
        email.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pnlHanhKhach.add(email);

        // thông tin của chuyến tàu
        pnlChuyenTau = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlChuyenTau.setBorder(BorderFactory.createLineBorder(Color.WHITE, 20));
        pnlChuyenTau.setBackground(Color.WHITE);

        matau = new JLabel("Mã tàu:");
        matau.setForeground(Color.BLACK);
        matau.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pnlChuyenTau.add(matau);

        gadi = new JLabel("Ga đi:");
        gadi.setForeground(Color.BLACK);
        gadi.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pnlChuyenTau.add(gadi);

        gaden = new JLabel("Ga đến:");
        gaden.setForeground(Color.BLACK);
        gaden.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pnlChuyenTau.add(gaden);

        ngaydi = new JLabel("Ngày đi:");
        ngaydi.setForeground(Color.BLACK);
        ngaydi.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pnlChuyenTau.add(ngaydi);

        giodi = new JLabel("Giờ đi:");
        giodi.setForeground(Color.BLACK);
        giodi.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pnlChuyenTau.add(giodi);

        loaighe = new JLabel("Loại ghế:");
        loaighe.setForeground(Color.BLACK);
        loaighe.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pnlChuyenTau.add(loaighe);

        // table
        pnlChiTiet = new JPanel(new BorderLayout());
        String[] tieudeheader = {"STT", "Toa", "Ghế", "Giá Vé", "Số Lượng", "Thành Tiền"};
        model = new DefaultTableModel(tieudeheader, 0);
        table = new JTable(model);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setForeground(new Color(30, 30, 30));
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setBackground(new Color(245, 245, 240));
        table.setSelectionBackground(new Color(0, 120, 215));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader tieudebang = table.getTableHeader();
        tieudebang.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tieudebang.setBackground(new Color(245, 245, 245));
        tieudebang.setReorderingAllowed(false);
        tieudebang.setBorder(null);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        pnlChiTiet.add(scrollPane, BorderLayout.CENTER);

        // tổng tiền ở dưới
        panelTong = new JPanel(new GridLayout(3, 2, 10, 10));
        panelTong.setBackground(Color.WHITE);

        tongtien = new JLabel("Tổng tiền vé:");
        tongtien.setForeground(Color.BLACK);
        tongtien.setFont(new Font("Segoe UI", Font.BOLD, 15));
        panelTong.add(tongtien);

        vat = new JLabel("Thuế VAT:");
        vat.setForeground(Color.BLACK);
        vat.setFont(new Font("Segoe UI", Font.BOLD, 15));
        panelTong.add(vat);

        tongcong = new JLabel("Tổng cộng phải thu:");
        tongcong.setForeground(Color.BLACK);
        tongcong.setFont(new Font("Segoe UI", Font.BOLD, 15));
        panelTong.add(tongcong);
        panelTong.setBorder(new EmptyBorder(20,10,10,10));

        // -button
        panelButtons = new JPanel(new BorderLayout());
        panelButtons.setBackground(Color.WHITE);
        panelButtons.setBorder(new EmptyBorder(50, 15, 15, 15));

        // Nút Trở Về
        btnThoat = new JButton("Trở Về");
        btnThoat.setBackground(new Color(189, 189, 189));
        btnThoat.setForeground(Color.WHITE);
        btnThoat.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThoat.setPreferredSize(new Dimension(130,50));
        ImageIcon iconThoat = new ImageIcon("src/img/undo.png"); 
        Image imgThoat = iconThoat.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        btnThoat.setIcon(new ImageIcon(imgThoat));
        btnThoat.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnThoat.setIconTextGap(10);

        // Nút Lưu hóa đơn
        btnLuu = new JButton("Lưu hóa đơn");
        btnLuu.setBackground(new Color(3, 192, 144));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLuu.setPreferredSize(new Dimension(170,50));
        ImageIcon iconLuu = new ImageIcon("src/img/mark.png"); 
        Image imgLuu = iconLuu.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        btnLuu.setIcon(new ImageIcon(imgLuu));
        btnLuu.setHorizontalTextPosition(SwingConstants.RIGHT); 
        btnLuu.setIconTextGap(10);

        panelButtons.add(btnThoat, BorderLayout.WEST);
        panelButtons.add(btnLuu, BorderLayout.EAST);

        pnlnd.add(pnlHanhKhach);
        pnlnd.add(pnlChuyenTau);
        pnlnd.add(pnlChiTiet);
        pnlnd.add(panelTong);
        pnlnd.add(panelButtons);

        add(pnlnd, BorderLayout.CENTER);
    }

    private void taiVaHienThiHoaDon(String maHoaDonFinal) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {

    }
    
    private void inHoaDon() {
        // Logic in ấn tương tự như trong HomeInventoryManager (Sử dụng Printable interface) [5, 6]
        JOptionPane.showMessageDialog(this, "Chức năng In hóa đơn đang được phát triển...", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

