'use strict';
angular.module('cloudoptingApp').filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        if(input==null) return;
        return input.slice(start);
    }
});

angular.module('cloudoptingApp')
    .controller('CatalogueController', function (SERVICE, localStorageService,
                                                 $scope, $log, $state, $stateParams, $document, $timeout,
                                                 ApplicationService, Principal, JackrabbitService) {
        $scope.scrollToSection = function () {
            $timeout(function () {
                if($stateParams.section!=null && $stateParams.section!=undefined && $stateParams.section!="") {
                    var someElement = angular.element(document.getElementById('contact'));
                    $document.scrollToElementAnimated( someElement, 30, 500 );
                    $stateParams.section = null;
                }
            }, 500);
        };

        $scope.applicationList = null;

        var callback = function(data, status, headers, config) {
            if(checkStatusCallback(data, status, headers, config)){
                var appList = data.content;

                //Select only applications with "Published" status.
                $scope.applicationList = [];
                for (var app in appList) {
                    if(appList[app].statusId.status == "Published") {
                        $scope.applicationList.push(appList[app]);
                    }
                }

                //Do the pagination
                pagination();

                //Get the images for each application.
                getImagesForAllApplications($scope.applicationList);

                //At the end do the scrolling
                $scope.scrollToSection();
            }
        };

        //TODO: Change findAllUnpaginated to findAll
        ApplicationService.findAllUnpaginated(callback);

        var getImagesForAllApplications = function(list){
            var defaultImage = "http://placehold.it/350x150";

            for (var app in list) {

                //If no image defined set a default image;
                if (list[app].applicationLogoReference == null
                    || list[app].applicationLogoReference == undefined
                    || list[app].applicationLogoReference == "")
                {
                    list[app].applicationLogoReference = defaultImage;
                }
                else {
                    //Changing the URL only instead of calling the rest API with AJAX
                    list[app].applicationLogoReference = buildImagePath(list[app].applicationLogoReference);

                    /*
                     var callback = function(data, status, headers, config){
                        if(checkStatusCallback(data, status, headers, config)){
                            if(data){
                                list[app].applicationLogoReference = data;
                            }
                        }
                     };
                     JackrabbitService.findImage(list[app].applicationLogoReference, callback);
                     */

                }
            }
        };

        var buildImagePath = function(path){
            return "api/jr/img?jcrPath=" + encodeURIComponent(path);
        };

        //Save the current application & go to the detail
        $scope.detail = function(application){
            localStorageService.set(SERVICE.STORAGE.DETAIL.APPLICATION, application);
            $state.go('detail');
        };

        //////////
        // PAGINATION
        //////////

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

        //////////
        // HANDLE ERRORS
        //////////

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
                $scope.errorMessage = "An error occurred. Wait a moment and try again, if problem persists contact the administrator";
                return false;
            } else {
                return true;
            }
        };
    }
);