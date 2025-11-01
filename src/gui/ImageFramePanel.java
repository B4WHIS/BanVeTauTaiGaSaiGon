package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImageFramePanel extends JPanel {
    private Image frameImage;

    
    public ImageFramePanel(String imagePath) {
        super(new BorderLayout()); 
        
       
        try {
          
            ImageIcon originalIcon = GiaoDienChinh.chinhKichThuoc(imagePath, -1, -1); 
            this.frameImage = originalIcon.getImage(); // [3, 4]
        } catch (Exception e) {
            System.err.println("Lỗi tải hình ảnh khung: " + e.getMessage());
            this.frameImage = null;
        }
        
        
        setOpaque(false); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        
        
        if (frameImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            
         
            g2d.drawImage(frameImage, 0, 0, getWidth(), getHeight(), this); 
        }
       
     
    }
}
