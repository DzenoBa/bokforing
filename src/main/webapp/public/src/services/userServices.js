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
            create: function(user) {
                return $http.get(url + '/create');
            },
            edit: function(user) {
                return $http.post(url + '/edit', user);
            }
        };
    }]);
