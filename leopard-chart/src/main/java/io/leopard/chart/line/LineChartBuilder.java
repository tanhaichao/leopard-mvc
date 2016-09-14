package io.leopard.chart.line;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LineChartBuilder {

	private List<LineData> list = new ArrayList<LineData>();

	public void add(String label, String color, LineDataProvider provider) {
		LineData line = new LineData();
		line.setLabel(label);
		line.setColor(color);
		try {
			line.setData(this.toData(provider.execute()));
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		list.add(line);
	}

	protected List<Object[]> toData(List<?> dataList) throws Exception {
		if (dataList == null) {
			return null;
		}
		List<Object[]> list = new ArrayList<Object[]>();
		for (Object obj : dataList) {
			Field[] fields = obj.getClass().getDeclaredFields();
			Object[] data = new Object[fields.length];
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				data[i] = field.get(obj);
			}
		}
		return list;
	}

	public List<LineData> build() {
		// List<TrackTrendVO> list = new ArrayList<TrackTrendVO>();
		// list.add(this.getTrackTrendVO(range.getStartTime(), "#5ab1ef"));
		// list.add(this.getTrackTrendVO(range.getEndTime(), "#f5994e"));
		return list;
	}

}
