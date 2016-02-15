'use strict';

angular.module('cloudoptingApp')
    .controller('ListController', function (SERVICE, $rootScope, $scope, $state, $timeout, $log, $filter,
                                            localStorageService, Principal, Auth, ApplicationService, $window) {
        //TODO: Change applicationListUnpaginated to applicationList once it is developed properly
        $scope.currentPage = 0;
        $scope.pageSize = 8;
        $scope.applicationList = [];
        $scope.searchTextApplication = '';
        $scope.numberOfPages = function(){
            return Math.ceil($scope.dataLength()/$scope.pageSize);
        };
        $scope.dataLength = function(){
            return $filter('filter')($scope.applicationList, $scope.searchTextApplication).length;
        };

        //Depending on the role, give the user a different list
        if(Principal.isInRole(SERVICE.ROLE.ADMIN)) {
            var callback = function (data, status, headers, config) {
                if(checkStatusCallback(data, status, headers, config)) {
                    $scope.applicationList = data.content;
                }
            };
            ApplicationService.findAllUnpaginated(callback);
        }
        else if(Principal.isInRole(SERVICE.ROLE.PUBLISHER)) {
            var callback = function (data, status, headers, config) {
                if(checkStatusCallback(data, status, headers, config)) {
                    var user = localStorageService.get(SERVICE.STORAGE.CURRENT_USER);
                    for(var org in data.content){
                        if(user.organizationId.id == data.content[org].organizationId.id){
                            $scope.applicationList.push(data.content[org]);
                        }
                    }
                }
            };
            ApplicationService.findAllUnpaginated(callback);
        }

        //Function to get to publish a new service
        $scope.goToPublish = function () {
            //Redirect to publication
            $state.go('publish');
        };

        //Function to go to the instances detail.
        $scope.goToEdit = function (app) {
            //Save the ID on a place where edit can get it.
            localStorageService.set(SERVICE.STORAGE.CURRENT_APP, app);

            localStorageService.set(SERVICE.STORAGE.PUBLISH_EDITION, true);

            //Redirect to instances
            $state.go('publish');
        };

        //Function to go to the instances detail.
        $scope.goToInstanceList = function (app) {
            //Save the ID on a place where instances can get it.
            localStorageService.set(SERVICE.STORAGE.CURRENT_APP, app);
            //FIXME: Do we have to set it as a TESTING deployment?

            //Redirect to instances
            $state.go('instances');
        };

        //Function to delete a service.
        $scope.goToDelete = function (app) {

            if($window.confirm('Are you sure that you want to delete this service?')) {
                var callback = function(data, status, headers, config) {
                    if(checkStatusCallback(data, status, headers, config)) {
                        //Deleteing current service on storage.
                        localStorageService.set(SERVICE.STORAGE.CURRENT_APP, null);

                        //Reload page
                        $state.go($state.current, {}, {reload: true});
                    }
                };
                //Deleting a service
                ApplicationService.delete(app.id, callback);
            } else {
            //Nothing to do.
            }
        };

        //Function to go to the instances detail.
        $scope.goToCreateInstance = function (app) {
            //Save the ID on a place where createinstance can get it.
            localStorageService.set(SERVICE.STORAGE.CURRENT_APP, app);

            //Redirect to instances
            $state.go('form_generation');
        };

        //////////////////////////////////////////
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
            } else if(status!=200 && status!=201) {
                //Show message
                $scope.errorMessage = "An error occurred. Wait a moment and try again, if problem persists contact the administrator";
                return false;
            } else {
                $log.info("Everything went ok!");
                return true;
            }
        };
    }
);