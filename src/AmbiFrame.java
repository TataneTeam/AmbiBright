import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class AmbiFrame extends JFrame{
	
	private JPanel[][] cells;
	
	public AmbiFrame(int rows, int cols){
		super("Ambi Frame");
		setLayout(new GridLayout(rows, cols));
		cells = new JPanel[rows][cols];
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				cells[i][j] = new JPanel();
				cells[i][j].setPreferredSize(new Dimension(40,40));
				cells[i][j].setBorder(BorderFactory.createEtchedBorder());
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
	
	public void setColor(int x, int y, Color color){
		cells[x][y].setBackground(color);
		validate();
	}

}
