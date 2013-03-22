package ambi.ihm;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ambi.ressources.Factory;
import ambi.ressources.Config.Parameters;

public class ConfigFrame extends JFrame {

	private JTextField arduinoSerial, appList;
	private JComboBox<String> screenDevice;
	private JComboBox<Integer> ledNbTop, lebNbLeft;

	public ConfigFrame() {
		super(Factory.appName + " - Configuration" );
		setIconImage(Factory.getImageIcon());
		setLayout(new GridLayout(7,2));

		arduinoSerial = new JTextField(Factory.getConfig().get(Parameters.CONFIG_ARDUINO_PORT));
		appList = new JTextField(Factory.getConfig().get(Parameters.CONFIG_APP_LIST));

		ledNbTop = new JComboBox<Integer>();
		lebNbLeft = new JComboBox<Integer>();
		for (int i = 0; i < 100; i++) {
			ledNbTop.addItem(i);
			lebNbLeft.addItem(i);
		}
		ledNbTop.setSelectedIndex(Integer.valueOf(Factory.getConfig().get(Parameters.CONFIG_LED_NB_TOP)));
		lebNbLeft.setSelectedIndex(Integer.valueOf(Factory.getConfig().get(Parameters.CONFIG_LED_NB_LEFT)));

		screenDevice = new JComboBox<String>();
		for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			screenDevice.addItem(device.getIDstring() + " - " + device.getDefaultConfiguration().getBounds().width + "x" + device.getDefaultConfiguration().getBounds().height);
		}
		screenDevice.setSelectedIndex(Integer.valueOf(Factory.getConfig().get(Parameters.CONFIG_SCREEN_DEVICE)));
		
		JButton save = new JButton("Save");
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Factory.getConfig().put(Parameters.CONFIG_LED_NB_LEFT,lebNbLeft.getSelectedIndex() + "");
				Factory.getConfig().put(Parameters.CONFIG_LED_NB_TOP,ledNbTop.getSelectedIndex() + "");
				Factory.getConfig().put(Parameters.CONFIG_ARDUINO_PORT,arduinoSerial.getText());
				Factory.getConfig().put(Parameters.CONFIG_APP_LIST,appList.getText());
				Factory.getConfig().put(Parameters.CONFIG_SCREEN_DEVICE,screenDevice.getSelectedIndex() + "");
				Factory.getConfig().save();
				dispose();
			}
		});

		add(new JLabel(" Screen"));
		add(screenDevice);
		
		add(new JLabel(" Top LED number"));
		add(ledNbTop);
		
		add(new JLabel(" Left/Right LED number"));
		add(lebNbLeft);
		
		add(new JLabel(" Arduino Serial Port"));
		add(arduinoSerial);
		
		add(new JLabel(" App list"));
		add(appList);
		
		add(new JLabel(" Screen"));
		add(screenDevice);
		
		add(new JLabel(""));
		add(save);

		pack();
		setAlwaysOnTop(true);
		setResizable(false);
		setLocationRelativeTo(getParent());
		setVisible(true);
	}

}
