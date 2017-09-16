import java.util.Stack;

public class Player extends Item {
	
	// Stack to store keys collected by player
	Stack<Key> keys = new Stack<Key>();
	// Coordinates
	private int x;
	private int y;

	public Player(String name, String iconPath) {
		// Movement define the movement of player, by default a player can move up to 2 squares in any direction.
		super(name, iconPath, new Movement(2, 2, 2, false));	
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

	@Override
	public void addKey(Key k) {
		// Put key in stack
		keys.push(k);
		// Set the movement of player to the one define in the latest key collected
		movement = keys.peek().getMovement();
	}

}