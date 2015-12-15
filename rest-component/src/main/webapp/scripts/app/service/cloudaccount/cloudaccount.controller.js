'use strict';

angular.module('cloudoptingApp')
    .controller('CloudAccountController', function(SERVICE, $location, $translate, $scope, $log, $state, localStorageService, Contact, Providers) {

        function resetMessages(){
            $scope.successNew = null;
            $scope.successUpdate = null;
            $scope.error = null;
        };

        resetMessages();

        $scope.cloudAcc = localStorageService.get(SERVICE.STORAGE.CURRENT_CLOUDACCOUNT);

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
                } else {
                    $scope.error = true;
                }
            };

            //CloudAccountService.save($scope.cloudAcc, callback);
        };

        $scope.updateCloudAccount = function () {
            resetMessages();

            var callback = function (data){
                if(data) {
                    $scope.successUpdate = true;
                } else {
                    $scope.error = true;
                }
            };

            //CloudAccountService.update($scope.cloudAcc, callback);
        };

        $scope.deleteCloudAccount = function (cloudAccount) {
            resetMessages();

            var callback = function(data){
                if(data) {
                    $state.go('profile', { tab: "tab_cloudaccounts" } );
                } else {
                    $scope.error = true;
                }
            };

            //CloudAccountService.delete(cloudAccount, callback);
        };

        $scope.goToProfile = function(){
            $state.go('profile', { tab: "tab_cloudaccounts" } );
        }
    }
);
