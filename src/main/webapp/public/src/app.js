'use strict';

/* 
 *  The Bokforing App
 */
var bok = angular.module('Bok', [
    'ngRoute',
    'AuthControllers',
    'AuthService'
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
                    controller: 'UserPageCtrl'
                }).
                otherwise({
                    redirectTo: 'index.html'
                });

    }]);


