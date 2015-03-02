
/**
 * AUTH SERVICES
 * 
 * @author Dženan
 */

'use strict';

var authService = angular.module('AuthService', []);

authService.factory('AuthProxy', ['$http',
    function($http) {
        var _isOnline = false;
        var url = 'http://localhost:8080/bokforing/auth';
        return {
            login: function(user) {
                return $http.post(url + '/login', user);
            },
            status: function() {
                return $http.get(url + '/status');
            },
            get: function() {
                return $http.get(url + '/get');
            },
            logout: function() {
                return $http.get(url + '/logout');
            },
            isOnline: function() {
                return _isOnline;
            },
            setOnline: function(boolean) {
                _isOnline = boolean;
            }
        };
    }]);


