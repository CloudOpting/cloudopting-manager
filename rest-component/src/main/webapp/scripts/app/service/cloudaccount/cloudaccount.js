'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('cloudaccount', {
                parent: 'service',
                url: '/cloudaccount',
                data: {
                    roles: ['ROLE_ADMIN', 'ROLE_SUBSCRIBER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/cloudaccount/cloudaccount.html',
                        controller: 'CloudAccountController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('cloudaccount');
                        return $translate.refresh();
                    }]
                }
            });
    });
