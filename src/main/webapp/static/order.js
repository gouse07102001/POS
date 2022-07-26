var orderItemsData = [];
grandTotal = 0;
function getOrderItemUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getProductUrl() {
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function postToBackend(){
	var url = $("meta[name=baseUrl]").attr("content") + "/api/order-details";
	var json = JSON.stringify(orderItemsData)
	console.log(json)
	/*if(orderItemsData.length == 0){
		return false
	}*/
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function() {
			getFromBackend()
			return true;	
		},
		error: function(response) {
			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
			return false;
		}
	});
	return false;
}
function getFromBackend(){
	var url = $("meta[name=baseUrl]").attr("content") + "/api/order-details/list";
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			orderItemsData = []
			orderItemsData = data
			if(orderItemsData.length >0){
				document.getElementById("submit").disabled = false;
			}
			displayOrderItemList()
			return true;
		},
		error: function(response) {
			var responseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', responseMessage);
			return false;
		}
	});
	return false;
}

function getOrderItem(event) {

	var barcode = $("#order-form input[name=barcode]").val();

	if (barcode === null || barcode === "") {
		errorDisplay('danger', 'Barcode cannot be empty');
		return false;
	}
	var url = $("meta[name=baseUrl]").attr("content") + "/api/products/search/" + barcode;
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			getQuantity(barcode, data.productId,data.mrp);
			return true;
		},
		error: function(response) {

			var responseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', responseMessage);
			return false;
		}
	});
	return false;
}

function getQuantity(barcode,productId,mrp){
	var url = $("meta[name=baseUrl]").attr("content") + "/api/inventory/quantity/"+barcode;
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			validate(barcode,productId,mrp,data);
			return true;
		},
		error: function(response) {

			var responseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', responseMessage);
			return false;
		}
	});
	return false;
}

function checkOrderItem(barcode, productId) {
	var quantity = $("#order-form input[name=quantity]").val();
	quantity = Number(quantity);
	var sellingPrice = $("#order-form input[name=sellingPrice]").val();
	var idx = orderItemsData.findIndex(o => o.barcode === barcode);
	if (idx !== -1){
		errorDisplay('danger', "Product with given barcode already exist in cart please edit to modify the order item");
		}
	else {
		if (idx === -1)
			orderItemsData.push({ barcode, productId, quantity, sellingPrice});
		else {
			orderItemsData[idx].quantity = quantity;
		}
		document.getElementById("order-form").reset();
		$('#submit').prop('disabled', false);

	}
	addProductNamesOrder(0);
	return true;
}

function validate(barcode,productId,mrp,inventory) {
	var quantity = $("#order-form input[name=quantity]").val();
	if (quantity === null || quantity === "") {
		errorDisplay('danger', 'Quantity cannot be empty');
		return false;
	}
	quantity = Number(quantity);
	if (quantity <= 0) {
		errorDisplay('danger', 'Enter a quantity greater than 0');
		return false;
	}
	var check = (quantity - Math.floor(quantity)) !== 0;
	if (check) {
		errorDisplay('danger', 'enter a valid quantity');
		return false;
	}
	var sellingPrice = $("#order-form input[name=sellingPrice]").val();
	if (sellingPrice === null || sellingPrice === "") {
		errorDisplay('danger', 'Selling price cannot be empty');
		return false;
	}
	pattern = /^\d+(?:\.\d{1,2})?$/;
	if (!pattern.test(sellingPrice)) {
		errorDisplay('danger', 'Selling price cannot be negative');
		return false
	}
	if(sellingPrice > mrp){
		errorDisplay('danger', 'Selling price cannot be greater than MRP');
		return false
	}
	if(quantity > inventory){
		errorDisplay('danger', 'Inventory shortage!! Available inventory is '+inventory);
		return false
	}
	var url = $("meta[name=baseUrl]").attr("content") + "/api/products/" + productId;

	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			checkOrderItem(data.barcode, productId);
			return true;
		},
		error: function(response) {
			document.getElementById("order-form").reset();
			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
			return false;
		}
	});


}

function deleteOrderItemsData(){
	 var url = $("meta[name=baseUrl]").attr("content") + "/api/order-details/delete";
	 $.ajax({
		url: url,
		type: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		success: function(data) {		
			return true;
		},
		error: function(response) {
			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
			return false;
		}
		});
	return false
}

function createOrder(event) {
	var json = JSON.stringify(orderItemsData);
	console.log(orderItemsData)
	var url = getOrderItemUrl();
	console.log(url)
	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function(data) {
			$('#invoice').prop('disabled', false);
			var $tbody = $('#order-table').find('tbody');
			$tbody.empty();
			errorDisplay('success', 'Order confirmed with Id : ' + data.orderId);
			deleteOrderItemsData()
			orderItemsData = [];
			grandTotal = 0;
			var visibility = document.getElementsByClassName("totalAmount");
			visibility[0].style.visibility = "hidden";
			document.getElementById("submit").disabled = true;
			document.getElementById("view-orders").disabled = false;
			return true;
		},
		error: function(response) {

			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
			return false;
		}
	});

	return false;
}


function printInvoice(data) {
	window.open("/pos/api/order/invoice/" + data.orderId, '_blank');
}

function addOrder(event) {
	// Set the values to update
	var $form = $("#order-form");
	var json = toJson($form);
	var url = getOrderUrl();

	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
			'Content-Type': 'application/json'
		},
		success: function(response) {
			getOrderList();
			return true;
		},
		error: function(response) {
			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
			return false;
		}
	});

	return false;
}

var flag = 1
function updateOrderItem(event) {

	var barcode = $("#order-edit-form input[name=barcode]").val();
	var quantity = $("#order-edit-form input[name=quantity]").val();
	if (quantity === null || quantity === "") {
		errorDisplay('danger', 'Quantity cannot be empty');
		return false;
	}
	quantity = Number(quantity);
	if (quantity <= 0) {
		errorDisplay('danger', 'Enter a quantity greater than 0');
		return false;
	}
	var check = (quantity - Math.floor(quantity)) !== 0;
	if (check) {
		errorDisplay('danger', 'enter a valid quantity');
		return false;
	}
	var sellingPrice = $("#order-edit-form input[name=sellingPrice]").val();
	if (sellingPrice === null || sellingPrice === "") {
		errorDisplay('danger', 'Selling price cannot be empty');
		return false;
	}
	pattern = /^\d+(?:\.\d{1,2})?$/;
	if (!pattern.test(sellingPrice)) {
		errorDisplay('danger', 'Selling price cannot be negative');
		return false
	}

	var idx = orderItemsData.findIndex(o => o.barcode === barcode);
	var tempQuantity = orderItemsData[idx].quantity;
	var tempSP = orderItemsData[idx].sellingPrice;
	orderItemsData[idx].quantity = quantity;
	orderItemsData[idx].sellingPrice = sellingPrice;
	const cb = () => {
		const cb1 = () => {
			orderItemsData[idx].amount = quantity*sellingPrice;
			postToBackend();
			displayOrderItemList();
			$('#edit-order-modal').modal('toggle');
			errorDisplay('success', 'Order Item Updated');

			return true;
		}
		checkSP(orderItemsData[idx], tempQuantity, tempSP, idx, cb1);
	}
	checkQuantity(orderItemsData[idx], tempQuantity, tempSP, idx, cb);
}

function checkSP(data, tempQuantity, tempSP, idx, cb) {
	var url = $("meta[name=baseUrl]").attr("content") + "/api/products/search/" + data.barcode;
	var json = JSON.stringify(data);
	$.ajax({
		url: url,
		type: 'GET',
		success: function(response) {
			if (data.sellingPrice <= response.mrp) {
				cb()
				return true
			}
			else {
				errorDisplay('danger', 'Selling Price should be less than or equal to MRP');
				orderItemsData[idx].quantity = tempQuantity;
				orderItemsData[idx].sellingPrice = tempSP;
				return false
			}

		},
		error: function(response) {
			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
			return false

		}
	});
	return false

}

function checkQuantity(data, tempQuantity, tempSP, idx, cb) {
	var url = $("meta[name=baseUrl]").attr("content") + "/api/inventory/quantity/" + data.barcode;
	var json = JSON.stringify(data);

	$.ajax({
		url: url,
		type: 'GET',
		success: function(response) {
			if (data.quantity <= response) {
				cb()
				return true

			}
			else {
				errorDisplay('danger', 'Quantity exceeded');
				orderItemsData[idx].quantity = tempQuantity;
				orderItemsData[idx].sellingPrice = tempSP;
				return false
			}

		},
		error: function(response) {
			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
			return false

		}
	});
	return false

}


function getOrderItemList() {
	var url = getOrderItemUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			addProductNamesOrder(0);
			return true;
		},
		error: function(response) {
			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
			return false;
		}
	});
}

// UI DISPLAY METHODS
function deleteOrderItem(i) {
	grandTotal -= Number(orderItemsData[i].quantity * orderItemsData[i].sellingPrice);
	
	orderItemsData.splice(i, 1)
	//console.log(orderItemsData[i])
	postToBackend()
	errorDisplay('success', "Order Item Deleted");
	displayOrderItemList(1);
}

function addProductNamesOrder(i) {
	if (i == orderItemsData.length) {
		postToBackend();
		//displayOrderItemList();
		return true;
	}
	var url = getProductUrl() + "s/search/" + orderItemsData[i].barcode;
	$.ajax({
		url: url,
		type: 'GET',
		success: function(pojo) {
			
			orderItemsData[i].productName = pojo.productName;
			orderItemsData[i].productId= pojo.productId;
			orderItemsData[i].amount = (orderItemsData[i].sellingPrice) * (orderItemsData[i].quantity);
			//console.log(orderItemsData[i])
			addProductNamesOrder(i + 1);
		},
		error: function(response) {
			var reponseMessage = JSON.parse(response.responseText).message;
			errorDisplay('danger', reponseMessage);
		}
	});

}

function displayOrderItemList(flag = 0) {
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	if (orderItemsData.length == 0) {
		var visibility = document.getElementsByClassName("totalAmount");
		visibility[0].style.visibility = "hidden";
		document.getElementById("submit").disabled = true;
	}
	else {
		var visibility = document.getElementsByClassName("totalAmount");
		visibility[0].style.visibility = "visible";
	}

	for (var i in orderItemsData) {
		console.log(orderItemsData[i].productName)
		var buttonHtml = ' <button class="btn btn-outline-primary btn-sm"  onclick="displayOrderItem(\'' + i + '\')">Edit</button>';
		buttonHtml += ' <button class="btn btn-outline-danger btn-sm"  onclick="deleteOrderItem(\'' + i + '\')">Delete</button>';
		var row = '<tr>'
			+ '<td><div style="width:180px;white-space: nowrap;  overflow: hidden; text-overflow: ellipsis;"data-toggle="tooltip" data-placement="bottom"title='+orderItemsData[i].productName+'>' + orderItemsData[i].productName + '</div></td>'
			+ '<td><div style="width:180px;white-space: nowrap;  overflow: hidden; text-overflow: ellipsis;"data-toggle="tooltip" data-placement="bottom"title='+orderItemsData[i].barcode+'>' + orderItemsData[i].barcode + '</div></td>'
			+ '<td>' + orderItemsData[i].quantity + '</td>'
			+ '<td>' + (Math.round(orderItemsData[i].sellingPrice * 100) / 100).toFixed(2) + '</td>'
			+ '<td>' + (Math.round((orderItemsData[i].amount) * 100) / 100).toFixed(2) + '</td>'
			+ '<td>' + buttonHtml + '</td>'

			+ '</tr>';
		$tbody.append(row);
	}
	calculateTotal();
	$('#total').val((Math.round(grandTotal * 100) / 100).toFixed(2));
	return true;
}

function calculateTotal() {
	sum = 0;
	for (var i in orderItemsData) {
		var e = orderItemsData[i];
		sum += Number(e.quantity * e.sellingPrice);
	}
	grandTotal = sum;
}


function print() {
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	orderItemsData = [];
}

function displayOrderItem(i) {
	$("#order-edit-form input[name=barcode]").val(orderItemsData[i].barcode);
	$("#order-edit-form input[name=quantity]").val(orderItemsData[i].quantity);
	$("#order-edit-form input[name=sellingPrice]").val(orderItemsData[i].sellingPrice);
	$('#edit-order-modal').modal('toggle');
}

function errorDisplay(template, message) {
	var $errorbar = $('#status-bar');
	var text = 'Success! ';
	if (template === 'danger') {
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
	else {
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

// INITIALIZATION CODE
function initOrderItem() {
	$('#add-orderitem').click(getOrderItem);
	$('#update-orderitem').click(updateOrderItem);
	$('#order-form').on('submit', e => e.preventDefault())
	$('#edit-order-modal').on('submit', e => e.preventDefault())
	$('#submit').click(createOrder);
	$('#invoice').click(print);
}

function highlight() {
	document.getElementById("addOrder-link").style.fontWeight = 900;

}
$(document).ready(highlight);
$(document).ready(getFromBackend);
$(document).ready(initOrderItem);


//HELPER METHOD
function toJson($form) {
	var serialized = $form.serializeArray();
	console.log(serialized);
	var s = '';
	var data = {};
	for (s in serialized) {
		data[serialized[s]['name']] = serialized[s]['value']
	}
	var json = JSON.stringify(data);
	console.log(json);
	return json;
}


$(function () {
  $('[data-toggle="tooltip"]').tooltip()
})

