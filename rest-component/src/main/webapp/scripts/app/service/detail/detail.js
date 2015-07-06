'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('detail', {
                parent: 'service',
                url: '/detail',
                data: {
                    roles: ['ROLE_ADMIN', 'ROLE_SUBSCRIBER', 'ROLE_OPERATOR', 'ROLE_PUBLISHER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/detail/detail.html',
                        controller: 'DetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('detail');
                        return $translate.refresh();
                    }]
                }
            });
    });
