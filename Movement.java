// Class that defines the range of movement of a player
public class Movement {

	// Horizontal, Vertical, Diagonal
	private int horizontal;
	private int vertical;
	private int diagonal;

	// Is it up to or must be skipped
	private Boolean mustSkip = false;


	// Constructor
	public Movement(int horizontal, int vertical, int diagonal, Boolean mustSkip) {
		this.horizontal = horizontal;
		this.vertical = vertical;
		this.diagonal = diagonal;
		this.mustSkip = mustSkip;
	}

	// Getters
	public int getHorizontal() {
		return horizontal;
	}

	public int getVertical() {
		return vertical;
	}

	public int getDiagonal() {
		return diagonal;
	}

	public Boolean mustSkip() {
		return mustSkip;
	}
}