'use strict';
angular.module('cloudoptingApp').filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        if(input==null) return;
        return input.slice(start);
    }
});

angular.module('cloudoptingApp')
    .controller('CatalogueController', function (SERVICE, $scope, $log, $state, ApplicationService, localStorageService, Principal, JackrabbitService) {
        //, ApplicationService
        //Save user
        Principal.identity().then(function(account) {
            localStorageService.set(SERVICE.STORAGE.CURRENT_USER, account);
        });

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

            //Get the images for each application.
            for (var app in $scope.applicationList) {
                if ($scope.applicationList[app].applicationLogoReference == null
                        || $scope.applicationList[app].applicationLogoReference == undefined
                        || $scope.applicationList[app].applicationLogoReference == "") {
                    //Set default image
                    $scope.applicationList[app].applicationLogoReference = "http://placehold.it/350x150";
                }
                else {
                    /*
                    var callback = function(data, status, headers, config){
                        if(checkStatusCallback(data, status, headers, config)){
                            if(data){
                                $scope.applicationList[app].applicationLogoReference = data;
                            }
                        }
                    };

                    JackrabbitService.findImage($scope.applicationList[app].applicationLogoReference, callback);
                    */

                    //Changeing the URL only instead of calling the rest API with AJAX
                    $scope.applicationList[app].applicationLogoReference = "/api/jr/img?jcrPath="+$scope.applicationList[app].applicationLogoReference

                }
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
        };

        //HANDLE ERRORS
        $scope.errorMessage = null;

        //function to check possible outputs for the end user information.
        var checkStatusCallback = function(data, status, headers, config){
            if(status==401) {
                //Unauthorised. Check if signed in.
                if(Principal.isAuthenticated()){
                    $scope.errorMessage = "You have no permissions to do so. Ask for more permissions to the administrator";
                } else {
                    $scope.errorMessage = "Your session has ended. Sign in again. Redirecting to login...";
                    $timeout(function() {
                        $state.go('login');
                    }, 3000);
                }
                return false;
            }else if(status!=200 && status!=201) {
                //Show message
                $scope.errorMessage = "An error occurred. Wait a moment and try again, if problem persists contact the administrator";
                return false;
            } else {
                //Return to the list
                return true;
            }
        };
    }
);