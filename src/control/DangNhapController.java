package control;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import connectDB.connectDB;
import entity.NhanVien;
import gui.DangNhapGUI;
import gui.NhanVienBanVeGUI;
import gui.NhanVienQuanLyGUI;

public class DangNhapController implements ActionListener, MouseListener {
    
    private DangNhapGUI view;

    public DangNhapController(DangNhapGUI view) {
        this.view = view;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        if (src == view.getTxtTenDN()) {
            view.getTxtMatKhau().requestFocus();
            return;
        } else if (src == view.getTxtMatKhau()) {
            view.getBtnDn().doClick();
            return;
        }
        
        if (src == view.getBtnThoat()) {
            xuLyThoat();
        } else if (src == view.getBtnDn()) {
            xuLyDangNhap();
        }
    }
    
    private void xuLyDangNhap() {
        String tenDangNhap = view.getTxtTenDN().getText().trim();
        String matKhau = new String(view.getTxtMatKhau().getPassword()).trim();
        
        if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = connectDB.getConnection();
           
            String sql = "SELECT lc.tenLoai, nv.hoTen, nv.maNhanVien " + 
                    "FROM TaiKhoan tk " +
                    "JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien " +
                    "JOIN LoaiChucVu lc ON nv.IDloaiChucVu = lc.IDloaiCV " +
                    "WHERE tk.tenDangNhap = ? AND tk.matKhau = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tenDangNhap);
            pstmt.setString(2, matKhau);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String loaiChucVu = rs.getString("tenLoai");
                String hoTen = rs.getString("hoTen");
                String maNhanVien = rs.getString("maNhanVien"); 

                NhanVien nhanVien = new NhanVien();
                nhanVien.setMaNhanVien(maNhanVien); 
                nhanVien.setHoTen(hoTen);
                nhanVien.setIDloaiChucVu(getIDFromChucVu(loaiChucVu));

                view.dispose();
                if ("Nhân viên bán vé".equals(loaiChucVu)) {
                    try {
                        new NhanVienBanVeGUI(nhanVien).setVisible(true); 
                    } catch (IOException ex) {
                        // ...
                    }
                } else if ("Nhân viên quản lý".equals(loaiChucVu)) {
                    new NhanVienQuanLyGUI(nhanVien).setVisible(true); 
                } else {
                    JOptionPane.showMessageDialog(null, "Loại chức vụ không được hỗ trợ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(view, "Tên đăng nhập hoặc mật khẩu không đúng!", "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
                view.getTxtMatKhau().setText("");
                view.getTxtTenDN().requestFocus();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Lỗi kết nối cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
  
    private int getIDFromChucVu(String chucVu) {
        switch (chucVu) {
            case "Nhân viên bán vé": return 1;
            case "Nhân viên quản lý": return 2;
            default: return 0;
        }
    }
    
    private void xuLyThoat() {
        int choice = JOptionPane.showOptionDialog(
            view,
            "Bạn có chắc chắn muốn thoát khỏi hệ thống?",
            "Xác Nhận Thoát Ứng Dụng",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"Thoát", "Hủy Bỏ"},
            "Hủy Bỏ"
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

  
    @Override public void mouseClicked(MouseEvent e) {} 
    @Override public void mousePressed(MouseEvent e) {} 
    @Override public void mouseReleased(MouseEvent e) {} 

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == view.getBtnDn()) {
            view.getBtnDn().setBackground(view.COLOR_LOGIN_BUTTON.darker()); 
        } else if (e.getSource() == view.getBtnThoat()) {
            view.getBtnThoat().setBackground(view.COLOR_EXIT_BUTTON.darker());
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == view.getBtnDn()) {
            view.getBtnDn().setBackground(view.COLOR_LOGIN_BUTTON);
        } else if (e.getSource() == view.getBtnThoat()) {
            view.getBtnThoat().setBackground(view.COLOR_EXIT_BUTTON);
        }
    }
}