'use strict';

angular.module('cloudoptingApp')
    .controller('LoginController', function (SERVICE, localStorageService, $rootScope, $scope, $state, $timeout, Auth) {

        $scope.scrollTo = function(element) {
            $( 'html, body').animate({
                scrollTop: $(element).offset().top
            }, 500);
        };
        $scope.scrollTo( "#page-top");

        $scope.user = {};
        $scope.errors = {};

        $scope.rememberMe = false;
        $timeout(function (){angular.element('[ng-model="username"]').focus();});
        $scope.login = function () {
            Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function () {
                $scope.authenticationError = false;
                if ($rootScope.previousStateName === 'register') {
                    //$state.go('home');
                    var currentApp = localStorageService.get(SERVICE.STORAGE.REGISTER.APPLICATION);
                    if(currentApp!=null) {
                        localStorageService.set(SERVICE.STORAGE.REGISTER.APPLICATION, null)
                        localStorageService.set(SERVICE.STORAGE.DETAIL.APPLICATION, currentApp);
                        $state.go('detail');
                    } else {
                        $state.go('catalogue');
                    }
                } else {
                    $rootScope.back();
                }
            }).catch(function () {
                $scope.authenticationError = true;
            });
        };
    });
