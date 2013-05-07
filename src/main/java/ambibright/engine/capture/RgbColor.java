package ambibright.engine.capture;

public class RgbColor {
	private int red, green, blue;

	public RgbColor() {
	}

	public RgbColor(int red, int green, int blue) {
		update(red, green, blue);
	}

	void update(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int red() {
		return red;
	}

	public int green() {
		return green;
	}

	public int blue() {
		return blue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof RgbColor)) {
			return false;
		}
		RgbColor rgbColor = (RgbColor) o;
		return rgbColor.red == red && rgbColor.green == green && rgbColor.blue == blue;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + red;
		result = 31 * result + green;
		result = 31 * result + blue;
		return result;
	}

	@Override
	public String toString() {
		return "{" + red + "," + green + "," + blue + '}';
	}
}
