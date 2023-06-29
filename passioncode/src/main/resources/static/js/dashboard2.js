function mychart2(){
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
			minimum: -10,
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
			name: "Myrtle Beach",
			type: "spline",
			yValueFormatString: "###,###,### 원",
			showInLegend: true,
			dataPoints: [
				{ x: new Date(2017,6,24), y: 5000 },
				{ x: new Date(2017,6,25), y: 0 },
				{ x: new Date(2017,6,26), y: 29 },
				{ x: new Date(2017,6,27), y: 0 },
				{ x: new Date(2017,6,28), y: 0 },
				{ x: new Date(2017,6,29), y: 0 },
				{ x: new Date(2017,6,30), y: -0 }
			]
		},
		{
			name: "Martha Vineyard",
			type: "spline",
			yValueFormatString: "#0.## °C",
			showInLegend: true,
			dataPoints: [
				{ x: new Date(2017,6,24), y: 20 },
				{ x: new Date(2017,6,25), y: 0 },
				{ x: new Date(2017,6,26), y: 0 },
				{ x: new Date(2017,6,27), y: 25 },
				{ x: new Date(2017,6,28), y: 0 },
				{ x: new Date(2017,6,29), y: 25 },
				{ x: new Date(2017,6,30), y: 0 }
			]
		},
		{
			name: "Nantucket",
			type: "spline",
			yValueFormatString: "#0.## °C",
			showInLegend: true,
			dataPoints: [
				{ x: new Date(2017,6,24), y: 22 },
				{ x: new Date(2017,6,25), y: 19 },
				{ x: new Date(2017,6,26), y: 23 },
				{ x: new Date(2017,6,27), y: 24 },
				{ x: new Date(2017,6,28), y: 24 },
				{ x: new Date(2017,6,29), y: 23 },
				{ x: new Date(2017,6,30), y: 23 }
			]
		},
		{
			name: "테스트",
			type: "spline",
			yValueFormatString: "#0.## °C",
			showInLegend: true,
			dataPoints: [
				{ x: new Date(2017,6,24), y: 20 },
				{ x: new Date(2017,6,25), y: 55 },
				{ x: new Date(2017,6,26), y: 51 },
				{ x: new Date(2017,6,27), y: 28 },
				{ x: new Date(2017,6,28), y: 22 },
				{ x: new Date(2017,6,29), y: 13 },
				{ x: new Date(2017,6,30), y: 19 }
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