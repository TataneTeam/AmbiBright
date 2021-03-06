package ambibright.ihm;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ambibright.config.Config;
import ambibright.config.Configurable;
import ambibright.config.FloatInterval;
import ambibright.config.IntInterval;
import ambibright.config.ListProvider;
import ambibright.config.ListProviderFactory;
import ambibright.config.PredefinedList;

/**
 * Factory used to generate component from config field
 */
public class ComponentFactory {

	private final Container container;
	private final AmbiFont ambiFont;
	private final Config config;
	private final Map<String, JComponent> propertyToComponent = new HashMap<String, JComponent>();
	private final List<Config.PropertyChangeRegistration> propertyChangeRegistrations = new ArrayList<Config.PropertyChangeRegistration>();

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

	public void clearPropertyChangeListeners() {
		Iterator<Config.PropertyChangeRegistration> iterator = propertyChangeRegistrations.iterator();
		while (iterator.hasNext()) {
			iterator.next().removeListener();
			iterator.remove();
		}
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
		addPropertyChangeListener(configurable.key(), new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				checkbox.setSelected((Boolean) evt.getNewValue());
			}
		});

		addLabel(configurable);
		addComponent(configurable, checkbox);

		return checkbox;
	}

	public JComboBox createComboBox(final Field field, Configurable configurable,
                                    Class<? extends ListProvider> providerClass) {

		final ListProvider<Object> provider = ListProviderFactory.getProvider( providerClass );

		final JComboBox comboBox = new JComboBox();
		comboBox.setBorder(null);
		for (Object object : provider.getAllDisplayableItems()) {
			comboBox.addItem(object);
		}
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				config.setValue(field, provider.getValueFromDisplayableItem( comboBox.getSelectedItem().toString() ));
			}
		});
		comboBox.setSelectedItem(provider.getDisplayableItemFromValue( config.getValue( field ) ));

		addPropertyChangeListener(configurable.key(), new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				comboBox.setSelectedItem(provider.getDisplayableItemFromValue( evt.getNewValue() ));
			}
		});

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

		addPropertyChangeListener(configurable.key(), new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				slider.setValue((Integer) evt.getNewValue());
			}
		});

		addLabel(configurable);
		addComponent(configurable, slider);

		return slider;
	}

	public JSlider createFloatSlider(final Field field, Configurable configurable, final FloatInterval interval) {
		final JSlider slider = new JSlider(0, interval.precision(), getSliderValueFromFloat(interval.min(), interval.max(), interval.precision(), (Float) config.getValue(field)));
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				float inter = interval.max() - interval.min();
				float percent = slider.getValue() / Float.valueOf(interval.precision());
				float value = interval.min() + (inter * percent);
				config.setValue(field, value);
			}
		});

		addPropertyChangeListener(configurable.key(), new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				slider.setValue(getSliderValueFromFloat(interval.min(), interval.max(), interval.precision(), (Float) evt.getNewValue()));
			}
		});

		addLabel(configurable);
		addComponent(configurable, slider);

		return slider;
	}

	private int getSliderValueFromFloat(float min, float max, int precision, float value) {
		float inter = max - min;
		return (int) (((value - min) / inter) * precision);
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

		addPropertyChangeListener(configurable.key(), new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				textField.setValue(evt.getNewValue());
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

	public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
		propertyChangeRegistrations.add(config.addPropertyChangeListener(property, listener));
	}
}
