package ambibright.config;

import java.util.ArrayList;
import java.util.List;

import ambibright.engine.squareAnalyser.SquareAnalyser;

public class SquareAnalyserProvider implements ListProvider {
	private static final List<Object> list;
	static {
		list = new ArrayList<Object>();
		for (SquareAnalyser c : SquareAnalyser.values()) {
			list.add(c);
		}
	}

	@Override
	public List<Object> getPossibleItems() {
		return list;
	}

	@Override
	public Object getValueFromItem(Object item) {
		return item;
	}

	@Override
	public Object getItemFromValue(Object value) {
		return value;
	}
}
