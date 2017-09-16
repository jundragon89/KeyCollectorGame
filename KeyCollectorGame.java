import javax.swing.*;
import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class KeyCollectorGame extends javax.swing.JFrame implements ActionListener {

	Tile[][] tiles = new Tile[9][9];
	Player current_player;
	Tile current_tile;

	public static void main(String[] args) {
		new KeyCollectorGame();
	}

	public KeyCollectorGame() {
		super("Key Collector Game");

		// Create instruction panel
		JPanel textPanel = new JPanel(new java.awt.BorderLayout());
		javax.swing.JLabel instructionLabel = new javax.swing.JLabel("Click on the player to play!");
		textPanel.add(instructionLabel, "Center");

		// Create map panel
		JPanel mapPanel = new JPanel(new java.awt.GridLayout(9, 0));

		// Add tiles
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				tiles[i][j] = new Tile(i, j);
				tiles[i][j].setFocusable(false);
				tiles[i][j].setBackground(new java.awt.Color(104, 69, 5));
				tiles[i][j].addActionListener(this);
				mapPanel.add(tiles[i][j]);
			}
		}

		// Initialize game
		initialize();

		// Add panels
		add(textPanel, "North");
		add(mapPanel, "Center");

		// JFrame settings
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 700);
		setVisible(true);
	}

	private void initialize() {

		// Generate players
		Player bangei = new Player("Ban Gei", "icons/1.gif");
		Player arkimides = new Player("Ark Imides", "icons/2.gif");
		Player canser = new Player("Can Ser", "icons/3.gif");
		Player dozciztem = new Player("Doz Ciztem", "icons/4.gif");
		
		// Generate keys
		Key treasurechest = new Key("Treasure Chest", "icons/5.gif", new Movement(0, 0, 0, false));
		Key pinkey = new Key("Pinkey", "icons/a.gif", new Movement(1, 1, 1, false));
		Key donkey = new Key("Donkey", "icons/b.gif", new Movement(0, 0, 3, false));
		Key keydisk = new Key("Key Disk", "icons/c.gif", new Movement(3, 3, 0, false));
		Key keynote = new Key("Key Note", "icons/d.gif", new Movement(2, 2, 2, true));
		Key monkey = new Key("Monkey", "icons/e.gif", new Movement(3, 3, 3, false));

		// Place player on tiles
		tiles[0][0].setPlayer(bangei);
		tiles[0][8].setPlayer(arkimides);
		tiles[8][0].setPlayer(canser);
		tiles[8][8].setPlayer(dozciztem);

		// Place keys on tiles
		tiles[5][5].setKey(treasurechest);
		tiles[5][6].setKey(pinkey);
		tiles[5][7].setKey(keydisk);
		tiles[4][8].setKey(keynote);
		tiles[3][5].setKey(monkey);

		// Set current player
		current_player = tiles[0][0].getPlayer();
		current_tile = tiles[0][0];
		checkWalkable();

	}

	private void checkWalkable() {
		Movement m = current_player.getMovement();

		resetWalkable();
		if (m.mustSkip()) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) { 
					//  Check for:	Left, 			Right,
					//				Up, 			Down,
					//				Bottom Left, 	Bottom Right,
					//				Top Left, 		Top Right
					if (i == current_player.getX() && j == current_player.getY()-m.getVertical() ||
						i == current_player.getX() && j == current_player.getY()+m.getVertical() ||	
						i == current_player.getX()-m.getHorizontal() && j == current_player.getY() ||			
						i == current_player.getX()+m.getHorizontal() && j == current_player.getY() ||
						i == current_player.getX()+m.getDiagonal() && j == current_player.getY()-m.getDiagonal() ||
						i == current_player.getX()+m.getDiagonal() && j == current_player.getY()+m.getDiagonal() ||
						i == current_player.getX()-m.getDiagonal() && j == current_player.getY()-m.getDiagonal() ||
						i == current_player.getX()-m.getDiagonal() && j == current_player.getY()+m.getDiagonal()) {
							tiles[i][j].setWalkable(true);
					}
				}
			}
		}
		else {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) { 
					// Check vertical
 					for (int k = 1; k <= m.getVertical(); k++) {
 						if (i == current_player.getX() && j == current_player.getY()-k ||
							i == current_player.getX() && j == current_player.getY()+k) {
								tiles[i][j].setWalkable(true);
						}
 					}
 					// Check horizontal
 					for (int k = 1; k <= m.getHorizontal(); k++) {
 						if (i == current_player.getX()-k && j == current_player.getY() ||			
							i == current_player.getX()+k && j == current_player.getY()) {
								tiles[i][j].setWalkable(true);
						}
 					}
 					// Check diagonal
 					for (int k = 1; k <= m.getHorizontal(); k++) {
 						if (i == current_player.getX()+k && j == current_player.getY()-k ||
							i == current_player.getX()+k && j == current_player.getY()+k ||
							i == current_player.getX()-k && j == current_player.getY()-k ||
							i == current_player.getX()-k && j == current_player.getY()+k) {
								tiles[i][j].setWalkable(true);
						}
 					}
				}
			}
		}
	}

	private void resetWalkable() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				tiles[i][j].setWalkable(false);
			}
		}
	}


	@Override
	public void actionPerformed(ActionEvent ae) {
		Tile clickedTile = (Tile)ae.getSource();
		int x = clickedTile.getTileX();
		int y = clickedTile.getTileY();
		String player = "";
		String key = "";
		if (clickedTile.hasPlayer())
			player = tiles[x][y].getPlayer().getName();
		if (clickedTile.hasKey())
			key = tiles[x][y].getKey().getName();
		System.out.println("(" + x +", " + y + ") - " + player + key);

		// Attempt to move to clicked tile
		if (current_tile.attemptMove(clickedTile)) {
			// Update current player coordinates
			current_player.setCoordinates(clickedTile.getTileX(), clickedTile.getTileY());
			// Change the current tile
			current_tile = clickedTile;
			checkWalkable();
		}
	}
}