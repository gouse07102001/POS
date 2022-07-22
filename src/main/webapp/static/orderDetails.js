var order_data = []
function getOrdersUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orderDetail";
}

function getOrdersList() {
	var url = getOrdersUrl();
	$.ajax({
		url : url,
		type : 'GET',
		success : function(data) {
			order_data = data;
			console.log(data)
			getSP(0);
		},
		error : function(response) {
			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
		}
	});
}

function getSP(i) {
	if (i == order_data.length) {
		displayOrdersList(order_data);
		return;
	}
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url = baseUrl + '/api/order/detail/' + order_data[i].orderId;
	$.ajax({
		url : url,
		type : 'GET',
		success : function(response) {
			var sum = 0;
			for ( var j in response) {
				sum += ((response[j].quantity) * (response[j].sellingPrice));
			}
			order_data[i].sellingPrice = sum;
			getSP(i + 1);
		},
		error : function(response) {
			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
		}
	});

}



/*
function ordersFilter() {
	$("#order-filter").on("keyup", function() {
		var value = $(this).val();
		$("#orders-table-body tr").filter(function() {
			$(this).toggle($(this).text().indexOf(value) > -1)
		});
	});
}

function filterByDates() {
	var url = getOrdersUrl();
	$.ajax({
		url : url,
		type : 'GET',
		success : function(data) {

			displayFilteredOrdersList(data);
		},
		error : function(response) {
			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
		}
	});
}
*/
/*
function displayFilteredOrdersList(data) {
	var startDate = $('#inputStartDate').val();
	if (startDate === "")
		startDate = "";
	else
		startDate = startDate.substring(8, 10) + startDate.substring(4, 8)
				+ startDate.substring(0, 4) + " 00:00";
	var endDate = $('#inputEndDate').val();
	if (endDate === "")
		endDate = "";
	else
		endDate = endDate.substring(8, 10) + endDate.substring(4, 8)
				+ endDate.substring(0, 4) + " 59:59";
	var id = $('#order-filter').val();
	var $tbody = $('#orders-table').find('tbody');
	$tbody.empty();
	var j=0;
	for (var i = data.length - 1; i >= 0; i--) {
		var e = order_data[i];
		var buttonHtml = ' <a class="btn btn-success" href="/pos/api/order/invoice/'
				+ e.orderId + '" target="_blank" id="invoice">Generate Invoice</a>&nbsp;'
				+ '<a class="btn btn-success" href="/pos/ui/order/view/'
                + e.orderId + '">View Order</a>';
		// buttonHtml.href="@{/api/order/invoice}";
		if ((compareDates(startDate, e.time) == -1 && compareDates(e.time,
				endDate) == -1)
				|| filterorder(e.orderId, id) === -1) {

			var row = '<tr>'
			        + '<td>' + (Number(j++)+1) + '</td>'
					+ '<td>' + e.orderId   + '</td>'
					+ '<td>' + e.time + '</td>'
					+ '<td>' + (Math.round(e.sellingPrice * 100) / 100).toFixed(2) + '</td>'
					+ '<td>' + buttonHtml + '</td>' 
					+ '</tr>';
			$tbody.append(row);
		}
	}
}

function compareDates(a, b) {
	if (a === "" || b === "")
		return 0;
	var ans = a < b ? -1 : (a > b ? 1 : 0);
	return ans;
}
function filterorder(a, b) {
	if (b === "" || b === undefined || Number(a) === Number(b))
		return -1;
	else
		return 1;
}*/

// INITIALIZATION CODE
function init() {
	$('#orders-table').on('submit', e => e.preventDefault())
}

function errorDisplay(template, message){
	var $errorbar = $('#status-bar');
	var text = 'Success! ';
	if(template === 'danger') {
		text = 'Failed! ';
		Toastify({
            text: text + " " + message,
            close: true,
            style: {
                background: "linear-gradient(to right, #ff0000, #c75858)",
              },
            duration: -1
        }).showToast();
	}
	else{
        Toastify({
            text: text + " " + message,
            close: false,
            style: {
                background: "linear-gradient(to right, #00ff11, #60e069)",
              },
            duration: 3000
        }).showToast();
	}

	}

	/*function just(){
        var date = new Date().toISOString().substring(0, 10),
                field = document.querySelector('#inputEndDate');
            field.value = date;

            $("#inputEndDate").val(field.value);
            var newDate = new Date(new Date().getTime() - (60*60*24*7*1000)).toISOString().substring(0, 10)

            field = document.querySelector('#inputStartDate');
                    field.value = newDate;
            $("#inputStartDate").val(field.value);
    }*/

function highlight()
{
	document.getElementById("viewOrder-link").style.fontWeight = 900;
	
}
$(document).ready(highlight);
$(document).ready(init);
$(document).ready(getOrdersList);
//$(document).ready(ordersFilter);
//$(document).ready(just);

function displayOrdersList(tabledata) {
	console.log(tabledata)
	var $tbody = $('#orders-table').find('tbody');
	$tbody.empty();
	var state = {
		'querySet': tabledata,

		'page': 1,
		'rows': 5,
		'window': 5,
	}

	buildTable()

	function pagination(querySet, page, rows) {

		var trimStart = (page - 1) * rows
		var trimEnd = trimStart + rows

		var trimmedData = querySet.slice(trimStart, trimEnd)

		var pages = Math.ceil(querySet.length / rows);

		return {
			'querySet': trimmedData,
			'pages': pages,
		}
	}

	function pageButtons(pages) {
		var wrapper = document.getElementById('pagination-wrapper')

		wrapper.innerHTML = ``
		console.log('Pages:', pages)

		var maxLeft = (state.page - Math.floor(state.window / 2))
		var maxRight = (state.page + Math.floor(state.window / 2))

		if (maxLeft < 1) {
			maxLeft = 1
			maxRight = state.window
		}

		if (maxRight > pages) {
			maxLeft = pages - (state.window - 1)

			if (maxLeft < 1) {
				maxLeft = 1
			}
			maxRight = pages
		}



		for (var page = maxLeft; page <= maxRight; page++) {
			wrapper.innerHTML += `<button value=${page} class="page btn btn-md ${page == state.page ? 'btn-primary' : 'btn-secondary'}" id="page-button-${page}" style="margin: 0 2px;">${page}</button>`
		}

		if (state.page != 1) {
			wrapper.innerHTML = `<button value=${1} class="page btn btn-md btn-primary" style="margin: 0 2px;">&#171; First</button>` + wrapper.innerHTML
		}

		if (state.page != pages) {
			wrapper.innerHTML += `<button value=${pages} class="page btn btn-md btn-primary" style="margin: 0 2px;">Last &#187;</button>`
		}

		$('.page').on('click', function() {
			$tbody.empty()

			state.page = Number($(this).val())

			buildTable()
		})

	}



	
function buildTable(data){
	var table = $('#orders-table')
		var data = pagination(state.querySet, state.page, state.rows)
		var myList = data.querySet
	$tbody.empty();
	for(var i in myList){
		var e = myList[i];
		var buttonHtml = ' <a class="btn btn-success" href="/pos/api/order/invoice/'
				+ e.orderId + '" target="_blank" id="invoice">Download Invoice</a>&nbsp;'
				+ '<a class="btn btn-success" style="color:white" onclick= "displayViewOrder(' + e.orderId
				+ ')"">View Order</a>';
		var row = '<tr>'
					+ '<td>' + e.orderId   + '</td>'
					+ '<td>' + e.time + '</td>'
					+ '<td>' + (Math.round(e.sellingPrice * 100) / 100).toFixed(2) + '</td>'
					+ '<td>' + buttonHtml + '</td>' 
					+ '</tr>';
			$tbody.append(row);
	}


	pageButtons(data.pages)
	}
}


function displayViewOrder(orderId){
	getOrder(orderId)
	$('#order-view-modal').modal('toggle');
	
}

///  ORDER VIEW CODES
function getOrderUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order/detail/";
}
order_data=[];
total = 0;
orderIdTemp=0

function getOrder(orderId){
	orderIdTemp = orderId
	total=0;
	var id = orderId
	$('#id').val(id);
	var t="/pos/api/order/invoice/"+id;
	document.getElementById("invoice").href=t;
	var url = getOrderUrl() + id;
	console.log(url);
	$.ajax({
		url : url,
		type : 'GET',
		success : function(data) {
			order_data=data;
			console.log(order_data)
			getBarcodes(0);
		},
		error : function(response) {
			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
		}
	});
}

function getBarcodes(i){
	if(i==order_data.length){
		displayOrderList(order_data);
		return;
	}
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url = baseUrl + '/api/products/' + order_data[i].productId;
	$.ajax({
		url : url,
		type : 'GET',
		success : function(response) {
			order_data[i].barcode = response.barcode;
			getBarcodes(i+1);
		},
		error : function(response) {
			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
		}
	});
}

function displayOrderList(order_data) {
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	console.log(order_data)
	for (var i in order_data) {

		var e = order_data[i];
		total += Number(e.quantity*e.sellingPrice);
		var row = '<tr>'
				+ '<td>' + e.barcode + '</td>'
				+ '<td>' + e.quantity + '</td>' 
				+ '<td>' + (Math.round(e.sellingPrice * 100) / 100).toFixed(2) + '</td>'
				+ '<td>' + (Math.round(Number(e.quantity*e.sellingPrice) * 100) /100).toFixed(2) + '</td>'
				+ '</tr>';
		$tbody.append(row);

	}
	$('#total').val((Math.round(total * 100) / 100).toFixed(2));
	getTime();
}

function getTime(){
	//var id = window.location.href.split("/").reverse()[0];
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url = baseUrl + '/api/orderDetail/' + orderIdTemp;
	$.ajax({
		url : url,
		type : 'GET',
		success : function(response) {
			$('#time').val(response.time);
		},
		error : function(response) {
			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
		}
	});
}


