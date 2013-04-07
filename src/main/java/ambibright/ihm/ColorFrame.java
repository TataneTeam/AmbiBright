package ambibright.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ambibright.ressources.Config.Parameters;
import ambibright.ressources.Factory;

public class ColorFrame extends JDialog implements ChangeListener {

	private JPanel back;
	private JColorChooser colorChooser;
	private RGBColorManager colorManager;
	private boolean isCheckApp;
	private JLayeredPane lpane;

	public ColorFrame(Rectangle bounds, AmbiFont ambiFont) {
		super();

		isCheckApp = Factory.get().isCheckProcess();
		Factory.get().getConfig().put(Parameters.CONFIG_CHECK_PROCESS, "false");

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

		colorManager = new RGBColorManager(ambiFont);

		JButton close = new JButton("Save");
		close.setOpaque(false);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				restorConfig();
                Factory.get().getConfig().save();
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				restorConfig();
			}
		});

		int y = 0;
		lpane.add(back, JLayeredPane.DEFAULT_LAYER);
        back.setBounds(0,0,bounds.width,bounds.height);

		lpane.add(colorChooser, JLayeredPane.POPUP_LAYER);
		y = (bounds.height - colorChooser.getPreferredSize().height) / 2;
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
		Factory.get().getConfig().put(Parameters.CONFIG_CHECK_PROCESS, isCheckApp + "");
	}

}
