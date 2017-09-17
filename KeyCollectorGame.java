import java.util.Random;
import java.util.ArrayList;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class KeyCollectorGame extends javax.swing.JFrame implements ActionListener  {

	// Game objects
	Tile[][] tiles = new Tile[9][9];
	Player[] players = new Player[4];
	Key[] keys = new Key[6];
	Player current_player;
	Tile current_tile;
	int round = 0;
	Boolean gameOver = false;
	String gameStatus;

	// GUI components
	// Create status panel
	JPanel statusPanel = new JPanel(new FlowLayout());
	javax.swing.JLabel statusLabel = new javax.swing.JLabel("");
	JPanel actionPanel = new JPanel(new BorderLayout());
	JPanel menuPanel = new JPanel(new FlowLayout());
	javax.swing.JLabel actionLabel = new javax.swing.JLabel("");
	

	public static void main(String[] args) throws FileNotFoundException {
		new KeyCollectorGame();
	}

	public KeyCollectorGame() {
		super("Key Collector Game");

		// Create map panel
		JPanel mapPanel = new JPanel(new java.awt.GridLayout(9, 9));

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

		// Add components to panels
		statusPanel.add(statusLabel);
		actionPanel.add(actionLabel, "North");
		JButton newBtn = new JButton("New");
		JButton loadBtn = new JButton("Load");
		JButton saveBtn = new JButton("Save");
		newBtn.addActionListener(this);
		loadBtn.addActionListener(this);
		saveBtn.addActionListener(this);
		menuPanel.add(newBtn);
		menuPanel.add(loadBtn);
		menuPanel.add(saveBtn);
		actionPanel.add(menuPanel, "South");


		// Add panels
		add(statusPanel, "North");
		add(mapPanel, "Center");
		add(actionPanel, "South");


		// JFrame settings
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 700);
		setVisible(true);
	}

	private void initialize() {

		// Reset game over
		gameOver = false;

		// Generate players
		Player bangei = new Player("Ban Gei", "icons/1.gif", 0, 0);
		Player arkimides = new Player("Ark Imides", "icons/2.gif", 0, 8);
		Player canser = new Player("Can Ser", "icons/3.gif", 8, 0);
		Player dozciztem = new Player("Doz Ciztem", "icons/4.gif", 8, 8);

		// Put players into an array
		players[0] = bangei;
		players[1] = arkimides;
		players[2] = canser;
		players[3] = dozciztem;
		
		// Generate keys
		Key treasurechest = new Key("Treasure Chest", "icons/5.gif", new Movement(0, 0, 0, false));
		Key pinkey = new Key("Pinkey", "icons/a.gif", new Movement(1, 1, 1, false));
		Key donkey = new Key("Donkey", "icons/b.gif", new Movement(0, 0, 3, false));
		Key keydisk = new Key("Key Disk", "icons/c.gif", new Movement(3, 3, 0, false));
		Key keynote = new Key("Key Note", "icons/d.gif", new Movement(2, 2, 2, true));
		Key monkey = new Key("Monkey", "icons/e.gif", new Movement(3, 3, 3, false));

		// Put keys into array
		keys[0] = treasurechest;
		keys[1] = pinkey;
		keys[2] = donkey;
		keys[3] = keydisk;
		keys[4] = keynote;
		keys[5] = monkey;

		// Place player on tiles
		tiles[0][0].setPlayer(bangei);
		tiles[0][8].setPlayer(arkimides);
		tiles[8][0].setPlayer(canser);
		tiles[8][8].setPlayer(dozciztem);

		// Place keys on tiles
		tiles[4][4].setKey(treasurechest);
		randomPlaceKeys();

		// Cheat code
		players[0].addKey(keydisk);
		players[0].addKey(keynote);
		players[0].addKey(donkey);
		players[0].addKey(pinkey);
		players[0].addKey(monkey);

		// Initialize first player
		current_player = players[0];
		current_tile = tiles[current_player.getX()][current_player.getY()];
		// Check walkable tiles for first player
		checkWalkable();
		// Update text labels
		updateGameStatus();
		updateActionLabel("");

	}

	private void randomPlaceKeys() {
		// ArrayList to store coordinates of unoccupied tiles
		ArrayList<Integer> xList = new ArrayList<Integer>();
		ArrayList<Integer> yList = new ArrayList<Integer>();
		// Random seed
		Random rand = new Random();
		// Check and store the coordinates with no player and key occupied
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (!tiles[i][j].hasPlayer() && !tiles[i][j].hasKey()) {
					xList.add(i);
					yList.add(j);
				}
			}
		}
		//
		for (int i = 1; i < 6; i++) {
			// Generate a number between 0 to the (size of available tiles - 1)
			int n = rand.nextInt(xList.size() - 1);
			// Get the nth integer from xList and yList and load as x and y
			int x = xList.get(n);
			int y = yList.get(n);
			// Remove nth integer from xList and yList, so it won't be repeated
			xList.remove(n);
			yList.remove(n);
			// Set ith key to tiles[x][y]
			tiles[x][y].setKey(keys[i]);
		}

	}

	private void checkWalkable() {
		// Get movement of current player
		Movement m = current_player.getMovement();
		// Reset all tile's walkable status
		resetWalkable();
		// Set tiles's walkable status
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
 					for (int k = 1; k <= m.getDiagonal(); k++) {
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
		// Make treasure chest unwalkable if player haven't collected all keys
		if (current_player.numKeysCollected() < 5) {
			tiles[4][4].setWalkable(false);
		}
	}

	private void resetWalkable() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				tiles[i][j].setWalkable(false);
			}
		}
	}

	private void updateGameStatus() {
		if (!gameOver) 
			gameStatus = "<html><b>" + current_player.getName() + "</b>'s turn.<br>";
		else
			gameStatus = "<html><b>" + tiles[4][4].getPlayer().getName() + "</b> wins!<br>";

		for (int  i = 0; i < 4; i++) {
			gameStatus += "<b>" + players[i].getName() + "</b>'s collected: " + players[i].keysCollected() + "<br>";
		}
		gameStatus += "</html>";
		statusLabel.setText(gameStatus);
	}

	private void updateActionLabel(String s) {
		String action = "<html>" + s + "</html>";
		actionLabel.setText(action);
	}

	private void newGame() {
		// Clear the tiles
		clearBoard();
		// Reinitialize
		initialize();
	}

	private void clearBoard() {
		// Clear the tiles
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				tiles[i][j].setPlayer(null);
				tiles[i][j].setKey(null);
			}
		}
	}

	private void loadGame() {
		String s = null;
		int x = -1;
		int y = -1;
		try {
			// Open save file
	    	FileInputStream in = new FileInputStream("save.txt");

	    	// Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			// Clear the tiles
			clearBoard();

			// Load round number
			if ((s = br.readLine()) != null) {
				round = Integer.parseInt(s);
			}

	    	// Load current player
	    	if ((s = br.readLine()) != null) {
	    		for (int i = 0; i < 4; i++) {
	    			// Clear all player's key
	    			players[i].clearKeys();
	    			// Set current player
	    			if (players[i].getName().equals(s)) {
						current_player = players[i];
					}
				}
			}
			System.out.println(current_tile.getTileX() + " " + current_tile.getTileY());
			// Load current tile (x, y)
			if ((s = br.readLine()) != null) {
				x = Integer.parseInt(s);
			}
			if ((s = br.readLine()) != null) {
				y = Integer.parseInt(s);
			}
			current_tile = tiles[x][y];

			// Load player's collected keys
    		for (int i = 0; i < 4; i++) {
    			if ((s = br.readLine()) != null) {
    				String[] keys = s.split(",");
    				for (int j = 0; j < keys.length; j++) {
    					// Remove spacing
    					keys[j] = keys[j].replace(" ", "");
    					if (keys[j].equals("Pinkey")) {
    						players[i].addKey(new Key("Pinkey", "icons/a.gif", new Movement(1, 1, 1, false)));
    					} else if (keys[j].equals("Donkey")) {
							players[i].addKey(new Key("Donkey", "icons/b.gif", new Movement(0, 0, 3, false)));
    					} else if (keys[j].equals("Monkey")) {
    						players[i].addKey(new Key("Monkey", "icons/e.gif", new Movement(3, 3, 3, false)));
    					} else if (keys[j].equals("KeyDisk")) {
							players[i].addKey(new Key("Key Disk", "icons/c.gif", new Movement(3, 3, 0, false)));
    					} else if (keys[j].equals("KeyNote")) {
    						players[i].addKey(new Key("Key Note", "icons/d.gif", new Movement(2, 2, 2, true)));
    					}
    				}
    			}
    		}
    		// Load all tiles
    		for (int i = 0; i < 81; i++) {
				for (int j = 0; j < 9; j++) {
					// Load player if any
					if ((s = br.readLine()) != null) {
						if (s.equals("Ban Gei")) {
	    					tiles[i][j].setPlayer(players[0]);
	    					players[0].setCoordinates(i, j);
	    				} else if (s.equals("Ark Imides")) {
	    					tiles[i][j].setPlayer(players[1]);
	    					players[1].setCoordinates(i, j);
	    				} else if (s.equals("Can Ser")) {
	    					tiles[i][j].setPlayer(players[2]);
	    					players[2].setCoordinates(i, j);
	    				} else if (s.equals("Doz Ciztem")) {
	    					tiles[i][j].setPlayer(players[3]);
	    					players[3].setCoordinates(i, j);
	    				}
					}
					// Load key if any
					if ((s = br.readLine()) != null) {
						if (s.equals("Treasure Chest")) {
	    					tiles[i][j].setKey(keys[0]);
	    				} else if (s.equals("Pinkey")) {
	    					tiles[i][j].setKey(keys[1]);
	    				} else if (s.equals("Donkey")) {
	    					tiles[i][j].setKey(keys[2]);
	    				} else if (s.equals("Key Disk")) {
	    					tiles[i][j].setKey(keys[3]);
	    				} else if (s.equals("Key Note")) {
	    					tiles[i][j].setKey(keys[4]);
	    				} else if (s.equals("Monkey")) {
	    					tiles[i][j].setKey(keys[5]);
	    				}
					}
				}
			}
			// Close file
			in.close();
			// Recalculate walkable 
			checkWalkable();
			// Update status
			updateGameStatus();
		}
	    catch (FileNotFoundException ex) {
			updateActionLabel("Error opening save file.");
		}
		catch (IOException io) {
			updateActionLabel("Error loading save file.");
		}
	}

	private void saveGame() {
		String s = "";
		if (!gameOver) {
			try {
				// Create or load save file
		    	FileOutputStream out = new FileOutputStream("save.txt");
		    	// Save current round number
		    	s += round + System.getProperty("line.separator");
		    	// Save current player and tile
		    	s += current_player.getName() + System.getProperty("line.separator");
		    	s += current_player.getX() + System.getProperty("line.separator");
		    	s += current_player.getY() + System.getProperty("line.separator");
		    	// Save player's key
		    	for (int i = 0; i < 4; i++) {
		    		s += players[i].keysCollected() + System.getProperty("line.separator");
		    	}
		    	// Save all tiles status
		    	for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						if (tiles[i][j].getPlayer() != null)
							s += tiles[i][j].getPlayer().getName() + System.getProperty("line.separator");
						else
							s += System.getProperty("line.separator");
						if (tiles[i][j].getKey() != null)
							s += tiles[i][j].getKey().getName() + System.getProperty("line.separator");
						else
							s += System.getProperty("line.separator");
					}
				}
		    	byte data[] = s.getBytes();
		    	out.write(data);
		    	out.close();	
		 	}
		    catch (FileNotFoundException ex) {

			}
			catch (IOException io) {

		    }
		} else {
			updateActionLabel("Can't save after game over! Click 'New' to start a new game.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		// Convert action event source to button
		JButton clickedButton = (JButton)ae.getSource();
		System.out.println(clickedButton.getText());
		if (clickedButton.getText().equals("New")) {
			newGame();
		}
		else if (clickedButton.getText().equals("Load")) {
			loadGame();
		}
		else if (clickedButton.getText().equals("Save")) {
			saveGame();
		}
		else {
			// Only allow any actions if the game haven't end
			if (!gameOver) {
				// Convert action event source to tile
				Tile clickedTile = (Tile)ae.getSource();

				// Debugging placeholder
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
					// Clear action label
					updateActionLabel("");
					// Update current player coordinates
					current_player.setCoordinates(clickedTile.getTileX(), clickedTile.getTileY());

					// Game over if current player occupied the tile treasure is on
					gameOver = (tiles[4][4].getPlayer() == current_player);

					// Game loop
					if (!gameOver) {
						// Increase round
						round++;
						// Set current player
						current_player = players[round%4];
						current_tile = tiles[current_player.getX()][current_player.getY()];
						// Refresh walkables
						checkWalkable();
					}
					else {
						// Update game status
						resetWalkable();
					}
					// Update game status
					updateGameStatus();
				}
				else {
					updateActionLabel("Invalid tile to move to.");
				}
			}
		}
	}
}