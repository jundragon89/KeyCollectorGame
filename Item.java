import javax.swing.ImageIcon;

// Parent class for Player and Key
public abstract class Item {

	// Item's name
	private String name;

	// Item's icon
	private ImageIcon icon;

	// Item's coordinates
	protected int x;
	protected int y;

	// Item's movement
	protected Movement movement;

	// Constructor
	public Item(String name, String iconPath, Movement movement) {
		this.name = name;
		this.icon = new ImageIcon(iconPath);
		this.x = x;
		this.y = y;
		this.movement = movement;
	}

	// Getters
	public String getName() {
		return name;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public Movement getMovement() {
		return movement;
	}

	// Setters
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}