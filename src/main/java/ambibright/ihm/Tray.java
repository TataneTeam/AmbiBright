package ambibright.ihm;

import java.awt.CheckboxMenuItem;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import ambibright.config.Config;
import ambibright.ressources.CheckUpdate;
import ambibright.ressources.Factory;

public class Tray extends TrayIcon {

	private final Config config;
	private final CheckboxMenuItem showFPSFrame, checkProcess, blackScreens;
	private final CheckUpdate checkUpdate;

	public Tray(Image icon, final AmbiFont ambiFont, final Config pConfig) {
		super(icon);
		this.config = pConfig;
		this.checkUpdate = new CheckUpdate(config.getUpdateUrl());
		setToolTip(Factory.appName);
		setImageAutoSize(true);
		setPopupMenu(new PopupMenu());
		try {
			SystemTray.getSystemTray().add(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final MenuItem checkUpdateItem = new MenuItem(" Check for update");
		checkUpdateItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkUpdateItem.setEnabled(false);
				checkUpdate.setUrl(config.getUpdateUrl());
				checkUpdate.manage();
				checkUpdateItem.setEnabled(true);
			}
		});
		getPopupMenu().add(ambiFont.setFont(checkUpdateItem));
		getPopupMenu().addSeparator();

		MenuItem configItem = new MenuItem(" Configuration");
		configItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ConfigFrame(ambiFont, config);
			}
		});
		getPopupMenu().add(ambiFont.setFont(configItem));

		Menu configMenu = new Menu(" Quick Configuration");
		getPopupMenu().add(ambiFont.setFont(configMenu));
		blackScreens = new CheckboxMenuItem(" Black other screens");
		blackScreens.setState(config.isBlackOtherScreens());
		blackScreens.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean newValue = !config.isBlackOtherScreens();
				config.setBlackOtherScreens(newValue);
				config.save();
				showInfo("Black other screens: " + (newValue ? "ON" : "OFF"));
			}
		});
		config.addPropertyChangeListener(Config.CONFIG_BLACK_OTHER_SCREENS, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				blackScreens.setState((Boolean) evt.getNewValue());
			}
		});
		configMenu.add(ambiFont.setFont(blackScreens));

		checkProcess = new CheckboxMenuItem(" Check for Process");
		checkProcess.setState(config.isCheckProcess());
		checkProcess.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean newValue = !config.isCheckProcess();
				config.setCheckProcess(newValue);
				config.save();
				showInfo("Check for process: " + (newValue ? "ON" : "OFF"));
			}
		});
		config.addPropertyChangeListener(Config.CONFIG_CHECK_PROCESS, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				checkProcess.setState((Boolean) evt.getNewValue());
			}
		});
		configMenu.add(ambiFont.setFont(checkProcess));

		showFPSFrame = new CheckboxMenuItem(" Show FPS Frame");
		showFPSFrame.setState(config.isShowFpsFrame());
		showFPSFrame.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean newValue = !config.isShowFpsFrame();
				config.setShowFpsFrame(newValue);
				config.save();
				Factory.get().getSimpleFPSFrame().setVisible(newValue);
				showInfo("Show FPS Frame: " + (newValue ? "ON" : "OFF"));
			}
		});
		config.addPropertyChangeListener(Config.CONFIG_SHOW_FPS_FRAME, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				showFPSFrame.setState((Boolean) evt.getNewValue());
			}
		});
		configMenu.add(ambiFont.setFont(showFPSFrame));

		MenuItem colorFrame = new MenuItem(" Show Color Frame");
		colorFrame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Factory.get().getNewColorFrame();
			}
		});
		getPopupMenu().add(ambiFont.setFont(colorFrame));
		MenuItem ambiFrame = new MenuItem(" Show Monitoring Frame");
		ambiFrame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Factory.get().getAmbiFrame().setVisible(true);
				Factory.get().getAmbiFrame().setState(Frame.NORMAL);
			}
		});
		getPopupMenu().add(ambiFont.setFont(ambiFrame));
		getPopupMenu().addSeparator();

		MenuItem stop = new MenuItem(" Stop");
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Factory.get().getManager().stop();
			}
		});
		getPopupMenu().add(ambiFont.setFont(stop));
		MenuItem restart = new MenuItem(" Restart");
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Factory.get().getManager().restart();
			}
		});
		getPopupMenu().add(ambiFont.setFont(restart));

		getPopupMenu().addSeparator();
		MenuItem exit = new MenuItem(" Close");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Factory.get().getManager().stop();
				System.exit(0);
			}
		});
		getPopupMenu().add(ambiFont.setFont(exit));
	}

	public void showInfo(String text) {
		displayMessage(Factory.appName, text, TrayIcon.MessageType.INFO);
	}

}
