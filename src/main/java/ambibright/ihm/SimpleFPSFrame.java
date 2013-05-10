package ambibright.ihm;

import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Rectangle;

import ambibright.engine.ColorsChangeObserver;
import ambibright.engine.capture.Image;
import ambibright.ressources.CurrentBounds;
import ambibright.ressources.Factory;
import com.sun.awt.AWTUtilities;

public class SimpleFPSFrame extends JDialog implements ColorsChangeObserver {

	private final CurrentBounds currentBounds;
	private final FpsCounter fpsCounter;
	private final JLabel text;

	public SimpleFPSFrame(CurrentBounds currentBounds) {
		super();
		this.currentBounds = currentBounds;
		this.fpsCounter = new FpsCounter();

		setUndecorated(true);
		setAlwaysOnTop(true);
		setFocusable(false);
		// a remplacer par setBackground(new Color(0, 0, 0, 0)); si on passe un
		// jour en jdk7+ only
		AWTUtilities.setWindowOpaque(this, false);
		text = new JLabel();
		text.setForeground(Color.magenta);
		text.setOpaque(false);
		text.setFont(AmbiFont.fontMonitoringImage);
		add(text);
		pack();
	}

	public boolean display() {
		setVisible(!isVisible());
		return isVisible();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			Factory.get().getManager().addObserver(SimpleFPSFrame.this);
		} else {
			Factory.get().getManager().removeObserver(SimpleFPSFrame.this);
		}
	}

	@Override
	public void onColorsChange(Image image, byte[] colors) {
		int fps = fpsCounter.fps();
		text.setText(fps + " fps");
		pack();
		Rectangle bounds = currentBounds.getFullscreenBounds();
		setLocation(bounds.x + (bounds.width - getPreferredSize().width) / 2, bounds.y + bounds.height - getPreferredSize().height);
	}
}
