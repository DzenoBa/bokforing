'use strict';

/* 
 *  The Bokforing App
 */
var bok = angular.module('Bok', [
    'ngRoute',
    'AuthControllers',
    'AuthService',
    'UserControllers',
    'DDService'
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
                when('/dd', {
                    templateUrl: 'dd.html',
                    controller: 'DDCtrl'
                }).
                when('/edituser', {
                    templateUrl: 'private/edituser.html',
                    controller: 'EditUserCtrl'
                }).
                otherwise({
                    redirectTo: 'index.html'
                });

    }]);


