package ambibright.ihm;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import ambibright.engine.ColorsChangeObserver;
import ambibright.ressources.Factory;

public class MonitoringFrame extends JFrame implements ColorsChangeObserver {

	private static final int squareSize = 40;
	private final FpsCounter fpsCounter;
	private JPanel[][] cells;
	private JLabel imageLabel;
	private int rows, cols, i, imageHeight, j;

	public MonitoringFrame(int rows, int cols, Image icon) {
		super(Factory.appName + " - Monitoring Frame");
		setIconImage(icon);
		this.rows = rows;
		this.cols = cols;
		this.fpsCounter = new FpsCounter();

		JLayeredPane lpane = new JLayeredPane();
		add(lpane);

		JPanel content = new JPanel();
		content.setLayout(new GridLayout(rows, cols));
		lpane.add(content, JLayeredPane.DEFAULT_LAYER);
		lpane.setPreferredSize(new Dimension(cols * squareSize, rows * squareSize));
		content.setBounds(0, 0, lpane.getPreferredSize().width, lpane.getPreferredSize().height);

		imageLabel = new JLabel();
		lpane.add(imageLabel, JLayeredPane.POPUP_LAYER);
		imageLabel.setBounds(squareSize + squareSize / 2, squareSize + squareSize / 2, lpane.getPreferredSize().width - 3 * squareSize, lpane.getPreferredSize().height - 3 * squareSize);
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
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowIconified(WindowEvent e) {
				hideFrame();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				hideFrame();
			}

			@Override
			public void windowActivated(WindowEvent e) {
				setLocation(Factory.get().getMonitoringLocation());
				Factory.get().getManager().addObserver(MonitoringFrame.this);
			}
		});

		pack();
		setAlwaysOnTop(true);
		setResizable(false);
		setLocationRelativeTo(getParent());
		setVisible(false);
	}

	private void hideFrame() {
		Factory.get().getManager().removeObserver(this);
		Factory.get().saveMonitoringLocation(getLocation().x, getLocation().y);
		setVisible(false);
	}

	public void setColor(int x, int y, Color color) {
		cells[x][y].setBackground(color);
	}

	public void setInfo(String string) {
		setTitle(Factory.appName + " - Monitoring Frame - " + string);
	}

	public void addZones(BufferedImage image, Rectangle[] bounds) {
		Graphics2D graphics2D = image.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.setColor(Color.GREEN);
		graphics2D.setStroke(new BasicStroke(2));
		for (Rectangle bound : bounds) {
			graphics2D.drawRect(bound.x, bound.y, bound.width, bound.height);
		}
		graphics2D.dispose();
	}

	public ImageIcon resizeImage(BufferedImage image, int width, int height) {
		String text = image.getWidth() + "x" + image.getHeight();
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = result.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.drawImage(image, 0, 0, width, height, null);
		graphics2D.setFont(AmbiFont.fontMonitoringImage);
		Rectangle2D rect = graphics2D.getFontMetrics(AmbiFont.fontMonitoringImage).getStringBounds(text, graphics2D);
		int x = (int) (width - rect.getWidth()) / 2;
		int y = (int) (height - rect.getHeight()) / 2;
		graphics2D.setColor(Color.black);
		graphics2D.drawString(text, x, y);
		graphics2D.setColor(Color.white);
		graphics2D.drawString(text, x + 1, y + 1);
		graphics2D.dispose();
		return new ImageIcon(result);
	}

	public boolean display() {
		setVisible(!isVisible());
		setState(Frame.NORMAL);
		return isVisible();
	}

	@Override
	public void onColorsChange(ambibright.engine.capture.Image image, byte[] colors) {
        // retrieve BufferedImage from image
        BufferedImage bufferedImage = image.getBufferedImage();

		// updating fps
		int fps = fpsCounter.fps();
		setInfo(fps + " fps");

		// update image
		addZones(bufferedImage, Factory.get().getCurrentBounds().getZones());
		imageHeight = bufferedImage.getHeight() * imageLabel.getWidth() / bufferedImage.getWidth();
		imageLabel.setIcon(resizeImage(bufferedImage, imageLabel.getWidth(), imageHeight));
		imageLabel.setSize(imageLabel.getWidth(), imageHeight);

		j = 6;
		// Left from bottom to up
		for (i = 0; i < rows; i++) {
			setColor(rows - 1 - i, 0, new Color(colors[j++] & 0xFF, colors[j++] & 0xFF, colors[j++] & 0xFF));
		}
		// Top from left to right
		for (i = 1; i < cols; i++) {
			setColor(0, i, new Color(colors[j++] & 0xFF, colors[j++] & 0xFF, colors[j++] & 0xFF));
		}
		// Right from to to bottom
		for (i = 1; i < rows; i++) {
			setColor(i, cols - 1, new Color(colors[j++] & 0xFF, colors[j++] & 0xFF, colors[j++] & 0xFF));
		}
	}
}
