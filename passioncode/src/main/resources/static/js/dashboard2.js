//대분류 차트 보여주는 함수
function mychart(){
	var chart = new CanvasJS.Chart("chartContainer1", {
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
			maximum: maxinumLC,
		},
		legend:{
			cursor: "pointer",
			fontSize: 16,
			itemclick: toggleDataSeries
		},
		toolTip:{
			shared: true
		},
		data: eachDataLCList
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

//중분류 차트 보여주는 차트
function mychart2(){
	var chart = new CanvasJS.Chart("chartContainer2", {
		animationEnabled: true,
		title:{
			text: "중분류별 재고금액"
		},
		axisX: {
			valueFormatString: "YYYY-MM-DD"
		},
		axisY: {
			title: "재고금액",
			suffix: " 원",
			minimum: -50000,
			maximum: maxinumMC,
		},
		legend:{
			cursor: "pointer",
			fontSize: 16,
			itemclick: toggleDataSeries
		},
		toolTip:{
			shared: true
		},
		data: eachDataMCList
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
