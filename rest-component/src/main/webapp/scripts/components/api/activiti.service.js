/**
 * Created by a591584 on 30/06/2015.
 */
'use strict';

angular.module('cloudoptingApp')
	.factory('ActivitiService',function(SERVICE, $http, $log) {

		var baseURI = 'runtime';
		var header = {
			'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
		};
		var headerText = {
			'Content-Type' : 'text/plain;'
		};

		return {
			getProcessVariables : function(processId, callback) {
				var endpoint = baseURI
					+ SERVICE.SEPARATOR
					+ "process-instances"
					+ SERVICE.SEPARATOR
					+ processId
					+ SERVICE.SEPARATOR
					+ "diagram";
				return $http.get(endpoint, {}, { headers : header })
					.success(function(data, status, headers, config) {
						callback(data, status, headers, config);
					}).error(function(data, status, headers, config) {
						$log.error("ActivitiService.getProcessVariables error. "
								+ "Data: " + data + ", status: " + status + ", headers: " + headers + ", config: " + config);
						callback(data, status, headers, config);
					});
			},

		};
	});