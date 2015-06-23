'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('service', {
                abstract: true,
                parent: 'site'
            });
    });
