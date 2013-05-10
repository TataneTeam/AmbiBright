package ambibright.ressources;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ambibright.config.Config;

/**
 * Class that handles the changes of screen device, aspect ratio, number of leds
 * and generate a list of zone to analyse.
 */
public final class CurrentBounds {

	private static final Logger logger = LoggerFactory.getLogger(CurrentBounds.class);

	public static Rectangle getScreenBounds(int screenDevice) {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screenDevice].getDefaultConfiguration().getBounds();
	}

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private int screenDevice;
	private Rectangle fullscreenBounds;
	private Rectangle bounds;
	private int ledNumberLeftRight, ledNumberTop;
	private int squareSize;
	private Rectangle[] zones;

	public CurrentBounds(Config config) {
		super();
		this.screenDevice = config.getScreenDevice();
		this.fullscreenBounds = getScreenBounds(screenDevice);
		this.bounds = fullscreenBounds;
		this.ledNumberLeftRight = config.getNbLedLeft();
		this.ledNumberTop = config.getNbLedTop();
		this.squareSize = config.getSquareSize();
		this.zones = new Rectangle[ledNumberLeftRight * 2 + ledNumberTop - 2];
		computeZone();

		config.addPropertyChangeListener(Config.CONFIG_SCREEN_DEVICE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				lock.writeLock().lock();
				try {
					CurrentBounds.this.screenDevice = (Integer) evt.getNewValue();
					CurrentBounds.this.fullscreenBounds = getScreenBounds(screenDevice);
					CurrentBounds.this.bounds = fullscreenBounds;
					computeZone();
				} finally {
					lock.writeLock().unlock();
				}
			}
		});

		config.addPropertyChangeListener(Config.CONFIG_LED_NB_LEFT, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				lock.writeLock().lock();
				try {
					CurrentBounds.this.ledNumberLeftRight = (Integer) evt.getNewValue();
					CurrentBounds.this.zones = new Rectangle[ledNumberLeftRight * 2 + ledNumberTop - 2];
					computeZone();
				} finally {
					lock.writeLock().unlock();
				}
			}
		});

		config.addPropertyChangeListener(Config.CONFIG_LED_NB_TOP, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				lock.writeLock().lock();
				try {
					CurrentBounds.this.ledNumberTop = (Integer) evt.getNewValue();
					CurrentBounds.this.zones = new Rectangle[ledNumberLeftRight * 2 + ledNumberTop - 2];
					computeZone();
				} finally {
					lock.writeLock().unlock();
				}
			}
		});

		config.addPropertyChangeListener(Config.CONFIG_SQUARE_SIZE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				lock.writeLock().lock();
				try {
					CurrentBounds.this.squareSize = (Integer) evt.getNewValue();
					computeZone();
				} finally {
					lock.writeLock().unlock();
				}
			}
		});
	}

	/**
	 * Updates the capture bounds.
	 *
	 * @param bounds
	 */
	public void updateBounds(Rectangle bounds) {
		lock.writeLock().lock();
		try {
			this.bounds = bounds;
			computeZone();
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * Acquire a read lock. Call
	 * {@link ambibright.ressources.CurrentBounds#readUnlock()} when you are
	 * done with the lock.
	 */
	public void readLock() {
		lock.readLock().lock();
	}

	/**
	 * Release a read lock. To call after a
	 * {@link ambibright.ressources.CurrentBounds#readLock()}.
	 */
	public void readUnlock() {
		lock.readLock().unlock();
	}

	public Rectangle getBounds() {
		readLock();
		try {
			return getBoundsNoLock();
		} finally {
			readUnlock();
		}
	}

	public Rectangle getBoundsNoLock() {
		return bounds;
	}

	public Rectangle[] getZones() {
		readLock();
		try {
			return getZonesNoLock();
		} finally {
			readUnlock();
		}
	}

	public Rectangle[] getZonesNoLock() {
		return zones;
	}

	public Rectangle getFullscreenBounds() {
		readLock();
		try {
			return getFullscreenBoundsNoLock();
		} finally {
			readUnlock();
		}
	}

	public Rectangle getFullscreenBoundsNoLock() {
		return fullscreenBounds;
	}

	public int getScreenDevice() {
		readLock();
		try {
			return getScreenDeviceNoLock();
		} finally {
			readUnlock();
		}
	}

	public int getScreenDeviceNoLock() {
		return screenDevice;
	}

	private void computeZone() {
		logger.info("Computing zones for bounds {}", bounds);
		// Square Size
		int squareSizeLeftRight = bounds.height / ledNumberLeftRight;
		int squareSizeTop = bounds.width / ledNumberTop;
		int paddingLR = (bounds.height - squareSizeLeftRight * ledNumberLeftRight) / 2;
		int paddingTOP = (bounds.width - squareSizeTop * ledNumberTop) / 2;
		int currentLed = 0;

		// Left from bottom to top
		for (int i = 0; i < ledNumberLeftRight - 1; i++) {
			zones[currentLed++] = new Rectangle(0, bounds.height - (i + 1) * squareSizeLeftRight - paddingLR, squareSize, squareSizeLeftRight);
		}

		// Top Left Corner
		zones[currentLed++] = new Rectangle(0, 0, squareSizeTop, squareSizeLeftRight);

		// Top from left to right
		for (int i = 0; i < ledNumberTop - 2; i++) {
			zones[currentLed++] = new Rectangle(squareSizeTop * (i + 1) + paddingTOP, 0, squareSizeTop, squareSize);
		}

		// Top Right Corner
		zones[currentLed++] = new Rectangle(bounds.width - squareSizeTop, 0, squareSizeTop, squareSizeLeftRight);

		// Right from top to bottom
		for (int i = 0; i < ledNumberLeftRight - 1; i++) {
			zones[currentLed++] = new Rectangle(bounds.width - squareSize, (i + 1) * squareSizeLeftRight + paddingLR, squareSize, squareSizeLeftRight);
		}
	}

}
