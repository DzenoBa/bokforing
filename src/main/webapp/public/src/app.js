
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
    'UserService',
    'PromiseService',
    'BookkeepingControllers',
    'BookkeepingService'
     // More here
]);


bok.config(['$routeProvider',
    function($routeProvider) {  // Injected object $routeProvider
        $routeProvider.
                when('/login', {
                    templateUrl: 'login.html',
                    controller: 'LoginCtrl'
                }).
                when('/userpage', {
                    templateUrl: 'private/userpage.html',
                    controller: 'UserPageCtrl',
                    auth: true,
                    resolve: {
                        init: ['PromiseProxy', function(PromiseProxy) {
                                PromiseProxy.refresh();
                                return PromiseProxy.promise();
                        }]
                    }
                }).
                when('/register', {
                    templateUrl: 'register.html',
                    controller: 'RegisterCtrl'
                }).
                when('/edituser', {
                    templateUrl: 'private/edituser.html',
                    controller: 'EditUserCtrl',
                    auth: true,
                    resolve: {
                        init: ['PromiseProxy', function(PromiseProxy) {
                                PromiseProxy.refresh();
                                return PromiseProxy.promise();
                        }]
                    }
                }).
                when('/manbok', {
                    templateUrl: 'private/manbok.html',
                    controller: 'ManBKCtrl',
                    auth: true,
                    resolve: {
                        init: ['PromiseProxy', function(PromiseProxy) {
                                PromiseProxy.refresh();
                                return PromiseProxy.promise();
                        }]
                    }
                }).
                when('/userinfo', {
                    templateUrl: 'private/userinfo.html',
                    controller: 'UserInfoCtrl',
                    auth: true,
                    resolve: {
                        init: ['PromiseProxy', function(PromiseProxy) {
                                PromiseProxy.refresh();
                                return PromiseProxy.promise();
                        }]
                    }
                }).
                when('/lstverifications', {
                    templateUrl: 'private/lstverifications.html',
                    controller: 'LstVerCtrl',
                    auth: true,
                    resolve: {
                        init: ['PromiseProxy', function(PromiseProxy) {
                                PromiseProxy.refresh();
                                return PromiseProxy.promise();
                        }]
                    }
                }).
                otherwise({
                    redirectTo: 'index.html'
                });

    }]);

bok.run(function($rootScope, $location, $route, $q, AuthProxy, PromiseProxy) {
    $rootScope.$on('$routeChangeStart', function (ev, next, current) {
        function isOnline() {
            var dfrd = $q.defer();
            AuthProxy.status()
                    .success(function(boolean) {
                        dfrd.resolve(boolean);
                    }).error(function() {
                        console.log("isOnline:error");
                    });
            return dfrd.promise;
        };

        isOnline().then(function(value) {
            AuthProxy.setOnline(value); // Set status
            var nextPath = $location.path();
            var nextRoute = $route.routes[nextPath];
            if(nextRoute && nextRoute.auth && !value) {
                // USER NOT ONLINE
                $location.path("/login");
            } else if (value) {
                // ENTER HERE ONLY IF YOU'RE ONLINE
                PromiseProxy.resolve();
            }
        });
    });
});


