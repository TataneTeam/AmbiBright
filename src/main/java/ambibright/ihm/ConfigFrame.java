package ambibright.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ambibright.ressources.Factory;
import ambibright.config.PredefinedList;
import ambibright.config.ListProvider;
import ambibright.config.IntInterval;
import ambibright.config.FloatInterval;
import ambibright.config.Configurable;
import ambibright.config.Config;

public class ConfigFrame extends JFrame {

	private final Config originalConfig;
	private final Config config;
	private final AmbiFont ambiFont;

	// private JButton findPortBtn;

	public ConfigFrame(AmbiFont ambiFont, Config pConfig) {
		super(Factory.appName + " - Configuration");
		this.originalConfig = pConfig.cloneConfig();
		this.config = pConfig;
		this.ambiFont = ambiFont;
		setIconImage(Factory.get().getImageIcon());

		Collection<Field> fields = config.getFieldsByGroup(Config.GROUP_CONFIG);

		setLayout(new GridLayout(fields.size() + 1, 2));

        new ComponentFactory(this, ambiFont, config).createComponents( fields );

		// TODO
		// findPortBtn = new JButton("Try to find the port");
		// findPortBtn.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// findPortBtn.setEnabled(false);
		// //
		// arduinoSerial.setText(ArduinoSender.getArduinoPort(Integer.valueOf(arduinoDataRate.getText()),
		// // ArduinoSender.defaultTestString));
		// findPortBtn.setEnabled(true);
		// }
		// });
		// add(ambiFont.setFontBold(new JLabel("")));
		// add(ambiFont.setFont(findPortBtn));

		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				config.save();
				Factory.get().getTray().updateCheckBox();
				Factory.get().getManager().restart();
				dispose();
			}
		});
		add(ambiFont.setFont(save));

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				config.restore(originalConfig);
				dispose();
			}
		});
		add(ambiFont.setFont(cancel));

		pack();
		setResizable(false);
		setLocationRelativeTo(getParent());
		setVisible(true);
	}
}
