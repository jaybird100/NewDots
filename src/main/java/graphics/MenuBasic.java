package graphics;

import javax.swing.*;

//This is the main class of the GUI, it stores and allows for communication between all the other menus.
public class MenuBasic {
	private JFrame frame;
	private JPanel panel;
	
	private MainMenu main;
	private PlayMenu play;
	private Rules rules;
	private Credits credits;
	
	//Sets up the frame
	public MenuBasic() {
		setUpMenus();
		
		this.frame=new JFrame();
		frame.setTitle("Dots & Boxes");
		frame.setSize(Paths.FRAME_WIDTH, Paths.FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		panel=main.getPanel();
		frame.add(panel);
		
		ImageIcon img = new ImageIcon(Paths.TOP_ICON);
		frame.setIconImage(img.getImage());
		
		frame.setVisible(true);
	}
	//Changes which menu is currently present
	public void setVisiblePanel(JPanel newPanel) {
		frame.remove(panel);
		this.panel=newPanel;
		frame.add(panel);
		refreshFrame();
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public void refreshFrame() {
		frame.setVisible(false);
		frame.setVisible(true);
	}
	//Sets up the menu and their settings.
	private void setUpMenus() {
		main=(MainMenu) MainMenu.getInstance();
		play=(PlayMenu) PlayMenu.getInstance();
		rules=(Rules) Rules.getInstance();
		credits=(Credits) Credits.getInstance();
		
		main.setUpActionListeners(this, play, rules, credits);
		play.setUpActionListeners(this, main);
		rules.setUpActionListeners(this, main);
		credits.setUpActionListeners(this, main);
	}
}
