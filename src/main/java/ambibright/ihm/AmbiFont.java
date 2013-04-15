package ambibright.ihm;

import java.awt.Component;
import java.awt.Font;
import java.awt.MenuItem;

/**
 * Helper class to manage the font used in application
 */
public class AmbiFont {
	public static final String fontName = "Calibri";
	public static final Font font = new Font(fontName, Font.PLAIN, 11);
	public static final Font fontBold = new Font(fontName, Font.BOLD, 11);
	public static final Font fontMonitoringImage = new Font(fontName, Font.BOLD, 24);

	public Component setFont(Component component) {
		component.setFont(font);
		return component;
	}

	public Component setFontBold(Component component) {
		component.setFont(fontBold);
		return component;
	}

	public MenuItem setFont(MenuItem component) {
		component.setFont(font);
		return component;
	}
}
