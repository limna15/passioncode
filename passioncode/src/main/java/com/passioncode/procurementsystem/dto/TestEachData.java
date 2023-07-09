package com.passioncode.procurementsystem.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TestEachData {
	private String name;
	private String type;
	private String yValueFormatString;
	private Boolean showInLegend;
	private List<TestXYObject> dataPoints;
}
