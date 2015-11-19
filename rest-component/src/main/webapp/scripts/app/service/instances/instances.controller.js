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

        //TODO: Implement button go for each instance.
        $scope.test = function(instance) {
            var callback = function(data){
                window.alert("Test requested.");
            };
            ProcessService.test(instance, callback);

        };
        $scope.deploy = function(instance) {
            var callback = function(data){
                window.alert("Deploy requested.");
                //TODO:When the bpmn is ready we should uncomment this line in order to save the new status.
                //InstanceService.deploy(instance);
                //{"timestamp":1447946243960,"status":500,"error":"Internal Server Error","exception":"org.activiti.engine.ActivitiObjectNotFoundException","message":"no processes deployed with key 'updateCustomization'","path":"/api/customization"}
            };
            ProcessService.deploy(instance, callback);
        };
        $scope.stop = function(instance) {
            InstanceService.stop(instance);
        };
        $scope.delete = function(instance) {
            InstanceService.delete(instance);
        };
        $scope.monitor = function(instance) {
            //TODO: Save the instance in the local storage in order to be accessible in the monitoring page.
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