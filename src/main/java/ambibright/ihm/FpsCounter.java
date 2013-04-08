package ambibright.ihm;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Simple counter for fps
 */
public class FpsCounter {

	private final Queue<Long> queue = new LinkedList<Long>();

	public int fps() {
		long currentMillis = System.currentTimeMillis();
		while (!queue.isEmpty() && (currentMillis - queue.peek()) > 1000) {
			queue.remove();
		}

		queue.add(currentMillis);

		return queue.size();
	}

}
