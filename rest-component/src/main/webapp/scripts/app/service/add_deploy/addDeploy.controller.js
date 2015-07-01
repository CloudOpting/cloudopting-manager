'use strict';

angular.module('cloudoptingApp')
    .controller('AddDeployController', function (SERVICE, $scope, $log, localStorageService, ApplicationService, InstanceService) {
        $scope.cloudNodeList = null;
        $scope.osList = null;
        $scope.skinList = null;

        var currentApp = localStorageService.get(SERVICE.STORAGE.CURRENT_APP);
        if(currentApp !== undefined && currentApp !== null)
        {
            ApplicationService.inputParameters(currentApp.id)
                .success(function(screen){
                    $scope.cloudNodeList = screen.cloudNodeList;
                    $scope.osList = screen.osList;
                    $scope.skinList = screen.skinList;
                });

        }
        else
        {
            //Show some error or tell user to go and select a Service.
            //throw Exception!!
            //throw new WebUIException("Application not present.");
        }

        $scope.saveTemplate = function(service) {
            //Save the template temporarily
            $scope.tempTemplate = service;
        };

        $scope.uploadTemplate = function(service) {
            //Save the template temporarily
            //InstanceService.create(service);
            //$scope.message = "Service send successfully!";
        };
    }
);
