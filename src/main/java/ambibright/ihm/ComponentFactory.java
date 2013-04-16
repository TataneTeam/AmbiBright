package ambibright.ihm;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ambibright.config.PredefinedList;
import ambibright.config.ListProvider;
import ambibright.config.IntInterval;
import ambibright.config.FloatInterval;
import ambibright.config.Configurable;
import ambibright.config.Config;

/**
 * Factory used to generate component from config field
 */
public class ComponentFactory {

	private final Container container;
	private final AmbiFont ambiFont;
	private final Config config;
	private final Map<String, JComponent> propertyToComponent = new HashMap<String, JComponent>();

	public ComponentFactory(Container container, AmbiFont ambiFont, Config config) {
		this.container = container;
		this.ambiFont = ambiFont;
		this.config = config;
	}

	public void createComponents(Collection<Field> fields) {
		for (Field field : fields) {
			Configurable configurable = field.getAnnotation(Configurable.class);
			JComponent component;
			if (field.isAnnotationPresent(PredefinedList.class)) {
				component = createComboBox(field, configurable, field.getAnnotation(PredefinedList.class).provider());
			} else if (field.isAnnotationPresent(IntInterval.class)) {
				component = createIntSlider(field, configurable, field.getAnnotation(IntInterval.class));
			} else if (field.isAnnotationPresent(FloatInterval.class)) {
				component = createFloatSlider(field, configurable, field.getAnnotation(FloatInterval.class));
			} else if (boolean.class == field.getType()) {
				component = createCheckBox(field, configurable);
			} else {
				component = createTextField(field, configurable);
			}
			propertyToComponent.put(configurable.key(), component);
		}
	}

	public JComponent getComponent(String property) {
		return propertyToComponent.get(property);
	}

	public JCheckBox createCheckBox(final Field field, Configurable configurable) {
		final JCheckBox checkbox = new JCheckBox();
		checkbox.setSelected((Boolean) config.getValue(field));
		checkbox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				config.setValue(field, checkbox.isSelected());
			}
		});

		addLabel(configurable);
		addComponent(configurable, checkbox);

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
		comboBox.setSelectedItem(provider.getItemFromValue(config.getValue(field)));

		addLabel(configurable);
		addComponent(configurable, comboBox);

		return comboBox;
	}

	public JSlider createIntSlider(final Field field, Configurable configurable, IntInterval interval) {
		final JSlider slider = new JSlider(interval.min(), interval.max(), (Integer) config.getValue(field));
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				config.setValue(field, slider.getValue());
			}
		});

		addLabel(configurable);
		addComponent(configurable, slider);

		return slider;
	}

	public JSlider createFloatSlider(final Field field, Configurable configurable, final FloatInterval interval) {
		float inter = interval.max() - interval.min();
		int percent = (int) (((((Integer) config.getValue(field)) - interval.min()) / inter) * interval.precision());

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

		addLabel(configurable);
		addComponent(configurable, slider);

		return slider;
	}

	public JTextField createTextField(final Field field, Configurable configurable) {
		final JFormattedTextField textField = new JFormattedTextField(new JFormattedTextField.AbstractFormatter() {
			@Override
			public Object stringToValue(String text) throws ParseException {
				try {
					if (field.getType() == int.class) {
						return Integer.parseInt(text);
					} else if (field.getType() == float.class) {
						return Float.parseFloat(text);
					}
				} catch (NumberFormatException e) {
					throw new ParseException(e.getMessage(), 0);
				}
				return text;
			}

			@Override
			public String valueToString(Object value) throws ParseException {
				return null == value ? null : value.toString();
			}
		});
		textField.setValue(config.getValue(field));
		textField.addPropertyChangeListener("value", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				config.setValue(field, evt.getNewValue());
			}
		});

		addLabel(configurable);
		addComponent(configurable, textField);

		return textField;
	}

	private void addLabel(Configurable configurable) {
		JLabel label = new JLabel(" " + configurable.label());
		if (!configurable.description().isEmpty()) {
			label.setToolTipText(configurable.description());
		}
		container.add(ambiFont.setFontBold(label));
	}

	private void addComponent(Configurable configurable, JComponent component) {
		if (!configurable.description().isEmpty()) {
			component.setToolTipText(configurable.description());
		}
		container.add(ambiFont.setFontBold(component));
	}
}
