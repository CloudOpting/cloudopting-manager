'use strict';

angular.module('cloudoptingApp')
    .controller('DetailController', function(SERVICE, $location, $translate, $scope, $log, $state, localStorageService, Principal) {

        if(!Principal.isAuthenticated()){
            $state.go('login');
        }
        $scope.currentApp = localStorageService.get(SERVICE.STORAGE.DETAIL.APPLICATION);
        $scope.showButton = true;

        //IF the status of the services is "UNFINISHED" we have to set the button "GO TO EDIT" if it is the Publisher


        //If not application go to catalogue.
        if($scope.currentApp == undefined || $scope.currentApp == null ){
            $state.go('catalogue');
        }
        if(!Principal.isAuthenticated()) {
            $scope.showRegister = true;
        }
        else if(Principal.isInRole(SERVICE.ROLE.PUBLISHER)){
            $scope.isPublisher = true;
            //TODO: Define all status possible for an APPLICATION.
            if($scope.currentApp.status === SERVICE.STATUS.UNFINISHED){
                //Edit application
                $scope.showEditService = true;
            } else {
                $scope.showInstances = true;
            }
        }
        else if(Principal.isInAnyRole([SERVICE.ROLE.ADMIN, SERVICE.ROLE.OPERATOR]))
        {
            $scope.showInstances = true;
        }
        else if(Principal.isInRole(SERVICE.ROLE.SUBSCRIBER))
        {
            $scope.showSubscribe = true;
        }
        else
        {
            //hide button.
            //If no detail_funtion is defined, the button will remain hidden.
            $scope.showButton = false;
        }

        $scope.goToEditService = function(){
            localStorageService.set(SERVICE.STORAGE.PUBLISH.APPLICATION, $scope.currentApp);
            $state.go('publish');
        };

        $scope.goToInstances = function(){
            localStorageService.set(SERVICE.STORAGE.INSTANCES.APPLICATION, $scope.currentApp);
            $state.go('instances');
        };

        $scope.goToRegister = function(){
            $state.go('register');
        };

        $scope.goToSubscribe = function(){
            localStorageService.set(SERVICE.STORAGE.FORM_GENERATION.APPLICATION, $scope.currentApp);
            $state.go('form_generation');
        };
    }
);
