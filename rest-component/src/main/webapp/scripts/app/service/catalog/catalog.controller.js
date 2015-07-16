'use strict';

angular.module('cloudoptingApp')
    .controller('CatalogController', function (SERVICE, $scope, $log, $state, ApplicationService, localStorageService) {
        //, ApplicationService
        //TODO: Change applicationListUnpaginated to applicationList once it is developed properly
        $scope.applicationList = null;

        var callback = function(applications) {
            $scope.applicationList = applications.content;
        };
        ApplicationService.findAllUnpaginated(callback);

        $scope.detail = function(application){
            //Save the current application
            localStorageService.set(SERVICE.STORAGE.CURRENT_APP, application);
            //Go to the detail of the applicaiton.
            $state.go('detail');
        };
    }
);