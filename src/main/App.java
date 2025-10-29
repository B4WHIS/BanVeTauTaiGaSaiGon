package main;

import java.io.IOException;

import javax.swing.SwingUtilities;

import gui.DangNhapGUI;
import gui.LookAndFeelManager;

//tao va hien thi cs dang nhap
public class App {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				LookAndFeelManager.setNimbusLookAndFeel(); 
				System.out.println("Khởi động ứng dụng bán vé tàu...");
				 DangNhapGUI dangNhap = new DangNhapGUI();
				 dangNhap.setVisible(true);
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
				System.err.println("Lỗi khởi tạo giao diện Đăng nhập.");
			}catch (Exception e) {
                e.printStackTrace();
            }
		});
	}
}
