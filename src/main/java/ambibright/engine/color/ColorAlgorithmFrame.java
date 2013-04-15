package ambibright.engine.color;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ambibright.ihm.AmbiFont;
import ambibright.ressources.Factory;

public class ColorAlgorithmFrame extends JFrame implements ChangeListener{

	private JComboBox algos;
	private JPanel currentColor, alteredColor;
	private JColorChooser colorChooser ;
	private JTextField algoValue;

	public ColorAlgorithmFrame(){
		super();
		setIconImage(Factory.get().getImageIcon());
		colorChooser = new JColorChooser();
		colorChooser.getSelectionModel().addChangeListener(this);
		colorChooser.setBorder(BorderFactory.createTitledBorder("Choose background color"));
		colorChooser.setPreviewPanel(new JPanel());

		currentColor = new JPanel();
		alteredColor = new JPanel();
		currentColor.setBackground(Color.black);

		JPanel algoPanel = new JPanel();
		algoPanel.setLayout(new GridLayout(2,1));

		algos = new JComboBox();
		algos.setBackground(null);
		algos.setFont(AmbiFont.fontMonitoringImage);
		List<ColorAlgorithm> colorAlgorithmList = Arrays.asList(new ColorAlgorithmBrightness(Factory.get().getConfig()), new ColorAlgorithmGamma(Factory.get().getConfig()), new ColorAlgorithmHue(Factory.get().getConfig()), new ColorAlgorithmSaturation(Factory.get().getConfig()));
		for(ColorAlgorithm algo: colorAlgorithmList){
			algos.addItem(algo);
		}
		algos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apply();
			}
		});
		algoValue = new JTextField("0");
		algoValue.setFont(AmbiFont.fontMonitoringImage);
		algoValue.setHorizontalAlignment(JLabel.CENTER);
		algoValue.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				apply();
			}
		});

		algoPanel.add(algos);
		algoPanel.add(algoValue);

		setLayout(new GridLayout(2,2));

		add(currentColor);
		add(alteredColor);
		add(colorChooser);
		add(algoPanel);

		apply();

		pack();
		setVisible(true);
		setLocationRelativeTo(getParent());
	}

	public void stateChanged(ChangeEvent e) {
		currentColor.setBackground( colorChooser.getColor());
		apply();
	}


	private void apply(){
		Color newColor = currentColor.getBackground();
		ColorAlgorithm colorAlgorithm = getSelectedColorAlgorithm();
		float algoValue = getSelectedColorAlgorithmValue();
		int[] color = new int[]{newColor.getRed(), newColor.getGreen(), newColor.getBlue()};
//		colorAlgorithm.updateParameter(algoValue);
		colorAlgorithm.apply(color);
		alteredColor.setBackground(new Color(color[0],color[1],color[2]));
		setTitle(colorAlgorithm + " applied with value " + algoValue);
	}

	private ColorAlgorithm getSelectedColorAlgorithm(){
		return (ColorAlgorithm) algos.getSelectedItem();
	}

	private float getSelectedColorAlgorithmValue(){
		float result = 0;
		try{
			result = Float.valueOf(algoValue.getText());
		}catch (Exception e) {
		}
		return result;
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		new ColorAlgorithmFrame();
	}

}
