'use strict';

angular.module('cloudoptingApp')
    .controller('InstancesController', function ($scope, $log, $location, RestApi) {
        //TODO: Change instancesList to applicationList once it is developed properly?
        $scope.instancesList = RestApi.instancesList();

        //TODO: Implement button "Search Service" functionality.

        //TODO: Implement button go for each instance.


        $scope.showDeploy = function(str){
            return str === "Requested";
        };
        $scope.showStop = function(str){
            return str === "Running";
        };
        $scope.showDelete = function(str){
            return str === "Running";
        };
        $scope.showStart = function(str){
            return str === "Stopped";
        };
        $scope.showMonitor = function(str){
            return str === "Running";
        };
    }
);