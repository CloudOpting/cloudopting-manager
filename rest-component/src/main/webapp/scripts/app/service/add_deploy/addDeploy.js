'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('addDeploy', {
                parent: 'service',
                url: '/addDeploy',
                data: {
                    roles: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/add_deploy/addDeploy.html',
                        controller: 'AddDeployController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('addDeploy');
                        return $translate.refresh();
                    }]
                }
            });
    });
