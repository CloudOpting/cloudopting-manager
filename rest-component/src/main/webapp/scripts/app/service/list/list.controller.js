'use strict';

angular.module('cloudoptingApp')
    .controller('ListController', function (SERVICE, $rootScope, $scope, $state, $timeout, localStorageService, Principal, Auth, ApplicationService, $window) {
        //TODO: Change applicationListUnpaginated to applicationList once it is developed properly
        $scope.applicationList = null;

        //TODO: Depending on the role, give the user a different list

        if(Principal.isInRole(SERVICE.ROLE.ADMIN)) {
            var callback = function (applications) {
                $scope.applicationList = applications.content;
            };
            ApplicationService.findAllUnpaginated(callback);
        }
        else if(Principal.isInRole(SERVICE.ROLE.PUBLISHER)) {
            var callback = function (applications) {
                $scope.applicationList = applications.content;
            };
            ApplicationService.findAllUnpaginated(callback);
        }

        //TODO: Implement button "Search Service" functionality.

        //TODO: Implement button go for each instance.

        //Function to get to publish a new service
        $scope.goToPublish = function () {
            //Redirect to publication
            $state.go('publish');
        };

        //Function to go to the instances detail.
        $scope.goToEdit = function (app) {
            //Save the ID on a place where edit can get it.
            localStorageService.set(SERVICE.STORAGE.CURRENT_APP, app);

            $window.alert("This functionality is not ready yet. For any inconvenience, contact cloudopting@gmail.com");

            //Redirect to instances
            //$state.go('edit');
        };

        //Function to go to the instances detail.
        $scope.goToInstanceList = function (app) {
            //Save the ID on a place where instances can get it.
            localStorageService.set(SERVICE.STORAGE.CURRENT_APP, app);

            //Redirect to instances
            $state.go('instances');
        };

        //Function to delete a service.
        $scope.goToDelete = function (app) {

            if($window.confirm('Are you sure that you want to delete this service?')) {
                var callback = function(data) {
                    //Deleteing current service on storage.
                    localStorageService.set(SERVICE.STORAGE.CURRENT_APP, null);

                    //Reload page
                    $state.go($state.current, {}, {reload: true});

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
    }
);