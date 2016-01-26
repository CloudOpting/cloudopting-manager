'use strict';

angular.module('cloudoptingApp')
    .controller('ProfileController', function (SERVICE, $scope, $state, $stateParams, $location, Principal, Auth, localStorageService, OrganizationService) {


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
            Auth.updateAccount($scope.settingsAccount).then(function() {
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
                Auth.changePassword($scope.password).then(function () {
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
        localStorageService.set(SERVICE.STORAGE.CURRENT_CLOUDACCOUNT, null);

        $scope.$watch( "$scope.settingsAccount" , function(){

            OrganizationService.getCloudAccount($scope.settingsAccount.organizationId.id,
                function(data) {
                    $scope.cloudAccList = data;
                });


        },true);

        /*
        $scope.cloudAccList = [
            {
                "id": 0,
                "customizationss": [
                    {
                        "customerOrganizationId": "Organizations",
                        "cloudAccount": "CloudAccounts",
                        "applicationId": 0,
                        "customizationToscaFile": "",
                        "customizationCreation": "",
                        "customizationActivation": "",
                        "customizationDecommission": "",
                        "statusId": 0,
                        "processId": "",
                        "id": 0
                    }
                ],
                "providerId": {
                    "provider": "CloudStack",
                    "id": 0
                },
                "name": "CloudAccoount 1",
                "apiKey": "",
                "secretKey": "",
                "endpoint": ""
            }
        ];
        */

        $scope.deleteCloudAccount = function (cloudAccount) {

            var callback = function(data, status, headers, config){
                if(status==200) {
                    $state.go('profile', { tab: "tab_cloudaccounts" }, {reload: true} );
                } else {
                    console.log("Error deleting the Cloud Account");
                }
            };

            OrganizationService.deleteCloudAccount($scope.settingsAccount.organizationId.id, cloudAccount.id, callback);
        };

        $scope.goToEdit = function(cloudAccount){
            //Save the cloudAccount on a place where edit can get it.
            localStorageService.set(SERVICE.STORAGE.CURRENT_CLOUDACCOUNT, cloudAccount);
            localStorageService.set(SERVICE.STORAGE.CURRENT_EDIT_ORG, $scope.settingsAccount.organizationId);
            $state.go('cloudaccount');

        };


        $scope.goToAdd = function(){
            //Save the cloudAccount on a place where edit can get it.
            localStorageService.set(SERVICE.STORAGE.CURRENT_CLOUDACCOUNT, null);
            localStorageService.set(SERVICE.STORAGE.CURRENT_EDIT_ORG, $scope.settingsAccount.organizationId);
            $state.go('cloudaccount');

        };

    }
);
