function mychart5(){
	var chart = new CanvasJS.Chart("chartContainer", {
		animationEnabled: true,
		title:{
			text: "대분류별 재고금액"
		},
		axisX: {
			valueFormatString: "YYYY-MM-DD"
		},
		axisY: {
			title: "재고금액",
			suffix: " 원",
			minimum: -50000,
			maximum: 350000,
		},
		legend:{
			cursor: "pointer",
			fontSize: 16,
			itemclick: toggleDataSeries
		},
		toolTip:{
			shared: true
		},
		data: [{
			name: "보드",
			type: "spline",
			yValueFormatString: "###,###,### 원",
			showInLegend: true,
			dataPoints: [
				{ x: new Date('2017-06-01 00:00:00'), y: 0 },
				{ x: new Date('2017-06-02 00:00:00'), y: 10000 },
				{ x: new Date('2017-06-03 00:00:00'), y: 0 },
				{ x: new Date('2017-06-04 00:00:00'), y: 20000 },
				{ x: new Date('2017-06-05 00:00:00'), y: 0 },
				{ x: new Date('2017-06-06 00:00:00'), y: 0 },
				{ x: new Date('2017-06-07 00:00:00'), y: 40000 },
				{ x: new Date('2017-06-08 00:00:00'), y: 0 },
				{ x: new Date('2017-06-09 00:00:00'), y: 0 },
				{ x: new Date('2017-06-10 00:00:00'), y: 0 }
			]
		},
		{
			name: "철",
			type: "spline",
			yValueFormatString: "###,###,### 원",
			showInLegend: true,
			dataPoints: [
				{ x: new Date('2017-06-01 00:00:00'), y: 0 },
				{ x: new Date('2017-06-02 00:00:00'), y: 0 },
				{ x: new Date('2017-06-03 00:00:00'), y: 100000 },
				{ x: new Date('2017-06-04 00:00:00'), y: 25000 },
				{ x: new Date('2017-06-05 00:00:00'), y: 0 },
				{ x: new Date('2017-06-06 00:00:00'), y: 30000 },
				{ x: new Date('2017-06-07 00:00:00'), y: 20000 },
				{ x: new Date('2017-06-08 00:00:00'), y: 0 },
				{ x: new Date('2017-06-09 00:00:00'), y: 0 },
				{ x: new Date('2017-06-10 00:00:00'), y: 0 }
			]
		},
		{
			name: "플라스틱",
			type: "spline",
			yValueFormatString: "###,###,### 원",
			showInLegend: true,
			dataPoints: [
				{ x: new Date('2017-06-01 00:00:00'), y: 0 },
				{ x: new Date('2017-06-02 00:00:00'), y: 10000 },
				{ x: new Date('2017-06-03 00:00:00'), y: 200000 },
				{ x: new Date('2017-06-04 00:00:00'), y: 0 },
				{ x: new Date('2017-06-05 00:00:00'), y: 40000 },
				{ x: new Date('2017-06-06 00:00:00'), y: 0 },
				{ x: new Date('2017-06-07 00:00:00'), y: 0 },
				{ x: new Date('2017-06-08 00:00:00'), y: 30000 },
				{ x: new Date('2017-06-09 00:00:00'), y: 0 },
				{ x: new Date('2017-06-10 00:00:00'), y: 0 }
			]
		},
		{
			name: "목재",
			type: "spline",
			yValueFormatString: "###,###,### 원",
			showInLegend: true,
			dataPoints: [
				{ x: new Date('2017-06-01 00:00:00'), y: 10000 },
				{ x: new Date('2017-06-02 00:00:00'), y: 0 },
				{ x: new Date('2017-06-03 00:00:00'), y: 20000 },
				{ x: new Date('2017-06-04 00:00:00'), y: 0 },
				{ x: new Date('2017-06-05 00:00:00'), y: 0 },
				{ x: new Date('2017-06-06 00:00:00'), y: 300000 },
				{ x: new Date('2017-06-07 00:00:00'), y: 0 },
				{ x: new Date('2017-06-08 00:00:00'), y: 25000 },
				{ x: new Date('2017-06-09 00:00:00'), y: 0 },
				{ x: new Date('2017-06-10 00:00:00'), y: 0 }
			]
		}
		]
	});
	chart.render();
	
	function toggleDataSeries(e){
		if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
			e.dataSeries.visible = false;
		}
		else{
			e.dataSeries.visible = true;
		}
		chart.render();
	}
}