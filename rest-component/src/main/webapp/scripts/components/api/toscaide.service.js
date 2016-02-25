/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular
	.module('cloudoptingApp')
	.factory(
			'ToscaideService',
			function(SERVICE, $http, $log) {
				var baseURI = 'api';
				var header = {
					'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
				};
				var headerText = {
					'Content-Type' : 'text/plain;'
				};
				return {
					getNodes : function(callback) {
						var endpoint = baseURI + SERVICE.SEPARATOR	+ "nodes";
						return $http
								.get(endpoint, {}, {
									headers : header
								})
								.success(
									function(data, status, headers,	config) {
										callback(data, status, headers,	config);
									})
								.error(
									function(data, status, headers, config) {
										$log.error("ToscaideService.getNodes error. "
													+ "Data: "	+ data
													+ ", status: "	+ status
													+ ", headers: "	+ headers
													+ ", config: "	+ config);
										callback(data, status, headers,	config);
										});
					},
					getNodeTypes : function(callback) {
						var endpoint = baseURI + SERVICE.SEPARATOR	+ "nodeTypes";
						return $http
								.get(endpoint, {}, {
									headers : header
								})
								.success(
									function(data, status, headers,	config) {
										callback(data, status, headers,	config);
									})
								.error(
									function(data, status, headers, config) {
										$log.error("ToscaideService.nodeTypes error. "
													+ "Data: "	+ data
													+ ", status: "	+ status
													+ ", headers: "	+ headers
													+ ", config: "	+ config);
										callback(data, status, headers,	config);
										});

					},
					getEdges : function(callback) {
						var endpoint = baseURI + SERVICE.SEPARATOR	+ "edges";
						return $http
								.get(endpoint, {}, {
									headers : header
								})
								.success(
									function(data, status, headers,	config) {
										callback(data, status, headers,	config);
									})
								.error(
									function(data, status, headers, config) {
										$log.error("ToscaideService.getEdges error. "
													+ "Data: "	+ data
													+ ", status: "	+ status
													+ ", headers: "	+ headers
													+ ", config: "	+ config);
										callback(data, status, headers,	config);
										});

					},

					getEdgeTypes : function(callback) {
						var endpoint = baseURI + SERVICE.SEPARATOR	+ "edgeTypes";
						return $http
								.get(endpoint, {}, {
									headers : header
								})
								.success(
									function(data, status, headers,	config) {
										callback(data, status, headers,	config);
									})
								.error(
									function(data, status, headers, config) {
										$log.error("ToscaideService.getEdgeTypes error. "
													+ "Data: "	+ data
													+ ", status: "	+ status
													+ ", headers: "	+ headers
													+ ", config: "	+ config);
										callback(data, status, headers,	config);
										});
					},
					sendData : function(data, callback) {
						var endpoint = baseURI + SERVICE.SEPARATOR	+ "sendData";
						return $http
								.post(endpoint, {}, {
									data : data,
									headers : headerText
								})
								.success(
									function(data, status, headers,	config) {
										callback(data, status, headers,	config);
									})
								.error(
									function(data, status, headers, config) {
										$log.error("ToscaideService.sendData error. "
													+ "Data: "	+ data
													+ ", status: "	+ status
													+ ", headers: "	+ headers
													+ ", config: "	+ config);
										callback(data, status, headers,	config);
										});
					},
					saveData : function(data, callback) {
						var endpoint = baseURI + SERVICE.SEPARATOR	+ "saveData";
						return $http
								.post(endpoint, {}, {
									data : data,
									headers : headerText
								})
								.success(
									function(data, status, headers,	config) {
										callback(data, status, headers,	config);
									})
								.error(
									function(data, status, headers, config) {
										$log.error("ToscaideService.saveData error. "
													+ "Data: "	+ data
													+ ", status: "	+ status
													+ ", headers: "	+ headers
													+ ", config: "	+ config);
										callback(data, status, headers,	config);
										});
					},
					loadTopology : function(data, callback) {
						var endpoint = baseURI + SERVICE.SEPARATOR	+ "loadTopology";
						return $http
								.post(endpoint, {}, {
									data : data,
									headers : headerText
								})
								.success(
									function(data, status, headers,	config) {
										callback(data, status, headers,	config);
									})
								.error(
									function(data, status, headers, config) {
										$log.error("ToscaideService.loadTopology error. "
													+ "Data: "	+ data
													+ ", status: "	+ status
													+ ", headers: "	+ headers
													+ ", config: "	+ config);
										callback(data, status, headers,	config);
										});
					},

						/**
						 * FIXME: This function should be deleted together with
						 * the Button Screen. Method to test a particular case
						 * of the process
						 * 
						 * @param callback
						 *            Function that will take care of the
						 *            returned objects.
						 * @returns {*}
						 */
						apiProcessOne : function(callback) {
							var customizationId = 4308;
							var isDemo = false;
							var isTesting = true;
							var endpoint = baseURI + '?customizationId='
									+ customizationId + '&isDemo=' + isDemo
									+ '&isTesting=' + isTesting;
							return $http
									.post(endpoint, {}, {
										headers : header
									})
									.success(
											function(data, status, headers,
													config) {
												callback(data, status, headers,
														config);
											})
									.error(
											function(data, status, headers,
													config) {
												$log
														.error("ProcessService.apiProcessOne error. "
																+ "Data: "
																+ data
																+ ", status: "
																+ status
																+ ", headers: "
																+ headers
																+ ", config: "
																+ config);
												callback(data, status, headers,
														config);
											});
						},

						/**
						 * Method to get a test zip file for the instance with
						 * identification 'instanceId'.
						 * 
						 * @param instanceId
						 *            Identification of the instance.
						 * @param callback
						 *            Function that will take care of the
						 *            returned objects.
						 * @returns {*}
						 */
						test : function(instanceId, callback) {
							var isDemo = false;
							var isTesting = true;
							var endpoint = baseURITest + '?customizationId='
									+ instanceId + '&isDemo=' + isDemo
									+ '&isTesting=' + isTesting;
							return $http
									.post(endpoint, {}, {
										headers : header,
										responseType : "blob"
									})
									.success(
											function(data, status, headers,
													config) {
												callback(data, status, headers,
														config);
											})
									.error(
											function(data, status, headers,
													config) {
												$log
														.error("ProcessService.test error. "
																+ "Data: "
																+ data
																+ ", status: "
																+ status
																+ ", headers: "
																+ headers
																+ ", config: "
																+ config);
												callback(data, status, headers,
														config);
											});
						},

						/**
						 * Method to launch a demo for the instance with
						 * identification 'instanceId'.
						 * 
						 * @param instanceId
						 *            Identification of the instance.
						 * @param callback
						 *            Function that will take care of the
						 *            returned objects.
						 * @returns {*}
						 */
						demo : function(instanceId, callback) {
							var isDemo = true;
							var isTesting = false;
							var endpoint = baseURI + '?customizationId='
									+ instanceId + '&isDemo=' + isDemo
									+ '&isTesting=' + isTesting;
							return $http
									.post(endpoint, {}, {
										headers : header
									})
									.success(
											function(data, status, headers,
													config) {
												callback(data, status, headers,
														config);
											})
									.error(
											function(data, status, headers,
													config) {
												$log
														.error("ProcessService.demo error. "
																+ "Data: "
																+ data
																+ ", status: "
																+ status
																+ ", headers: "
																+ headers
																+ ", config: "
																+ config);
												callback(data, status, headers,
														config);
											});
						},

						/**
						 * Method to deploy an instance for the instance with
						 * identification 'instanceId'.
						 * 
						 * @param instanceId
						 *            Identification of the instance.
						 * @param callback
						 *            Function that will take care of the
						 *            returned objects.
						 * @returns {*}
						 */
						deploy : function(instanceId, callback) {
							var isDemo = false;
							var isTesting = false;
							var endpoint = baseURI + '?customizationId='
									+ instanceId + '&isDemo=' + isDemo
									+ '&isTesting=' + isTesting;
							return $http
									.post(endpoint, {}, {
										headers : header
									})
									.success(
											function(data, status, headers,
													config) {
												callback(data, status, headers,
														config);
											})
									.error(
											function(data, status, headers,
													config) {
												$log
														.error("ProcessService.deploy error. "
																+ "Data: "
																+ data
																+ ", status: "
																+ status
																+ ", headers: "
																+ headers
																+ ", config: "
																+ config);
												callback(data, status, headers,
														config);
											});
						}
					};
				});