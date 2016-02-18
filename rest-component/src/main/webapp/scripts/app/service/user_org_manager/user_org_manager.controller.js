'use strict';

angular.module('cloudoptingApp')
    .controller('UserOrgManagerController', function (SERVICE, $scope, Principal, Auth, UserService, $filter,
                                                      OrganizationService, $state, $log, localStorageService, $window, $timeout) {

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

        $scope.user = localStorageService.get(SERVICE.STORAGE.CURRENT_EDIT_USER);

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
        UserService.findAll()
            .success(function (users) {
                $scope.users = users;
            });

        $scope.createUserPage = function() {
            $scope.user = null;
            localStorageService.set(SERVICE.STORAGE.CURRENT_EDIT_USER, null);
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
            localStorageService.set(SERVICE.STORAGE.CURRENT_EDIT_USER, user);
            $state.go('user_detail_manager');
        };

        var createUser = function() {
            var callback = function(data, status, headers, config){
                checkStatusCallback(data, status, headers, config, 'user_manager');
            };
/*
            for(var r in $scope.user.roles) {
                $scope.user.roles.push(r);
            }*/
            //$scope.user.roles = [ '"' + $scope.user.roles + '"' ];
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

        $scope.org = localStorageService.get(SERVICE.STORAGE.CURRENT_EDIT_ORG);

        $scope.status = null;
        $scope.types = null;

        //Get all organizations
        OrganizationService.findAll()
            .success(function (organizations) {
                $scope.organizations = organizations;
            });

        OrganizationService.getTypes()
            .success(function (types) {
                $scope.types = types;
            });

        OrganizationService.getStatus()
            .success(function (status) {
                $scope.status = status;
            });

        $scope.createOrganizationPage = function() {
            $scope.org = null;
            localStorageService.set(SERVICE.STORAGE.CURRENT_EDIT_ORG, null);
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
            localStorageService.set(SERVICE.STORAGE.CURRENT_EDIT_ORG, organization);
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
        //////////////////////////////////////////
        $scope.errorMessage = null;

        //function to check possible outputs for the end user information.
        var checkStatusCallback = function(data, status, headers, config, okpage){
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
                //Return to the list
                $state.go(okpage, {}, {reload: true});
            }
        };

        $scope.cancel = function(page) {
            $state.go(page);
        }

    }
);
