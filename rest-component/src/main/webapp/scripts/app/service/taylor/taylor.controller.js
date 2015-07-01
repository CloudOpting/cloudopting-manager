'use strict';

angular.module('cloudoptingApp')
    .controller('TaylorController', function (SERVICE, $scope, $log, ApplicationService, localStorageService) {

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

        $scope.requestSubscription = function(){
            //Request Subscription?
            //ApplicationService.requestSubscription($scope.application);
        };
    }
);