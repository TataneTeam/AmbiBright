package ambibright.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ambibright.config.Config;

public class ColorFrame extends JDialog implements ChangeListener {

	private final Config config;
	private JPanel back;
	private JColorChooser colorChooser;
	private RGBColorManager colorManager;
	private boolean isCheckApp;
	private JLayeredPane lpane;

	public ColorFrame(Rectangle bounds, AmbiFont ambiFont, Config pConfig) {
		super();

		this.config = pConfig;

		isCheckApp = config.isCheckProcess();
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

		JButton close = new JButton("Save");
		close.setOpaque(false);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				restorConfig();
				config.save();
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				restorConfig();
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

		lpane.add(ambiFont.setFont(close), JLayeredPane.POPUP_LAYER);
		close.setBounds((bounds.width - close.getPreferredSize().width) / 2, bounds.y + (bounds.height - close.getPreferredSize().height), close.getPreferredSize().width, close.getPreferredSize().height);

		setUndecorated(true);
		pack();
		setLocation(bounds.x, bounds.y);
		setAlwaysOnTop(true);
		setVisible(true);
	}

	public void stateChanged(ChangeEvent e) {
		back.setBackground(colorChooser.getColor());
	}

	private void restorConfig() {
		config.setCheckProcess(isCheckApp);
	}

}
