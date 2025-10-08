package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.Random;

public class TraCuuVeTauGUI extends GiaoDienChinh {
    private JTextField txtMaVe, txtHoTen, txtCMND, txtSDT;
    private JComboBox<String> cmbLoaiVe;
    private JTable tableKetQua;
    private DefaultTableModel tableModel;
    private JButton btnTimKiem, btnThoat, btnInVe, btnLamMoi;
    private JLabel lblThoiGianRealTime;

    public TraCuuVeTauGUI() {
        setTitle("Tra Cứu Vé Tàu - Hệ thống quản lý bán vé tàu lửa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
       

        // Cài đặt LookAndFeel cho siêu đẹp (Nimbus)
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

       
        JPanel headerPanel = createVIPHeader();
        add(headerPanel, BorderLayout.NORTH);

      
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setDividerLocation(400);
        mainSplit.setBackground(Color.WHITE);

        
        JPanel formPanel = createVIPFormPanel();
        mainSplit.setLeftComponent(formPanel);

        JPanel tablePanel = createVIPTablePanel();
        mainSplit.setRightComponent(tablePanel);

        add(mainSplit, BorderLayout.CENTER);

        
        JPanel footerPanel = createVIPFooter();
        add(footerPanel, BorderLayout.SOUTH);

        

        
    }

    private JPanel createVIPHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 100));
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Tra Cứu Vé Tàu ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD , 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 30, 0, 20));
        JPanel panelHeader = new JPanel(new FlowLayout());
        ImageIcon originalIcon = chinhKichThuoc("/imgticket.png", 90, 80);
        
        Image scaledImage = originalIcon.getImage().getScaledInstance(90, 80, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        
        
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(scaledIcon);
        imageLabel.setBorder(new EmptyBorder(0,100,0,10));
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        panelHeader.add(imageLabel);
        panelHeader.add(titleLabel);
        header.add(panelHeader, BorderLayout.CENTER);
        header.setBackground(new Color(103,192,144));
        panelHeader.setBackground(new Color(103,192,144));
        return header;
    }

    private JPanel createVIPFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setPreferredSize(new Dimension(400, 0));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(225, 242, 232), 2, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Title cho form
        JLabel formTitle = new JLabel("Thông Tin Xác Thực Vé", SwingConstants.CENTER);
        formTitle.setFont(new Font("Segoe UI", Font.BOLD , 20));
        formTitle.setForeground(new Color(103, 192, 144));
        formPanel.add(formTitle, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã vé với ảnh icon
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        ImageIcon maVeIcon = new ImageIcon("E:\\STT08_23734471_TranHoaiChau\\QLBVTau\\src\\img\\train-ticket.png");
        Image scaledMaVeIcon = maVeIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        JLabel lblMaVe = new JLabel("Mã vé:", new ImageIcon(scaledMaVeIcon), SwingConstants.LEFT);
        lblMaVe.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMaVe.setHorizontalTextPosition(SwingConstants.RIGHT);  // Icon trước text
        inputPanel.add(lblMaVe, gbc);

        txtMaVe = new JTextField(15);
        txtMaVe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtMaVe.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1; gbc.gridy = 0;
        inputPanel.add(txtMaVe, gbc);

        // Họ tên với ảnh icon
        gbc.gridx = 0; gbc.gridy = 1;
        ImageIcon hoTenIcon = new ImageIcon("E:\\STT08_23734471_TranHoaiChau\\QLBVTau\\src\\img\\user.png");  
        Image scaledHoTenIcon = hoTenIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        JLabel lblHoTen = new JLabel("Họ tên:", new ImageIcon(scaledHoTenIcon), SwingConstants.LEFT);
        lblHoTen.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblHoTen.setHorizontalTextPosition(SwingConstants.RIGHT);  // Icon trước text
        inputPanel.add(lblHoTen, gbc);

        txtHoTen = new JTextField(15);
        txtHoTen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtHoTen.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1; gbc.gridy = 1;
        inputPanel.add(txtHoTen, gbc);

        // CMND/Hộ chiếu với ảnh icon
        gbc.gridx = 0; gbc.gridy = 2;
        ImageIcon cmndIcon = new ImageIcon("E:\\STT08_23734471_TranHoaiChau\\QLBVTau\\src\\img\\passport.png"); 
        Image scaledCmndIcon = cmndIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        JLabel lblCMND = new JLabel("CMND/Hộ chiếu:", new ImageIcon(scaledCmndIcon), SwingConstants.LEFT);
        lblCMND.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCMND.setHorizontalTextPosition(SwingConstants.RIGHT);  // Icon trước text
        inputPanel.add(lblCMND, gbc);

        txtCMND = new JTextField(15);
        txtCMND.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCMND.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1; gbc.gridy = 2;
        inputPanel.add(txtCMND, gbc);

        // SĐT với ảnh icon
        gbc.gridx = 0; gbc.gridy = 3;
        ImageIcon sdtIcon = new ImageIcon("E:\\STT08_23734471_TranHoaiChau\\QLBVTau\\src\\img\\phone.png");  
        Image scaledSdtIcon = sdtIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        JLabel lblSDT = new JLabel("SĐT:", new ImageIcon(scaledSdtIcon), SwingConstants.LEFT);
        lblSDT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSDT.setHorizontalTextPosition(SwingConstants.RIGHT);  // Icon trước text
        inputPanel.add(lblSDT, gbc);

        txtSDT = new JTextField(15);
        txtSDT.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSDT.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1; gbc.gridy = 3;
        inputPanel.add(txtSDT, gbc);

        // Loại vé combo box với ảnh icon
        gbc.gridx = 0; gbc.gridy = 4;
        ImageIcon loaiVeIcon = new ImageIcon("E:\\STT08_23734471_TranHoaiChau\\QLBVTau\\src\\img\\ve2.png"); 
        Image scaledLoaiVeIcon = loaiVeIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        JLabel lblLoaiVe = new JLabel("Loại vé:", new ImageIcon(scaledLoaiVeIcon), SwingConstants.LEFT);
        lblLoaiVe.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLoaiVe.setHorizontalTextPosition(SwingConstants.RIGHT);  // Icon trước text
        inputPanel.add(lblLoaiVe, gbc);

        String[] loaiVe = {"Tất cả", "Thường", "VIP", "Pro Suite"};
        cmbLoaiVe = new JComboBox<>(loaiVe);
        cmbLoaiVe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbLoaiVe.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1; gbc.gridy = 4;
        inputPanel.add(cmbLoaiVe, gbc);

        
        ImageIcon timKiemIcon = new ImageIcon("E:\\STT08_23734471_TranHoaiChau\\QLBVTau\\src\\img\\traCuu.png"); 
        Image scaledTimKiemIcon = timKiemIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnTimKiem = new JButton("Tìm Kiếm", new ImageIcon(scaledTimKiemIcon));
        btnTimKiem.setBorder(new EmptyBorder(15,25,15,25));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTimKiem.setBackground(new Color(93, 156, 236));
        btnTimKiem.setHorizontalTextPosition(SwingConstants.RIGHT);  // Icon trước text
        btnTimKiem.setVerticalTextPosition(SwingConstants.CENTER);
       
        gbc.gridx = 0; gbc.gridy = 5; 
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(btnTimKiem, gbc);

        ImageIcon lamMoiIcon = new ImageIcon("E:\\STT08_23734471_TranHoaiChau\\QLBVTau\\src\\img\\reload.png"); 
        Image scaledLamMoiIcon = lamMoiIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnLamMoi = new JButton("Làm Mới", new ImageIcon(scaledLamMoiIcon));
        btnLamMoi.setBorder(new EmptyBorder(15,25,15,25));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLamMoi.setBackground(new Color(229, 115, 115));
        btnLamMoi.setHorizontalTextPosition(SwingConstants.RIGHT);  // Icon trước text
        btnLamMoi.setVerticalTextPosition(SwingConstants.CENTER);
        gbc.gridx = 1; gbc.gridy = 5;
        inputPanel.add(btnLamMoi, gbc);

        formPanel.add(inputPanel, BorderLayout.CENTER);

        return formPanel;
    }

    private JPanel createVIPTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(225, 242, 232), 2, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

      
        JLabel tableTitle = new JLabel("Kết Quả Xác Thực Vé ", SwingConstants.CENTER);
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tableTitle.setForeground(new Color(74, 140, 103));
        tablePanel.add(tableTitle, BorderLayout.NORTH);

    
        String[] columns = {"STT", "Mã vé", "Họ tên", "CMND", "Chuyến tàu", "Ga đi - Ga đến", "Ngày đi", "Giá vé (VNĐ)", "Loại vé", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0);
        tableKetQua = new JTable(tableModel);
        tableKetQua.setRowHeight(50);
        tableKetQua.setGridColor(new Color(220, 220, 255));
        tableKetQua.setBackground(new Color(93, 156, 236));
        tableKetQua.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tableKetQua.setShowHorizontalLines(true);
        tableKetQua.setShowVerticalLines(false);

        tableKetQua.getTableHeader().setBackground(Color.WHITE);
        tableKetQua.getTableHeader().setForeground(new Color(74, 140, 103));
        tableKetQua.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableKetQua.getTableHeader().setReorderingAllowed(false);
       

    

     

        JScrollPane scrollPane = new JScrollPane(tableKetQua);
        scrollPane.setPreferredSize(new Dimension(900, 600));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(225, 242, 232)));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createVIPFooter() {
    	 JPanel footer = new JPanel(new BorderLayout());
         footer.setBackground(new Color(103, 192, 144));
         footer.setPreferredSize(new Dimension(0, 90));
         footer.setBorder(new EmptyBorder(10, 10, 10, 10));

         // Clock real-time bên trái
         lblThoiGianRealTime = new JLabel();
         lblThoiGianRealTime.setFont(new Font("Segoe UI", Font.BOLD, 14));
         lblThoiGianRealTime.setForeground(Color.WHITE);
         footer.add(lblThoiGianRealTime, BorderLayout.WEST);

         // Buttons bên phải
         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
         buttonPanel.setBackground(new Color(103, 192, 144));
        
         // Nút Đặt Vé với ảnh icon
         ImageIcon datVeIcon = new ImageIcon("E:\\STT08_23734471_TranHoaiChau\\QLBVTau\\src\\img\\ve.png");  
         Image scaledDatVeIcon = datVeIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
         btnInVe = new JButton("In Vé", new ImageIcon(scaledDatVeIcon));
         btnInVe.setForeground(Color.BLACK);
         btnInVe.setFont(new Font("Segoe UI", Font.BOLD, 18));
         btnInVe.setBackground(new Color(255, 237, 0));
         btnInVe.setBorder(new EmptyBorder(15,25,15,25));
         btnInVe.setHorizontalTextPosition(SwingConstants.RIGHT);  // Icon trước text
         btnInVe.setVerticalTextPosition(SwingConstants.CENTER);

        
         ImageIcon thoatIcon = new ImageIcon("E:\\STT08_23734471_TranHoaiChau\\QLBVTau\\src\\img\\loginicon.png"); 
         Image scaledThoatIcon = thoatIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
         btnThoat = new JButton("Trở về", new ImageIcon(scaledThoatIcon));
         btnThoat.setForeground(Color.WHITE);
         btnThoat.setFont(new Font("Segoe UI", Font.BOLD, 18));
         btnThoat.setBackground(new Color(93, 156, 236));
         btnThoat.setBorder(new EmptyBorder(15,25,15,25));
         btnThoat.setHorizontalTextPosition(SwingConstants.RIGHT);  // Icon trước text
         btnThoat.setVerticalTextPosition(SwingConstants.CENTER);

      
         footer.add(btnThoat, BorderLayout.WEST);
         footer.add(btnInVe, BorderLayout.EAST);

         return footer;
    }

   
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TraCuuVeTauGUI().setVisible(true));
    }
}
