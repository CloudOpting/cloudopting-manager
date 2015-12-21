'use strict';

angular.module('cloudoptingApp')
    .controller('InstancesController', function (SERVICE, $scope, $state, $log, $location, Principal, localStorageService, InstanceService, ProcessService) {
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

            $scope.instancesList = $scope.currentApp.customizationss;

            angular.forEach($scope.instancesList, function(instance, key) {
                instance.applicationName = $scope.currentApp.applicationName;
            });

/*
            angular.forEach(instancesList, function(instancesList, key) {
                $scope.instancesList.push({
                    "service_name": $scope.currentApp.applicationName,
                    "author": customization.customerOrganizationId,
                    "date": customization.customizationCreation,
                    "status": customization.statusId

                })
            })*/
        }

        //TODO: Implement button "Search Service" functionality.

        $scope.test = function(instance) {
            //Save the organization in order to retrieve later the clouds accounts.
            var currentApp = localStorageService.get(SERVICE.STORAGE.CURRENT_APP);
            localStorageService.set(SERVICE.STORAGE.WIZARD_INSTANCES.ORGANIZATION, currentApp.organizationId);

            var func = function (cloudAccount, call_b) {
                var callback = function (data) {
                    window.alert("Test requested.");
                    call_b();
                };
                ProcessService.test(instance, cloudAccount, callback);
            };

            localStorageService.set(SERVICE.STORAGE.WIZARD_INSTANCES.FUNCTION, func);

            $state.go('chooseaccount');

        };
        $scope.deploy = function(instance) {
            //Save the organization in order to retrieve later the clouds accounts.
            var currentApp = localStorageService.get(SERVICE.STORAGE.CURRENT_APP);
            localStorageService.set(SERVICE.STORAGE.WIZARD_INSTANCES.ORGANIZATION, currentApp);

            var func = function (cloudAccount, call_b) {
                var callback = function (data) {
                    window.alert("Deploy requested.");
                    call_b();
                };
                ProcessService.deploy(instance, cloudAccount, callback);
            };

            localStorageService.set(SERVICE.STORAGE.WIZARD_INSTANCES.FUNCTION, func);

            $state.go('chooseaccount');

        };
        $scope.stop = function(instance) {
            InstanceService.stop(instance);
        };
        $scope.delete = function(instance) {
            InstanceService.delete(instance);
        };
        $scope.monitor = function(instance) {
            localStorageService.set(SERVICE.STORAGE.CURRENT_INSTANCE, instance);
            $state.go('monitoring');
        };
        $scope.start = function(instance) {
            InstanceService.start(instance);
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