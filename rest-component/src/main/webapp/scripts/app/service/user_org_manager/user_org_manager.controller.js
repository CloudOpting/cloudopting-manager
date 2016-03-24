'use strict';

angular.module('cloudoptingApp')
    .controller('UserOrgManagerController', function (SERVICE, localStorageService,
                                                      $scope, $filter, $state, $log, $window, $timeout, $translate,
                                                      Principal, Auth, UserService, OrganizationService) {

        /// USERS ////////////////////////////////
        $scope.currentPageU = 0;
        $scope.pageSizeU = 8;
        $scope.users = [];
        $scope.searchTextUser = '';
        $scope.numberOfPagesU = function(){
            return Math.ceil($scope.dataLengthU()/$scope.pageSizeU);
        };
        $scope.dataLengthU = function(){
            return $filter('filter')($scope.users, $scope.searchTextUser).length;
        };

        $scope.user = localStorageService.get(SERVICE.STORAGE.USER_MANAGER.USER);

        $scope.languages = [
            { langKey: "en", langName: "English" },
            { langKey: "it", langName: "Italian" },
            { langKey: "es", langName: "Spanish" },
            { langKey: "ca", langName: "Catalan" }
        ];

        $scope.roles = [
            'ROLE_ADMIN',
            'ROLE_USER',
            'ROLE_OPERATOR',
            'ROLE_SUBSCRIBER',
            'ROLE_PUBLISHER'
        ];


        //Get all users
        var findAllCallback = function(data, status, headers, config){
            checkStatusCallback(data, status, headers, config, null);
            if(data){
                $scope.users = data;
            }
        };
        UserService.findAll('', '', '', '', '', findAllCallback);

        $scope.createUserPage = function() {
            $scope.user = null;
            localStorageService.set(SERVICE.STORAGE.USER_MANAGER.USER, null);
            $state.go('user_detail_manager');
        };

        $scope.deleteUser = function(idUser) {
            if($window.confirm('Are you sure that you want to delete this user?')) {
                var deleteCallback = function(data, status, headers, config) {
                    checkStatusCallback(data, status, headers, config, 'user_manager');
                };

                //Delete user
                UserService.delete(idUser, deleteCallback);
            } else {
                //Nothing to do.
            }
        };

        $scope.submitUserForm = function(userId) {
            if(userId) {
                saveUser();
            } else {
                createUser();
            }
        };

        $scope.editUser = function(user) {
            localStorageService.set(SERVICE.STORAGE.USER_MANAGER.USER, user);
            $state.go('user_detail_manager');
        };

        var createUser = function() {
            var callback = function(data, status, headers, config){
                checkStatusCallback(data, status, headers, config, 'user_manager');
            };
            UserService.create($scope.user, callback);
        };

        var saveUser = function() {
            var callback = function(data, status, headers, config){
                checkStatusCallback(data, status, headers, config, 'user_manager');
            };
            UserService.update($scope.user, callback);
        };
        //////////////////////////////////////////


        /// ORGANIZATIONS ////////////////////////
        $scope.currentPageO = 0;
        $scope.pageSizeO = 8;
        $scope.organizations = [];
        $scope.searchTextOrg = '';
        $scope.numberOfPagesO = function(){
            return Math.ceil($scope.dataLengthO()/$scope.pageSizeO);
        };
        $scope.dataLengthO = function(){
            return $filter('filter')($scope.organizations, $scope.searchTextOrg).length;
        };

        $scope.org = localStorageService.get(SERVICE.STORAGE.ORG_MANAGER.ORGANIZATION);

        $scope.status = null;
        $scope.types = null;

        //Get all organizations
        var findAllOrgsCallback = function(data, status, headers, config){
            checkStatusCallback(data, status, headers, config, null);
            if(data){
                $scope.organizations = data;
            }
        };
        OrganizationService.findAll('', '', '', '', '', findAllOrgsCallback);

        var getTypesCallback = function(data, status, headers, config){
            checkStatusCallback(data, status, headers, config, null);
            if(data){
                $scope.types = data;
            }
        };
        OrganizationService.getTypes(getTypesCallback);

        var getStatusCallback = function(data, status, headers, config){
            checkStatusCallback(data, status, headers, config, null);
            if(data){
                $scope.status = data;
            }
        };
        OrganizationService.getStatus(getStatusCallback);

        $scope.createOrganizationPage = function() {
            $scope.org = null;
            localStorageService.set(SERVICE.STORAGE.ORG_MANAGER.ORGANIZATION, null);
            $state.go('org_detail_manager');
        };

        $scope.deleteOrganization = function(idOrganization) {
            if($window.confirm('Are you sure that you want to delete this organization?')) {
                var deleteCallback = function(data, status, headers, config) {
                    checkStatusCallback(data, status, headers, config, 'org_manager');
                };
                //Delete organization.
                OrganizationService.delete(idOrganization, deleteCallback);
            } else {
                //Nothing to do.
            }
        };

        $scope.submitOrganizationForm = function(orgId) {
            if(orgId) {
                saveOrganization();
            } else {
                createOrganization();
            }
        };

        $scope.editOrganization = function(organization) {
            localStorageService.set(SERVICE.STORAGE.ORG_MANAGER.ORGANIZATION, organization);
            $state.go('org_detail_manager');
        };

        var createOrganization = function() {
            var callback = function(data, status, headers, config){
                checkStatusCallback(data, status, headers, config, 'org_manager');
            };
            OrganizationService.create($scope.org, callback);
        };

        var saveOrganization = function() {
            var callback = function(data, status, headers, config){
                checkStatusCallback(data, status, headers, config, 'org_manager');
            };
            OrganizationService.update($scope.org, callback);
        };

        /////////////////
        // ERROR HANDLING
        /////////////////

        $scope.errorMessage = null;

        //function to check possible outputs for the end user information.
        var checkStatusCallback = function(data, status, headers, config, okpage){
            if(status==401) {
                //Unauthorised. Check if signed in.
                if(Principal.isAuthenticated()){
                    $scope.errorMessage = $translate.use("callback.no_permissions");
                } else {
                    $scope.errorMessage = $translate.use("callback.session_ended");
                    $timeout(function() {
                        $state.go('login');
                    }, 3000);
                }
            }else if(status!=200 && status!=201) {
                //Show message
                $scope.errorMessage = $translate.use("callback.generic_error");

            } else {
                //Return to the list
                if(okpage!=null) {
                    $state.go(okpage, {}, {reload: true});
                }
            }
        };

        $scope.cancel = function(page) {
            $state.go(page);
        }

    }
);
