package ambibright.ihm;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ambibright.ressources.Factory;
import ambibright.engine.ArduinoSender;
import ambibright.config.Config;

public class ConfigFrame extends JFrame {

	private final Config config;
	private final ComponentFactory factory;
	private Config originalConfig;
	private JButton findPortBtn;

	public ConfigFrame(AmbiFont ambiFont, Config pConfig) {
		super(Factory.appName + " - Configuration");
		this.originalConfig = pConfig.cloneConfig();
		this.config = pConfig;
		setIconImage(Factory.get().getImageIcon());

		Collection<Field> fields = config.getFieldsByGroup(Config.GROUP_CONFIG);

		setLayout(new GridLayout(fields.size() + 2, 2));

		factory = new ComponentFactory(this, ambiFont, config);
		factory.createComponents(fields);

		findPortBtn = new JButton("Try to find the port");
		findPortBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findPortBtn.setEnabled(false);
				String port = ArduinoSender.getArduinoPort(config.getArduinoDataRate(), ArduinoSender.defaultTestString);
				config.setArduinoSerialPort(port);
				((JTextField) factory.getComponent(Config.CONFIG_ARDUINO_PORT)).setText(port);
				findPortBtn.setEnabled(true);
			}
		});
		add(ambiFont.setFontBold(new JLabel("")));
		add(ambiFont.setFont(findPortBtn));

		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				config.save();
				originalConfig = null;
				Factory.get().getTray().updateCheckBox();
				Factory.get().getManager().restart();
				dispose();
			}
		});
		add(ambiFont.setFont(save));

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		add(ambiFont.setFont(cancel));

		pack();
		setResizable(false);
		setLocationRelativeTo(getParent());
		setVisible(true);
	}

	@Override
	public void hide() {
        factory.clearPropertyChangeListeners();
		if (null != originalConfig) {
			config.restore(originalConfig);
		}
		super.hide();
	}
}
