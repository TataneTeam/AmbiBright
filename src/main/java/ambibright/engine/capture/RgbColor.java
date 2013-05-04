package ambibright.engine.capture;

public class RgbColor {
	private int red, green, blue;

	public RgbColor() {
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
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		RgbColor rgbColor = (RgbColor) o;

		if (blue != rgbColor.blue) {
			return false;
		}
		if (green != rgbColor.green) {
			return false;
		}
		if (red != rgbColor.red) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = red;
		result = 31 * result + green;
		result = 31 * result + blue;
		return result;
	}

	@Override
	public String toString() {
		return "{" + red + "," + green + "," + blue + '}';
	}
}
