'use strict';

angular.module('cloudoptingApp')
    .controller('DetailController', function(SERVICE, $location, $translate, $scope, $log, $state, localStorageService, Principal) {

        $scope.currentApp = localStorageService.get(SERVICE.STORAGE.CURRENT_APP);
        $scope.showButton = true;

        //IF the status of the services is "UNFINISHED" we have to set the button "GO TO EDIT" if it is the Publisher


        //If not application go to catalog.
        if($scope.currentApp == undefined || $scope.currentApp == null ){
            $state.go('catalog');
        }

        if(Principal.isInRole(SERVICE.ROLE.PUBLISHER)){
            //TODO: Define all status possible for an APPLICATION.
            if($scope.currentApp.status === SERVICE.STATUS.UNFINISHED){
                $scope.detail_function = function() {
                    $state.go('publish');
                    //TODO: Set the current application to the application service?
                    //ApplicationService.currentApplication = appDetail;
                };
                $scope.buttonValue = $translate.instant('detail.button.completepublish');
            } else {
                $scope.detail_function = function() {
                    $state.go('instances');
                };
                $scope.buttonValue = $translate.instant('detail.button.instances');
            }
        }
        else if(Principal.isInAnyRole([SERVICE.ROLE.ADMIN, SERVICE.ROLE.OPERATOR]))
        {
            $scope.detail_function = function() {
                $state.go('instances');
            };
            $scope.buttonValue = $translate.instant('detail.button.instances');
        }
        else if(Principal.isInRole(SERVICE.ROLE.SUBSCRIBER))
        {
            $scope.detail_function = function() {
                //$state.go('taylor');
                $state.go('form_generation');
            };
            $scope.buttonValue = $translate.instant('detail.button.subscribe');
        }
        else
        {
            //hide button.
            //If no detail_funtion is defined, the button will remain hidden.
            $scope.showButton = false;
        }
    }
);
