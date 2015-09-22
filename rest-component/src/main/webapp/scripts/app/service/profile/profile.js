'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('profile', {
                parent: 'service',
                url: '/profile',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/profile/profile.html',
                        controller: 'ProfileController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('profile');
                        $translatePartialLoader.addPart('settings');
                        $translatePartialLoader.addPart('password');
                        return $translate.refresh();
                    }]
                }
            });
    });
