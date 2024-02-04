package io.portfel.model.chart;

import java.util.List;

public record ChartRecord(String name, List<? extends ChartValue> values, List<String> legends) {
}
