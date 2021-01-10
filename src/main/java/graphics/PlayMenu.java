package graphics;

import game.GameBoard;
import game.Graph;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.text.DecimalFormat;

public class PlayMenu implements Menu{
	private static Menu instance=null;
	//Components of the Panel
	private JPanel playMenuPanel;
	private JButton back,play,human,bot,size1,size2,size3,custom, bMCTS, bBASE, miniMax, QTable, DeepQ;
	private JTextField player1name, player2name;
	private JFormattedTextField boardW, boardH;
	private JCheckBox initials;
	//reference to original frame
	private MenuBasic base;
	//Game Settings
	private boolean botActive=false;
	private boolean MCTS=false;
	private boolean showInitials=false;
	private boolean customSize=false;
	private int size=1;
	private int botV=1;


	private PlayMenu() {
		playMenuPanel=new Background(Paths.BACKGROUND_PLAY);
		playMenuPanel.setLayout(null);

		back=Menu.backButton();

		playMenuPanel.add(back);
		setUpButtons();
	}

	private void setUpButtons(){
		setUpPlay();
		setUpPlayer();
		setUpBoard();
		setUpBots();
		
		add(play);
		add(human);
		add(bot);
		add(size1);
		add(size2);
		add(size3);
		add(custom);
		add(player1name);
		add(player2name);
		add(boardW);
		add(boardH);
		add(initials);
		
		add(bBASE);
		add(bMCTS);
		add(miniMax);
		add(QTable);
		add(DeepQ);
	}
	@Override
	public JPanel getPanel() {
		return this.playMenuPanel;
	}
	//adds a component to the panel
	private void add(Component obj) {
		this.playMenuPanel.add(obj);
	}
	private void setUpPlay(){
		play = Button(Paths.BUTTON_START);
		play.setLocation(475,573);
		play.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
			//Creates the game with the given settings
			if(customSize){
				Graph.setWidth(Math.abs(Integer.parseInt(boardW.getText())));
				Graph.setHeight(Math.abs(Integer.parseInt(boardH.getText())));
			}
			else{
				switch (size){
					case 1:
						Graph.setWidth(3);
						Graph.setHeight(3);
						break;
					case 2:
						Graph.setWidth(4);
						Graph.setHeight(4);
						break;
					case 3:
						Graph.setWidth(5);
						Graph.setHeight(5);
						break;
				}
			}
			if(botActive) {
				Graph.setBothPlayers(false);
				Graph.setPlayerisP1(false);
				if(botV==2) {
					Graph.setMCTS(true);
					Graph.setActivateRandom(false);
					Graph.setMiniMax(false);
					Graph.setDeepQ(false);
					Graph.setQTable(false);
					Graph.setPlayerisP1(true);
				}
				else if(botV==1) {
					Graph.setActivateRandom(true);
					Graph.setMCTS(false);
					Graph.setMiniMax(false);
					Graph.setDeepQ(false);
					Graph.setQTable(false);
					Graph.setPlayerisP1(true);
				}
				else if(botV==3) {
					Graph.setActivateRandom(false);
					Graph.setMCTS(false);
					Graph.setMiniMax(true);
					Graph.setDeepQ(false);
					Graph.setQTable(false);
					Graph.setPlayerisP1(true);
				}
				else if(botV==4) {
					// TODO set this to be QTable
					Graph.setActivateRandom(false);
					Graph.setMCTS(false);
					Graph.setMiniMax(false);
					Graph.setDeepQ(false);
					Graph.setQTable(true);
				}
				else if(botV==5) {
					Graph.setActivateRandom(false);
					Graph.setMCTS(false);
					Graph.setMiniMax(false);
					Graph.setDeepQ(true);
					Graph.setQTable(false);
				}

			}
			else {
				Graph.setActivateRandom(false);
				Graph.setMCTS(false);
				Graph.setMiniMax(false);
				Graph.setBothPlayers(true);
				Graph.setPlayerisP1(true);

			}
			try {
				new GameBoard();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
			Graph.setPlayer1Name(player1name.getText());
			Graph.setPlayer2Name(player2name.getText());
			Graph.setPlayerPlays(true);

			base.getFrame().setVisible(false);
		}});
	}
//Sets up the player buttons and options
	private void setUpPlayer(){
		human=Button(Paths.BUTTON_HUMAN_SELECTED);
		bot=Button(Paths.BUTTON_BOT);
		
		bot.setLocation(336,128);
		human.setLocation(164,128);
		bot.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
			botActive=true;
			setIcon(bot, Paths.BUTTON_BOT_SELECTED);
			setIcon(human, Paths.BUTTON_HUMAN);
			player2name.setEditable(false);
			player2name.setText("BaseBot");
			
			setIcon(bBASE, Paths.BUTTON_BASE_SELECTED);
			setIcon(bMCTS, Paths.BUTTON_MCTS);
		}});
		human.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
			botActive=false;
			setIcon(bot, Paths.BUTTON_BOT);
			setIcon(human, Paths.BUTTON_HUMAN_SELECTED);
			player2name.setEditable(true);
			player2name.setText("Player 2");
			
			setIcon(bBASE, Paths.BUTTON_BASE);
			setIcon(bMCTS, Paths.BUTTON_MCTS);
		}});
		
		player1name=new JTextField("Player 1");
		player2name=new JTextField("Player 2");
		
		player1name.setSize(Paths.BUTTONS_WIDTH_PLAY,30);
		player2name.setSize(Paths.BUTTONS_WIDTH_PLAY,30);

		player1name.setLocation(178,202);
		player2name.setLocation(178,255);
		
		initials= new JCheckBox();
		initials.setSize(30, 30);
		initials.setLocation(178,302);
		initials.setOpaque(false);
		initials.addItemListener(new ItemListener() {public void itemStateChanged(ItemEvent e) {showInitials=!showInitials;
		Graph.setInitials(showInitials);}});
	}
	
	private void setUpBots() {
		bMCTS=Button(Paths.BUTTON_MCTS);
		bBASE=Button(Paths.BUTTON_BASE);
		miniMax=Button(Paths.BUTTON_MIN);
		DeepQ=Button(Paths.BUTTON_DEEPQ);
		QTable=Button(Paths.BUTTON_QTABLE);
		
		bBASE.setLocation(323,445);
		bMCTS.setLocation(164,445);
		miniMax.setLocation(475,445);
		
		QTable.setLocation(323,495);
		DeepQ.setLocation(164,495);
		
		bBASE.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
			setIcon(bBASE, Paths.BUTTON_BASE_SELECTED);
			setIcon(bMCTS, Paths.BUTTON_MCTS);
			setIcon(miniMax, Paths.BUTTON_MIN);
			setIcon(QTable, Paths.BUTTON_QTABLE);
			setIcon(DeepQ, Paths.BUTTON_DEEPQ);
			
			botActive=true;
			botV=1;
			setIcon(bot, Paths.BUTTON_BOT_SELECTED);
			setIcon(human, Paths.BUTTON_HUMAN);
			player2name.setEditable(false);
			player2name.setText("BaseBot");
			
			player1name.setEditable(true);
			player1name.setText("Player1");
			
			setActiveSize(1);
		}});
		bMCTS.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
			setIcon(bBASE, Paths.BUTTON_BASE);
			setIcon(bMCTS, Paths.BUTTON_MCTS_SELECTED);
			setIcon(QTable, Paths.BUTTON_QTABLE);
			setIcon(DeepQ, Paths.BUTTON_DEEPQ);
			
			botActive=true;
			botV=2;
			setIcon(bot, Paths.BUTTON_BOT_SELECTED);
			setIcon(human, Paths.BUTTON_HUMAN);
			setIcon(miniMax, Paths.BUTTON_MIN);
			player2name.setEditable(false);
			player2name.setText("MCTS");
			
			player1name.setEditable(true);
			player1name.setText("Player1");
			
			setActiveSize(1);
		}});
		miniMax.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
			setIcon(bBASE, Paths.BUTTON_BASE);
			setIcon(bMCTS, Paths.BUTTON_MCTS);
			setIcon(QTable, Paths.BUTTON_QTABLE);
			setIcon(DeepQ, Paths.BUTTON_DEEPQ);
			
			botActive=true;
			botV=3;
			setIcon(bot, Paths.BUTTON_BOT_SELECTED);
			setIcon(human, Paths.BUTTON_HUMAN);
			setIcon(miniMax, Paths.BUTTON_MIN_SELECTED);
			player2name.setEditable(false);
			player2name.setText("MiniMax");
			
			player1name.setEditable(true);
			player1name.setText("Player1");
			
			setActiveSize(1);
		}});
		QTable.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
			setIcon(bBASE, Paths.BUTTON_BASE);
			setIcon(bMCTS, Paths.BUTTON_MCTS);
			setIcon(miniMax, Paths.BUTTON_MIN);
			setIcon(QTable, Paths.BUTTON_QTABLE_SELECTED);
			setIcon(DeepQ, Paths.BUTTON_DEEPQ);
			
			botActive=true;
			botV=4;
			setIcon(bot, Paths.BUTTON_BOT_SELECTED);
			setIcon(human, Paths.BUTTON_HUMAN);
			player1name.setEditable(false);
			player1name.setText("QTable");
			
			player2name.setEditable(true);
			player2name.setText("Player2");
			
			setActiveSize(4);
			
			boardW.setValue(3);
			boardH.setValue(3);
			
			boardW.setEditable(false);
			boardH.setEditable(false);
		}});
		DeepQ.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
			setIcon(bBASE, Paths.BUTTON_BASE);
			setIcon(bMCTS, Paths.BUTTON_MCTS);
			setIcon(miniMax, Paths.BUTTON_MIN);
			setIcon(QTable, Paths.BUTTON_QTABLE);
			setIcon(DeepQ, Paths.BUTTON_DEEPQ_SELECTED);
			
			botActive=true;
			botV=5;
			setIcon(bot, Paths.BUTTON_BOT_SELECTED);
			setIcon(human, Paths.BUTTON_HUMAN);
			player1name.setEditable(false);
			player1name.setText("DeepQ");
			
			player2name.setEditable(true);
			player2name.setText("Player2");
			
			setActiveSize(4);
			
			boardW.setValue(3);
			boardH.setValue(4);
			
			boardW.setEditable(false);
			boardH.setEditable(false);
		}});
	}
	
//Sets up the board options
	private void setUpBoard(){
		size1=Button(Paths.BUTTON_SIZE1_SELECTED);
		size2=Button(Paths.BUTTON_SIZE2);
		size3=Button(Paths.BUTTON_SIZE3);
		custom=Button(Paths.BUTTON_CUSTOM);
		
		size1.setLocation(164,339);
		size2.setLocation(323,339);
		size3.setLocation(475,339);
		custom.setLocation(164,389);
		
		boardW= new JFormattedTextField(new NumberFormatter(new DecimalFormat("##;")));
		boardH= new JFormattedTextField(new NumberFormatter(new DecimalFormat("##;")));
		boardW.setValue(5);
		boardH.setValue(5);
		
		boardW.setLocation(365, 400);
		boardW.setSize(40, 40);
		boardW.setEditable(false);
		
		boardH.setLocation(440, 400);
		boardH.setSize(40, 40);
		boardH.setEditable(false);
		
		
		
		size1.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){setActiveSize(1);}});
		size2.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){setActiveSize(2);}});
		size3.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){setActiveSize(3);}});
		custom.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){setActiveSize(4);}});
	}
	//Gives a button with an image
	private JButton Button(String path) {
		ImageIcon icon = new ImageIcon(path);
		JButton button=new JButton(icon);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setSize(Paths.BUTTONS_WIDTH_PLAY,Paths.BUTTONS_HEIGHT_PLAY);
		return button;
	}
	//changes the current choice of active board size 
	private void setActiveSize(int s) {
		if(s==size) return;
		switch (size){
		case 1:
			setIcon(size1, Paths.BUTTON_SIZE1);
			break;
		case 2:
			setIcon(size2, Paths.BUTTON_SIZE2);
			break;
		case 3:
			setIcon(size3, Paths.BUTTON_SIZE3);
			break;
		case 4:
			if(botV==4 || botV==5) {
				setIcon(custom, Paths.BUTTON_CUSTOM);
				setIcon(bBASE, Paths.BUTTON_BASE);
				setIcon(bMCTS, Paths.BUTTON_MCTS_SELECTED);
				setIcon(QTable, Paths.BUTTON_QTABLE);
				setIcon(DeepQ, Paths.BUTTON_DEEPQ);
			
				botActive=true;
				botV=2;
				setIcon(bot, Paths.BUTTON_BOT_SELECTED);
				setIcon(human, Paths.BUTTON_HUMAN);
				setIcon(miniMax, Paths.BUTTON_MIN);
				player2name.setEditable(false);
				player2name.setText("MCTS");
				
				player1name.setEditable(true);
				player1name.setText("Player1");
			}
			break;
		}
		
		switch (s){
		case 1:
			setIcon(size1, Paths.BUTTON_SIZE1_SELECTED);
			customSize=false;
			break;
		case 2:
			setIcon(size2, Paths.BUTTON_SIZE2_SELECTED);
			customSize=false;
			break;
		case 3:
			setIcon(size3, Paths.BUTTON_SIZE3_SELECTED);
			customSize=false;
			break;
		case 4:
			setIcon(custom, Paths.BUTTON_CUSTOM_SELECTED);
			customSize=true;
			boardW.setEditable(true);
			boardH.setEditable(true);
			break;
		}
		size=s;
	}
	//changes the icon of an image
	private void setIcon(JButton button, String path) {
		ImageIcon icon = new ImageIcon(path);
		button.setIcon(icon);
	}
	
	public static Menu getInstance() {
		if(instance==null) instance=new PlayMenu();
		return instance;
	}

	public void setUpActionListeners(MenuBasic base,Menu Main) {
		this.base=base;
	    Menu.setNavigationTo(base, this.back, Main);
	}
}
