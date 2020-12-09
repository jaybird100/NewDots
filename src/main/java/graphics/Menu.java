package graphics;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//an interface for all the Menus used in the game
public interface Menu {
	
	public JPanel getPanel();
	
	//A method that gives a back button that is used to navigate back to the main menu
	public static JButton backButton() {
		ImageIcon icon = new ImageIcon(Paths.BUTTON_BACK);
		JButton back=new JButton(icon);
		back.setOpaque(false);
		back.setContentAreaFilled(false);
		back.setBorderPainted(false);
		back.setSize(170,63);
		back.setLocation(5,576);
		return back;
	}
	//A method that adds an action listener to change the panel that is being shown
	public static void setNavigationTo(MenuBasic base, JButton button, Menu menu) {
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				base.setVisiblePanel(menu.getPanel());
			}
		});
	}
}
