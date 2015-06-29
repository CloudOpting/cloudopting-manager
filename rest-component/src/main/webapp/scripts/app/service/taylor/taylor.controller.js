'use strict';

angular.module('cloudoptingApp')
    .controller('TaylorController', function (SERVICE, $scope, $log, RestApi, localStorageService) {

        var currentApp = localStorageService.get(SERVICE.STORAGE.CURRENT_APP);
        if(currentApp !== undefined && currentApp !== null)
        {
            var screen = RestApi.inputParameters(currentApp.id);
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

        $scope.requestSubscription = function(){
            //Request Subscription?
            //RestApi.requestSubscription($scope.application);
        };
    }
);