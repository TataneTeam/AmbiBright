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

	public CurrentBounds(Rectangle initialBounds) {
		this.bounds = initialBounds;
	}

	public void updateBounds(Rectangle bounds) {
		lock.writeLock().lock();
		try {
			Factory.get().getScreenSquares().computeSquares(bounds);
			this.bounds = bounds;
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

}
