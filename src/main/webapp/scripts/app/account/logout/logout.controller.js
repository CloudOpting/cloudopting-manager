'use strict';

angular.module('cloudoptingApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
