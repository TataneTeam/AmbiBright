package ambi;

import javax.swing.UIManager;

import ambi.engine.AmbiEngineManager;
import ambi.ressources.Factory;

public class Launcher {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		Factory.getTray();
		AmbiEngineManager.start();
	}

}
