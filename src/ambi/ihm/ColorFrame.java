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

import ambi.ressources.Factory;

public class ColorFrame extends JDialog implements ChangeListener {

	private JPanel back;
	private JColorChooser colorChooser;

	public ColorFrame(Rectangle bounds) {
		super();
		JLayeredPane lpane = new JLayeredPane();
		add(lpane);
		setUndecorated(true);

		back = new JPanel();
		back.setBackground(Color.black);
		lpane.setPreferredSize(new Dimension(bounds.width, bounds.height));

		colorChooser = new JColorChooser(back.getBackground());
		colorChooser.getSelectionModel().addChangeListener(this);
		colorChooser.setBorder(BorderFactory.createTitledBorder("Choose background color"));

		JButton close = new JButton("Close");
		close.setOpaque(false);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});

		lpane.add(back, JLayeredPane.DEFAULT_LAYER);
		back.setBounds(bounds);
		lpane.add(colorChooser, JLayeredPane.POPUP_LAYER);
		colorChooser.setBounds(bounds.x + (bounds.width - colorChooser.getPreferredSize().width) / 2, bounds.y + (bounds.height - colorChooser.getPreferredSize().height) / 2, colorChooser.getPreferredSize().width, colorChooser.getPreferredSize().height);
		lpane.add(Factory.setFont(close), JLayeredPane.POPUP_LAYER);
		close.setBounds(bounds.x + (bounds.width - close.getPreferredSize().width) / 2, bounds.y + (bounds.height - close.getPreferredSize().height), close.getPreferredSize().width, close.getPreferredSize().height);

		pack();
		setLocation(bounds.x, bounds.y);
		setVisible(true);
	}

	public void stateChanged(ChangeEvent e) {
		back.setBackground(colorChooser.getColor());
		revalidate();
	}

}
