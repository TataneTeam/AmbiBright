package ambibright.ihm;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JLabel;

import ambibright.ressources.Factory;

import com.sun.awt.AWTUtilities;

public class SimpleFPSFrame extends JDialog {

	private JLabel text;
	private Rectangle bounds;

	public SimpleFPSFrame() {
		super();
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

	public void setValue(String value) {
		if (isVisible()) {
			text.setText(value);
			pack();
			bounds = Factory.get().getBounds();
			setLocation(bounds.x + (bounds.width - getPreferredSize().width) / 2, bounds.y + bounds.height - getPreferredSize().height);
		}
	}

	public boolean display() {
		setVisible(!isVisible());
		return isVisible();
	}

}
