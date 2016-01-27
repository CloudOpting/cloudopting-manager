'use strict';

angular.module('cloudoptingApp')
    .controller('ChooseAccountController', function (SERVICE, $rootScope, $scope, $state, $timeout, localStorageService, OrganizationService) {

        var organization = localStorageService.get(SERVICE.STORAGE.WIZARD_INSTANCES.ORGANIZATION);
        var func = localStorageService.get(SERVICE.STORAGE.WIZARD_INSTANCES.FUNCTION);

        //Get account for organization and show it.

        OrganizationService.findById(organization.id)
            .success(function (organization) {
                $scope.org = organization;
                $scope.cloudAccountsList = organization.cloudAccountss;
            });


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