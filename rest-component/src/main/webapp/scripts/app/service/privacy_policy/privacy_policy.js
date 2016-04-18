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
                        templateUrl: 'scripts/app/service/privacy_policy/privacy_policy.html',
                        controller: 'PrivacyPolicyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('privacy_policy');
                        return $translate.refresh();
                    }]
                }
            });
    });
