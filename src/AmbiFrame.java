import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AmbiFrame extends JFrame {

	private JPanel[][] cells;
	int rows, cols;

	public AmbiFrame(int rows, int cols) {
		super("Ambi Frame");
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
			public void windowOpened(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {System.exit(0);}
			public void windowClosed(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
		});
		pack();
		setAlwaysOnTop(true);
		setResizable(false);
		setLocationRelativeTo(getParent());
		setVisible(true);
	}

	public void setColor(int x, int y, Color color) {
		cells[x][y].setBackground(color);
		validate();
	}

	public void refresh(List<Color> colors) {
		for (int i = 0; i < rows; i++) {
			setColor(rows - 1 - i, 0, colors.get(i));
		}
		for (int i = 1; i < cols; i++) {
			setColor(0, i, colors.get(i + rows - 1));
		}
		for (int i = 1; i < rows; i++) {
			setColor(i, cols - 1, colors.get(i + rows + cols - 2));
		}
		validate();
	}

}
