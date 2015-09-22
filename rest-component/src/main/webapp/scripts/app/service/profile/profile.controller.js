'use strict';

angular.module('cloudoptingApp')
    .controller('ProfileController', function (SERVICE, $scope, Principal, Auth) {
        $scope.tab_selected = 'tab_settings';
        $scope.changeTab = function(id) {
            $scope.tab_selected = id;
        }

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
                    $scope.passEuccess = 'OK';
                }).catch(function () {
                    $scope.passSuccess = null;
                    $scope.passError = 'ERROR';
                });
            }
        };

    }
);
