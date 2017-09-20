import java.util.ArrayList;

public class Player extends Item {
	
	// Stack to store keys collected by player
	private ArrayList<Key> keys = new ArrayList<Key>();

	// Coordinates
	private int x;
	private int y;

	public Player(String name, String iconPath, int x, int y) {
		// Movement define the movement of player, by default a player can move up to 2 squares in any direction.
		super(name, iconPath, new Movement(2, 2, 2, false));
		setCoordinates(x, y);
	}

	// Setters
	public void setCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// Getters
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}


	// Class methods
	// Remove all the keys from player
	public void clearKeys() {
		keys.clear();
	}


	// Return a string of all the name of keys collected by user
	public String keysCollected() {
		String keysCollected = "";
		if (keys.size() > 0) {
			keysCollected = keys.get(0).getName();
			for (int i = 1; i < keys.size(); i++) {
				keysCollected += new String(", " + keys.get(i).getName());
			}
		}

		return keysCollected;
	}

	// Return the number of keys collected by the player
	public int numKeysCollected() {
		return keys.size();
	}

	// Add key
	public void addKey(Key k) {
		// Check if the key is already collected, if not, put key in list
		if (!keys.contains(k)) {
			
			keys.add(k);
		}
		// Set the movement of player to the one define in the latest key collected
		movement = keys.get(keys.size()-1).getMovement();
	}

}