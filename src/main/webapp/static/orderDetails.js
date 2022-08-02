var order_data = []
function getOrdersUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order-details";
}

function getOrdersList() {
	var url = getOrdersUrl();
	$.ajax({
		url : url,
		type : 'GET',
		success : function(data) {
			order_data = data;
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





// INITIALIZATION CODE
function init() {
	$('#orders-table').on('submit', e => e.preventDefault())
}

function errorDisplay(template, message){
	var $errorbar = $('#status-bar');
	var text = '	Success    ';
	if(template === 'danger') {
		text = 'Failed! ';
		Toastify({
            text:message,
            close: true,
            style: {
                background: "linear-gradient(to right, #ff0000, #c75858)",
              },
            duration: -1
        }).showToast();
	}
	else{
        Toastify({
            text: text,
            close: false,
            style: {
                background: "linear-gradient(to right, #00ff11, #60e069)",
              },
            duration: 3000
        }).showToast();
	}

	}

function highlight()
{
	document.getElementById("viewOrder-link").style.fontWeight = 900;
	
}
$(document).ready(highlight);
$(document).ready(init);
$(document).ready(getOrdersList);

function displayOrdersList(tabledata) {
	var $tbody = $('#orders-table').find('tbody');
	$tbody.empty();
	var state = {
		'querySet': tabledata,

		'page': 1,
		'rows': 5,
		'window': 3,
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
			wrapper.innerHTML = `<button value=${1} class="page btn btn-md btn-secondary" style="margin: 0 2px;">&#171; First</button>` + wrapper.innerHTML
		}

		if (state.page != pages) {
			wrapper.innerHTML += `<button value=${pages} class="page btn btn-md btn-secondary" style="margin: 0 2px;">Last &#187;</button>`
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
		var buttonHtml = ' <a class="btn btn-primary" href="/pos/api/order/invoice/'
				+ e.orderId + '" target="_blank" id="invoice">Download Invoice</a>&nbsp;'
				+ '<a class="btn btn-primary" style="color:white" onclick= "displayViewOrder(' + e.orderId
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
	$.ajax({
		url : url,
		type : 'GET',
		success : function(data) {
			order_data=data;
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
	for (var i in order_data) {

		var e = order_data[i];
		total += Number(e.quantity*e.sellingPrice);
		var row = '<tr>'
				+ '<td><div style="width:70px;white-space: nowrap;  overflow: hidden; text-overflow: ellipsis;"data-toggle="tooltip" data-placement="bottom"title='+e.barcode+'>' + e.barcode + '</div></td>'
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
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url = baseUrl + '/api/order-details/' + orderIdTemp;
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


$(function () {
  $('[data-toggle="tooltip"]').tooltip()
})



