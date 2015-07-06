'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('taylor', {
                parent: 'service',
                url: '/taylor',
                data: {
                    roles: ['ROLE_ADMIN', 'ROLE_SUBSCRIBER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/taylor/taylor.html',
                        controller: 'TaylorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taylor');
                        return $translate.refresh();
                    }]
                }
            });
    });
