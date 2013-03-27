package ambibright.ressources;

import java.awt.Rectangle;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ScreenSquares {

	private int ledNumberLeftRight, ledNumberTop;
	private int squareSize;
	private int squareSizeLeftRight, squareSizeTop;
	private int i, currentLed;
	private int paddingLR, paddingTOP;
	private Rectangle[] bounds;

	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	public ScreenSquares(Rectangle bound, int ledNumberLeftRight, int ledNumberTop, int squareSize) {
		super();
		this.ledNumberLeftRight = ledNumberLeftRight;
		this.ledNumberTop = ledNumberTop;
		this.squareSize = squareSize;
		computeSquares(bound);
	}

	public Rectangle[] computeSquares(Rectangle bound) {
		lock.writeLock().lock();
		try {
			bounds = new Rectangle[ledNumberLeftRight * 2 + ledNumberTop - 2];
			currentLed = 0;

			// Square Size
			squareSizeLeftRight = bound.height / ledNumberLeftRight;
			squareSizeTop = bound.width / ledNumberTop;
			paddingLR = (bound.height - squareSizeLeftRight * ledNumberLeftRight) / 2;
			paddingTOP = (bound.width - squareSizeTop * ledNumberTop) / 2;

			// Left from bottom to top
			for (i = 0; i < ledNumberLeftRight - 1; i++) {
				bounds[currentLed++] = new Rectangle(0, bound.height - (i + 1) * squareSizeLeftRight - paddingLR, squareSize, squareSizeLeftRight);
			}

			// Top Left Corner
			bounds[currentLed++] = new Rectangle(0, 0, squareSizeTop, squareSizeLeftRight);

			// Top from left to right
			for (i = 0; i < ledNumberTop - 2; i++) {
				bounds[currentLed++] = new Rectangle(squareSizeTop * (i + 1) + paddingTOP, 0, squareSizeTop, squareSize);
			}

			// Top Right Corner
			bounds[currentLed++] = new Rectangle(bound.width - squareSizeTop, 0, squareSizeTop, squareSizeLeftRight);

			// Right from top to bottom
			for (i = 0; i < ledNumberLeftRight - 1; i++) {
				bounds[currentLed++] = new Rectangle(bound.width - squareSize, (i + 1) * squareSizeLeftRight + paddingLR, squareSize, squareSizeLeftRight);
			}
		} finally {
			lock.writeLock().unlock();
		}
		return bounds;
	}

	public Rectangle[] getSquares() {
		lock.readLock().lock();
		try {
			return bounds;
		} finally {
			lock.readLock().unlock();
		}
	}

}
