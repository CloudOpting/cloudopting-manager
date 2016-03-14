'use strict';

angular.module('cloudoptingApp')
	.controller('ActivitiController', function(SERVICE, localStorageService,
											   $scope, $state, $log, $timeout,
											   ActivitiService) {
		// need to know if need to do something to set the $scope.processId
		var processId = localStorageService.get(SERVICE.STORAGE.ACTIVITI.PROCESS_ID);
		//processId = 852627;
		$scope.imageUrl = "/runtime/process-instances/" + processId + "/diagram";
		var refresh = 5000; //Miliseconds

		//Refresh it.
		var reloadFunction = function(){
			$timeout(function () {
				$scope.imageUrl = "/runtime/process-instances/" + processId + "/diagram?" + new Date().getTime();
				reloadFunction();
			}, refresh);
		};
		reloadFunction();

		//NOTE: It is not needed to call the Activiti service.
		/*
		var callback = function(data, status, headers, config) {
			if(checkStatusCallback(data, status, headers, config)) {
				//If all went ok, refresh the picture.
			}
			//Execute again to refresh the picture in seconds.
			$timeout(function () {
				ActivitiService.getProcessVariables($scope.processId, callback);
			}, refresh);
		};

		//First execution
		ActivitiService.getProcessVariables($scope.processId, callback);
		*/


		//////////////////
		// HANDLE ERRORS
		//////////////////

		//Handle errors not needed
		/*
		$scope.errorMessage = null;

		//function to check possible outputs for the end user information.
		var checkStatusCallback = function(data, status, headers, config){
			if(status==401) {
				//Unauthorised. Check if signed in.
				if(Principal.isAuthenticated()){
					$scope.errorMessage = "You have no permissions to do so. Ask for more permissions to the administrator";
				} else {
					$scope.errorMessage = "Your session has ended. Sign in again. Redirecting to login...";
					$timeout(function() {
						$state.go('login');
					}, 3000);
				}
				return false;
			} else if(status!=200 && status!=201) {
				//Show message
				$scope.errorMessage = "An error occurred. Wait a moment and try again, if problem persists contact the administrator";
				return false;
			} else {
				$log.info("Everything went ok!");
				return true;
			}
		};
		 */
	});