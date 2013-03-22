package ambi.ihm;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import ambi.ressources.Factory;

public class Tray extends TrayIcon {

	public Tray() {
		super(Factory.getImageIcon());
		setToolTip(Factory.appName);
		setImageAutoSize(true);
		setPopupMenu(new PopupMenu());
		try {
			SystemTray.getSystemTray().add(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		MenuItem configItem = new MenuItem(" Configuration");
		configItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ConfigFrame();
			}
		});
		getPopupMenu().add(configItem);
		MenuItem ambiFrame = new MenuItem(" Show AmbiFrame");
		ambiFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Factory.getAmbiFrame().setVisible(true);
				Factory.getAmbiFrame().setState(JFrame.NORMAL);
			}
		});
		getPopupMenu().add(ambiFrame);
		MenuItem exit = new MenuItem(" Close");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Factory.getArduinoSender().close();
				System.exit(0);
			}
		});
		getPopupMenu().add(exit);
	}

	public static void main(String[] args) {
		new Tray();
	}
}
