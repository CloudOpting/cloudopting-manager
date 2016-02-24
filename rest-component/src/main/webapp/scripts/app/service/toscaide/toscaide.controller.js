'use strict';

angular.module('cloudoptingApp').controller('ToscaideController', function() {
	// TODO: Write a controller here
	$scope.mapData = [];
	$scope.edgeData = [];
	$scope.formTypes = [];
	// data types/groups object - used Cytoscape's shapes just
	// to make
	// it more clear
	// $scope.objTypes =
	// ['ellipse','triangle','rectangle','roundrectangle','pentagon','octagon','hexagon','heptagon','star'];
	// $scope.objTypes = [ 'container', 'application', 'host',
	// 'application-container' ];
	$scope.form = [ "*", {
		type : "submit",
		title : "Save"
	} ];

	$scope.model = {};

	$scope.schema = {
		type : "object",
		properties : {
			name : {
				type : "string",
				minLength : 2,
				title : "Name",
				description : "Name or alias"
			},
			title : {
				type : "string",
				enum : [ 'dr', 'jr', 'sir', 'mrs', 'mr', 'NaN', 'dj' ]
			}
		}
	};

	$scope.schema = {
		type : "object",
		title : "DockerContainerProperties properties",
		properties : {
			entrypoint : {
				title : "Entry1point in the Dockerfile",
				type : "string"
			},
			from : {
				title : "Base image1",
				type : "string"
			},
			cmd : {
				title : "Command in th1e Dockerfile",
				type : "string"
			}
		}
	};

	$scope.getNodes = function() {
		var callback = function(data, status, headers, config) {
			$scope.objTypes = data;
			console.debug($scope.objTypes);
			$rootScope.$broadcast('appChanged');
		};
		IdeService.getNodes(callback);
	};
	/*
	 * $http.get('/api/nodes').then(function data(response) {
	 * console.debug('called api'); console.debug(response); $scope.objTypes =
	 * response.data; console.debug($scope.objTypes);
	 * $rootScope.$broadcast('appChanged');
	 * 
	 * });
	 * 
	 * 
	 * $http.get('/api/nodeTypes').then(function data(response) {
	 * console.debug('called nodeTypes'); console.debug(response);
	 * $scope.templateData = response.data; console.debug($scope.templateData);
	 * $rootScope.$broadcast('appChanged');
	 * 
	 * });
	 * 
	 * $http.get('/api/edges').then(function data(response) {
	 * console.debug('called edges'); console.debug(response); $scope.formArr =
	 * response.data; console.debug($scope.formTypes);
	 * $rootScope.$broadcast('appChanged');
	 * 
	 * });
	 * 
	 * $http.get('/api/edgeTypes').then(function data(response) {
	 * console.debug('called edgeTypes'); console.debug(response);
	 * $scope.templateEdgeData = response.data;
	 * console.debug($scope.templateEdgeData);
	 * $rootScope.$broadcast('appChanged');
	 * 
	 * });
	 */
	// add object from the form then broadcast event which
	// triggers the
	// directive redrawing of the chart
	// you can pass values and add them without redrawing the
	// entire
	// chart, but this is the simplest way
	$scope.addObj = function() {
		// collecting data from the form
		// var newObj = $scope.form.obj.name;
		// var newObjType = $scope.form.obj.objTypes;
		var newObj = $scope.scheda.obj.name;
		var newObjType = $scope.scheda.obj.objTypes;
		// building the new Node object
		// using the array length to generate an id for the
		// sample (you
		// can do it any other way)
		var newNode = {
			id : ($scope.mapData.length),
			name : newObj,
			type : newObjType
		};
		// adding the new Node to the nodes array
		$scope.mapData.push(newNode);
		// broadcasting the event
		$rootScope.$broadcast('appChanged');
		// resetting the form
		$scope.scheda.obj = '';
		$scope.$broadcast('schemaFormRedraw');
	};

	// add Edges to the edges object, then broadcast the change
	// event
	$scope.addEdge = function() {
		// collecting the data from the form
		var edge1 = $scope.formEdges.fromName.id;
		var edge2 = $scope.formEdges.toName.id;
		// building the new Edge object from the data
		// using the array length to generate an id for the
		// sample (you
		// can do it any other way)
		var newEdge = {
			id : "e" + ($scope.edgeData.length),
			source : edge1,
			target : edge2,
			type : $scope.formEdges.type
		};
		// adding the new edge object to the adges array
		$scope.edgeData.push(newEdge);
		// broadcasting the event
		$rootScope.$broadcast('appChanged');
		// resetting the form
		$scope.formEdges = '';
	};

	// sample function to be called when clicking on an object
	// in the
	// chart
	$scope.doClick = function(value) {
		// sample just passes the object's ID then output it to
		// the
		// console and to an alert
		console.debug(value);
		// alert(value);
		console.debug('before' + $scope.schema);
		console.debug($scope.schema);
		$scope.schema = JSON.parse(JSON.stringify(value.props));
		$scope.workingNode = value.id;
		$scope.isFormNode = true;
		console.debug('after' + $scope.schema);
		console.debug($scope.schema);
		// $scope.typeform.pop();
		$scope.form = [ "*", {
			type : "submit",
			title : "Save"
		} ];

		console.debug("value.model");
		console.debug($scope.mapData[value.id].model);

		if (typeof $scope.mapData[value.id].model == "undefined") {
			$scope.model = {};
		} else {
			$scope.model = $scope.mapData[value.id].model;
		}
		$scope.$broadcast('schemaFormRedraw');

		$scope.$apply();
	};

	$scope.doEdgeClick = function(value) {
		console.debug(value);
		$scope.schema = JSON.parse(JSON.stringify(value.props));
		$scope.workingEdge = value.id.substring(1);
		$scope.isFormNode = false;
		console.debug($scope.workingEdge);
		console.debug('after' + $scope.schema);
		console.debug($scope.schema);
		// $scope.typeform.pop();
		$scope.form = [ "*", {
			type : "submit",
			title : "Save"
		} ];

		console.debug("value.model");
		console.debug($scope.edgeData[$scope.workingEdge].model);

		if (typeof $scope.edgeData[$scope.workingEdge].model == "undefined") {
			$scope.model = {};
		} else {
			$scope.model = $scope.edgeData[$scope.workingEdge].model;
		}
		$scope.$broadcast('schemaFormRedraw');

		$scope.$apply();
	}

	$scope.onSubmit = function(form) {
		// First we broadcast an event so all fields validate
		// themselves
		$scope.$broadcast('schemaFormValidate');
		console.debug("saving the model");
		console.debug($scope.model);
		console.debug($scope.mapData);
		console.debug($scope.edgeData);
		console.debug($scope.workingNode);
		console.debug($scope.workingEdge);
		console.debug("the mapdata");
		console.debug($scope.mapData[$scope.workingNode]);
		console.debug($scope.edgeData[$scope.workingEdge]);
		if ($scope.isFormNode) {
			$scope.mapData[$scope.workingNode].model = $scope.model;
		} else {
			$scope.edgeData[$scope.workingEdge].model = $scope.model;
		}
		console.debug($scope.mapData[$scope.workingNode]);
		// Then we check if the form is valid
		if (form.$valid) {
			// ... do whatever you need to do with your data.
			console.debug("The form is valid, let's send it: "

			);
			var callback = function(data) {
				console.debug("sendCustomForm succeeded with data: " + data);
			};
		}
	}

	// reset the sample nodes
	$scope.reset = function() {
		$scope.mapData = [];
		$scope.edgeData = [];
		// $scope.$broadcast('schemaFormRedraw');
		$scope.$apply();
		$rootScope.$broadcast('appChanged');

	}

	$scope.sendService = function() {
		console.debug("sending data");
		var data = JSON.stringify({
			nodes : $scope.mapData,
			edges : $scope.edgeData,
			serviceName : $scope.serviceName
		});

		$http({
			url : "/api/sendData",
			data : data,
			method : "POST",
			headers : {
				"Content-Type" : "text/plain",
			}
		}).success(function(data, status) {
			console.debug(data);
			console.debug(status);
		});
	}

	$scope.saveService = function() {
		console.debug("sending data for save");
		var data = JSON.stringify({
			nodes : $scope.mapData,
			edges : $scope.edgeData,
			serviceName : $scope.serviceName
		});

		$http({
			url : "/api/saveData",
			data : data,
			method : "POST",
			headers : {
				"Content-Type" : "text/plain",
			}
		}).success(function(data, status) {
			console.debug(data);
			console.debug(status);
		});
	}
	$scope.loadTopology = function() {
		console.debug("loading saved data");
		var data = JSON.stringify({
			serviceName : $scope.serviceName
		});

		$http({
			url : "/api/loadTopology",
			data : data,
			method : "POST",
			headers : {
				"Content-Type" : "text/plain",
			}
		}).success(function(data, status) {
			$scope.mapData = [];
			$scope.edgeData = [];
			console.debug(data);
			console.debug(status);
			// $scope.mapData = data.nodes;
			data.nodes.forEach(function(entry) {
				console.log(entry);
				$scope.mapData.push(entry);
			});
			data.edges.forEach(function(entry) {
				console.log(entry);
				$scope.edgeData.push(entry);
			});
			console.debug(data.nodes);
			console.debug($scope.mapData);
			// $scope.edgeData = data.edges;
			$rootScope.$broadcast('appChanged');
		});
	}

});
