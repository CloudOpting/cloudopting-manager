'use strict';

angular.module('cloudoptingApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


