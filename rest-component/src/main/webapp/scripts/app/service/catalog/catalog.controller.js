'use strict';
angular.module('cloudoptingApp').filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        return input.slice(start);
    }
});

angular.module('cloudoptingApp')
    .controller('CatalogController', function (SERVICE, $scope, $log, $state, ApplicationService, localStorageService) {
        //, ApplicationService
        //TODO: Change applicationListUnpaginated to applicationList once it is developed properly
        $scope.applicationList = null;

        var callback = function(applications) {
            $scope.applicationList = applications.content;
            pagination();

            for (var app in $scope.applicationList) {
                $scope.applicationList[app].applicationImage = "http://placehold.it/200x180";
            }
        };

        ApplicationService.findAllUnpaginated(callback);

        $scope.detail = function(application){
            //Save the current application
            localStorageService.set(SERVICE.STORAGE.CURRENT_APP, application);
            //Go to the detail of the applicaiton.
            $state.go('detail');
        };

        //PAGINATION
        $scope.currentPage = 0;
        $scope.pageSize = 4;

        var pagination = function(){
            $scope.totalItems = $scope.applicationList.length;

            $scope.numberOfPages = function(){
                return Math.ceil($scope.totalItems/$scope.pageSize);
            };
            $scope.nextDisable = function(){
                return $scope.currentPage >= $scope.totalItems/$scope.pageSize - 1;
            };


            $scope.setPage = function (pageNo) {
                $scope.currentPage = pageNo;
            };

            $scope.pageChanged = function() {
                $log.log('Page changed to: ' + $scope.currentPage);
            };


            //$scope.bigTotalItems = 175;
            //$scope.bigCurrentPage = 1;
        }
    }
);