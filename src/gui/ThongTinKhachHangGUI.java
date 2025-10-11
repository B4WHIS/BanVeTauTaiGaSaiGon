package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ThongTinKhachHangGUI extends JFrame {
    private JPanel header;
    private JLabel lblTieuDe;
    private JPanel pnlCenter;
    private JPanel pnlTrai;
    private JLabel lblFormTitle;
    private JPanel pnlNhap;
    private GridBagConstraints gbc;
    private JLabel lblHoTen;
    private JTextField txtHoTen;
    private JLabel lblNgaySinh;
    private JTextField txtNgaySinh;
    private JLabel lblSDT;
    private JTextField txtSDT;
    private JLabel lblCMND;
    private JTextField txtCMND;
    private JLabel lblLoai;
    private JComboBox<String> cboLoaiHK;
    private JPanel pnlButton;
    private JButton btnLamMoi;
    private JButton btnXacNhan;
    private JPanel pnlPhai;
    private JLabel lblTableTitle;
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JPanel footer;
    private JButton btnQuayLai;

    public ThongTinKhachHangGUI() {
        setTitle("Thông Tin Khách Hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1000);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ========================== HEADER ==========================
        header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(100, 100));
        header.setBackground(new Color(103, 192, 144));
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        lblTieuDe = new JLabel("Thông Tin Khách Hàng", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTieuDe.setForeground(Color.WHITE);
        //icon
        ImageIcon iconthongtin = new ImageIcon("src/img/boss.png");
        Image imagethongtin = iconthongtin.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        lblTieuDe.setIcon(new ImageIcon(imagethongtin));
        lblTieuDe.setHorizontalTextPosition(SwingConstants.RIGHT);
        lblTieuDe.setIconTextGap(10);


        header.add(lblTieuDe, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // panel giữa
        pnlCenter = new JPanel(new BorderLayout());
        add(pnlCenter, BorderLayout.CENTER);

        // trái
        pnlTrai = new JPanel(new BorderLayout(10, 10));
        pnlTrai.setPreferredSize(new Dimension(450, 0));
        pnlTrai.setBackground(Color.WHITE);
        pnlTrai.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(225, 242, 232), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        pnlCenter.add(pnlTrai, BorderLayout.WEST);

        lblFormTitle = new JLabel("Thông Tin Khách Hàng", SwingConstants.CENTER);
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblFormTitle.setForeground(new Color(103, 192, 144));
        pnlTrai.add(lblFormTitle, BorderLayout.NORTH);

        pnlNhap = new JPanel(new GridBagLayout());
        pnlNhap.setBackground(Color.WHITE);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font lblFont = new Font("Segoe UI", Font.BOLD, 12);

        // tên
        gbc.gridx = 0; gbc.gridy = 0;
        lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setFont(lblFont);
        pnlNhap.add(lblHoTen, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        txtHoTen = new JTextField(15);
        txtHoTen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtHoTen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        pnlNhap.add(txtHoTen, gbc);

        // date
        gbc.gridx = 0; gbc.gridy = 1;
        lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setFont(lblFont);
        pnlNhap.add(lblNgaySinh, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        txtNgaySinh = new JTextField(15);
        txtNgaySinh.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNgaySinh.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        pnlNhap.add(txtNgaySinh, gbc);

        // sdt
        gbc.gridx = 0; gbc.gridy = 2;
        lblSDT = new JLabel("SĐT:");
        lblSDT.setFont(lblFont);
        pnlNhap.add(lblSDT, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        txtSDT = new JTextField(15);
        txtSDT.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSDT.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        pnlNhap.add(txtSDT, gbc);

        // cccd
        gbc.gridx = 0; gbc.gridy = 3;
        lblCMND = new JLabel("CMND:");
        lblCMND.setFont(lblFont);
        pnlNhap.add(lblCMND, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        txtCMND = new JTextField(15);
        txtCMND.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCMND.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        pnlNhap.add(txtCMND, gbc);

        //loại hành khách
        gbc.gridx = 0; gbc.gridy = 4;
        lblLoai = new JLabel("Loại KH:");
        lblLoai.setFont(lblFont);
        pnlNhap.add(lblLoai, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        String[] loaiHK = {"Người lớn", "Trẻ em", "Sinh viên", "Người cao tuổi"};
        cboLoaiHK = new JComboBox<>(loaiHK);
        cboLoaiHK.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pnlNhap.add(cboLoaiHK, gbc);

        //button
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlButton.setBackground(Color.WHITE);

        btnLamMoi = new JButton("Làm Mới");
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setBackground(new Color(93, 156, 236));
        btnLamMoi.setPreferredSize(new Dimension(120,40));
        btnLamMoi.setBorder(new EmptyBorder(10, 20, 10, 20));

        ImageIcon iconlammoi = new ImageIcon("src/img/undo.png");
        Image imagelammoi = iconlammoi.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnLamMoi.setIcon(new ImageIcon(imagelammoi));
        btnLamMoi.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnLamMoi.setIconTextGap(10);
        pnlButton.add(btnLamMoi);
        
        

        btnXacNhan = new JButton("Xác Nhận");
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setBackground(new Color(103, 192, 144));
        btnXacNhan.setPreferredSize(new Dimension(120,40));
        btnXacNhan.setBorder(new EmptyBorder(10, 20, 10, 20));

        ImageIcon iconxacnhan = new ImageIcon("src/img/mark.png");
        Image imagexacnhan = iconxacnhan.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnXacNhan.setIcon(new ImageIcon(imagexacnhan));
        btnXacNhan.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnXacNhan.setIconTextGap(10);
        pnlButton.add(btnXacNhan);

        pnlNhap.add(pnlButton, gbc);
        pnlTrai.add(pnlNhap, BorderLayout.CENTER);

        // phải
        pnlPhai = new JPanel(new BorderLayout());
        pnlPhai.setBackground(Color.WHITE);
        pnlPhai.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(225, 242, 232), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        pnlCenter.add(pnlPhai, BorderLayout.CENTER);

        lblTableTitle = new JLabel("Danh Sách Khách Hàng", SwingConstants.CENTER);
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTableTitle.setForeground(new Color(103, 192, 144));
        pnlPhai.add(lblTableTitle, BorderLayout.NORTH);

        String[] columns = {"STT", "Họ tên", "Ngày sinh", "SĐT", "CMND", "Loại KH"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(Color.WHITE);


        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 600));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(225, 242, 232)));
        pnlPhai.add(scrollPane, BorderLayout.CENTER);

        // footer
        footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(103, 192, 144));
        footer.setPreferredSize(new Dimension(0, 60));
        footer.setBorder(new EmptyBorder(10, 20, 10, 20));

        btnQuayLai = new JButton("Quay Lại");
        btnQuayLai.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnQuayLai.setForeground(Color.WHITE);
        btnQuayLai.setBackground(Color.lightGray);
        btnQuayLai.setBorder(new EmptyBorder(10, 0, 10, 0));
        btnQuayLai.setPreferredSize(new Dimension(100,40));
        footer.add(btnQuayLai,BorderLayout.WEST);

        add(footer, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
         new ThongTinKhachHangGUI().setVisible(true);
    }
}
