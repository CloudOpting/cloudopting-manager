'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('instances', {
                parent: 'service',
                url: '/instances',
                data: {
                    roles: ['ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_SUBSCRIBER', 'ROLE_PUBLISHER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/instances/instances.html',
                        controller: 'InstancesController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('instances');
                        $translatePartialLoader.addPart('callback');
                        return $translate.refresh();
                    }]
                }
            });
    });
