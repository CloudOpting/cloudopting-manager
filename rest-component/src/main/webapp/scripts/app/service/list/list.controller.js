'use strict';

angular.module('cloudoptingApp')
    .controller('ListController', function ($rootScope, $scope, $state, $timeout, Auth, RestApi) {
        //function ($scope, $log, $location, RestApi, ApplicationService) {
        //TODO: Change applicationListUnpaginated to applicationList once it is developed properly
        $scope.applicationList = RestApi.applicationListUnpaginated();

        //TODO: Implement button "Search Service" functionality.

        //TODO: Implement button go for each instance.

        //Function to get to publish a new service
        $scope.goToPublish = function () {
            //Redirect to publication
            $state.path('publish');
        };

        //Function to go to the instances detail.
        $scope.goToEdit = function (appId) {
            //TODO: Save the ID on a place where edit can get it.


            //Redirect to instances
            //$state.path('edit');
        };

        //Function to go to the instances detail.
        $scope.goToInstanceList = function (appId) {
            //TODO: Save the ID on a place where instances can get it.


            //Redirect to instances
            $state.path('instances');
        };

        //Function to go to the instances detail.
        $scope.goToDelete = function (appId) {
            //TODO: Save the ID on a place where delete can get it.


            //Redirect to instances
            //$state.path('delete');
        };

        //Function to go to the instances detail.
        $scope.goToCreateInstance = function (appId) {
            //TODO: Save the ID on a place where createinstance can get it.


            //Redirect to instances
            //$state.path('createinstance');
        };
    });