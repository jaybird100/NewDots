package graphics;

import javax.swing.*;

//Rules Menu
public class Rules implements Menu{
	private static Menu instance=null;;

	private JPanel panel;
	private JButton back;
	
	private Rules() {
    	this.panel= new Background(Paths.BACKGROUND_RULES);
    	panel.setLayout(null);
		
		back=Menu.backButton();
		//
		panel.add(back);
    }
    
    @Override
	public JPanel getPanel() {
    	return panel;
    }
    
    public static Menu getInstance() {
    	if(instance==null) instance=new Rules();
		return instance;
    }
    
    public void setUpActionListeners(MenuBasic base,Menu Main) {
    	Menu.setNavigationTo(base, this.back, Main);
    }
    
   
}
