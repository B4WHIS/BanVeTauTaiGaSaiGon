package gui;

import javax.swing.UIManager;

public class lookandFeel {
	public lookandFeel() {
		 try {
	            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
}
