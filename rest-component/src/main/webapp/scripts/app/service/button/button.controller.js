'use strict';

angular.module('cloudoptingApp').controller('ButtonController',
		function(SERVICE, $scope, $state, $log, IdeService) {

			$scope.apiProcessOne = function() {
				var callback = function(data, status, headers, config) {
					// Nothing to do
				};
				ProcessService.apiProcessOne(callback);
			};

			// container objects

		});