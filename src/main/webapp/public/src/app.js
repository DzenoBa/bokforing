
/**
 * BOKFORING APP
 * 
 * @author Dženan
 */

'use strict';

var bok = angular.module('Bok', [
    'ngRoute',
    'AuthControllers',
    'AuthService',
    'UserControllers',
    'DefaultDataControllers',
    'UserService',
    'AuthResolverService',
    'BookkeepingControllers',
    'BookkeepingService',
    'DefaultDataService'
     // More here
]);


bok.config(['$routeProvider', 'USER_LEVELS',
    function($routeProvider, USER_LEVELS) {  // Injected object $routeProvider
        $routeProvider.
                when('/login', {
                    templateUrl: 'login.html',
                    controller: 'LoginCtrl',
                    auth: USER_LEVELS.guest
                }).
                when('/defaultdata', {
                    templateUrl: 'defaultdata.html',
                    controller: 'DefaultDataCtrl',
                    auth: USER_LEVELS.all
                }).
                when('/userpage', {
                    templateUrl: 'private/userpage.html',
                    controller: 'UserPageCtrl',
                    auth: USER_LEVELS.user,
                    resolve: {
                        auth: ['AuthResolver', function(AuthResolver) {
                                return AuthResolver.promise();
                        }]
                    }
                }).
                when('/register', {
                    templateUrl: 'register.html',
                    controller: 'RegisterCtrl',
                    auth: USER_LEVELS.guest
                }).
                when('/passwdreset', {
                    templateUrl: 'passwdreset.html',
                    controller: 'PasswdResetCtrl',
                    auth: USER_LEVELS.guest
                }).
                when('/passwdrecovery', {
                    templateUrl: 'passwdrecovery.html',
                    controller: 'PasswdRecoveryCtrl',
                    auth: USER_LEVELS.guest
                }).
                when('/edituser', {
                    templateUrl: 'private/edituser.html',
                    controller: 'EditUserCtrl',
                    auth: USER_LEVELS.user,
                    resolve: {
                        auth: ['AuthResolver', function(AuthResolver) {
                                return AuthResolver.promise();
                        }]
                    }
                }).
                when('/manbok', {
                    templateUrl: 'private/manbok.html',
                    controller: 'ManBKCtrl',
                    auth: USER_LEVELS.user,
                    resolve: {
                        auth: ['AuthResolver', function(AuthResolver) {
                                return AuthResolver.promise();
                        }]
                    }
                }).
                when('/userinfo', {
                    templateUrl: 'private/userinfo.html',
                    controller: 'UserInfoCtrl',
                    auth: USER_LEVELS.user,
                    resolve: {
                        auth: ['AuthResolver', function(AuthResolver) {
                                return AuthResolver.promise();
                        }]
                    }
                }).
                when('/verifications', {
                    templateUrl: 'private/verifications.html',
                    controller: 'LstVerCtrl',
                    auth: USER_LEVELS.user,
                    resolve: {
                        auth: ['AuthResolver', function(AuthResolver) {
                                return AuthResolver.promise();
                        }]
                    }
                }).
                otherwise({
                    redirectTo: 'index.html'
                });

    }]);

bok.run(function($rootScope, $location, $route, AuthProxy, AuthResolver) {
    $rootScope.$on('$routeChangeStart', function (ev, next, current) {

        var nextPath = $location.path();
        var nextRoute = $route.routes[nextPath];
        
        // CHECK IF THE SESSION IS DEFINED
        // IF NOT LOAD SESSION FROM SERVER
        if(!angular.isDefined(AuthProxy.class().getSession().level)) {
            AuthProxy.class().getAuthentication().then(function() {
                // CHECK IF AUTHORIZED
                if(nextRoute && !AuthProxy.class().isAuthorized(nextRoute.auth)) {
                    AuthResolver.redirect(AuthProxy.class().getSession().level);
                }
            });
        } // CHECK IF AUTHORIZED 
        else if(nextRoute && !AuthProxy.class().isAuthorized(nextRoute.auth)) {
            AuthResolver.redirect(AuthProxy.class().getSession().level);
        }
    });
});


