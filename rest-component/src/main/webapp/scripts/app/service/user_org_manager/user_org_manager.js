'use strict';

angular.module('cloudoptingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('org_manager', {
                parent: 'service',
                url: '/org_manager',
                data: {
                    roles: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/user_org_manager/org_manager.html',
                        controller: 'UserOrgManagerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user_org_manager');
                        $translatePartialLoader.addPart('settings');
                        return $translate.refresh();
                    }]
                }
            })
            .state('user_manager', {
                parent: 'service',
                url: '/user_manager',
                data: {
                    roles: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/user_org_manager/user_manager.html',
                        controller: 'UserOrgManagerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user_org_manager');
                        $translatePartialLoader.addPart('settings');
                        return $translate.refresh();
                    }]
                }
            })
            .state('user_detail_manager', {
                parent: 'service',
                url: '/user_detail_manager',
                data: {
                    roles: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/user_org_manager/user_detail.html',
                        controller: 'UserOrgManagerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user_org_manager');
                        $translatePartialLoader.addPart('settings');
                        return $translate.refresh();
                    }]
                }
            })
            .state('org_detail_manager', {
                parent: 'service',
                url: '/org_detail_manager',
                data: {
                    roles: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/service/user_org_manager/org_detail.html',
                        controller: 'UserOrgManagerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user_org_manager');
                        $translatePartialLoader.addPart('settings');
                        return $translate.refresh();
                    }]
                }
            });
    });
