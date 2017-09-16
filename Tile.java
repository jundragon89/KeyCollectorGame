import javax.swing.JButton;
import javax.swing.ImageIcon;

public class Tile extends JButton {

	// Tile coordinates
	private int x;
	private int y;
	// Check if the tile is walkable by the current player
	private Boolean walkable = false;
	// The player and key key the tile is holding
	private Key key = null;
	private Player player = null;

	// Cosntructor
	public Tile() {
		super();
	}

	public Tile(String text) {
		super(text);
	}

	public Tile(String text, ImageIcon icon) {
		super(text, icon);
	}

	public Tile(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	// Getters
	public int getTileX() {
		return x;
	}

	public int getTileY() {
		return y;
	}

	public Player getPlayer() {
		return player;
	}

	public Key getKey() {
		return key;
	}

	public Boolean hasPlayer() {
		if (player != null)
			return true;
		else
			return false;
	}

	public Boolean hasKey() {
		if (key != null)
			return true;
		else
			return false;
	}

	public Boolean getWalkable() {
		return walkable;
	}

	// Setter
	public void setPlayer(Player player) {
		this.player = player;
		if (player == null)
			if (hasKey())
				setIcon(key.getIcon());
			else
				setIcon(null);
		else
			setIcon(player.getIcon());
	}

	public void setKey(Key key) {
		this.key = key;
		setIcon(key.getIcon());
	}

	public void setWalkable(Boolean walkable) {
		// walkable is true only if the tile doesn't have player and the function is set to
		this.walkable = (walkable && !hasPlayer());
		if (this.walkable)
			setBackground(new java.awt.Color(255, 255, 0));
		else
			setBackground(new java.awt.Color(104, 69, 5));
	}

	public void removePlayer() {
		setPlayer(null);
	}


	// Move
	public Boolean attemptMove(Tile target) {
		// Check if the target is occupied by another player.
		if (target.getWalkable()) { // && 
			// Obtain key for player if the target tile has a key.
			if (target.hasKey()) {
				getPlayer().addKey(target.getKey());
			}
			// Move the player to the target tile
			target.setPlayer(getPlayer());
			target.getPlayer().setCoordinates(target.getTileX(), target.getTileY());
			// Delete the player from this tile
			removePlayer();

			return true;
		}
		else {
			return false;
		}
	}
}