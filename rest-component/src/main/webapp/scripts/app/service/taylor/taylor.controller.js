'use strict';

angular.module('cloudoptingApp')
    .controller('TaylorController', function (SERVICE, localStorageService,
                                              $scope, $state) {

        $scope.cloudNodeList = null;
        $scope.osList = null;
        $scope.skinList = null;

        var currentApp = localStorageService.get(SERVICE.STORAGE.TAYLOR.APPLICATION);
        if(currentApp !== undefined && currentApp !== null)
        {
            $scope.cloudNodeList = currentApp.inputParameters.cloudNodeList;
            $scope.osList = currentApp.inputParameters.osList;
            $scope.skinList = currentApp.inputParameters.skinList;
        }
        else
        {
            //If not application go to catalogue.
            $state.go('catalogue');
        }

        $scope.requestSubscription = function(){
            //Request Subscription?
            //ApplicationService.requestSubscription($scope.application);
        };
    }
);