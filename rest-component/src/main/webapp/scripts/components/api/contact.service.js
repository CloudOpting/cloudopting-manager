'use strict';

angular.module('cloudoptingApp')
    .factory('Contact', function Register($resource) {
        return $resource('api/contact', {}, {});
    });


