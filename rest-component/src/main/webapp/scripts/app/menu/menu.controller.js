'use strict';

angular.module('cloudoptingApp')
    .controller('MenuController', function (SERVICE, $state, $scope, Principal, Auth) {

        $scope.logoutButton = Principal.isAuthenticated;
        //$scope.name = Principal.isAuthenticated ? Principal.identity().login : '';
        Principal.identity().then(function(account) {
            $scope.name = account.login;
        });

        $scope.logout = function(){
            Auth.logout();
            $state.go("login");
        };

        $scope.$watch(
            function() {
                return Principal.identity();
            },
            function(newVal, oldVal)
            {
                $scope.logoutButton = Principal.isAuthenticated;
                //$scope.name = Principal.isAuthenticated ? Principal.identity().login : '';
                Principal.identity().then(function(account) {
                    $scope.name = account.login;
                });

                $scope.isPublisher = function (){
                    return Principal.isInRole(SERVICE.ROLE.SUBSCRIBER);
                };

                $scope.logout = function(){
                    Auth.logout();
                    $state.go("catalog");
                };
            },
            true
        );
    }
);
