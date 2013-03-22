package ambi.ihm;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ambi.ressources.Config.Parameters;
import ambi.ressources.Factory;

public class RGBColorManager extends JPanel {

	private JSlider r, g, b;
	private int min = -50;
	private int max = 50;
	private JCheckBox all;

	public RGBColorManager() {
		setLayout(new GridLayout(5, 1));
		r = new JSlider(JSlider.HORIZONTAL, min, max, Factory.getRGB_R());
		r.setPreferredSize(new Dimension(400, r.getPreferredSize().height));
		g = new JSlider(JSlider.HORIZONTAL, min, max, Factory.getRGB_G());
		b = new JSlider(JSlider.HORIZONTAL, min, max, Factory.getRGB_B());
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
		add(Factory.setFont(r));
		add(Factory.setFont(g));
		add(Factory.setFont(b));
		r.addChangeListener(new SliderColorListener(r, Parameters.CONFIG_RGB_R));
		g.addChangeListener(new SliderColorListener(g, Parameters.CONFIG_RGB_G));
		b.addChangeListener(new SliderColorListener(b, Parameters.CONFIG_RGB_B));
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
				}
			}
		});

		add(Factory.setFont(all));
		add(Factory.setFont(reset));
		setOpaque(false);
		reset.setOpaque(false);
	}

	class SliderColorListener implements ChangeListener {

		private JSlider slider;
		private Parameters config;

		public SliderColorListener(JSlider slider, Parameters config) {
			this.config = config;
			this.slider = slider;
		}

		public void stateChanged(ChangeEvent arg0) {
			if (all.isSelected()) {
				g.setValue(r.getValue());
				b.setValue(r.getValue());
			}
			Factory.getConfig().put(config, slider.getValue() + "");
		}

	}
}
