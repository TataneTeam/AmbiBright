package ambibright;

import javax.swing.UIManager;

import ambibright.ressources.Factory;

public class Launcher {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		Factory.get().getManager().start();
	}

}
