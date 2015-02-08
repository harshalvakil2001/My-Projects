var points = {};
var tables = {};
var keys = [];
var units = {
	tableWidth : 250,
	tableHeight : 20,
	textHeight : 15,
	textIndent : 10,
	fontSize : 10
}
var ctx = null;
function addToPoints(tableName, col, x, y) {
	col = col.substr(0, col.indexOf(":") - 1);
	var str = tableName + "-" + col
	if (null == points[str])
		points[str] = [];
	points[str].push({
		"x" : x,
		"y" : y + units.textHeight
	});
	points[str].push({
		"x" : x + units.tableWidth,
		"y" : y + units.textHeight
	});
}

function cropImageFromCanvas(ctx, canvas) {
	var w = canvas.width, h = canvas.height, pix = {
		x : [],
		y : []
	}, imageData = ctx.getImageData(0, 0, canvas.width, canvas.height), x, y, index;

	for (y = 0; y < h; y++) {
		for (x = 0; x < w; x++) {
			index = (y * w + x) * 4;
			if (imageData.data[index + 3] > 0) {
				pix.x.push(x);
				pix.y.push(y);
			}
		}
	}
	pix.x.sort(function(a, b) {
		return a - b
	});
	pix.y.sort(function(a, b) {
		return a - b
	});
	var n = pix.x.length - 1;

	w = pix.x[n] - pix.x[0];
	h = pix.y[n] - pix.y[0];
	var cut = ctx.getImageData(pix.x[0], pix.y[0], w, h);

	canvas.width = w + 20;
	canvas.height = h + 20;
	ctx.putImageData(cut, 10, 10);
}

$(document).ready(
		function() {

			document.getElementById('download').addEventListener('click',
					function() {
						downloadCanvas(this, 'myCanvas', 'diagram.png');
					}, false);

			var c = document.getElementById("myCanvas");
			ctx = c.getContext("2d");

			var tableLength = json.tables.length;
			tablePoints = getTablePoints(tableLength);

			for (var i = 0; i < tableLength; i++) {
				table = json.tables[i];
				tableName = table.tableName;
				var cols = [];
				$.each(table.columns, function(key, value) {
					value = value.replace("_IGNORECASE", "")
					cols.push(key + " : " + value);
				});
				if (null != table.relations && table.relations.length > 0) {
					for (var j = 0; j < table.relations.length; j++) {
						var relation = table.relations[j];
						if (null != relation) {
							var str = relation.pkTableName + "-"
									+ relation.pkColumnName;
							keys.push(str);
							var str = relation.fkTableName + "-"
									+ relation.fkColumnName;
							keys.push(str);
						}
					}
				}
				createTable(tablePoints[i], tableName, cols);
			}

			if (keys.length > 0) {
				for (var i = 0; i < keys.length; i = i + 2) {
					pt1arr = points[keys[i]];
					pt2arr = points[keys[i + 1]];

					var x0x0, x0x1, x1x1, x1x0, txt1 = {}, txt2 = {};
					x0x0 = Math.abs(pt1arr[0].x - pt2arr[0].x);
					x0x1 = Math.abs(pt1arr[0].x - pt2arr[1].x);
					x1x1 = Math.abs(pt1arr[1].x - pt2arr[1].x);
					x1x0 = Math.abs(pt1arr[1].x - pt2arr[0].x);

					var num = Math.min(x0x0, x0x1, x1x1, x1x0);
					if (num == x0x0 || num == x0x1) {
						pt1 = pt1arr[0];
					} else {
						pt1 = pt1arr[1];
					}

					if (num == x0x0 || num == x1x0) {
						pt2 = pt2arr[0];
					} else {
						pt2 = pt2arr[1];
					}

					table1 = keys[i].substr(0, keys[i].indexOf("-"));
					table2 = keys[i + 1].substr(0, keys[i + 1].indexOf("-"));

					

					if (pt1.x == tables[table1].x1) 
						if (pt1.x < tables[table1].x2)
							txt1.x = pt1.x - 15;
						else
							txt1.x = pt1.x + 10;
					else
						if (pt1.x < tables[table1].x1)
							txt1.x = pt1.x - 15;
						else
							txt1.x = pt1.x +10;
					
					console.log("pt2.x = " + pt2.x);
					console.log("tables[table2].x1 = " + tables[table2].x1);
					console.log("tables[table2].x2 = " + tables[table2].x2);

					if (pt2.x == tables[table2].x1) 
						if (pt2.x < tables[table2].x2)
							txt2.x = pt2.x - 15;
						else
							txt2.x = pt2.x + 10;
					else
						if (pt2.x < tables[table2].x1)
							txt2.x = pt2.x - 15;
						else
							txt2.x = pt2.x +10;

					drawArrow(ctx, pt1.x, pt1.y, pt2.x, pt2.y - 5, 4, 2);
					ctx.fillStyle = "red";
					ctx.fillText("1", txt1.x, pt1.y);
					ctx.fillText("n", txt2.x, pt2.y);
					ctx.fillStyle = "black";

				}
			}
			cropImageFromCanvas(ctx, c);
			ctx.fillStyle = "#eeeeee";
			ctx.globalCompositeOperation = "destination-over";
			ctx.fillRect(0, 0, c.width, c.height);

		});

function getTablePoints(tableName) {
	var arr = [ {
		x : 10,
		y : 350
	}, {
		x : 300,
		y : 10
	}, {
		x : 700,
		y : 350
	}, {
		x : 400,
		y : 700
	} ];

	if (tableName >= 5) {
		arr = [ {
			x : 1,
			y : 370
		}, {
			x : 273,
			y : 19
		}, {
			x : 679,
			y : 131
		}, {
			x : 731,
			y : 591
		}, {
			x : 247,
			y : 751
		}, {
			x : 137,
			y : 95
		}, {
			x : 581,
			y : 51
		}, {
			x : 727,
			y : 135
		}, {
			x : 669,
			y : 649
		}, {
			x : 111,
			y : 651
		} ];
	}
	return arr;
}

function createTable(obj, tableName, cols) {
	x = obj.x, y = obj.y;
	tables[tableName] = {
		x1 : x,
		x2 : x + units.tableWidth
	}
	ctx.font = "bold " + units.fontSize + "pt sans-serif ";
	ctx.fillText(tableName, x + units.textIndent, y + units.textHeight);
	ctx.font = units.fontSize + "pt sans-serif ";
	ctx.strokeRect(x, y, units.tableWidth, units.tableHeight);
	y = y + units.tableHeight;

	if (null != cols && cols.length > 0) {
		for (var i = 0; i < cols.length; i++) {
			var col = cols[i];
			ctx.fillText(col, x + units.textIndent, y + units.textHeight);
			ctx.strokeRect(x, y, units.tableWidth, units.tableHeight);
			addToPoints(tableName, col, x, y);
			y = y + units.tableHeight;
		}
	}
}

function downloadCanvas(link, canvasId, filename) {
	link.href = document.getElementById(canvasId).toDataURL();
	link.download = filename;
}