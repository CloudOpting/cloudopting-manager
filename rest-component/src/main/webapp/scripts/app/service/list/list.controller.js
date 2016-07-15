'use strict';

angular.module('cloudoptingApp')
    .controller('ListController', function (SERVICE, localStorageService,
                                            $rootScope, $scope, $state, $timeout, $log, $filter, $window, $translate,
                                            Principal, Auth, ApplicationService) {

        $scope.currentPage = 1;
        $scope.pageSize = 8;
        $scope.applicationList = [];
        $scope.searchTextApplication = "";
        $scope.numberOfPages = 0;
        $scope.dataLength = 0;
        var sortBy = "applicationName";
        var sortOrder = "ASC";
/*
        $scope.numberOfPages = function(){
            return Math.ceil($scope.dataLength()/$scope.pageSize);
        };
        $scope.dataLength = function(){
            return $filter('filter')($scope.applicationList, $scope.searchTextApplication).length;
        };
 */

        var callback_findAll = function (data, status, headers, config) {
            if(checkStatusCallback(data, status, headers, config)) {
                $scope.applicationList = data.content;
                $scope.numberOfPages = data.totalPages;
                $scope.dataLength = data.totalElements;
                //Hide waiting gif
            }
        };

        $scope.disableLeftArrow = function(){
            return $scope.currentPage == 1;
        };
        $scope.disableRightArrow = function(){
            return $scope.currentPage >= $scope.dataLength/$scope.pageSize;
        };
        $scope.clickLeftArrow = function() {
            $scope.currentPage = $scope.currentPage - 1;
            ApplicationService.findAll($scope.currentPage, $scope.pageSize, sortBy, sortOrder, null, callback_findAll);
            //Show waiting gif.
        };
        $scope.clickRightArrow = function(){
            $scope.currentPage = $scope.currentPage+1;
            ApplicationService.findAll($scope.currentPage, $scope.pageSize, sortBy, sortOrder, null, callback_findAll);
            //Show waiting gif.
        };
        $scope.searchText = function() {
            $scope.currentPage = 1;
            ApplicationService.findAll($scope.currentPage, $scope.pageSize, sortBy, sortOrder, "applicationName="+$scope.searchTextApplication, callback_findAll);
        };

        //Depending on the role, give the user a different list
        if(Principal.isInRole(SERVICE.ROLE.ADMIN)) {
            //ApplicationService.findAllUnpaginated(callback);
            //var filter;
            ApplicationService.findAll($scope.currentPage, $scope.pageSize, sortBy, sortOrder, null, callback_findAll);
        }
        else if(Principal.isInRole(SERVICE.ROLE.PUBLISHER)) {
            var callback = function (data, status, headers, config) {
                if(checkStatusCallback(data, status, headers, config)) {

                    //Check the organization id of the user to filter the list.
                    Principal.identity().then(function(user) {
                        for(var org in data.content){
                            if(user.organizationId.id == data.content[org].organizationId.id){
                                $scope.applicationList.push(data.content[org]);
                            }
                        }
                    });

                }
            };
            //FIXME: Change findAllUnpaginated for findAll.
            ApplicationService.findAllUnpaginated(callback);
        }

        //Function to get to publish a new service
        $scope.goToPublish = function () {
            localStorageService.set(SERVICE.STORAGE.PUBLISH.APPLICATION, null);
            localStorageService.set(SERVICE.STORAGE.PUBLISH.IS_EDITION, false);
            $state.go('publish');
        };

        //Function to go to edit the service.
        $scope.goToEdit = function (app) {
            localStorageService.set(SERVICE.STORAGE.PUBLISH.APPLICATION, app);
            localStorageService.set(SERVICE.STORAGE.PUBLISH.IS_EDITION, true);
            $state.go('publish');
        };

        //Function to go to the instances list.
        $scope.goToInstanceList = function (app) {
            localStorageService.set(SERVICE.STORAGE.INSTANCES.APPLICATION, app);
            $state.go('instances');
        };

        //Function to delete a service after confirmation of the user.
        $scope.delete = function (app) {

            if($window.confirm('Are you sure that you want to delete this service?')) {
                var callback = function(data, status, headers, config) {
                    if(checkStatusCallback(data, status, headers, config)) {
                        //Reload page
                        $state.go($state.current, {}, {reload: true});
                    }
                };
                return ApplicationService.delete(app.id, callback);
            } else {
                //Nothing to do.
            }
        };

        //Function to go to the form generation screen.
        $scope.goToCreateInstance = function (app) {
            localStorageService.set(SERVICE.STORAGE.FORM_GENERATION.APPLICATION, app);
            $state.go('form_generation');
        };

        //////////////////
        // HANDLE ERRORS
        //////////////////

        $scope.errorMessage = null;

        //function to check possible outputs for the end user information.
        var checkStatusCallback = function(data, status, headers, config){
            if(status==401 || status==403) {
                //Unauthorised. Check if signed in.
                if(Principal.isAuthenticated()){
                    $scope.errorMessage = $translate.instant("callback.no_permissions");
                } else {
                    $scope.errorMessage = $translate.instant("callback.session_ended");
                    $timeout(function() {
                        $state.go('login');
                    }, 3000);
                }
                return false;
            } else if(status!=200 && status!=201) {
                //Show message
                $scope.errorMessage = $translate.instant("callback.generic_error");
                return false;
            } else {
                $log.info("Everything went ok!");
                return true;
            }
        };
    }
);