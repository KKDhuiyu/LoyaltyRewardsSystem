function Coupon(points, customerID) {

	this.points = points;
	this.used = false;
	this.customerID = customerID;
}

var module = angular.module('CustomersModule', ['ngResource']);

module.controller('CustomerController', function ($scope, $resource) {
	var pointsResource = $resource('http://localhost\\:8081/customers/:customer/points/unused', null, {
		'getPoints': {transformResponse: function (data, headers, status) {
				return {value: data};
			}
		}});

	//___________________________________________________________________________
	////___________________________________________________________________________
	////___________________________________________________________________________
	////___________________________________________________________________________
	////___________________________________________________________________________
	var emailws = new WebSocket("ws://localhost:9091/email");
	emailws.onmessage = function (event) {
		// unmarshal the customer from JSON and insert the details into the form
		var object = JSON.parse(event.data);
		var customer = object.customers[0];
		console.log(customer);
		$scope.customer = customer;

		pointsResource.getPoints({"customer": $scope.customer.id}, function (points) {
			$scope.available_points = points.value;

		});
	};

	emailws.onopen = function (event) {
		console.log("email ws is ready");
	};
	emailws.onerror = function (event) {
		console.log("error!");
	};
	emailws.onclose = function (event) {
		console.log("email ws is closed");
	};
	$scope.login = function () {
		var email = $scope.customer_email;
		emailws.send(email);
	};
	//___________________________________________________________________________
	////___________________________________________________________________________
	////___________________________________________________________________________
	////___________________________________________________________________________
	////___________________________________________________________________________
	//product
	var productsws = new WebSocket("ws://localhost:9091/products");
	productsws.onmessage = function (event) {
		var object = JSON.parse(event.data);
		var productId = object.product.id;
		JsBarcode("#barcode", productId);
	};

	productsws.onopen = function (event) {
		console.log("product ws is ready");
	};
	productsws.onerror = function (event) {
		console.log(event.data);
	};
	productsws.onclose = function (event) {
		console.log("product ws is closed");
	};

	//___________________________________________________________________________
	////___________________________________________________________________________
	////___________________________________________________________________________
	////___________________________________________________________________________
	////___________________________________________________________________________

	var couponws = new WebSocket("ws://localhost:9091/coupon");
	couponws.onmessage = function (event) {

	};
	couponws.onopen = function (event) {
		console.log("coupon ws is ready");
	};
	couponws.onerror = function (event) {
		console.log(event.data);
	};
	couponws.onclose = function (event) {
		console.log("coupon ws closed.");
	};

	$scope.createCoupon = function () {
		var coupon = new Coupon($scope.points_to_use, $scope.customer.id);
		$scope.eqivalent_price = $scope.points_to_use / 100;
		var json = JSON.stringify(coupon);
		couponws.send(json);
		var couponResource = $resource('http://localhost\\:8081/customers/:customer/coupons', null, {get: {method: 'GET', isArray: true}});
		var coupons = couponResource.get({"customer": $scope.customer.id}, coupon, function (event) {

			$scope.couponID = event[0].id;
			console.log($scope.couponID);
			var price = 0 - $scope.eqivalent_price;

			var product = new Object();
			product.handle = $scope.couponID;
			product.type = "coupon";
			product.name = "jiahu599" + $scope.couponID;
			product.retail_price = price;
			var json2 = JSON.stringify(product);
			productsws.send(json2);
			console.log(product);

		});



	};

	$scope.calculatePrice = function () {
		$scope.eqivalent_price = $scope.points_to_use / 100;
	};



});
