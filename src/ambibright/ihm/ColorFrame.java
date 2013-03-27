package ambibright.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ambibright.ressources.Config.Parameters;
import ambibright.ressources.Factory;

import com.sun.awt.AWTUtilities;

public class ColorFrame extends JDialog implements ChangeListener {

	private JPanel back;
	private JColorChooser colorChooser;
	private RGBColorManager colorManager;
	private boolean isCheckApp;
	private JCheckBox displaySquare, opaqueDialog;
	private List<JPanel> squares;
	private JLayeredPane lpane;
	private AmbiFont ambiFont;

	public ColorFrame(Rectangle bounds, AmbiFont ambiFont) {
		super();

		this.ambiFont = ambiFont;
		isCheckApp = Factory.get().isCheckProcess();
		Factory.get().getConfig().put(Parameters.CONFIG_CHECK_PROCESS, "false");

		lpane = new JLayeredPane();
		lpane.setOpaque(false);
		add(lpane);
		lpane.setPreferredSize(new Dimension(bounds.width, bounds.height));

		back = new JPanel();
		back.setBackground(Color.white);

		colorChooser = new JColorChooser(back.getBackground());
		colorChooser.getSelectionModel().addChangeListener(this);
		colorChooser.setBorder(BorderFactory.createTitledBorder("Choose background color"));
		colorChooser.setPreviewPanel(new JPanel());

		squares = new ArrayList<JPanel>();
		displaySquare = new JCheckBox("Display squares");
		displaySquare.setOpaque(false);
		displaySquare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displaySquare();
			}
		});

		opaqueDialog = new JCheckBox("Opaque background");
		opaqueDialog.setOpaque(false);
		opaqueDialog.setSelected(true);
		opaqueDialog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeOpaque(opaqueDialog.isSelected());
			}
		});

		colorManager = new RGBColorManager(ambiFont);

		JButton close = new JButton("Save");
		close.setOpaque(false);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				restorConfig();
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				restorConfig();
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

		lpane.add(ambiFont.setFont(displaySquare), JLayeredPane.POPUP_LAYER);
		y += colorManager.getPreferredSize().height + 20;
		displaySquare.setBounds(bounds.x + (bounds.width - displaySquare.getPreferredSize().width) / 2, y, displaySquare.getPreferredSize().width, displaySquare.getPreferredSize().height);

		lpane.add(ambiFont.setFont(opaqueDialog), JLayeredPane.POPUP_LAYER);
		y += displaySquare.getPreferredSize().height + 20;
		opaqueDialog.setBounds(bounds.x + (bounds.width - opaqueDialog.getPreferredSize().width) / 2, y, opaqueDialog.getPreferredSize().width, opaqueDialog.getPreferredSize().height);

		lpane.add(ambiFont.setFont(close), JLayeredPane.POPUP_LAYER);
		close.setBounds(bounds.x + (bounds.width - close.getPreferredSize().width) / 2, bounds.y + (bounds.height - close.getPreferredSize().height), close.getPreferredSize().width, close.getPreferredSize().height);

		setUndecorated(true);
		pack();
		setLocation(bounds.x, bounds.y);
		setAlwaysOnTop(true);
		setVisible(true);
	}

	public void displaySquare() {
		if (isVisible()) {
			for (JPanel square : squares) {
				lpane.remove(square);
			}
			squares.clear();
			if (displaySquare.isSelected()) {
				int ledNumber = 1;
				int x = (Factory.get().getBounds().width - Factory.get().getCurrentBounds().getBounds().width) /2;
				int y = (Factory.get().getBounds().height - Factory.get().getCurrentBounds().getBounds().height) /2;
				for (Rectangle bound : Factory.get().getScreenSquares().getSquares()) {
					JPanel square = new JPanel();
					lpane.add(square, JLayeredPane.POPUP_LAYER);
					square.setBounds(x + bound.x, y + bound.y, bound.width, bound.height);
					JLabel label = new JLabel("#" + ledNumber++);
					label.setHorizontalAlignment(JLabel.CENTER);
					square.setLayout(new GridLayout(1, 1));
					square.add(ambiFont.setFontBold(label));
					square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					square.setOpaque(false);
					squares.add(square);
				}
			}
			repaint();
		}
	}

	private void changeOpaque(boolean opaque) {
		back.setOpaque(opaque);
		AWTUtilities.setWindowOpaque(this, opaque);
	}

	public void stateChanged(ChangeEvent e) {
		changeOpaque(true);
		opaqueDialog.setSelected(true);
		back.setBackground(colorChooser.getColor());
	}

	private void restorConfig() {
		Factory.get().getConfig().put(Parameters.CONFIG_CHECK_PROCESS, isCheckApp + "");
	}

}
