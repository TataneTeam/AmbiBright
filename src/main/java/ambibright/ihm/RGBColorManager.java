package ambibright.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ambibright.config.FloatInterval;
import ambibright.config.Configurable;
import ambibright.config.Config;
import ambibright.config.IntInterval;
import ambibright.config.PredefinedList;

public class RGBColorManager extends JPanel {

	private final Config config;
	private final AmbiFont ambiFont;
	private JSlider r, g, b;
	private int min = -150;
	private int max = 150;
	private JCheckBox all;
	private JSlider hue, saturation, brightness, gamma;

	public RGBColorManager(AmbiFont ambiFont, Config pConfig) {
		this.config = pConfig;
		this.ambiFont = ambiFont;

        Collection<Field> fields = config.getFieldsByGroup( Config.GROUP_COLOR );

		setLayout(new GridLayout(5+fields.size(), 2));
		r = new JSlider(JSlider.HORIZONTAL, min, max, config.getDeltaRed());
		r.setPreferredSize(new Dimension(800, r.getPreferredSize().height));
		g = new JSlider(JSlider.HORIZONTAL, min, max, config.getDeltaGreen());
		b = new JSlider(JSlider.HORIZONTAL, min, max, config.getDeltaBlue());
		r.setMajorTickSpacing(10);
		g.setMajorTickSpacing(10);
		b.setMajorTickSpacing(10);
		r.setMinorTickSpacing(1);
		g.setMinorTickSpacing(1);
		b.setMinorTickSpacing(1);
		r.setPaintTicks(true);
		g.setPaintTicks(true);
		b.setPaintTicks(true);
		r.setPaintLabels(true);
		g.setPaintLabels(true);
		b.setPaintLabels(true);
		add(ambiFont.setFontBold(new JLabel(" Red")));
		add(ambiFont.setFont(r));
		add(ambiFont.setFontBold(new JLabel(" Green")));
		add(ambiFont.setFont(g));
		add(ambiFont.setFontBold(new JLabel(" Blue")));
		add(ambiFont.setFont(b));
		ChangeListener slideColorListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (all.isSelected()) {
					g.setValue(r.getValue());
					b.setValue(r.getValue());
				}
				config.setDeltaRed(r.getValue());
				config.setDeltaGreen(g.getValue());
				config.setDeltaBlue(b.getValue());
			}
		};
		r.addChangeListener(slideColorListener);
		g.addChangeListener(slideColorListener);
		b.addChangeListener(slideColorListener);
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				r.setValue(0);
				g.setValue(0);
				b.setValue(0);
			}
		});

		all = new JCheckBox("Same value for all primary color");
		all.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				g.setEnabled(!all.isSelected());
				b.setEnabled(!all.isSelected());
				if (all.isSelected()) {
					g.setValue(r.getValue());
					b.setValue(r.getValue());
					r.requestFocus();
				}
			}
		});

		all.setHorizontalAlignment(JLabel.CENTER);
		add(ambiFont.setFontBold(new JLabel("")));
		add(ambiFont.setFont(all));

        new ComponentFactory(this, ambiFont, config).createComponents( fields );

		add(ambiFont.setFont(reset));
		setOpaque(false);
		reset.setOpaque(false);
	}

}
