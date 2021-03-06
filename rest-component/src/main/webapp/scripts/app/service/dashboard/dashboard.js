'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dashboard', {
                parent: 'service',
                url: '/dashboard',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/dashboard/dashboard.html',
                        controller: 'DashboardController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dashboard');
                        $translatePartialLoader.addPart('callback');
                        return $translate.refresh();
                    }]
                }
            });
    });
