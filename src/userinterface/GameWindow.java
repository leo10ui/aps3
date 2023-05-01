package userinterface;

import javax.swing.JFrame;


public class GameWindow extends JFrame {

	public static final int SCREEN_WIDTH = 600;
	private GameScreen gameScreen;
	
	public GameWindow() {
		super("Super Amigo");
		setSize(SCREEN_WIDTH, 300);
		setLocation(400, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		
		
		
		gameScreen = new GameScreen();
		addKeyListener(gameScreen);
		add(gameScreen);
	}
	
	public void startGame() {
		setVisible(true);
		gameScreen.startGame();
	}

	public void CloseGame() {
		setVisible(false);
		dispose();
	 }


	public static void main(String args[]) {
		(new GameWindow()).startGame();
		
		
	}
}
