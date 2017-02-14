'use strict';

angular.module('cloudoptingApp')
    .controller('ProfileController', function (SERVICE, localStorageService,
                                               $scope, $state, $stateParams, $location,
                                               Principal, Auth, OrganizationService) {


        $scope.hideAddEditButton = Principal.isInRole(SERVICE.ROLE.OPERATOR) || Principal.isInRole(SERVICE.ROLE.PUBLISHER);
        $scope.isOrgEditable = Principal.isInRole(SERVICE.ROLE.ADMIN) == true;

        $scope.tab_selected = 'tab_settings';
        if($stateParams.tab!=null && $stateParams.tab!=undefined && $stateParams.tab!="") {
            $scope.tab_selected = $stateParams.tab;
        }

        $scope.changeTab = function(id) {
            $scope.tab_selected = id;
        };

        //SETTINGS
        $scope.settingsSuccess = null;
        $scope.settingsError = null;
        Principal.identity().then(function(account) {
            $scope.settingsAccount = account;
        });

        $scope.save = function () {
            return Auth.updateAccount($scope.settingsAccount).then(function() {
                $scope.settingsError = null;
                $scope.settingsSuccess = 'OK';
                Principal.identity().then(function(account) {
                    $scope.settingsAccount = account;
                });
            }).catch(function() {
                $scope.settingsSuccess = null;
                $scope.settingsError = 'ERROR';
            });
        };

        //PASSWORD
        Principal.identity().then(function(account) {
            $scope.passAccount = account;
        });

        $scope.passSuccess = null;
        $scope.passError = null;
        $scope.doNotMatch = null;
        $scope.changePassword = function () {
            if ($scope.password !== $scope.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
            } else {
                $scope.doNotMatch = null;
                return Auth.changePassword($scope.password).then(function () {
                    $scope.passError = null;
                    $scope.passSuccess = 'OK';
                }).catch(function () {
                    $scope.passSuccess = null;
                    $scope.passError = 'ERROR';
                });
            }
        };

        //ORGANIZATION

        //$scope.organizations = OrganizationService.findAll();
        /*
        Principal.identity().then(function(account) {
            $scope.passAccount = account;
        });

        $scope.passSuccess = null;
        $scope.passError = null;
        $scope.doNotMatch = null;
        $scope.updateOrganization = function () {
            if ($scope.password !== $scope.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
            } else {
                $scope.doNotMatch = null;
                Auth.changePassword($scope.password).then(function () {
                    $scope.passError = null;
                    $scope.passEuccess = 'OK';
                }).catch(function () {
                    $scope.passSuccess = null;
                    $scope.passError = 'ERROR';
                });
            }
        };
*/

        //CLOUD ACCOUNTS
        localStorageService.set(SERVICE.STORAGE.PROFILE.CLOUD_ACCOUNT, null);

        $scope.$watch( "$scope.settingsAccount" , function(){

            var callback = function(data) {
                $scope.cloudAccList = data;
            };
            OrganizationService.getCloudAccount($scope.settingsAccount.organizationId.id, callback);

        }, true);

        $scope.deleteCloudAccount = function (cloudAccount) {

            var callback = function(data, status, headers, config){
                if(status==200) {
                    $state.go('profile', { tab: "tab_cloudaccounts" }, {reload: true} );
                } else {
                    console.log("Error deleting the Cloud Account");
                }
            };

            return OrganizationService.deleteCloudAccount($scope.settingsAccount.organizationId.id, cloudAccount.id, callback);
        };

        $scope.goToEdit = function(cloudAccount){
            //Save the cloudAccount on a place where edit can get it.
            localStorageService.set(SERVICE.STORAGE.CLOUD_ACCOUNT.CLOUD_ACCOUNT, cloudAccount);
            localStorageService.set(SERVICE.STORAGE.CLOUD_ACCOUNT.ORGANIZATION, $scope.settingsAccount.organizationId);
            $state.go('cloudaccount');

        };


        $scope.goToAdd = function(){
            //Save the cloudAccount on a place where edit can get it.
            localStorageService.set(SERVICE.STORAGE.CLOUD_ACCOUNT.CLOUD_ACCOUNT, null);
            localStorageService.set(SERVICE.STORAGE.CLOUD_ACCOUNT.ORGANIZATION, $scope.settingsAccount.organizationId);
            $state.go('cloudaccount');

        };

    }
);
