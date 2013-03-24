package ambi.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import ambi.ressources.Config;
import ambi.ressources.Config.Parameters;
import ambi.ressources.Factory;

public class Tray extends TrayIcon {

	public Tray(Image icon, final AmbiFont ambiFont, final Config config) {
		super(icon);
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
				new ConfigFrame(ambiFont, config);
			}
		});
		getPopupMenu().add(ambiFont.setFont( configItem ));
		MenuItem checkApp = new MenuItem(" Check for Process");
		checkApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                config.put( Parameters.CONFIG_CHECK_PROCESS, !Factory.get().isCheckProcess() + "" );
                config.save();
				showInfo("Check for process: " + Factory.get().isCheckProcess());
			}
		});
		getPopupMenu().add(ambiFont.setFont( checkApp ));
		getPopupMenu().addSeparator();

		MenuItem colorFrame = new MenuItem(" Color Frame");
		colorFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ColorFrame(Factory.get().getBounds(), ambiFont);
			}
		});
		getPopupMenu().add(ambiFont.setFont( colorFrame ));
		MenuItem ambiFrame = new MenuItem(" Monitoring Frame");
		ambiFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Factory.get().getAmbiFrame().setVisible(true);
				Factory.get().getAmbiFrame().setState(JFrame.NORMAL);
			}
		});
		getPopupMenu().add(ambiFont.setFont( ambiFrame ));
		getPopupMenu().addSeparator();

		MenuItem stop = new MenuItem(" Stop");
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                Factory.get().getManager().stop();
			}
		});
		getPopupMenu().add(ambiFont.setFont( stop ));
		MenuItem restart = new MenuItem(" Restart");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                Factory.get().getManager().restart();
			}
		});
		getPopupMenu().add(ambiFont.setFont( restart ));

		getPopupMenu().addSeparator();
		MenuItem exit = new MenuItem(" Close");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                Factory.get().getManager().stop();
				System.exit(0);
			}
		});
		getPopupMenu().add(ambiFont.setFont( exit ));
	}

	public void showInfo(String text) {
		displayMessage( Factory.appName, text, TrayIcon.MessageType.INFO);
	}

}
