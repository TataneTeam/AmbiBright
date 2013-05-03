package ambibright.ihm;

import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Rectangle;

import ambibright.engine.ColorsChangeObserver;
import ambibright.engine.capture.Image;
import ambibright.ressources.Factory;
import com.sun.awt.AWTUtilities;

public class SimpleFPSFrame extends JDialog implements ColorsChangeObserver {

	private final FpsCounter fpsCounter;
	private JLabel text;
	private Rectangle bounds;

	public SimpleFPSFrame() {
		super();
		this.fpsCounter = new FpsCounter();

		setUndecorated(true);
		setAlwaysOnTop(true);
		setFocusable(false);
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
		bounds = Factory.get().getBounds();
		setLocation(bounds.x + (bounds.width - getPreferredSize().width) / 2, bounds.y + bounds.height - getPreferredSize().height);
	}
}
