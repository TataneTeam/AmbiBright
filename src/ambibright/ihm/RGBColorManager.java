package ambibright.ihm;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ambibright.ressources.Config.Parameters;
import ambibright.ressources.Factory;

public class RGBColorManager extends JPanel {

	private JSlider r, g, b;
	private int min = -150;
	private int max = 150;
	private JCheckBox all;

	public RGBColorManager(AmbiFont ambiFont) {
		setLayout(new GridLayout(5, 1));
		r = new JSlider(JSlider.HORIZONTAL, min, max, Factory.get().getRGB_R());
		r.setPreferredSize(new Dimension(800, r.getPreferredSize().height));
		g = new JSlider(JSlider.HORIZONTAL, min, max, Factory.get().getRGB_G());
		b = new JSlider(JSlider.HORIZONTAL, min, max, Factory.get().getRGB_B());
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
		add(ambiFont.setFont(r));
		add(ambiFont.setFont(g));
		add(ambiFont.setFont(b));
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
					r.requestFocus();
				}
			}
		});

		all.setHorizontalAlignment(JLabel.CENTER);
		add(ambiFont.setFont(all));
		add(ambiFont.setFont(reset));
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
			Factory.get().getConfig().put(config, slider.getValue() + "");
		}

	}
}
