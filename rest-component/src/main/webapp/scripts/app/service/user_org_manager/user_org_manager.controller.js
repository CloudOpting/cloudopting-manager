'use strict';

angular.module('cloudoptingApp')
    .controller('UserOrgManagerController', function (SERVICE, $scope, Principal, Auth, UserService, OrganizationService, $state, $log, localStorageService, $window) {

        /// USERS ////////////////////////////////
        $scope.user = localStorageService.get(SERVICE.STORAGE.CURRENT_EDIT_USER);
        $scope.users = null;

        $scope.languages = [
            { langKey: "en", langName: "English" },
            { langKey: "it", langName: "Italian" },
            { langKey: "es", langName: "Spanish" },
            { langKey: "ca", langName: "Catalan" }
        ];

        $scope.roles = [
            { roleName: "ROLE_ADMIN" },
            { roleName: "ROLE_USER" },
            { roleName: "ROLE_OPERATOR" },
            { roleName: "ROLE_SUBSCRIBER" },
            { roleName: "ROLE_PUBLISHER" }
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
                var deleteCallback = function(data) {
                    //if data XXX...
                    $state.go($state.current, {}, {reload: true});
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
            var callback = function(data){
                //Return to the list
                $state.go('user_manager');
            };
            $scope.user.roles = [ "\"" + $scope.user.roles + "\"" ];
            UserService.create($scope.user, callback);
        };

        var saveUser = function() {
            var callback = function(data){
                //Return to the list
                $state.go('user_manager');
            };
            UserService.update($scope.user, callback);
        };
        //////////////////////////////////////////


        /// ORGANIZATIONS ////////////////////////
        $scope.org = null;
        $scope.organizations = null;

        //Get all users
        OrganizationService.findAll()
            .success(function (organizations) {
                $scope.organizations = organizations;
            });

        $scope.createOrganizationPage = function() {
            $scope.org = null;
            localStorageService.set(SERVICE.STORAGE.CURRENT_EDIT_ORG, null);
            $state.go('org_detail_manager');
        };

        $scope.deleteOrganization = function(idOrganization) {

            if($window.confirm('Are you sure that you want to delete this user?')) {
                var deleteCallback = function(data) {
                    //if data XXX...
                    $state.go($state.current, {}, {reload: true});
                };
                //Delete organization.
                OrganizationService.delete(idOrganization, deleteCallback);
            } else {
                //Nothing to do.
            }
        };



        $scope.editOrganization = function(organization) {
            localStorageService.set(SERVICE.STORAGE.CURRENT_EDIT_ORG, organization);
            $state.go('org_detail_manager');
        };

        $scope.createOrganization = function() {
            var callback = function(data){
                //Return to the list
                $state.go('org_manager');
            };
            OrganizationService.create($scope.org, callback);
        };

        $scope.saveOrganization = function() {
            var callback = function(data){
                //Return to the list
                $state.go('org_manager');
            };
            UserService.update($scope.org, callback);
        };
        //////////////////////////////////////////

    }
);
