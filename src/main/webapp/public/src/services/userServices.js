'use strict';

/* 
 * Services 
 * 
 * @author Dženan
 */

var userService = angular.module('UserService', []);

userService.factory('UserProxy', ['$http', '$location',
    function($http, $location) {
       //var url = '//localhost:'+$location.port()+'/bokforing/user';
       var url = '//'+window.location.hostname+':'+$location.port()+'/bokforing/auth';
        return {
            create: function(user) {
                return $http.post(url + '/create', user);
            },
            editPassword: function(user) {
                return $http.post(url + '/edit', user);
            },
            editEmail: function(user) {
                return $http.post(url + '/editemail', user);
            },
            getUserInfo: function() {
                return $http.get(url + '/getuserinfo');
            },
            editUserInfo: function(userInfo) {
                return $http.put(url + '/edituserinfo', userInfo);
            },
            passwdReset: function(user) {
                return $http.post(url + '/passwdreset', user);
            },
            keyExist: function(user) {
                return $http.post(url + '/keyexist', user);
            },
            passwdRecovery: function(user) {
                return $http.post(url + '/passwdrecovery', user);
            }
        };
    }]);
