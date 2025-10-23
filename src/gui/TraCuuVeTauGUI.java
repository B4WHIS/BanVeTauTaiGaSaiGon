package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

public class TraCuuVeTauGUI extends GiaoDienChinh implements ActionListener {

    //tp chính
    private JPanel pnlChinh;         
    private JPanel pnlTitle;        
    private JLabel lblTieuDe;

    // panel bên trái chứa form tra cứu
    private JPanel pnlTraCuu;          
    private JComboBox<String> cbGaDi;
    private JComboBox<String> cbGaDen;
    private JDateChooser jdcNgayDi;
    private JTextField txtMaVe, txtHoTen, txtCMND, txtSDT;
    private JComboBox<String> cbLoaiVe;
    private JButton btnTim, btnLamMoi, btnTroVe, btnInVe;

    // Panel phải
    private JPanel pnlKetQua;
    private JTable tblKetQua;
    private DefaultTableModel modelKetQua;
    private JScrollPane scpKetQua;

    // Màu
    private final Color COLOR_PRIMARY = new Color(74, 140, 103); 
    private final Color COLOR_ACCENT = new Color(93, 156, 236);      
    private final Color COLOR_ALERT = new Color(229, 115, 115);       
    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 42);
    private final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);

    public TraCuuVeTauGUI() {
      
        setTitle("Tra cứu vé tàu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        //pnlChinh
        pnlChinh = new JPanel(new BorderLayout(12, 12));
        pnlChinh.setBorder(new EmptyBorder(12, 12, 12, 12));
        getContentPane().add(pnlChinh);

        // tieude
        pnlTitle = new JPanel(new BorderLayout());
        pnlTitle.setBorder(BorderFactory.createEtchedBorder());
        lblTieuDe = new JLabel("TRA CỨU VÉ TÀU", SwingConstants.CENTER);
        lblTieuDe.setFont(FONT_TITLE);
        lblTieuDe.setForeground(COLOR_PRIMARY);
        pnlTitle.add(lblTieuDe, BorderLayout.CENTER);
        pnlChinh.add(pnlTitle, BorderLayout.NORTH);

        //panel trai
        pnlTraCuu = new JPanel(new GridBagLayout());
        TitledBorder tb = BorderFactory.createTitledBorder("THÔNG TIN TRA CỨU");
        tb.setTitleFont(FONT_SECTION);
        tb.setTitleColor(COLOR_ACCENT);
        pnlTraCuu.setBorder(tb);
        pnlTraCuu.setPreferredSize(new Dimension(420, 0));

        setupFormTraCuu();

        pnlChinh.add(pnlTraCuu, BorderLayout.WEST);

        //pnlketQua
        pnlKetQua = new JPanel(new BorderLayout());
        TitledBorder tbKetQua = BorderFactory.createTitledBorder("KẾT QUẢ XÁC THỰC");
        tbKetQua.setTitleFont(FONT_SECTION);
        tbKetQua.setTitleColor(COLOR_ALERT);
        pnlKetQua.setBorder(tbKetQua);

        setupTableKetQua();

        pnlChinh.add(pnlKetQua, BorderLayout.CENTER);

        //pnl duoi
        JPanel pnlChucNang = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnTroVe = new JButton("Trở về", GiaoDienChinh.chinhKichThuoc("/img/loginicon.png", 20, 20));
        btnTroVe.setBackground(COLOR_ALERT);
        btnTroVe.setForeground(Color.WHITE);
        btnTroVe.setFont(FONT_LABEL);
        btnTroVe.addActionListener(this);

        pnlChucNang.add(btnTroVe);
        pnlChinh.add(pnlChucNang, BorderLayout.SOUTH);

       
        btnTim.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnInVe.addActionListener(this);

//        pack();
    }

    private void setupFormTraCuu() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // ga di
        JLabel lblGaDi = new JLabel("Ga đi:");
        lblGaDi.setFont(FONT_LABEL);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlTraCuu.add(lblGaDi, gbc);

        cbGaDi = new JComboBox<>();
        cbGaDi.setFont(FONT_INPUT);
        cbGaDi.setPreferredSize(new Dimension(220, 32));
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(cbGaDi, gbc);

        // ga den
        JLabel lblGaDen = new JLabel("Ga đến:");
        lblGaDen.setFont(FONT_LABEL);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlTraCuu.add(lblGaDen, gbc);

        cbGaDen = new JComboBox<>();
        cbGaDen.setFont(FONT_INPUT);
        cbGaDen.setPreferredSize(new Dimension(220, 32));
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(cbGaDen, gbc);

        //ngay di
        JLabel lblNgayDi = new JLabel("Ngày đi:");
        lblNgayDi.setFont(FONT_LABEL);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlTraCuu.add(lblNgayDi, gbc);

        jdcNgayDi = new JDateChooser();
        jdcNgayDi.setPreferredSize(new Dimension(220, 32));
        jdcNgayDi.setFont(FONT_INPUT);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(jdcNgayDi, gbc);

        // ma ve
        JLabel lblMaVe = new JLabel("Mã vé:");
        lblMaVe.setFont(FONT_LABEL);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlTraCuu.add(lblMaVe, gbc);

        txtMaVe = new JTextField();
        txtMaVe.setFont(FONT_INPUT);
        txtMaVe.setPreferredSize(new Dimension(220, 32));
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(txtMaVe, gbc);

        // ho ten
        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setFont(FONT_LABEL);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlTraCuu.add(lblHoTen, gbc);

        txtHoTen = new JTextField();
        txtHoTen.setFont(FONT_INPUT);
        txtHoTen.setPreferredSize(new Dimension(220, 32));
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(txtHoTen, gbc);

        // cccd
        JLabel lblCMND = new JLabel("CMND/Hộ chiếu:");
        lblCMND.setFont(FONT_LABEL);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlTraCuu.add(lblCMND, gbc);

        txtCMND = new JTextField();
        txtCMND.setFont(FONT_INPUT);
        txtCMND.setPreferredSize(new Dimension(220, 32));
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(txtCMND, gbc);

        // sdt
        JLabel lblSDT = new JLabel("SĐT:");
        lblSDT.setFont(FONT_LABEL);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlTraCuu.add(lblSDT, gbc);

        txtSDT = new JTextField();
        txtSDT.setFont(FONT_INPUT);
        txtSDT.setPreferredSize(new Dimension(220, 32));
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(txtSDT, gbc);

        //loai ve
        JLabel lblLoaiVe = new JLabel("Loại vé:");
        lblLoaiVe.setFont(FONT_LABEL);
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        pnlTraCuu.add(lblLoaiVe, gbc);

        cbLoaiVe = new JComboBox<>(new String[] {"Tất cả", "Thường", "VIP", "Pro Suite"});
        cbLoaiVe.setFont(FONT_INPUT);
        cbLoaiVe.setPreferredSize(new Dimension(220, 32));
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(cbLoaiVe, gbc);

        // nút chức năng
        JPanel pnlNut = new JPanel(new GridLayout(1, 2, 10, 0));
        btnLamMoi = new JButton("Làm mới", GiaoDienChinh.chinhKichThuoc("/img/undo.png", 20, 20));
        btnLamMoi.setBackground(COLOR_ALERT);
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(FONT_LABEL);

        btnTim = new JButton("Tìm", GiaoDienChinh.chinhKichThuoc("/img/traCuu.png", 20, 20));
        btnTim.setBackground(COLOR_ACCENT);
        btnTim.setForeground(Color.WHITE);
        btnTim.setFont(FONT_LABEL);

        pnlNut.add(btnLamMoi);
        pnlNut.add(btnTim);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        pnlTraCuu.add(pnlNut, gbc);

        // nút in ve
        btnInVe = new JButton("In vé", GiaoDienChinh.chinhKichThuoc("/img/ve.png", 20, 20));
        btnInVe.setBackground(new Color(255, 237, 0));
        btnInVe.setFont(FONT_LABEL);
        btnInVe.setForeground(Color.BLACK);

        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        pnlTraCuu.add(btnInVe, gbc);
    }

    
    private void setupTableKetQua() {
        String[] columns = {"STT", "Mã vé", "Họ tên", "CMND", "Chuyến tàu", "Ga đi - Ga đến", "Ngày đi", "Giá vé (VNĐ)", "Loại vé", "Trạng thái"};
        modelKetQua = new DefaultTableModel(columns, 0) {
            
        };

        tblKetQua = new JTable(modelKetQua);
        tblKetQua.setRowHeight(48);
        tblKetQua.getTableHeader().setReorderingAllowed(false);
        tblKetQua.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblKetQua.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblKetQua.setFillsViewportHeight(true);

        scpKetQua = new JScrollPane(tblKetQua);
        scpKetQua.setBorder(BorderFactory.createLineBorder(new Color(225, 242, 232)));
        pnlKetQua.add(scpKetQua, BorderLayout.CENTER);

        //tro ve
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(new EmptyBorder(8, 8, 8, 8));


        JPanel pnlButtonsRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtonsRight.setOpaque(false);
        pnlFooter.add(pnlButtonsRight, BorderLayout.EAST);

        pnlKetQua.add(pnlFooter, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnTim) {
            modelKetQua.setRowCount(0); 
            if ((txtMaVe.getText().trim().isEmpty())
                    && (txtHoTen.getText().trim().isEmpty())
                    && (txtCMND.getText().trim().isEmpty())
                    && jdcNgayDi.getDate() == null) {
                JOptionPane.showMessageDialog(this, ".", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

        } else if (src == btnLamMoi) {
            cbGaDi.setSelectedIndex(-1);
            cbGaDen.setSelectedIndex(-1);
            jdcNgayDi.setDate(null);
            txtMaVe.setText("");
            txtHoTen.setText("");
            txtCMND.setText("");
            txtSDT.setText("");
            cbLoaiVe.setSelectedIndex(0);
            modelKetQua.setRowCount(0);
        } else if (src == btnInVe) {
            JOptionPane.showMessageDialog(this, ".", "In vé", JOptionPane.INFORMATION_MESSAGE);
        } else if (src == btnTroVe) {
        
        }
    }

    public static void main(String[] args) {
        try {
            LookAndFeelManager.setNimbusLookAndFeel();
        } catch (Exception ex) {
        }

        SwingUtilities.invokeLater(() -> {
            TraCuuVeTauGUI gui = new TraCuuVeTauGUI();
            gui.setVisible(true);
        });
    }
}
