'use strict';
angular.module('cloudoptingApp').directive('cytoscape', function($rootScope,$http) {
    // graph visualisation by - https://github.com/cytoscape/cytoscape.js
    return {
        restrict: 'E',
        template :'<div id="cy"></div>',
        replace: true,
        scope: {
            // data objects to be passed as an attributes - for nodes and edges
            cyData: '=',
            cyEdges: '=',
            cyTemplates: '=',
            cyEdgetemplates: '=',
            // controller function to be triggered when clicking on a node
            cyClick:'&',
            cyEdgeclick:'&'
        },
        link: function(scope, element, attrs, fn) {
            // dictionary of colors by types. Just to show some design options

            // graph  build
            scope.doCy = function(){ // will be triggered on an event broadcast
                // initialize data object
                scope.elements = {};
                scope.elements.nodes = [];
                scope.elements.edges = [];
console.debug(scope.elements.nodes);
                // parse edges
                // you can build a complete object in the controller and pass it without rebuilding it in the directive.
                // doing it like that allows you to add options, design or what needed to the objects
                // doing it like that is also good if your data object/s has a different structure
                for (i=0; i<scope.cyEdges.length; i++)
                {
                    // get edge source
                    var eSource = scope.cyEdges[i].source;
                    // get edge target
                    var eTarget = scope.cyEdges[i].target;
                    // get edge id
                    var eId = scope.cyEdges[i].id;
                    var eType = scope.cyEdges[i].type;
                    var eStyle = scope.cyEdgetemplates[eType].style;
                    var eProps = scope.cyEdgetemplates[eType].props;
                    var eColor = scope.cyEdgetemplates[eType].color;
                    console.debug("edge type:" + scope.cyEdges[i].type);
                    // build the edge object
                    var edgeObj = {
                        data:{
                        id:eId,
                        source:eSource,
                        target:eTarget,
                        color:eColor,
                        style:eStyle,
                        properties:eProps,
                        type: eType
                        }
                    };
                    // adding the edge object to the edges array
                    scope.elements.edges.push(edgeObj);
                }

                // parse data and create the Nodes array
                // object type - is the object's group
                for (i=0; i<scope.cyData.length; i++)
                {
                    // get id, name and type  from the object
                    var Oid = scope.cyData[i].id;
                    var Oname = scope.cyData[i].name;
					var Otype = scope.cyData[i].type;
					var Oshape = scope.cyTemplates[Otype].shape;
					var Oprops = scope.cyTemplates[Otype].props;
					var typeColor = scope.cyTemplates[Otype].color;
					var image = 'assets/type/'+scope.cyTemplates[Otype].image;
                    // build the object, add or change properties as you need - just have a name and id
                    var elementObj = {
                        group:Otype,'data':{
                            id:Oid,
                            name:Oname,
                            typeColor:typeColor,
                            image:image,
                            typeShape:Oshape,  //was Otype
							properties:Oprops,
                            type:Otype
                    }};
                    // add new object to the Nodes array
                    scope.elements.nodes.push(elementObj);
                }
                console.debug(scope.elements.nodes);
                // graph  initialization
                // use object's properties as properties using: data(propertyName)
                // check Cytoscapes site for much more data, options, designs etc
                // http://cytoscape.github.io/cytoscape.js/
                // here are just some basic options
                $('#cy').cytoscape({
                    layout: {
                        name: 'breadthfirst',
                        fit: true, // whether to fit the viewport to the graph
                        ready: undefined, // callback on layoutready
                        stop: undefined, // callback on layoutstop
                        padding: 5 // the padding on fit
                    },
                    style: cytoscape.stylesheet()
                        .selector('node')
                        .css({
                            'shape': 'data(typeShape)',
                            'width': '120',
                            'height': '90',
                            'background-color': 'data(typeColor)',
                            'background-image': 'data(image)',
                            'background-fit': 'contain',
                            'content': 'data(name)',
                            'text-valign': 'center',
                            'color': 'white',
                            'text-outline-width': 2,
                            'text-outline-color': '#000000'
                        })
                        .selector('edge')
                        .css({
                            'width': '5',
                            'target-arrow-shape': 'triangle',
                            'source-arrow-shape': 'none',
                            'content': 'data(type)',
                            'line-color': 'data(color)',
                            'line-style': 'data(style)'
                        })
                        .selector(':selected')
                        .css({
                            'background-color': 'black',
                            'line-color': 'black',
                            'target-arrow-color': 'black',
                            'source-arrow-color': 'black'
                        })
                        .selector('.faded')
                        .css({
                            'opacity': 0.65,
                            'text-opacity': 0.65
                        }),
                        ready: function(){
                        window.cy = this;

                        // giddy up...
                        cy.elements().unselectify();

                        // Event listeners
                        // with sample calling to the controller function as passed as an attribute
                        cy.on('tap', 'node', function(e){
                            var evtTarget = e.cyTarget;
                            var nodeId = evtTarget.id();
							var nodeProps = evtTarget.data('properties');
                            scope.cyClick({value:{id:nodeId,props:nodeProps}});
                        });

                        cy.on('tap', 'edge', function(e){
                        	console.debug("tapping on edge");
                            var evtTarget = e.cyTarget;
                            var edgeId = evtTarget.id();
							var edgeProps = evtTarget.data('properties');
                            scope.cyEdgeclick({value:{id:edgeId,props:edgeProps}});
                        });

                        // load the objects array
                        // use cy.add() / cy.remove() with passed data to add or remove nodes and edges without rebuilding the graph
                        // sample use can be adding a passed variable which will be broadcast on change
                        console.debug(scope.elements);
                        cy.load(scope.elements);
                    }
                });

            }; // end doCy()

            // When the app object changed = redraw the graph
            // you can use it to pass data to be added or removed from the object without redrawing it
            // using cy.remove() / cy.add()
            $rootScope.$on('appChanged', function(){
                scope.doCy();
            });
        }
    };
});

angular
		.module('cloudoptingApp')
		.controller(
				'ButtonController',
				function(SERVICE, $scope, $state, $log, IdeService) {
/*
					$scope.apiProcessOne = function() {
						var callback = function(data, status, headers, config) {
							// Nothing to do
						};
						ProcessService.apiProcessOne(callback);
					};
*/
					// container objects
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
								enum : [ 'dr', 'jr', 'sir', 'mrs', 'mr', 'NaN',
										'dj' ]
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
					$http.get('/api/nodes').then(function data(response) {
						console.debug('called api');
						console.debug(response);
						$scope.objTypes = response.data;
						console.debug($scope.objTypes);
						$rootScope.$broadcast('appChanged');

					});
					

					$http.get('/api/nodeTypes').then(function data(response) {
						console.debug('called nodeTypes');
						console.debug(response);
						$scope.templateData = response.data;
						console.debug($scope.templateData);
						$rootScope.$broadcast('appChanged');

					});

					$http.get('/api/edges').then(function data(response) {
						console.debug('called edges');
						console.debug(response);
						$scope.formArr = response.data;
						console.debug($scope.formTypes);
						$rootScope.$broadcast('appChanged');

					});

					$http.get('/api/edgeTypes').then(function data(response) {
						console.debug('called edgeTypes');
						console.debug(response);
						$scope.templateEdgeData = response.data;
						console.debug($scope.templateEdgeData);
						$rootScope.$broadcast('appChanged');

					});
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
						console
								.debug($scope.edgeData[$scope.workingEdge].model);

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
								console
										.debug("sendCustomForm succeeded with data: "
												+ data);
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