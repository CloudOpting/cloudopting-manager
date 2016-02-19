'use strict';

angular.module('cloudoptingApp')
    .controller('AddDeployController', function (SERVICE, localStorageService,
                                                 $scope, $state, $log,
                                                 ApplicationService, InstanceService) {
        $scope.cloudNodeList = null;
        $scope.osList = null;
        $scope.skinList = null;

        var currentApp = localStorageService.get(SERVICE.STORAGE.ADD_DEPLOY.APPLICATION);
        if(currentApp !== undefined && currentApp !== null)
        {
            $scope.cloudNodeList = currentApp.cloudNodeList;
            $scope.osList = currentApp.osList;
            $scope.skinList = currentApp.skinList;
        }
        else
        {
            //If not application go to catalogue.
            $state.go('catalogue');
        }

        $scope.saveTemplate = function(service) {
            //Save the template temporarily
            $scope.tempTemplate = service;
        };

        $scope.uploadTemplate = function(service) {
            //Save the template temporarily
            var callback = function(data, status, headers, config){
                $log.info(data);
            };
            InstanceService.create(service, callback);
            //$scope.message = "Service send successfully!";
        };
    }
);
