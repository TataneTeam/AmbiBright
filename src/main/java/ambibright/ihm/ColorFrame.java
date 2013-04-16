package ambibright.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ambibright.config.Config;

public class ColorFrame extends JDialog implements ChangeListener {

	private final Config config;
	private Config originalConfig;
	private JPanel back;
	private JColorChooser colorChooser;
	private RGBColorManager colorManager;
	private JLayeredPane lpane;

	public ColorFrame(Rectangle bounds, AmbiFont ambiFont, Config pConfig) {
		super();

		this.originalConfig = pConfig.cloneConfig();
		this.config = pConfig;

		config.setCheckProcess(false);

		lpane = new JLayeredPane();
		lpane.setOpaque(false);
		add(lpane);
		lpane.setPreferredSize(new Dimension(bounds.width, bounds.height));
		lpane.setLocation(0, 0);

		back = new JPanel();
		back.setBackground(Color.white);

		colorChooser = new JColorChooser(back.getBackground());
		colorChooser.getSelectionModel().addChangeListener(this);
		colorChooser.setBorder(BorderFactory.createTitledBorder("Choose background color"));
		colorChooser.setPreviewPanel(new JPanel());

		colorManager = new RGBColorManager(ambiFont, config);

		JButton save = new JButton("Save");
		save.setOpaque(false);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                config.setCheckProcess( originalConfig.isCheckProcess() );
				originalConfig = null;
				dispose();
				config.save();
			}
		});

		lpane.add(back, JLayeredPane.DEFAULT_LAYER);
		back.setBounds(0, 0, bounds.width, bounds.height);

		lpane.add(colorChooser, JLayeredPane.POPUP_LAYER);
		int y = (bounds.height - colorChooser.getPreferredSize().height) / 4;
		colorChooser.setBounds((bounds.width - colorChooser.getPreferredSize().width) / 2, y, colorChooser.getPreferredSize().width, colorChooser.getPreferredSize().height);

		lpane.add(colorManager, JLayeredPane.POPUP_LAYER);
		y += colorChooser.getPreferredSize().height + 20;
		colorManager.setBounds((bounds.width - colorManager.getPreferredSize().width) / 2, y, colorManager.getPreferredSize().width, colorManager.getPreferredSize().height);

		lpane.add(ambiFont.setFont(save), JLayeredPane.POPUP_LAYER);
		save.setBounds((bounds.width - save.getPreferredSize().width) / 2, bounds.y + (bounds.height - save.getPreferredSize().height), save.getPreferredSize().width, save.getPreferredSize().height);

		setUndecorated(true);
		pack();
		setLocation(bounds.x, bounds.y);
		setAlwaysOnTop(true);
		setVisible(true);
	}

	public void stateChanged(ChangeEvent e) {
		back.setBackground(colorChooser.getColor());
	}

	@Override
	public void hide() {
		colorManager.clear();
		if (null != originalConfig) {
			config.restore(originalConfig);
		}
		super.hide();
	}

}
