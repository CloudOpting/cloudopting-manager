'use strict';

angular.module('cloudoptingApp')
    .controller('InstancesController', function (SERVICE, $scope, $log, $location, Principal, localStorageService, InstanceService) {
        $scope.instancesList = null;
        if(Principal.isInRole(SERVICE.ROLE.SUBSCRIBER)) {
            //Get all instances of the user if it is a SUBSCRIBER.
            InstanceService.findAllUnpaginatedSubscriber()
                .success(function (instances) {
                    $scope.instancesList = instances;
                });

        } else if(Principal.isInRole(SERVICE.ROLE.ADMIN) || Principal.isInRole(SERVICE.ROLE.OPERATOR) ) {
            //If the user is an ADMIN or an OPERATOR and they comes from DETAIL screen
            //get only the instances of the current application.
            $scope.currentApp = localStorageService.get(SERVICE.STORAGE.CURRENT_APP);

            $scope.instancesList = $scope.currentApp.instancesList;
        }



        //TODO: Implement button "Search Service" functionality.

        //TODO: Implement button go for each instance.
        $scope.deploy = function() {
            InstanceService.deploy();
        };
        $scope.stop = function() {
            InstanceService.stop();
        };
        $scope.delete = function() {
            InstanceService.delete();
        };
        $scope.monitor = function() {
            //$state.go('monitor');
        };
        $scope.start = function() {
            InstanceService.start();
        };
        //Checks for showing the buttons.
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