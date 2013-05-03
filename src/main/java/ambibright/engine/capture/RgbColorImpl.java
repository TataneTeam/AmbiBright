package ambibright.engine.capture;

class RgbColorImpl implements Image.RgbColor {
	private final int red, green, blue;

	RgbColorImpl(int color) {
		red = (color & 0x00ff0000) >> 16;
		green = (color & 0x0000ff00) >> 8;
		blue = color & 0x000000ff;
	}

	RgbColorImpl(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	@Override
	public int red() {
		return red;
	}

	@Override
	public int green() {
		return green;
	}

	@Override
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

		RgbColorImpl rgbColor = (RgbColorImpl) o;

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
