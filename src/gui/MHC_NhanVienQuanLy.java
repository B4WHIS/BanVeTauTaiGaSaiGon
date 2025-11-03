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

    private JButton btnQLNV;
    private JButton btnQLHK;
    private JButton btnQLCT;
    private JButton btnQLKM;
    private JButton btnDangXuat;
	private JButton btnQuanLyGa;
	private JButton btnQLLT;
	private JButton btnLoaiChucVu;

   
    public MHC_NhanVienQuanLy(NhanVien nhanVien) {
        super(nhanVien);
        pnlChucNang = taoPanelMenuChinh();
        pnlChinh.add(pnlChucNang, BorderLayout.CENTER);
        
        btnQLLT.addActionListener(this);
   
        btnQLNV.addActionListener(this);
        btnQLHK.addActionListener(this);
        btnQLCT.addActionListener(this);
        btnQLKM.addActionListener(this);
        btnDangXuat.addActionListener(this);
 
    }

   
    public MHC_NhanVienQuanLy() {
        this(null);
    }

    public JPanel taoPanelMenuChinh() {
        pnlChucNang = new JPanel();
        pnlChucNang.setLayout(new GridLayout(0, 2, 0, 10));
        pnlChucNang.setBackground(new Color(221, 218, 208));

        // Nút Quản lý nhân viên
        btnQLNV = new JButton("QUẢN LÝ NHÂN VIÊN");
        btnQLNV.setBackground(Color.white);
        btnQLNV.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnQLNV.setForeground(new Color(93, 156, 236));
        btnQLNV.setIcon(chinhKichThuoc("/img/group.png", 60, 60));
        themHieuUngHover(btnQLNV, new Color(93, 156, 236));

        // Nút Quản lý hành khách
        btnQLHK = new JButton("QUẢN LÝ HÀNH KHÁCH");
        btnQLHK.setBackground(Color.white);
        btnQLHK.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnQLHK.setForeground(new Color(74, 140, 103));
        btnQLHK.setIcon(chinhKichThuoc("/img/passenger.png", 55, 55));
        themHieuUngHover(btnQLHK, new Color(74, 140, 103));

        //  Nút Quản lý chuyến tàu 
        btnQLCT = new JButton("QUẢN LÝ CHUYẾN TÀU");
        btnQLCT.setBackground(Color.white);
        btnQLCT.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnQLCT.setForeground(new Color(247, 82, 112));
        btnQLCT.setIcon(chinhKichThuoc("/img/QLtrain.png", 55, 55));
        themHieuUngHover(btnQLCT, new Color (102, 11, 5));

        // === Nút Quản lý khuyến mãi ===
        btnQLKM = new JButton("QUẢN LÝ KHUYẾN MÃI");
        btnQLKM.setBackground(Color.white);
        btnQLKM.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnQLKM.setForeground(new Color(228, 90, 146));
        btnQLKM.setIcon(chinhKichThuoc("/img/coupon.png", 65, 65));
        themHieuUngHover(btnQLKM, new Color (83, 59, 77));

    
        
     // === Nút Quản lý khuyến mãi ===
        btnQLLT = new JButton("QUẢN LÝ LỊCH TRÌNH");
        btnQLLT.setBackground(Color.white);
        btnQLLT.setFont(new Font("Segoe UI", Font.BOLD, 35));
        btnQLLT.setForeground(new Color (138, 190, 185));
        btnQLLT.setIcon(chinhKichThuoc("/img/lichTrinh.png", 55, 55));
        themHieuUngHover(btnQLLT, new Color (48, 86, 105));
        
        
        //Nút Đăng xuất
        btnDangXuat = new JButton("ĐĂNG XUẤT");
        btnDangXuat.setBackground(Color.white);
        btnDangXuat.setFont(new Font("Segoe UI", Font.BOLD, 30));
        btnDangXuat.setForeground(Color.orange);
        btnDangXuat.setIcon(chinhKichThuoc("/img/export.png", 55, 55));
        themHieuUngHover(btnDangXuat,new Color (123, 84, 47));

        //THÊM NÚT VÀO PANEL
        

        pnlChucNang.add(btnQLNV);
        pnlChucNang.add(btnQLHK);
        pnlChucNang.add(btnQLCT);
        pnlChucNang.add(btnQLKM);
        pnlChucNang.add(btnQLLT);	
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

        if (src == btnQLNV) {
            new QuanLyNhanVien().setVisible(true);
            this.dispose();
        } 
        else if (src == btnQLHK) {
            new QuanLyHanhKhach().setVisible(true);
            this.dispose();
        } 
        else if (src == btnQLCT) {
            new QuanLyChuyenTau().setVisible(true);
            this.dispose();
        } 
        else if (src == btnQLKM) {
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
      
        else if (src == btnQLLT) {
            try {
				new QuanLyLichTrinh().setVisible(true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            this.dispose();
        } 
        
    }
    public static void main(String[] args) {
		new MHC_NhanVienQuanLy().setVisible(true);
	}
}