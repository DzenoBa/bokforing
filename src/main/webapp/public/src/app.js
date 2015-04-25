
/**
 * BOKFORING APP
 * 
 * @author Dženan
 */

'use strict';

var bok = angular.module('Bok', [
    'ngRoute',
    'IndexControllers',
    'AuthControllers',
    'AuthService',
    'UserControllers',
    'DefaultDataControllers',
    'ProductControllers',
    'CustomerControllers',
    'TimesheetControllers',
    'UserService',
    'AuthHandlerService',
    'BookkeepingControllers',
    'BookkeepingService',
    'DefaultDataService',
    'ProductService',
    'CustomerService',
    'StatisticsService',
    'TimesheetService'
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
                        auth: ['AuthHandler', function(AuthHandler) {
                                return AuthHandler.promise();
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
                        auth: ['AuthHandler', function(AuthHandler) {
                                return AuthHandler.promise();
                        }]
                    }
                }).
                when('/bookkeeping', {
                    templateUrl: 'private/bookkeeping.html',
                    controller: 'BookkeepingCtrl',
                    auth: USER_LEVELS.user,
                    resolve: {
                        auth: ['AuthHandler', function(AuthHandler) {
                                return AuthHandler.promise();
                        }]
                    }
                }).
                when('/fastbookkeeping', {
                    templateUrl: 'private/fastbookkeeping.html',
                    controller: 'FastbookkeepingCtrl',
                    auth: USER_LEVELS.user,
                    resolve: {
                        auth: ['AuthHandler', function(AuthHandler) {
                                return AuthHandler.promise();
                        }]
                    }
                }).
                when('/userinfo', {
                    templateUrl: 'private/userinfo.html',
                    controller: 'UserInfoCtrl',
                    auth: USER_LEVELS.user,
                    resolve: {
                        auth: ['AuthHandler', function(AuthHandler) {
                                return AuthHandler.promise();
                        }]
                    }
                }).
                when('/verifications', {
                    templateUrl: 'private/verifications.html',
                    controller: 'VerificationCtrl',
                    auth: USER_LEVELS.user,
                    resolve: {
                        auth: ['AuthHandler', function(AuthHandler) {
                                return AuthHandler.promise();
                        }]
                    }
                }).
                when('/products', {
                    templateUrl: 'private/products.html',
                    controller: 'ProductCtrl',
                    auth: USER_LEVELS.user,
                    resolve: {
                        auth: ['AuthHandler', function(AuthHandler) {
                                return AuthHandler.promise();
                        }]
                    }
                }).
                when('/customers', {
                    templateUrl: 'private/customers.html',
                    controller: 'CustomerCtrl',
                    auth: USER_LEVELS.user,
                    resolve: {
                        auth: ['AuthHandler', function(AuthHandler) {
                                return AuthHandler.promise();
                        }]
                    }
                }).
                when('/timesheets', {
                    templateUrl: 'private/timesheets.html',
                    controller: 'TimesheetCtrl',
                    auth: USER_LEVELS.user,
                    resolve: {
                        auth: ['AuthHandler', function(AuthHandler) {
                                return AuthHandler.promise();
                        }]
                    }
                }).
                otherwise({
                    redirectTo: 'index.html'
                });

    }]);

bok.run(function($rootScope, $location, $route, AuthProxy, AuthHandler) {
    $rootScope.$on('$routeChangeStart', function (ev, next, current) {

        var nextPath = $location.path();
        var nextRoute = $route.routes[nextPath];
        
        // CHECK IF THE SESSION IS DEFINED
        // IF NOT LOAD SESSION FROM SERVER
        if(!angular.isDefined(AuthProxy.class().getSession().level)) {
            AuthProxy.class().getAuthentication().then(function() {
                // CHECK IF AUTHORIZED
                if(nextRoute && !AuthProxy.class().isAuthorized(nextRoute.auth)) {
                    AuthHandler.redirect(AuthProxy.class().getSession().level);
                }
            });
        } // CHECK IF AUTHORIZED 
        else if(nextRoute && !AuthProxy.class().isAuthorized(nextRoute.auth)) {
            AuthHandler.redirect(AuthProxy.class().getSession().level);
        }
    });
});


