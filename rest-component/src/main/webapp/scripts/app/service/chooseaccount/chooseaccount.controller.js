'use strict';

angular.module('cloudoptingApp')
    .controller('ChooseAccountController',
    function (SERVICE, Principal, $rootScope, $scope, $state, $timeout, $log,
              localStorageService, OrganizationService, InstanceService) {

        var instance = localStorageService.get(SERVICE.STORAGE.CHOOSE_ACCOUNT.INSTANCE);

        //Get accounts for organization and show it.
        var findByIdCallback = function(data, status, headers, config){
            checkStatusCallback(data, status, headers, config);
            if(data){
                $scope.org = data;
                $scope.cloudAccountsList = data.cloudAccountss;
            }
        };
        OrganizationService.findById(instance.customerOrganizationId.id, findByIdCallback);

        //Selected organization, execute the function with the account selected.
        $scope.choose = function(cloudAccount) {
            var callback = function(data, status, headers, config) {
                checkStatusCallback(data, status, headers, config);
                $state.go('dashboard');
            };
            var instanceWithAccount = {};
            instanceWithAccount.id = instance.id;
            instanceWithAccount.cloudAccountId = cloudAccount.id;
            InstanceService.update(instanceWithAccount, callback);
        };

        //Error handling
        $scope.errorMessage = null;

        //function to check possible outputs for the end user information.
        var checkStatusCallback = function(data, status, headers, config){
            if(status==401) {
                //Unauthorised. Check if signed in.
                if(Principal.isAuthenticated()){
                    $scope.errorMessage = "You have no permissions to do so. Ask for more permissions to the administrator";
                } else {
                    $scope.errorMessage = "Your session has ended. Sign in again. Redirecting to login...";
                    $timeout(function() {
                        $state.go('login');
                    }, 3000);
                }
            }else if(status!=200 && status!=201) {
                //Show message
                $scope.errorMessage = "An error occurred. Wait a moment and try again, if problem persists contact the administrator";
            } else {
                $log.info("Service customization succeeded with data: " + data);
            }
        };
    }
);