'use strict';
angular.module('cloudoptingApp').filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        if(input==null) return;
        return input.slice(start);
    }
});

angular.module('cloudoptingApp')
    .controller('CatalogController', function (SERVICE, $scope, $log, $state, ApplicationService, localStorageService) {
        //, ApplicationService
        //TODO: Change applicationListUnpaginated to applicationList once it is developed properly
        $scope.applicationList = null;

        var callback = function(applications) {
            var applicationFiltered = applications.content;

            //Select only applications with "Published" status.
            $scope.applicationList = [];
            for (var app in applicationFiltered) {
                if(applicationFiltered[app].statusId.status == "Published") {
                    $scope.applicationList.push(applicationFiltered[app]);
                }
            }

            //Do the pagination
            pagination();

            //FIXME: Delete once a proper image is setted to the applications.
            for (var app in $scope.applicationList) {
                var random=Math.random();
                $scope.applicationList[app].applicationImage = "http://placehold.it/200x180";
                if(random<=0.33) $scope.applicationList[app].applicationImage = "http://www.i2space.com/img/mobile_inner.jpg";
                if(random>0.33 && random<0.66) $scope.applicationList[app].applicationImage = "http://kksoft.in/Images/Mobile-Application-Development.png";
                if(random>0.66) $scope.applicationList[app].applicationImage = "http://community.nagra.com/images/nagra/icons/framework.jpg";

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