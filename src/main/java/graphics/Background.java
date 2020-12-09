package graphics;

import javax.swing.*;
import java.awt.*;

//This is a class that acts as a JPanel with a background that is an image.
public class Background extends JPanel{
	private Image bg;
	
	public Background(String path) {
		ImageIcon img= new ImageIcon(path);
		bg= img.getImage();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bg,0,0,null);
	}


}
