'use strict';

angular.module('cloudoptingApp')
    .controller('CatalogController', function ($scope, $log, $state, RestApi) {
        //, ApplicationService
        //TODO: Change applicationListUnpaginated to applicationList once it is developed properly
        $scope.applicationList = RestApi.applicationListUnpaginated();

        $scope.detail = function(applicaiton){
            //save the current application
            //ApplicationService.setCurrentApplication(applicaiton);
            //ApplicationService.currentApplication = applicaiton;
            //go to the detail
            $state.path('detail');
        };
    }
);