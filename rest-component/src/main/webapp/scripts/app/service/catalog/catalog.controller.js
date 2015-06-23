'use strict';

angular.module('cloudoptingApp')
    .controller('CatalogController', function ($scope, $log, $state, RestApi, localStorageService) {
        //, ApplicationService
        //TODO: Change applicationListUnpaginated to applicationList once it is developed properly
        $scope.applicationList = RestApi.applicationListUnpaginated();

        $scope.detail = function(application){
            //save the current application
            //ApplicationService.setCurrentApplication(applicaiton);
            //ApplicationService.currentApplication = applicaiton;
            localStorageService.set("currentApplication", application);
            //go to the detail
            $state.go('detail');
        };
    }
);