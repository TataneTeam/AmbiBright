package ambibright.engine;

import java.awt.Rectangle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ambibright.ressources.CurrentBounds;
import ambibright.ihm.AspectRatioDebugFrame;
import ambibright.engine.capture.RgbColor;
import ambibright.engine.capture.Image;
import ambibright.config.Config;

/**
 * Checks for changes in aspect ratio and update the bounds if any.
 * <p>
 * Assumptions :
 * <ul>
 * <li>We can only have black strips on left and right or top and bottom. Once
 * we found a couple, we don't process the other one.</li>
 * <li>No subtitles on left and right side. Subtitles can be present on top or
 * bottom but not simultaneously.</li>
 * <li>Both side must have the same strip size. If not, we ignore them.</li>
 * <li>For top and bottom strip, the four corner must be black and at least one
 * of the rest must be black to handle subtitle on top or bottom.</li>
 * </ul>
 * </p>
 */
public class AspectRatioService implements Runnable {

	private static class Result {
		private boolean ambiguous;
		// size of the black strip
		private int size;

		private Result() {
			this.ambiguous = true;
		}

		private Result(int size) {
			this.ambiguous = false;
			this.size = size;
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(AspectRatioService.class);
	// With some br iso, the ratio of the video is 16:9 but there are black
	// bands included in it and the main black color used is (16, 16,
	// 16) with the last few lines with varying dark color that go up to 23.
	private static final int blackLimit = 23;
	private final CurrentBounds currentBounds;
	private final Config config;
	private final AspectRatioDebugFrame debugFrame;
	/**
	 * RgbColor used to store the value. We create only one instance and update
	 * it to avoid the creation of million of objects.
	 */
	private final RgbColor color = new RgbColor();
	private Rectangle lastScreenBounds;
	private Rectangle prevScreenBounds;

	public AspectRatioService(CurrentBounds currentBounds, Config config) {
		this.currentBounds = currentBounds;
		this.config = config;
		this.lastScreenBounds = currentBounds.getFullscreenBounds();
		this.prevScreenBounds = lastScreenBounds;
		this.debugFrame = new AspectRatioDebugFrame();
	}

	@Override
	public void run() {
		logger.trace("Checking if aspect ratio changed");

		// we retrieve the fullscreen bounds and screen device in the same lock
		// to be sure we have coherent datas
		int screenDevice;
		Rectangle fullscreenBounds;
		currentBounds.readLock();
		try {
			screenDevice = currentBounds.getScreenDeviceNoLock();
			fullscreenBounds = currentBounds.getFullscreenBoundsNoLock();
		} finally {
			currentBounds.readUnlock();
		}

		// Get current image
		Image image = config.getScreenCapture().captureScreen(fullscreenBounds, screenDevice);

		int maxX = fullscreenBounds.width / 4;
		int maxY = fullscreenBounds.height / 4;

		Result leftAndRightResult = calculateLeftAndRightStrip(image, fullscreenBounds, maxX);
		Result topAndBottomResult;
		if (leftAndRightResult.ambiguous) {
			topAndBottomResult = null;
		} else if (leftAndRightResult.size > 0) {
			topAndBottomResult = new Result(0);
		} else {
			// didn't find black strip for sure on left and right side, we
			// calculate top and bottom
			topAndBottomResult = calculateTopAndBottomStrip(image, fullscreenBounds, maxY);
		}

		if (leftAndRightResult.ambiguous || topAndBottomResult.ambiguous) {
			logger.debug("The image is too dark to determine aspect ratio.");
		} else {
			int x = leftAndRightResult.size;
			int y = topAndBottomResult.size;

			Rectangle newBounds = new Rectangle(fullscreenBounds.x + x, fullscreenBounds.y + y, fullscreenBounds.width - (2 * x), fullscreenBounds.height - (2 * y));

			if (y >= maxY || x >= maxX) {
				logger.debug("Aspect ratio found lower than normal, we skip it : {}", newBounds);
			} else if (!prevScreenBounds.equals(newBounds)) {
				logger.debug("New aspect ratio found but different from the previous one. We wait the next calculation to see if it's not a glitch.");
				prevScreenBounds = newBounds;
			} else if (!lastScreenBounds.equals(newBounds)) {
				logger.info("Aspect ratio changed. New bounds : {}", newBounds);
				lastScreenBounds = newBounds;
				prevScreenBounds = newBounds;
				currentBounds.updateBounds(newBounds);
				if (config.isAspectRatioDebug()) {
					debugFrame.showAspectRatioChange(newBounds);
				}
			} else {
				logger.trace("Aspect ratio didn't change");
			}
		}

		// Flushing the image
		image.flush();
	}

	/**
	 * Indicates if the color given as parameter can be considered black.
	 *
	 * @param color
	 *            the color to test
	 * @return true if the color is black
	 */
	private boolean isBlack(RgbColor color) {
		return color.red() <= blackLimit && color.blue() <= blackLimit && color.green() <= blackLimit;
	}

	/**
	 * Calculate the top and bottom strip size. If we can't find it for sure
	 * because the two strip have not the same size, we return an ambiguous
	 * flag.
	 *
	 * @param image
	 *            the image to parse
	 * @param fullscreenBounds
	 *            the dimensions of the image
	 * @param maxY
	 *            the maximum y value to test
	 * @return a flag that indicate if the result is ambiguous or the size of
	 *         the strip
	 */
	private Result calculateTopAndBottomStrip(Image image, Rectangle fullscreenBounds, int maxY) {

		// we check the corner first
		int cornerSize = fullscreenBounds.width / 10;

		int topCornerSize = calculateTopStripSize(image, fullscreenBounds, maxY, 0, cornerSize);
		if (topCornerSize > 0) {
			topCornerSize = calculateTopStripSize(image, fullscreenBounds, topCornerSize, fullscreenBounds.width - cornerSize, fullscreenBounds.width);
		}

		if (topCornerSize == 0) {
			// no need to go further
			return new Result(0);
		}

		int bottomCornerSize = calculateBottomStripSize(image, fullscreenBounds, maxY, 0, cornerSize);
		if (bottomCornerSize > 0) {
			bottomCornerSize = calculateBottomStripSize(image, fullscreenBounds, bottomCornerSize, fullscreenBounds.width - cornerSize, fullscreenBounds.width);
		}

		if (bottomCornerSize == 0) {
			// no need to go further
			return new Result(0);
		}

		if (topCornerSize != bottomCornerSize) {
			// ambiguous
			return new Result();
		}

		// we browse the rest of the top strip that may contains subtitles
		int topRestSize = calculateTopStripSize(image, fullscreenBounds, topCornerSize, cornerSize, fullscreenBounds.width - cornerSize - 1);
		if (topRestSize == 0) {
			// no need to go further
			return new Result(0);
		}

		// we browse the rest of the bottom strip that may contains subtitles
		int bottomRestSize = calculateBottomStripSize(image, fullscreenBounds, bottomCornerSize, cornerSize, fullscreenBounds.width - cornerSize - 1);
		if (bottomRestSize == 0) {
			// no need to go further
			return new Result(0);
		}

		if (topRestSize < topCornerSize) {
			// we may have subtitles on top
			if (bottomCornerSize == bottomRestSize) {
				// bottom size == top corner size, we have a winner
				return new Result(bottomCornerSize);
			} else {
				return new Result();
			}
		} else {
			return new Result(topRestSize);
		}
	}

	/**
	 * Calculate the size of the top black strip
	 *
	 * @param image
	 *            image to parse
	 * @param fullscreenBounds
	 *            dimensions of the image
	 * @param currentTopSize
	 *            the current top size found
	 * @param startX
	 *            start x position
	 * @param endX
	 *            end x position
	 * @return the top size of the strip
	 */
	private int calculateTopStripSize(Image image, Rectangle fullscreenBounds, int currentTopSize, int startX, int endX) {
		int size = currentTopSize;
		for (int testX = startX; testX < endX; testX++) {
			for (int testY = 0; testY < size; testY++) {
				if (!isBlack(image.getRGB(testX, testY, color))) {
					size = Math.min(size, testY);
					break;
				}
			}
			if (size == 0) {
				break;
			}
		}
		return size;
	}

	/**
	 * Calculate the size of the bottom black strip
	 *
	 * @param image
	 *            image to parse
	 * @param fullscreenBounds
	 *            dimensions of the image
	 * @param currentBottomSize
	 *            the current bottom size found
	 * @param startX
	 *            start x position
	 * @param endX
	 *            end x position
	 * @return the bottom size of the strip
	 */
	private int calculateBottomStripSize(Image image, Rectangle fullscreenBounds, int currentBottomSize, int startX, int endX) {
		int size = currentBottomSize;
		for (int testX = startX; testX < endX; testX++) {
			for (int testY = 0; testY < size; testY++) {
				if (!isBlack(image.getRGB(testX, fullscreenBounds.height - 1 - testY, color))) {
					size = Math.min(size, testY);
					break;
				}
			}
			if (size == 0) {
				break;
			}
		}
		return size;
	}

	/**
	 * Calculate the left and right strip size. If we can't find it for sure
	 * because the two strip have not the same size, we return an ambiguous
	 * flag.
	 *
	 * @param image
	 *            the image to parse
	 * @param fullscreenBounds
	 *            the dimensions of the image
	 * @param maxX
	 *            the maximum x value to test
	 * @return a flag that indicate if the result is ambiguous or the size of
	 *         the strip
	 */
	private Result calculateLeftAndRightStrip(Image image, Rectangle fullscreenBounds, int maxX) {
		// calculating left strip
		int leftSize = maxX;
		for (int testY = 0; testY < fullscreenBounds.height; testY++) {
			for (int testX = 0; testX < leftSize; testX++) {
				if (!isBlack(image.getRGB(testX, testY, color))) {
					leftSize = Math.min(leftSize, testX);
					break;
				}
			}
			if (leftSize == 0) {
				break;
			}
		}

		if (leftSize == 0) {
			// no need to go further
			return new Result(0);
		}

		// calculating right strip
		int rightSize = maxX;
		for (int testY = 0; testY < fullscreenBounds.height; testY++) {
			for (int testX = 0; testX <= rightSize; testX++) {
				if (!isBlack(image.getRGB(fullscreenBounds.width - testX - 1, testY, color))) {
					rightSize = Math.min(rightSize, testX);
					break;
				}
			}
			if (rightSize == 0) {
				break;
			}
		}

		if (rightSize == 0) {
			// no need to go further
			return new Result(0);
		}

		if (rightSize == leftSize) {
			return new Result(leftSize);
		} else {
			// ambiguous
			return new Result();
		}
	}
}
