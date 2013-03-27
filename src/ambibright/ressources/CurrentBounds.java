package ambibright.ressources;

import java.awt.Rectangle;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created with IntelliJ IDEA. User: Nico Date: 23/03/13 Time: 21:51 To change
 * this template use File | Settings | File Templates.
 */
public class CurrentBounds {

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Rectangle bounds;
	private int ledNumberLeftRight, ledNumberTop;
	private int squareSize;
	private int squareSizeLeftRight, squareSizeTop;
	private int i, currentLed;
	private int paddingLR, paddingTOP;
	private Rectangle[] zones;

	public CurrentBounds(Rectangle bounds, int ledNumberLeftRight, int ledNumberTop, int squareSize) {
		super();
		this.bounds = bounds;
		this.ledNumberLeftRight = ledNumberLeftRight;
		this.ledNumberTop = ledNumberTop;
		this.squareSize = squareSize;
		this.zones = new Rectangle[ledNumberLeftRight * 2 + ledNumberTop - 2];
		computeZone();
	}

	public void updateBounds(Rectangle bounds) {
		lock.writeLock().lock();
		try {
			this.bounds = bounds;
			computeZone();
		} finally {
			lock.writeLock().unlock();
		}
	}

	public Rectangle getBounds() {
		lock.readLock().lock();
		try {
			return bounds;
		} finally {
			lock.readLock().unlock();
		}
	}

	public Rectangle[] getZones() {
		lock.readLock().lock();
		try {
			return zones;
		} finally {
			lock.readLock().unlock();
		}
	}

	private void computeZone() {
		// Square Size
		squareSizeLeftRight = bounds.height / ledNumberLeftRight;
		squareSizeTop = bounds.width / ledNumberTop;
		paddingLR = (bounds.height - squareSizeLeftRight * ledNumberLeftRight) / 2;
		paddingTOP = (bounds.width - squareSizeTop * ledNumberTop) / 2;
		currentLed = 0;

		// Left from bottom to top
		for (i = 0; i < ledNumberLeftRight - 1; i++) {
			zones[currentLed++] = new Rectangle(0, bounds.height - (i + 1) * squareSizeLeftRight - paddingLR, squareSize, squareSizeLeftRight);
		}

		// Top Left Corner
		zones[currentLed++] = new Rectangle(0, 0, squareSizeTop, squareSizeLeftRight);

		// Top from left to right
		for (i = 0; i < ledNumberTop - 2; i++) {
			zones[currentLed++] = new Rectangle(squareSizeTop * (i + 1) + paddingTOP, 0, squareSizeTop, squareSize);
		}

		// Top Right Corner
		zones[currentLed++] = new Rectangle(bounds.width - squareSizeTop, 0, squareSizeTop, squareSizeLeftRight);

		// Right from top to bottom
		for (i = 0; i < ledNumberLeftRight - 1; i++) {
			zones[currentLed++] = new Rectangle(bounds.width - squareSize, (i + 1) * squareSizeLeftRight + paddingLR, squareSize, squareSizeLeftRight);
		}
	}

}
