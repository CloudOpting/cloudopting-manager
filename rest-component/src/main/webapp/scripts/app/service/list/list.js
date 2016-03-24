'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('list', {
                parent: 'service',
                url: '/list',
                data: {
                    roles: ['ROLE_ADMIN', 'ROLE_PUBLISHER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/list/list.html',
                        controller: 'ListController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('list');
                        $translatePartialLoader.addPart('callback');
                        return $translate.refresh();
                    }]
                }
            });
    });
