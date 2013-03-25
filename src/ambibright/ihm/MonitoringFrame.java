package ambibright.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ambibright.ressources.Factory;

public class MonitoringFrame extends JFrame {

	private JPanel[][] cells;
	int rows, cols, i;

	public MonitoringFrame(int rows, int cols, Image icon) {
		super(Factory.appName + " - Monitoring Frame");
		setIconImage(icon);
		this.rows = rows;
		this.cols = cols;
		setLayout(new GridLayout(rows, cols));
		cells = new JPanel[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				cells[i][j] = new JPanel();
				cells[i][j].setPreferredSize(new Dimension(40, 40));
				cells[i][j].setBorder(BorderFactory.createEtchedBorder());
				cells[i][j].setBackground(Color.black);
				add(cells[i][j]);
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

}
