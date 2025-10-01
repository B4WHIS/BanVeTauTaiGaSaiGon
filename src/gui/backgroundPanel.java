package gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class backgroundPanel extends JPanel{
	 private Image backgroundImage;
	 
	public backgroundPanel(String duongDan) throws IOException {
		// TODO Auto-generated constructor stub
		backgroundImage = ImageIO.read(getClass().getResource(duongDan));
		
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
			
		}
	}
			
}
