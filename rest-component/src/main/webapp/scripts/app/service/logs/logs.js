'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('logs', {
                parent: 'service',
                url: '/logs',
                data: {
                    roles: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/logs/logs.html',
                        controller: 'LogsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('service_logs');
                        return $translate.refresh();
                    }]
                }
            });
    });
