var keys = {};
var units = {
	tableWidth : 200,
	tableHeight : 30,
	textHeight : 20,
	textIndent : 10
}
var con = null;
function addToKeys(tableName, col, x, y) {
	var str = ("to-" + tableName + "-" + col).toLowerCase()
	keys[str] = {
		"x" : x,
		"y" : y + units.textHeight
	};
}
function addFromKeys(tableName, col, x, y) {
	var str = ("from-" + tableName + "-" + col).toLowerCase()
	keys[str] = {
		"x" : x + units.tableWidth,
		"y" : y + units.textHeight
	};
}

$(document).ready(function() {
	
	var json = $.ajax("json.txt");
	
	var c = document.getElementById("myCanvas");
	con = c.getContext("2d");
	tableName = "Student";
	cols = [ "ID : Number", "Name : String" ];
	createTable(10, 10, tableName, cols);
});

function createTable(x, y, tableName, cols) {
	con.font = "bold 12pt sans-serif ";
	con.fillText(tableName, x + units.textIndent, y + units.textHeight);
	con.font = "12pt sans-serif ";
	con.strokeRect(x, y, units.tableWidth, units.tableHeight);
	y = y + units.tableHeight;

	if (null != cols && cols.length > 0) {
		for (var i = 0; i < cols.length; i++) {
			var col = cols[i];
			con.fillText(col, x + units.textIndent, y + units.textHeight);
			con.strokeRect(x, y, units.tableWidth, units.tableHeight);
			addToKeys(tableName, col, x, y);
			addFromKeys(tableName, col, x, y)
			y = y + units.tableHeight;
		}
	}
}