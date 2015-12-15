'use strict';

angular.module('cloudoptingApp')
    .factory('Providers', function Register($resource) {
        return $resource('api/providers', {}, {
            'get': { method: 'GET', params: {}, isArray: true}
        });
    });


