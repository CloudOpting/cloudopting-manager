'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('privacy_policy', {
                parent: 'service',
                url: '/privacy_policy',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/privacy_policy/privacy_policy.html'
                    }
                }
            });
    });
