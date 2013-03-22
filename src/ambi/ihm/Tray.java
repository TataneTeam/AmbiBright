package ambi.ihm;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import ambi.engine.AmbiEngineManager;
import ambi.ressources.Config.Parameters;
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
		MenuItem checkApp = new MenuItem(" Check for Process");
		checkApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Factory.getConfig().put(Parameters.CONFIG_CHECK_PROCESS, !Factory.isCheckProcess() + "");
				Factory.getConfig().save();
				showInfo("Check for process: " + Factory.isCheckProcess());
			}
		});
		getPopupMenu().add(Factory.setFont(checkApp));
		getPopupMenu().addSeparator();
		
		MenuItem colorFrame = new MenuItem(" Color Frame");
		colorFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ColorFrame(Factory.getBounds());
			}
		});
		getPopupMenu().add(Factory.setFont(colorFrame));
		MenuItem ambiFrame = new MenuItem(" Monitoring Frame");
		ambiFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AmbiEngineManager.getAmbiFrame().setVisible(true);
				AmbiEngineManager.getAmbiFrame().setState(JFrame.NORMAL);
			}
		});
		getPopupMenu().add(Factory.setFont(ambiFrame));
		getPopupMenu().addSeparator();

		MenuItem stop = new MenuItem(" Stop");
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AmbiEngineManager.stop();
			}
		});
		getPopupMenu().add(Factory.setFont(stop));
		MenuItem restart = new MenuItem(" Restart");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AmbiEngineManager.restart();
			}
		});
		getPopupMenu().add(Factory.setFont(restart));

		getPopupMenu().addSeparator();
		MenuItem exit = new MenuItem(" Close");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AmbiEngineManager.stop();
				System.exit(0);
			}
		});
		getPopupMenu().add(Factory.setFont(exit));
	}

	public void showInfo(String text) {
		displayMessage(Factory.appName, text, TrayIcon.MessageType.INFO);
	}

}
