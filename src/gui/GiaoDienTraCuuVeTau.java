package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import control.TraCuuVeControl;
import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.HanhKhach;
import entity.NhanVien;
import entity.Ve;

public class GiaoDienTraCuuVeTau extends JFrame {
    private TraCuuVeControl dieuKhienTraCuu = new TraCuuVeControl();
	private NhanVien nhanVien;

    public GiaoDienTraCuuVeTau() {
    	this.nhanVien = nhanVien;
        setTitle("Tra cứu vé tàu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        Color MAU_CHU_DAO = new Color(103, 192, 144);
        Font FONT_TIEU_DE = new Font("Segoe UI", Font.BOLD, 30);
        Font FONT_NHAN = new Font("Segoe UI", Font.BOLD, 18);
        Font FONT_NHAP = new Font("Segoe UI", Font.PLAIN, 18);
        JPanel pnlChinh = new JPanel(new BorderLayout(10, 10));
        pnlChinh.setBackground(new Color(245, 247, 250));

        // tiêu đề 
        JLabel lblTieuDe = new JLabel("TRA CỨU VÉ TÀU", SwingConstants.CENTER);
        lblTieuDe.setFont(FONT_TIEU_DE);
        lblTieuDe.setForeground(Color.WHITE);
        lblTieuDe.setOpaque(true);
        lblTieuDe.setBackground(MAU_CHU_DAO);
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        pnlChinh.add(lblTieuDe, BorderLayout.NORTH);

        // thông tin tra cứu
        JPanel pnlTraCuu = new JPanel(new BorderLayout(10, 10));
        pnlTraCuu.setPreferredSize(new Dimension(450, 0));
        pnlTraCuu.setBackground(new Color(245, 247, 250));

        JLabel lblThongTin = new JLabel("THÔNG TIN TRA CỨU", SwingConstants.CENTER);
        lblThongTin.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblThongTin.setForeground(MAU_CHU_DAO);
        lblThongTin.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        pnlForm.setBackground(Color.WHITE);

        // label
        JLabel lblHoTen = new JLabel("Họ tên:");
        JLabel lblCMND = new JLabel("CMND/CCCD:");
        JLabel lblSDT = new JLabel("Số điện thoại:");
        JLabel[] cacNhan = {lblHoTen, lblCMND, lblSDT};
        for (JLabel lbl : cacNhan) {
            lbl.setFont(FONT_NHAN);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        // text
        JTextField txtHoTen = new JTextField();
        JTextField txtCMND = new JTextField();
        JTextField txtSDT = new JTextField();
        JTextField[] cacO = {txtHoTen, txtCMND, txtSDT};
        for (JTextField txt : cacO) {
            txt.setFont(FONT_NHAP);
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            txt.setPreferredSize(new Dimension(230, 40));
        }

        // bố trí
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlForm.add(lblHoTen, gbc);
        gbc.gridx = 1;
        pnlForm.add(txtHoTen, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlForm.add(lblCMND, gbc);
        gbc.gridx = 1;
        pnlForm.add(txtCMND, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnlForm.add(lblSDT, gbc);
        gbc.gridx = 1;
        pnlForm.add(txtSDT, gbc);

        //button
        JButton btnTim = taoButton("Tìm", new Color(93, 156, 236), "/img/timKiemDV.png");
        JButton btnLamMoi = taoButton("Làm mới", new Color(52, 152, 21), "/img/reset.png");
        JButton btnDoiVe = taoButton("Đổi vé", new Color(255, 193, 7), "/img/exchange.png");
        JButton btnHuyVe = taoButton("Hủy vé", new Color(231, 76, 60), "/img/bin.png");

        JPanel pnlNut = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlNut.setBackground(new Color(245, 247, 250));
        pnlNut.add(btnTim);
        pnlNut.add(btnLamMoi);
        pnlNut.add(btnDoiVe);
        pnlNut.add(btnHuyVe);

        pnlTraCuu.add(lblThongTin, BorderLayout.NORTH);
        pnlTraCuu.add(pnlForm, BorderLayout.CENTER);
        pnlTraCuu.add(pnlNut, BorderLayout.SOUTH);

        // bảng kết quả
        JPanel pnlBang = new JPanel(new BorderLayout(10, 10));
        pnlBang.setBackground(new Color(245, 247, 250));

        JLabel lblKetQua = new JLabel("KẾT QUẢ TRA CỨU", SwingConstants.CENTER);
        lblKetQua.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblKetQua.setForeground(MAU_CHU_DAO);
        lblKetQua.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlBang.add(lblKetQua, BorderLayout.NORTH);

        DefaultTableModel moHinhBang = new DefaultTableModel(
                new Object[]{"STT", "Họ tên", "Thông tin vé", "Giá vé (VNĐ)", "Loại vé", "Trạng thái"}, 0);

        JTable bangVe = new JTable(moHinhBang);
        bangVe.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bangVe.setRowHeight(30);
        bangVe.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        bangVe.setSelectionBackground(new Color(173, 216, 230));

        // Căn chỉnh độ rộng cột
        TableColumnModel kichThuocCot = bangVe.getColumnModel();
        kichThuocCot.getColumn(0).setPreferredWidth(40);
        kichThuocCot.getColumn(1).setPreferredWidth(150);
        kichThuocCot.getColumn(2).setPreferredWidth(250);
        kichThuocCot.getColumn(3).setPreferredWidth(120);
        kichThuocCot.getColumn(4).setPreferredWidth(120);
        kichThuocCot.getColumn(5).setPreferredWidth(150);

        JScrollPane cuonBang = new JScrollPane(bangVe);
        cuonBang.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnlBang.add(cuonBang, BorderLayout.CENTER);

        // footer
        JPanel pnlChan = new JPanel(new BorderLayout());
        pnlChan.setBackground(MAU_CHU_DAO);
        pnlChan.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton btnTroVe = taoButton("Trở về", new Color(41, 128, 185), "/img/arrow.png");
        btnTroVe.setPreferredSize(new Dimension(130, 45));
        pnlChan.add(btnTroVe, BorderLayout.WEST);

        pnlChinh.add(pnlTraCuu, BorderLayout.WEST);
        pnlChinh.add(pnlBang, BorderLayout.CENTER);
        pnlChinh.add(pnlChan, BorderLayout.SOUTH);
        add(pnlChinh);

        // action
        btnTim.addActionListener(e -> {
            String hoTen = txtHoTen.getText().trim();
            String cmnd = txtCMND.getText().trim();
            String sdt = txtSDT.getText().trim();

            try {
                List<Ve> dsVe = dieuKhienTraCuu.timVeTongHop(null, hoTen, cmnd, sdt, null);
                moHinhBang.setRowCount(0);
                int stt = 1;

                if (dsVe == null || dsVe.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy vé nào phù hợp!", "Kết quả", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "✅ Đã tìm thấy " + dsVe.size() + " vé phù hợp!", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
                    for (Ve ve : dsVe) {
                        HanhKhach hk = ve.getMaHanhkhach();
                        ChuyenTau ct = ve.getMaChuyenTau();
                        ChoNgoi cn = ve.getMaChoNgoi();
                        String tenHK = (hk != null) ? hk.getHoTen() : "Không rõ";
                        String thongTinVe = "";
                        if (ct != null) thongTinVe += "Mã chuyến: " + ct.getMaChuyenTau();
                        if (cn != null) thongTinVe += " | Chỗ: " + cn.getMaChoNgoi();
                        moHinhBang.addRow(new Object[]{
                                stt++, tenHK, thongTinVe,
                                String.format("%,.0f", ve.getGiaThanhToan()),
                                (ve.getMaKhuyenMai() != null ? "Khuyến mãi" : "Thường"),
                                ve.getTrangThai()
                        });
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm vé: " + ex.getMessage(), "Thông báo", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLamMoi.addActionListener(e -> {
            txtHoTen.setText("");
            txtCMND.setText("");
            txtSDT.setText("");
            moHinhBang.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Đã làm mới thông tin tra cứu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });

        btnTroVe.addActionListener(e -> {
            try {
                new MHC_NhanVienBanVe().setVisible(true);
                dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    //hàm tạo nút cho nhanh
    private JButton taoButton(String ten, Color mau, String duongDanIcon) {
        ImageIcon icon = chinhKichThuoc(duongDanIcon, 26, 26);
        JButton nut = new JButton(ten, icon);
        nut.setBackground(mau);
        nut.setForeground(Color.WHITE);
        nut.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nut.setFocusPainted(false);
        nut.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        nut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { nut.setBackground(mau.darker()); }
            public void mouseExited(java.awt.event.MouseEvent e) { nut.setBackground(mau); }
        });
        return nut;
    }

    public static ImageIcon chinhKichThuoc(String duongDan, int rong, int cao) {
        URL iconUrl = GiaoDienTraCuuVeTau.class.getResource(duongDan);
        if (iconUrl == null) {
        	return null;
        }
        ImageIcon icon = new ImageIcon(iconUrl);
        Image img = icon.getImage().getScaledInstance(rong, cao, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public static void main(String[] args) {
        LookAndFeelManager.setNimbusLookAndFeel();
        new GiaoDienTraCuuVeTau().setVisible(true);
    }
}
