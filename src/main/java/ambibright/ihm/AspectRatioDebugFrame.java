package ambibright.ihm;

import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.awt.AWTUtilities;

public class AspectRatioDebugFrame extends JDialog {

	private final JLabel text;
	private final Timer timer;
	private TimerTask currentTask;

	public AspectRatioDebugFrame() {
		super();

		timer = new Timer();
		setUndecorated(true);
		setAlwaysOnTop(true);
		setFocusable(false);
		setBackground(Color.red);
		// a remplacer par setBackground(new Color(0, 0, 0, 0)); si on passe un
		// jour en jdk7+ only
		AWTUtilities.setWindowOpaque(this, false);
		text = new JLabel();
		text.setForeground(Color.magenta);
		text.setOpaque(false);
        text.setFont( new Font(AmbiFont.fontName, Font.BOLD, 48) );
		add(text);
		pack();
	}

	public void showAspectRatioChange(Rectangle newBounds) {
		if (null != currentTask) {
			currentTask.cancel();
		}

		setVisible(false);

		text.setText(newBounds.width + "x" + newBounds.height);
		setLocation(newBounds.x + 30, newBounds.y + 30);

		currentTask = new TimerTask() {
			@Override
			public void run() {
				setVisible(false);
				currentTask = null;
			}
		};
		timer.schedule(currentTask, 3000);

		pack();
		setVisible(true);
	}
}
