'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('subscriber', {
                parent: 'service',
                url: '/subscriber',
                data: {
                    roles: ['ROLE_ADMIN', 'ROLE_SUBSCRIBER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/subscriber/subscriber.html',
                        controller: 'SubscriberController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('subscriber');
                        return $translate.refresh();
                    }]
                }
            });
    });
