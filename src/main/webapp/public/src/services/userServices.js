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
                return $http.post(url + '/create', user);
            },
            edit: function(user) {
                return $http.post(url + '/edit', user);
            },
            getUserInfo: function() {
                return $http.get(url + '/getuserinfo');
            },
            editUserInfo: function(userInfo) {
                return $http.put(url + '/edituserinfo', userInfo);
            },
            passwdReset: function(user) {
                return $http.post(url + '/passwdreset', user);
            }
        };
    }]);
