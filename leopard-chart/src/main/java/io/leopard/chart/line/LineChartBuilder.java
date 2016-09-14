package io.leopard.chart.line;

import java.util.ArrayList;
import java.util.List;

public class LineChartBuilder {

	private List<LineData> list = new ArrayList<LineData>();

	public void add(String label, String color) {
		LineData line = new LineData();
		line.setLabel(label);
		line.setColor(color);
		list.add(line);
	}

	public List<LineData> build() {
		// List<TrackTrendVO> list = new ArrayList<TrackTrendVO>();
		// list.add(this.getTrackTrendVO(range.getStartTime(), "#5ab1ef"));
		// list.add(this.getTrackTrendVO(range.getEndTime(), "#f5994e"));

		return list;
	}

}
