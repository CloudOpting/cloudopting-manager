'use strict';

angular.module('cloudoptingApp')
    .controller('CloudAccountController', function(SERVICE, localStorageService,
                                                   $location, $translate, $scope, $log, $state,
                                                   Principal, Contact, Providers, OrganizationService) {

        function resetMessages(){
            $scope.successNew = null;
            $scope.successUpdate = null;
            $scope.error = null;
        };

        resetMessages();

        $scope.cloudAcc = localStorageService.get(SERVICE.STORAGE.CLOUD_ACCOUNT.CLOUD_ACCOUNT);
        $scope.organization = localStorageService.get(SERVICE.STORAGE.CLOUD_ACCOUNT.ORGANIZATION);

        Providers.get(function(providers) {
            $scope.providerList =  providers;
        });

        $scope.cancel = function () {
            $state.go('profile', { tab: "tab_cloudaccounts" } );
        };


        $scope.createCloudAccount = function () {
            resetMessages();

            var callback = function (data){
                if(data) {
                    $scope.successNew = true;
                    $state.go('profile', { tab: "tab_cloudaccounts" }, {reload: true} );
                } else {
                    $scope.error = true;
                }
            };

            //Set provider for the DTO.
            for(var prov in $scope.providerList){
                if($scope.providerList[prov].id == $scope.cloudAcc.providerId.id) {
                    $scope.cloudAcc.provider = $scope.providerList[prov];
                }
            }

            OrganizationService.createCloudAccount($scope.organization.id, $scope.cloudAcc, callback);
        };

        $scope.updateCloudAccount = function () {
            resetMessages();

            var callback = function (data){
                if(data) {
                    $scope.successUpdate = true;
                    $state.go('profile', { tab: "tab_cloudaccounts" }, {reload: true} );
                } else {
                    $scope.error = true;
                }
            };

            OrganizationService.updateCloudAccount($scope.organization.id, $scope.cloudAcc, callback);
        };


        $scope.deleteCloudAccount = function () {

            var callback = function(data, status, headers, config){
                if(status==200) {
                    $state.go('profile', { tab: "tab_cloudaccounts" }, {reload: true} );
                } else {
                    console.log("Error deleting the Cloud Account");
                }
            };

            OrganizationService.deleteCloudAccount($scope.organization.id, $scope.cloudAcc.id, callback);
        };

        $scope.goToProfile = function(){
            $state.go('profile', { tab: "tab_cloudaccounts" } );
        }
    }
);
