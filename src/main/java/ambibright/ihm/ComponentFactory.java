package ambibright.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ambibright.config.PredefinedList;
import ambibright.config.ListProvider;
import ambibright.config.IntInterval;
import ambibright.config.FloatInterval;
import ambibright.config.Configurable;
import ambibright.config.Config;

/**
 * Created with IntelliJ IDEA. User: Nico Date: 15/04/13 Time: 19:32 To change
 * this template use File | Settings | File Templates.
 */
public class ComponentFactory {
	private final Container container;
	private final AmbiFont ambiFont;
	private final Config config;

	public ComponentFactory(Container container, AmbiFont ambiFont, Config config) {
		this.container = container;
		this.ambiFont = ambiFont;
		this.config = config;
	}

	public JCheckBox createCheckBox(final Field field, Configurable configurable) {
		final JCheckBox checkbox = new JCheckBox();
		checkbox.setSelected(config.getValueAsBoolean(field));
		checkbox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				config.setValue(field, checkbox.isSelected());
			}
		});
		container.add(ambiFont.setFontBold(new JLabel(" " + configurable.label())));
		container.add(ambiFont.setFont(checkbox));
		return checkbox;
	}

	public JComboBox createComboBox(final Field field, Configurable configurable, Class<? extends ListProvider> providerClass) {

		final ListProvider provider;
		try {
			provider = providerClass.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

		final JComboBox comboBox = new JComboBox();
		comboBox.setBorder(null);
		for (Object object : provider.getPossibleItems()) {
			comboBox.addItem(object);
		}
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				config.setValue(field, provider.getValueFromItem(comboBox.getSelectedItem()));
			}
		});
		comboBox.setSelectedItem(provider.getItemFromValue(config.getValueAsObject(field)));
		container.add(ambiFont.setFontBold(new JLabel(" " + configurable.label())));
		container.add(ambiFont.setFont(comboBox));
		return comboBox;
	}

	public JSlider createIntSlider(final Field field, Configurable configurable, IntInterval interval) {
		final JSlider slider = new JSlider(interval.min(), interval.max(), config.getValueAsInt(field));
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				config.setValue(field, slider.getValue());
			}
		});
		container.add(ambiFont.setFontBold(new JLabel(" " + configurable.label())));
		container.add(ambiFont.setFont(slider));
		return slider;
	}

	public JSlider createFloatSlider(final Field field, Configurable configurable, final FloatInterval interval) {
		float inter = interval.max() - interval.min();
		int percent = (int) (((config.getValueAsFloat(field) - interval.min()) / inter) * interval.precision());

		final JSlider slider = new JSlider(0, interval.precision(), percent);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				float inter = interval.max() - interval.min();
				float percent = slider.getValue() / Float.valueOf(interval.precision());
				float value = interval.min() + (inter * percent);
				config.setValue(field, value);
			}
		});

		container.add(ambiFont.setFontBold(new JLabel(" " + configurable.label())));
		container.add(ambiFont.setFont(slider));
		return slider;
	}

	public void createComponents(Collection<Field> fields) {
		for (Field field : fields) {
			Configurable configurable = field.getAnnotation(Configurable.class);
			if (field.isAnnotationPresent(PredefinedList.class)) {
				createComboBox(field, configurable, field.getAnnotation(PredefinedList.class).provider());
			} else if (field.isAnnotationPresent(IntInterval.class)) {
				createIntSlider(field, configurable, field.getAnnotation(IntInterval.class));
			} else if (field.isAnnotationPresent(FloatInterval.class)) {
				createFloatSlider(field, configurable, field.getAnnotation(FloatInterval.class));
			} else if (boolean.class == field.getType()) {
				createCheckBox(field, configurable);
			} else {
				createTextField(field, configurable);
			}
		}
	}

	public JTextField createTextField(final Field field, Configurable configurable) {
		final JTextField textField = new JTextField(config.getValueAsString(field));
		textField.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				config.setValue(field, textField.getText());
			}
		});
		container.add(ambiFont.setFontBold(new JLabel(" " + configurable.label())));
		container.add(ambiFont.setFont(textField));
		return textField;
	}
}