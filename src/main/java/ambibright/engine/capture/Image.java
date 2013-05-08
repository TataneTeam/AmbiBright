package ambibright.engine.capture;

import java.awt.image.BufferedImage;

/**
 *
 * @author Nicolas Morel
 */
public interface Image {

	/**
	 * @return the width of the image
	 */
	int getWidth();

	/**
	 * @return the height of the image
	 */
	int getHeight();

	/**
	 * @param x
	 * @param y
	 * @return a new instance of RgbColor containing the rgb value of the pixel
	 *         positionned at x, y.
	 */
	RgbColor getRGB(int x, int y);

	/**
	 * @param x
	 * @param y
	 * @param rgb
	 *            instance used to store the rgb color
	 * @return the same instance as rgb parameter with the new rgb color
	 */
	RgbColor getRGB(int x, int y, RgbColor rgb);

	/**
	 * This method returns a {@link BufferedImage}. Depending of the
	 * implementation, this method can take some time.
	 *
	 * @return a buffered image
	 */
	BufferedImage getBufferedImage();

	/**
	 * Deletes the image datas and free the memory
	 */
	void flush();

}
