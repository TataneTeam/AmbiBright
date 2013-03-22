package ambi.ihm;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import ambi.engine.AmbiEngineManagement;
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
		getPopupMenu().add(Factory.setFont(configItem));
		MenuItem ambiFrame = new MenuItem(" Show AmbiFrame");
		ambiFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AmbiEngineManagement.getAmbiFrame().setVisible(true);
				AmbiEngineManagement.getAmbiFrame().setState(JFrame.NORMAL);
			}
		});
		getPopupMenu().add(Factory.setFont(ambiFrame));
		MenuItem stop = new MenuItem(" Stop");
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AmbiEngineManagement.stop();
			}
		});
		getPopupMenu().add(Factory.setFont(stop));
		MenuItem restart = new MenuItem(" Restart");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AmbiEngineManagement.restart();
			}
		});
		getPopupMenu().add(Factory.setFont(restart));
		getPopupMenu().addSeparator();
		MenuItem exit = new MenuItem(" Close");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AmbiEngineManagement.stop();
				System.exit(0);
			}
		});
		getPopupMenu().add(Factory.setFont(exit));
	}

	public static void main(String[] args) {
		new Tray();
	}
}
