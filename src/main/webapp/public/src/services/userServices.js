'use strict';

/* 
 * Services 
 * 
 * @author Dženan
 */

var userService = angular.module('UserService', []);

userService.factory('UserProxy', ['$http',
    function($http) {
       var url = 'http://localhost:8080/bokforing/user';
        return {
            set: function() {
                return $http.get(url + '/set');
            },
            edit: function(user) {
                return $http.post(url + '/edit', user);
            }
        };
    }]);
