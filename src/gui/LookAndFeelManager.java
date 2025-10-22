package gui;

import javax.swing.*;
import java.awt.*;

public class LookAndFeelManager {
    
    /**
     * Set Nimbus Look and Feel (hiện đại, mượt mà).
     * Gọi trước khi tạo JFrame để áp dụng toàn bộ ứng dụng.
     */
    public static void setNimbusLookAndFeel() {
        setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    }
    
    /**
     * Set System Look and Feel (theo OS: Windows, macOS, Linux).
     */
    public static void setSystemLookAndFeel() {
        setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    
    /**
     * Set Metal Look and Feel (cổ điển, cross-platform).
     */
    public static void setMetalLookAndFeel() {
        setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    }
    
    /**
     * Phương thức private chung để set LAF với fallback.
     */
    private static void setLookAndFeel(String lafClassName) {
        try {
            UIManager.setLookAndFeel(lafClassName);
            // Cập nhật UI để áp dụng LAF (có thể gọi sau khi tạo frame nếu cần)
            SwingUtilities.updateComponentTreeUI(new JFrame());
        } catch (Exception ex) {
            System.err.println("Lỗi khi set Look and Feel: " + ex.getMessage());
            // Fallback về System LAF nếu Nimbus/Metal lỗi
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(new JFrame());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Kiểm tra LAF hiện tại (tùy chọn, để debug).
     */
    public static String getCurrentLookAndFeel() {
        return UIManager.getLookAndFeel().getClass().getName();
    }
}