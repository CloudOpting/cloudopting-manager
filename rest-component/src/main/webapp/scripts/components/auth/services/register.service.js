'use strict';

angular.module('cloudoptingApp')
    .factory('Register', function Register($resource) {
        return $resource('api/register', {}, {});
    });


