package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImageFramePanel extends JPanel {
    private Image frameImage;

    // Khởi tạo với đường dẫn ảnh
    public ImageFramePanel(String imagePath) {
        super(new BorderLayout()); 
        
        // Tải ảnh sử dụng ImageIcon (giả định đường dẫn hợp lệ)
        try {
            // Sử dụng lớp hiện có để lấy ImageIcon (tùy thuộc vào cấu trúc package của bạn)
            ImageIcon originalIcon = GiaoDienChinh.chinhKichThuoc(imagePath, -1, -1); 
            this.frameImage = originalIcon.getImage(); // [3, 4]
        } catch (Exception e) {
            System.err.println("Lỗi tải hình ảnh khung: " + e.getMessage());
            this.frameImage = null;
        }
        
        // Quan trọng: Tắt opaque để đảm bảo các vùng trong suốt của ảnh hiện thị lớp dưới (nếu có)
        // Tuy nhiên, vì ta đang vẽ toàn bộ nền, ta có thể giữ nguyên opaque để tránh vấn đề.
        // Nhưng nếu muốn vẽ frame ảnh và cho phép các component con vẽ đè lên mà không bị nền mặc định che phủ, 
        // việc ghi đè paintComponent là cần thiết.
        setOpaque(false); // Để cho phép ảnh nền tùy chỉnh hiển thị
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Không gọi super.paintComponent(g) VÌ ta đã setOpaque(false)
        // và ta chỉ muốn vẽ ảnh của mình.
        
        if (frameImage != null) {
            Graphics2D g2d = (Graphics2D) g; // Ép kiểu sang Graphics2D [5, 6]
            
            // Vẽ và co giãn ảnh để khớp với kích thước panel (dùng làm nền/khung)
            // Kỹ thuật drawImage này cho phép scaling hình ảnh [7, 8]
            g2d.drawImage(frameImage, 0, 0, getWidth(), getHeight(), this); 
        }
        
        // Lưu ý: Các thành phần con sẽ được vẽ sau đó (do cơ chế của Swing).
    }
}
