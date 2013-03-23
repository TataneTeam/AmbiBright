package ambi.ihm;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ambi.ressources.Config.Parameters;
import ambi.ressources.Factory;

public class ConfigFrame extends JFrame {

	private JTextField arduinoSerial, arduinoDataRate, appList, squareSize;
	private JComboBox screenDevice;
	private JComboBox ledNbTop, lebNbLeft;
	private JCheckBox checkApp;

	public ConfigFrame() {
		super(Factory.appName + " - Configuration");
		setIconImage(Factory.getImageIcon());
		setLayout(new GridLayout(9, 2));

		arduinoSerial = new JTextField(Factory.getConfig().get(Parameters.CONFIG_ARDUINO_PORT));
		appList = new JTextField(Factory.getConfig().get(Parameters.CONFIG_PROCESS_LIST));
		arduinoDataRate = new JTextField(Factory.getConfig().get(Parameters.CONFIG_ARDUINO_DATA_RATE));
		squareSize = new JTextField(Factory.getConfig().get(Parameters.CONFIG_SQUARE_SIZE));

		ledNbTop = new JComboBox();
		lebNbLeft = new JComboBox();
		lebNbLeft.setBorder(null);
		ledNbTop.setBorder(null);
		for (int i = 0; i < 100; i++) {
			ledNbTop.addItem(i);
			lebNbLeft.addItem(i);
		}
		ledNbTop.setSelectedIndex(Integer.valueOf(Factory.getConfig().get(Parameters.CONFIG_LED_NB_TOP)));
		lebNbLeft.setSelectedIndex(Integer.valueOf(Factory.getConfig().get(Parameters.CONFIG_LED_NB_LEFT)));

		screenDevice = new JComboBox();
		screenDevice.setBorder(null);
		for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			screenDevice.addItem(device.getIDstring() + " - " + device.getDefaultConfiguration().getBounds().width + "x" + device.getDefaultConfiguration().getBounds().height);
		}
		screenDevice.setSelectedIndex(Integer.valueOf(Factory.getConfig().get(Parameters.CONFIG_SCREEN_DEVICE)));

		checkApp = new JCheckBox();
		checkApp.setSelected(Factory.isCheckProcess());

		JButton save = new JButton("Save");

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Factory.getConfig().put(Parameters.CONFIG_LED_NB_LEFT, lebNbLeft.getSelectedIndex() + "");
				Factory.getConfig().put(Parameters.CONFIG_LED_NB_TOP, ledNbTop.getSelectedIndex() + "");
				Factory.getConfig().put(Parameters.CONFIG_ARDUINO_PORT, arduinoSerial.getText());
				Factory.getConfig().put(Parameters.CONFIG_PROCESS_LIST, appList.getText());
				Factory.getConfig().put(Parameters.CONFIG_SCREEN_DEVICE, screenDevice.getSelectedIndex() + "");
				Factory.getConfig().put(Parameters.CONFIG_CHECK_PROCESS, checkApp.isSelected() + "");
				Factory.getConfig().put(Parameters.CONFIG_ARDUINO_DATA_RATE, arduinoDataRate.getText());
				Factory.getConfig().put(Parameters.CONFIG_SQUARE_SIZE, squareSize.getText());
				Factory.getConfig().save();
				dispose();
			}
		});

		add(Factory.setFontBold(new JLabel(" Screen")));
		add(Factory.setFont(screenDevice));

		add(Factory.setFontBold(new JLabel(" Top LED number")));
		add(Factory.setFont(ledNbTop));

		add(Factory.setFontBold(new JLabel(" Left/Right LED number")));
		add(Factory.setFont(lebNbLeft));

		add(Factory.setFontBold(new JLabel(" Arduino Serial Port")));
		add(Factory.setFont(arduinoSerial));

		add(Factory.setFontBold(new JLabel(" Arduino Data Rate")));
		add(Factory.setFont(arduinoDataRate));
		
		add(Factory.setFontBold(new JLabel(" Square Size")));
		add(Factory.setFont(squareSize));

		add(Factory.setFontBold(new JLabel(" Check for Process")));
		add(Factory.setFont(checkApp));

		add(Factory.setFontBold(new JLabel(" Check for Process list")));
		add(Factory.setFont(appList));

		add(Factory.setFontBold(new JLabel("")));
		add(Factory.setFont(save));

		pack();
		setAlwaysOnTop(true);
		setResizable(false);
		setLocationRelativeTo(getParent());
		setVisible(true);
	}

}
