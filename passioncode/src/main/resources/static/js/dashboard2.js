function mychart2(sentence5){
		console.log("여기선 읽나~ sentence3 : ",sentence3);
		console.log("여기선 읽나~ sentence5 : ",sentence5);
	var aaaa=[	{ x: '2017-07-24 00:00:00', y: 100 },
				{ x: '2017-07-25 00:00:00', y: 0 },
				{ x: '2017-07-26 00:00:00', y: 29 },
				{ x: '2017-07-27 00:00:00', y: 0 },
				{ x: '2017-07-28 00:00:00', y: 0 },
				{ x: '2017-07-29 00:00:00', y: 0 },
				{ x: '2017-07-30 00:00:00', y: -0 }];
	console.log("1호기"+sentence);
	console.log("2호기"+sentence2);
	var bbb=sentence;
	
	bbb.forEach(function(item){
		console.log("반복"+item.x+"  "+item.y);
	});
	console.log("찍을데이터"+aaaa);
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
			minimum: -100,
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
			dataPoints: aaaa
		},
		{
			name: "Martha Vineyard",
			type: "spline",
			yValueFormatString: "#0.## °C",
			showInLegend: true,
			dataPoints: [
				{ x: new Date('2017-07-24 00:00:00'), y: 90 },
				{ x: new Date('2017-07-25 00:00:00'), y: 0 },
				{ x: new Date('2017-07-26 00:00:00'), y: 21 },
				{ x: new Date('2017-07-27 00:00:00'), y: 0 },
				{ x: new Date('2017-07-28 00:00:00'), y: 17 },
				{ x: new Date('2017-07-29 00:00:00'), y: 0 },
				{ x: new Date('2017-07-30 00:00:00'), y: 20 }
			]
		},
		{
			name: "Nantucket",
			type: "spline",
			yValueFormatString: "#0.## °C",
			showInLegend: true,
			dataPoints: [
				{ x: new Date('2017-07-24 00:00:00'), y: 10 },
				{ x: new Date('2017-07-25 00:00:00'), y: 0 },
				{ x: new Date('2017-07-26 00:00:00'), y: 20 },
				{ x: new Date('2017-07-27 00:00:00'), y: 0 },
				{ x: new Date('2017-07-28 00:00:00'), y: 15 },
				{ x: new Date('2017-07-29 00:00:00'), y: 0 },
				{ x: new Date('2017-07-30 00:00:00'), y: 10 }
			]
		},
		{
			name: "테스트",
			type: "spline",
			yValueFormatString: "#0.## °C",
			showInLegend: true,
			dataPoints: [
				{ x: new Date('2017-07-24 00:00:00'), y: 100 },
				{ x: new Date('2017-07-25 00:00:00'), y: 0 },
				{ x: new Date('2017-07-26 00:00:00'), y: 20 },
				{ x: new Date('2017-07-27 00:00:00'), y: 10 },
				{ x: new Date('2017-07-28 00:00:00'), y: 0 },
				{ x: new Date('2017-07-29 00:00:00'), y: 150 },
				{ x: new Date('2017-07-30 00:00:00'), y: 20 }
			]
		}
		]
	});
	console.log("바보다");
	console.log(chart);
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