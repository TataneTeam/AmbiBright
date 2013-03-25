package ambibright.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import ambibright.ressources.Factory;

public class MonitoringFrame extends JFrame {

	private static final int squareSize = 40;
	private JPanel[][] cells;
	private JLabel imageLabel;
	int rows, cols, i, imageHeight;

	public MonitoringFrame(int rows, int cols, Image icon) {
		super(Factory.appName + " - Monitoring Frame");
		setIconImage(icon);
		this.rows = rows;
		this.cols = cols;

		JLayeredPane lpane = new JLayeredPane();
		add(lpane);

		JPanel content = new JPanel();
		content.setLayout(new GridLayout(rows, cols));
		lpane.add(content, JLayeredPane.DEFAULT_LAYER);
		lpane.setPreferredSize(new Dimension(cols * squareSize, rows * squareSize));
		content.setBounds(0, 0, lpane.getPreferredSize().width, lpane.getPreferredSize().height);

		imageLabel = new JLabel();
		lpane.add(imageLabel, JLayeredPane.POPUP_LAYER);
		imageLabel.setBounds(squareSize * 2, squareSize * 2, lpane.getPreferredSize().width - 4 * squareSize, lpane.getPreferredSize().height - 4 * squareSize);
		imageLabel.setBorder(BorderFactory.createLineBorder(Color.red));

		cells = new JPanel[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				cells[i][j] = new JPanel();
				cells[i][j].setPreferredSize(new Dimension(squareSize, squareSize));
				cells[i][j].setBorder(BorderFactory.createEtchedBorder());
				cells[i][j].setBackground(Color.black);
				content.add(cells[i][j]);
			}
		}
		addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
				setVisible(false);
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}

			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowActivated(WindowEvent e) {
			}
		});

		pack();
		setAlwaysOnTop(true);
		setResizable(false);
		setLocationRelativeTo(getParent());
		setVisible(false);
	}

	public void setColor(int x, int y, Color color) {
		cells[x][y].setBackground(color);
	}

	public void refresh(Integer[][] colors) {
		if (isVisible()) {
			for (i = 0; i < rows; i++) {
				setColor(rows - 1 - i, 0, new Color(colors[i][0], colors[i][1], colors[i][2]));
			}
			for (i = 1; i < cols; i++) {
				setColor(0, i, new Color(colors[i + rows - 1][0], colors[i + rows - 1][1], colors[i + rows - 1][2]));
			}
			for (i = 1; i < rows; i++) {
				setColor(i, cols - 1, new Color(colors[i + rows + cols - 2][0], colors[i + rows + cols - 2][1], colors[i + rows + cols - 2][2]));
			}
		}
	}

	public void setInfo(String string) {
		setTitle(Factory.appName + " - Monitoring Frame - " + string);
	}

	public void setImage(BufferedImage image) {
		if (isVisible()) {
			imageHeight = image.getHeight() * imageLabel.getWidth() / image.getWidth();
			imageLabel.setIcon(resizeImage(image, imageLabel.getWidth(), imageHeight));
			imageLabel.setSize(new Dimension(imageLabel.getWidth(), imageHeight));
		}
	}

	public ImageIcon resizeImage(BufferedImage image, int width, int height) {
		BufferedImage resutl = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = resutl.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, width, height, null);
		graphics2D.dispose();
		return new ImageIcon(resutl);
	}

}
