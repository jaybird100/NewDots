package graphics;

import javax.swing.*;

//Main menu of the game
public class MainMenu implements Menu{
	private static Menu instance=null;;
	
	private JPanel MainMenuPanel;

	private JButton play;
	private JButton rules;
	private JButton credits;
	
	
	private MainMenu() {
		this.MainMenuPanel=new Background(Paths.BACKGROUND_MAIN);
		this.MainMenuPanel.setLayout(null);
		setUpImageButtons();
	}
	//Sets up the action listeners for all buttons in the menu
	public void setUpActionListeners(MenuBasic base,Menu playM, Menu rulesM, Menu creditsM) {
		Menu.setNavigationTo(base, this.play, playM);
		Menu.setNavigationTo(base, this.rules, rulesM);
		Menu.setNavigationTo(base, this.credits, creditsM);
	}
	
	
	@Override
	public JPanel getPanel() {
		return this.MainMenuPanel;
	}
	
	private void setUpImageButtons() {
		setUpPlay();
		setUpRules();
		setUpCredits();
		
		this.MainMenuPanel.add(play);
		this.MainMenuPanel.add(rules);
		this.MainMenuPanel.add(credits);
	}
	
	private void setUpPlay() {
		play=Button(Paths.BUTTON_PLAY);
		play.setLocation(160,237);
	}
	
	private void setUpRules() {
		rules=Button(Paths.BUTTON_RULES);
		rules.setLocation(160,329);
		
	}
	
	private void setUpCredits() {
		credits=Button(Paths.BUTTON_CREDITS);
		credits.setLocation(160,422);
		
		
	}
	//Creates a button with a specific image displayed on it
	private JButton Button(String path) {
		ImageIcon icon = new ImageIcon(path);
		JButton button=new JButton(icon);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setSize(Paths.BUTTONS_WIDTH,Paths.BUTTONS_HEIGHT);
		return button;
	}

	public static Menu getInstance() {
		if(instance==null)instance=new MainMenu();
		return instance;
	}
	
}
