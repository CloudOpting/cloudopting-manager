'use strict';

angular.module('cloudoptingApp')
    .controller('ChooseAccountController', function (SERVICE, $rootScope, $scope, $state, $timeout, localStorageService) {

        var organization = localStorageService.set(SERVICE.STORAGE.WIZARD_INSTANCES.ORGANIZATION);
        var func = localStorageService.set(SERVICE.STORAGE.WIZARD_INSTANCES.FUNCTION);

        //Get account for organization and show it.
        $scope.cloudAccountsList = organization.cloudAccount;

        //Selected organization, execute the function with the account selected.
        $scope.choose = function(cloudAccount) {
            var callback = function() {
                $state.go('instances');
            };
            func(cloudAccount, callback);
        };

        $scope.goToInstances = function() {
            $state.go('instances');
        } ;
    }
);