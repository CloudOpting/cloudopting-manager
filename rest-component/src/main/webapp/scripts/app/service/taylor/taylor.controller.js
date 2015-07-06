'use strict';

angular.module('cloudoptingApp')
    .controller('TaylorController', function (SERVICE, $scope, $state, $log, ApplicationService, localStorageService) {

        $scope.cloudNodeList = null;
        $scope.osList = null;
        $scope.skinList = null;

        var currentApp = localStorageService.get(SERVICE.STORAGE.CURRENT_APP);
        if(currentApp !== undefined && currentApp !== null)
        {
            $scope.cloudNodeList = currentApp.inputParameters.cloudNodeList;
            $scope.osList = currentApp.inputParameters.osList;
            $scope.skinList = currentApp.inputParameters.skinList;
        }
        else
        {
            //If not application go to catalog.
            $state.go('catalog');
        }

        $scope.requestSubscription = function(){
            //Request Subscription?
            //ApplicationService.requestSubscription($scope.application);
        };
    }
);