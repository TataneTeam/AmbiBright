package ambibright.ihm;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ambibright.config.Config;

public class RGBColorManager extends JPanel {

	private final Config config;
	private final ComponentFactory componentFactory;
	private JCheckBox all;

	public RGBColorManager(AmbiFont ambiFont, Config pConfig) {
		this.config = pConfig;

		final Collection<Field> fields = config.getFieldsByGroup(Config.GROUP_COLOR);

		setLayout(new GridLayout(2 + fields.size(), 2));

		componentFactory = new ComponentFactory(this, ambiFont, config);
		componentFactory.createComponents(fields);

		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				config.resetToDefault(fields);
			}
		});

		all = new JCheckBox("Same value for all primary color");
		all.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				componentFactory.getComponent(Config.CONFIG_RGB_G).setEnabled(!all.isSelected());
				componentFactory.getComponent(Config.CONFIG_RGB_B).setEnabled(!all.isSelected());
				if (all.isSelected()) {
					config.setDeltaGreen(config.getDeltaRed());
					config.setDeltaBlue(config.getDeltaRed());
					componentFactory.getComponent(Config.CONFIG_RGB_R).requestFocus();
				}
			}
		});
		all.setHorizontalAlignment(SwingConstants.CENTER);
		componentFactory.addPropertyChangeListener(Config.CONFIG_RGB_R, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (all.isSelected()) {
					config.setDeltaGreen(config.getDeltaRed());
					config.setDeltaBlue(config.getDeltaRed());
				}
			}
		});

		add(ambiFont.setFontBold(new JLabel("")), 6);
		add(ambiFont.setFont(all), 7);

		add(ambiFont.setFont(reset));
		reset.setOpaque(false);
	}

	public void clear() {
		componentFactory.clearPropertyChangeListeners();
	}

}
