package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entity.NhanVien;

public class MHC_NhanVienQuanLy extends GiaoDienChinh implements ActionListener {

    private JButton btnDatVe;
    private JButton btnHuyVe;
    private JButton btnDoiVe;
    private JButton btnTimChuyenTau;
    private JButton btnDangXuat;
	private JButton btnQuanLyGa;
	private JButton btnQuanLyLichTrinh;
	private JButton btnLoaiChucVu;

   
    public MHC_NhanVienQuanLy(NhanVien nhanVien) {
        super(nhanVien);
        pnlChucNang = taoPanelMenuChinh();
        pnlChinh.add(pnlChucNang, BorderLayout.CENTER);
        
        btnQuanLyLichTrinh.addActionListener(this);
       btnQuanLyGa.addActionListener(this);
        btnDatVe.addActionListener(this);
        btnHuyVe.addActionListener(this);
        btnDoiVe.addActionListener(this);
        btnTimChuyenTau.addActionListener(this);
        btnDangXuat.addActionListener(this);
        btnLoaiChucVu.addActionListener(this);
    }

   
    public MHC_NhanVienQuanLy() {
        this(null);
    }

    public JPanel taoPanelMenuChinh() {
        pnlChucNang = new JPanel();
        pnlChucNang.setLayout(new GridLayout(0, 2, 0, 10));
        pnlChucNang.setBackground(new Color(221, 218, 208));

        // Nút Quản lý nhân viên
        btnDatVe = new JButton("QUẢN LÝ NHÂN VIÊN");
        btnDatVe.setBackground(Color.white);
        btnDatVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnDatVe.setForeground(new Color(93, 156, 236));
        btnDatVe.setIcon(chinhKichThuoc("/img/group.png", 60, 60));
        themHieuUngHover(btnDatVe, new Color(93, 156, 236));

        // Nút Quản lý hành khách
        btnHuyVe = new JButton("QUẢN LÝ HÀNH KHÁCH");
        btnHuyVe.setBackground(Color.white);
        btnHuyVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnHuyVe.setForeground(new Color(74, 140, 103));
        btnHuyVe.setIcon(chinhKichThuoc("/img/passenger.png", 55, 55));
        themHieuUngHover(btnHuyVe, new Color(74, 140, 103));

        //  Nút Quản lý chuyến tàu 
        btnDoiVe = new JButton("QUẢN LÝ CHUYẾN TÀU");
        btnDoiVe.setBackground(Color.white);
        btnDoiVe.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnDoiVe.setForeground(new Color(247, 82, 112));
        btnDoiVe.setIcon(chinhKichThuoc("/img/QLtrain.png", 55, 55));
        themHieuUngHover(btnDoiVe, new Color(247, 82, 112));

        // === Nút Quản lý khuyến mãi ===
        btnTimChuyenTau = new JButton("QUẢN LÝ KHUYẾN MÃI");
        btnTimChuyenTau.setBackground(Color.white);
        btnTimChuyenTau.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnTimChuyenTau.setForeground(new Color(228, 90, 146));
        btnTimChuyenTau.setIcon(chinhKichThuoc("/img/coupon.png", 65, 65));
        themHieuUngHover(btnTimChuyenTau, new Color(228, 90, 146));

        // === Nút Quản lý khuyến mãi ===
        btnQuanLyGa = new JButton("QUẢN LÝ GA");
        btnQuanLyGa.setBackground(Color.white);
        btnQuanLyGa.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnQuanLyGa.setForeground(new Color(247, 82, 112));
//        btnQuanLyGa.setIcon(chinhKichThuoc("/img/coupon.png", 65, 65));
//        themHieuUngHover(btnQuanLyGa, new Color(228, 90, 146));
        
     // === Nút Quản lý khuyến mãi ===
        btnQuanLyLichTrinh = new JButton("QUẢN LÝ LỊCH TRÌNH");
        btnQuanLyLichTrinh.setBackground(Color.white);
        btnQuanLyLichTrinh.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnQuanLyLichTrinh.setForeground(Color.CYAN);
//        btnQuanLyGa.setIcon(chinhKichThuoc("/img/coupon.png", 65, 65));
//        themHieuUngHover(btnQuanLyLichTrinh, new Color(228, 90, 146));
        
        btnLoaiChucVu = new JButton("QUẢN LÝ LOẠI CHỨC VỤ");
        btnLoaiChucVu.setBackground(Color.white);
        btnLoaiChucVu.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnLoaiChucVu.setForeground(Color.BLACK);
//        btnQuanLyGa.setIcon(chinhKichThuoc("/img/coupon.png", 65, 65));
//        themHieuUngHover(btnQuanLyLichTrinh, new Color(228, 90, 146));
        
        //Nút Đăng xuất
        btnDangXuat = new JButton("ĐĂNG XUẤT");
        btnDangXuat.setBackground(Color.white);
        btnDangXuat.setFont(new Font("Segoe UI", Font.BOLD, 30));
        btnDangXuat.setForeground(Color.orange);
        btnDangXuat.setIcon(chinhKichThuoc("/img/export.png", 55, 55));
        themHieuUngHover(btnDangXuat, Color.orange);

        //THÊM NÚT VÀO PANEL
        

        pnlChucNang.add(btnDatVe);
        pnlChucNang.add(btnHuyVe);
        pnlChucNang.add(btnDoiVe);
        pnlChucNang.add(btnTimChuyenTau);
        pnlChucNang.add(btnQuanLyLichTrinh);	
        pnlChucNang.add(btnQuanLyGa);
        pnlChucNang.add(btnLoaiChucVu);
        pnlChucNang.add(btnDangXuat);
        
        pnlChucNang.setPreferredSize(new Dimension(350, 70));
        return pnlChucNang;
    }

    
    private void themHieuUngHover(JButton btn, Color color) {
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
                btn.setBorder(BorderFactory.createLineBorder(color, 3));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.white);
                btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnDatVe) {
            new QuanLyNhanVien().setVisible(true);
            this.dispose();
        } 
        else if (src == btnHuyVe) {
            new QuanLyHanhKhach().setVisible(true);
            this.dispose();
        } 
        else if (src == btnDoiVe) {
            new QuanLyChuyenTau().setVisible(true);
            this.dispose();
        } 
        else if (src == btnTimChuyenTau) {
            new QuanLykhuyenMai().setVisible(true);
            this.dispose();
        } 
        else if (src == btnDangXuat) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn đăng xuất không?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    new GiaoDienDangNhap().setVisible(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                this.dispose();
            }
        }
        else if (src == btnQuanLyGa) {
            new QuanLyGa().setVisible(true);
            this.dispose();
        } 
        else if (src == btnQuanLyLichTrinh) {
            new QuanLyLichTrinh().setVisible(true);
            this.dispose();
        } else if (src == btnLoaiChucVu) {
            new QuanLyLoaiChucVu().setVisible(true);
            this.dispose();
        } 
        
    }
    public static void main(String[] args) {
		new MHC_NhanVienQuanLy().setVisible(true);
	}
}