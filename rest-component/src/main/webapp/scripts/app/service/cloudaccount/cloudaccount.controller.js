'use strict';

angular.module('cloudoptingApp')
    .controller('CloudAccountController', function(SERVICE, $location, $translate, $scope, $log, $state, localStorageService, Contact, Providers, OrganizationService) {

        function resetMessages(){
            $scope.successNew = null;
            $scope.successUpdate = null;
            $scope.error = null;
        };

        resetMessages();

        $scope.cloudAcc = localStorageService.get(SERVICE.STORAGE.CURRENT_CLOUDACCOUNT);
        $scope.organization = localStorageService.get(SERVICE.STORAGE.CURRENT_EDIT_ORG);

        Providers.get(function(providers) {
            $scope.providerList =  providers;
        });

        $scope.cancel = function () {
            $state.go('profile', { tab: "tab_cloudaccounts" } );
        };


        $scope.createCloudAccount = function (cloudAccount) {
            resetMessages();

            var callback = function (data){
                if(data) {
                    $scope.successNew = true;
                } else {
                    $scope.error = true;
                }
            };

            OrganizationService.createCloudAccount($scope.organization.id, cloudAccount, callback);
        };

        $scope.updateCloudAccount = function (cloudAccount) {
            resetMessages();

            var callback = function (data){
                if(data) {
                    $scope.successUpdate = true;
                } else {
                    $scope.error = true;
                }
            };

            OrganizationService.updateCloudAccount($scope.organization.id, cloudAccount, callback);
        };


        $scope.deleteCloudAccount = function (cloudAccount) {

            var callback = function(data){
                if(data) {
                    $state.go('profile', { tab: "tab_cloudaccounts" } );
                } else {
                    console.log("Error deleting the Cloud Account");
                }
            };

            OrganizationService.deleteCloudAccount($scope.organization.id, cloudAccount, callback);
        };

        $scope.goToProfile = function(){
            $state.go('profile', { tab: "tab_cloudaccounts" } );
        }
    }
);
