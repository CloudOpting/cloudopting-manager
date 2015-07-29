'use strict';

angular.module('cloudoptingApp')
    .controller('ListController', function (SERVICE, $rootScope, $scope, $state, $timeout, localStorageService, Principal, Auth, ApplicationService) {
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



            //Redirect to instances
            //$state.go('edit');
        };

        //Function to go to the instances detail.
        $scope.goToInstanceList = function (appId) {
            //Save the ID on a place where instances can get it.
            localStorageService.set(SERVICE.STORAGE.CURRENT_APP, app);


            //Redirect to instances
            $state.go('instances');
        };

        //Function to delete a service.
        $scope.goToDelete = function (app) {
            //Deleting a serive
            ApplicationService.delete(app, '123456');

            //Deleteing current service on storage.
            localStorageService.set(SERVICE.STORAGE.CURRENT_APP, null);
            //ApplicationService

            //$state.go('delete');
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