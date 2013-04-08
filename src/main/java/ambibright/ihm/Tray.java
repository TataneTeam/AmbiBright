package ambibright.ihm;

import java.awt.CheckboxMenuItem;
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
import java.util.List;

import javax.swing.JFrame;

import ambibright.engine.color.ColorAlgorithm;
import ambibright.ressources.Config;
import ambibright.ressources.Config.Parameters;
import ambibright.ressources.Factory;

public class Tray extends TrayIcon {

	private final CheckboxMenuItem showFPSFrame, checkProcess, blackScreens;

	public Tray(Image icon, final AmbiFont ambiFont, final Config config, final List<ColorAlgorithm> colorAlgorithmList) {
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
				new ConfigFrame(ambiFont, config, colorAlgorithmList);
			}
		});
		getPopupMenu().add(ambiFont.setFont(configItem));

		Menu configMenu = new Menu(" Quick Configuration");
		getPopupMenu().add(ambiFont.setFont(configMenu));
		blackScreens = new CheckboxMenuItem(" Black other screens");
		blackScreens.setState("true".equals(config.get(Parameters.CONFIG_BLACK_OTHER_SCREENS)));
		blackScreens.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				boolean newValue = !Factory.get().isBlackOtherScreens();
				config.put(Parameters.CONFIG_BLACK_OTHER_SCREENS, newValue + "");
				config.save();
				showInfo("Black other screens: " + (newValue ? "ON" : "OFF"));
			}
		});
		configMenu.add(ambiFont.setFont(blackScreens));
		checkProcess = new CheckboxMenuItem(" Check for Process");
		checkProcess.setState("true".equals(config.get(Parameters.CONFIG_CHECK_PROCESS)));
		checkProcess.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				boolean newValue = !Factory.get().isCheckProcess();
				config.put(Parameters.CONFIG_CHECK_PROCESS, newValue + "");
				config.save();
				showInfo("Check for process: " + (newValue ? "ON" : "OFF"));
			}
		});
		configMenu.add(ambiFont.setFont(checkProcess));
		showFPSFrame = new CheckboxMenuItem(" Show FPS Frame");
		showFPSFrame.setState("true".equals(config.get(Parameters.CONFIG_SHOW_FPS_FRAME)));
		showFPSFrame.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				boolean newValue = !Factory.get().isShowFPSFrame();
				config.put(Parameters.CONFIG_SHOW_FPS_FRAME, newValue + "");
				config.save();
				Factory.get().getSimpleFPSFrame().setVisible(newValue);
				showInfo("Show FPS Frame: " + (newValue ? "ON" : "OFF"));
			}
		});
		configMenu.add(ambiFont.setFont(showFPSFrame));

		MenuItem colorFrame = new MenuItem(" Show Color Frame");
		colorFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Factory.get().getNewColorFrame();
			}
		});
		getPopupMenu().add(ambiFont.setFont(colorFrame));
		MenuItem ambiFrame = new MenuItem(" Show Monitoring Frame");
		ambiFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Factory.get().getAmbiFrame().setVisible(true);
				Factory.get().getAmbiFrame().setState(JFrame.NORMAL);
			}
		});
		getPopupMenu().add(ambiFont.setFont(ambiFrame));
		getPopupMenu().addSeparator();

		MenuItem stop = new MenuItem(" Stop");
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Factory.get().getManager().stop();
			}
		});
		getPopupMenu().add(ambiFont.setFont(stop));
		MenuItem restart = new MenuItem(" Restart");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Factory.get().getManager().restart();
			}
		});
		getPopupMenu().add(ambiFont.setFont(restart));

		getPopupMenu().addSeparator();
		MenuItem exit = new MenuItem(" Close");
		exit.addActionListener(new ActionListener() {
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

	public void updateCheckBox() {
		showFPSFrame.setState(Factory.get().isShowFPSFrame());
		checkProcess.setState(Factory.get().isCheckProcess());
		blackScreens.setState(Factory.get().isBlackOtherScreens());
	}

}
