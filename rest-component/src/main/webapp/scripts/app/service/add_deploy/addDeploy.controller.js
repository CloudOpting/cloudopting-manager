'use strict';

angular.module('cloudoptingApp')
    .controller('AddDeployController', function ($scope, $log, RestApi, ApplicationService) {
        //Get information about the specific Service
        //RestApi.
        //Call to the middleware to get the roles

        if(ApplicationService.currentApplication !== undefined && ApplicationService.currentApplication !== null)
        {
            var screen = RestApi.inputParameters(ApplicationService.currentApplication.id);
            $scope.cloudNodeList = screen.cloudNodeList;
            $scope.osList = screen.osList;
            $scope.skinList = screen.skinList;
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
            //RestApi.customizationCreate(service);
            //$scope.message = "Service send successfully!";
        };
    }
);
