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

import ambi.ressources.Config;
import ambi.ressources.Config.Parameters;
import ambi.ressources.Factory;

public class ConfigFrame extends JFrame {

	private JTextField arduinoSerial, arduinoDataRate, appList, squareSize, threadSleep, ledNbTop, lebNbLeft, analysePitch;
	private JComboBox screenDevice;
	private JCheckBox checkApp;

	public ConfigFrame(AmbiFont ambiFont, final Config config) {
		super(Factory.appName + " - Configuration");
		setIconImage(Factory.get().getImageIcon());
		setLayout(new GridLayout(11, 2));

		arduinoSerial = new JTextField(Factory.get().getConfig().get(Parameters.CONFIG_ARDUINO_PORT));
		appList = new JTextField(Factory.get().getConfig().get(Parameters.CONFIG_PROCESS_LIST));
		arduinoDataRate = new JTextField(Factory.get().getConfig().get(Parameters.CONFIG_ARDUINO_DATA_RATE));
		squareSize = new JTextField(Factory.get().getConfig().get(Parameters.CONFIG_SQUARE_SIZE));
		threadSleep = new JTextField(Factory.get().getConfig().get(Parameters.CONFIG_FPS));
		ledNbTop = new JTextField(Factory.get().getConfig().get(Parameters.CONFIG_LED_NB_TOP));
		lebNbLeft = new JTextField(Factory.get().getConfig().get(Parameters.CONFIG_LED_NB_LEFT));
		analysePitch = new JTextField(Factory.get().getConfig().get(Parameters.CONFIG_ANALYSE_PITCH));

		screenDevice = new JComboBox();
		screenDevice.setBorder(null);
		for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			screenDevice.addItem(device.getIDstring() + " - " + device.getDefaultConfiguration().getBounds().width + "x" + device.getDefaultConfiguration().getBounds().height);
		}
		screenDevice.setSelectedIndex(Integer.valueOf(Factory.get().getConfig().get(Parameters.CONFIG_SCREEN_DEVICE)));

		checkApp = new JCheckBox();
		checkApp.setSelected(Factory.get().isCheckProcess());

		JButton save = new JButton("Save");

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                config.put(Parameters.CONFIG_LED_NB_LEFT, lebNbLeft.getText());
                config.put(Parameters.CONFIG_LED_NB_TOP, ledNbTop.getText());
                config.put(Parameters.CONFIG_ARDUINO_PORT, arduinoSerial.getText());
                config.put(Parameters.CONFIG_PROCESS_LIST, appList.getText());
                config.put(Parameters.CONFIG_SCREEN_DEVICE, screenDevice.getSelectedIndex() + "");
                config.put(Parameters.CONFIG_CHECK_PROCESS, checkApp.isSelected() + "");
                config.put(Parameters.CONFIG_ARDUINO_DATA_RATE, arduinoDataRate.getText());
                config.put(Parameters.CONFIG_SQUARE_SIZE, squareSize.getText());
                config.put(Parameters.CONFIG_FPS, threadSleep.getText());
                config.put(Parameters.CONFIG_ANALYSE_PITCH, analysePitch.getText());
                config.save();
                Factory.get().getManager().restart();
				dispose();
			}
		});

		add(ambiFont.setFontBold( new JLabel( " Screen" ) ));
		add(ambiFont.setFont( screenDevice ));

		add(ambiFont.setFontBold( new JLabel( " Top LED number" ) ));
		add(ambiFont.setFont( ledNbTop ));

		add(ambiFont.setFontBold( new JLabel( " Left/Right LED number" ) ));
		add(ambiFont.setFont( lebNbLeft ));

		add(ambiFont.setFontBold( new JLabel( " Arduino Serial Port" ) ));
		add(ambiFont.setFont( arduinoSerial ));

		add( ambiFont.setFontBold( new JLabel( " Arduino Data Rate" ) ) );
		add(ambiFont.setFont( arduinoDataRate ));

		add(ambiFont.setFontBold( new JLabel( " Square Size" ) ));
		add(ambiFont.setFont( squareSize ));

		add(ambiFont.setFontBold( new JLabel( " Check for Process" ) ));
		add(ambiFont.setFont( checkApp ));

		add(ambiFont.setFontBold( new JLabel( " Check for Process list" ) ));
		add(ambiFont.setFont( appList ));

		add(ambiFont.setFontBold( new JLabel( " FPS" ) ));
		add( ambiFont.setFont( threadSleep ) );

		add(ambiFont.setFontBold( new JLabel( " Capture analyse pitch" ) ));
		add(ambiFont.setFont( analysePitch ));

		add(ambiFont.setFontBold( new JLabel( "" ) ));
		add(ambiFont.setFont( save ));

		pack();
		setAlwaysOnTop(true);
		setResizable(false);
		setLocationRelativeTo(getParent());
		setVisible(true);
	}

}
