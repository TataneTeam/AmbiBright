package ambibright.ihm;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

public class BlackScreenManager {

	private final List<JDialog> blackFrames;

	public BlackScreenManager() {
		super();
		this.blackFrames = new ArrayList<JDialog>();
	}

	public void createBlackScreens(Rectangle bounds) {
		for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			if (!device.getDefaultConfiguration().getBounds().equals(bounds)) {
				blackFrames.add(new BlackScreenFrame(device.getDefaultConfiguration().getBounds()));
			}
		}
	}

	public void removeBlackScreens() {
		for (JDialog blackFrame : blackFrames) {
			if (blackFrame.isVisible()) {
				blackFrame.dispose();
			}
		}
		blackFrames.clear();
	}

	class BlackScreenFrame extends JDialog {

		public BlackScreenFrame(Rectangle bounds) {
			super();
			getContentPane().setBackground(Color.black);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						setAlwaysOnTop(!isAlwaysOnTop());
					}
				}
			});
			addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						dispose();
					}
				}
			});
			setBounds(bounds);
			setAlwaysOnTop(true);
			setUndecorated(true);
			setVisible(true);
		}

	}

}
