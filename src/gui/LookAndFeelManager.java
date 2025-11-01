package gui;

import javax.swing.*;
import java.awt.*;

public class LookAndFeelManager {
    
    
    public static void setNimbusLookAndFeel() {
        setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    }
    
   
    public static void setSystemLookAndFeel() {
        setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    
   
    public static void setMetalLookAndFeel() {
        setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    }
    
   
    private static void setLookAndFeel(String lafClassName) {
        try {
            UIManager.setLookAndFeel(lafClassName);
            
            SwingUtilities.updateComponentTreeUI(new JFrame());
        } catch (Exception ex) {
            System.err.println("Lỗi khi set Look and Feel: " + ex.getMessage());
            
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(new JFrame());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public static String getCurrentLookAndFeel() {
        return UIManager.getLookAndFeel().getClass().getName();
    }
}