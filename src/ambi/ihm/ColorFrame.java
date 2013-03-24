package ambi.ihm;

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

import ambi.ressources.Config.Parameters;
import ambi.ressources.Factory;

public class ColorFrame extends JDialog implements ChangeListener {

	private JPanel back;
	private JColorChooser colorChooser;
	private RGBColorManager colorManager;
	private boolean isCheckApp;

	public ColorFrame(Rectangle bounds, AmbiFont ambiFont) {
		super();

		isCheckApp = Factory.get().isCheckProcess();
		Factory.get().getConfig().put(Parameters.CONFIG_CHECK_PROCESS, "false");

		JLayeredPane lpane = new JLayeredPane();
		add(lpane);
		setUndecorated(true);

		back = new JPanel();
		back.setBackground(Color.black);
		lpane.setPreferredSize(new Dimension(bounds.width, bounds.height));

		colorChooser = new JColorChooser(back.getBackground());
		colorChooser.getSelectionModel().addChangeListener(this);
		colorChooser.setBorder(BorderFactory.createTitledBorder("Choose background color"));
		colorChooser.setPreviewPanel(new JPanel());

		colorManager = new RGBColorManager(ambiFont);

		JButton close = new JButton("Save");
		close.setOpaque(false);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				Factory.get().getConfig().put(Parameters.CONFIG_CHECK_PROCESS, isCheckApp + "");
				Factory.get().getConfig().save();
			}
		});

		int y = 0;
		lpane.add(back, JLayeredPane.DEFAULT_LAYER);
		back.setBounds(bounds);

		lpane.add(colorChooser, JLayeredPane.POPUP_LAYER);
		y = bounds.y + (bounds.height - colorChooser.getPreferredSize().height) / 2;
		colorChooser.setBounds(bounds.x + (bounds.width - colorChooser.getPreferredSize().width) / 2, y, colorChooser.getPreferredSize().width, colorChooser.getPreferredSize().height);

		lpane.add(colorManager, JLayeredPane.POPUP_LAYER);
		y += colorChooser.getPreferredSize().height + 20;
		colorManager.setBounds(bounds.x + (bounds.width - colorManager.getPreferredSize().width) / 2, y, colorManager.getPreferredSize().width, colorManager.getPreferredSize().height);

		lpane.add(ambiFont.setFont(close), JLayeredPane.POPUP_LAYER);
		close.setBounds(bounds.x + (bounds.width - close.getPreferredSize().width) / 2, bounds.y + (bounds.height - close.getPreferredSize().height), close.getPreferredSize().width, close.getPreferredSize().height);

		pack();
		setLocation(bounds.x, bounds.y);
		setVisible(true);
	}

	public void stateChanged(ChangeEvent e) {
		back.setBackground(colorChooser.getColor());
	}

}
